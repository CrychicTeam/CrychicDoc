package com.mna.gui.containers.item;

import com.mna.gui.containers.ContainerInit;
import com.mna.items.artifice.ItemEnderDisk;
import com.mna.network.ClientMessageDispatcher;
import com.mna.network.ServerMessageDispatcher;
import com.mna.tools.math.MathUtils;
import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ContainerEnderDisc extends AbstractContainerMenu {

    public ItemStack heldItem;

    private ArrayList<String> _dimensions;

    private ArrayList<ContainerEnderDisc.EnderDiscPattern> _patterns;

    private byte[] dirty;

    private int _curIndex = 0;

    private Level level;

    private ContainerEnderDisc(@Nullable MenuType<?> type, int id, Inventory playerInv, ItemStack heldItem) {
        super(type, id);
        this.heldItem = heldItem;
        this._patterns = new ArrayList();
        this._dimensions = new ArrayList();
        this._curIndex = MathUtils.clamp(ItemEnderDisk.getIndex(this.heldItem), 0, 8);
        this.dirty = new byte[8];
        for (int i = 0; i < this.dirty.length; i++) {
            this.dirty[i] = 0;
            this._patterns.add(ContainerEnderDisc.EnderDiscPattern.fromStack(this.heldItem, i));
        }
        this.level = playerInv.player.m_9236_();
        MinecraftServer server = this.level.getServer();
        this._dimensions.add("mna:dimension_current");
        if (server != null) {
            server.getAllLevels().forEach(sl -> this._dimensions.add(sl.m_46472_().location().toString()));
        }
    }

    public ContainerEnderDisc(int i, Inventory playerInv, FriendlyByteBuf buffer) {
        this(ContainerInit.ENDER_DISC.get(), i, playerInv, playerInv.getItem(playerInv.selected));
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    public void changePatternIndex(int newIndex) {
        if (newIndex >= 0 && newIndex < this._patterns.size()) {
            this._curIndex = newIndex;
        }
    }

    public ArrayList<ResourceLocation> getCurPattern() {
        return ((ContainerEnderDisc.EnderDiscPattern) this._patterns.get(this._curIndex))._pattern;
    }

    public ResourceLocation getCurDimension() {
        return ((ContainerEnderDisc.EnderDiscPattern) this._patterns.get(this._curIndex))._dimensionID;
    }

    public int getCurPatternIndex() {
        return this._curIndex;
    }

    public String getCurPatternName() {
        return ((ContainerEnderDisc.EnderDiscPattern) this._patterns.get(this._curIndex))._name;
    }

    public void setPattern(ResourceLocation pattern, int index) {
        if (index >= 0 && index < 8) {
            ((ContainerEnderDisc.EnderDiscPattern) this._patterns.get(this._curIndex))._pattern.set(index, pattern);
            this.dirty[this._curIndex] = 1;
        }
    }

    public void setDimension(ResourceLocation dimension) {
        if (dimension == null) {
            dimension = new ResourceLocation("");
        }
        ((ContainerEnderDisc.EnderDiscPattern) this._patterns.get(this._curIndex))._dimensionID = dimension;
    }

    public void setName(String name) {
        ((ContainerEnderDisc.EnderDiscPattern) this._patterns.get(this._curIndex))._name = name;
        this.dirty[this._curIndex] = 1;
    }

    @Override
    public boolean clickMenuButton(Player pPlayer, int pId) {
        String currentDim = this.getCurDimension().toString();
        int foundIndex = -1;
        for (int idx = 0; idx < this._dimensions.size(); idx++) {
            if (((String) this._dimensions.get(idx)).equals(currentDim)) {
                foundIndex = idx;
                break;
            }
        }
        int nextIdx = (foundIndex + 1) % this._dimensions.size();
        ResourceLocation nextDim = new ResourceLocation((String) this._dimensions.get(nextIdx));
        this.setDimension(nextDim);
        ServerMessageDispatcher.sendEnderDiscGuiDimensionCycle((ServerPlayer) pPlayer, nextDim);
        return true;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        if (playerIn.m_9236_().isClientSide()) {
            for (int i = 0; i < this.dirty.length; i++) {
                if (this.dirty[i] == 1) {
                    ClientMessageDispatcher.sendEnderDiscPatternChange(((ContainerEnderDisc.EnderDiscPattern) this._patterns.get(i))._pattern, ((ContainerEnderDisc.EnderDiscPattern) this._patterns.get(i))._dimensionID, i, ((ContainerEnderDisc.EnderDiscPattern) this._patterns.get(i))._name);
                }
            }
            ClientMessageDispatcher.sendEnderDiscIndexChange(this._curIndex, false);
        }
    }

    public String getPatternName(int index) {
        return index >= 0 && index < this._patterns.size() ? ((ContainerEnderDisc.EnderDiscPattern) this._patterns.get(index))._name : "";
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    static class EnderDiscPattern {

        ArrayList<ResourceLocation> _pattern;

        ResourceLocation _dimensionID = new ResourceLocation("mna:dimension_current");

        String _name = "";

        static ContainerEnderDisc.EnderDiscPattern fromStack(ItemStack stack, int index) {
            ContainerEnderDisc.EnderDiscPattern pattern = new ContainerEnderDisc.EnderDiscPattern();
            pattern._pattern = ItemEnderDisk.getPattern(stack, index);
            pattern._name = ItemEnderDisk.getPatternName(stack, index).getString();
            pattern._dimensionID = ItemEnderDisk.getDimension(stack, index);
            return pattern;
        }
    }
}