package com.example.hi_breed.classesFile;

public class ServiceDisplay {
    private  service_class service;
    private  float percent;
    private  int numReviews;

    public ServiceDisplay(service_class service, float percent,int numReviews){

        this.service = service;
        this.percent = percent;
        this.numReviews = numReviews;
    }

    public void setNumReviews(int numReviews) {
        this.numReviews = numReviews;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public void setService(service_class service) {
        this.service = service;
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
