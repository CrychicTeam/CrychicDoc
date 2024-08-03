package net.minecraftforge.common.ticket;

import com.google.common.base.Preconditions;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleTicket<T> {

    @Nullable
    private ITicketManager<T> masterManager;

    private ITicketManager<T>[] dummyManagers;

    protected boolean isValid = false;

    @SafeVarargs
    public final void setManager(@NotNull ITicketManager<T> masterManager, @NotNull ITicketManager<T>... dummyManagers) {
        Preconditions.checkState(this.masterManager == null, "Ticket is already registered to a managing system");
        this.masterManager = masterManager;
        this.dummyManagers = dummyManagers;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void invalidate() {
        if (this.isValid()) {
            this.forEachManager(ticketManager -> ticketManager.remove(this));
        }
        this.isValid = false;
    }

    public boolean unload(ITicketManager<T> unloadingManager) {
        if (unloadingManager != this.masterManager) {
            return false;
        } else {
            for (ITicketManager<T> manager : this.dummyManagers) {
                manager.remove(this);
            }
            this.isValid = false;
            return true;
        }
    }

    public void validate() {
        if (!this.isValid()) {
            this.forEachManager(ticketManager -> ticketManager.add(this));
        }
        this.isValid = true;
    }

    public abstract boolean matches(T var1);

    protected final void forEachManager(Consumer<ITicketManager<T>> consumer) {
        Preconditions.checkState(this.masterManager != null, "Ticket is not registered to a managing system");
        consumer.accept(this.masterManager);
        for (ITicketManager<T> manager : this.dummyManagers) {
            consumer.accept(manager);
        }
    }

    protected final ITicketManager<T> getMasterManager() {
        return this.masterManager;
    }

    protected final ITicketManager<T>[] getDummyManagers() {
        return this.dummyManagers;
    }
}