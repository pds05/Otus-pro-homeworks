package ru.otus.java.pro.homeworks.hibernate.services;

import ru.otus.java.pro.homeworks.hibernate.dtos.UserProfileDto;

public interface AuthenticationProvider {

    UserProfileDto login(String username, String password);

    void logout(long userId);

    boolean isAuthenticated(long userId);

}
