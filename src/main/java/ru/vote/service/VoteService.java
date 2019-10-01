package ru.vote.service;


import ru.vote.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.vote.projectUtils.SecurityUtil;
import ru.vote.projectUtils.ValidationUtil;
import ru.vote.projectUtils.exception.ModificationRestrictionException;
import ru.vote.repository.UserRepository;
import ru.vote.repository.VoteRepository;
import ru.vote.projectUtils.CheckTimeUtil;
import ru.vote.projectUtils.exception.TimeRestrictionException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service("voteService")
public class VoteService {

    private final Sort SORT_DATE = new Sort(Sort.Direction.DESC, "date");

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    @Autowired
    public VoteService(VoteRepository repository, UserRepository userRepository) {
        this.voteRepository = repository;
        this.userRepository = userRepository;
    }
    //Создает с учетом текущего id пользователя
    public Vote create(Vote vote) {
        int userId = SecurityUtil.authUserId();
        vote.setUser_id(userId);
        return voteRepository.save(vote);
    }

    public void delete(int id){
        int userId = SecurityUtil.authUserId();
        int check_int = voteRepository.delete(id, userId);
        ValidationUtil.checkNotFoundWithId(check_int != 0, id);
    }

    public Vote get(int id) {
        return ValidationUtil.checkNotFoundWithId(voteRepository.findById(id).orElse(null), id);
    }

    //Получить список всех голосов
    public List<Vote> getAllHistory() {
        return voteRepository.findAll(SORT_DATE);
    }

    //Получить список голосов на текущую дату
    public List<Vote> getAllActual() {
        LocalDate date = LocalDate.now();
        return voteRepository.getAllActual(date);
    }

    //Обновляет голос за ресторан с учетом временного фильтра
    public void update(Vote vote, int id) {
        LocalTime time = LocalTime.now();
        int user_id_auth = SecurityUtil.authUserId();
        int user_id_from_base = get(id).getUser_id();
        if(!vote.isNew() &&  user_id_auth != user_id_from_base){
            throw new ModificationRestrictionException();
        }
        if(!CheckTimeUtil.checkTimeToVote(time)) {
            throw new TimeRestrictionException();
        }
        voteRepository.save(vote);
        }
    //Получает id ресторана с максимальным числом голосов
     public int resultVote(){
        return voteRepository.getResultVote();
     }
}
