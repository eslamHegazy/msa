package com.ScalableTeam.reddit.app.seeders;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class UserSeeder {
    private static final String prefix = "u";
    private static final String photoLink = "https://google.com/image/123";
    private static final String email = "someemail@gmail.com";
    private static final String password = "password";
    private final UserRepository userRepository;

    public Set<String> seedUsers() {
        log.info("Seed Users:-----");
        int userNum = 0;
        Set<String> users = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            String id = prefix + userNum++;
            users.add(id);
            User user = User.builder()
                    .userNameId(id)
                    .email(email)
                    .password(password)
                    .profilePhotoLink(photoLink)
                    .build();
            userRepository.save(user);
        }
        return users;
    }
}
