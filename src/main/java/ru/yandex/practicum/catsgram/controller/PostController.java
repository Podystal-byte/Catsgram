package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll(@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "desc") String sort, @RequestParam(defaultValue = "0") Integer from) {
        return postService.findAll(size, sort, from);
    }

    @PostMapping
    public Post create(@RequestBody Post post) throws ConditionsNotMetException {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) throws ConditionsNotMetException, NotFoundException {
        return postService.update(newPost);
    }

    @GetMapping("/{id}")
    public Post findById(@PathVariable Long id) throws NotFoundException {
        if (postService.findPostById(id).isEmpty()){
            throw new NotFoundException("Пользователь не найден");
        }
        return postService.findPostById(id).get();
    }
}