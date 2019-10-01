package ru.vote.service;

import ru.vote.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vote.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.vote.Data_test.BELKIN2;

class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    RestaurantRepository repository;

    @Test
    void create() {
        repository.save(BELKIN2);
    }

    @Test
    void delete() {
    }

    @Test
    void get() {
    }

    @Test
    void getAllHistory() {
        List<Restaurant> list = repository.findAll();
        assertNotNull(list);
    }

    @Test
    void getAllActual() {
        LocalDate date = LocalDate.now();
        LocalDate date2 = LocalDate.parse("2019-09-17");
        List<Restaurant> list = repository.getAllActual(date);
        assertNotNull(list);
    }

    @Test
    void update() {
    }
}