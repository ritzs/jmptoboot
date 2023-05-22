package com.example.jmptoboot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;


    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Answer> answerList;

    @ManyToOne
    private Member author;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Member> voter;

    @Builder
    public Question(Integer id, String subject, String content, LocalDateTime createDate, List<Answer> answerList, Member author, Set<Member> voter){
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.createDate = createDate;
        this.answerList = answerList;
        this.author = author;
        this.voter = voter;
    }
}
