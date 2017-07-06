package com.jsyoon.rxjavaex1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

// Add library package
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    Observable<String> myObservable;

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

        // case 3
        myObservable = Observable.just("Hello");
        myObservable.subscribe(myObserver);
        myObservable.subscribe(myObserver1);
        myObservable.subscribe(myAction);

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

    // case 3
    Observer<String> myObserver = new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
            Log.e("myObserver", "onSubscribe");
        }

        @Override
        public void onError(Throwable e) {
            // Called when the observable encounters an error
        }

        @Override
        public void onComplete() {
            // Called when the observable has no more data to emit
        }

        @Override
        public void onNext(String s) {
            // Called each time the observable emits data
            Log.e("myObserver", s);
        }
    };

    Observer<String> myObserver1 = new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
            Log.e("myObserver1", "onSubscribe");
        }

        @Override
        public void onError(Throwable e) {
            // Called when the observable encounters an error
        }

        @Override
        public void onComplete() {
            // Called when the observable has no more data to emit
        }

        @Override
        public void onNext(String s) {
            // Called each time the observable emits data
            Log.e("myObserver1", s);
        }
    };

    Consumer<String> myAction = new Consumer <String>() {
        @Override
        public void accept(String s) throws Exception {
            Log.e("My Action", s);
        }
    };
}
