package com.ScalableTeam.notifications.utils;

public interface Command {
    Object execute(Object body) throws Exception;
}
