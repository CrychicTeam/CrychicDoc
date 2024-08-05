package net.minecraft.server.level;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.SortedArraySet;
import net.minecraft.world.level.ChunkPos;

public class TickingTracker extends ChunkTracker {

    public static final int MAX_LEVEL = 33;

    private static final int INITIAL_TICKET_LIST_CAPACITY = 4;

    protected final Long2ByteMap chunks = new Long2ByteOpenHashMap();

    private final Long2ObjectOpenHashMap<SortedArraySet<Ticket<?>>> tickets = new Long2ObjectOpenHashMap();

    public TickingTracker() {
        super(34, 16, 256);
        this.chunks.defaultReturnValue((byte) 33);
    }

    private SortedArraySet<Ticket<?>> getTickets(long long0) {
        return (SortedArraySet<Ticket<?>>) this.tickets.computeIfAbsent(long0, p_184180_ -> SortedArraySet.create(4));
    }

    private int getTicketLevelAt(SortedArraySet<Ticket<?>> sortedArraySetTicket0) {
        return sortedArraySetTicket0.isEmpty() ? 34 : sortedArraySetTicket0.first().getTicketLevel();
    }

    public void addTicket(long long0, Ticket<?> ticket1) {
        SortedArraySet<Ticket<?>> $$2 = this.getTickets(long0);
        int $$3 = this.getTicketLevelAt($$2);
        $$2.add(ticket1);
        if (ticket1.getTicketLevel() < $$3) {
            this.m_140715_(long0, ticket1.getTicketLevel(), true);
        }
    }

    public void removeTicket(long long0, Ticket<?> ticket1) {
        SortedArraySet<Ticket<?>> $$2 = this.getTickets(long0);
        $$2.remove(ticket1);
        if ($$2.isEmpty()) {
            this.tickets.remove(long0);
        }
        this.m_140715_(long0, this.getTicketLevelAt($$2), false);
    }

    public <T> void addTicket(TicketType<T> ticketTypeT0, ChunkPos chunkPos1, int int2, T t3) {
        this.addTicket(chunkPos1.toLong(), new Ticket<>(ticketTypeT0, int2, t3));
    }

    public <T> void removeTicket(TicketType<T> ticketTypeT0, ChunkPos chunkPos1, int int2, T t3) {
        Ticket<T> $$4 = new Ticket<>(ticketTypeT0, int2, t3);
        this.removeTicket(chunkPos1.toLong(), $$4);
    }

    public void replacePlayerTicketsLevel(int int0) {
        List<Pair<Ticket<ChunkPos>, Long>> $$1 = new ArrayList();
        ObjectIterator var3 = this.tickets.long2ObjectEntrySet().iterator();
        while (var3.hasNext()) {
            Entry<SortedArraySet<Ticket<?>>> $$2 = (Entry<SortedArraySet<Ticket<?>>>) var3.next();
            for (Ticket<?> $$3 : (SortedArraySet) $$2.getValue()) {
                if ($$3.getType() == TicketType.PLAYER) {
                    $$1.add(Pair.of($$3, $$2.getLongKey()));
                }
            }
        }
        for (Pair<Ticket<ChunkPos>, Long> $$4 : $$1) {
            Long $$5 = (Long) $$4.getSecond();
            Ticket<ChunkPos> $$6 = (Ticket<ChunkPos>) $$4.getFirst();
            this.removeTicket($$5, $$6);
            ChunkPos $$7 = new ChunkPos($$5);
            TicketType<ChunkPos> $$8 = $$6.getType();
            this.addTicket($$8, $$7, int0, $$7);
        }
    }

    @Override
    protected int getLevelFromSource(long long0) {
        SortedArraySet<Ticket<?>> $$1 = (SortedArraySet<Ticket<?>>) this.tickets.get(long0);
        return $$1 != null && !$$1.isEmpty() ? $$1.first().getTicketLevel() : Integer.MAX_VALUE;
    }

    public int getLevel(ChunkPos chunkPos0) {
        return this.getLevel(chunkPos0.toLong());
    }

    @Override
    protected int getLevel(long long0) {
        return this.chunks.get(long0);
    }

    @Override
    protected void setLevel(long long0, int int1) {
        if (int1 > 33) {
            this.chunks.remove(long0);
        } else {
            this.chunks.put(long0, (byte) int1);
        }
    }

    public void runAllUpdates() {
        this.m_75588_(Integer.MAX_VALUE);
    }

    public String getTicketDebugString(long long0) {
        SortedArraySet<Ticket<?>> $$1 = (SortedArraySet<Ticket<?>>) this.tickets.get(long0);
        return $$1 != null && !$$1.isEmpty() ? $$1.first().toString() : "no_ticket";
    }
}