package ru.vote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.vote.model.Restaurant;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.vote.projectUtils.ValidationUtil;
import ru.vote.repository.RestaurantRepository;
import java.time.LocalDate;
import java.util.List;

import static ru.vote.projectUtils.ValidationUtil.checkNotFoundWithId;

@Service("restaurantService")
public class RestaurantService {

    private final Sort SORT_NAME = new Sort(Sort.Direction.DESC, "name");

    private RestaurantRepository repository;

    @Autowired
    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Restaurant create(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    @Transactional
    public void delete(int id){
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public Restaurant get(int id) {
        return ValidationUtil.checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public List<Restaurant> getAllHistory() {
        return repository.findAll(SORT_NAME);
    }

    public List<Restaurant> getAllActual() {
        LocalDate date = LocalDate.now();
        return repository.getAllActual(date);
    }

    @Transactional
    public void update(Restaurant restaurant) {
        repository.save(restaurant);
    }

}
