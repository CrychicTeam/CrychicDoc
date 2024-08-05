package net.minecraftforge.event.entity.player;

import com.google.common.base.Preconditions;
import java.util.List;
import javax.annotation.Nonnegative;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class ItemFishedEvent extends PlayerEvent {

    private final NonNullList<ItemStack> stacks = NonNullList.create();

    private final FishingHook hook;

    private int rodDamage;

    public ItemFishedEvent(List<ItemStack> stacks, int rodDamage, FishingHook hook) {
        super(hook.getPlayerOwner());
        this.stacks.addAll(stacks);
        this.rodDamage = rodDamage;
        this.hook = hook;
    }

    public int getRodDamage() {
        return this.rodDamage;
    }

    public void damageRodBy(@Nonnegative int rodDamage) {
        Preconditions.checkArgument(rodDamage >= 0);
        this.rodDamage = rodDamage;
    }

    public NonNullList<ItemStack> getDrops() {
        return this.stacks;
    }

    public FishingHook getHookEntity() {
        return this.hook;
    }
}