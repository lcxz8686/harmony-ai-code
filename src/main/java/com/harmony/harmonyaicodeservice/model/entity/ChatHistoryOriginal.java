package com.harmony.harmonyaicodeservice.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("chat_history_original")
public class ChatHistoryOriginal implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    @Column("message")
    private String message;

    @Column("messageType")
    private String messageType;

    @Column("appId")
    private Long appId;

    @Column("userId")
    private Long userId;

    @Column("createTime")
    private Date createTime;

    @Column("updateTime")
    private Date updateTime;

    @Column("isDelete")
    private Integer isDelete;
}
