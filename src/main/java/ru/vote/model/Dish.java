package ru.vote.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "dishes")
public class Dish extends AbstractBaseEntity  {

    @Column(name = "date", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date = LocalDate.now();

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "dish_name", nullable = false)
    protected String dish_name;

    @Column(name = "price")
    @Digits(integer = 10 /*precision*/, fraction = 2 /*scale*/)
    @NotNull
    protected Double price;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "rest_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @NotNull
//    private Restaurant restaurant;


    //https://stackoverflow.com/questions/6311776/hibernate-foreign-keys-instead-of-entities
    @Column(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Integer rest_id;

    public Dish() {
    }

    public Dish(Integer id, LocalDate date, String dish_name,  Double price, Integer rest_id) {
        super(id);
        this.date = date;
        this.dish_name = dish_name;
        this.price = price;
        this.rest_id = rest_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDish_name() {
        return dish_name;
    }

    public void setDish_name(String dish_name) {
        this.dish_name = dish_name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Integer getRest_id() {
        return rest_id;
    }

    public void setRest_id(Integer rest_id) {
        this.rest_id = rest_id;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "date=" + date +
                ", dish_name='" + dish_name + '\'' +
                ", price=" + price +
                ", rest_id=" + rest_id +
                '}';
    }

}
