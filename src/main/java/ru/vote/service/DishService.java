package ru.vote.service;

import ru.vote.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vote.projectUtils.ValidationUtil;
import ru.vote.repository.DishRepository;
import ru.vote.repository.RestaurantRepository;

import java.util.List;
import java.util.Objects;

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


    public Dish create(Dish dish) {
        Integer dish_rest_id = Objects.requireNonNull(dish.getRest_id());
        Integer restoraunt_id = Objects.requireNonNull(restaurantRepository.getOne(dish_rest_id).getId());
        if (!dish.isNew() && dish_rest_id != restoraunt_id) {
            return null;
        }
        return dishRepository.save(dish);
    }

    public void delete(int id){
        checkNotFoundWithId(dishRepository.delete(id) != 0, id);
    }

    public Dish get(int id) {
        return ValidationUtil.checkNotFoundWithId(dishRepository.findById(id).orElse(null), id);
    }

    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    public void update(Dish dish) {
        dishRepository.save(dish);
    }
}
