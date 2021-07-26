package pl.salata.f1betapp.datapopulating;

import org.springframework.batch.item.ItemProcessor;
import pl.salata.f1betapp.model.Driver;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;


public class DriverDataProcessor implements ItemProcessor<DriverInput, Driver> {

    @Override
    public Driver process(DriverInput input) throws Exception {
        Driver driver = new Driver();

        InputProcessor.parseNumber(input.getDriverId(), Long.class).ifPresent(driver::setId);

        driver.setDriverCode(InputProcessor.validateString(input.getCode()));
        driver.setForename(InputProcessor.validateString(input.getForename()));
        driver.setSurname(InputProcessor.validateString(input.getSurname()));
        driver.setDriverNumber(InputProcessor.validateString(input.getNumber()));

        driver.setNationality(InputProcessor.validateString(input.getNationality()));
        driver.setUrl(InputProcessor.validateString(input.getUrl()));

        driver.setDateOfBirth(InputProcessor.parseDate(input.getDob()));

        return driver;
    }
}

