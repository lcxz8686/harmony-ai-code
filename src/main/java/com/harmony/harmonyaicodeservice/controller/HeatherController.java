package com.harmony.harmonyaicodeservice.controller;

import com.harmony.harmonyaicodeservice.common.BaseResponse;
import com.harmony.harmonyaicodeservice.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeatherController {

    @GetMapping("/heather")
    public BaseResponse<String> getHeather(){
        return ResultUtils.success("ok");
    }
}
