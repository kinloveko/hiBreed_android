package com.example.hi_breed.classesFile;

public class ServiceDisplay {
    private final service_class service;
    private final float percent;
    private final int numReviews;

    public ServiceDisplay(service_class service, float percent,int numReviews){

        this.service = service;
        this.percent = percent;
        this.numReviews = numReviews;
    }

    public service_class getService() {
        return service;
    }

    public float getPercent() {
        return percent;
    }

    public int getNumReviews() {
        return numReviews;
    }
}
