package com.mna.rituals.effects;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.IRitualTeleportLocation;
import com.mna.api.capabilities.IWorldMagic;
import com.mna.api.faction.IFaction;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.Portal;
import com.mna.items.ItemInit;
import com.mna.items.ritual.ItemWorldCharm;
import com.mna.tools.TeleportHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualEffectGate extends RitualEffect {

    public RitualEffectGate(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        List<ItemStack> reagents = context.getCollectedReagents();
        if (reagents.size() == 11 && !context.getLevel().isClientSide) {
            ItemStack reagentNine = (ItemStack) reagents.get(9);
            ResourceKey<Level> worldKey = !reagentNine.isEmpty() && reagentNine.getItem() instanceof ItemWorldCharm ? ((ItemWorldCharm) reagentNine.getItem()).GetWorldTarget(reagentNine) : context.getLevel().dimension();
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
                ItemStack reagentEight = (ItemStack) reagents.get(8);
                DyeColor portalColor = null;
                boolean rtp = false;
                if (!reagentEight.isEmpty()) {
                    portalColor = reagentEight.getItem() instanceof DyeItem ? ((DyeItem) ((ItemStack) reagents.get(8)).getItem()).getDyeColor() : null;
                    if (reagentEight.getItem() == ItemInit.GREATER_MOTE_ENDER.get()) {
                        rtp = true;
                    }
                }
                ArrayList<ResourceLocation> runes = new ArrayList();
                for (int i = 0; i < 8; i++) {
                    runes.add(ForgeRegistries.ITEMS.getKey(((ItemStack) reagents.get(i)).getItem()));
                }
                IRitualTeleportLocation teleportTarget = worldMagic.getRitualTeleportBlockLocation(runes, worldKey);
                if (teleportTarget == null && !rtp) {
                    context.getCaster().m_213846_(Component.translatable("mna:rituals/return.not_found"));
                    return false;
                }
                Portal portal = new Portal(EntityInit.PORTAL_ENTITY.get(), context.getLevel());
                portal.m_146884_(Vec3.atBottomCenterOf(context.getCenter()));
                if (!rtp) {
                    portal.setTeleportBlockPos(teleportTarget.getPos().above(), teleportTarget.getWorldType());
                }
                portal.setPermanent();
                if (portalColor != null) {
                    portal.setDyeColor(portalColor);
                }
                if (rtp) {
                    portal.setRTP();
                }
                if (context.getCaster() != null) {
                    IPlayerProgression p = (IPlayerProgression) context.getCaster().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                    IPlayerMagic m = (IPlayerMagic) context.getCaster().getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                    if (p != null && m != null && p.hasAlliedFaction()) {
                        IFaction faction = p.getAlliedFaction();
                        byte idx = 0;
                        ResourceLocation castingResource = m.getCastingResource().getRegistryName();
                        for (byte i = 0; i < p.getAlliedFaction().getCastingResources().length; i++) {
                            if (faction.getCastingResources()[i].equals(castingResource)) {
                                idx = i;
                                break;
                            }
                        }
                        portal.setRenderData(p.getAlliedFaction(), idx);
                    }
                }
                context.getLevel().m_7967_(portal);
                return true;
            }
        }
        return false;
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 20;
    }
}