package com.ScalableTeam.notifications;

public interface Command {
    Object execute(Object body) throws Exception;
}
