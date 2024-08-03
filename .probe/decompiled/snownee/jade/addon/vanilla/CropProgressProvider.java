package snownee.jade.addon.vanilla;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum CropProgressProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
        if (accessor.getBlock() == Blocks.WHEAT) {
            return IElementHelper.get().item(new ItemStack(Items.WHEAT));
        } else {
            return accessor.getBlock() == Blocks.BEETROOTS ? IElementHelper.get().item(new ItemStack(Items.BEETROOT)) : null;
        }
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        BlockState state = accessor.getBlockState();
        Block block = state.m_60734_();
        if (block instanceof CropBlock crop) {
            addMaturityTooltip(tooltip, (float) crop.getAge(state) / (float) crop.getMaxAge());
        } else if (state.m_61138_(BlockStateProperties.AGE_7)) {
            addMaturityTooltip(tooltip, (float) ((Integer) state.m_61143_(BlockStateProperties.AGE_7)).intValue() / 7.0F);
        } else if (state.m_61138_(BlockStateProperties.AGE_2)) {
            addMaturityTooltip(tooltip, (float) ((Integer) state.m_61143_(BlockStateProperties.AGE_2)).intValue() / 2.0F);
        } else if (state.m_61138_(BlockStateProperties.AGE_3) && (block instanceof SweetBerryBushBlock || block instanceof NetherWartBlock)) {
            addMaturityTooltip(tooltip, (float) ((Integer) state.m_61143_(BlockStateProperties.AGE_3)).intValue() / 3.0F);
        }
    }

    private static void addMaturityTooltip(ITooltip tooltip, float growthValue) {
        growthValue *= 100.0F;
        if (growthValue < 100.0F) {
            tooltip.add(Component.translatable("tooltip.jade.crop_growth", IThemeHelper.get().info(String.format("%.0f%%", growthValue))));
        } else {
            tooltip.add(Component.translatable("tooltip.jade.crop_growth", IThemeHelper.get().success(Component.translatable("tooltip.jade.crop_mature"))));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Identifiers.MC_CROP_PROGRESS;
    }
}