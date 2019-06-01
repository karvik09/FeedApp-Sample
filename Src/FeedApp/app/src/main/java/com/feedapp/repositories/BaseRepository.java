package com.feedapp.repositories;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseRepository {

    CompositeDisposable mDisposable = new CompositeDisposable();

    public void dispose(){
        mDisposable.clear();
    }

    public void add(Disposable disposable){
        mDisposable.add(disposable);
    }

}
