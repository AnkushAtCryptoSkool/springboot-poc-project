package com.ankush.poc.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.Temporal;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@Entity
@SuperBuilder
@AllArgsConstructor
@Table(name  = "confirmation")
public class Confirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreatedDate
    private LocalDateTime createdOn;

    private String token;
    @OneToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(nullable = false,name = "user_id")
    private User user;

    public Confirmation(User user) {
        this.user = user;
        this.createdOn = LocalDateTime.now();
        this.token = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Confirmation{" +
                "id=" + id +
                ", createdOn=" + createdOn +
                ", token='" + token + '\'' +
                ", user=" + user +
                '}';
    }


}
