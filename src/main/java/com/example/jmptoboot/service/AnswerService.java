package com.example.jmptoboot.service;

import com.example.jmptoboot.dto.AnswerFormVo;
import com.example.jmptoboot.entity.Answer;
import com.example.jmptoboot.entity.Member;
import com.example.jmptoboot.entity.Question;
import com.example.jmptoboot.exception.DataNotFoundException;
import com.example.jmptoboot.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer create(Question question, AnswerFormVo answerFormVo, Member author){
        Answer answer = Answer.builder()
                .content(answerFormVo.getContent())
                .createDate(LocalDateTime.now())
                .question(question)
                .author(author)
                .build();
        //answerRepository.save(answer);
        return answerRepository.save(answer);
    }

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        return answer.orElseThrow(()->new DataNotFoundException("게시물 없음"));
    }

    public void modify(Answer answer, String content) {
        Answer modifyAnswer = answer.toBuilder()
                .content(content)
                .build();
        this.answerRepository.save(modifyAnswer);
    }

    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

    public void vote(Answer answer, Member siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }
}
