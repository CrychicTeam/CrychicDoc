package com.simibubi.create.content.contraptions.actors.trainControls;

import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.utility.ControlsUtil;
import com.simibubi.create.foundation.utility.Lang;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public class ControlsHandler {

    public static Collection<Integer> currentlyPressed = new HashSet();

    public static int PACKET_RATE = 5;

    private static int packetCooldown;

    private static WeakReference<AbstractContraptionEntity> entityRef = new WeakReference(null);

    private static BlockPos controlsPos;

    public static void levelUnloaded(LevelAccessor level) {
        packetCooldown = 0;
        entityRef = new WeakReference(null);
        controlsPos = null;
        currentlyPressed.clear();
    }

    public static void startControlling(AbstractContraptionEntity entity, BlockPos controllerLocalPos) {
        entityRef = new WeakReference(entity);
        controlsPos = controllerLocalPos;
        Minecraft.getInstance().player.displayClientMessage(Lang.translateDirect("contraption.controls.start_controlling", entity.getContraptionName()), true);
    }

    public static void stopControlling() {
        ControlsUtil.getControls().forEach(kb -> kb.setDown(ControlsUtil.isActuallyPressed(kb)));
        AbstractContraptionEntity abstractContraptionEntity = (AbstractContraptionEntity) entityRef.get();
        if (!currentlyPressed.isEmpty() && abstractContraptionEntity != null) {
            AllPackets.getChannel().sendToServer(new ControlsInputPacket(currentlyPressed, false, abstractContraptionEntity.m_19879_(), controlsPos, false));
        }
        packetCooldown = 0;
        entityRef = new WeakReference(null);
        controlsPos = null;
        currentlyPressed.clear();
        Minecraft.getInstance().player.displayClientMessage(Lang.translateDirect("contraption.controls.stop_controlling"), true);
    }

    public static void tick() {
        AbstractContraptionEntity entity = (AbstractContraptionEntity) entityRef.get();
        if (entity != null) {
            if (packetCooldown > 0) {
                packetCooldown--;
            }
            if (!entity.m_213877_() && !InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 256)) {
                Vector<KeyMapping> controls = ControlsUtil.getControls();
                Collection<Integer> pressedKeys = new HashSet();
                for (int i = 0; i < controls.size(); i++) {
                    if (ControlsUtil.isActuallyPressed((KeyMapping) controls.get(i))) {
                        pressedKeys.add(i);
                    }
                }
                Collection<Integer> newKeys = new HashSet(pressedKeys);
                Collection<Integer> releasedKeys = currentlyPressed;
                newKeys.removeAll(releasedKeys);
                releasedKeys.removeAll(pressedKeys);
                if (!releasedKeys.isEmpty()) {
                    AllPackets.getChannel().sendToServer(new ControlsInputPacket(releasedKeys, false, entity.m_19879_(), controlsPos, false));
                }
                if (!newKeys.isEmpty()) {
                    AllPackets.getChannel().sendToServer(new ControlsInputPacket(newKeys, true, entity.m_19879_(), controlsPos, false));
                    packetCooldown = PACKET_RATE;
                }
                if (packetCooldown == 0) {
                    AllPackets.getChannel().sendToServer(new ControlsInputPacket(pressedKeys, true, entity.m_19879_(), controlsPos, false));
                    packetCooldown = PACKET_RATE;
                }
                currentlyPressed = pressedKeys;
                controls.forEach(kb -> kb.setDown(false));
            } else {
                BlockPos pos = controlsPos;
                stopControlling();
                AllPackets.getChannel().sendToServer(new ControlsInputPacket(currentlyPressed, false, entity.m_19879_(), pos, true));
            }
        }
    }

    @Nullable
    public static AbstractContraptionEntity getContraption() {
        return (AbstractContraptionEntity) entityRef.get();
    }

    @Nullable
    public static BlockPos getControlsPos() {
        return controlsPos;
    }
}