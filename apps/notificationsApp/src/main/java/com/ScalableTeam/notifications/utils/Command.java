package com.ScalableTeam.notifications.utils;

public interface Command<Request, Response> {
    Response execute(Request body) throws Exception;
}
