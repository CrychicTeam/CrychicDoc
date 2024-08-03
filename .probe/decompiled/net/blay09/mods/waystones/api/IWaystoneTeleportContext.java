package net.blay09.mods.waystones.api;

import java.util.List;
import net.blay09.mods.waystones.core.WarpMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IWaystoneTeleportContext {

    Entity getEntity();

    IWaystone getTargetWaystone();

    TeleportDestination getDestination();

    void setDestination(TeleportDestination var1);

    List<Mob> getLeashedEntities();

    List<Entity> getAdditionalEntities();

    void addAdditionalEntity(Entity var1);

    @Nullable
    IWaystone getFromWaystone();

    void setFromWaystone(@Nullable IWaystone var1);

    ItemStack getWarpItem();

    void setWarpItem(ItemStack var1);

    boolean isDimensionalTeleport();

    int getXpCost();

    void setXpCost(int var1);

    void setCooldown(int var1);

    int getCooldown();

    WarpMode getWarpMode();

    void setWarpMode(WarpMode var1);

    boolean playsSound();

    void setPlaysSound(boolean var1);

    boolean playsEffect();

    void setPlaysEffect(boolean var1);

    default boolean consumesWarpItem() {
        return this.getWarpMode() != null && this.getWarpMode().consumesItem();
    }

    default void setConsumesWarpItem(boolean consumesWarpItem) {
    }
}