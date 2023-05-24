package com.test.demo.Service;

import org.springframework.stereotype.Service;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FileReadingService {
    public String readWordDocument(String filePath) throws IOException {
        FileInputStream inputStream=new FileInputStream(filePath);
        XWPFDocument document=new XWPFDocument(inputStream);
        XWPFWordExtractor extractor=new XWPFWordExtractor(document);

        String content=extractor.getText();

        extractor.close();
        document.close();
        inputStream.close();

        return content;
    }
}


