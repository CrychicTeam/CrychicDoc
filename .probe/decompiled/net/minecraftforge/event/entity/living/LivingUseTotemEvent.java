package net.minecraftforge.event.entity.living;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class LivingUseTotemEvent extends LivingEvent {

    private final DamageSource source;

    private final ItemStack totem;

    private final InteractionHand hand;

    public LivingUseTotemEvent(LivingEntity entity, DamageSource source, ItemStack totem, InteractionHand hand) {
        super(entity);
        this.source = source;
        this.totem = totem;
        this.hand = hand;
    }

    public DamageSource getSource() {
        return this.source;
    }

    public ItemStack getTotem() {
        return this.totem;
    }

    public InteractionHand getHandHolding() {
        return this.hand;
    }
}