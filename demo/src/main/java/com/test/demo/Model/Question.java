package com.test.demo.Model;

import lombok.Data;

import java.util.List;

@Data
public class Question {

    String questionText;
    List<String>answers;
}
