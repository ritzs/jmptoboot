package com.example.jmptoboot;

import com.example.jmptoboot.dto.QuestionFormVo;
import com.example.jmptoboot.entity.Question;
import com.example.jmptoboot.repository.QuestionRepository;
import com.example.jmptoboot.service.QuestionService;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.DisplayName.class)
class JmptobootApplicationTests {

    @Value("${local.string.jaspty.key}")
    String privateKey;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionService questionService;

    @DisplayName("1. DB에 테스트 데이터 넣기")
    @Test
    void testJpaInsert() {
        Question q1 = Question.builder()
                .subject("sbb가 무엇인가요?")
                .content("sbb에 대해서 알고 싶습니다.")
                .createDate(LocalDateTime.now())
                .build();
        this.questionRepository.save(q1);  // 첫번째 질문 저장
        System.out.printf("첫번째 데이터 : %s, %s, %s%n", q1.getSubject(), q1.getContent(), q1.getCreateDate().toString());

        Question q2 = Question.builder()
                .subject("스프링부트 모델 질문입니다.")
                .content("id는 자동으로 생성 되나요?")
                .createDate(LocalDateTime.now())
                .build();
        this.questionRepository.save(q2);  // 두번째 질문 저장
        System.out.printf("두번째 데이터 : %s, %s, %s%n", q1.getSubject(), q1.getContent(), q1.getCreateDate().toString());
    }

    @DisplayName("2. DB의 데이터 읽기")
    @Test
    void testJpaRead() {
        List<Question> questionList = this.questionRepository.findAll();
        assertEquals(2, questionList.size());
        System.out.printf("조회된 데이터 수 : %d", questionList.size());

        Question q = questionList.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
        System.out.printf("첫번째 데이터늬 본문 : %s", questionList.get(0).getContent());

    }

    @DisplayName("3. 테스트 데이터 삭제")
    @Test
    void testJpaDelete() {
        this.questionRepository.deleteAll();
        List<Question> truncateList = this.questionRepository.findAll();
        assertEquals(0, truncateList.size());
        System.out.printf("테스트 데이터 삭제 후 DB에 저장된 rows 수 : %d", truncateList.size());
    }

    @DisplayName("4. 300개의 데이터 만들기 ")
    @Test
    void testJpa() {
        for (int i = 1; i <= 300; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            QuestionFormVo questionFormVo = new QuestionFormVo();
            questionFormVo.setSubject(subject);
            questionFormVo.setContent(content);
            this.questionService.create(questionFormVo, null);
        }
    }

    @DisplayName("5. Jasypt 데이터 생성")
    @Test
    void jasyptString(){
        String username = "boptop";
        String password = "soakadlek1!";

        System.out.println(jasyptEncoding(username));
        System.out.println(jasyptEncoding(password));
    }

    public String jasyptEncoding(String value) {

        String key = privateKey;
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.encrypt(value);
    }



}
