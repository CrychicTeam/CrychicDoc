package com.mna.api.gui;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.tile.EldrinCapacitorTile;
import com.mna.api.faction.IFaction;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;

public class EldrinCapacitorPermissionsContainer extends AbstractContainerMenu {

    private static final int MAX_INTERACT_DISTANCE = 256;

    private Player user;

    private EldrinCapacitorTile tile;

    public EldrinCapacitorPermissionsContainer(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(i, playerInventory, ((EldrinCapacitorTile) playerInventory.player.m_9236_().getBlockEntity(packetBuffer.readBlockPos())).readFrom(packetBuffer));
    }

    public EldrinCapacitorPermissionsContainer(int i, Inventory playerInv, EldrinCapacitorTile tile) {
        super(ManaAndArtificeMod.getContainerHelper().GetEldrinPermissionsContainerType(), i);
        this.user = playerInv.player;
        this.tile = tile;
        this.m_38884_(tile);
        tile.addContainer(this);
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.tile.removeContainer(this);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.user != null && this.tile != null && this.user == pPlayer ? this.tile.m_58899_().m_203198_(pPlayer.m_20185_(), pPlayer.m_20186_(), pPlayer.m_20189_()) <= 256.0 : false;
    }

    @Override
    public boolean clickMenuButton(Player pPlayer, int pId) {
        switch(pId) {
            case 0:
                this.toggleShareTeam();
                break;
            case 1:
                this.toggleShareFaction();
                break;
            case 2:
                this.toggleSharePublic();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void setData(int pId, int pData) {
        super.setData(pId, pData);
        this.m_38946_();
    }

    public boolean userCanEdit() {
        return this.user.getGameProfile().getId().equals(this.tile.getPlacedBy());
    }

    public boolean shareTeam() {
        return ((DataSlot) this.f_38842_.get(0)).get() == 1;
    }

    public boolean shareFaction() {
        return ((DataSlot) this.f_38842_.get(1)).get() == 1;
    }

    public boolean sharePublic() {
        return ((DataSlot) this.f_38842_.get(2)).get() == 1;
    }

    public float getAmount(Affinity affinity) {
        int dataSlotIndex = -1;
        byte var3;
        switch(affinity.getShiftAffinity()) {
            case ARCANE:
                var3 = 3;
                break;
            case EARTH:
                var3 = 7;
                break;
            case ENDER:
                var3 = 4;
                break;
            case FIRE:
                var3 = 5;
                break;
            case WATER:
                var3 = 6;
                break;
            case WIND:
                var3 = 8;
                break;
            default:
                return 0.0F;
        }
        return (float) ((DataSlot) this.f_38842_.get(var3)).get() / 100.0F;
    }

    public float getCapacity(Affinity affinity) {
        return this.tile.getCapacity(affinity);
    }

    public float getChargeRate() {
        return (float) ((DataSlot) this.f_38842_.get(9)).get() / 100.0F;
    }

    public float getChargeRadius() {
        return (float) ((DataSlot) this.f_38842_.get(10)).get() / 100.0F;
    }

    public void toggleShareTeam() {
        DataSlot dataSlot = (DataSlot) this.f_38842_.get(0);
        if (dataSlot.get() == 1) {
            dataSlot.set(0);
        } else {
            dataSlot.set(1);
        }
    }

    public void toggleShareFaction() {
        DataSlot dataSlot = (DataSlot) this.f_38842_.get(1);
        if (dataSlot.get() == 1) {
            dataSlot.set(0);
        } else {
            dataSlot.set(1);
        }
    }

    public void toggleSharePublic() {
        DataSlot dataSlot = (DataSlot) this.f_38842_.get(2);
        if (dataSlot.get() == 1) {
            dataSlot.set(0);
        } else {
            dataSlot.set(1);
        }
    }

    @Nullable
    public IFaction getShareFaction() {
        return this.tile.getPlacedByFaction();
    }

    @Nullable
    public String getShareTeam() {
        return this.tile.getPlacedByTeam();
    }

    @Nullable
    public String getPlacedByPlayerName() {
        return this.tile.getPlacedByPlayerName();
    }

    public boolean supplies(Affinity affinity) {
        return this.tile.supplies(affinity);
    }
}