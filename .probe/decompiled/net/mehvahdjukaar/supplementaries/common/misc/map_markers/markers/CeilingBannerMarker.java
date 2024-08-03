package net.mehvahdjukaar.supplementaries.common.misc.map_markers.markers;

import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.ModMapMarkers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBannerBlock;
import org.jetbrains.annotations.Nullable;

public class CeilingBannerMarker extends ColoredMarker {

    public CeilingBannerMarker() {
        super(ModMapMarkers.BANNER_DECORATION_TYPE);
    }

    public CeilingBannerMarker(BlockPos pos, DyeColor color, @Nullable Component name) {
        super(ModMapMarkers.BANNER_DECORATION_TYPE, pos, color, name);
    }

    @Nullable
    public static CeilingBannerMarker getFromWorld(BlockGetter world, BlockPos pos) {
        Block block = world.getBlockState(pos).m_60734_();
        if (block instanceof AbstractBannerBlock && !(block instanceof WallBannerBlock) && !(block instanceof BannerBlock)) {
            DyeColor col = BlocksColorAPI.getColor(block);
            if (col != null) {
                Component var10000;
                label17: {
                    if (world.getBlockEntity(pos) instanceof Nameable n && n.hasCustomName()) {
                        var10000 = n.getCustomName();
                        break label17;
                    }
                    var10000 = null;
                }
                Component name = var10000;
                return new CeilingBannerMarker(pos, col, name);
            }
        }
        return null;
    }
}