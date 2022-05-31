package com.ScalableTeam.services.managers;

public abstract class BaseDatabasePooling {
    public abstract void changeMaxPoolSize(int max);
    public abstract boolean canChangeMaxPoolSize();
    public abstract void changeMaxConnectionTimeout(long max);
    public abstract boolean canChangeMaxConnectionTimeout();
    public abstract void changeMinIdleConnectionSize(int min);
    public abstract boolean canChangeMinIdleConnectionSize();
    public abstract void changeMaxLifetime(long lifetime);
    public abstract boolean canChangeMaxLifetime();
    public abstract void changePoolName(String pool);
    public abstract String getPoolName();
}
