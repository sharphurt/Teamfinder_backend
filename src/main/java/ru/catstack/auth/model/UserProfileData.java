package ru.catstack.auth.model;

import javax.persistence.*;

@Table(name = "user_profile_data")
@Entity
public class UserProfileData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true)
    private Long userId;

    @Column(name = "about_me", unique = true)
    private String aboutMe;

    public UserProfileData() {
    }

    public UserProfileData(Long userId, String aboutMe) {
        this.userId = userId;
        this.aboutMe = aboutMe;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAboutMe() {
        return aboutMe;
    }
}
