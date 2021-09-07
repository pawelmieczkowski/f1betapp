package pl.salata.f1betapp.datapopulating.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class RaceResultsDeserializer extends StdDeserializer<RaceResultsResponse> {

    public RaceResultsDeserializer() {
        this(null);
    }

    public RaceResultsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public RaceResultsResponse deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        RaceResultsResponse raceResultsResponse = new RaceResultsResponse();
        raceResultsResponse.setNumber(node.get("number").asText());
        raceResultsResponse.setPosition(node.get("position").asText());
        raceResultsResponse.setPositionText(node.get("positionText").asText());
        raceResultsResponse.setPoints(node.get("points").asText());
        raceResultsResponse.setDriverGivenName(node.at("/Driver/givenName").asText());
        raceResultsResponse.setDriverFamilyName(node.at("/Driver/familyName").asText());
        raceResultsResponse.setDriverDateOfBirth(node.at("/Driver/dateOfBirth").asText());
        raceResultsResponse.setConstructorName(node.at("/Constructor/name").asText());
        raceResultsResponse.setGrid(node.get("grid").asText());
        raceResultsResponse.setLaps(node.get("laps").asText());
        raceResultsResponse.setStatus(node.get("status").asText());
        raceResultsResponse.setTimeInMilliseconds(node.at("/Time/millis").asText());
        raceResultsResponse.setTime(node.at("/Time/time").asText());
        raceResultsResponse.setFastestLapRank(node.at("/FastestLap/rank").asText());
        raceResultsResponse.setFastestLapLap(node.at("/FastestLap/lap").asText());
        raceResultsResponse.setFastestLapTime(node.at("/FastestLap/Time/time").asText());
        raceResultsResponse.setFastestLapSpeed(node.at("/FastestLap/AverageSpeed/speed").asText());
        return raceResultsResponse;
    }
}
