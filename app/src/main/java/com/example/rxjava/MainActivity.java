package com.example.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.schedulers.Schedulers;

import static java.sql.DriverManager.println;


public class MainActivity extends AppCompatActivity {
    private Button btn;
    long TIME = 60;
    Subscription s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //interval 按照固定时间发射整数序列的Observable
                Flowable.interval(0, 1, TimeUnit.SECONDS)
                        //take 发射的次数
                        .take(TIME - 1)
                        //map 对Observable发射的每一项数据应用一个函数
                        .map(new Function<Long, Long>(){
                            @Override
                            public Long apply(Long aLong) {
                                return TIME - aLong;
                            }
                        })
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) {
                                return aLong + "s";
                            }
                            //doOnSubscribe一般用于执行一些初始化操作
                        }).doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) {
                        btn.setClickable(false);
                    }
                    //ObserveOn 指定一个观察者在哪个调度器上观察这个Observable
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onSubscribe(Subscription s){
                                MainActivity.this.s = s;
                                s.request(Long.MAX_VALUE);
                            }

                            @Override
                            public void onNext(String string) {
                                btn.setText(string);
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {
                                btn.setClickable(true);
                                btn.setText("获取验证码");
                            }
                        });
            }
        });



    }
}
