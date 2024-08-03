package com.illusivesoulworks.polymorph.common.capability;

import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.illusivesoulworks.polymorph.api.common.capability.IBlockEntityRecipeData;
import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractBlockEntityRecipeData<E extends BlockEntity> extends AbstractRecipeData<BlockEntity> implements IBlockEntityRecipeData {

    private final Map<UUID, ServerPlayer> listeners = new HashMap();

    private NonNullList<Item> lastInput = NonNullList.create();

    public AbstractBlockEntityRecipeData(E owner) {
        super(owner);
    }

    protected abstract NonNullList<ItemStack> getInput();

    @Override
    public void tick() {
        boolean changed = false;
        NonNullList<ItemStack> currentInput = this.getInput();
        this.lastInput = this.validateList(this.lastInput, currentInput.size());
        for (int i = 0; i < currentInput.size(); i++) {
            Item lastItem = this.lastInput.get(i);
            ItemStack currentStack = currentInput.get(i);
            Item item = Items.AIR;
            if (currentStack.getCount() > 0) {
                item = currentStack.getItem();
            }
            if (lastItem != currentStack.getItem()) {
                changed = true;
            }
            this.lastInput.set(i, item);
        }
        if (changed) {
            this.sendRecipesListToListeners(this.isFailing() || this.isEmpty());
        }
    }

    @Override
    public void addListener(ServerPlayer serverPlayer) {
        this.listeners.put(serverPlayer.m_20148_(), serverPlayer);
    }

    @Override
    public void removeListener(ServerPlayer serverPlayer) {
        this.listeners.remove(serverPlayer.m_20148_());
    }

    private NonNullList<Item> validateList(NonNullList<Item> pList, int pSize) {
        if (pList.size() == pSize) {
            return pList;
        } else {
            NonNullList<Item> resized = NonNullList.withSize(pSize, Items.AIR);
            for (int i = 0; i < Math.min(resized.size(), pList.size()); i++) {
                resized.set(i, pList.get(i));
            }
            return resized;
        }
    }

    @Override
    public Set<ServerPlayer> getListeners() {
        return new HashSet(this.listeners.values());
    }

    public E getOwner() {
        return super.getOwner();
    }

    public boolean isEmpty() {
        for (ItemStack stack : this.getInput()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Pair<SortedSet<IRecipePair>, ResourceLocation> getPacketData() {
        return new Pair(this.getRecipesList(), null);
    }

    @Override
    public boolean isEmpty(Container inventory) {
        return this.isEmpty();
    }
}