package com.polugrudov.telegrambot.parser;

import com.polugrudov.telegrambot.entity.SearchSubject;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class ParserService {

    Parser parser = new Parser();
    public List<SearchSubject> findSubject(String title) {
        List<SearchSubject> searchSubjects = new ArrayList<>();

        searchSubjects.add(parser.findSearchSubjectOnOtzovik(title));
        searchSubjects.add(parser.findSearchSubjectOnIRecommend(title));

        return searchSubjects;
    }
}
