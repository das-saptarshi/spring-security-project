package com.saptarshi.das.admin.services;

import com.saptarshi.das.admin.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    public List<User> getUsers() {
        final List<User> users = new ArrayList<>();
        users.add(User.builder().id(UUID.randomUUID().toString()).username("saptarshi.das").name("Saptarshi Das").email("saptarshi.das@mail.com").build());
        users.add(User.builder().id(UUID.randomUUID().toString()).username("debjit.ghosh").name("Debjit Ghosh").email("debjit.ghosh@mail.com").build());
        users.add(User.builder().id(UUID.randomUUID().toString()).username("abhiroop.das").name("Abhiroop Das").email("abhiroop.das@mail.com").build());
        users.add(User.builder().id(UUID.randomUUID().toString()).username("ritam.biswas").name("Ritam Biswas").email("ritam.biswas@mail.com").build());
        users.add(User.builder().id(UUID.randomUUID().toString()).username("soumyadeep.manna").name("Soumyadeep Manna").email("soumyadeep.manna@mail.com").build());
        users.add(User.builder().id(UUID.randomUUID().toString()).username("debdutta.chatterjee").name("Debdutta Chatterjee").email("debdutta.chatterjee@mail.com").build());

        return users;
    }
}
