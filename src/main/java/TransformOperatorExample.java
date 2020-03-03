import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;
import model.Shape;
import utils.CommonUtils;
import utils.Log;

import java.util.concurrent.TimeUnit;

public class TransformOperatorExample {
    public void concatMapExample() {
        CommonUtils.exampleStart();

        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> balls[idx])
                .take(balls.length)
//                .concatMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                .flatMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                    .map(notUsed -> ball + "<>")
                    .take(2)
                );
        source.subscribe(Log::it);

        CommonUtils.sleep(2000);
    }

    public void switchMapExample() {
        CommonUtils.exampleStart();

        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> balls[idx])
                .take(balls.length)
                .doOnNext(Log::it)
                .switchMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .map(notUsed -> ball + "<>")
                        .take(2)
                );
        source.subscribe(Log::it);

        CommonUtils.sleep(2000);
    }

    public void groupByExample() {
        String[] objs = {"6", "4", "2-T", "2", "6-T", "4-T"};
        Observable<GroupedObservable<String, String>> source = Observable.fromArray(objs).groupBy(CommonUtils::getShape);
        source.subscribe(obj -> {
            obj.filter(val -> obj.getKey().equals(Shape.BALL))
                    .subscribe(val -> System.out.println("GROUP: " + obj.getKey() + "\t Value: " + val));
        });
    }

    public void scanExample() {
        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.fromArray(balls)
                .scan((ball1, ball2) -> ball2 + "(" + ball1 + ")");
        source.subscribe(Log::i);
    }
}
