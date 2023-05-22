package com.example.jmptoboot.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuestionFormVo {

    @NotEmpty(message = "제목을 입력하세요")
    @Size(max = 200, message = "제목은 200글자를 초과 할 수 없습니다. ")
    private String subject;

    @NotEmpty(message = "내용을 입력하세요")
    private String content;
}
