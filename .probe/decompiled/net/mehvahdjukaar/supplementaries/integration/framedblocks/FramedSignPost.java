package net.mehvahdjukaar.supplementaries.integration.framedblocks;

import net.mehvahdjukaar.supplementaries.common.block.tiles.SignPostBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class FramedSignPost {

    public static BlockState framedFence = Blocks.OAK_FENCE.defaultBlockState();

    public static ModelData getModelData(BlockState mimic) {
        return ModelData.EMPTY;
    }

    public static Block tryGettingFramedBlock(Block targetBlock, Level world, BlockPos blockpos) {
        return null;
    }

    public static boolean handleInteraction(SignPostBlockTile tile, Player player, InteractionHand handIn, ItemStack itemstack, Level level, BlockPos pos) {
        return false;
    }
}