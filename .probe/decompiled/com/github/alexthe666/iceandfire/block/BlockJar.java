package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BlockJar extends BaseEntityBlock {

    protected static final VoxelShape AABB = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);

    public Item itemBlock;

    private final boolean empty;

    private final int pixieType;

    public BlockJar(int pixieType) {
        super(pixieType != -1 ? BlockBehaviour.Properties.of().mapColor(MapColor.NONE).instrument(NoteBlockInstrument.HAT).noOcclusion().dynamicShape().strength(1.0F, 2.0F).sound(SoundType.GLASS).lightLevel(state -> pixieType == -1 ? 0 : 10).dropsLike(IafBlockRegistry.JAR_EMPTY.get()) : BlockBehaviour.Properties.of().mapColor(MapColor.NONE).instrument(NoteBlockInstrument.HAT).noOcclusion().dynamicShape().strength(1.0F, 2.0F).sound(SoundType.GLASS));
        this.empty = pixieType == -1;
        this.pixieType = pixieType;
    }

    static String name(int pixieType) {
        return pixieType == -1 ? "pixie_jar_empty" : "pixie_jar_%d".formatted(pixieType);
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AABB;
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AABB;
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        this.dropPixie(worldIn, pos);
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }

    public void dropPixie(Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityJar && ((TileEntityJar) world.getBlockEntity(pos)).hasPixie) {
            ((TileEntityJar) world.getBlockEntity(pos)).releasePixie();
        }
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult resultIn) {
        if (!this.empty && world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityJar && ((TileEntityJar) world.getBlockEntity(pos)).hasPixie && ((TileEntityJar) world.getBlockEntity(pos)).hasProduced) {
            ((TileEntityJar) world.getBlockEntity(pos)).hasProduced = false;
            ItemEntity item = new ItemEntity(world, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, new ItemStack(IafItemRegistry.PIXIE_DUST.get()));
            if (!world.isClientSide) {
                world.m_7967_(item);
            }
            world.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, IafSoundRegistry.PIXIE_HURT, SoundSource.NEUTRAL, 1.0F, 1.0F, false);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void setPlacedBy(Level world, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity placer, @NotNull ItemStack stack) {
        if (world.getBlockEntity(pos) instanceof TileEntityJar) {
            TileEntityJar jar = (TileEntityJar) world.getBlockEntity(pos);
            if (!this.empty) {
                jar.hasPixie = true;
                jar.pixieType = this.pixieType;
            } else {
                jar.hasPixie = false;
            }
            jar.m_6596_();
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        return m_152132_(entityType, IafTileEntityRegistry.PIXIE_JAR.get(), TileEntityJar::tick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityJar(pos, state, this.empty);
    }
}