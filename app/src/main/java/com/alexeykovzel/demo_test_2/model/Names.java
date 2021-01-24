package com.alexeykovzel.demo_test_2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Names {
    @JsonProperty("en")
    private String en;
    @JsonProperty("ua")
    private String ua;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    @Override
    public String toString() {
        return "Names{" +
                "en='" + en + '\'' +
                ", ua='" + ua + '\'' +
                '}';
    }
}
