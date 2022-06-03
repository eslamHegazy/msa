package com.ScalableTeam.user;

import com.ScalableTeam.models.user.LoginBody;
import com.ScalableTeam.models.user.LoginResponse;
import com.ScalableTeam.models.user.SignUpBody;
import com.ScalableTeam.user.caching.RedisUtility;
import com.ScalableTeam.user.commands.LoginCommand;
import com.ScalableTeam.user.commands.SignUpCommand;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;

@AllArgsConstructor
//@Component
public class AppRunner implements CommandLineRunner {

    SignUpCommand signUpCommand;
    LoginCommand loginCommand;

    RedisUtility redisUtility;

    @Override
    public void run(String... args) throws Exception {
        String userId = "mo";
        String email = "mo@gmail.com";
        String password = "pass";
        SignUpBody signUpBody = new SignUpBody(userId, email, password);
        signUpCommand.execute(signUpBody);
        LoginBody loginBody = new LoginBody(userId, password);
        LoginResponse loginResponse = loginCommand.execute(loginBody);
        System.out.println(loginResponse.getAuthToken());
    }
}
