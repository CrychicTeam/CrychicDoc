package net.mehvahdjukaar.supplementaries.common.block.tiles;

import net.mehvahdjukaar.supplementaries.common.block.IKeyLockable;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class KeyLockableTile extends BlockEntity implements IKeyLockable {

    private String password = null;

    public KeyLockableTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.KEY_LOCKABLE_TILE.get(), pos, state);
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void clearPassword() {
        this.password = null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public boolean handleAction(Player player, InteractionHand handIn, String translName) {
        if (player.isSpectator()) {
            return false;
        } else {
            ItemStack stack = player.m_21120_(handIn);
            String keyPassword = IKeyLockable.getKeyPassword(stack);
            if (player.isSecondaryUseActive() && keyPassword != null) {
                if (this.tryClearingKey(player, stack)) {
                    return false;
                }
            } else if (this.password == null) {
                if (keyPassword != null) {
                    this.setPassword(keyPassword);
                    this.onKeyAssigned(this.f_58857_, this.f_58858_, player, keyPassword);
                    return false;
                }
                return true;
            }
            return player.isCreative() || this.testIfHasCorrectKey(player, this.password, true, translName);
        }
    }

    public boolean tryClearingKey(Player player, ItemStack stack) {
        if (!player.isCreative() && this.getKeyStatus(stack) != IKeyLockable.KeyStatus.CORRECT_KEY) {
            return false;
        } else {
            this.clearPassword();
            this.onPasswordCleared(player, this.f_58858_);
            return true;
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("Password")) {
            this.password = compound.getString("Password");
        } else {
            this.password = null;
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.password != null) {
            tag.putString("Password", this.password);
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}