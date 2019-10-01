package ru.vote.valid;

import ru.vote.model.User;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    private int user_id;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        this.user_id = user.getId();
    }

    public int getUser_id() {
        return user_id;
    }
}