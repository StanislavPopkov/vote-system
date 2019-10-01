package ru.vote.projectUtils;

import ru.vote.model.Role;
import ru.vote.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import java.util.HashSet;
import java.util.Set;


public class UserUtil {

    public static User createNewRole(User user) {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(Role.ROLE_USER);
        if(CollectionUtils.isEmpty(user.getRoles())){
            user.setRoles(roleSet);
        }
        return user;
    }

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}