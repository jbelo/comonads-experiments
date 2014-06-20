import rx.functions.Func1;

/**
 * Created by jbelo on 6/15/14.
 */
public abstract class SchedulableCoFuture<T> {
    /* Schedule for side effects
     *
     * NOTE:
     *  i) the continuation may be formulated as an Observer/Handler instead, to
     *     the same effect, since neither the argument nor the result value are
     *     being used.
     *  ii)the evaluation of should be performed only once, and not call extract
     *     every time.
     */
    public abstract <R> void schedule(Func1<SchedulableCoFuture<T>, R> continuation);

    // CoMonad structure
    public abstract T extract();

    public <R> SchedulableCoFuture<R> extend(Func1<SchedulableCoFuture<T>, R> fn) {
        return new SchedulableCoFuture<R>() {
            @Override
            public R extract() {
                return fn.call(SchedulableCoFuture.this);
            }

            @Override
            public <S> void schedule(Func1<SchedulableCoFuture<R>, S> continuation) {
                SchedulableCoFuture.this.schedule(prev -> {
                    R intermediate = fn.call(prev);
                    return continuation.call(this);
                });
            }
        };
    }
}
