package me.lucko.spark.common.command.sender;

public abstract class AbstractCommandSender<S> implements CommandSender {

    protected final S delegate;

    public AbstractCommandSender(S delegate) {
        this.delegate = delegate;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbstractCommandSender<?> that = (AbstractCommandSender<?>) o;
            return this.delegate.equals(that.delegate);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }
}