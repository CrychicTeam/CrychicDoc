package dev.shadowsoffire.attributeslib.mixin;

import dev.shadowsoffire.attributeslib.util.IEntityOwned;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Abilities.class })
public class AbilitiesMixin implements IEntityOwned {

    protected LivingEntity owner;

    @Override
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(LivingEntity owner) {
        if (this.owner != null) {
            throw new UnsupportedOperationException("Cannot set the owner when it is already set.");
        } else if (owner == null) {
            throw new UnsupportedOperationException("Cannot set the owner to null.");
        } else {
            this.owner = owner;
        }
    }
}