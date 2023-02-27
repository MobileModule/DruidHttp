package com.druid.http.able;


public interface Finishable {

    /**
     * Finish handle.
     */
    void finish();

    /**
     * Whether they have been completed.
     *
     * @return true: finished, false: unfinished.
     */
    boolean isFinished();
}
