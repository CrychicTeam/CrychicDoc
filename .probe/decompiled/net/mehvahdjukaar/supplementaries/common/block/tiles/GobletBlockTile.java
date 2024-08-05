package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.block.IOwnerProtected;
import net.mehvahdjukaar.moonlight.api.block.ISoftFluidTankProvider;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.model.IExtraModelDataProvider;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GobletBlockTile extends BlockEntity implements ISoftFluidTankProvider, IOwnerProtected, IExtraModelDataProvider {

    private UUID owner = null;

    public final SoftFluidTank fluidHolder = SoftFluidTank.create(1);

    public static final ModelDataKey<SoftFluid> FLUID_ID = ModBlockProperties.FLUID;

    public GobletBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.GOBLET_TILE.get(), pos, state);
    }

    @Override
    public void addExtraModelData(ExtraModelData.Builder builder) {
        builder.with(FLUID_ID, this.getSoftFluidTank().getFluidValue());
    }

    @Nullable
    @Override
    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public void setChanged() {
        if (this.f_58857_ != null) {
            this.f_58857_.updateNeighborsAt(this.f_58858_, this.m_58900_().m_60734_());
            int light = this.fluidHolder.getFluidValue().getLuminosity();
            if (light != (Integer) this.m_58900_().m_61143_(ModBlockProperties.LIGHT_LEVEL_0_15)) {
                this.f_58857_.setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(ModBlockProperties.LIGHT_LEVEL_0_15, light), 2);
            }
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 2);
            super.setChanged();
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public boolean handleInteraction(Player player, InteractionHand hand) {
        if (this.fluidHolder.interactWithPlayer(player, hand, this.f_58857_, this.f_58858_)) {
            return true;
        } else if (!player.m_6144_() && (Boolean) CommonConfigs.Building.GOBLET_DRINK.get()) {
            boolean b = this.fluidHolder.tryDrinkUpFluid(player, this.f_58857_);
            if (b && player instanceof ServerPlayer serverPlayer) {
                Advancement advancement = this.f_58857_.getServer().getAdvancements().getAdvancement(Supplementaries.res("nether/goblet"));
                if (advancement != null && !serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    serverPlayer.getAdvancements().award(advancement, "unlock");
                }
            }
            return b;
        } else {
            return false;
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        try {
            this.fluidHolder.load(compound);
        } catch (Exception var3) {
            Supplementaries.LOGGER.warn("Failed to load fluid container at {}:", this.m_58899_(), var3);
        }
        this.loadOwner(compound);
        if (this.f_58857_ != null && this.f_58857_.isClientSide) {
            this.requestModelReload();
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        try {
            this.fluidHolder.save(tag);
        } catch (Exception var3) {
            Supplementaries.LOGGER.warn("Failed to save fluid container at {}:", this.m_58899_(), var3);
        }
        this.saveOwner(tag);
    }

    @Override
    public SoftFluidTank getSoftFluidTank() {
        return this.fluidHolder;
    }
}