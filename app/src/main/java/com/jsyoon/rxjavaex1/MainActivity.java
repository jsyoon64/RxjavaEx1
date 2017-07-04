package com.jsyoon.rxjavaex1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

// TODO 1 Add library package
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO 2
        Observable.just(1, 2, 3)
                .subscribeOn(Schedulers.newThread())
                .subscribe(Observer);
    }

    // TODO 3
    Observer<Integer> Observer = new Observer<Integer>() {

        @Override
        public void onSubscribe(Disposable d) {
            Log.e(TAG, "onSubscribe" + Thread.currentThread().getName());
        }

        @Override
        public void onNext(Integer value) {
            Log.e(TAG, "onNext: " + value + "," + Thread.currentThread().getName());
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ");
        }

        @Override
        public void onComplete() {
            Log.e(TAG, "onComplete: All Done!" + Thread.currentThread().getName());
        }

    };
}
