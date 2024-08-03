package dev.xkmc.modulargolems.events.event;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import java.util.function.Function;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GolemThrowableEvent extends GolemItemUseEvent {

    private boolean isThrowable;

    private Function<Level, Projectile> supplier;

    public GolemThrowableEvent(HumanoidGolemEntity golem, ItemStack stack, InteractionHand hand) {
        super(golem, stack, hand);
    }

    public void setThrowable(Function<Level, Projectile> supplier) {
        this.supplier = supplier;
        this.isThrowable = true;
    }

    public boolean isThrowable() {
        return this.isThrowable;
    }

    public Projectile createProjectile(Level level) {
        return (Projectile) this.supplier.apply(level);
    }
}