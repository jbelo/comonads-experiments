import rx.functions.Func1;

/**
 * Created by jbelo on 6/15/14.
 */
public abstract class CoFuture<T> {
    public abstract T extract();

    public <R> CoFuture<R> extend(Func1<CoFuture<T>, R> fn) {
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
}
