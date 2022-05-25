package com.ScalableTeam.user.commands;

public interface ICommand<B, R> {
    R execute(B body);
}
