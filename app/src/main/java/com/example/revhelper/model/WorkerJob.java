package com.example.revhelper.model;

public enum WorkerJob {

    LNP("Начальник поезда"),
    PEM("Поездной электромеханик"),
    DVR("Директор вагона-ресторана"),
    MVB("Менеджер вагона-бистро");

    private final String jobTitle;

    WorkerJob(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public static WorkerJob fromString(String jobTitle) {
        for (WorkerJob job : WorkerJob.values()) {
            if (job.jobTitle.equalsIgnoreCase(jobTitle)) {
                return job;
            }
        }
        throw new IllegalArgumentException("Unknown job title: " + jobTitle);
    }

}
