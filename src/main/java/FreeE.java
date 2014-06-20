import rx.functions.Func1;

/**
 * Created by jbelo on 6/20/2014.
 */
public abstract class FreeE<T, M extends Monad<T>> {
    public abstract T fold(Func1<Monad<T>, T> fn);

    public static <T, M extends Monad<T>> FreeE<T, Monad<T>> unit(T value) {
        return new FreeE<T, Monad<T>>() {
            @Override
            public T fold(Func1<Monad<T>, T> fn) {
                return value;
            }
        };
    }

    public <R> FreeE<R, Monad<R>> bind(Func1<T, Monad<R>> fn) {
        return new FreeE<R, Monad<R>>() {
            @Override
            public R fold(Func1<Monad<R>, R> hn) {
                return hn.call(FreeE.this.fold(m -> m.bind(fn)));
            }
        };
    }
}
