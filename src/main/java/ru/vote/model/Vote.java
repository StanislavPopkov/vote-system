package ru.vote.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "votes")
public class Vote  extends AbstractBaseEntity{

    @Column(name = "date", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date = LocalDate.now();

    //https://stackoverflow.com/questions/6311776/hibernate-foreign-keys-instead-of-entities
    @Column(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Integer user_id;

    //https://stackoverflow.com/questions/6311776/hibernate-foreign-keys-instead-of-entities
    @Column(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Integer rest_id;

    public Vote() {
    }

    public Vote(Integer id, @NotNull LocalDate date, Integer user_id, Integer rest_id) {
        super(id);
        this.date = date;
        this.user_id = user_id;
        this.rest_id = rest_id;
    }

    public Vote(Vote vote) {
        this(vote.getId(), vote.getDate(), vote.getUser_id(), vote.getRest_id());
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getRest_id() {
        return rest_id;
    }

    public void setRest_id(Integer rest_id) {
        this.rest_id = rest_id;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "date=" + date +
                ", user_id=" + user_id +
                ", rest_id=" + rest_id +
                '}';
    }
}
