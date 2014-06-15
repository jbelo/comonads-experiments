import rx.functions.Func1;

/**
 * Created by jbelo on 6/15/14.
 */
public abstract class Future<T> {
    public abstract <R> Future<R> bind(Func1<T, Future<R>> fn);

    public static <T> Future<T> unit(T value) {
        return new Future<T>() {
            @Override
            public <R> Future<R> bind(Func1<T, Future<R>> fn) {
                return fn.call(value);
            }
        };
    }

    public static <T, R> Future<Future<R>> fmap(Future<T> future, Func1<T, Future<R>> fn) {
        return unit(future.bind(fn));
    }

    public static <T> Future<T> join(Future<Future<T>> future) {
        return future.bind(f -> f);
    }
}
