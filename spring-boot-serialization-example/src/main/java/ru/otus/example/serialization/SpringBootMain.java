package ru.otus.example.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.example.serialization.entitites.UserTeams;

import java.io.File;
import java.io.IOException;

@Slf4j
@SpringBootApplication
//@EnableAutoConfiguration
//@EnableScheduling
public class SpringBootMain  implements CommandLineRunner {

    public static void main(String... args) {
        SpringApplication.run(SpringBootMain.class);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting serialization/deserialization test...");
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new ParameterNamesModule());
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        doTestSerialization(xmlMapper, getEntityDtoWithMap());
    }

    private static void doTestSerialization(ObjectMapper xmlMapper, UserTeams userTeams) throws IOException {
        log.info("Original object: {}", userTeams);
        xmlMapper.writeValue(new File("test-serialization.xml"), userTeams);
        log.info("TEST: serialization completed");
        UserTeams deserialized = xmlMapper.readValue(new File("test-serialization.xml"), UserTeams.class);
        log.info("TEST: deserialized modified xml to object: {}", deserialized);
        log.info("TEST RESULT: source object equals deserialized object = {}", userTeams.equals(deserialized));
    }

    private UserTeams getEntityDtoWithMap() {
        UserTeams userTeams = new UserTeams();
        UserTeams.User ivan = new UserTeams.User("Ivan", 5,  "boy" );
        UserTeams.User sasha = new UserTeams.User("Sasha", 10, "boy");

        UserTeams.User kolya = new UserTeams.User("Kolya", 20,  "male" );
        UserTeams.User petya = new UserTeams.User("Petya", 30,  "male" );

        userTeams.addEntryToMultiMap("children", ivan);
        userTeams.addEntryToMultiMap("children", sasha);
        userTeams.addEntryToMultiMap("man", kolya);
        userTeams.addEntryToMultiMap("man", petya);

        userTeams.addEntryToMap("children", ivan);
        userTeams.addEntryToMap("man", sasha);

        userTeams.addEntryToMultiList(ivan, "children");
        userTeams.addEntryToMultiList(sasha, "children");
        userTeams.addEntryToMultiList(kolya, "man");
        userTeams.addEntryToMultiList(petya, "man");

        userTeams.addEntryToList(ivan);
        userTeams.addEntryToList(sasha);

        userTeams.setSingleUser(kolya);
        return userTeams;
    }
}
