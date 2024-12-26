package ru.otus.java.pro.homeworks.hibernate.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.homeworks.hibernate.daos.OrderDao;
import ru.otus.java.pro.homeworks.hibernate.daos.UserDao;
import ru.otus.java.pro.homeworks.hibernate.dtos.OrderDto;
import ru.otus.java.pro.homeworks.hibernate.dtos.UserProfileDto;
import ru.otus.java.pro.homeworks.hibernate.entities.Order;
import ru.otus.java.pro.homeworks.hibernate.entities.User;
import ru.otus.java.pro.homeworks.hibernate.entities.UserContact;
import ru.otus.java.pro.homeworks.hibernate.exceptions.ApplicationException;
import ru.otus.java.pro.homeworks.hibernate.util.EntityDtoMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class UserService implements AuthenticationProvider {
    public static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final Set<Long> CACHE_LOGGED_USER = new HashSet<>();

    private UserDao userDao;
    private OrderDao orderDao;

    public UserProfileDto login(String username, String password) {
        User user = userDao.findByCredentials(username, password).orElseThrow(() -> new ApplicationException("Wrong username or password"));
        CACHE_LOGGED_USER.add(user.getId());
        logger.info("Logged in user {}", user);
        return EntityDtoMapper.map(user);
    }

    public void logout(long userId) {
        CACHE_LOGGED_USER.remove(userId);
        logger.info("Logged out user {}", userId);
    }

    public UserProfileDto restoreProfile(String phoneNumber, String email) {
        User user = userDao.findByContacts(phoneNumber, email).orElseThrow(() -> new ApplicationException("User not found"));
        logger.info("Restored user {}", user);
        return EntityDtoMapper.map(user);
    }

    public UserProfileDto getProfile(long userId) {
        if (CACHE_LOGGED_USER.contains(userId)) {
            User user = userDao.findById(userId).orElseThrow(() -> new ApplicationException("User not found"));
            return EntityDtoMapper.map(user);
        } else {
            throw new ApplicationException("User must be authenticated");
        }
    }

    public UserProfileDto createProfile(String username, String password, String phoneNumber, String email) {
        UserContact userContact = new UserContact();
        userContact.setPhoneNumber(phoneNumber);
        userContact.setEmail(email);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setUserContact(userContact);
        userDao.save(user);
        logger.info("Created user {}", user);
        return EntityDtoMapper.map(user);
    }

    public void deleteProfile(long userId) {
        if (CACHE_LOGGED_USER.contains(userId)) {
            userDao.delete(userId);
            CACHE_LOGGED_USER.remove(userId);
            logger.info("Deleted userId={}", userId);
        } else {
            throw new ApplicationException("User must be authenticated");
        }
    }

    public void updateProfile(UserProfileDto userDto) {
        if (CACHE_LOGGED_USER.contains(userDto.getUserId())) {
            User user = userDao.findById(userDto.getUserId()).orElseThrow(() -> new ApplicationException("User not found"));
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            user.getUserContact().setPhoneNumber(userDto.getPhone());
            user.getUserContact().setEmail(userDto.getEmail());
            userDao.update(user);
            logger.info("Updated user {}", user);
        } else {
            throw new ApplicationException("User must be authenticated");
        }
    }

    public List<OrderDto> getOrders(long userId) {
        if (CACHE_LOGGED_USER.contains(userId)) {
            List<Order> orders = orderDao.findAllByUserId(userId);
            return orders.stream().map(EntityDtoMapper::map).toList();
        } else {
            throw new ApplicationException("User must be authenticated");
        }
    }

    public void removeOrder(long userId, long orderId) {
        if (CACHE_LOGGED_USER.contains(userId)) {
            orderDao.findById(orderId).orElseThrow(() -> new ApplicationException("Order not found"));
            orderDao.delete(orderId);
        } else {
            throw new ApplicationException("User must be authenticated");
        }
    }

    public boolean isAuthenticated(long userId) {
        return CACHE_LOGGED_USER.contains(userId);
    }
}
