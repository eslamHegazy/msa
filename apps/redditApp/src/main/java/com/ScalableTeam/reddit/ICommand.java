package com.ScalableTeam.reddit;

public interface ICommand<B, R> {
    R execute(B body) throws Exception;
}