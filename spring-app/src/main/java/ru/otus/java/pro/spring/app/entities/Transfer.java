package ru.otus.java.pro.spring.app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "transfers")
public class Transfer {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "client_id", updatable = false, insertable = false)
    private Client client;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "target_client_id")
    private String targetClientId;

    @Column(name = "source_account")
    private String sourceAccount;

    @Column(name = "target_account")
    private String targetAccount;

    @Column(name = "message")
    private String message;

    @Column(name = "amount")
    private int amount;

    @Transient
    private Direction direction;

    public enum Direction {
        IN, OUT, INNER
    }
}
