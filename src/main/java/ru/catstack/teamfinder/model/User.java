package ru.catstack.teamfinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.context.annotation.Role;
import ru.catstack.teamfinder.model.audit.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User extends DateAudit {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private Byte age;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "about_me")
    private String aboutMe = "";

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role = RoleEnum.ROLE_USER;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;

    public User() {
    }

    public User(String email, String password, String username, String firstName,
                String lastName, Byte age, RoleEnum role, Status status) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.role = role;
        this.status = status;
    }

    public User(Long id, String email, String password, String username, String firstName,
                String lastName, Byte age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public RoleEnum getRoles() {
        return role;
    }

    public Status getStatus() {
        return status;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Byte getAge() {
        return age;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
