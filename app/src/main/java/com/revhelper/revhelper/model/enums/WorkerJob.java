package com.revhelper.revhelper.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum WorkerJob {
    LNP("Начальник поезда"),
    PEM("Поездной электромеханик"),
    DVR("Директор вагона-ресторана"),
    MVB("Менеджер вагона-бистро"),
    KBL("Билетный кассир"),
    SEN_CONDUCTOR("Старший проводник"),
    CONDUCTOR("Проводник"),
    AUTSTAFF("Работник сторонней организации");

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

    public static List<String> getJobListTitles() {
        List<String> result = new ArrayList<>();
        for (WorkerJob job : WorkerJob.values()) {
            result.add(job.getJobTitle());
        }

        return result;
    }

}
