import rx.functions.Func1;

/**
 * Created by jbelo on 6/20/2014.
 */
public interface Monad<T> {
    <R> Monad<R> bind(Func1<T, Monad<R>> fn);
}
