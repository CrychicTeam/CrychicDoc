package com.mna.capabilities.playerdata.magic;

import com.mna.api.cantrips.ICantrip;
import com.mna.api.capabilities.IPlayerCantrip;
import com.mna.api.capabilities.IPlayerCantrips;
import com.mna.cantrips.Cantrip;
import com.mna.cantrips.CantripRegistry;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.inventory.InventoryCantrips;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.commons.lang3.mutable.MutableInt;

public class PlayerCantrips implements IPlayerCantrips {

    private List<IPlayerCantrip> cantrips;

    private boolean _needsSync = true;

    public PlayerCantrips() {
        this.cantrips = new ArrayList();
        CantripRegistry.INSTANCE.getCantrips().forEach(c -> this.cantrips.add(new PlayerCantrip(c)));
    }

    @Nullable
    @Override
    public ICantrip matchAndCastCantrip(Player player, InteractionHand hand, List<Recipe<?>> recipes) {
        MutableInt playerTier = new MutableInt(1);
        player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> playerTier.setValue(p.getTier()));
        List<ManaweavingPattern> patterns = new ArrayList();
        for (Recipe<?> recipe : recipes) {
            if (recipe instanceof ManaweavingPattern) {
                patterns.add(recipe);
            }
        }
        for (IPlayerCantrip cantrip : this.cantrips) {
            if (cantrip.matches(patterns)) {
                Optional<ICantrip> registeredCantrip = CantripRegistry.INSTANCE.getCantrip(cantrip.getCantripID());
                if (registeredCantrip != null && registeredCantrip.isPresent() && ((ICantrip) registeredCantrip.get()).getTier() <= playerTier.getValue()) {
                    ((Cantrip) registeredCantrip.get()).callEffector(player, hand);
                    return (ICantrip) registeredCantrip.get();
                }
            }
        }
        return null;
    }

    @Override
    public CompoundTag writeToNBT(boolean patternsOnly) {
        CompoundTag nbt = new CompoundTag();
        this.cantrips.forEach(c -> nbt.put(c.getCantripID().toString(), c.writeToNBT()));
        return nbt;
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        if (nbt != null) {
            this.cantrips.forEach(c -> {
                if (nbt.contains(c.getCantripID().toString())) {
                    c.readFromNBT((CompoundTag) nbt.get(c.getCantripID().toString()));
                }
            });
        }
    }

    @Override
    public void setPattern(ResourceLocation cantripID, List<ResourceLocation> manaweavingPatterns) {
        this.cantrips.stream().filter(c -> c.getCantripID().equals(cantripID)).findFirst().ifPresent(c -> {
            c.setPatterns(manaweavingPatterns);
            this._needsSync = true;
        });
    }

    @Override
    public Optional<IPlayerCantrip> getCantrip(ResourceLocation cantripID) {
        return this.cantrips.stream().filter(c -> c.getCantripID().equals(cantripID)).findFirst();
    }

    @Override
    public List<IPlayerCantrip> getCantrips() {
        return this.cantrips;
    }

    @Override
    public Container getAsInventory() {
        return new InventoryCantrips(this);
    }

    @Override
    public boolean needsSync() {
        return this._needsSync;
    }

    @Override
    public void clearSync() {
        this._needsSync = false;
    }

    @Override
    public void setNeedsSync() {
        this._needsSync = true;
    }
}