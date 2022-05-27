package com.ScalableTeam.user;

import com.ScalableTeam.user.commands.LoginCommand;
import com.ScalableTeam.user.commands.SignUpCommand;
import com.ScalableTeam.user.entity.User;
import com.ScalableTeam.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import models.LoginBody;
import models.LoginResponse;
import models.SignUpBody;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@AllArgsConstructor
@Component
public class AppRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("helooooooooooooooo");
    }
}
