package com.ScalableTeam.user;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.LoginBody;
import com.ScalableTeam.models.user.LoginResponse;
import com.ScalableTeam.models.user.SignUpBody;
import com.ScalableTeam.user.caching.RedisUtility;
import com.ScalableTeam.user.commands.LoginCommand;
import com.ScalableTeam.user.commands.SignUpCommand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class AppRunner implements CommandLineRunner {

//    SignUpCommand signUpCommand;
//    LoginCommand loginCommand;
//
//    RedisUtility redisUtility;
    UserRepository userRepository;
    @Override
    public void run(String... args) {
//        String userId = "mo";
//        String email = "mo@gmail.com";
//        String password = "pass";
//        SignUpBody signUpBody = new SignUpBody(userId, email, password);
//        signUpCommand.execute(signUpBody);
//        LoginBody loginBody = new LoginBody(userId, password);
//        LoginResponse loginResponse = loginCommand.execute(loginBody);
//        System.out.println(loginResponse.getAuthToken());
//        System.out.println(redisUtility.getValue(userId));

        log.error("how this is not working");
        String userId = "1404";
        String userId2 = "1523";

        User user = User.builder().userNameId(userId).build();
        userRepository.save(user);
        user = User.builder().userNameId(userId2).build();
        userRepository.save(user);

        log.info(userRepository.findAll().toString());
    }
}
