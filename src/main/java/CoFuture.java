import rx.functions.Func1;

import java.util.concurrent.Callable;

/**
 * Created by jbelo on 6/15/14.
 *
 * When should the computation of the extractors be performed (for side effects)?
 */
public abstract class CoFuture<T> {
    public abstract T extract();

    public <R> CoFuture<R> extend(Func1<CoFuture<T>, R> fn) {
        return new CoFuture<R>() {
            @Override
            public R extract() {
                return fn.call(CoFuture.this);
            }
        };
    }

    public <R> CoFuture<R> extendDef(Func1<CoFuture<T>, R> fn) {
        return fmap(duplicate(this), fn);
    }

    public static <T> CoFuture<CoFuture<T>> duplicate(CoFuture<T> coFuture) {
        return new CoFuture<CoFuture<T>>() {
            @Override
            public CoFuture<T> extract() {
                return coFuture;
            }
        };
    }

    public static <T, R> CoFuture<R> fmap(final CoFuture<CoFuture<T>> coFuture, Func1<CoFuture<T>, R> fn) {
        return new CoFuture<R>() {
            @Override
            public R extract() {
                return fn.call(coFuture.extract());
            }
        };
    }

    public static void main(String[] args) {
        CoFuture<Integer> one = new CoFuture<Integer>() {
            @Override
            public Integer extract() {
                return 1;
            }
        };

        CoFuture<Integer> two = one.extend(future -> future.extract() * 2);

        CoFuture<Integer> delayed1 = two.extend(future -> {
            Integer value = future.extract();
            try {
                System.out.println("Sleeping for 1 sec...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return value;
        });

        CoFuture<Integer> delayed2 = delayed1.extend(future -> {
            Integer value = future.extract();
            try {
                System.out.println("Sleeping for 2 sec...");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return value;
        });

        System.out.println(delayed2.extract());
    }
}
