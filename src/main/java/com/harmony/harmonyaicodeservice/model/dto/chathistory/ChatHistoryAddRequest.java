package com.harmony.harmonyaicodeservice.model.dto.chathistory;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatHistoryAddRequest implements Serializable {

    private String message;

    private String messageType;

    private Long appId;

    private static final long serialVersionUID = 1L;
}
