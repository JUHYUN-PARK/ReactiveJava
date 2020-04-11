package ch7;

import io.reactivex.Observable;
import utils.Log;

/**
 * Rxjava는 try-catch를 통해 에러 핸들링 불가
 * Rxjava에서 제공하는 에러핸들링 함수를 통해 에러핸들링 가능
 */
public class ErrorHandlingExample {

    /**
     * onError의 경우 데이터 흐름을 중단시키기 때문에
     * OutOfMemory등 프로그램을 중단 시키는 등 중대한 영향이 있는 경우에만 이벤트 처리
     *
     * onErrorReturn은 에러가 발생했을 때, 내가 원하는 데이터로 대체해주는 함수
     *
     * onError 대비 onErrorReturn 이용의 장점?
     * 1. 예외 발생이 예상되는 지점에 부분적 예외처리가 가능함
     * 2. Observable을 구독 / 발행하는 주체가 서로 다를 수 있음
     *    -> 구독자가 구독한 Observable에서 발생가능한 모든 에러를 직접 예외처리 해주는데 한계가 있음
     *       이를 위해 Observable에서 선언적 예외처리를 해두면 구독자는 그것에 따른 예외 처리만 생각하면 된다
     */
    public void onErrorReturnExample() {
        String[] grades = {"70", "88", "$100", "93", "83"};

        Observable<Integer> source = Observable.fromArray(grades)
                .map(data -> Integer.parseInt(data))
                .onErrorReturn(e -> {
                    if(e instanceof NumberFormatException) {
                        e.printStackTrace();
                    }
                    return -1;
                });

        source.subscribe(data -> {
            if(data < 0) {
                Log.e("Wrong Data found!!");
                return;
            }

            Log.i("Grade is " + data);
        });
    }

    public void onErrorExample() {
        String[] grades = {"70", "88", "$100", "93", "83"};
        Observable<Integer> source = Observable.fromArray(grades)
                .map(data -> Integer.parseInt(data));

        source.subscribe(
                data -> Log.i("Gradie is " + data),
                e -> {
                    if(e instanceof NumberFormatException) {
                        e.printStackTrace();
                    }
                    Log.d("Wrong Data Type...");
                }
        );
    }
}
