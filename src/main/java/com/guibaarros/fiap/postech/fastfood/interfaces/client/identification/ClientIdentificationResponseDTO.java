package com.guibaarros.fiap.postech.fastfood.interfaces.client.identification;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientIdentificationResponseDTO {
    private String sub;
    private String cpf;
    private String email;
    private String name;
}
