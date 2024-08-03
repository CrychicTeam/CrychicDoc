package org.violetmoon.zeta.event.bus;

public interface Cancellable {

    boolean isCanceled();

    void setCanceled(boolean var1);

    default void cancel() {
        this.setCanceled(true);
    }
}