package com.ScalableTeam.media.commands;

public interface ICommand<B, R> {
    R execute(B body);
}
