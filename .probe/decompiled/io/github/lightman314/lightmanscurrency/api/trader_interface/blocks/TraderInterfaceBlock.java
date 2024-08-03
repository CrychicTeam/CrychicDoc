package io.github.lightman314.lightmanscurrency.api.trader_interface.blocks;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IEasyEntityBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IOwnableBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.RotatableBlock;
import io.github.lightman314.lightmanscurrency.api.trader_interface.blockentity.TraderInterfaceBlockEntity;
import io.github.lightman314.lightmanscurrency.common.emergency_ejection.EjectionData;
import io.github.lightman314.lightmanscurrency.common.emergency_ejection.EjectionSaveData;
import io.github.lightman314.lightmanscurrency.common.items.TooltipItem;
import io.github.lightman314.lightmanscurrency.util.BlockEntityUtil;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.NonNullSupplier;

public abstract class TraderInterfaceBlock extends RotatableBlock implements IEasyEntityBlock, IOwnableBlock {

    protected TraderInterfaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult result) {
        if (!level.isClientSide) {
            TraderInterfaceBlockEntity blockEntity = this.getBlockEntity(level, pos, state);
            if (blockEntity != null) {
                BlockEntityUtil.sendUpdatePacket(blockEntity);
                blockEntity.openMenu(player);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity player, @Nonnull ItemStack stack) {
        if (!level.isClientSide) {
            TraderInterfaceBlockEntity blockEntity = this.getBlockEntity(level, pos, state);
            if (blockEntity != null) {
                blockEntity.initOwner(player);
            }
        }
    }

    @Override
    public void playerWillDestroy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
        TraderInterfaceBlockEntity blockEntity = this.getBlockEntity(level, pos, state);
        if (blockEntity != null) {
            if (!blockEntity.isOwner(player)) {
                return;
            }
            InventoryUtil.dumpContents(level, pos, blockEntity.getContents(level, pos, state, !player.isCreative()));
            blockEntity.flagAsRemovable();
        }
        super.m_5707_(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, BlockState newState, boolean flag) {
        if (state.m_60734_() != newState.m_60734_()) {
            if (!level.isClientSide) {
                TraderInterfaceBlockEntity blockEntity = this.getBlockEntity(level, pos, state);
                if (blockEntity != null) {
                    if (!blockEntity.allowRemoval()) {
                        LightmansCurrency.LogError("Trader block at " + pos.m_123341_() + " " + pos.m_123342_() + " " + pos.m_123343_() + " was broken by illegal means!");
                        LightmansCurrency.LogError("Activating emergency eject protocol.");
                        EjectionData data = EjectionData.create(level, pos, state, blockEntity);
                        EjectionSaveData.HandleEjectionData(level, pos, data);
                        blockEntity.flagAsRemovable();
                        try {
                            this.onInvalidRemoval(state, level, pos, blockEntity);
                        } catch (Throwable var9) {
                            var9.printStackTrace();
                        }
                    } else {
                        LightmansCurrency.LogInfo("Trader block was broken by legal means!");
                    }
                }
            }
            super.m_6810_(state, level, pos, newState, flag);
        }
    }

    protected abstract void onInvalidRemoval(BlockState var1, Level var2, BlockPos var3, TraderInterfaceBlockEntity var4);

    @Override
    public boolean canBreak(@Nonnull Player player, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        TraderInterfaceBlockEntity be = this.getBlockEntity(level, pos, state);
        return be == null ? true : be.isOwner(player);
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return this.createBlockEntity(pos, state);
    }

    protected abstract BlockEntity createBlockEntity(BlockPos var1, BlockState var2);

    protected abstract BlockEntityType<?> interfaceType();

    @Nonnull
    @Override
    public Collection<BlockEntityType<?>> getAllowedTypes() {
        return ImmutableList.of(this.interfaceType());
    }

    protected final TraderInterfaceBlockEntity getBlockEntity(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity be = level.m_7702_(pos);
        return be instanceof TraderInterfaceBlockEntity ? (TraderInterfaceBlockEntity) be : null;
    }

    protected NonNullSupplier<List<Component>> getItemTooltips() {
        return ArrayList::new;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        TooltipItem.addTooltip(tooltip, this.getItemTooltips());
        super.m_5871_(stack, level, tooltip, flagIn);
    }

    @Override
    public boolean isSignalSource(@Nonnull BlockState state) {
        return true;
    }

    public ItemStack getDropBlockItem(BlockState state, TraderInterfaceBlockEntity traderInterface) {
        return new ItemStack(state.m_60734_());
    }
}