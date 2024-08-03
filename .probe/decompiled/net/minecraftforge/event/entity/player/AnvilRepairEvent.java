package net.minecraftforge.event.entity.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AnvilRepairEvent extends PlayerEvent {

    @NotNull
    private final ItemStack left;

    @NotNull
    private final ItemStack right;

    @NotNull
    private final ItemStack output;

    private float breakChance;

    public AnvilRepairEvent(Player player, @NotNull ItemStack left, @NotNull ItemStack right, @NotNull ItemStack output) {
        super(player);
        this.output = output;
        this.left = left;
        this.right = right;
        this.setBreakChance(0.12F);
    }

    @NotNull
    public ItemStack getOutput() {
        return this.output;
    }

    @NotNull
    public ItemStack getLeft() {
        return this.left;
    }

    @NotNull
    public ItemStack getRight() {
        return this.right;
    }

    public float getBreakChance() {
        return this.breakChance;
    }

    public void setBreakChance(float breakChance) {
        this.breakChance = breakChance;
    }
}