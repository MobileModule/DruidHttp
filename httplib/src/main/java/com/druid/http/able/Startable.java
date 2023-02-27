package com.druid.http.able;


public interface Startable {

    /**
     * Start handle.
     */
    void start();

    /**
     * Whether has already begun.
     *
     * @return true: has already started, false: haven't started.
     */
    boolean isStarted();
}
