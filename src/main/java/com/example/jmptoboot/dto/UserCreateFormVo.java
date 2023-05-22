package com.example.jmptoboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateFormVo {

    @Size(min = 4, max = 25, message = "사용자ID 는 최소 4글자, 최대 25자 까지 입니다.")
    @NotEmpty(message = "사용자 ID를 입력 하세요")
    private String username;

    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password1;

    @NotEmpty(message = "비밀번호 재확인을 입력하세요")
    private String password2;

    @NotEmpty(message = "이메일 주소를 입력하세요")
    @Email
    private String email;

}

