package com.sihenzhang.crockpot.integration.jade;

import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.block.entity.BirdcageBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;

public enum BirdcageProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (config.get(ModIntegrationJade.BIRDCAGE)) {
            CompoundTag serverData = accessor.getServerData();
            if (serverData.contains("OutputBuffer", 9)) {
                IElementHelper elements = tooltip.getElementHelper();
                ListTag outputBuffer = serverData.getList("OutputBuffer", 10);
                for (int i = 0; i < outputBuffer.size(); i++) {
                    CompoundTag output = outputBuffer.getCompound(i);
                    tooltip.add(elements.item(ItemStack.of(output.getCompound("Item"))));
                    float progress = (float) (40L - output.getLong("Time")) / 40.0F;
                    tooltip.append(elements.progress(progress, Component.literal((int) (progress * 100.0F) + "%"), elements.progressStyle(), BoxStyle.DEFAULT, false).size(new Vec2(60.0F, 14.0F)).translate(new Vec2(0.0F, 4.0F)));
                }
            }
        }
    }

    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof BirdcageBlockEntity birdcageBlockEntity) {
            ListTag list = new ListTag();
            for (Pair<ItemStack, Long> output : birdcageBlockEntity.getOutputBuffer()) {
                CompoundTag tag = new CompoundTag();
                tag.put("Item", ((ItemStack) output.getFirst()).serializeNBT());
                tag.putLong("Time", (Long) output.getSecond() - accessor.getLevel().getGameTime());
                list.add(tag);
            }
            data.put("OutputBuffer", list);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModIntegrationJade.BIRDCAGE;
    }
}