package ru.otus.example.serialization.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.example.serialization.config.exception.SmsException;

import java.io.File;
import java.io.IOException;

@Slf4j
@Getter
@Setter
@Component
public class ObjectFileConverter implements Transmittable {
    private ObjectMapper objectMapper;
    private String ioFileName;

    public ObjectFileConverter(@Autowired ObjectMapper objectMapper,
                               @Autowired(required = false) String fileName) {
        this.objectMapper = objectMapper;
        this.ioFileName = fileName;
    }

    @Override
    public <T> T pull(Class<T> cls) {
        T result = null;
        try {
            result = objectMapper.readValue(new File(ioFileName), cls);
        } catch (IOException e) {
            log.error("Fail reading from file {}", ioFileName, e);
            throw new SmsException("READ_ERROR", "Ошибка чтения ресурса");
        }
        log.debug("Deserialized from file {} to object: {}", ioFileName, result);
        return result;
    }

    @Override
    public <T> void send(T object) {
        try {
            objectMapper.writeValue(new File(ioFileName), object);
        } catch (IOException e) {
            log.error("Fail writing to file {}", ioFileName, e);
            throw new SmsException("WRITE_ERROR", "Ошибка записи ресурса");
        }
        log.debug("Serialization {} to file {} completed", object, ioFileName);
    }
}
