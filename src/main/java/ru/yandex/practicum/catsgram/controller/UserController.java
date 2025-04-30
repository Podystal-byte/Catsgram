package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) throws ConditionsNotMetException, DuplicatedDataException {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws ConditionsNotMetException, NotFoundException, DuplicatedDataException {
        return userService.update(user);
    }
}
