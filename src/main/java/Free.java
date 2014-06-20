import rx.functions.Func1;

/**
 * Created by jbelo on 6/20/2014.
 */
public abstract class Free<T> {
    public abstract T eval();

    public static <T> Free<T> unit(T value) {
        return new Free<T>() {
            @Override
            public T eval() {
                return value;
            }
        };
    }

    public <R> Free<R> bind(Func1<T, Free<R>> fn) {
        return new Free<R>() {
            @Override
            public R eval() {
                return fn.call(Free.this.eval()).eval();
            }
        };
    }
}
