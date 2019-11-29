package com.example.rxjava;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * ********文件描述：********
 * ********作者：huleiyang********
 * ********创建时间：2019/11/28********
 * ********更改时间：2019/11/28********
 * ********版本号：1********
 */
public class RxjavaActivity extends AppCompatActivity {
    //定义Disposable类变量
    Disposable mDisposable;
    Integer i = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rxjava_activity_layout);

        findViewById(R.id.rx_basic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 创建被观察者 Observable 对象
                Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) {
                        emitter.onNext("哈喽! Rxjava");
                        emitter.onNext("嗨! Rxjava");
                        emitter.onNext("喂! Rxjava");
                        emitter.onComplete();
                    }
                });
                //创建观察者 （Observer ）并 定义响应事件的行为
                Observer<String> observer = new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("-----rxjava", "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(String value) {
                        Log.d("-----rxjava", "对Next事件作出响应:" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("-----rxjava", "对Error事件作出响应" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("-----rxjava", "对onComplete事件作出响应:RxJava认识完成");
                    }
                };
                //通过订阅（Subscribe）连接观察者和被观察者
                observable.subscribe(observer);

            }
        });
        //基于事件流的链式调用
        findViewById(R.id.rx_link_basic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建被观察者 & 生产事件
                //create()作用是创建被观察者对象,是最基本的操作符,只有创建
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) {
                        emitter.onNext("哈喽! Rxjava");
                        emitter.onNext("嗨! Rxjava");
                        emitter.onNext("喂! Rxjava");
                        emitter.onComplete();
                    }
                    //subscribe订阅者连接观察者和被观察者
                    //Observer 创建观察者 & 定义响应事件的行为
                }).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("-----rxjava", "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("-----rxjava", "对Next事件作出响应:" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("-----rxjava", "对Error事件作出响应" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("-----rxjava", "对onComplete事件作出响应:RxJava认识完成");
                    }
                });
            }
        });
        //通过操作符快速创建
        findViewById(R.id.rx_just).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //just()作用不仅能创建还能发射对象等会通过案例对比下
                //just 的特点:1 创建被观察者对象 2 发送事件
                Observable.just("哈喽 Rxjava", "嗨 Rxjava", "喂! Rxjava").subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("-----rxjava", "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("-----rxjava", "对Next事件作出响应:" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("-----rxjava", "对Error事件作出响应" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("-----rxjava", "对onComplete事件作出响应:RxJava认识完成");
                    }
                });
            }
        });


        //通过操作符快速创建Consumer
        findViewById(R.id.rx_just_consumer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.just("哈喽 Rxjava", "嗨 Rxjava", "喂! Rxjava").subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        Log.d("-----rxjava", s);
                    }
                });
            }
        });

        //Disposable.dispose()切断观察者 与 被观察者 之间的连接
        findViewById(R.id.rx_break_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建被观察者 & 生产事件
                //create()作用是创建被观察者对象,是最基本的操作符,只有创建
                //just()作用不能能创建还能发射对象等会通过案例对比下
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) {
                        emitter.onNext("哈喽! Rxjava");
                        emitter.onNext("嗨! Rxjava");
                        emitter.onNext("喂! Rxjava");
                        emitter.onComplete();
                    }
                    //subscribe订阅者连接观察者和被观察者
                    //Observer 创建观察者 & 定义响应事件的行为
                }).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;//对Disposable类变量赋值
                        Log.d("-----rxjava", "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("嗨! Rxjava")) {
                            mDisposable.dispose();
                        }
                        Log.d("-----rxjava", "对Next事件作出响应:" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("-----rxjava", "对Error事件作出响应" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("-----rxjava", "对onComplete事件作出响应:RxJava认识完成");
                    }
                });
            }
        });

        findViewById(R.id.rx_fromArray).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] str = {"哈喽! Rxjava", "嗨! Rxjava", "喂! Rxjava"};
                //fromArray:创建被观察者对象（Observable）时传入数组
                Observable.fromArray(str).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("-----rxjava", "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("-----rxjava", "对Next事件作出响应:" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("-----rxjava", "对Next事件作出响应" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("-----rxjava", "对onComplete事件作出响应:RxJava认识完成");
                    }
                });

            }
        });

        findViewById(R.id.rx_fromIterable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                list.add("哈喽! Rxjava");
                list.add("嗨! Rxjava");
                list.add("喂! Rxjava");
                //fromIterable:1创建被观察者2直接发送 传入的集合List数据
                Observable.fromIterable(list).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("-----rxjava", "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("-----rxjava", "对Next事件作出响应:" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("-----rxjava", "对Error事件作出响应" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("-----rxjava", "对onComplete事件作出响应:RxJava认识完成");
                    }
                });
            }
        });

        findViewById(R.id.rx_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.just(1, 2, 3, 4, 5, 6)
                        .flatMap(new Function<Integer, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Integer integer) {
                                if (integer > 3) {
//                                    return Observable.empty();
//                                    return Observable.never();
                                    return Observable.error(new RuntimeException());
                                } else {
                                    return Observable.just(integer);
                                }

                            }

                        }).subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("-----rxjava", "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d("-----rxjava", "对Next事件作出响应:" + o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("-----rxjava", "对Error事件作出响应" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("-----rxjava", "对onComplete事件作出响应");
                    }
                });
            }
        });

        findViewById(R.id.rx_defer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        findViewById(R.id.rx_timer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //timer 快速创建1个被观察者对象 延迟指定时间后发射
                Observable.timer(5, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("-----rxjava", "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d("-----rxjava", "对Next事件作出响应:" + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("-----rxjava", "对Error事件作出响应" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("-----rxjava", "对onComplete事件作出响应");
                    }
                });
            }
        });


        findViewById(R.id.rx_interval).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //interval 快速创建1个被观察者对象 指定时间发送时间
                Observable.interval(1, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                        Log.d("-----rxjava", "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (aLong == 10) {
                            mDisposable.dispose();
                        }
                        Log.d("-----rxjava", "对Next事件作出响应:" + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("-----rxjava", "对Error事件作出响应" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("-----rxjava", "对onComplete事件作出响应");
                    }
                });
            }
        });


        findViewById(R.id.rx_intervalRange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intervalRange快速创建1个被观察者对象  可指定发送的数据的数量
                Observable.intervalRange(0, 6, 2, 1, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("-----rxjava", "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d("-----rxjava", "对Next事件作出响应:" + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("-----rxjava", "对Error事件作出响应" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("-----rxjava", "对onComplete事件作出响应");
                    }
                });
            }
        });

        findViewById(R.id.rx_range).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.range(1, 10).subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("-----rxjava", "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d("-----rxjava", "对Next事件作出响应:" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("-----rxjava", "对Error事件作出响应" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("-----rxjava", "对onComplete事件作出响应");
                    }
                });
            }
        });


        findViewById(R.id.rx_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) {
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                    }
                }).map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        return "使用 Map变换操作符 将事件" + integer + "的参数从 整型" + integer + " 变换成 字符串类型" + integer;
                    }
                }).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d("-----rxjava", "开始采用subscribe连接");
                            }

                            @Override
                            public void onNext(String s) {
                                Log.d("-----rxjava", "对Next事件作出响应:" + s);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("-----rxjava", "对Error事件作出响应" + e);
                            }

                            @Override
                            public void onComplete() {
                                Log.d("-----rxjava", "对onComplete事件作出响应");
                            }
                        });
            }
        });

        findViewById(R.id.rx_debounce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              Observable.interval(5,TimeUnit.SECONDS).debounce(3,TimeUnit.SECONDS).subscribe(new Observer<Long>() {
//                  @Override
//                  public void onSubscribe(Disposable d) {
//                      Log.d("-----rxjava", "开始采用subscribe连接");
//                  }
//
//                  @Override
//                  public void onNext(Long aLong) {
//                      Log.d("-----rxjava", "对Next事件作出响应:" + aLong);
//                  }
//
//                  @Override
//                  public void onError(Throwable e) {
//                      Log.d("-----rxjava", "对Error事件作出响应" + e);
//                  }
//
//                  @Override
//                  public void onComplete() {
//                      Log.d("-----rxjava", "对onComplete事件作出响应");
//                  }
//              });


                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws InterruptedException {
//                        emitter.onNext(1);
//                        Thread.sleep(200);
//                        emitter.onNext(2);// 1 和 2 之间时间小于debounce指定时间1 被丢弃了
//                        Thread.sleep(200);
//                        emitter.onNext(3);// 2 和 3 之间时间大于debounce指定时间2 输出了
//                        Thread.sleep(200);
//                        emitter.onNext(4);// 3 和 4 之间时间小于debounce指定时间3 被丢弃了
//                        Thread.sleep(200);
//                        emitter.onNext(5);// 4 和 5 之间时间大于debounce指定时间4 输出了
//                        Thread.sleep(200);
//                        emitter.onNext(6);// 5 和 6 之间时间大于debounce指定时间5 输出了
//                        Thread.sleep(100);//这个无论怎么设置都输入 最后一个都输出 奇怪了
//                        emitter.onComplete();

                        for (int i=0;i<10;i++){
                            emitter.onNext(i);
                            Thread.sleep(i*100);
                        }

                    }
                }).debounce(5000, TimeUnit.MILLISECONDS).subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("-----rxjava", "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d("-----rxjava", "对Next事件作出响应:" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("-----rxjava", "对Error事件作出响应" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("-----rxjava", "对onComplete事件作出响应");
                    }
                });
            }
        });

    }
}
