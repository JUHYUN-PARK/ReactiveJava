import io.reactivex.Observable;
import model.Shape;
import org.apache.commons.lang3.tuple.Pair;
import utils.CommonUtils;
import utils.Log;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import static java.lang.Double.max;
import static java.lang.Double.min;

public class CombineOperatorExample {
    public void zipExample() {
        String[] shapes = {"BALL", "PENTAGON", "STAR"};
        String[] coloredTriangles = {"2-T", "6-T", "4-T"};

        Observable<String> source = Observable.zip(
                Observable.fromArray(shapes).map(Shape::getSuffix),
                Observable.fromArray(coloredTriangles).map(Shape::getColor),
                (suffix, color) -> color + suffix
        );

        source.subscribe(Log::i);
    }

    public void zipExample2() {
        Observable<Integer> source = Observable.zip(
                Observable.just(100, 200, 300),
                Observable.just(10, 20, 30),
                Observable.just(1, 2, 3),
                (a, b, c) -> a + b + c
        );

        source.subscribe(Log::i);
    }

    public void zipIntervalExample() {
        Observable<String> source = Observable.zip(
                Observable.just("RED", "GREEN", "BLUE"),
                Observable.interval(200L, TimeUnit.MILLISECONDS),
                (value, i) -> value
        );

        CommonUtils.exampleStart();
        source.subscribe(Log::it);
        CommonUtils.sleep(1000);
    }

    private int index = 0;

    public void billsExample() {
        String[] data = {
                "100",
                "300"
        };

        Observable<Integer> basePrice = Observable.fromArray(data)
                .map(Integer::parseInt)
                .map(val -> {
                            if(val <= 200) return 910;
                            if(val <= 400) return 1600;
                            return 7300;
                        }
                );

        Observable<Integer> usagePrice = Observable.fromArray(data)
                .map(Integer::parseInt)
                .map(val -> {
                    double series1 = min(200, val) * 93.3;
                            double series2 = min(200, max(val-200, 0)) * 187.9;
                            double series3 = min(0, max(val-400, 0)) * 280.65;
                            return (int)(series1 + series2 + series3);
                        }
                );

        Observable<Integer> source = Observable.zip(
                basePrice,
                usagePrice,
                (v1, v2) -> v1 + v2
        );

        source.map(val -> new DecimalFormat("#,###").format(val))
                .subscribe(val -> {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Usage: " + data[index] + " kWh => ");
                            sb.append("Price: " + val + "Won");
                            Log.i(sb.toString());

                            index++;    // 멤버변수를 사용하기 때문에 사이드 이펙트 발생 가능. zip() 함수를 통해 해결 가능
                        }
                );
    }

    public void billsExample2() {
        String[] data = {
                "100",
                "300"
        };

        Observable<Integer> basePrice = Observable.fromArray(data)
                .map(Integer::parseInt)
                .map(val -> {
                            if(val <= 200) return 910;
                            if(val <= 400) return 1600;
                            return 7300;
                        }
                );

        Observable<Integer> usagePrice = Observable.fromArray(data)
                .map(Integer::parseInt)
                .map(val -> {
                            double series1 = min(200, val) * 93.3;
                            double series2 = min(200, max(val-200, 0)) * 187.9;
                            double series3 = min(0, max(val-400, 0)) * 280.65;
                            return (int)(series1 + series2 + series3);
                        }
                );

        Observable<Pair<String, Integer>> source = Observable.zip(
                basePrice,
                usagePrice,
                Observable.fromArray(data),
                (v1, v2, i) -> Pair.of(i, v1 + v2)
        );

        source.map(val -> Pair.of(val.getLeft(),
                new DecimalFormat("#,###").format(val.getValue())))
                .subscribe(val -> {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Usage: " + val.getLeft() + "kWh => ");
                            sb.append("Price: " + val.getRight() + "Won");
                            Log.i(sb.toString());
                        }
                );
    }

    public void zipWithExample() {
        Observable<Integer> source = Observable.zip(
                Observable.just(100, 200, 300),
                Observable.just(10, 20, 30),
                (a, b) -> a + b)
                .zipWith(Observable.just(1, 2, 3), (ab, c) -> ab + c
                );
        source.subscribe(Log::i);
    }

    public void combineLastestExample() {
        String[] data1 = {"6", "7", "4", "2"};
        String[] data2 = {"DIAMOND", "STAR", "PENTAGON"};

        Observable<String> source = Observable.combineLatest(
                Observable.fromArray(data1)
                        .zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS),
                                (shape, notUsed) -> Shape.getColor(shape)),
                Observable.fromArray(data2)
                        .zipWith(Observable.interval(150L, 200L, TimeUnit.MILLISECONDS),
                                (shape, notUsed) -> Shape.getSuffix(shape)), (v1, v2) -> v1 + v2
        );

        source.subscribe(Log::i);
        CommonUtils.sleep(1000);
    }
}
