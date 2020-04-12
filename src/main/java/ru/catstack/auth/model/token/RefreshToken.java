package ru.catstack.auth.model.token;

import org.hibernate.annotations.NaturalId;
import ru.catstack.auth.model.Session;
import ru.catstack.auth.model.audit.DateAudit;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "token")
public class RefreshToken extends DateAudit {

    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    @NaturalId(mutable = true)
    private String token;

    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="session_id")
    private Session session;

    @Column(name = "refresh_count")
    private Long refreshCount;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate = Instant.now();

    public RefreshToken() {
    }

    public RefreshToken(String token, Session userSession, Long refreshCount, Instant expiryDate) {
        this.token = token;
        this.session = userSession;
        this.refreshCount = refreshCount;
        this.expiryDate = expiryDate;
    }

    public void incrementRefreshCount() {
        refreshCount = refreshCount + 1;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Session getUserSession() {
        return session;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public Long getRefreshCount() {
        return refreshCount;
    }
}
