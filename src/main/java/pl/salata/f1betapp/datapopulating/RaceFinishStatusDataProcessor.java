package pl.salata.f1betapp.datapopulating;

import org.springframework.batch.item.ItemProcessor;
import pl.salata.f1betapp.model.RaceFinishStatus;

public class RaceFinishStatusDataProcessor implements ItemProcessor<RaceFinishStatusInput, RaceFinishStatus> {

    @Override
    public RaceFinishStatus process(RaceFinishStatusInput input) throws Exception {
        RaceFinishStatus raceFinishStatus = new RaceFinishStatus();

        InputProcessor.parseNumber(input.getStatusId(), Long.class).ifPresent(raceFinishStatus::setId);
        raceFinishStatus.setStatus(InputProcessor.validateString(input.getStatus()));

        return raceFinishStatus;
    }
}
