package ru.vote.web;

import org.springframework.format.annotation.DateTimeFormat;
import ru.vote.model.Restaurant;
import ru.vote.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ru.vote.projectUtils.CheckTimeUtil;
import ru.vote.service.UserService;
import ru.vote.projectUtils.UserUtil;
import ru.vote.projectUtils.ValidationUtil;


import javax.validation.Valid;
import java.net.URI;
import java.time.LocalTime;
import java.util.List;

//Доступ ко всем методам есть только у user с ролью role_admin
@RestController
@RequestMapping(value = AdminRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestController  {

    public static final String REST_URL = "/rest/admin/users";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserService service;

    @GetMapping
    public List<User> getAll() {
        log.info("getAll");
        List<User> list= service.getAll();
        return list;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    //Получает ресторан с максимальным количеством голосов
    @GetMapping("/result")
    public Restaurant getResultRestaurant() {
        log.info("get Result Restaurant");
        return service.getResultRestaurantService();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody User user) {
        log.info("create {}", user);
        ValidationUtil.checkNew(user);
        User created = service.create(UserUtil.createNewRole(user));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    //изменяет время, после которого пользователи не могут изменить свой голос
    @GetMapping("/localtime")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setTime(@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)@RequestParam LocalTime localTime){
        CheckTimeUtil.setTimeToVote(localTime);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        service.delete(id);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @PathVariable int id) {
        log.info("update {} with id={}", user, id);
        service.update(user);
    }

    @GetMapping("/by")
    public User getByMail(@RequestParam String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }


    @PatchMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        service.enable(id, enabled);
    }
}