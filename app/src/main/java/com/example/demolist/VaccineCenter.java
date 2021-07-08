package com.example.demolist;

public class VaccineCenter {
    String name,districtName;
    Boolean isAvailable;

    public VaccineCenter(String name, String districtName, Boolean isAvailable) {
        this.name = name;
        this.districtName = districtName;
        this.isAvailable = isAvailable;
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

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}
