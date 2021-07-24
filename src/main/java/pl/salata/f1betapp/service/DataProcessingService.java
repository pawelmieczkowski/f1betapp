package pl.salata.f1betapp.service;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DataProcessingService {

    //TODO: custom itemWriter to update rows instead of failing if id is being duplicated


    private final JobLauncher jobLauncher;

    private final Job importCircuitJob;
    private final Job importGrandPrixJob;
    private final Job importRaceFinishStatusJob;
    private final Job importDriverJob;

    public DataProcessingService(@Qualifier("jobCustomLauncher") JobLauncher jobLauncher,
            /*@Qualifier("importCircuitJob")*/ Job importCircuitJob,
            /*@Qualifier("importGrandPrixJob")*/ Job importGrandPrixJob,
                                 Job importRaceFinishStatusJob,
                                 Job importDriverJob) {
        this.jobLauncher = jobLauncher;
        this.importCircuitJob = importCircuitJob;
        this.importGrandPrixJob = importGrandPrixJob;
        this.importRaceFinishStatusJob = importRaceFinishStatusJob;
        this.importDriverJob = importDriverJob;
    }

    public String populateCircuits() {
        return populateData(importCircuitJob);
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
}
