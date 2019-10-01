package ru.vote.repository;

import ru.vote.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Modifying
    @Query("SELECT DISTINCT r FROM Restaurant r LEFT OUTER JOIN FETCH Dish d on r.id=d.rest_id WHERE d.date=:date")
    List<Restaurant> getAllActual(@Param("date") LocalDate date);

    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);
}
