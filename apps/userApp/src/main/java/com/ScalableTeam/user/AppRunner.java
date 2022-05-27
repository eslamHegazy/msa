package com.ScalableTeam.user;

import com.ScalableTeam.user.entity.User;
import com.ScalableTeam.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@AllArgsConstructor
@Component
public class AppRunner implements CommandLineRunner {

    final UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
        System.out.println("how the hell this is not working");
        User user=User.builder().build();
        user.setBlockedUsers(new HashMap<>());
        userRepository.save(user);
    }
}
