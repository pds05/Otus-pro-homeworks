package ru.otus.example.serialization.io;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class XmlConverter extends ObjectFileConverter {

    private static XmlMapper xmlMapper;

    @Autowired
    public void setXmlMapper(XmlMapper xmlMapper) {
        XmlConverter.xmlMapper = xmlMapper;
    }

    @Value("${sms.file-export.xml}")
    public static String FILE_NAME;

    @Value("${sms.file-export.xml}")
    public void setFileName(String fileName) {
        FILE_NAME = fileName;
    }

    public XmlConverter() {
        super(xmlMapper, FILE_NAME);
    }
}
