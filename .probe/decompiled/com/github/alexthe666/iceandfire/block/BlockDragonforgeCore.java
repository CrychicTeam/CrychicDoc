package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class BlockDragonforgeCore extends BaseEntityBlock implements IDragonProof, INoTab {

    private static boolean keepInventory;

    private final int isFire;

    private final boolean activated;

    public BlockDragonforgeCore(int isFire, boolean activated) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).dynamicShape().strength(40.0F, 500.0F).sound(SoundType.METAL).lightLevel(state -> activated ? 15 : 0));
        this.isFire = isFire;
        this.activated = activated;
    }

    static String name(int dragonType, boolean activated) {
        return "dragonforge_%s_core%s".formatted(DragonType.getNameFromInt(dragonType), activated ? "" : "_disabled");
    }

    public static void setState(int dragonType, boolean active, Level worldIn, BlockPos pos) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        keepInventory = true;
        if (active) {
            if (dragonType == 0) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get().defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get().defaultBlockState(), 3);
            } else if (dragonType == 1) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE.get().defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE.get().defaultBlockState(), 3);
            } else if (dragonType == 2) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.get().defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.get().defaultBlockState(), 3);
            }
        } else if (dragonType == 0) {
            worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get().defaultBlockState(), 3);
            worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get().defaultBlockState(), 3);
        } else if (dragonType == 1) {
            worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get().defaultBlockState(), 3);
            worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get().defaultBlockState(), 3);
        } else if (dragonType == 2) {
            worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get().defaultBlockState(), 3);
            worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get().defaultBlockState(), 3);
        }
        keepInventory = false;
        if (tileentity != null) {
            tileentity.clearRemoved();
            worldIn.setBlockEntity(tileentity);
        }
    }

    @NotNull
    public PushReaction getPistonPushReaction(@NotNull BlockState state) {
        return PushReaction.BLOCK;
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult hit) {
        if (!player.m_6144_()) {
            if (worldIn.isClientSide) {
                IceAndFire.PROXY.setRefrencedTE(worldIn.getBlockEntity(pos));
            } else {
                MenuProvider inamedcontainerprovider = this.m_7246_(state, worldIn, pos);
                if (inamedcontainerprovider != null) {
                    player.openMenu(inamedcontainerprovider);
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    public ItemStack getItem(Level worldIn, BlockPos pos, BlockState state) {
        if (this.isFire == 0) {
            return new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get().asItem());
        } else if (this.isFire == 1) {
            return new ItemStack(IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get().asItem());
        } else {
            return this.isFire == 2 ? new ItemStack(IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get().asItem()) : new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get().asItem());
        }
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof TileEntityDragonforge) {
            Containers.dropContents(worldIn, pos, (TileEntityDragonforge) tileentity);
            worldIn.updateNeighbourForOutputSignal(pos, this);
            worldIn.removeBlockEntity(pos);
        }
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState blockState, Level worldIn, @NotNull BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(worldIn.getBlockEntity(pos));
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    public boolean shouldBeInTab() {
        return !this.activated;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        return m_152132_(entityType, IafTileEntityRegistry.DRAGONFORGE_CORE.get(), TileEntityDragonforge::tick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityDragonforge(pos, state, this.isFire);
    }
}