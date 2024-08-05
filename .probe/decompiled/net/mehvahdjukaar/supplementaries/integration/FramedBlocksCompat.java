package net.mehvahdjukaar.supplementaries.integration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SignPostBlockTile;
import net.mehvahdjukaar.supplementaries.integration.forge.FramedBlocksCompatImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FramedBlocksCompat {

    @ExpectPlatform
    @Transformed
    public static BlockState getFramedFence() {
        return FramedBlocksCompatImpl.getFramedFence();
    }

    @ExpectPlatform
    @Transformed
    public static Block tryGettingFramedBlock(Block targetBlock, Level world, BlockPos blockpos) {
        return FramedBlocksCompatImpl.tryGettingFramedBlock(targetBlock, world, blockpos);
    }

    @ExpectPlatform
    @Transformed
    public static boolean interactWithFramedSignPost(SignPostBlockTile tile, Player player, InteractionHand handIn, ItemStack itemstack, Level level, BlockPos pos) {
        return FramedBlocksCompatImpl.interactWithFramedSignPost(tile, player, handIn, itemstack, level, pos);
    }

    @OnlyIn(Dist.CLIENT)
    @ExpectPlatform
    @Transformed
    public static ExtraModelData getModelData(BlockState mimic) {
        return FramedBlocksCompatImpl.getModelData(mimic);
    }
}