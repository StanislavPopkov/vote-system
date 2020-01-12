package ru.vote.service;


import org.springframework.transaction.annotation.Transactional;
import ru.vote.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.vote.projectUtils.SecurityUtil;
import ru.vote.projectUtils.ValidationUtil;
import ru.vote.projectUtils.exception.ModificationRestrictionException;
import ru.vote.projectUtils.exception.NotFoundException;
import ru.vote.repository.UserRepository;
import ru.vote.repository.VoteRepository;
import ru.vote.projectUtils.CheckTimeUtil;
import ru.vote.projectUtils.exception.TimeRestrictionException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

    /**
    Создает с учетом текущего id пользователя
     */
    @Transactional
    public Vote create(Vote vote) {
        if(!vote.isNew()) {
            throw new ModificationRestrictionException();
        }
        int userId = SecurityUtil.authUserId();
        vote.setUser_id(userId);
        return voteRepository.save(vote);
    }

    @Transactional
    public void delete(int id){
        ValidationUtil.checkNotFoundWithId(voteRepository.delete(id) != 0, id);
    }

    public Vote get(int id) {
        return ValidationUtil.checkNotFoundWithId(voteRepository.findById(id).orElse(null), id);
    }

    /**
    Получить список всех голосов
     */
    public List<Vote> getAllHistory() {
        return voteRepository.findAll(SORT_DATE);
    }

    /**
    Получить список голосов на текущую дату
     */
    public List<Vote> getAllActual() {
        LocalDate date = LocalDate.now();
        return voteRepository.getAllActual(date);
    }

    /**
    Обновляет голос за ресторан с учетом временного фильтра
     */
    @Transactional
    public void update(Vote vote, int id) {
        int user_id_auth = SecurityUtil.authUserId();
        Optional<Vote> userOptional = voteRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new NotFoundException("Not found vote with id = " + id);
        }
        int user_id_from_base = userOptional.get().getUser_id();
        if (vote.isNew() || user_id_auth != user_id_from_base){
            throw new ModificationRestrictionException();
        }
        LocalTime time = LocalTime.now();
        if (!CheckTimeUtil.checkTimeToVote(time)) {
            throw new TimeRestrictionException();
        }
        voteRepository.save(vote);
    }

    /**
    Получает id ресторана с максимальным числом голосов
     */
     public int resultVote(){
        return voteRepository.getResultVote();
     }
}
