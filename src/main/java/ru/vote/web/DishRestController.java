package ru.vote.web;

import org.springframework.security.access.annotation.Secured;
import ru.vote.model.Dish;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vote.projectUtils.ValidationUtil;
import ru.vote.service.DishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(DishRestController.REST_URL)
public class DishRestController {
    static final String REST_URL = "/rest/dishes";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected DishService service;

    @GetMapping()
    public List<Dish> getAll() {
        log.info("get All Dishes");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        log.info("get {}", id);
        return service.get(id);
    }
    /**
    Доступ только у user с ролью role_admin
     */
    @Secured("ROLE_ADMIN")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish) {
        log.info("create {}", dish);
        ValidationUtil.checkNew(dish);
        Dish created = service.create(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    /**
     Доступ только у user с ролью role_admin
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    /**
     Доступ только у user с ролью role_admin
     */
    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id) {
        log.info("update {} with id={}", dish, id);
        service.update(dish);
    }

}