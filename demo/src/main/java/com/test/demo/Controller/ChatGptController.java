package com.test.demo.Controller;

import com.test.demo.Payload.QuestionSaveRequest;
import com.test.demo.Service.ChatGptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequestMapping("/chat")
@RestController
public class ChatGptController {

    private final ChatGptService service;

    public ChatGptController(ChatGptService service) {
        this.service = service;
    }

    @GetMapping("/get_response/{fileName}")
    public List<QuestionSaveRequest> getResponse(@PathVariable String fileName) throws IOException {
        String filePath="/home/emir/Downloads/"+fileName;
        return this.service.generateQuestionsAndAnswers(filePath);
    }

}
