package net.minecraft.server.level;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntMaps;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import net.minecraft.core.SectionPos;
import net.minecraft.util.SortedArraySet;
import net.minecraft.util.thread.ProcessorHandle;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.slf4j.Logger;

public abstract class DistanceManager {

    static final Logger LOGGER = LogUtils.getLogger();

    static final int PLAYER_TICKET_LEVEL = ChunkLevel.byStatus(FullChunkStatus.ENTITY_TICKING);

    private static final int INITIAL_TICKET_LIST_CAPACITY = 4;

    final Long2ObjectMap<ObjectSet<ServerPlayer>> playersPerChunk = new Long2ObjectOpenHashMap();

    final Long2ObjectOpenHashMap<SortedArraySet<Ticket<?>>> tickets = new Long2ObjectOpenHashMap();

    private final DistanceManager.ChunkTicketTracker ticketTracker = new DistanceManager.ChunkTicketTracker();

    private final DistanceManager.FixedPlayerDistanceChunkTracker naturalSpawnChunkCounter = new DistanceManager.FixedPlayerDistanceChunkTracker(8);

    private final TickingTracker tickingTicketsTracker = new TickingTracker();

    private final DistanceManager.PlayerTicketTracker playerTicketManager = new DistanceManager.PlayerTicketTracker(32);

    final Set<ChunkHolder> chunksToUpdateFutures = Sets.newHashSet();

    final ChunkTaskPriorityQueueSorter ticketThrottler;

    final ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<Runnable>> ticketThrottlerInput;

    final ProcessorHandle<ChunkTaskPriorityQueueSorter.Release> ticketThrottlerReleaser;

    final LongSet ticketsToRelease = new LongOpenHashSet();

    final Executor mainThreadExecutor;

    private long ticketTickCounter;

    private int simulationDistance = 10;

    protected DistanceManager(Executor executor0, Executor executor1) {
        ProcessorHandle<Runnable> $$2 = ProcessorHandle.of("player ticket throttler", executor1::execute);
        ChunkTaskPriorityQueueSorter $$3 = new ChunkTaskPriorityQueueSorter(ImmutableList.of($$2), executor0, 4);
        this.ticketThrottler = $$3;
        this.ticketThrottlerInput = $$3.getProcessor($$2, true);
        this.ticketThrottlerReleaser = $$3.getReleaseProcessor($$2);
        this.mainThreadExecutor = executor1;
    }

    protected void purgeStaleTickets() {
        this.ticketTickCounter++;
        ObjectIterator<Entry<SortedArraySet<Ticket<?>>>> $$0 = this.tickets.long2ObjectEntrySet().fastIterator();
        while ($$0.hasNext()) {
            Entry<SortedArraySet<Ticket<?>>> $$1 = (Entry<SortedArraySet<Ticket<?>>>) $$0.next();
            Iterator<Ticket<?>> $$2 = ((SortedArraySet) $$1.getValue()).iterator();
            boolean $$3 = false;
            while ($$2.hasNext()) {
                Ticket<?> $$4 = (Ticket<?>) $$2.next();
                if ($$4.timedOut(this.ticketTickCounter)) {
                    $$2.remove();
                    $$3 = true;
                    this.tickingTicketsTracker.removeTicket($$1.getLongKey(), $$4);
                }
            }
            if ($$3) {
                this.ticketTracker.m_140715_($$1.getLongKey(), getTicketLevelAt((SortedArraySet<Ticket<?>>) $$1.getValue()), false);
            }
            if (((SortedArraySet) $$1.getValue()).isEmpty()) {
                $$0.remove();
            }
        }
    }

    private static int getTicketLevelAt(SortedArraySet<Ticket<?>> sortedArraySetTicket0) {
        return !sortedArraySetTicket0.isEmpty() ? sortedArraySetTicket0.first().getTicketLevel() : ChunkLevel.MAX_LEVEL + 1;
    }

    protected abstract boolean isChunkToRemove(long var1);

    @Nullable
    protected abstract ChunkHolder getChunk(long var1);

    @Nullable
    protected abstract ChunkHolder updateChunkScheduling(long var1, int var3, @Nullable ChunkHolder var4, int var5);

    public boolean runAllUpdates(ChunkMap chunkMap0) {
        this.naturalSpawnChunkCounter.runAllUpdates();
        this.tickingTicketsTracker.runAllUpdates();
        this.playerTicketManager.runAllUpdates();
        int $$1 = Integer.MAX_VALUE - this.ticketTracker.runDistanceUpdates(Integer.MAX_VALUE);
        boolean $$2 = $$1 != 0;
        if ($$2) {
        }
        if (!this.chunksToUpdateFutures.isEmpty()) {
            this.chunksToUpdateFutures.forEach(p_183908_ -> p_183908_.updateFutures(chunkMap0, this.mainThreadExecutor));
            this.chunksToUpdateFutures.clear();
            return true;
        } else {
            if (!this.ticketsToRelease.isEmpty()) {
                LongIterator $$3 = this.ticketsToRelease.iterator();
                while ($$3.hasNext()) {
                    long $$4 = $$3.nextLong();
                    if (this.getTickets($$4).stream().anyMatch(p_183910_ -> p_183910_.getType() == TicketType.PLAYER)) {
                        ChunkHolder $$5 = chunkMap0.getUpdatingChunkIfPresent($$4);
                        if ($$5 == null) {
                            throw new IllegalStateException();
                        }
                        CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>> $$6 = $$5.getEntityTickingChunkFuture();
                        $$6.thenAccept(p_183905_ -> this.mainThreadExecutor.execute(() -> this.ticketThrottlerReleaser.tell(ChunkTaskPriorityQueueSorter.release(() -> {
                        }, $$4, false))));
                    }
                }
                this.ticketsToRelease.clear();
            }
            return $$2;
        }
    }

    void addTicket(long long0, Ticket<?> ticket1) {
        SortedArraySet<Ticket<?>> $$2 = this.getTickets(long0);
        int $$3 = getTicketLevelAt($$2);
        Ticket<?> $$4 = $$2.addOrGet(ticket1);
        $$4.setCreatedTick(this.ticketTickCounter);
        if (ticket1.getTicketLevel() < $$3) {
            this.ticketTracker.m_140715_(long0, ticket1.getTicketLevel(), true);
        }
    }

    void removeTicket(long long0, Ticket<?> ticket1) {
        SortedArraySet<Ticket<?>> $$2 = this.getTickets(long0);
        if ($$2.remove(ticket1)) {
        }
        if ($$2.isEmpty()) {
            this.tickets.remove(long0);
        }
        this.ticketTracker.m_140715_(long0, getTicketLevelAt($$2), false);
    }

    public <T> void addTicket(TicketType<T> ticketTypeT0, ChunkPos chunkPos1, int int2, T t3) {
        this.addTicket(chunkPos1.toLong(), new Ticket<>(ticketTypeT0, int2, t3));
    }

    public <T> void removeTicket(TicketType<T> ticketTypeT0, ChunkPos chunkPos1, int int2, T t3) {
        Ticket<T> $$4 = new Ticket<>(ticketTypeT0, int2, t3);
        this.removeTicket(chunkPos1.toLong(), $$4);
    }

    public <T> void addRegionTicket(TicketType<T> ticketTypeT0, ChunkPos chunkPos1, int int2, T t3) {
        Ticket<T> $$4 = new Ticket<>(ticketTypeT0, ChunkLevel.byStatus(FullChunkStatus.FULL) - int2, t3);
        long $$5 = chunkPos1.toLong();
        this.addTicket($$5, $$4);
        this.tickingTicketsTracker.addTicket($$5, $$4);
    }

    public <T> void removeRegionTicket(TicketType<T> ticketTypeT0, ChunkPos chunkPos1, int int2, T t3) {
        Ticket<T> $$4 = new Ticket<>(ticketTypeT0, ChunkLevel.byStatus(FullChunkStatus.FULL) - int2, t3);
        long $$5 = chunkPos1.toLong();
        this.removeTicket($$5, $$4);
        this.tickingTicketsTracker.removeTicket($$5, $$4);
    }

    private SortedArraySet<Ticket<?>> getTickets(long long0) {
        return (SortedArraySet<Ticket<?>>) this.tickets.computeIfAbsent(long0, p_183923_ -> SortedArraySet.create(4));
    }

    protected void updateChunkForced(ChunkPos chunkPos0, boolean boolean1) {
        Ticket<ChunkPos> $$2 = new Ticket<>(TicketType.FORCED, ChunkMap.FORCED_TICKET_LEVEL, chunkPos0);
        long $$3 = chunkPos0.toLong();
        if (boolean1) {
            this.addTicket($$3, $$2);
            this.tickingTicketsTracker.addTicket($$3, $$2);
        } else {
            this.removeTicket($$3, $$2);
            this.tickingTicketsTracker.removeTicket($$3, $$2);
        }
    }

    public void addPlayer(SectionPos sectionPos0, ServerPlayer serverPlayer1) {
        ChunkPos $$2 = sectionPos0.chunk();
        long $$3 = $$2.toLong();
        ((ObjectSet) this.playersPerChunk.computeIfAbsent($$3, p_183921_ -> new ObjectOpenHashSet())).add(serverPlayer1);
        this.naturalSpawnChunkCounter.m_140715_($$3, 0, true);
        this.playerTicketManager.m_140715_($$3, 0, true);
        this.tickingTicketsTracker.addTicket(TicketType.PLAYER, $$2, this.getPlayerTicketLevel(), $$2);
    }

    public void removePlayer(SectionPos sectionPos0, ServerPlayer serverPlayer1) {
        ChunkPos $$2 = sectionPos0.chunk();
        long $$3 = $$2.toLong();
        ObjectSet<ServerPlayer> $$4 = (ObjectSet<ServerPlayer>) this.playersPerChunk.get($$3);
        $$4.remove(serverPlayer1);
        if ($$4.isEmpty()) {
            this.playersPerChunk.remove($$3);
            this.naturalSpawnChunkCounter.m_140715_($$3, Integer.MAX_VALUE, false);
            this.playerTicketManager.m_140715_($$3, Integer.MAX_VALUE, false);
            this.tickingTicketsTracker.removeTicket(TicketType.PLAYER, $$2, this.getPlayerTicketLevel(), $$2);
        }
    }

    private int getPlayerTicketLevel() {
        return Math.max(0, ChunkLevel.byStatus(FullChunkStatus.ENTITY_TICKING) - this.simulationDistance);
    }

    public boolean inEntityTickingRange(long long0) {
        return ChunkLevel.isEntityTicking(this.tickingTicketsTracker.getLevel(long0));
    }

    public boolean inBlockTickingRange(long long0) {
        return ChunkLevel.isBlockTicking(this.tickingTicketsTracker.getLevel(long0));
    }

    protected String getTicketDebugString(long long0) {
        SortedArraySet<Ticket<?>> $$1 = (SortedArraySet<Ticket<?>>) this.tickets.get(long0);
        return $$1 != null && !$$1.isEmpty() ? $$1.first().toString() : "no_ticket";
    }

    protected void updatePlayerTickets(int int0) {
        this.playerTicketManager.updateViewDistance(int0);
    }

    public void updateSimulationDistance(int int0) {
        if (int0 != this.simulationDistance) {
            this.simulationDistance = int0;
            this.tickingTicketsTracker.replacePlayerTicketsLevel(this.getPlayerTicketLevel());
        }
    }

    public int getNaturalSpawnChunkCount() {
        this.naturalSpawnChunkCounter.runAllUpdates();
        return this.naturalSpawnChunkCounter.chunks.size();
    }

    public boolean hasPlayersNearby(long long0) {
        this.naturalSpawnChunkCounter.runAllUpdates();
        return this.naturalSpawnChunkCounter.chunks.containsKey(long0);
    }

    public String getDebugStatus() {
        return this.ticketThrottler.getDebugStatus();
    }

    private void dumpTickets(String string0) {
        try {
            FileOutputStream $$1 = new FileOutputStream(new File(string0));
            try {
                ObjectIterator var3 = this.tickets.long2ObjectEntrySet().iterator();
                while (var3.hasNext()) {
                    Entry<SortedArraySet<Ticket<?>>> $$2 = (Entry<SortedArraySet<Ticket<?>>>) var3.next();
                    ChunkPos $$3 = new ChunkPos($$2.getLongKey());
                    for (Ticket<?> $$4 : (SortedArraySet) $$2.getValue()) {
                        $$1.write(($$3.x + "\t" + $$3.z + "\t" + $$4.getType() + "\t" + $$4.getTicketLevel() + "\t\n").getBytes(StandardCharsets.UTF_8));
                    }
                }
            } catch (Throwable var9) {
                try {
                    $$1.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }
                throw var9;
            }
            $$1.close();
        } catch (IOException var10) {
            LOGGER.error("Failed to dump tickets to {}", string0, var10);
        }
    }

    @VisibleForTesting
    TickingTracker tickingTracker() {
        return this.tickingTicketsTracker;
    }

    public void removeTicketsOnClosing() {
        ImmutableSet<TicketType<?>> $$0 = ImmutableSet.of(TicketType.UNKNOWN, TicketType.POST_TELEPORT, TicketType.LIGHT);
        ObjectIterator<Entry<SortedArraySet<Ticket<?>>>> $$1 = this.tickets.long2ObjectEntrySet().fastIterator();
        while ($$1.hasNext()) {
            Entry<SortedArraySet<Ticket<?>>> $$2 = (Entry<SortedArraySet<Ticket<?>>>) $$1.next();
            Iterator<Ticket<?>> $$3 = ((SortedArraySet) $$2.getValue()).iterator();
            boolean $$4 = false;
            while ($$3.hasNext()) {
                Ticket<?> $$5 = (Ticket<?>) $$3.next();
                if (!$$0.contains($$5.getType())) {
                    $$3.remove();
                    $$4 = true;
                    this.tickingTicketsTracker.removeTicket($$2.getLongKey(), $$5);
                }
            }
            if ($$4) {
                this.ticketTracker.m_140715_($$2.getLongKey(), getTicketLevelAt((SortedArraySet<Ticket<?>>) $$2.getValue()), false);
            }
            if (((SortedArraySet) $$2.getValue()).isEmpty()) {
                $$1.remove();
            }
        }
    }

    public boolean hasTickets() {
        return !this.tickets.isEmpty();
    }

    class ChunkTicketTracker extends ChunkTracker {

        private static final int MAX_LEVEL = ChunkLevel.MAX_LEVEL + 1;

        public ChunkTicketTracker() {
            super(MAX_LEVEL + 1, 16, 256);
        }

        @Override
        protected int getLevelFromSource(long long0) {
            SortedArraySet<Ticket<?>> $$1 = (SortedArraySet<Ticket<?>>) DistanceManager.this.tickets.get(long0);
            if ($$1 == null) {
                return Integer.MAX_VALUE;
            } else {
                return $$1.isEmpty() ? Integer.MAX_VALUE : $$1.first().getTicketLevel();
            }
        }

        @Override
        protected int getLevel(long long0) {
            if (!DistanceManager.this.isChunkToRemove(long0)) {
                ChunkHolder $$1 = DistanceManager.this.getChunk(long0);
                if ($$1 != null) {
                    return $$1.getTicketLevel();
                }
            }
            return MAX_LEVEL;
        }

        @Override
        protected void setLevel(long long0, int int1) {
            ChunkHolder $$2 = DistanceManager.this.getChunk(long0);
            int $$3 = $$2 == null ? MAX_LEVEL : $$2.getTicketLevel();
            if ($$3 != int1) {
                $$2 = DistanceManager.this.updateChunkScheduling(long0, int1, $$2, $$3);
                if ($$2 != null) {
                    DistanceManager.this.chunksToUpdateFutures.add($$2);
                }
            }
        }

        public int runDistanceUpdates(int int0) {
            return this.m_75588_(int0);
        }
    }

    class FixedPlayerDistanceChunkTracker extends ChunkTracker {

        protected final Long2ByteMap chunks = new Long2ByteOpenHashMap();

        protected final int maxDistance;

        protected FixedPlayerDistanceChunkTracker(int int0) {
            super(int0 + 2, 16, 256);
            this.maxDistance = int0;
            this.chunks.defaultReturnValue((byte) (int0 + 2));
        }

        @Override
        protected int getLevel(long long0) {
            return this.chunks.get(long0);
        }

        @Override
        protected void setLevel(long long0, int int1) {
            byte $$2;
            if (int1 > this.maxDistance) {
                $$2 = this.chunks.remove(long0);
            } else {
                $$2 = this.chunks.put(long0, (byte) int1);
            }
            this.onLevelChange(long0, $$2, int1);
        }

        protected void onLevelChange(long long0, int int1, int int2) {
        }

        @Override
        protected int getLevelFromSource(long long0) {
            return this.havePlayer(long0) ? 0 : Integer.MAX_VALUE;
        }

        private boolean havePlayer(long long0) {
            ObjectSet<ServerPlayer> $$1 = (ObjectSet<ServerPlayer>) DistanceManager.this.playersPerChunk.get(long0);
            return $$1 != null && !$$1.isEmpty();
        }

        public void runAllUpdates() {
            this.m_75588_(Integer.MAX_VALUE);
        }

        private void dumpChunks(String string0) {
            try {
                FileOutputStream $$1 = new FileOutputStream(new File(string0));
                try {
                    ObjectIterator var3 = this.chunks.long2ByteEntrySet().iterator();
                    while (var3.hasNext()) {
                        it.unimi.dsi.fastutil.longs.Long2ByteMap.Entry $$2 = (it.unimi.dsi.fastutil.longs.Long2ByteMap.Entry) var3.next();
                        ChunkPos $$3 = new ChunkPos($$2.getLongKey());
                        String $$4 = Byte.toString($$2.getByteValue());
                        $$1.write(($$3.x + "\t" + $$3.z + "\t" + $$4 + "\n").getBytes(StandardCharsets.UTF_8));
                    }
                } catch (Throwable var8) {
                    try {
                        $$1.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                    throw var8;
                }
                $$1.close();
            } catch (IOException var9) {
                DistanceManager.LOGGER.error("Failed to dump chunks to {}", string0, var9);
            }
        }
    }

    class PlayerTicketTracker extends DistanceManager.FixedPlayerDistanceChunkTracker {

        private int viewDistance;

        private final Long2IntMap queueLevels = Long2IntMaps.synchronize(new Long2IntOpenHashMap());

        private final LongSet toUpdate = new LongOpenHashSet();

        protected PlayerTicketTracker(int int0) {
            super(int0);
            this.viewDistance = 0;
            this.queueLevels.defaultReturnValue(int0 + 2);
        }

        @Override
        protected void onLevelChange(long long0, int int1, int int2) {
            this.toUpdate.add(long0);
        }

        public void updateViewDistance(int int0) {
            ObjectIterator var2 = this.f_140886_.long2ByteEntrySet().iterator();
            while (var2.hasNext()) {
                it.unimi.dsi.fastutil.longs.Long2ByteMap.Entry $$1 = (it.unimi.dsi.fastutil.longs.Long2ByteMap.Entry) var2.next();
                byte $$2 = $$1.getByteValue();
                long $$3 = $$1.getLongKey();
                this.onLevelChange($$3, $$2, this.haveTicketFor($$2), $$2 <= int0);
            }
            this.viewDistance = int0;
        }

        private void onLevelChange(long long0, int int1, boolean boolean2, boolean boolean3) {
            if (boolean2 != boolean3) {
                Ticket<?> $$4 = new Ticket<>(TicketType.PLAYER, DistanceManager.PLAYER_TICKET_LEVEL, new ChunkPos(long0));
                if (boolean3) {
                    DistanceManager.this.ticketThrottlerInput.tell(ChunkTaskPriorityQueueSorter.message((Runnable) (() -> DistanceManager.this.mainThreadExecutor.execute(() -> {
                        if (this.haveTicketFor(this.m_6172_(long0))) {
                            DistanceManager.this.addTicket(long0, $$4);
                            DistanceManager.this.ticketsToRelease.add(long0);
                        } else {
                            DistanceManager.this.ticketThrottlerReleaser.tell(ChunkTaskPriorityQueueSorter.release(() -> {
                            }, long0, false));
                        }
                    })), long0, () -> int1));
                } else {
                    DistanceManager.this.ticketThrottlerReleaser.tell(ChunkTaskPriorityQueueSorter.release(() -> DistanceManager.this.mainThreadExecutor.execute(() -> DistanceManager.this.removeTicket(long0, $$4)), long0, true));
                }
            }
        }

        @Override
        public void runAllUpdates() {
            super.runAllUpdates();
            if (!this.toUpdate.isEmpty()) {
                LongIterator $$0 = this.toUpdate.iterator();
                while ($$0.hasNext()) {
                    long $$1 = $$0.nextLong();
                    int $$2 = this.queueLevels.get($$1);
                    int $$3 = this.m_6172_($$1);
                    if ($$2 != $$3) {
                        DistanceManager.this.ticketThrottler.onLevelChange(new ChunkPos($$1), () -> this.queueLevels.get($$1), $$3, p_140928_ -> {
                            if (p_140928_ >= this.queueLevels.defaultReturnValue()) {
                                this.queueLevels.remove($$1);
                            } else {
                                this.queueLevels.put($$1, p_140928_);
                            }
                        });
                        this.onLevelChange($$1, $$3, this.haveTicketFor($$2), this.haveTicketFor($$3));
                    }
                }
                this.toUpdate.clear();
            }
        }

        private boolean haveTicketFor(int int0) {
            return int0 <= this.viewDistance;
        }
    }
}