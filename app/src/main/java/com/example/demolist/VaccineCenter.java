package com.example.demolist;

import java.util.ArrayList;

public class VaccineCenter {
    String name,districtName,vaccineType;
    Boolean isAvailable;
    int min_age_limit,max_age_limit,available_capacity,dose1,dose2;
    ArrayList<String>slot;


    public VaccineCenter(String name, String districtName,String vaccineType, int min_age_limit,int max_age_limit,int dose1,int dose2,int available_capacity,Boolean isAvailable,ArrayList<String>slot) {
        this.name = name;
        this.districtName = districtName;
        this.vaccineType=vaccineType;
        this.min_age_limit=min_age_limit;
        this.max_age_limit=max_age_limit;
        this.available_capacity=available_capacity;
        this.isAvailable = isAvailable;
        this.dose1=dose1;
        this.dose2=dose2;
        this.slot=new ArrayList<String>();
        this.slot=slot;
    }



    public String getVaccineType() {
        return vaccineType;
    }

    public void setVaccineType(String vaccineType) {
        this.vaccineType = vaccineType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public int getMin_age_limit() {
        return min_age_limit;
    }

    public void setMin_age_limit(int min_age_limit) {
        this.min_age_limit = min_age_limit;
    }

    public int getMax_age_limit() {
        return max_age_limit;
    }

    public void setMax_age_limit(int max_age_limit) {
        this.max_age_limit = max_age_limit;
    }

    public int getAvailable_capacity() {
        return available_capacity;
    }

    public int getDose1() {
        return dose1;
    }

    public void setDose1(int dose1) {
        this.dose1 = dose1;
    }

    public int getDose2() {
        return dose2;
    }

    public void setDose2(int dose2) {
        this.dose2 = dose2;
    }

    public void setAvailable_capacity(int available_capacity) {
        this.available_capacity = available_capacity;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}
