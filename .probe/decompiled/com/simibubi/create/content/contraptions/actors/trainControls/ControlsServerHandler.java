package com.simibubi.create.content.contraptions.actors.trainControls;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.utility.IntAttached;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.WorldAttached;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;

public class ControlsServerHandler {

    public static WorldAttached<Map<UUID, ControlsServerHandler.ControlsContext>> receivedInputs = new WorldAttached<>($ -> new HashMap());

    static final int TIMEOUT = 30;

    public static void tick(LevelAccessor world) {
        Map<UUID, ControlsServerHandler.ControlsContext> map = receivedInputs.get(world);
        Iterator<Entry<UUID, ControlsServerHandler.ControlsContext>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<UUID, ControlsServerHandler.ControlsContext> entry = (Entry<UUID, ControlsServerHandler.ControlsContext>) iterator.next();
            ControlsServerHandler.ControlsContext ctx = (ControlsServerHandler.ControlsContext) entry.getValue();
            Collection<ControlsServerHandler.ManuallyPressedKey> list = ctx.keys;
            if (ctx.entity.m_213877_()) {
                iterator.remove();
            } else {
                Iterator<ControlsServerHandler.ManuallyPressedKey> entryIterator = list.iterator();
                while (entryIterator.hasNext()) {
                    ControlsServerHandler.ManuallyPressedKey pressedKey = (ControlsServerHandler.ManuallyPressedKey) entryIterator.next();
                    pressedKey.decrement();
                    if (!pressedKey.isAlive()) {
                        entryIterator.remove();
                    }
                }
                Player player = world.m_46003_((UUID) entry.getKey());
                if (player == null) {
                    ctx.entity.stopControlling(ctx.controlsLocalPos);
                    iterator.remove();
                } else {
                    if (!ctx.entity.control(ctx.controlsLocalPos, list.stream().map(Pair::getSecond).toList(), player)) {
                        ctx.entity.stopControlling(ctx.controlsLocalPos);
                    }
                    if (list.isEmpty()) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    public static void receivePressed(LevelAccessor world, AbstractContraptionEntity entity, BlockPos controlsPos, UUID uniqueID, Collection<Integer> collect, boolean pressed) {
        Map<UUID, ControlsServerHandler.ControlsContext> map = receivedInputs.get(world);
        if (map.containsKey(uniqueID) && ((ControlsServerHandler.ControlsContext) map.get(uniqueID)).entity != entity) {
            map.remove(uniqueID);
        }
        ControlsServerHandler.ControlsContext ctx = (ControlsServerHandler.ControlsContext) map.computeIfAbsent(uniqueID, $ -> new ControlsServerHandler.ControlsContext(entity, controlsPos));
        Collection<ControlsServerHandler.ManuallyPressedKey> list = ctx.keys;
        label35: for (Integer activated : collect) {
            for (ControlsServerHandler.ManuallyPressedKey entry : list) {
                Integer inputType = entry.getSecond();
                if (inputType.equals(activated)) {
                    if (!pressed) {
                        entry.setFirst(Integer.valueOf(0));
                    } else {
                        entry.keepAlive();
                    }
                    continue label35;
                }
            }
            if (pressed) {
                list.add(new ControlsServerHandler.ManuallyPressedKey(activated));
            }
        }
    }

    static class ControlsContext {

        Collection<ControlsServerHandler.ManuallyPressedKey> keys;

        AbstractContraptionEntity entity;

        BlockPos controlsLocalPos;

        public ControlsContext(AbstractContraptionEntity entity, BlockPos controlsPos) {
            this.entity = entity;
            this.controlsLocalPos = controlsPos;
            this.keys = new ArrayList();
        }
    }

    static class ManuallyPressedKey extends IntAttached<Integer> {

        public ManuallyPressedKey(Integer second) {
            super(30, second);
        }

        public void keepAlive() {
            this.setFirst(Integer.valueOf(30));
        }

        public boolean isAlive() {
            return this.getFirst() > 0;
        }
    }
}