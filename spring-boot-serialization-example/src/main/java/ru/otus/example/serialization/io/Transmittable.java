package ru.otus.example.serialization.io;

public interface Transmittable {

    <T> T pull(Class<T> cls);

    <T> void send(T o);
}
