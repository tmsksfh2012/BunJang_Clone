package com.example.demo.src.sms.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSMSRes {
    private String requestId;
    private LocalDateTime requestTime;
    private String statusCode;
    private String statusName;
}
