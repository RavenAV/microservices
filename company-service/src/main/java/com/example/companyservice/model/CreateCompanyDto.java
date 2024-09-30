package com.example.companyservice.model;

public class CreateCompanyDto {
    private String name;
    private String ogrn;
    private String description;
    private Long directorId;

    // Getters and setters
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }
}
