package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.model.IExtraModelDataProvider;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FaucetBlock;
import net.mehvahdjukaar.supplementaries.common.block.faucet.FaucetItemSource;
import net.mehvahdjukaar.supplementaries.common.block.faucet.FaucetSource;
import net.mehvahdjukaar.supplementaries.common.block.faucet.FaucetTarget;
import net.mehvahdjukaar.supplementaries.common.block.faucet.FluidOffer;
import net.mehvahdjukaar.supplementaries.common.utils.ItemsUtil;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FaucetBlockTile extends BlockEntity implements IExtraModelDataProvider {

    private static final List<FaucetSource.BlState> BLOCK_INTERACTIONS = new ArrayList();

    private static final List<FaucetSource.Tile> TILE_INTERACTIONS = new ArrayList();

    private static final List<FaucetSource.Fluid> SOURCE_FLUID_INTERACTIONS = new ArrayList();

    private static final List<FaucetItemSource> ITEM_INTERACTIONS = new ArrayList();

    private static final List<FaucetTarget.BlState> TARGET_BLOCK_INTERACTIONS = new ArrayList();

    private static final List<FaucetTarget.Tile> TARGET_TILE_INTERACTIONS = new ArrayList();

    private static final List<FaucetTarget.Fluid> TARGET_FLUID_INTERACTIONS = new ArrayList();

    public static final ModelDataKey<SoftFluid> FLUID = ModBlockProperties.FLUID;

    public static final ModelDataKey<Integer> FLUID_COLOR = ModBlockProperties.FLUID_COLOR;

    public static final int COOLDOWN_PER_BOTTLE = 20;

    private int transferCooldown = 0;

    public final SoftFluidTank tempFluidHolder = SoftFluidTank.create(5);

    public static final Predicate<Entity> NON_PLAYER = e -> e.isAlive() && !(e instanceof Player);

    public FaucetBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.FAUCET_TILE.get(), pos, state);
    }

    @Override
    public void addExtraModelData(ExtraModelData.Builder builder) {
        int color = -1;
        if (this.f_58857_ != null) {
            color = this.tempFluidHolder.getCachedFlowingColor(this.f_58857_, this.f_58858_);
        }
        builder.with(FLUID, this.tempFluidHolder.getFluidValue());
        builder.with(FLUID_COLOR, color);
    }

    public void updateLight() {
        if (this.f_58857_ != null) {
            int light = this.tempFluidHolder.getFluidValue().getLuminosity();
            if (light != 0) {
                light = (int) Mth.clamp((float) light / 2.0F, 1.0F, 7.0F);
            }
            if (light != (Integer) this.m_58900_().m_61143_(FaucetBlock.LIGHT_LEVEL)) {
                this.f_58857_.setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(FaucetBlock.LIGHT_LEVEL, light), 2);
            }
        }
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.m_58899_().offset(0, -1, 0), this.m_58899_().offset(1, 1, 1));
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, FaucetBlockTile tile) {
        if (tile.transferCooldown > 0) {
            tile.transferCooldown--;
        } else if (tile.isOpen()) {
            int cooldown = tile.tryExtract(pLevel, pPos, pState, false);
            tile.transferCooldown += cooldown;
        }
    }

    public boolean updateContainedFluidVisuals(Level level, BlockPos pos, BlockState state) {
        boolean r = this.tryExtract(level, pos, state, true) != 0;
        this.updateLight();
        this.requestModelReload();
        return r;
    }

    private int tryExtract(Level level, BlockPos pos, BlockState state, boolean justVisual) {
        Direction dir = (Direction) state.m_61143_(FaucetBlock.FACING);
        BlockPos behind = pos.relative(dir.getOpposite());
        BlockState backState = level.getBlockState(behind);
        this.tempFluidHolder.clear();
        if (backState.m_60795_()) {
            return 0;
        } else {
            Integer filledAmount = this.runInteractions(BLOCK_INTERACTIONS, level, dir, behind, backState, justVisual);
            if (filledAmount != null) {
                return filledAmount;
            } else {
                BlockEntity tileBack = level.getBlockEntity(behind);
                if (tileBack != null) {
                    filledAmount = this.runInteractions(TILE_INTERACTIONS, level, dir, behind, tileBack, justVisual);
                    if (filledAmount != null) {
                        return filledAmount;
                    }
                }
                if (!this.isConnectedBelow() && !justVisual && ((Boolean) CommonConfigs.Redstone.FAUCET_DROP_ITEMS.get() || (Boolean) CommonConfigs.Redstone.FAUCET_FILL_ENTITIES.get())) {
                    for (FaucetItemSource bi : ITEM_INTERACTIONS) {
                        ItemStack removed = bi.tryExtractItem(level, behind, backState, dir, tileBack);
                        if (!removed.isEmpty()) {
                            if ((!(Boolean) CommonConfigs.Redstone.FAUCET_FILL_ENTITIES.get() || !this.fillEntityBelow(removed, dir)) && (Boolean) CommonConfigs.Redstone.FAUCET_DROP_ITEMS.get()) {
                                this.drop(removed);
                            }
                            return 20;
                        }
                    }
                }
                FluidState fluidState = level.getFluidState(behind);
                filledAmount = this.runInteractions(SOURCE_FLUID_INTERACTIONS, level, dir, behind, fluidState, justVisual);
                return filledAmount != null ? filledAmount : 0;
            }
        }
    }

    @Nullable
    private <T, S extends FaucetSource<T>> Integer runInteractions(List<S> interactions, Level level, Direction dir, BlockPos pos, T source, boolean justVisual) {
        for (S inter : interactions) {
            FluidOffer fluid = inter.getProvidedFluid(level, pos, dir, source);
            if (fluid != null) {
                if (justVisual) {
                    this.tempFluidHolder.setFluid(fluid.fluid());
                    return 20;
                }
                Integer amountFilled = this.tryFillingBlockBelow(fluid);
                if (amountFilled != null) {
                    if (amountFilled == 0) {
                        return 0;
                    }
                    inter.drain(level, pos, dir, source, amountFilled);
                    return amountFilled * 20;
                }
            }
        }
        return null;
    }

    private Integer tryFillingBlockBelow(FluidOffer fluid) {
        BlockPos below = this.f_58858_.below();
        BlockState belowState = this.f_58857_.getBlockState(below);
        for (FaucetTarget.BlState bi : TARGET_BLOCK_INTERACTIONS) {
            Integer res = bi.fill(this.f_58857_, below, belowState, fluid.fluid(), fluid.minAmount());
            if (res != null) {
                return res;
            }
        }
        BlockEntity tileBelow = this.f_58857_.getBlockEntity(below);
        if (tileBelow != null) {
            for (FaucetTarget.Tile bix : TARGET_TILE_INTERACTIONS) {
                Integer res = bix.fill(this.f_58857_, below, tileBelow, fluid.fluid(), fluid.minAmount());
                if (res != null) {
                    return res;
                }
            }
        }
        FluidState fluidState = belowState.m_60819_();
        for (FaucetTarget.Fluid bixx : TARGET_FLUID_INTERACTIONS) {
            Integer res = bixx.fill(this.f_58857_, below, fluidState, fluid.fluid(), fluid.minAmount());
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    public boolean isOpen() {
        return (Boolean) this.m_58900_().m_61143_(BlockStateProperties.POWERED) ^ (Boolean) this.m_58900_().m_61143_(BlockStateProperties.ENABLED);
    }

    public boolean hasWater() {
        return (Boolean) this.m_58900_().m_61143_(FaucetBlock.HAS_WATER);
    }

    public boolean isConnectedBelow() {
        return (Boolean) this.m_58900_().m_61143_(FaucetBlock.CONNECTED);
    }

    private void drop(ItemStack extracted) {
        BlockPos pos = this.f_58858_;
        ItemEntity drop = new ItemEntity(this.f_58857_, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5, extracted);
        drop.m_20256_(new Vec3(0.0, 0.0, 0.0));
        this.f_58857_.m_7967_(drop);
        float f = (this.f_58857_.random.nextFloat() - 0.5F) / 4.0F;
        this.f_58857_.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.3F, 0.5F + f);
    }

    private boolean fillEntityBelow(ItemStack stack, Direction direction) {
        List<Entity> list = this.f_58857_.getEntities((Entity) null, new AABB(this.f_58858_).move(0.0, -0.75, 0.0), NON_PLAYER);
        Collections.shuffle(list);
        for (Entity o : list) {
            stack = ItemsUtil.tryAddingItem(stack, this.f_58857_, direction, o);
            if (stack.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.transferCooldown = compound.getInt("TransferCooldown");
        this.tempFluidHolder.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("TransferCooldown", this.transferCooldown);
        this.tempFluidHolder.save(tag);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public static void registerInteraction(Object interaction) {
        boolean success = false;
        if (interaction instanceof FaucetSource.BlState bs) {
            BLOCK_INTERACTIONS.add(bs);
            success = true;
        }
        if (interaction instanceof FaucetSource.Tile ts) {
            TILE_INTERACTIONS.add(ts);
            success = true;
        }
        if (interaction instanceof FaucetSource.Fluid bs) {
            SOURCE_FLUID_INTERACTIONS.add(bs);
            success = true;
        }
        if (interaction instanceof FaucetTarget.BlState tb) {
            TARGET_BLOCK_INTERACTIONS.add(tb);
            success = true;
        }
        if (interaction instanceof FaucetTarget.Tile tt) {
            TARGET_TILE_INTERACTIONS.add(tt);
            success = true;
        }
        if (interaction instanceof FaucetTarget.Fluid tf) {
            TARGET_FLUID_INTERACTIONS.add(tf);
            success = true;
        }
        if (interaction instanceof FaucetItemSource is) {
            ITEM_INTERACTIONS.add(is);
            success = true;
        }
        if (!success) {
            throw new UnsupportedOperationException("Unsupported faucet interaction class: " + interaction.getClass().getSimpleName());
        }
    }

    public static <T> void removeDataInteractions(Collection<T> interactions) {
        for (T v : interactions) {
            if (v instanceof FaucetTarget fs) {
                BLOCK_INTERACTIONS.remove(fs);
            } else if (v instanceof FaucetItemSource fs) {
                ITEM_INTERACTIONS.remove(fs);
            }
        }
    }

    @FunctionalInterface
    public interface FillAction {

        int tryExecute(int var1);
    }
}