package ru.otus.example.serialization.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class JsonConverter extends ObjectFileConverter {

    private static ObjectMapper objectMapper;

    @Autowired
    public void setXmlMapper(ObjectMapper objectMapper) {
        JsonConverter.objectMapper = objectMapper;
    }

    public static String FILE_NAME;

    @Value("${sms.file-export.json}")
    public void setFileName(String fileName) {
        FILE_NAME = fileName;
    }

    public JsonConverter() {
        super(objectMapper, FILE_NAME);
    }
}
