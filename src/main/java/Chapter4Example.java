import io.reactivex.Observable;
import model.Shape;
import network.OkHttpHelper;
import utils.CommonUtils;
import utils.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Chapter4Example {
    public void intervalExample() {
        Observable<Long> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(data -> (data + 1) * 100)
                .take(5);
        source.subscribe(System.out::println);
        try {
            // interval은 별도의 쓰레드에서 동작하기 때문에, 계산결과를 가져올때 까지 기다려줘야함
            // 기다리지 않을 경우 메인쓰레드는 추가작업이 없으므로 그대로 종료되고 결과값 확인 불가
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void intervalExample2() {
        CommonUtils.exampleStart();
        Observable<Long> source = Observable.interval(0L, 100L, TimeUnit.MILLISECONDS)
                .map(data -> data + 100)
                .take(5);
        source.subscribe(Log::it);
        CommonUtils.sleep(1000);
    }

    public void timerExample() {
        CommonUtils.exampleStart();
        Observable<String> source = Observable.timer(500L, TimeUnit.MILLISECONDS)
                .map(notUsed -> {
                    return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                            .format(new Date());
                });
        source.subscribe(Log::it);
        CommonUtils.sleep(1000);
    }

    public void rangeExample() {
        Observable<Integer> source = Observable.range(1, 10)
                .filter(number -> number % 2 == 0);
        source.subscribe(Log::it);
    }

    public void intervalRangeExample() {
        Observable<Long> source = Observable.intervalRange(1, 5, 100L, 100L, TimeUnit.MILLISECONDS);
        source.subscribe(Log::i);
        CommonUtils.sleep(1000);
    }

    public void intervalRaangeExample2() {
        CommonUtils.exampleStart();
        Observable<Long> source = Observable.interval(0L, 100L, TimeUnit.MILLISECONDS)
                .map(data -> data + 1)
                .take(5);
        source.subscribe(Log::it);
        CommonUtils.sleep(1000);
    }

    Iterator<String> colors = Arrays.asList("1", "3", "5", "6").iterator();

    public void marbleDiagram() {
        Callable<Observable<String>> supplier = () -> getObservable();
//        Observable<String> source = Observable.defer(supplier);
        Observable<String> source = getObservable();
        source.subscribe(val -> Log.i("Subscriber #1: " + val));
        source.subscribe(val -> Log.i("Subscriber #2: " + val));
    }

    public void repeatExample() {
        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.fromArray(balls)
                .repeat(3);

        source.doOnComplete(() -> System.out.println("onComplete"))
                .subscribe(Log::i);
    }

    private Observable<String> getObservable() {
        if(colors.hasNext()) {
            String color = colors.next();
            return Observable.just(Shape.getString(color, Shape.BALL),
                    Shape.getString(color, Shape.RECTANGLE),
                    Shape.getString(color, Shape.PENTAGON));
        }
        return Observable.empty();
    }

    /**
     * pingExample의 경우 timer, repeat을 사용하여 작성
     * timer는 1회만 호출하는 함수지만, repeat을 이용해 무한반복 실행
     * repeat은 subscribe를 반복하는 함수이므로 Computation Thread가 매 반복마다 다름
     */
    public void pingExample() {
        CommonUtils.exampleStart();
        String serverUrl = "https://api.github.com/zen";

        Observable.timer(2, TimeUnit.SECONDS)
                .map(val -> serverUrl)
                .map(OkHttpHelper::get)
                .repeat()
                .subscribe(res -> Log.it("Ping Result: " + res));

        CommonUtils.sleep(10000);
    }

    /**
     * pingExample2는 interval을 사용하여 작성
     * interval은 동일한 Computation Thread에서 작업을 반복함
     */
    public void pingExample2() {
        CommonUtils.exampleStart();
        String serverUrl = "https://api.github.com/zen";

        Observable.interval(1, TimeUnit.SECONDS)
                .map(val -> serverUrl)
                .map(OkHttpHelper::get)
                .subscribe(res -> Log.it("Ping Result: " + res));

        CommonUtils.sleep(10000);
    }
}
