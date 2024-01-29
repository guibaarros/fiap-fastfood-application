package com.guibaarros.fiap.postech.fastfood.application.dtos.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientResponseDTO {

    private Long id;
    private Long cpf;
    private String name;
    private String email;
    private LocalDate lastVisit;
    private LocalDateTime createdAt;

}
