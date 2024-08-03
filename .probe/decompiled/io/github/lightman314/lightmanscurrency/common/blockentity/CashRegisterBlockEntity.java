package io.github.lightman314.lightmanscurrency.common.blockentity;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.traders.ITraderSource;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.blockentity.TraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.menus.validation.types.BlockEntityValidator;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class CashRegisterBlockEntity extends BlockEntity implements ITraderSource {

    List<BlockPos> positions = new ArrayList();

    public CashRegisterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CASH_REGISTER.get(), pos, state);
    }

    public void loadDataFromItems(CompoundTag itemTag) {
        if (itemTag != null) {
            this.readPositions(itemTag);
        }
    }

    public void OpenContainer(Player player) {
        MenuProvider provider = TraderData.getTraderMenuProvider(this.f_58858_, BlockEntityValidator.of(this));
        if (!(player instanceof ServerPlayer)) {
            LightmansCurrency.LogError("Player is not a server player entity. Cannot open the trade menu.");
        } else {
            if (!this.getTraders().isEmpty()) {
                NetworkHooks.openScreen((ServerPlayer) player, provider, this.f_58858_);
            } else {
                player.m_213846_(LCText.MESSAGE_CASH_REGISTER_NOT_LINKED.get());
            }
        }
    }

    @Override
    public boolean isSingleTrader() {
        return false;
    }

    @Nonnull
    @NotNull
    @Override
    public List<TraderData> getTraders() {
        List<TraderData> traders = new ArrayList();
        for (BlockPos position : this.positions) {
            BlockEntity be = this.f_58857_.getBlockEntity(position);
            if (be instanceof TraderBlockEntity) {
                TraderData trader = ((TraderBlockEntity) be).getTraderData();
                if (trader != null) {
                    traders.add(trader);
                }
            }
        }
        return traders;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        ListTag storageList = new ListTag();
        for (BlockPos thisPos : this.positions) {
            CompoundTag thisEntry = new CompoundTag();
            thisEntry.putInt("x", thisPos.m_123341_());
            thisEntry.putInt("y", thisPos.m_123342_());
            thisEntry.putInt("z", thisPos.m_123343_());
            storageList.add(thisEntry);
        }
        if (!storageList.isEmpty()) {
            compound.put("TraderPos", storageList);
        }
        super.saveAdditional(compound);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        this.readPositions(compound);
        super.load(compound);
    }

    private void readPositions(CompoundTag compound) {
        if (compound.contains("TraderPos")) {
            this.positions = new ArrayList();
            ListTag storageList = compound.getList("TraderPos", 10);
            for (int i = 0; i < storageList.size(); i++) {
                CompoundTag thisEntry = storageList.getCompound(i);
                if (thisEntry.contains("x") && thisEntry.contains("y") && thisEntry.contains("z")) {
                    BlockPos thisPos = new BlockPos(thisEntry.getInt("x"), thisEntry.getInt("y"), thisEntry.getInt("z"));
                    this.positions.add(thisPos);
                }
            }
        }
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }
}