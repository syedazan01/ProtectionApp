package com.example.protectionapp.model;

public class ServiceBean {
    String serviceName;
    int serviceResId;

    public ServiceBean(String serviceName, int serviceResId) {
        this.serviceName = serviceName;
        this.serviceResId = serviceResId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getServiceResId() {
        return serviceResId;
    }
}
