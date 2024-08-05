package com.sihenzhang.crockpot.integration.theoneprobe;

import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.block.BirdcageBlock;
import com.sihenzhang.crockpot.block.entity.BirdcageBlockEntity;
import com.sihenzhang.crockpot.util.RLUtils;
import java.util.function.Function;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BirdcageProbeInfoProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void> {

    public Void apply(ITheOneProbe theOneProbe) {
        theOneProbe.registerProvider(this);
        return null;
    }

    public ResourceLocation getID() {
        return RLUtils.createRL("birdcage");
    }

    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData data) {
        if (blockState.m_60734_() instanceof BirdcageBlock birdcageBlock && birdcageBlock.getBlockEntity(level, data.getPos(), blockState) instanceof BirdcageBlockEntity birdcageBlockEntity) {
            for (Pair<ItemStack, Long> output : birdcageBlockEntity.getOutputBuffer()) {
                long remainTime = (Long) output.getSecond() - level.getGameTime();
                float progress = (float) (40L - remainTime) / 40.0F;
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).item((ItemStack) output.getFirst()).progress((int) (progress * 100.0F), 100, probeInfo.defaultProgressStyle().suffix(Component.literal("%")));
            }
        }
    }
}