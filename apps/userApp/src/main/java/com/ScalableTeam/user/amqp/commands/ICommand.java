package com.ScalableTeam.user.amqp.commands;

public interface ICommand<B, R> {
    R execute(B body);
}
