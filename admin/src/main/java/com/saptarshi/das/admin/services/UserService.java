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
        users.add(User.builder().id(UUID.randomUUID().toString()).username("saptarshi.das").firstName("Saptarshi").lastName("Das").email("saptarshi.das@mail.com").build());
        users.add(User.builder().id(UUID.randomUUID().toString()).username("debjit.ghosh").firstName("Debjit").lastName("Ghosh").email("debjit.ghosh@mail.com").build());
        users.add(User.builder().id(UUID.randomUUID().toString()).username("abhiroop.das").firstName("Abhiroop").lastName("Das").email("abhiroop.das@mail.com").build());
        users.add(User.builder().id(UUID.randomUUID().toString()).username("ritam.biswas").firstName("Ritam").lastName("Biswas").email("ritam.biswas@mail.com").build());
        users.add(User.builder().id(UUID.randomUUID().toString()).username("soumyadeep.manna").firstName("Soumyadeep").lastName("Manna").email("soumyadeep.manna@mail.com").build());
        users.add(User.builder().id(UUID.randomUUID().toString()).username("debdutta.chatterjee").firstName("Debdutta").lastName("Chatterjee").email("debdutta.chatterjee@mail.com").build());

        return users;
    }
}
