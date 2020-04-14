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

    @Column(name = "type")
    private String type;

    @Column(name = "pic_bytes", length = 1000)
    private byte[] picBytes;

    public ImageModel() {
    }

    public ImageModel(Long userId, String name, String type, byte[] pic) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.picBytes = pic;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public byte[] getPicBytes() {
        return picBytes;
    }

    public Long getUserId() {
        return userId;
    }
}
