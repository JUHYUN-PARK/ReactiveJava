package ch7;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import utils.CommonUtils;
import utils.Log;

import java.util.concurrent.TimeUnit;

/**
 * DoOnXXX Method
 * doOnNext()
 * 데이터 발행할때 발생
 *
 * doOnComplete()
 * 모든 데이터를 발행하면 발생
 *
 * doOnError()
 * doOnComplete() 전 Error가 발생한 경우 발생
 */
public class DoOnMethodExample {
    public void simpleExample() {
        String[] orgs = {"1", "3", "5"};
        Observable<String> source = Observable.fromArray(orgs);

        source.doOnNext(data -> Log.d("onNext()", data))
                .doOnComplete(() -> Log.d("onComplete()"))
                .doOnError(e -> Log.e("onError()", e.getMessage()))
                .subscribe(Log::i);
    }

    public void simpleErrorExample() {
        Integer[] divider = {10, 5, 0};
        Observable.fromArray(divider)
                .map(div -> 1000/div)
                .doOnNext(data -> Log.d("onNext", data))
                .doOnComplete(() -> Log.d("onComplete"))
                .doOnError(e -> Log.e("onError()", e.getMessage()))
                .subscribe(Log::i);
    }

    /**
     * doOnEach
     * onNext, onComplete, onError 이벤트를 한번에 처리하는 함수
     */
    public void doOnEachExample() {
        String[] data = {"ONE", "TWO", "THREE"};
        Observable<String> source = Observable.fromArray(data);
        source.doOnEach(noti -> {
           if(noti.isOnNext()) Log.d("onNext()", noti.getValue());
            if(noti.isOnComplete()) Log.d("OnComplete()");
            if(noti.isOnError()) Log.d("OnError()", noti.getError().getMessage());
        }).subscribe(Log::i);
    }

    /**
     * doOnEach 대신 Observer 인터페이스를 이용해 각 이벤트에 대한 코드를 정의
     */
    public void similarDoOnEachExample() {
        String[] data = {"ONE", "TWO", "THREE"};
        Observable<String> source = Observable.fromArray(data);
        source.doOnEach(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                //doOnEach에서는 onSubscribe() 함수 호출 안함
            }

            @Override
            public void onNext(String s) {
                Log.d("onNext()", s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("onError()", e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("onComplete()");
            }
        }).subscribe(Log::i);
    }

    public void doOnSubDispExample() {
        String[] orgs = {"1", "3", "5", "2", "6"};
        Observable<String> source = Observable.fromArray(orgs)
                .zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS), (a, b) -> a)
                .doOnSubscribe(d -> Log.d("onSubscribe()"))
                .doOnDispose(() -> Log.d("onDispose()"));
        Disposable d = source.subscribe(Log::i);

        CommonUtils.sleep(200);
        d.dispose();
        CommonUtils.sleep(300);
    }

    /**
     * doOnEach와 유사하게 doOnSubscribe, doOnDispose를 한번에 처리하는 함수
     */
    public void doOnLifecycleExample() {
        String[] orgs = {"1", "3", "5", "2", "6"};
        Observable<String> source = Observable.fromArray(orgs)
                .zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS), (a, b) -> a)
                .doOnLifecycle(
                        d -> Log.d("onSubscribe()"), () -> Log.d("onDispose()")
                );
        Disposable d = source.subscribe(Log::i);

        CommonUtils.sleep(200);
        d.dispose();
        CommonUtils.sleep(300);
    }
}
