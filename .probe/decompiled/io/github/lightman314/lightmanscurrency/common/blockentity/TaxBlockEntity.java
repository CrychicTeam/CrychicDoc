package io.github.lightman314.lightmanscurrency.common.blockentity;

import io.github.lightman314.lightmanscurrency.api.misc.blockentity.EasyBlockEntity;
import io.github.lightman314.lightmanscurrency.api.misc.world.WorldPosition;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.emergency_ejection.EjectionData;
import io.github.lightman314.lightmanscurrency.common.emergency_ejection.EjectionSaveData;
import io.github.lightman314.lightmanscurrency.common.emergency_ejection.IDumpable;
import io.github.lightman314.lightmanscurrency.common.taxes.TaxEntry;
import io.github.lightman314.lightmanscurrency.common.taxes.TaxSaveData;
import io.github.lightman314.lightmanscurrency.util.BlockEntityUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TaxBlockEntity extends EasyBlockEntity {

    private long taxEntryID = -1L;

    private boolean validBreak = false;

    public final TaxEntry getTaxEntry() {
        return TaxSaveData.GetTaxEntry(this.taxEntryID, this.isClient());
    }

    public TaxBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.TAX_BLOCK.get(), pos, state);
    }

    protected TaxBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void initialize(Player owner) {
        this.taxEntryID = TaxSaveData.CreateAndRegister(this, owner);
        this.m_6596_();
        BlockEntityUtil.sendUpdatePacket(this);
    }

    public void flagAsValidBreak() {
        this.validBreak = true;
    }

    public List<ItemStack> getContents(boolean dropBlock) {
        List<ItemStack> drops = new ArrayList();
        if (dropBlock) {
            drops.add(new ItemStack(this.m_58900_().m_60734_()));
        }
        TaxEntry entry = this.getTaxEntry();
        if (entry != null) {
            for (MoneyValue value : entry.getStoredMoney().allValues()) {
                List<ItemStack> items = value.onBlockBroken(this.f_58857_, entry.getOwner());
                if (items != null) {
                    drops.addAll(items);
                }
            }
            entry.clearStoredMoney();
        }
        return drops;
    }

    public void onRemove() {
        TaxEntry entry = this.getTaxEntry();
        if (entry != null) {
            if (!this.validBreak) {
                EjectionSaveData.HandleEjectionData(this.f_58857_, this.f_58858_, EjectionData.create(this.f_58857_, this.f_58858_, this.m_58900_(), IDumpable.preCollected(this.getContents(true), entry.getName(), entry.getOwner())));
                this.validBreak = true;
            }
            TaxSaveData.RemoveEntry(this.taxEntryID);
        }
    }

    public void onLoad() {
        if (this.isClient()) {
            BlockEntityUtil.requestUpdatePacket(this);
        } else {
            TaxEntry entry = this.getTaxEntry();
            if (entry != null) {
                entry.moveCenter(WorldPosition.ofBE(this));
            }
        }
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compound) {
        compound.putLong("EntryID", this.taxEntryID);
        super.m_183515_(compound);
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.m_142466_(compound);
        if (compound.contains("EntryID")) {
            this.taxEntryID = compound.getLong("EntryID");
        }
    }
}