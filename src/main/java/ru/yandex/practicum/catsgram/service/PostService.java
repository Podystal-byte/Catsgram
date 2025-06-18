package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.*;

@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();

    public Collection<Post> findAll(Integer size, String sort, Integer from) {
        int safeSize = (size <= 0) ? 10 : size;
        int safeFrom = (from < 0) ? 0 : from;
        SortOrder request = SortOrder.from(sort == null ? "desc" : sort);
        if (request == null) {
            request = SortOrder.DESCENDING;
        }

        Comparator<Post> comparator = Comparator.comparing(Post::getPostDate);
        if (request == SortOrder.DESCENDING) {
            comparator = comparator.reversed();
        }

        return posts.values().stream()
                .sorted(comparator)
                .skip(safeFrom)
                .limit(safeSize)
                .toList();
    }


    public Post create(Post post) throws ConditionsNotMetException {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) throws ConditionsNotMetException, NotFoundException {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    public Optional<Post> findPostById(Long id) {
        return posts.values().stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    private long getNextId() {
        long currentMaxId = posts.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }
}

enum SortOrder {
    ASCENDING, DESCENDING;

    // Преобразует строку в элемент перечисления
    public static SortOrder from(String order) {
        switch (order.toLowerCase()) {
            case "ascending":
                return ASCENDING;
            case "asc":
                return ASCENDING;
            case "descending":
                return DESCENDING;
            case "desc":
                return DESCENDING;
            default:
                return null;
        }
    }
}
