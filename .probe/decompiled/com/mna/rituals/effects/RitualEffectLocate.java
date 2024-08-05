package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.entities.utility.PresentItem;
import com.mna.items.ItemInit;
import com.mna.items.artifice.ItemThaumaturgicCompass;
import com.mojang.datafixers.util.Pair;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class RitualEffectLocate extends RitualEffect {

    public RitualEffectLocate(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        Optional<ItemStack> thaumLinkOptional = context.getCollectedReagents(i -> i.getItem() == ItemInit.THAUMATURGIC_LINK.get()).stream().findFirst();
        if (!thaumLinkOptional.isPresent()) {
            return false;
        } else {
            ItemStack outputCompass = new ItemStack(ItemInit.THAUMATURGIC_COMPASS.get());
            ItemStack thaumLink = (ItemStack) thaumLinkOptional.get();
            boolean isBiome = false;
            BlockPos targetPos = this.locateStructure((ServerLevel) context.getLevel(), context.getCenter(), thaumLink);
            if (targetPos == null) {
                isBiome = true;
                targetPos = this.locateBiome((ServerLevel) context.getLevel(), context.getCenter(), thaumLink);
                if (targetPos == null) {
                    context.getCollectedReagents().forEach(i -> {
                        ItemEntity item = new ItemEntity(context.getLevel(), (double) context.getCenter().m_123341_(), (double) context.getCenter().above().m_123342_(), (double) context.getCenter().m_123343_(), i);
                        context.getLevel().m_7967_(item);
                    });
                    if (context.getCaster() != null) {
                        context.getCaster().m_213846_(Component.translatable("mna:rituals/locating.failed"));
                    }
                    return false;
                }
            }
            context.getLevel().playSound((Player) null, context.getCenter(), SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
            ItemThaumaturgicCompass.setTrackedPosition(outputCompass, context.getLevel().dimension(), targetPos, ItemInit.THAUMATURGIC_LINK.get().getLocationKey(thaumLink), isBiome ? ItemThaumaturgicCompass.TrackType.Biome : ItemThaumaturgicCompass.TrackType.Structure);
            PresentItem epi = new PresentItem(context.getLevel(), (double) context.getCenter().m_123341_(), (double) context.getCenter().above().m_123342_(), (double) context.getCenter().m_123343_(), outputCompass);
            context.getLevel().m_7967_(epi);
            return false;
        }
    }

    @Nullable
    private BlockPos locateBiome(ServerLevel world, BlockPos center, ItemStack thaumLink) {
        ResourceLocation biomeLoc = ItemInit.THAUMATURGIC_LINK.get().getLocationKey(thaumLink);
        if (biomeLoc == null) {
            return null;
        } else {
            Registry<Biome> registry = world.m_9598_().registryOrThrow(Registries.BIOME);
            Biome biome = registry.get(biomeLoc);
            if (biome == null) {
                return null;
            } else {
                Pair<BlockPos, Holder<Biome>> result = world.findClosestBiome3d(b -> b.isBound() && ((Biome) b.get()).equals(biome), center, 6400, 8, 64);
                return result == null ? null : (BlockPos) result.getFirst();
            }
        }
    }

    @Nullable
    private BlockPos locateStructure(ServerLevel world, BlockPos center, ItemStack thaumLink) {
        ResourceLocation structureLoc = ItemInit.THAUMATURGIC_LINK.get().getLocationKey(thaumLink);
        if (structureLoc == null) {
            return null;
        } else {
            Registry<Structure> registry = world.m_9598_().registryOrThrow(Registries.STRUCTURE);
            Structure pStructure = registry.get(structureLoc);
            try {
                HolderSet<Structure> holderset = HolderSet.direct(Holder.direct(pStructure));
                Pair<BlockPos, Holder<Structure>> pair = world.getChunkSource().getGenerator().findNearestMapStructure(world, holderset, center, 100, false);
                if (pair != null) {
                    return (BlockPos) pair.getFirst();
                }
            } catch (Throwable var9) {
            }
            return null;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 0;
    }
}