package com.example.jmptoboot.controller;

import com.example.jmptoboot.dto.AnswerFormVo;
import com.example.jmptoboot.dto.QuestionFormVo;
import com.example.jmptoboot.entity.Member;
import com.example.jmptoboot.entity.Question;
import com.example.jmptoboot.service.MemberService;
import com.example.jmptoboot.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/question")
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;

    @GetMapping(value ="/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Question> questionList = questionService.list(model, page, kw);
        model.addAttribute("questionList", questionList);
        model.addAttribute("kw", kw);
        return "question_list";
    }

    @GetMapping(value ="/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerFormVo answerFormVo) {
       Question questionDetail = questionService.getQuestion(id);
       model.addAttribute("question", questionDetail);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionFormVo request) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Validated QuestionFormVo questionFormVo, BindingResult bindingResult, Principal principal ) {
        Member member = memberService.getUser(principal.getName());
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        questionService.create(questionFormVo, member);
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionFormVo questionFormVo, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionFormVo.setSubject(question.getSubject());
        questionFormVo.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionFormVo questionFormVo, BindingResult bindingResult, Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        this.questionService.modify(question, questionFormVo.getSubject(), questionFormVo.getContent());

        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        Member siteUser = this.memberService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }

}
