package com.guibaarros.fiap.postech.fastfood.interfaces.client.identification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClientIdentificationImpl implements ClientIdentificationPort {

    @Value("${client.identification.url}")
    private String clientIdentificationUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ClientIdentificationResponseDTO identifyClient(final String username) {
        final String fullUrl = clientIdentificationUrl + "/" + username;

        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(fullUrl, String.class);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            try {
                return objectMapper.readValue(
                        responseEntity.getBody(), ClientIdentificationResponseDTO.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        throw new ClientIdentificationNotFoundException(username);
    }
}
