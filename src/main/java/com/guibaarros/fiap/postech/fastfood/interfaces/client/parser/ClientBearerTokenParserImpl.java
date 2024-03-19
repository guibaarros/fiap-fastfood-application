package com.guibaarros.fiap.postech.fastfood.interfaces.client.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Objects;

@Component
@AllArgsConstructor
public class ClientBearerTokenParserImpl implements ClientBearerTokenParser {

    private ObjectMapper objectMapper;

    @Override
    public String parseTokenToSub(final String token) {
        final String validToken = token.replace("Bearer ", "");
        final Base64.Decoder decoder = Base64.getUrlDecoder();
        final String[] splitToken = validToken.split("\\.");
        if (splitToken.length >= 2) {
            final String rawBearerTokenDetailDTO = new String(decoder.decode(splitToken[1]));
            try {
                final BearerTokenDTO bearerTokenDTO = objectMapper.readValue(rawBearerTokenDetailDTO, BearerTokenDTO.class);
                if (Objects.nonNull(bearerTokenDTO.getSub())) {
                    return bearerTokenDTO.getSub();
                }
                throw new InvalidBearerTokenException();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        throw new InvalidBearerTokenException();
    }
}
