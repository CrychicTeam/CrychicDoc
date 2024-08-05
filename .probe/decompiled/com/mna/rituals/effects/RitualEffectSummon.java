package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.Portal;
import com.mna.items.ItemInit;
import com.mna.tools.TeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RitualEffectSummon extends RitualEffect {

    public RitualEffectSummon(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        BlockPos teleportTarget = context.getCenter().above();
        Player portalTarget = null;
        ResourceKey<Level> worldKey = null;
        ItemStack playerCharm = ItemStack.EMPTY;
        for (ItemStack stack : context.getCollectedReagents()) {
            if (stack.getItem() == ItemInit.PLAYER_CHARM.get()) {
                playerCharm = stack;
            }
            if (stack.getItem() == ItemInit.WORLD_CHARM.get()) {
                worldKey = ItemInit.WORLD_CHARM.get().GetWorldTarget(stack);
            }
        }
        if (worldKey == null) {
            worldKey = context.getLevel().dimension();
        }
        ServerLevel targetWorld = TeleportHelper.resolveRegistryKey((ServerLevel) context.getLevel(), worldKey.location());
        if (!playerCharm.isEmpty()) {
            Player player = ItemInit.PLAYER_CHARM.get().GetPlayerTarget(playerCharm, targetWorld);
            if (player != null) {
                portalTarget = player;
            }
        }
        if (portalTarget == null) {
            context.getCaster().m_213846_(Component.translatable("mna:rituals/visit.not_found"));
            return false;
        } else {
            Portal portal = new Portal(EntityInit.PORTAL_ENTITY.get(), context.getLevel());
            portal.m_146884_(Vec3.atBottomCenterOf(portalTarget.m_20183_()));
            portal.setTeleportBlockPos(teleportTarget.above(), context.getLevel().dimension());
            context.getLevel().m_7967_(portal);
            return false;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 20;
    }
}