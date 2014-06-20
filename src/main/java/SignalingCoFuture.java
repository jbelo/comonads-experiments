import rx.functions.Func1;

/**
 * Created by jbelo on 6/16/2014.
 */
public abstract class SignalingCoFuture<T> {
    public abstract T extract();

    public abstract void register(SignalHandler signalHandler);

    public <R> SignalingCoFuture<R> extend(Func1<SignalingCoFuture<T>, R> fn) {
        return new SignalingCoFuture<R>() {
            @Override
            public R extract() {
                return fn.call(SignalingCoFuture.this);
            }

            @Override
            public void register(SignalHandler signalHandler) {
                SignalingCoFuture.this.register(signalHandler);
            }
        };
    }
}
