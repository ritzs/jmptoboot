package com.example.jmptoboot.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AnswerFormVo {

    @NotEmpty(message = "답변 내용을 입력하세요")
    @Size(max = 1000, message = "적당히 쑤셔 넣라.")
    private String content;
}
