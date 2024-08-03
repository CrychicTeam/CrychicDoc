package com.simibubi.create.content.equipment.toolbox;

import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.networking.ISyncPersistentData;
import com.simibubi.create.foundation.utility.WorldAttached;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.List;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class ToolboxHandler {

    public static final WorldAttached<WeakHashMap<BlockPos, ToolboxBlockEntity>> toolboxes = new WorldAttached<>(w -> new WeakHashMap());

    static int validationTimer = 20;

    public static void onLoad(ToolboxBlockEntity be) {
        toolboxes.get(be.m_58904_()).put(be.m_58899_(), be);
    }

    public static void onUnload(ToolboxBlockEntity be) {
        toolboxes.get(be.m_58904_()).remove(be.m_58899_());
    }

    public static void entityTick(Entity entity, Level world) {
        if (!world.isClientSide) {
            if (world instanceof ServerLevel) {
                if (entity instanceof ServerPlayer) {
                    if (entity.tickCount % validationTimer == 0) {
                        ServerPlayer player = (ServerPlayer) entity;
                        if (player.getPersistentData().contains("CreateToolboxData")) {
                            boolean sendData = false;
                            CompoundTag compound = player.getPersistentData().getCompound("CreateToolboxData");
                            for (int i = 0; i < 9; i++) {
                                String key = String.valueOf(i);
                                if (compound.contains(key)) {
                                    CompoundTag data = compound.getCompound(key);
                                    BlockPos pos = NbtUtils.readBlockPos(data.getCompound("Pos"));
                                    int slot = data.getInt("Slot");
                                    if (world.isLoaded(pos)) {
                                        if (!(world.getBlockState(pos).m_60734_() instanceof ToolboxBlock)) {
                                            compound.remove(key);
                                            sendData = true;
                                        } else {
                                            BlockEntity prevBlockEntity = world.getBlockEntity(pos);
                                            if (prevBlockEntity instanceof ToolboxBlockEntity) {
                                                ((ToolboxBlockEntity) prevBlockEntity).connectPlayer(slot, player, i);
                                            }
                                        }
                                    }
                                }
                            }
                            if (sendData) {
                                syncData(player);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void playerLogin(Player player) {
        if (player instanceof ServerPlayer) {
            if (player.getPersistentData().contains("CreateToolboxData") && !player.getPersistentData().getCompound("CreateToolboxData").isEmpty()) {
                syncData(player);
            }
        }
    }

    public static void syncData(Player player) {
        AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new ISyncPersistentData.PersistentDataPacket(player));
    }

    public static List<ToolboxBlockEntity> getNearest(LevelAccessor world, Player player, int maxAmount) {
        Vec3 location = player.m_20182_();
        double maxRange = getMaxRange(player);
        return (List<ToolboxBlockEntity>) toolboxes.get(world).keySet().stream().filter(p -> distance(location, p) < maxRange * maxRange).sorted((p1, p2) -> Double.compare(distance(location, p1), distance(location, p2))).limit((long) maxAmount).map(toolboxes.get(world)::get).filter(ToolboxBlockEntity::isFullyInitialized).collect(Collectors.toList());
    }

    public static void unequip(Player player, int hotbarSlot, boolean keepItems) {
        CompoundTag compound = player.getPersistentData().getCompound("CreateToolboxData");
        Level world = player.m_9236_();
        String key = String.valueOf(hotbarSlot);
        if (compound.contains(key)) {
            CompoundTag prevData = compound.getCompound(key);
            BlockPos prevPos = NbtUtils.readBlockPos(prevData.getCompound("Pos"));
            int prevSlot = prevData.getInt("Slot");
            if (world.getBlockEntity(prevPos) instanceof ToolboxBlockEntity toolbox) {
                toolbox.unequip(prevSlot, player, hotbarSlot, keepItems || !withinRange(player, toolbox));
            }
            compound.remove(key);
        }
    }

    public static boolean withinRange(Player player, ToolboxBlockEntity box) {
        if (player.m_9236_() != box.m_58904_()) {
            return false;
        } else {
            double maxRange = getMaxRange(player);
            return distance(player.m_20182_(), box.m_58899_()) < maxRange * maxRange;
        }
    }

    public static double distance(Vec3 location, BlockPos p) {
        return location.distanceToSqr((double) ((float) p.m_123341_() + 0.5F), (double) p.m_123342_(), (double) ((float) p.m_123343_() + 0.5F));
    }

    public static double getMaxRange(Player player) {
        return AllConfigs.server().equipment.toolboxRange.get().doubleValue();
    }
}