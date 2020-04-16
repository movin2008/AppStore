package com.shuiyes.appstore.ui.viewmodel;

public abstract class BaseViewModel {

    protected final String TAG = this.getClass().getSimpleName();

    public abstract void start();

    public abstract void destroy();

}
