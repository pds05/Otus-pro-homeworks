package ru.otus.example.serialization.config.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

//@Configuration
public class GsonConfig {

    @Bean
    @Primary
    public Gson gson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter().nullSafe())
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter().nullSafe())
                .create();

        return gson;
    }
}

class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

    protected final DateTimeFormatter df = DateTimeFormatter.ofPattern("'gson'yyyy/MM/dd");

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        out.value(df.format(value));
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        return LocalDate.parse(in.nextString(), df);
    }

}

class ZonedDateTimeTypeAdapter extends TypeAdapter<ZonedDateTime> {

    protected final DateTimeFormatter df = DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(ZoneId.of("Asia/Singapore"));

    @Override
    public void write(JsonWriter out, ZonedDateTime value) throws IOException {
        out.value(df.format(value));
    }

    @Override
    public ZonedDateTime read(JsonReader in) throws IOException {
        return ZonedDateTime.parse(in.nextString(), df);
    }

}
