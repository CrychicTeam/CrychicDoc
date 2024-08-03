package net.mehvahdjukaar.moonlight.api.block;

import dev.architectury.injectables.annotations.PlatformOnly;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public abstract class MimicBlock extends Block {

    protected MimicBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos) {
        if (worldIn.getBlockEntity(pos) instanceof IBlockHolder tile) {
            BlockState mimicState = tile.getHeldBlock();
            if (!mimicState.m_60795_() && !(mimicState.m_60734_() instanceof MimicBlock)) {
                return Math.min(super.m_5880_(state, player, worldIn, pos), mimicState.m_60625_(player, worldIn, pos));
            }
        }
        return super.m_5880_(state, player, worldIn, pos);
    }

    @PlatformOnly({ "forge" })
    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
        if (world.m_7702_(pos) instanceof IBlockHolder tile) {
            BlockState mimicState = tile.getHeldBlock();
            if (!mimicState.m_60795_()) {
                return mimicState.m_60827_();
            }
        }
        return super.getSoundType(state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.m_49635_(state, builder);
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof IBlockHolder tile) {
            BlockState heldState = tile.getHeldBlock();
            if (builder.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof ServerPlayer player && !ForgeHelper.canHarvestBlock(heldState, builder.getLevel(), BlockPos.containing(builder.getParameter(LootContextParams.ORIGIN)), player)) {
                return drops;
            }
            List<ItemStack> newDrops = heldState.m_287290_(builder);
            drops.addAll(newDrops);
        }
        return drops;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @PlatformOnly({ "forge" })
    public float getExplosionResistance(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
        if (world.getBlockEntity(pos) instanceof IBlockHolder tile) {
            BlockState mimicState = tile.getHeldBlock();
            if (!mimicState.m_60795_() && !(mimicState.m_60734_() instanceof MimicBlock)) {
                return Math.max(ForgeHelper.getExplosionResistance(mimicState, (Level) world, pos, explosion), state.m_60734_().getExplosionResistance());
            }
        }
        return 2.0F;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof IBlockHolder tile) {
            BlockState mimic = tile.getHeldBlock();
            return mimic.m_60734_().getCloneItemStack(level, pos, state);
        } else {
            return super.getCloneItemStack(level, pos, state);
        }
    }
}