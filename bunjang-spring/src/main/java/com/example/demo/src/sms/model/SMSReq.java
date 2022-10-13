package com.example.demo.src.sms.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SMSReq {
    private String type;
    private String contentType;
    private String countryCode;
    private String from;
    private String content;
    private List<MessageReq> messages;
}
