package org.violetmoon.zeta.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.violetmoon.zeta.block.ext.IZetaBlockExtensions;
import org.violetmoon.zeta.module.IDisableable;
import org.violetmoon.zeta.registry.CreativeTabManager;

public interface IZetaBlock extends IZetaBlockExtensions, IDisableable<IZetaBlock> {

    default Block getBlock() {
        return (Block) this;
    }

    default Block setCreativeTab(ResourceKey<CreativeModeTab> tab) {
        Block b = this.getBlock();
        CreativeTabManager.addToCreativeTab(tab, b);
        return b;
    }

    default Block setCreativeTab(ResourceKey<CreativeModeTab> tab, ItemLike parent, boolean behindParent) {
        Block b = this.getBlock();
        CreativeTabManager.addToCreativeTabNextTo(tab, b, parent, behindParent);
        return b;
    }

    @Override
    default int getFlammabilityZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (state.m_61148_().containsKey(BlockStateProperties.WATERLOGGED) && (Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            return 0;
        } else {
            SoundType totallyMaterialTrustMeImADolphin = state.m_60827_();
            if (totallyMaterialTrustMeImADolphin == SoundType.WOOL) {
                return 60;
            } else if (totallyMaterialTrustMeImADolphin != SoundType.WOOD && !state.m_278200_()) {
                ResourceLocation loc = BuiltInRegistries.BLOCK.getKey(state.m_60734_());
                return loc == null || !loc.getPath().endsWith("_log") && !loc.getPath().endsWith("_wood") ? 0 : 5;
            } else {
                return 20;
            }
        }
    }

    @Override
    default int getFireSpreadSpeedZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (state.m_61148_().containsKey(BlockStateProperties.WATERLOGGED) && (Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            return 0;
        } else {
            SoundType gaming = state.m_60827_();
            if (gaming == SoundType.WOOL) {
                return 30;
            } else {
                return gaming != SoundType.WOOD && !state.m_278200_() ? 0 : 5;
            }
        }
    }
}