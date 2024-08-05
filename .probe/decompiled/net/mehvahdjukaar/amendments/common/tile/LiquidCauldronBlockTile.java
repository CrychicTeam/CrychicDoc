package net.mehvahdjukaar.amendments.common.tile;

import net.mehvahdjukaar.amendments.common.LiquidMixer;
import net.mehvahdjukaar.amendments.common.block.DyeCauldronBlock;
import net.mehvahdjukaar.amendments.common.block.LiquidCauldronBlock;
import net.mehvahdjukaar.amendments.common.block.ModCauldronBlock;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.block.ISoftFluidTankProvider;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.model.IExtraModelDataProvider;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LiquidCauldronBlockTile extends BlockEntity implements IExtraModelDataProvider, ISoftFluidTankProvider {

    public static final ModelDataKey<SoftFluid> FLUID = new ModelDataKey<>(SoftFluid.class);

    private final SoftFluidTank fluidTank;

    public SoftFluidTank makeTank(BlockState blockState) {
        return blockState.m_60734_() instanceof DyeCauldronBlock ? this.createCauldronDyeTank() : this.createCauldronLiquidTank();
    }

    private boolean canMixPotions() {
        CommonConfigs.MixingMode config = (CommonConfigs.MixingMode) CommonConfigs.POTION_MIXING.get();
        return config == CommonConfigs.MixingMode.ON || config == CommonConfigs.MixingMode.ONLY_BOILING && (Boolean) this.m_58900_().m_61143_(LiquidCauldronBlock.BOILING);
    }

    public LiquidCauldronBlockTile(BlockPos blockPos, BlockState blockState) {
        super((BlockEntityType<?>) ModRegistry.LIQUID_CAULDRON_TILE.get(), blockPos, blockState);
        this.fluidTank = this.makeTank(blockState);
    }

    @Override
    public void addExtraModelData(ExtraModelData.Builder builder) {
        builder.with(FLUID, this.fluidTank.getFluidValue());
    }

    @Override
    public SoftFluidTank getSoftFluidTank() {
        return this.fluidTank;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.fluidTank.load(compound);
        if (this.f_58857_ != null && this.f_58857_.isClientSide) {
            this.fluidTank.refreshTintCache();
            this.requestModelReload();
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.fluidTank.save(tag);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    @Override
    public void setChanged() {
        if (this.f_58857_ != null) {
            this.f_58857_.updateNeighborsAt(this.f_58858_, this.m_58900_().m_60734_());
            BlockState state = this.m_58900_();
            if (state.m_60734_() instanceof ModCauldronBlock cb) {
                state = cb.updateStateOnFluidChange(state, this.f_58857_, this.f_58858_, this.fluidTank.getFluid());
            }
            if (state != this.m_58900_()) {
                this.f_58857_.setBlockAndUpdate(this.f_58858_, state);
            }
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), state, 2);
            super.setChanged();
        }
    }

    public boolean handleInteraction(Player player, InteractionHand hand) {
        if (this.fluidTank.interactWithPlayer(player, hand, this.f_58857_, this.f_58858_)) {
            this.setChanged();
            return true;
        } else {
            return false;
        }
    }

    public void consumeOneLayer() {
        this.fluidTank.getFluid().shrink(1);
        this.setChanged();
    }

    public SoftFluidTank createCauldronLiquidTank() {
        return new SoftFluidTank(PlatHelper.getPlatform().isFabric() ? 3 : 4) {

            @Override
            public boolean isFluidCompatible(SoftFluidStack fluidStack) {
                if (fluidStack.is(BuiltInSoftFluids.WATER.get())) {
                    return false;
                } else {
                    return LiquidCauldronBlockTile.this.canMixPotions() && fluidStack.is(BuiltInSoftFluids.POTION.get()) && fluidStack.is(this.getFluidValue()) ? this.fluidStack.getTag().getString("Bottle").equals(fluidStack.getTag().getString("Bottle")) : super.isFluidCompatible(fluidStack);
                }
            }

            @Override
            protected void addFluidOntoExisting(SoftFluidStack incoming) {
                if (LiquidCauldronBlockTile.this.canMixPotions() && incoming.is(BuiltInSoftFluids.POTION.get())) {
                    LiquidMixer.mixPotions(this.fluidStack, incoming);
                    this.needsColorRefresh = true;
                }
                super.addFluidOntoExisting(incoming);
            }
        };
    }

    public SoftFluidTank createCauldronDyeTank() {
        return new SoftFluidTank(3) {

            @Override
            public boolean isFluidCompatible(SoftFluidStack fluidStack) {
                return fluidStack.is(ModRegistry.DYE_SOFT_FLUID.get()) && fluidStack.is(this.getFluidValue()) ? true : super.isFluidCompatible(fluidStack);
            }

            @Override
            protected void addFluidOntoExisting(SoftFluidStack fluidStack) {
                if (fluidStack.is(ModRegistry.DYE_SOFT_FLUID.get())) {
                    LiquidMixer.mixDye(this.fluidStack, fluidStack);
                }
                super.addFluidOntoExisting(fluidStack);
            }
        };
    }
}