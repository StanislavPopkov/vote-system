package ru.vote.service;

import ru.vote.model.Restaurant;
import ru.vote.projectUtils.ValidationUtil;
import ru.vote.valid.AuthorizedUser;
import ru.vote.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.vote.repository.UserRepository;


import java.util.List;

import static ru.vote.projectUtils.UserUtil.prepareToSave;
import static ru.vote.projectUtils.ValidationUtil.checkNotFound;
import static ru.vote.projectUtils.ValidationUtil.checkNotFoundWithId;




@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {

    static final Sort SORT_NAME_EMAIL = new Sort(Sort.Direction.ASC, "name", "email");

    private final UserRepository userRepository;
    private final RestaurantService restaurantService;
    private final VoteService voteService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, RestaurantService restaurantService,
                       VoteService voteService, PasswordEncoder passwordEncoder) {
        this.userRepository = repository;
        this.restaurantService = restaurantService;
        this.voteService = voteService;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return userRepository.save(prepareToSave(user, passwordEncoder));
    }

    public void delete(int id) {
        checkNotFoundWithId(userRepository.delete(id) != 0, id);
    }

    public User get(int id) {
        return ValidationUtil.checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(userRepository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        return userRepository.findAll(SORT_NAME_EMAIL);
    }

    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        userRepository.save(prepareToSave(user, passwordEncoder));
    }


    @Transactional
    public void enable(int id, boolean enabled) {
        User user = get(id);
        user.setEnabled(enabled);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }

    //Получает id ресторана с max числом голосов, запрашивает и возвращает ресторан
    public Restaurant getResultRestaurantService(){
        int voteResultRest_id = voteService.resultVote();
        return restaurantService.get(voteResultRest_id);
    }

}