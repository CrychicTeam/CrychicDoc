package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.Portal;
import com.mna.items.ItemInit;
import com.mna.tools.TeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RitualEffectVisit extends RitualEffect {

    public RitualEffectVisit(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        Player teleportTarget = null;
        int boneCount = 0;
        ResourceKey<Level> worldKey = null;
        ItemStack playerCharm = ItemStack.EMPTY;
        for (ItemStack stack : context.getCollectedReagents()) {
            if (stack.getItem() == ItemInit.PLAYER_CHARM.get()) {
                playerCharm = stack;
            }
            if (stack.getItem() == ItemInit.WORLD_CHARM.get()) {
                worldKey = ItemInit.WORLD_CHARM.get().GetWorldTarget(stack);
            }
            if (stack.getItem() == Items.BONE) {
                boneCount++;
            }
        }
        if (worldKey == null) {
            worldKey = context.getLevel().dimension();
        }
        if (!playerCharm.isEmpty()) {
            ServerLevel targetWorld = TeleportHelper.resolveRegistryKey((ServerLevel) context.getLevel(), worldKey.location());
            Player player = ItemInit.PLAYER_CHARM.get().GetPlayerTarget(playerCharm, targetWorld);
            if (player != null) {
                teleportTarget = player;
            }
        } else {
            teleportTarget = context.getCaster();
        }
        if (teleportTarget == null) {
            if (context.getCaster() != null) {
                context.getCaster().m_213846_(Component.translatable("mna:rituals/visit.not_found"));
            }
            return false;
        } else {
            BlockPos teleportTargetPos = teleportTarget.m_20183_().above();
            ResourceKey<Level> teleportTargetDimension = teleportTarget.m_9236_().dimension();
            if (boneCount >= 2) {
                CompoundTag casterPD = teleportTarget.getPersistentData();
                if (!casterPD.contains("mna_last_death_data")) {
                    context.getCaster().m_213846_(Component.translatable("mna:rituals/visit.no_death_data"));
                    return false;
                }
                CompoundTag deathData = casterPD.getCompound("mna_last_death_data");
                teleportTargetPos = BlockPos.of(deathData.getLong("position"));
                teleportTargetDimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(deathData.getString("dimension")));
            }
            Portal portal = new Portal(EntityInit.PORTAL_ENTITY.get(), context.getLevel());
            portal.m_146884_(Vec3.atBottomCenterOf(context.getCenter()));
            portal.setTeleportBlockPos(teleportTargetPos, teleportTargetDimension);
            context.getLevel().m_7967_(portal);
            return false;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 20;
    }
}