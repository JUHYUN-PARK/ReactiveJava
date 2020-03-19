package ch5;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import model.Shape;
import utils.CommonUtils;
import utils.Log;

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
}
