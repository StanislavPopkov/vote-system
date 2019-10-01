package ru.vote.web;


import org.springframework.security.access.annotation.Secured;
import ru.vote.model.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vote.projectUtils.ValidationUtil;
import ru.vote.service.VoteService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {

    public static final String REST_URL = "/rest/votes";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected VoteService service;

    //Получает список голосов на сегодняшнюю дату
    @GetMapping
    public List<Vote> getAllActual() {
        log.info("get All Actual votes");
        List<Vote> list = service.getAllActual();
        return list;
    }

    //Доступ только у user с ролью role_admin
    //Получает список всех голосов
    @Secured("ROLE_ADMIN")
    @GetMapping("/total")
    public List<Vote> getAllHistory() {
        log.info("get All History votes");
        List<Vote> list = service.getAllHistory();
        return list;
    }


    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        log.info("get {}", id);
        Vote v = service.get(id);
        return v;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@Valid @RequestBody Vote vote) {
        log.info("create {}", vote);
        ValidationUtil.checkNew(vote);
        Vote created = service.create(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    //Доступ только у user с ролью role_admin
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        service.delete(id);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Vote vote, @PathVariable int id) {
        log.info("update {} with id={}", vote, id);
        service.update(vote, id);
    }
}
