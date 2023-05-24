package com.test.demo.Controller;

import com.test.demo.Service.FileReadingService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/document")
@RestController
public class FileController {

    private final FileReadingService service;

    public FileController(FileReadingService service) {
        this.service = service;
    }
    @GetMapping("/getcontent/{fileName}")
    public String getContent(@PathVariable String fileName) throws IOException {
        String filePath="/home/emir/Downloads/"+fileName;
        return this.service.readWordDocument(filePath);
    }
}
