package com.guibaarros.fiap.postech.fastfood.domain.entities.client;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity(name = "tb_client")
@Getter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @Column
    private Long cpf;

    @Column
    private String name;

    @Column
    private String email;

    @Column(nullable = false)
    private LocalDate lastVisit;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        this.lastVisit = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        this.createdAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

    public Client(final Long cpf, final String name, final String email) {
        this.cpf = cpf;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
