package com.druid.http.able;

public interface Cancelable {

    /**
     * Cancel handle.
     */
    void cancelHand();

    /**
     * Whether has been cancelled.
     *
     * @return true: canceled, false: there is no cancel.
     */
    boolean isCanceled();

}
