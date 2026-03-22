package com.harmony.harmonyaicodeservice.utils;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.harmony.harmonyaicodeservice.exception.BusinessException;
import com.harmony.harmonyaicodeservice.exception.ErrorCode;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 截图工具类
 * <p>
 * 使用线程池和 WebDriver 池管理，支持并发截图
 */
@Slf4j
public class WebScreenshotUtil {

    // 线程池大小
    private static final int THREAD_POOL_SIZE = 5;
    // WebDriver 池大小
    private static final int DRIVER_POOL_SIZE = 5;
    // 线程池
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    // WebDriver 池
    private static final BlockingQueue<WebDriver> driverPool = new LinkedBlockingQueue<>(DRIVER_POOL_SIZE);

    static {
        // 初始化 WebDriver 池
        for (int i = 0; i < DRIVER_POOL_SIZE; i++) {
            try {
                WebDriver driver = initChromeDriver(1600, 900);
                driverPool.offer(driver);
                log.info("初始化 WebDriver 实例 {} 成功", i + 1);
            } catch (Exception e) {
                log.error("初始化 WebDriver 实例失败", e);
            }
        }

        // 注册 JVM 关闭钩子，确保资源正确释放
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdown();
        }));
    }

    /**
     * 初始化 Chrome 浏览器驱动
     */
    private static WebDriver initChromeDriver(int width, int height) {
        try {
            // 自动管理 ChromeDriver
            WebDriverManager.chromedriver().setup();
            // 配置 Chrome 选项
            ChromeOptions options = getChromeOptions(width, height);
            // 创建驱动
            WebDriver driver = new ChromeDriver(options);
            // 设置页面加载超时
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            // 设置隐式等待
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return driver;
        } catch (Exception e) {
            log.error("初始化 Chrome 浏览器失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "初始化 Chrome 浏览器失败");
        }
    }

    /**
     * 生成网页截图（同步）
     *
     * @param webUrl 网页URL
     * @return 压缩后的截图文件路径，失败返回null
     */
    public static String saveWebPageScreenshot(String webUrl) {
        if (StrUtil.isBlank(webUrl)) {
            log.error("网页URL不能为空");
            return null;
        }

        WebDriver driver = null;
        try {
            // 从池获取 WebDriver
            driver = getWebDriver();
            if (driver == null) {
                log.error("无法获取 WebDriver 实例");
                return null;
            }

            // 创建临时目录
            String rootPath = System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "screenshots"
                    + File.separator + UUID.randomUUID().toString().substring(0, 8);
            FileUtil.mkdir(rootPath);
            // 原始截图文件路径
            String imageSavePath = rootPath + File.separator + RandomUtil.randomNumbers(5) + ".png";
            // 访问网页
            driver.get(webUrl);
            // 等待页面加载完成
            waitForPageLoad(driver);
            // 截图
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            // 保存原始图片
            saveImage(screenshotBytes, imageSavePath);
            log.info("原始截图保存成功: {}", imageSavePath);

            // 压缩图片
            String compressedImagePath = rootPath + File.separator + RandomUtil.randomNumbers(5) + "_compressed.jpg";
            compressImage(imageSavePath, compressedImagePath);
            log.info("压缩图片保存成功: {}", compressedImagePath);

            // 删除原始图片，只保留压缩图片
            FileUtil.del(imageSavePath);
            return compressedImagePath;
        } catch (Exception e) {
            log.error("网页截图失败: {}", webUrl, e);
            return null;
        } finally {
            // 归还 WebDriver 到池
            if (driver != null) {
                returnWebDriver(driver);
            }
        }
    }

    /**
     * 生成网页截图（异步）
     *
     * @param webUrl 网页URL
     * @return CompletableFuture，包含截图文件路径
     */
    public static CompletableFuture<String> saveWebPageScreenshotAsync(String webUrl) {
        return CompletableFuture.supplyAsync(() -> saveWebPageScreenshot(webUrl), executorService);
    }

    /**
     * 从池获取 WebDriver 实例
     */
    private static WebDriver getWebDriver() {
        try {
            // 最多等待10秒获取 WebDriver
            return driverPool.poll(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("获取 WebDriver 实例被中断", e);
            return null;
        }
    }

    /**
     * 归还 WebDriver 实例到池
     */
    private static void returnWebDriver(WebDriver driver) {
        if (driver != null) {
            try {
                // 重置 WebDriver 状态
                driver.manage().deleteAllCookies();
                driver.navigate().to("about:blank");
                driverPool.offer(driver);
            } catch (Exception e) {
                log.error("归还 WebDriver 实例失败", e);
                // 销毁损坏的 WebDriver
                try {
                    driver.quit();
                } catch (Exception ex) {
                    log.error("销毁 WebDriver 实例失败", ex);
                }
                // 重新创建 WebDriver 实例并加入池
                try {
                    WebDriver newDriver = initChromeDriver(1600, 900);
                    driverPool.offer(newDriver);
                    log.info("重新创建并添加 WebDriver 实例到池");
                } catch (Exception ex) {
                    log.error("重新创建 WebDriver 实例失败", ex);
                }
            }
        }
    }

    /**
     * 关闭所有资源
     */
    public static void shutdown() {
        log.info("开始关闭 WebScreenshotUtil 资源");
        
        // 关闭所有 WebDriver 实例
        while (!driverPool.isEmpty()) {
            WebDriver driver = driverPool.poll();
            if (driver != null) {
                try {
                    driver.quit();
                    log.info("关闭 WebDriver 实例成功");
                } catch (Exception e) {
                    log.error("关闭 WebDriver 实例失败", e);
                }
            }
        }
        
        // 关闭线程池
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
            log.info("关闭线程池成功");
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            log.error("关闭线程池被中断", e);
        }
        
        log.info("WebScreenshotUtil 资源关闭完成");
    }

    /**
     * 获取 Chrome 浏览器选项
     */
    private static ChromeOptions getChromeOptions(int width, int height) {
        ChromeOptions options = new ChromeOptions();
        // 无头模式
        options.addArguments("--headless");
        // 禁用GPU（在某些环境下避免问题）
        options.addArguments("--disable-gpu");
        // 禁用沙盒模式（Docker环境需要）
        options.addArguments("--no-sandbox");
        // 禁用开发者shm使用
        options.addArguments("--disable-dev-shm-usage");
        // 设置窗口大小
        options.addArguments(String.format("--window-size=%d,%d", width, height));
        // 禁用扩展
        options.addArguments("--disable-extensions");
        // 设置用户代理
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        return options;
    }

    /**
     * 保存图片到文件
     */
    private static void saveImage(byte[] imageBytes, String imagePath) {
        try {
            FileUtil.writeBytes(imageBytes, imagePath);
        } catch (Exception e) {
            log.error("保存图片失败: {}", imagePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存图片失败");
        }
    }

    /**
     * 压缩图片
     */
    private static void compressImage(String originalImagePath, String compressedImagePath) {
        // 压缩图片质量（0.1 = 10% 质量）
        final float COMPRESSION_QUALITY = 0.3f;
        try {
            /**
             * 压缩图片
             * @param imageFile 源图片文件
             * @param outFile 目标图片文件
             * @param quality 压缩质量
             */
            ImgUtil.compress(
                    FileUtil.file(originalImagePath),
                    FileUtil.file(compressedImagePath),
                    COMPRESSION_QUALITY
            );
        } catch (Exception e) {
            log.error("压缩图片失败: {} -> {}", originalImagePath, compressedImagePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "压缩图片失败");
        }
    }

    /**
     * 等待页面加载完成
     */
    private static void waitForPageLoad(WebDriver driver) {
        try {
            // 创建等待页面加载对象
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // 等待 document.readyState 为complete
            wait.until(webDriver ->
                    ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
                            .equals("complete")
            );
            // 额外等待一段时间，确保动态内容加载完成
            Thread.sleep(2000);
            log.info("页面加载完成");
        } catch (Exception e) {
            log.error("等待页面加载时出现异常，继续执行截图", e);
        }
    }
}