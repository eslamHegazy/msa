package com.ScalableTeam.user.commands;

import com.ScalableTeam.user.entity.User;
import com.ScalableTeam.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import models.ReportUserBody;
import models.ReportedUserResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportUserCommand implements ICommand<ReportUserBody, ReportedUserResponse> {

    private final UserRepository userRepository;

    @Override
    public ReportedUserResponse execute(ReportUserBody body) {
        String  userId = body.getUserId();
        String reportedUserId= body.getReportedUserId();
        String reason= body.getReason();

        if(userId==reportedUserId){
            return new ReportedUserResponse(false,"The user can not report himself/herself");
        }
        Optional<User> reportedUser = userRepository.findById(reportedUserId);
        if(!reportedUser.isPresent()){
            return new ReportedUserResponse(false,"The reported user not found in DB!");
        }

        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            return new ReportedUserResponse(false,"User not found in DB!");
        }

        HashMap<String, String> report = new HashMap<String, String>();
        report.put(reportedUserId, reason);
        userRepository.updateReportedUsersWithID(userId, report);

        return new ReportedUserResponse(true,"User reported successfully");

    }
}
