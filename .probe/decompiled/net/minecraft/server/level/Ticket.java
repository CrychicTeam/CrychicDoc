package net.minecraft.server.level;

import java.util.Objects;

public final class Ticket<T> implements Comparable<Ticket<?>> {

    private final TicketType<T> type;

    private final int ticketLevel;

    private final T key;

    private long createdTick;

    protected Ticket(TicketType<T> ticketTypeT0, int int1, T t2) {
        this.type = ticketTypeT0;
        this.ticketLevel = int1;
        this.key = t2;
    }

    public int compareTo(Ticket<?> ticket0) {
        int $$1 = Integer.compare(this.ticketLevel, ticket0.ticketLevel);
        if ($$1 != 0) {
            return $$1;
        } else {
            int $$2 = Integer.compare(System.identityHashCode(this.type), System.identityHashCode(ticket0.type));
            return $$2 != 0 ? $$2 : this.type.getComparator().compare(this.key, ticket0.key);
        }
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return !(object0 instanceof Ticket<?> $$1) ? false : this.ticketLevel == $$1.ticketLevel && Objects.equals(this.type, $$1.type) && Objects.equals(this.key, $$1.key);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.type, this.ticketLevel, this.key });
    }

    public String toString() {
        return "Ticket[" + this.type + " " + this.ticketLevel + " (" + this.key + ")] at " + this.createdTick;
    }

    public TicketType<T> getType() {
        return this.type;
    }

    public int getTicketLevel() {
        return this.ticketLevel;
    }

    protected void setCreatedTick(long long0) {
        this.createdTick = long0;
    }

    protected boolean timedOut(long long0) {
        long $$1 = this.type.timeout();
        return $$1 != 0L && long0 - this.createdTick > $$1;
    }
}