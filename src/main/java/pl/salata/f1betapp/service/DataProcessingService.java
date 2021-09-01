package pl.salata.f1betapp.service;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DataProcessingService {

    private final String CIRCUIT_DATA_SOURCE = "C:/dev_salata/f1betapp/src/main/resources/data/circuits.csv";

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
        return populateDataWithDataSource(importCircuitJob, CIRCUIT_DATA_SOURCE);
    }

    public String populateGrandPrix() {
        return populateData(importGrandPrixJob);
    }

    public String populateRaceFinishStatus() {
        return populateData(importRaceFinishStatusJob);
    }

    public String populateDriver() {
        return populateData(importDriverJob);
    }

    public String populateTeam() {
        return populateData(importTeamJob);
    }

    public String populateRaceResults() {
        return populateData(importRaceResultJob);
    }

    public String populateQualificationResults() {
        return populateData(importQualificationResultJob);
    }

    public String populateData(Job job) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addParameter("timestamp", new JobParameter(System.currentTimeMillis()))
                    .toJobParameters();
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            return jobExecution.getStatus().toString();
        } catch (Exception e) {
            final String MSG = job.getName() + " Job failed" + e;
            System.out.println(MSG);
            return MSG;
        }
    }

    public String populateDataWithDataSource(Job job, String dataSource) {
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
