package com.mna.rituals.effects;

import com.mna.api.capabilities.IWorldMagic;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.blocks.BlockInit;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.items.runes.ItemStoneRune;
import java.util.ArrayList;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualEffectHomestead extends RitualEffect {

    public RitualEffectHomestead(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        if (context.getCaster() == null) {
            return false;
        } else {
            LazyOptional<IWorldMagic> worldMagicContainer = context.getLevel().getCapability(WorldMagicProvider.MAGIC);
            if (worldMagicContainer.isPresent()) {
                IWorldMagic worldMagic = worldMagicContainer.orElse(null);
                ArrayList<ResourceLocation> runes = new ArrayList();
                context.getCollectedReagents(i -> i.getItem() instanceof ItemStoneRune || i.getItem() == Items.AIR).forEach(i -> runes.add(ForgeRegistries.ITEMS.getKey(i.getItem())));
                BlockState state = context.getLevel().getBlockState(context.getCenter().below());
                if (state.m_60734_() != Blocks.BEDROCK && context.getLevel().getBlockEntity(context.getCenter().below()) == null) {
                    if (worldMagic.setRitualTeleportLocation((ServerLevel) context.getLevel(), context.getCenter().below(), runes, Direction.NORTH)) {
                        context.getLevel().setBlockAndUpdate(context.getCenter().below(), BlockInit.RITUAL_TELEPORT_DESTINATION.get().defaultBlockState());
                    } else {
                        context.getCaster().m_213846_(Component.translatable("mna:rituals/homestead.duplicate"));
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 0;
    }
}