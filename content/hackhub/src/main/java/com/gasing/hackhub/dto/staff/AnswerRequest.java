package com.gasing.hackhub.dto.staff;

import lombok.Data;

@Data
public class AnswerRequest {
    private Long requestId;    // ID della richiesta di supporto
    private Long mentorId;     // Chi risponde
    private String callLink;   // Il link Meet/Zoom
}