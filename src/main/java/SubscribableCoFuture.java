import rx.functions.Func1;

/**
 * Created by jbelo on 6/16/2014.
 */
public abstract class SubscribableCoFuture<T> {
    public abstract T extract();

    public abstract void subscribe(Obsv<T> obsv);

    public <R> SubscribableCoFuture<R> extend(Func1<SubscribableCoFuture<T>, R> fn) {
        return new SubscribableCoFuture<R>() {
            private boolean isDone = false;
            private R value = null;

            private R evaluate() {
                if (!isDone) {
                    this.value = fn.call(SubscribableCoFuture.this);
                    this.isDone = true;
                }
                return value;
            }

            @Override
            public R extract() {
                return evaluate();
            }

            @Override
            public void subscribe(Obsv<R> obsv) {
                SubscribableCoFuture.this.subscribe(new Obsv<T>() {
                    @Override
                    public void onDone() {
                        evaluate();
                        obsv.onDone();
                    }
                });
            }
        };
    }
}
