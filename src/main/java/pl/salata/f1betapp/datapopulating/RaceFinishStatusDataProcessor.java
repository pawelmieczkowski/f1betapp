package pl.salata.f1betapp.datapopulating;

import org.springframework.batch.item.ItemProcessor;
import pl.salata.f1betapp.model.RaceFinishStatus;

public class RaceFinishStatusDataProcessor implements ItemProcessor<RaceFinishStatusInput, RaceFinishStatus> {

    @Override
    public RaceFinishStatus process(RaceFinishStatusInput input) throws Exception {
        RaceFinishStatus raceFinishStatus = new RaceFinishStatus();

        raceFinishStatus.setId(Long.parseLong(input.getStatusId()));
        raceFinishStatus.setStatus(input.getStatus());

        return raceFinishStatus;
    }
}
