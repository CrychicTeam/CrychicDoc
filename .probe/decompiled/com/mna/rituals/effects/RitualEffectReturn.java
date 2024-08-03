package com.mna.rituals.effects;

import com.mna.api.capabilities.IRitualTeleportLocation;
import com.mna.api.capabilities.IWorldMagic;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.Portal;
import com.mna.items.ritual.ItemWorldCharm;
import com.mna.tools.TeleportHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualEffectReturn extends RitualEffect {

    public RitualEffectReturn(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        if (context.getCaster() == null) {
            return false;
        } else {
            List<ItemStack> reagents = context.getCollectedReagents();
            if (reagents.size() == 9 && !context.getLevel().isClientSide) {
                ItemStack reagentEight = (ItemStack) reagents.get(8);
                ResourceKey<Level> worldKey = !reagentEight.isEmpty() && reagentEight.getItem() instanceof ItemWorldCharm ? ((ItemWorldCharm) reagentEight.getItem()).GetWorldTarget(reagentEight) : context.getLevel().dimension();
                if (worldKey == null) {
                    worldKey = context.getLevel().dimension();
                }
                ServerLevel targetWorld = TeleportHelper.resolveRegistryKey((ServerLevel) context.getLevel(), worldKey.location());
                if (targetWorld == null) {
                    context.getCaster().m_213846_(Component.translatable("mna:rituals/return.world_not_found"));
                    return false;
                }
                LazyOptional<IWorldMagic> worldMagicContainer = targetWorld.getCapability(WorldMagicProvider.MAGIC);
                if (worldMagicContainer.isPresent()) {
                    IWorldMagic worldMagic = worldMagicContainer.orElse(null);
                    ArrayList<ResourceLocation> runes = new ArrayList();
                    for (int i = 0; i < 8; i++) {
                        runes.add(ForgeRegistries.ITEMS.getKey(((ItemStack) reagents.get(i)).getItem()));
                    }
                    IRitualTeleportLocation teleportTarget = worldMagic.getRitualTeleportBlockLocation(runes, worldKey);
                    if (teleportTarget == null) {
                        context.getCaster().m_213846_(Component.translatable("mna:rituals/return.not_found"));
                        return false;
                    }
                    Portal portal = new Portal(EntityInit.PORTAL_ENTITY.get(), context.getLevel());
                    portal.m_146884_(Vec3.atBottomCenterOf(context.getCenter()));
                    portal.setTeleportBlockPos(teleportTarget.getPos().above(), teleportTarget.getWorldType());
                    context.getLevel().m_7967_(portal);
                    return true;
                }
            }
            return true;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 20;
    }
}