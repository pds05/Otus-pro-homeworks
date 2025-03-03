package ru.otus.java.pro.mt.core.transfers.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    @Id
    @Column(name = "id")
    private String id;

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
    private BigDecimal amount;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Transfer(String id, String clientId, String targetClientId, String sourceAccount, String targetAccount, String message, BigDecimal amount) {
        this.id = id;
        this.clientId = clientId;
        this.targetClientId = targetClientId;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.message = message;
        this.amount = amount;
    }
}
