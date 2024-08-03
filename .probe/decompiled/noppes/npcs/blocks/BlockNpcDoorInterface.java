package noppes.npcs.blocks;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.storage.loot.LootParams;

public abstract class BlockNpcDoorInterface extends DoorBlock implements EntityBlock {

    public BlockNpcDoorInterface(BlockBehaviour.Properties properties) {
        super(properties, BlockSetType.STONE);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        super.m_6810_(state, level, pos, newState, isMoving);
        level.removeBlockEntity(pos);
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState0, LootParams.Builder lootParamsBuilder1) {
        return Collections.emptyList();
    }

    @Override
    public void playerDestroy(Level p_180657_1_, Player p_180657_2_, BlockPos p_180657_3_, BlockState p_180657_4_, @Nullable BlockEntity p_180657_5_, ItemStack p_180657_6_) {
        p_180657_2_.awardStat(Stats.BLOCK_MINED.get(this));
        p_180657_2_.causeFoodExhaustion(0.005F);
        m_49881_(p_180657_4_, p_180657_1_, p_180657_3_, p_180657_5_, p_180657_2_, p_180657_6_);
    }
}