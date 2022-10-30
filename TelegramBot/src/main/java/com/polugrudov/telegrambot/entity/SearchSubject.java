package com.polugrudov.telegrambot.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class SearchSubject {

    private Long id = 0L;

    private String name ="";

    private String averageRate = "";

    private String url = "";

    private List<String> rates = new ArrayList<>();

    public String getRates() {

        StringBuilder stringBuilder = new StringBuilder();

        for (String rate : rates) stringBuilder.append(rate).append("\n");

        return stringBuilder.toString();
    }
}
