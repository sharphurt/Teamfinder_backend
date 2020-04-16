package ru.catstack.auth.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "images")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "base64_code", length = 1000)
    private String base64Code;

    public ImageModel() {
    }

    public ImageModel(Long userId, String name, String base64Code) {
        this.userId = userId;
        this.name = name;
        this.base64Code = base64Code;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBase64Code() {
        return base64Code;
    }

    public Long getUserId() {
        return userId;
    }
}
