package de.keksuccino.fancymenu.util.event.acara;

public abstract class EventBase {

    private boolean canceled = false;

    public abstract boolean isCancelable();

    public void setCanceled(boolean b) {
        try {
            if (!this.isCancelable()) {
                throw new EventCancellationException("[ACARA] Tried to cancel non-cancelable event: " + this.getClass().getName());
            }
            this.canceled = b;
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public boolean isCanceled() {
        return this.canceled;
    }
}