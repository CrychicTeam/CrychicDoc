package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.NotNull;

public class LivingEntityUseItemEvent extends LivingEvent {

    private final ItemStack item;

    private int duration;

    private LivingEntityUseItemEvent(LivingEntity entity, @NotNull ItemStack item, int duration) {
        super(entity);
        this.item = item;
        this.setDuration(duration);
    }

    @NotNull
    public ItemStack getItem() {
        return this.item;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public static class Finish extends LivingEntityUseItemEvent {

        private ItemStack result;

        public Finish(LivingEntity entity, @NotNull ItemStack item, int duration, @NotNull ItemStack result) {
            super(entity, item, duration);
            this.setResultStack(result);
        }

        @NotNull
        public ItemStack getResultStack() {
            return this.result;
        }

        public void setResultStack(@NotNull ItemStack result) {
            this.result = result;
        }
    }

    @Cancelable
    public static class Start extends LivingEntityUseItemEvent {

        public Start(LivingEntity entity, @NotNull ItemStack item, int duration) {
            super(entity, item, duration);
        }
    }

    @Cancelable
    public static class Stop extends LivingEntityUseItemEvent {

        public Stop(LivingEntity entity, @NotNull ItemStack item, int duration) {
            super(entity, item, duration);
        }
    }

    @Cancelable
    public static class Tick extends LivingEntityUseItemEvent {

        public Tick(LivingEntity entity, @NotNull ItemStack item, int duration) {
            super(entity, item, duration);
        }
    }
}