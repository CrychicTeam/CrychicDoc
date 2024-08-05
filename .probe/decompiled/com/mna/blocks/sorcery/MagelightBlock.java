package com.mna.blocks.sorcery;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.WaterloggableBlock;
import com.mna.blocks.tileentities.MagelightTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MagelightBlock extends WaterloggableBlock implements IDontCreateBlockItem, EntityBlock {

    protected static final VoxelShape SHAPE = Block.box(5.0, 5.0, 5.0, 11.0, 11.0, 11.0);

    public MagelightBlock() {
        super(BlockBehaviour.Properties.of().strength(0.0F).noOcclusion().noCollission().lightLevel(state -> 15).noLootTable(), false);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MagelightTile(pos, state);
    }

    @Nullable
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return BlockPathTypes.OPEN;
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        for (int i = 0; i < 50; i++) {
            level.addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, -0.25 + Math.random() * 0.5, Math.random() * 0.25, -0.25 + Math.random() * 0.5);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (!world.isClientSide()) {
            ItemStack heldItem = player.m_21120_(hand);
            if (heldItem.getItem() instanceof DyeItem) {
                this.setLightColor(world, pos, ((DyeItem) heldItem.getItem()).getDyeColor());
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    public void setLightColor(Level world, BlockPos pos, DyeColor color) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te != null && te instanceof MagelightTile tile) {
            tile.setColor(color.getFireworkColor());
        }
    }

    public void setLightColor(Level world, BlockPos pos, int color) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te != null && te instanceof MagelightTile tile) {
            tile.setColor(color);
        }
    }
}