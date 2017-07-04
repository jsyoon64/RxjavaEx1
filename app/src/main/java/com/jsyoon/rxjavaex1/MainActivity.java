package com.jsyoon.rxjavaex1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

// case 1 Add library package
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // case 1
        Observable.just(1, 2, 3)
                .subscribeOn(Schedulers.newThread())
                .subscribe(Observer);

        // case 2
        Single.just("Hello World")
                .subscribe(getSingleObserver());
    }


    // case 1
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

    // case 2
    private SingleObserver<String> getSingleObserver() {
        return new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe");
            }

            @Override
            public void onSuccess(String value) {
                Log.e(TAG, "onSuccess : " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ");
            }

        };
    }

}
