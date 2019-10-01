package ru.vote.service;

import ru.vote.model.Vote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vote.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    VoteRepository repository;

    @Test
    void create() {
    }

    @Test
    void delete() {
    }

    @Test
    void get() {
    }

    @Test
    void getAllHistory() {
    }

    @Test
    void getAllActual() {
        LocalDate date = LocalDate.now();
        List<Vote> list =repository.getAllActual(date);
        assertNotNull(list);

    }

    @Test
    void update() {
    }
}