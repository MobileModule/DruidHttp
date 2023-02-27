package com.druid.http.rest;

import com.druid.http.HandlerDelivery;

public class Messenger {
    private final int what;
    private final OnResponseListener listener;

    private Messenger(int what, OnResponseListener listener) {
        this.what = what;
        this.listener = listener;
    }

    static  Messenger newInstance(int what, OnResponseListener listener) {
        return new Messenger(what, listener);
    }

    void start() {
        HandlerDelivery.getInstance().post(Poster.newInstance(what, listener).start());
    }

    void response(DruidHttpResponse response) {
        HandlerDelivery.getInstance().post(Poster.newInstance(what, listener).response(response));
    }

    void finish() {
        HandlerDelivery.getInstance().post(Poster.newInstance(what, listener).finish());
    }

    private static class Poster implements Runnable {

        private static  Poster newInstance(int what, OnResponseListener listener) {
            return new Poster(what, listener);
        }

        private final int what;
        private final OnResponseListener listener;

        private int command;
        private DruidHttpResponse response;

        private Poster(int what, OnResponseListener listener) {
            this.what = what;
            this.listener = listener;
        }

        Poster start() {
            this.command = -1;
            return this;
        }

        Poster response(DruidHttpResponse response) {
            this.command = -2;
            this.response = response;
            return this;
        }

        Poster finish() {
            this.command = -3;
            return this;
        }

        @Override
        public void run() {
            if (listener == null) return;
            switch (command) {
                case -1: {
                    listener.onStart(what);
                    break;
                }
                case -2: {
                    if (response.isSucceed())
                        listener.onSucceed(what, response);
                    else
                        listener.onFailed(what, response);
                    break;
                }
                case -3: {
                    listener.onFinish(what);
                    break;
                }
            }
        }
    }
}
