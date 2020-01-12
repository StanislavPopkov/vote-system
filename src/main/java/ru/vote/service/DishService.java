package ru.vote.service;

import org.springframework.transaction.annotation.Transactional;
import ru.vote.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vote.model.Restaurant;
import ru.vote.projectUtils.ValidationUtil;
import ru.vote.projectUtils.exception.NotFoundException;
import ru.vote.repository.DishRepository;
import ru.vote.repository.RestaurantRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.vote.projectUtils.ValidationUtil.checkNotFoundWithId;

@Service("dishService")
public class DishService {

    private DishRepository dishRepository;
    private RestaurantRepository restaurantRepository;

    @Autowired
    public DishService(DishRepository repository, RestaurantRepository restaurantRepository) {
        this.dishRepository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Dish create(Dish dish) {
        checkRestaurant(dish);
        return dishRepository.save(dish);
    }

    @Transactional
    public void delete(int id){
        checkNotFoundWithId(dishRepository.delete(id) != 0, id);
    }

    public Dish get(int id) {
        return ValidationUtil.checkNotFoundWithId(dishRepository.findById(id).orElse(null), id);
    }

    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    @Transactional
    public void update(Dish dish) {
        checkRestaurant(dish);
        dishRepository.save(dish);
    }

    private void checkRestaurant(Dish dish) throws NotFoundException {
        Integer dish_rest_id = Objects.requireNonNull(dish.getRest_id());
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(dish_rest_id);
        if (!restaurantOptional.isPresent()) {
            throw new NotFoundException("Not found restaurant with id = " + dish_rest_id);
        }
    }
}
