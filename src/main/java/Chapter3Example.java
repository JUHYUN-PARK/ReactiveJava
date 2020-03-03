import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import model.Pair;
import utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Chapter3Example {
    public void mapExample() {
        String[] balls = {"1", "2", "3", "5"};
        Observable<String> source = Observable.fromArray(balls)
                .map(ball -> ball + "<>");
        source.subscribe(System.out::println);
    }

    public void mapExample2() {
        Function<String, Integer> balltoIndex = ball -> {
            switch (ball) {
                case "RED": return 1;
                case "YELLOW": return 2;
                case "GREEN": return 3;
                case "BLUE": return 4;
                default: return-1;
            }
        };

        String[] balls = {"RED", "GREEN", "YELLOW", "BLUE"};
        Observable<Integer> source = Observable.fromArray(balls)
                .map(balltoIndex);
        source.subscribe(System.out::println);
    }

    public void flatMapExample() {
        Function<String, Observable<String>> getDoubleDiamonds = ball -> Observable.just(ball + "<>", ball + "<>");
        String[] balls = {"1", "3", "5"};

        Observable<String> source = Observable.fromArray(balls)
                .flatMap(getDoubleDiamonds);
        source.subscribe(System.out::println);
    }

    public void flatMapLambdaExample() {
        String[] balls = {"1", "3", "5"};

        Observable<String> source = Observable.fromArray(balls)
                .flatMap(ball -> Observable.just(ball + "<>", ball + "<>"));
        source.subscribe(System.out::println);
    }

    public void standardGuGudan() {
        Scanner in = new Scanner(System.in);
        System.out.println("standard GuGudan\nEnter input: ");
        int dan = Integer.parseInt(in.nextLine());

        for (int row = 1; row <=9; ++row) {
            System.out.println(dan + "*" + row + " = " + dan*row);
        }
    }

    public void rxGugudan() {
        Scanner in = new Scanner(System.in);
        System.out.println("RxJava style Gugudan\n Enter input: ");
        int dan = Integer.parseInt(in.nextLine());

        Observable<Integer> source = Observable.range(1, 9);
        source.subscribe(row -> System.out.println(dan + "*" + row + "=" + dan*row));
    }

    public void rxGugudan2() {
        Scanner in = new Scanner(System.in);
        System.out.println("rxGugudan2 input: ");
        int dan = Integer.parseInt(in.nextLine());

        Function<Integer, Observable<String>> gugudan = num ->
                Observable.range(1,9).map(row -> num + "*" + row + "=" + dan*row);
        Observable<String> source = Observable.just(dan).flatMap(gugudan);
        source.subscribe(System.out::println);
    }

    public void rxGugudan3() {
        Scanner in = new Scanner(System.in);
        System.out.println("rxGugudan3 input: ");
        int dan = Integer.parseInt(in.nextLine());

        Observable<String> source = Observable.just(dan)
                .flatMap(gugu -> Observable.range(1, 9), (gugu, i) -> gugu + "*" + i + "=" + gugu*i);
        source.subscribe(System.out::println);
    }

    public void filterExample() {
        String[] objs = {"1 CIRCLE", "2 DIAMOND", "3 TRIANGLE", "4 DIAMOND", "5 CIRCLE", "6 HEXAGON"};
        // 람다 표현식을 별도의 Predicate로 정의하여 사용할 시 예제
        Predicate<String> filterCircle = obj -> obj.endsWith("CIRCLE");

        Observable<String> source = Observable.fromArray(objs)
                .filter(obj -> obj.endsWith("CIRCLE"));
        source.subscribe(System.out::println);
    }

    public void filterExample2() {
        Integer[] numbers = {100, 200, 300, 400, 500};
        Single<Integer> single;
        Observable<Integer> source;

        // 1. first
        single = Observable.fromArray(numbers).first(-1);
        single.subscribe(data -> System.out.println("first() value = " + data));

        // 2. last
        single = Observable.fromArray(numbers).last(-1);
        single.subscribe(data -> System.out.println("last() value = " + data));

        // 3. take N
        source = Observable.fromArray(numbers).take(3);
        source.subscribe(data -> System.out.println("take 3 value = " + data));

        // 4. takeLast N
        source = Observable.fromArray(numbers).takeLast(3);
        source.subscribe(data -> System.out.println("takeLast 3 value = " + data));

        // 5. skip N
        source = Observable.fromArray(numbers).skip(3);
        source.subscribe(data -> System.out.println("skip 3 value = " + data));

        // 6. skipLast N
        source = Observable.fromArray(numbers).skipLast(4);
        source.subscribe(data -> System.out.println("skipLast 4 value = " + data));
    }

    public void reduceExample() {
        String[] balls = {"1", "3", "5"};
        // 람다 표현식을 별도의 함수로 분리한 예제
        BiFunction<String, String, String> mergeBalls = (ball1, ball2) -> ball2 + "(" + ball1 + ")";

        Maybe<String> source = Observable.fromArray(balls)
                .reduce((ball1, ball2) -> ball2 + "(" + ball1 + ")");
        source.subscribe(Log::i);
    }

    public void simpleDataQueryExample() {
        List<Pair<String, Integer>> sales = new ArrayList<>();

        sales.add(new Pair("TV", 2500));
        sales.add(new Pair("Camera", 300));
        sales.add(new Pair("TV", 1600));
        sales.add(new Pair("Phone", 800));

        Maybe<Integer> tvSales = Observable.fromIterable(sales)
                .filter(sale -> "TV".equals(sale.getName()))
                .map(sale -> sale.getPrice())
                .reduce((sale1, sale2) -> sale1 + sale2);

        tvSales.subscribe(tot -> System.out.println("total amount: " + tot));
    }
}
