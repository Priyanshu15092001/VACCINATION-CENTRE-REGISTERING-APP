package com.example.demolist;

import java.util.ArrayList;

public class DistrictVaccineCenter {

    String name,address,vaccineType;
    Boolean isAvailable;
    int min_age_limit,available_capacity,dose1,dose2;
    ArrayList<String> slot;
    Boolean allow_all_age;


    public DistrictVaccineCenter(String name, String address,String vaccineType, int min_age_limit,Boolean allow_all_age,int available_capacity,int dose1,int dose2,Boolean isAvailable,ArrayList<String>slot) {
        this.name = name;
        this.address = address;
        this.vaccineType=vaccineType;
        this.min_age_limit=min_age_limit;
        this.allow_all_age=allow_all_age;
        this.available_capacity=available_capacity;
        this.isAvailable = isAvailable;
        this.dose1=dose1;
        this.dose2=dose2;
        this.slot=new ArrayList<String>();
        this.slot=slot;
    }

    public Boolean getAllow_all_age() {
        return allow_all_age;
    }

    public void setAllow_all_age(Boolean allow_all_age) {
        this.allow_all_age = allow_all_age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVaccineType() {
        return vaccineType;
    }

    public void setVaccineType(String vaccineType) {
        this.vaccineType = vaccineType;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public int getMin_age_limit() {
        return min_age_limit;
    }

    public void setMin_age_limit(int min_age_limit) {
        this.min_age_limit = min_age_limit;
    }



    public int getAvailable_capacity() {
        return available_capacity;
    }

    public void setAvailable_capacity(int available_capacity) {
        this.available_capacity = available_capacity;
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
}

