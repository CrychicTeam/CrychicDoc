package net.minecraftforge.event.entity.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.NotNull;

@Cancelable
public class ArrowLooseEvent extends PlayerEvent {

    private final ItemStack bow;

    private final Level level;

    private final boolean hasAmmo;

    private int charge;

    public ArrowLooseEvent(Player player, @NotNull ItemStack bow, Level level, int charge, boolean hasAmmo) {
        super(player);
        this.bow = bow;
        this.level = level;
        this.charge = charge;
        this.hasAmmo = hasAmmo;
    }

    @NotNull
    public ItemStack getBow() {
        return this.bow;
    }

    public Level getLevel() {
        return this.level;
    }

    public boolean hasAmmo() {
        return this.hasAmmo;
    }

    public int getCharge() {
        return this.charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }
}