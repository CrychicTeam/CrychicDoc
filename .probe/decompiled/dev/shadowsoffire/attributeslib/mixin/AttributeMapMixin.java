package dev.shadowsoffire.attributeslib.mixin;

import com.mojang.datafixers.util.Pair;
import dev.shadowsoffire.attributeslib.api.AttributeChangedValueEvent;
import dev.shadowsoffire.attributeslib.util.IAttributeManager;
import dev.shadowsoffire.attributeslib.util.IEntityOwned;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ AttributeMap.class })
public class AttributeMapMixin implements IEntityOwned, IAttributeManager {

    protected LivingEntity owner;

    private boolean areAttributesUpdating;

    private Map<Attribute, Pair<AttributeInstance, Double>> updatingAttributes = new HashMap();

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

    @Override
    public boolean areAttributesUpdating() {
        return this.areAttributesUpdating;
    }

    @Override
    public void setAttributesUpdating(boolean updating) {
        this.areAttributesUpdating = updating;
        if (this.areAttributesUpdating()) {
            this.updatingAttributes.clear();
        } else {
            if (!this.getOwner().m_9236_().isClientSide) {
                this.updatingAttributes.forEach((attr, pair) -> MinecraftForge.EVENT_BUS.post(new AttributeChangedValueEvent(this.getOwner(), (AttributeInstance) pair.getFirst(), (Double) pair.getSecond(), ((AttributeInstance) pair.getFirst()).getValue())));
            }
            this.updatingAttributes.clear();
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "onAttributeModified(Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;)V" }, require = 1)
    public void apoth_attrModifiedEvent(AttributeInstance inst, CallbackInfo ci) {
        if (this.owner == null) {
            throw new RuntimeException("An AttributeMap object was modified without a set owner!");
        } else {
            if (!this.areAttributesUpdating() && !this.owner.m_9236_().isClientSide) {
                double oldValue = ((AttributeInstanceAccessor) inst).getCachedValue();
                double newValue = inst.getValue();
                if (oldValue != newValue) {
                    MinecraftForge.EVENT_BUS.post(new AttributeChangedValueEvent(this.getOwner(), inst, oldValue, newValue));
                }
            } else if (this.areAttributesUpdating()) {
                this.updatingAttributes.putIfAbsent(inst.getAttribute(), Pair.of(inst, ((AttributeInstanceAccessor) inst).getCachedValue()));
            }
        }
    }
}