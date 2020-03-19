package ch4;

import hu.akarnokd.rxjava2.math.MathFlowable;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Timed;
import org.intellij.lang.annotations.Flow;
import utils.CommonUtils;
import utils.Log;

import java.util.concurrent.TimeUnit;

public class MathOperatorExample {
    public void mathFunctionExample() {
        Integer[] data = {1,2,3,4};

        // count
        Single<Long> source = Observable.fromArray(data)
                .count();
        source.subscribe(count -> Log.i("count is " + count));

        // max, min
        Flowable.fromArray(data)
                .to(MathFlowable::max)
                .subscribe(max -> Log.i("max is " + max));

        Flowable.fromArray(data)
                .to(MathFlowable::min)
                .subscribe(min -> Log.i("min is " + min));

        // sum, average
        Flowable<Integer> flowable = Flowable.fromArray(data)
                .to(MathFlowable::sumInt);
        flowable.subscribe(sum -> Log.i("Sum is " + sum));

        Flowable<Double> flowable2 = Observable.fromArray(data)
                .toFlowable(BackpressureStrategy.BUFFER)
                .to(MathFlowable::averageDouble);
        flowable2.subscribe(avg -> Log.i("average is " + avg));
    }

    public void delayExample() {
        CommonUtils.exampleStart();
        String[] data = {"1", "7", "2", "3", "4"};
        Observable<String> source = Observable.fromArray(data)
                .delay(100L, TimeUnit.MILLISECONDS);

        Observable<String> source2 = Observable.fromArray(data);

        source.subscribe(Log::it);
        source2.subscribe(Log::it);


        CommonUtils.sleep(1000);
    }

    public void timeIntervalExample() {
        String[] data = {"1", "3", "7"};

        CommonUtils.exampleStart();
        Observable<Timed<String>> source = Observable.fromArray(data)
                .delay(item -> {
                    CommonUtils.doSomething();
                    return Observable.just(item);
                })
                .timeInterval();

        source.subscribe(Log::it);
        CommonUtils.sleep(1000);
    }
}
