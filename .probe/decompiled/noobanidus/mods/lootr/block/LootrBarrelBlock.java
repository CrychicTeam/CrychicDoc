package noobanidus.mods.lootr.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.model.data.ModelProperty;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.block.entities.LootrBarrelBlockEntity;
import noobanidus.mods.lootr.util.ChestUtil;

public class LootrBarrelBlock extends BarrelBlock {

    public static final ModelProperty<Boolean> OPENED = new ModelProperty<>();

    public LootrBarrelBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public float getExplosionResistance() {
        return LootrAPI.getExplosionResistance(this, super.m_7325_());
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.m_60713_(pNewState.m_60734_())) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof Container) {
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
            if (pState.m_155947_() && (!pState.m_60713_(pNewState.m_60734_()) || !pNewState.m_155947_())) {
                pLevel.removeBlockEntity(pPos);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
        if (player.m_6144_()) {
            ChestUtil.handleLootSneak(this, world, pos, player);
        } else {
            ChestUtil.handleLootChest(this, world, pos, player);
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LootrBarrelBlockEntity(pos, state);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int id, int param) {
        super.m_8133_(state, world, pos, id, param);
        BlockEntity tile = world.getBlockEntity(pos);
        return tile != null && tile.triggerEvent(id, param);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.m_7702_(pPos) instanceof LootrBarrelBlockEntity barrel) {
            barrel.recheckOpen();
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public float getDestroyProgress(BlockState blockState0, Player player1, BlockGetter blockGetter2, BlockPos blockPos3) {
        return LootrAPI.getDestroyProgress(blockState0, player1, blockGetter2, blockPos3, super.m_5880_(blockState0, player1, blockGetter2, blockPos3));
    }

    @Override
    public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
        return LootrAPI.getAnalogOutputSignal(pBlockState, pLevel, pPos, 0);
    }
}