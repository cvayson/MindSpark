package com.test.demo.Payload;

import lombok.Data;

import java.util.List;

@Data
public class QuestionSaveRequest {

    String questionText;
    List<String>answers;
}
