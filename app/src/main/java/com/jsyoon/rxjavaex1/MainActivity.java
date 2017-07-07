package com.jsyoon.rxjavaex1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/* Add library package */

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    Observable<String> myObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* case 1 */
        Observable.just(1, 2, 3)
                .subscribeOn(Schedulers.newThread())
                .subscribe(Observer);

        /* case 2 */
        Single.just("Hello World")
                .subscribe(getSingleObserver());

        /* case 3 */
        myObservable = Observable.just("Hello");
        myObservable.subscribe(myObserver);
        myObservable.subscribe(myObserver1);
        myObservable.subscribe(myAction);

        /* case 4 */
        Observable<Integer> myArrayObservable  = Observable.just(1, 2, 3, 4, 5, 6); // Emits each item of the array, one at a time

        myArrayObservable.map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer * integer; // Square the number;
            } // Input and Output are both Integer
        }).subscribe(myIntegerAction);

        myArrayObservable
                .skip(2) // Skip the first two items
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 2 == 0;
                    }
                }).subscribe(myIntegerAction);

        // case 5
        flapMapEx();

        flapMapEx1();

        rxAndex1();
        rxAndex2();
        rxAndex3();
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
    Consumer<Integer> myIntegerAction = new Consumer <Integer>() {
        @Override
        public void accept(Integer s) throws Exception {
            Log.e("myIntegerAction", String.valueOf(s));
        }
    };

    // case 5
    private void flapMapEx() {
        List<Integer> ints = new ArrayList<>();
        for (int i=1; i<10; i++) {
            ints.add(new Integer(i));
        }
        Log.e("flatMap", "1,2,3,4,5,6,7,8,9");

        Observable.just(ints)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<List<Integer>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(List<Integer> ints) {
                        return Observable.fromIterable(ints);
                    }
                })
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) {
                        Log.e("flatMap", "filter out odd numbers.........");
                        return integer.intValue() % 2 == 0;
                    }
                })
                .flatMap(new Function<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(Integer integer) {
                        //simulating a heavy duty computational expensive operation
                        for (int i = 0; i < 1000000000; i++) {
                        }
                        return Observable.just(integer* 2);
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onComplete() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(Integer integer) {
                        Log.e("flatMap", "onNext: " + integer.toString());
                    }
                });
    }

    // case 6
    private void flapMapEx1() {
        Observable<Integer> observable = Observable.range(1, 5);

        Observable<String> observableFin = observable.map(new Function<Integer, String>(){
            @Override
            public String apply(Integer itemFromSource) throws Exception {
                return "maped "+itemFromSource;
            }
        });
        observableFin.subscribe(new Consumer <String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e("flapMapEx1", s);
            }
        });
    }

    // case 7
    private void rxAndex1() {
        Observable<String> observable = Observable.just("java", "spring", "hibernate", "android");
        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e("rxAndex1", " on subscribe");
            }
            @Override
            public void onNext(String value) {
                Log.e("rxAndex1", " onNext," + value);
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });

        Observable<String> observableRepeat = observable.repeat(5);
        observableRepeat.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e("observableRepeat", " on subscribe");
            }
            @Override
            public void onNext(String value) {
                Log.e("observableRepeat", " onNext," + value);
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });
    }

    // case 8
    private void rxAndex2(){
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.e("rxAndex2","Observerable on Thread: " + Thread.currentThread().getId());
                e.onNext("Hello");
                e.onNext("Welcome");
                e.onNext("This is your first RxJava example");
                e.onComplete();
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e("rxAndex2"," observer subscribed to observable");
            }

            @Override
            public void onNext(String value) {
                Log.e("rxAndex2"," observer - onNext "+value + "on Thread: "+Thread.currentThread().getId());
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };

        //subscribeOn specifies thread observable code which calls onNext method of observer runs in
        //observeOn specifies thread onNext method code of observer runs in, this is the thread which needs the results
        observable
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe(observer);
    }


    // case 9
    private void rxAndex3(){
        Observable<Integer> observable = Observable.range(1,10);
        Observable<List<Integer>> observableBuffer = observable.buffer(3);
        observableBuffer.subscribe(new Observer<List<Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Integer> value) {
                Log.e("rxAndex3","observer - values from buffer "+ value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
