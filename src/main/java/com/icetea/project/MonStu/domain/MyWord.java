package com.icetea.project.MonStu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Entity
@ToString(exclude = "content")
@Table(name="my_word")
public class MyWord {

    @Id
    @Column(name="my_word_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myWordId;

    @Column(name="word")
    private String word;

    @Column(name="type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    public void setContent(Content content) {
        this.content = content;
        if(!content.getMyWords().contains(this)) content.getMyWords().add(this);
    }
}
