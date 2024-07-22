package com.icetea.project.MonStu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Entity
@ToString(exclude = "content")
@Table(name="my_sentence")
public class MySentence {

    @Id
    @Column(name="my_sentence_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mySentenceId;

    @Column(name="sentence")
    private String sentence;

    @Column(name="type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    public void setContent(Content content) {
        this.content = content;
        if(!content.getMySentences().contains(this)) content.getMySentences().add(this);
    }


}
