package pl.salata.f1betapp.service;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DataProcessingService {

    private final String CIRCUIT_DATA_SOURCE = "C:/dev_salata/f1betapp/src/main/resources/data/circuits.csv";
    private final String GRAND_PRIX_DATA_SOURCE = "C:/dev_salata/f1betapp/src/main/resources/data/races.csv";
    private final String DRIVERS_DATA_SOURCE = "C:/dev_salata/f1betapp/src/main/resources/data/drivers.csv";
    private final String QUALIFICATION_RESULTS_DATA_SOURCE = "C:/dev_salata/f1betapp/src/main/resources/data/qualifying.csv";
    private final String RACE_RESULTS_DATA_SOURCE = "C:/dev_salata/f1betapp/src/main/resources/data/results.csv";
    private final String TEAM_DATA_SOURCE = "C:/dev_salata/f1betapp/src/main/resources/data/constructors.csv";
    private final String RACE_FINISH_STATUS_DATA_SOURCE = "C:/dev_salata/f1betapp/src/main/resources/data/status.csv";

    private final JobLauncher jobLauncher;

    private final Job importCircuitJob;
    private final Job importGrandPrixJob;
    private final Job importRaceFinishStatusJob;
    private final Job importDriverJob;
    private final Job importTeamJob;
    private final Job importRaceResultJob;
    private final Job importQualificationResultJob;

    public DataProcessingService(@Qualifier("jobAsyncLauncher") JobLauncher jobLauncher,
                                 Job importCircuitJob,
                                 Job importGrandPrixJob,
                                 Job importRaceFinishStatusJob,
                                 Job importDriverJob,
                                 Job importTeamJob,
                                 Job importRaceResultJob,
                                 Job importQualificationResultJob) {
        this.jobLauncher = jobLauncher;
        this.importCircuitJob = importCircuitJob;
        this.importGrandPrixJob = importGrandPrixJob;
        this.importRaceFinishStatusJob = importRaceFinishStatusJob;
        this.importDriverJob = importDriverJob;
        this.importTeamJob = importTeamJob;
        this.importRaceResultJob = importRaceResultJob;
        this.importQualificationResultJob = importQualificationResultJob;
    }

    public String populateCircuits() {
        return populateData(importCircuitJob, CIRCUIT_DATA_SOURCE);
    }

    public String populateGrandPrix() {
        return populateData(importGrandPrixJob, GRAND_PRIX_DATA_SOURCE);
    }

    public String populateRaceFinishStatus() {
        return populateData(importRaceFinishStatusJob, RACE_FINISH_STATUS_DATA_SOURCE);
    }

    public String populateDriver() {
        return populateData(importDriverJob, DRIVERS_DATA_SOURCE);
    }

    public String populateTeam() {
        return populateData(importTeamJob, TEAM_DATA_SOURCE);
    }

    public String populateRaceResults() {
        return populateData(importRaceResultJob, RACE_RESULTS_DATA_SOURCE);
    }

    public String populateQualificationResults() {
        return populateData(importQualificationResultJob, QUALIFICATION_RESULTS_DATA_SOURCE);
    }

    public String populateData(Job job, String dataSource) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addParameter("timestamp", new JobParameter(System.currentTimeMillis()))
                    .addParameter("dataSource", new JobParameter(dataSource))
                    .toJobParameters();
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            return jobExecution.getStatus().toString();
        } catch (Exception e) {
            final String MSG = job.getName() + " Job failed" + e;
            System.out.println(MSG);
            return MSG;
        }
    }
}
