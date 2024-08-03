package com.simibubi.create.content.redstone.link;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.WorldHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

public class RedstoneLinkNetworkHandler {

    static final Map<LevelAccessor, Map<Couple<RedstoneLinkNetworkHandler.Frequency>, Set<IRedstoneLinkable>>> connections = new IdentityHashMap();

    public final AtomicInteger globalPowerVersion = new AtomicInteger();

    public void onLoadWorld(LevelAccessor world) {
        connections.put(world, new HashMap());
        Create.LOGGER.debug("Prepared Redstone Network Space for " + WorldHelper.getDimensionID(world));
    }

    public void onUnloadWorld(LevelAccessor world) {
        connections.remove(world);
        Create.LOGGER.debug("Removed Redstone Network Space for " + WorldHelper.getDimensionID(world));
    }

    public Set<IRedstoneLinkable> getNetworkOf(LevelAccessor world, IRedstoneLinkable actor) {
        Map<Couple<RedstoneLinkNetworkHandler.Frequency>, Set<IRedstoneLinkable>> networksInWorld = this.networksIn(world);
        Couple<RedstoneLinkNetworkHandler.Frequency> key = actor.getNetworkKey();
        if (!networksInWorld.containsKey(key)) {
            networksInWorld.put(key, new LinkedHashSet());
        }
        return (Set<IRedstoneLinkable>) networksInWorld.get(key);
    }

    public void addToNetwork(LevelAccessor world, IRedstoneLinkable actor) {
        this.getNetworkOf(world, actor).add(actor);
        this.updateNetworkOf(world, actor);
    }

    public void removeFromNetwork(LevelAccessor world, IRedstoneLinkable actor) {
        Set<IRedstoneLinkable> network = this.getNetworkOf(world, actor);
        network.remove(actor);
        if (network.isEmpty()) {
            this.networksIn(world).remove(actor.getNetworkKey());
        } else {
            this.updateNetworkOf(world, actor);
        }
    }

    public void updateNetworkOf(LevelAccessor world, IRedstoneLinkable actor) {
        Set<IRedstoneLinkable> network = this.getNetworkOf(world, actor);
        this.globalPowerVersion.incrementAndGet();
        int power = 0;
        Iterator<IRedstoneLinkable> iterator = network.iterator();
        while (iterator.hasNext()) {
            IRedstoneLinkable other = (IRedstoneLinkable) iterator.next();
            if (!other.isAlive()) {
                iterator.remove();
            } else if (withinRange(actor, other) && power < 15) {
                power = Math.max(other.getTransmittedStrength(), power);
            }
        }
        if (actor instanceof LinkBehaviour linkBehaviour && linkBehaviour.isListening()) {
            linkBehaviour.newPosition = true;
            linkBehaviour.setReceivedStrength(power);
        }
        for (IRedstoneLinkable other : network) {
            if (other != actor && other.isListening() && withinRange(actor, other)) {
                other.setReceivedStrength(power);
            }
        }
    }

    public static boolean withinRange(IRedstoneLinkable from, IRedstoneLinkable to) {
        return from == to ? true : from.getLocation().m_123314_(to.getLocation(), (double) AllConfigs.server().logistics.linkRange.get().intValue());
    }

    public Map<Couple<RedstoneLinkNetworkHandler.Frequency>, Set<IRedstoneLinkable>> networksIn(LevelAccessor world) {
        if (!connections.containsKey(world)) {
            Create.LOGGER.warn("Tried to Access unprepared network space of " + WorldHelper.getDimensionID(world));
            return new HashMap();
        } else {
            return (Map<Couple<RedstoneLinkNetworkHandler.Frequency>, Set<IRedstoneLinkable>>) connections.get(world);
        }
    }

    public boolean hasAnyLoadedPower(Couple<RedstoneLinkNetworkHandler.Frequency> frequency) {
        for (Map<Couple<RedstoneLinkNetworkHandler.Frequency>, Set<IRedstoneLinkable>> map : connections.values()) {
            Set<IRedstoneLinkable> set = (Set<IRedstoneLinkable>) map.get(frequency);
            if (set != null && !set.isEmpty()) {
                for (IRedstoneLinkable link : set) {
                    if (link.getTransmittedStrength() > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static class Frequency {

        public static final RedstoneLinkNetworkHandler.Frequency EMPTY = new RedstoneLinkNetworkHandler.Frequency(ItemStack.EMPTY);

        private static final Map<Item, RedstoneLinkNetworkHandler.Frequency> simpleFrequencies = new IdentityHashMap();

        private ItemStack stack;

        private Item item;

        private int color;

        public static RedstoneLinkNetworkHandler.Frequency of(ItemStack stack) {
            if (stack.isEmpty()) {
                return EMPTY;
            } else {
                return !stack.hasTag() ? (RedstoneLinkNetworkHandler.Frequency) simpleFrequencies.computeIfAbsent(stack.getItem(), $ -> new RedstoneLinkNetworkHandler.Frequency(stack)) : new RedstoneLinkNetworkHandler.Frequency(stack);
            }
        }

        private Frequency(ItemStack stack) {
            this.stack = stack;
            this.item = stack.getItem();
            CompoundTag displayTag = stack.getTagElement("display");
            this.color = displayTag != null && displayTag.contains("color") ? displayTag.getInt("color") : -1;
        }

        public ItemStack getStack() {
            return this.stack;
        }

        public int hashCode() {
            return this.item.hashCode() * 31 ^ this.color;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else {
                return obj instanceof RedstoneLinkNetworkHandler.Frequency ? ((RedstoneLinkNetworkHandler.Frequency) obj).item == this.item && ((RedstoneLinkNetworkHandler.Frequency) obj).color == this.color : false;
            }
        }
    }
}