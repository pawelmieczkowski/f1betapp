package pl.salata.f1betapp.datapopulating.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class QualifyingResultDeserializer extends StdDeserializer<QualifyingResultResponse> {

    public QualifyingResultDeserializer() {
        this(null);
    }

    public QualifyingResultDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public QualifyingResultResponse deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        QualifyingResultResponse qualifyingResultResponse = new QualifyingResultResponse();
        qualifyingResultResponse.setNumber(node.get("number").asText());
        qualifyingResultResponse.setPosition(node.get("position").asText());
        qualifyingResultResponse.setDriverGivenName(node.at("/Driver/givenName").asText());
        qualifyingResultResponse.setDriverFamilyName(node.at("/Driver/familyName").asText());
        qualifyingResultResponse.setDriverDateOfBirth(node.at("/Driver/dateOfBirth").asText());
        qualifyingResultResponse.setConstructorName(node.at("/Constructor/name").asText());
        qualifyingResultResponse.setQ1time(node.at("/Q1").asText());
        qualifyingResultResponse.setQ2time(node.at("/Q2").asText());
        qualifyingResultResponse.setQ3time(node.at("/Q3").asText());
        return qualifyingResultResponse;
    }
}
