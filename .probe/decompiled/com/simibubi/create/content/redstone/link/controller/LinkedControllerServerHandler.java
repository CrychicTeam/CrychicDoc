package com.simibubi.create.content.redstone.link.controller;

import com.simibubi.create.Create;
import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.IntAttached;
import com.simibubi.create.foundation.utility.WorldAttached;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

public class LinkedControllerServerHandler {

    public static WorldAttached<Map<UUID, Collection<LinkedControllerServerHandler.ManualFrequencyEntry>>> receivedInputs = new WorldAttached<>($ -> new HashMap());

    static final int TIMEOUT = 30;

    public static void tick(LevelAccessor world) {
        Map<UUID, Collection<LinkedControllerServerHandler.ManualFrequencyEntry>> map = receivedInputs.get(world);
        Iterator<Entry<UUID, Collection<LinkedControllerServerHandler.ManualFrequencyEntry>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UUID, Collection<LinkedControllerServerHandler.ManualFrequencyEntry>> entry = (Entry<UUID, Collection<LinkedControllerServerHandler.ManualFrequencyEntry>>) iterator.next();
            Collection<LinkedControllerServerHandler.ManualFrequencyEntry> list = (Collection<LinkedControllerServerHandler.ManualFrequencyEntry>) entry.getValue();
            Iterator<LinkedControllerServerHandler.ManualFrequencyEntry> entryIterator = list.iterator();
            while (entryIterator.hasNext()) {
                LinkedControllerServerHandler.ManualFrequencyEntry manualFrequencyEntry = (LinkedControllerServerHandler.ManualFrequencyEntry) entryIterator.next();
                manualFrequencyEntry.decrement();
                if (!manualFrequencyEntry.isAlive()) {
                    Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(world, manualFrequencyEntry);
                    entryIterator.remove();
                }
            }
            if (list.isEmpty()) {
                iterator.remove();
            }
        }
    }

    public static void receivePressed(LevelAccessor world, BlockPos pos, UUID uniqueID, List<Couple<RedstoneLinkNetworkHandler.Frequency>> collect, boolean pressed) {
        Map<UUID, Collection<LinkedControllerServerHandler.ManualFrequencyEntry>> map = receivedInputs.get(world);
        Collection<LinkedControllerServerHandler.ManualFrequencyEntry> list = (Collection<LinkedControllerServerHandler.ManualFrequencyEntry>) map.computeIfAbsent(uniqueID, $ -> new ArrayList());
        label42: for (Couple<RedstoneLinkNetworkHandler.Frequency> activated : collect) {
            for (LinkedControllerServerHandler.ManualFrequencyEntry entry : list) {
                if (entry.getSecond().equals(activated)) {
                    if (!pressed) {
                        entry.setFirst(Integer.valueOf(0));
                    } else {
                        entry.updatePosition(pos);
                    }
                    continue label42;
                }
            }
            if (pressed) {
                LinkedControllerServerHandler.ManualFrequencyEntry entryx = new LinkedControllerServerHandler.ManualFrequencyEntry(pos, activated);
                Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(world, entryx);
                list.add(entryx);
                for (IRedstoneLinkable linkable : Create.REDSTONE_LINK_NETWORK_HANDLER.getNetworkOf(world, entryx)) {
                    if (linkable instanceof LinkBehaviour lb && lb.isListening()) {
                        AllAdvancements.LINKED_CONTROLLER.awardTo(world.m_46003_(uniqueID));
                    }
                }
            }
        }
    }

    static class ManualFrequencyEntry extends IntAttached<Couple<RedstoneLinkNetworkHandler.Frequency>> implements IRedstoneLinkable {

        private BlockPos pos;

        public ManualFrequencyEntry(BlockPos pos, Couple<RedstoneLinkNetworkHandler.Frequency> second) {
            super(30, second);
            this.pos = pos;
        }

        public void updatePosition(BlockPos pos) {
            this.pos = pos;
            this.setFirst(Integer.valueOf(30));
        }

        @Override
        public int getTransmittedStrength() {
            return this.isAlive() ? 15 : 0;
        }

        @Override
        public boolean isAlive() {
            return this.getFirst() > 0;
        }

        @Override
        public BlockPos getLocation() {
            return this.pos;
        }

        @Override
        public void setReceivedStrength(int power) {
        }

        @Override
        public boolean isListening() {
            return false;
        }

        @Override
        public Couple<RedstoneLinkNetworkHandler.Frequency> getNetworkKey() {
            return this.getSecond();
        }
    }
}