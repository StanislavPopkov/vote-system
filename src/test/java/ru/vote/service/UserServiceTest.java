package ru.vote.service;

import ru.vote.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vote.Data_test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.vote.Data_test.*;


import java.util.List;

class UserServiceTest extends AbstractServiceTest{

    @Autowired
    protected UserService service;

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
    void getByEmail() {
    }

    @Test
    void getAll() {
        List<User> all = service.getAll();
        Data_test.assertMatch(all, ADMIN, USER, USER2);
    }

    @Test
    void update() {

    }

    @Test
    void testUpdate() {
    }

    @Test
    void enable() {
    }
}