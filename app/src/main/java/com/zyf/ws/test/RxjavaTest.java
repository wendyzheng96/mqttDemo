package com.zyf.ws.test;

import android.util.Log;

import com.zyf.ws.bean.Course;
import com.zyf.ws.bean.Student;

import org.reactivestreams.Subscriber;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zyf on 2019/1/14.
 */
public class RxjavaTest {

    private static final String TAG = "RxjavaTest";

    private void test() {
        Student[] students = new Student[]{};
        Observable.fromArray(students).map(new Function<Student, Student>() {
            @Override
            public Student apply(Student student) {
                return student;
            }
        }).subscribe(new DefaultObserver<Student>() {
            @Override
            public void onNext(Student student) {
                Log.d(TAG, "student:" + student.getName());
                List<Course> courses = student.getCourses();
                if (courses != null) {
                    for (Course course : courses) {
                        Log.d(TAG, "course: "+ course.getCourseName());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


//        Observable.fromArray(students).flatMap(new Function<Student, Observable<Course>>() {
//            @Override
//            public Observable<Course> apply(Student student) {
//                return Observable.just(student.getCourses());
//            }
//        }).subscribe(new Observer<Object>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Object o) {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

        Observable observable = new Observable() {
            @Override
            protected void subscribeActual(Observer observer) {

            }
        };
        observable.lift(new ObservableOperator<String, Integer>() {
            @Override
            public Observer<? super Integer> apply(final Observer<? super String> observer) {
                return new DefaultObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        observer.onNext("" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                };
            }
        });


        Observable.fromArray(students)
                //指定 subscribe() 所发生的线程
                .subscribeOn(Schedulers.io())
                //在 doOnSubscribe()的后面跟一个 subscribeOn() ，就能指定准备工作的线程
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {

                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                //指定 Subscriber 所运行在的线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Student>() {
                    @Override
                    public void onNext(Student student) {

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
