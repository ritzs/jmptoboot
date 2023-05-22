package com.example.jmptoboot.service;

import com.example.jmptoboot.dto.QuestionFormVo;
import com.example.jmptoboot.entity.Answer;
import com.example.jmptoboot.entity.Member;
import com.example.jmptoboot.entity.Question;
import com.example.jmptoboot.exception.DataNotFoundException;
import com.example.jmptoboot.repository.QuestionRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Page<Question> list(Model model, Integer page, String kw){
        Page<Question> paging = this.getList(page, kw);
        model.addAttribute("paging", paging);
        return paging;
    }

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
    }

    @Transactional
    public Question getQuestion(Integer id) {
        Optional<Question> question = questionRepository.findById(id);
        return question.orElseThrow(() -> new DataNotFoundException("게시물 없음"));
    }

    public void create(QuestionFormVo questionFormVo, Member author) {
        Question question = Question.builder()
                .subject(questionFormVo.getSubject())
                .content(questionFormVo.getContent())
                .createDate(LocalDateTime.now())
                .author(author)
                .build();
        questionRepository.save(question);
    }

    public void modify(Question question, String subject, String content) {
        Question modifiedQuestion = question.toBuilder()
                .subject(subject)
                .content(content)
                .build();
        questionRepository.save(modifiedQuestion);
    }

    public void delete(Question question) {
        questionRepository.delete(question);
    }

    public void vote(Question question, Member user) {
        question.getVoter().add(user);
        this.questionRepository.save(question);
    }

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, Member> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, Member> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

}
