package pl.salata.f1betapp.datapopulating;

import org.springframework.batch.item.ItemProcessor;
import pl.salata.f1betapp.model.Driver;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class DriverDataProcessor implements ItemProcessor<DriverInput, Driver> {

    @Override
    public Driver process(DriverInput input) throws Exception {
        Driver driver = new Driver();

        driver.setId(Long.parseLong(input.getDriverId()));
        driver.setDriverCode(input.getCode());
        driver.setForename(input.getForename());
        driver.setSurname(input.getSurname());

        driver.setDriverNumber(InputProcessor.validateString(input.getNumber()));
        driver.setDateOfBirth(LocalDate.parse(input.getDob(), ISO_LOCAL_DATE));

        driver.setNationality(input.getNationality());
        driver.setUrl(input.getUrl());

        return driver;
    }
}

