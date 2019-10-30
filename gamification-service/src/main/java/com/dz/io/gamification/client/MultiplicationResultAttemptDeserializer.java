package com.dz.io.gamification.client;

import com.dz.io.gamification.client.dto.MultiplicationResultAttempt;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class MultiplicationResultAttemptDeserializer extends JsonDeserializer<MultiplicationResultAttempt> {
    @Override
    public MultiplicationResultAttempt deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode js = oc.readTree(jsonParser);
        return new MultiplicationResultAttempt(js.get("user").get("alias").asText(),
                js.get("multiplication").get("factorA").asInt(),
                js.get("multiplication").get("factorB").asInt(),
                js.get("resultAttempt").asInt(),
                js.get("correct").asBoolean());
    }
}
