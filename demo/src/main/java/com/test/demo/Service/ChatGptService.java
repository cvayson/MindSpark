package com.test.demo.Service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.demo.Model.Question;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatGptService {

    private final FileReadingService fileReadingService;

    public ChatGptService(FileReadingService fileReadingService) {
        this.fileReadingService = fileReadingService;
    }

    public List<Question> generateQuestionsAndAnswers(String filePath) throws IOException {
        String apiKey="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";
        String apiEndpoint="https://api.openai.com/v1/chat/completions";
        String lecture=this.fileReadingService.readWordDocument(filePath);


        HttpHeaders headers =new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Map<String,String>> messages =new ArrayList<>();
        Map<String,String>userMessage=new HashMap<>();
        userMessage.put("role","system");
        userMessage.put("content","Give me questions with a correct answer and three wrong answers to summarise this lecture, and always place the correct answer as the first one,with every new question marked with Q: "+lecture);
        messages.add(userMessage);

        Map<String,Object> requestData=new HashMap<>();
        requestData.put("messages",messages);
        requestData.put("max_tokens",3500);
        requestData.put("model","gpt-3.5-turbo");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestData, headers);
        RestTemplate restTemplate =new RestTemplate();
        ResponseEntity<String>response=restTemplate.postForEntity(apiEndpoint,request,String.class);

        if(response.getStatusCode()== HttpStatus.OK)
         return saveQuestions(extractContentFromResponse(response.getBody()));
        return null;

    }
    private String extractContentFromResponse(String apiResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(apiResponse);


            JsonNode choicesNode = responseJson.get("choices");
            if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode firstChoice = choicesNode.get(0);
                JsonNode messageNode = firstChoice.get("message");
                if (messageNode != null && messageNode.isObject()) {
                    JsonNode contentNode = messageNode.get("content");
                    if (contentNode != null) {
                        return contentNode.asText();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Unable to extract content from API response.";
    }
    private List<Question> saveQuestions(String questionsString)
    {
        List<Question>questionList=new ArrayList<>();
        String[] splitQuestions=questionsString.split("Q:");

        for(String splitQuestion:splitQuestions)
        {
            String[] questionParts=splitQuestion.split("\n");
            String questionText=questionParts[0];
            List<String>answers=new ArrayList<>();
            for (int i=1;i<questionParts.length;i++)
            {
                String answer=questionParts[i].trim();
                if(!answer.isEmpty())
                {
                    answers.add(answer);
                }
            }
            Question question=new Question();
            if(!questionText.isEmpty()&&!answers.isEmpty())
            {
                question.setQuestionText(questionText);
                question.setAnswers(answers);
                questionList.add(question);
            }
        }
        return questionList;
    }
}
