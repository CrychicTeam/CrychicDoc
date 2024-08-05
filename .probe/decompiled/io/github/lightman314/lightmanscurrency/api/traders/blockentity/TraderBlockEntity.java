package io.github.lightman314.lightmanscurrency.api.traders.blockentity;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.IServerTicker;
import io.github.lightman314.lightmanscurrency.api.misc.blockentity.EasyBlockEntity;
import io.github.lightman314.lightmanscurrency.api.misc.blockentity.IOwnableBlockEntity;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IRotatableBlock;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.taxes.ITaxCollector;
import io.github.lightman314.lightmanscurrency.api.taxes.TaxAPI;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.common.text.TextEntry;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.util.BlockEntityUtil;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public abstract class TraderBlockEntity<D extends TraderData> extends EasyBlockEntity implements IOwnableBlockEntity, IServerTicker {

    private long traderID = -1L;

    private CompoundTag customTrader = null;

    private boolean ignoreCustomTrader = false;

    private boolean legitimateBreak = false;

    public long getTraderID() {
        return this.traderID;
    }

    @Deprecated
    public void setTraderID(long traderID) {
        this.traderID = traderID;
    }

    public void flagAsLegitBreak() {
        this.legitimateBreak = true;
    }

    public boolean legitimateBreak() {
        return this.legitimateBreak;
    }

    public TraderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    private D buildTrader(Player owner, ItemStack placementStack) {
        if (this.customTrader != null) {
            D newTrader = this.fullyBuildCustomTrader();
            if (newTrader != null) {
                return newTrader;
            }
        }
        D newTrader = this.buildNewTrader();
        newTrader.getOwner().SetOwner(PlayerReference.of(owner));
        if (placementStack.hasCustomHoverName()) {
            newTrader.setCustomName(null, placementStack.getHoverName().getString());
        }
        return newTrader;
    }

    protected final D initCustomTrader() {
        try {
            return this.castOrNullify(TraderData.Deserialize(false, this.customTrader));
        } catch (Throwable var2) {
            LightmansCurrency.LogError("Error while attempting to load the custom trader!", var2);
            return null;
        }
    }

    protected final D fullyBuildCustomTrader() {
        try {
            D newTrader = this.initCustomTrader();
            this.moveCustomTrader(newTrader);
            return newTrader;
        } catch (Throwable var2) {
            LightmansCurrency.LogError("Error while attempting to load the custom trader!", var2);
            return null;
        }
    }

    protected final void moveCustomTrader(D customTrader) {
        if (customTrader != null) {
            customTrader.move(this.f_58857_, this.f_58858_);
        }
    }

    @Nonnull
    protected abstract D buildNewTrader();

    public final void saveCurrentTraderAsCustomTrader() {
        TraderData trader = this.getTraderData();
        if (trader != null) {
            this.customTrader = trader.save();
            this.ignoreCustomTrader = true;
            this.markDirty();
        }
    }

    @Nullable
    private CompoundTag getCurrentTraderAsTag() {
        TraderData trader = this.getTraderData();
        return trader != null ? trader.save() : null;
    }

    public void initialize(@Nonnull Player owner, @Nonnull ItemStack placementStack) {
        if (this.getTraderData() == null) {
            D newTrader = this.buildTrader(owner, placementStack);
            this.traderID = TraderSaveData.RegisterTrader(newTrader, owner);
            List<ITaxCollector> taxes = TaxAPI.GetPossibleTaxCollectorsFor(newTrader);
            taxes.forEach(e -> e.AcceptTaxable(newTrader));
            if (!taxes.isEmpty()) {
                TextEntry firstMessage = LCText.MESSAGE_TAX_COLLECTOR_PLACEMENT_TRADER;
                if (taxes.size() == 1 && ((ITaxCollector) taxes.get(0)).isServerEntry()) {
                    firstMessage = LCText.MESSAGE_TAX_COLLECTOR_PLACEMENT_TRADER_SERVER_ONLY;
                }
                EasyText.sendMessage(owner, firstMessage.get());
                EasyText.sendMessage(owner, LCText.MESSAGE_TAX_COLLECTOR_PLACEMENT_TRADER_INFO.get());
            }
            this.markDirty();
        }
    }

    public TraderData getRawTraderData() {
        return TraderSaveData.GetTrader(this.isClient(), this.traderID);
    }

    @Nullable
    public D getTraderData() {
        TraderData rawData = this.getRawTraderData();
        return rawData == null ? null : this.castOrNullify(rawData);
    }

    @Nullable
    protected abstract D castOrNullify(@Nonnull TraderData var1);

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.m_183515_(compound);
        compound.putLong("TraderID", this.traderID);
        if (this.customTrader != null) {
            compound.put("CustomTrader", this.customTrader);
        }
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.m_142466_(compound);
        if (compound.contains("TraderID", 4)) {
            this.traderID = compound.getLong("TraderID");
        }
        if (compound.contains("CustomTrader")) {
            this.customTrader = compound.getCompound("CustomTrader");
        }
    }

    @Override
    public void serverTick() {
        if (this.f_58857_ != null) {
            if (this.customTrader != null && !this.ignoreCustomTrader) {
                D customTrader = this.initCustomTrader();
                if (customTrader == null) {
                    LightmansCurrency.LogWarning("The trader block at " + this.f_58858_.m_123344_() + " could not properly load it's custom trader.");
                    this.customTrader = null;
                }
                if (customTrader.getLevel() == this.f_58857_.dimension() && this.f_58858_.equals(customTrader.getPos())) {
                    this.ignoreCustomTrader = true;
                } else {
                    this.moveCustomTrader(customTrader);
                    this.traderID = TraderSaveData.RegisterTrader(customTrader, null);
                    this.customTrader = null;
                    this.ignoreCustomTrader = true;
                    this.markDirty();
                    LightmansCurrency.LogInfo("Successfully loaded custom trader at " + this.f_58858_.m_123344_());
                }
            }
        }
    }

    public final void markDirty() {
        this.m_6596_();
        if (!this.isClient()) {
            BlockEntityUtil.sendUpdatePacket(this);
        }
    }

    public void onLoad() {
        if (this.f_58857_.isClientSide) {
            BlockEntityUtil.requestUpdatePacket(this);
        } else {
            this.moveCustomTrader(this.getTraderData());
        }
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        TraderData trader = this.getTraderData();
        if (trader != null) {
            Direction relativeSide = side;
            if (this.m_58900_().m_60734_() instanceof IRotatableBlock block) {
                relativeSide = IRotatableBlock.getRelativeSide(block.getFacing(this.m_58900_()), side);
            }
            return trader.getCapability(cap, relativeSide);
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public boolean canBreak(@Nullable Player player) {
        TraderData trader = this.getTraderData();
        return trader != null ? trader.hasPermission(player, "breakTrader") : true;
    }

    public void onBreak() {
        TraderSaveData.DeleteTrader(this.traderID);
    }

    public AABB getRenderBoundingBox() {
        return this.m_58900_() != null ? this.m_58900_().m_60812_(this.f_58857_, this.f_58858_).bounds().move(this.f_58858_) : super.getRenderBoundingBox();
    }
}