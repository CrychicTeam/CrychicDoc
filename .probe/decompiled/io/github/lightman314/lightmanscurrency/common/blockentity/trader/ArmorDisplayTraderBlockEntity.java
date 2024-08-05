package io.github.lightman314.lightmanscurrency.common.blockentity.trader;

import io.github.lightman314.lightmanscurrency.api.misc.blocks.IRotatableBlock;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderDataArmor;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.ItemTradeData;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.restrictions.EquipmentRestriction;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.restrictions.ItemTradeRestriction;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ArmorDisplayTraderBlockEntity extends ItemTraderBlockEntity {

    public static final int TRADE_COUNT = 4;

    private static final int TICK_DELAY = 20;

    UUID armorStandID = null;

    int updateTimer = 0;

    private boolean loaded = false;

    public void flagAsLoaded() {
        this.loaded = true;
    }

    public ArmorDisplayTraderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARMOR_TRADER.get(), pos, state, 4);
    }

    @Nonnull
    @Override
    public ItemTraderData buildNewTrader() {
        return new ItemTraderDataArmor(this.f_58857_, this.f_58858_);
    }

    @Override
    public void serverTick() {
        super.serverTick();
        if (this.loaded) {
            if (this.updateTimer <= 0) {
                this.updateTimer = 20;
                this.validateArmorStand();
                this.validateArmorStandValues();
                this.updateArmorStandArmor();
                this.killIntrudingArmorStands();
            } else {
                this.updateTimer--;
            }
        }
    }

    public void validateArmorStand() {
        if (!this.isClient()) {
            ArmorStand armorStand = this.getArmorStand();
            if (armorStand == null || armorStand.m_213877_()) {
                this.spawnArmorStand();
            }
        }
    }

    private void spawnArmorStand() {
        if (this.f_58857_ != null && !this.isClient()) {
            ArmorStand armorStand = new ArmorStand(this.f_58857_, (double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_() + 0.5);
            armorStand.m_7678_((double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_() + 0.5, this.getStandRotation(), 0.0F);
            armorStand.m_20331_(true);
            armorStand.m_20242_(true);
            armorStand.m_20225_(true);
            CompoundTag compound = armorStand.m_20240_(new CompoundTag());
            compound.putBoolean("Marker", true);
            compound.putBoolean("NoBasePlate", true);
            armorStand.m_20258_(compound);
            this.f_58857_.m_7967_(armorStand);
            this.armorStandID = armorStand.m_20148_();
            this.m_6596_();
        }
    }

    protected void updateArmorStandArmor() {
        ArmorStand armorStand = this.getArmorStand();
        if (armorStand != null) {
            ItemTraderData trader = this.getTraderData();
            if (trader != null) {
                List<ItemTradeData> trades = trader.getTradeData();
                for (int i = 0; i < 4 && i < trades.size(); i++) {
                    ItemTradeData thisTrade = (ItemTradeData) trades.get(i);
                    ItemTradeRestriction r = thisTrade.getRestriction();
                    EquipmentSlot slot = null;
                    if (r instanceof EquipmentRestriction er) {
                        slot = er.getEquipmentSlot();
                    }
                    if (slot != null) {
                        if (!thisTrade.hasStock(trader) && !trader.isCreative()) {
                            armorStand.setItemSlot(slot, ItemStack.EMPTY);
                        } else {
                            armorStand.setItemSlot(slot, thisTrade.getSellItem(0));
                        }
                    }
                }
            }
        }
    }

    public void killIntrudingArmorStands() {
        ArmorStand armorStand = this.getArmorStand();
        if (this.f_58857_ != null && armorStand != null) {
            this.f_58857_.m_45976_(ArmorStand.class, this.m_58900_().m_60808_(this.f_58857_, this.f_58858_).bounds()).forEach(as -> {
                if (as.m_20182_().equals(armorStand.m_20182_())) {
                    as.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            });
        }
    }

    protected void validateArmorStandValues() {
        ArmorStand armorStand = this.getArmorStand();
        if (armorStand != null) {
            armorStand.m_7678_((double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_(), (double) ((float) this.f_58858_.m_123343_() + 0.5F), this.getStandRotation(), 0.0F);
            if (!armorStand.m_20147_()) {
                armorStand.m_20331_(true);
            }
            if (armorStand.m_20145_()) {
                armorStand.setInvisible(false);
            }
            if (!armorStand.f_19794_) {
                armorStand.m_20242_(true);
            }
            if (!armorStand.m_20067_()) {
                armorStand.m_20225_(true);
            }
            if (!armorStand.isMarker() || !armorStand.isNoBasePlate()) {
                CompoundTag compound = armorStand.m_20240_(new CompoundTag());
                if (!armorStand.isMarker()) {
                    compound.putBoolean("Marker", true);
                }
                if (!armorStand.isNoBasePlate()) {
                    compound.putBoolean("NoBasePlate", true);
                }
                armorStand.m_20258_(compound);
            }
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        this.writeArmorStandData(compound);
        super.saveAdditional(compound);
    }

    protected CompoundTag writeArmorStandData(CompoundTag compound) {
        if (this.armorStandID != null) {
            compound.putUUID("ArmorStand", this.armorStandID);
        }
        return compound;
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        this.loaded = true;
        if (compound.contains("ArmorStand")) {
            this.armorStandID = compound.getUUID("ArmorStand");
        }
        super.load(compound);
    }

    protected ArmorStand getArmorStand() {
        if (this.f_58857_ instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.f_58857_).getEntity(this.armorStandID);
            if (entity instanceof ArmorStand) {
                return (ArmorStand) entity;
            }
        }
        return null;
    }

    public void destroyArmorStand() {
        ArmorStand armorStand = this.getArmorStand();
        if (armorStand != null) {
            armorStand.kill();
        }
    }

    protected float getStandRotation() {
        Direction facing = Direction.NORTH;
        if (this.m_58900_().m_60734_() instanceof IRotatableBlock) {
            facing = ((IRotatableBlock) this.m_58900_().m_60734_()).getFacing(this.m_58900_());
        }
        if (facing == Direction.SOUTH) {
            return 180.0F;
        } else if (facing == Direction.NORTH) {
            return 0.0F;
        } else if (facing == Direction.WEST) {
            return -90.0F;
        } else {
            return facing == Direction.EAST ? 90.0F : 0.0F;
        }
    }
}