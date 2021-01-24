package com.alexeykovzel.demo_test_2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("names")
    private Names names;

    @JsonProperty("caloriesIn100g")
    private Double caloriesIn100g;

    @JsonProperty("glycemicIndex")
    private Double glycemicIndex;

    @JsonProperty("proteins")
    private Double proteins;

    @JsonProperty("fats")
    private Double fats;

    @JsonProperty("carbohydrates")
    private Double carbohydrates;

    @JsonProperty("labels")
    private Labels labels;

    @JsonProperty("isComplex")
    private Boolean isComplex;

    @JsonProperty("kind")
    private String kind;

    @JsonProperty("type")
    private String type;

    @JsonProperty("subtype")
    private String subtype;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Names getNames() {
        return names;
    }

    public void setNames(Names names) {
        this.names = names;
    }

    public Double getCaloriesIn100g() {
        return caloriesIn100g;
    }

    public void setCaloriesIn100g(Double caloriesIn100g) {
        this.caloriesIn100g = caloriesIn100g;
    }

    public Double getGlycemicIndex() {
        return glycemicIndex;
    }

    public void setGlycemicIndex(Double glycemicIndex) {
        this.glycemicIndex = glycemicIndex;
    }

    public Double getProteins() {
        return proteins;
    }

    public void setProteins(Double proteins) {
        this.proteins = proteins;
    }

    public Double getFats() {
        return fats;
    }

    public void setFats(Double fats) {
        this.fats = fats;
    }

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }

    public Boolean getComplex() {
        return isComplex;
    }

    public void setComplex(Boolean complex) {
        isComplex = complex;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    @Override
    public String toString() {
        return "Product{" +
                "uuid='" + uuid + '\'' +
                ", names=" + names +
                ", caloriesIn100g=" + caloriesIn100g +
                ", glycemicIndex=" + glycemicIndex +
                ", proteins=" + proteins +
                ", fats=" + fats +
                ", carbohydrates=" + carbohydrates +
                ", labels=" + labels +
                ", isComplex=" + isComplex +
                ", kind='" + kind + '\'' +
                ", type='" + type + '\'' +
                ", subtype='" + subtype + '\'' +
                '}';
    }
}

