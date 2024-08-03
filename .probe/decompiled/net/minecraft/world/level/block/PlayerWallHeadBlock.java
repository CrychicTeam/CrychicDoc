package net.minecraft.world.level.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

public class PlayerWallHeadBlock extends WallSkullBlock {

    protected PlayerWallHeadBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(SkullBlock.Types.PLAYER, blockBehaviourProperties0);
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, @Nullable LivingEntity livingEntity3, ItemStack itemStack4) {
        Blocks.PLAYER_HEAD.setPlacedBy(level0, blockPos1, blockState2, livingEntity3, itemStack4);
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState0, LootParams.Builder lootParamsBuilder1) {
        return Blocks.PLAYER_HEAD.m_49635_(blockState0, lootParamsBuilder1);
    }
}