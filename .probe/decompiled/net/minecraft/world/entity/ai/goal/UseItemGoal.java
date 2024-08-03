package net.minecraft.world.entity.ai.goal;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class UseItemGoal<T extends Mob> extends Goal {

    private final T mob;

    private final ItemStack item;

    private final Predicate<? super T> canUseSelector;

    @Nullable
    private final SoundEvent finishUsingSound;

    public UseItemGoal(T t0, ItemStack itemStack1, @Nullable SoundEvent soundEvent2, Predicate<? super T> predicateSuperT3) {
        this.mob = t0;
        this.item = itemStack1;
        this.finishUsingSound = soundEvent2;
        this.canUseSelector = predicateSuperT3;
    }

    @Override
    public boolean canUse() {
        return this.canUseSelector.test(this.mob);
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.m_6117_();
    }

    @Override
    public void start() {
        this.mob.setItemSlot(EquipmentSlot.MAINHAND, this.item.copy());
        this.mob.m_6672_(InteractionHand.MAIN_HAND);
    }

    @Override
    public void stop() {
        this.mob.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        if (this.finishUsingSound != null) {
            this.mob.m_5496_(this.finishUsingSound, 1.0F, this.mob.m_217043_().nextFloat() * 0.2F + 0.9F);
        }
    }
}