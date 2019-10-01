package ru.vote.repository;

import ru.vote.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Modifying
    @Query("SELECT v FROM Vote v WHERE v.date=:date ORDER BY v.date desc")
    List<Vote> getAllActual(@Param("date") LocalDate date);

    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id AND v.user_id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query(nativeQuery = true, value = "SELECT v.rest_id, COUNT(*) as mycount FROM votes v GROUP BY v.rest_id ORDER BY mycount DESC LIMIT 1")
    int getResultVote();
}
