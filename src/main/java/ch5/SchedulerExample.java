package ch5;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import model.Shape;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.CommonUtils;
import utils.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SchedulerExample {
    public void introExample() {
        Observable.just("Hello", "Rxjava2!!")
                .subscribe(Log::i);
    }

    public void flipExample() {
        String[] objs = {"1-S", "2-T", "3-P"};
        Observable<String> source = Observable.fromArray(objs)
                .doOnNext(data -> Log.i("Original data: " + data))
                .subscribeOn(Schedulers.newThread())
//                .observeOn(Schedulers.newThread())
                .map(Shape::flip);
        source.subscribe(Log::i);
        CommonUtils.sleep(500);
    }

    public void newThreadSchedulerExample() {
        String[] orgs = {"1", "3", "5"};
        Observable.fromArray(orgs)
                .doOnNext(data -> Log.i("Original data : " + data))
                .map(data -> "<<" + data + ">>")
                .subscribeOn(Schedulers.newThread())
                .subscribe(Log::i);
//        CommonUtils.sleep(500);

        Observable.fromArray(orgs)
                .doOnNext(data -> Log.i("Original Data: " + data))
                .map(data -> "##" + data + "##")
                .subscribeOn(Schedulers.newThread())
                .subscribe(Log::i);
        CommonUtils.sleep(500);
    }

    public void computationSchedulerExample() {
        String[] orgs = {"1", "3", "5"};
        Observable<String> source = Observable.fromArray(orgs)
                .zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS), (a, b) -> a);

        source.map(item -> "<<" + item + ">>")
                .subscribeOn(Schedulers.computation())
                .subscribe(Log::i);

        source.map(item -> "##" + item + "##")
                .subscribeOn(Schedulers.computation())
                .subscribe(Log::i);
        CommonUtils.sleep(1000);
    }

    public void IOSchedulerExample() {
        String root = "/Users/juhyun/Documents/workspace/reactivejava/";
        File[] files = new File(root).listFiles();
        Observable<String> source = Observable.fromArray(files)
                .filter(f -> !f.isDirectory())
                .map(f -> f.getAbsolutePath())
                .subscribeOn(Schedulers.io());

        source.subscribe(Log::i);
        CommonUtils.sleep(1000);
    }

    public void trampolineSchedulerExample() {
        String[] orgs = {"1", "3", "5"};
        Observable<String> source = Observable.fromArray(orgs);

        // sub #1
        source.subscribeOn(Schedulers.trampoline())
                .map(data -> "<<" + data + ">>")
                .subscribe(Log::i);

        // sub #2
        source.subscribeOn(Schedulers.trampoline())
                .map(data -> "##" + data + "##")
                .subscribe(Log::i);
        CommonUtils.sleep(1000);
    }

    public void singleThreadSchedulerExample() {
        Observable<Integer> numbers = Observable.range(100, 5);
        Observable<String> chars = Observable.range(0, 5)
                .map(CommonUtils::numberToAlphabet);

        numbers.subscribeOn(Schedulers.single())
                .subscribe(Log::i);
        chars.subscribeOn(Schedulers.single())
                .subscribe(Log::i);
        CommonUtils.sleep(1000);
    }

    public void executorSchedulerExample() {
        final int THREAD_NUM = 10;

        String[] data = {"1", "3", "5"};
        Observable<String> source = Observable.fromArray(data);
        Executor executor = Executors.newFixedThreadPool(THREAD_NUM);

        source.subscribeOn(Schedulers.from(executor))
                .subscribe(Log::i);
        source.subscribeOn(Schedulers.from(executor))
                .subscribe(Log::i);
        CommonUtils.sleep(1000);
    }
}
