package me.jellysquid.mods.lithium.mixin.experimental.chunk_tickets;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collections;
import java.util.Iterator;
import me.jellysquid.mods.lithium.common.util.collections.ChunkTicketSortedArraySet;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.Ticket;
import net.minecraft.util.SortedArraySet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ DistanceManager.class })
public abstract class ChunkTicketManagerMixin {

    @Shadow
    private long ticketTickCounter;

    @Shadow
    @Final
    Long2ObjectOpenHashMap<SortedArraySet<Ticket<?>>> tickets;

    private final Long2ObjectOpenHashMap<SortedArraySet<Ticket<?>>> positionWithExpiringTicket = new Long2ObjectOpenHashMap();

    private static boolean canNoneExpire(SortedArraySet<Ticket<?>> tickets) {
        if (!tickets.isEmpty()) {
            for (Ticket<?> ticket : tickets) {
                if (canExpire(ticket)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Redirect(method = { "method_14041", "m_183922_", "lambda$getTickets$6" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/SortedArraySet;create(I)Lnet/minecraft/util/collection/SortedArraySet;"))
    private static SortedArraySet<Ticket<?>> useLithiumSortedArraySet(int initialCapacity) {
        return new ChunkTicketSortedArraySet(initialCapacity);
    }

    private static boolean canExpire(Ticket<?> ticket) {
        return ticket.getType().timeout() != 0L;
    }

    @Inject(method = { "addTicket(JLnet/minecraft/server/world/ChunkTicket;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ChunkTicket;setTickCreated(J)V") }, locals = LocalCapture.CAPTURE_FAILHARD)
    private void registerExpiringTicket(long position, Ticket<?> ticket, CallbackInfo ci, SortedArraySet<Ticket<?>> ticketsAtPos, int i, Ticket<?> chunkTicket) {
        if (canExpire(ticket)) {
            this.positionWithExpiringTicket.put(position, ticketsAtPos);
        }
    }

    @Inject(method = { "removeTicket(JLnet/minecraft/server/world/ChunkTicket;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ChunkTicketManager$TicketDistanceLevelPropagator;updateLevel(JIZ)V") })
    private void unregisterExpiringTicket(long pos, Ticket<?> ticket, CallbackInfo ci) {
        if (canExpire(ticket)) {
            SortedArraySet<Ticket<?>> ticketsAtPos = (SortedArraySet<Ticket<?>>) this.positionWithExpiringTicket.get(pos);
            if (canNoneExpire(ticketsAtPos)) {
                this.positionWithExpiringTicket.remove(pos);
            }
        }
    }

    @Inject(method = { "addTicket(JLnet/minecraft/server/world/ChunkTicket;)V" }, at = { @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/util/collection/SortedArraySet;addAndGet(Ljava/lang/Object;)Ljava/lang/Object;") }, locals = LocalCapture.CAPTURE_FAILHARD)
    private void updateSetMinExpiryTime(long position, Ticket<?> ticket, CallbackInfo ci, SortedArraySet<Ticket<?>> sortedArraySet, int i) {
        if (canExpire(ticket) && sortedArraySet instanceof ChunkTicketSortedArraySet<?> chunkTickets) {
            chunkTickets.addExpireTime(this.ticketTickCounter + ticket.getType().timeout());
        }
    }

    @Redirect(method = { "purge" }, at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ChunkTicketManager;ticketsByPosition:Lit/unimi/dsi/fastutil/longs/Long2ObjectOpenHashMap;", ordinal = 0))
    private Long2ObjectOpenHashMap<SortedArraySet<Ticket<?>>> getExpiringTicketsByPosition(DistanceManager chunkTicketManager) {
        return this.positionWithExpiringTicket;
    }

    @Redirect(method = { "purge" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/SortedArraySet;isEmpty()Z"))
    private boolean retCanNoneExpire(SortedArraySet<Ticket<?>> tickets) {
        return canNoneExpire(tickets);
    }

    @Inject(method = { "purge" }, locals = LocalCapture.CAPTURE_FAILHARD, at = { @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/util/collection/SortedArraySet;isEmpty()Z") })
    private void removeIfEmpty(CallbackInfo ci, ObjectIterator<?> objectIterator, Entry<SortedArraySet<Ticket<?>>> entry) {
        SortedArraySet<Ticket<?>> ticketsAtPos = (SortedArraySet<Ticket<?>>) entry.getValue();
        if (ticketsAtPos.isEmpty()) {
            this.tickets.remove(entry.getLongKey(), ticketsAtPos);
        }
    }

    @Redirect(method = { "purge" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/SortedArraySet;iterator()Ljava/util/Iterator;"))
    private Iterator<Ticket<?>> skipIfNotExpiringNow(SortedArraySet<Ticket<?>> ticketsAtPos) {
        if (ticketsAtPos instanceof ChunkTicketSortedArraySet<?> optimizedSet && optimizedSet.getMinExpireTime() > this.ticketTickCounter) {
            return Collections.emptyIterator();
        }
        return ticketsAtPos.iterator();
    }
}