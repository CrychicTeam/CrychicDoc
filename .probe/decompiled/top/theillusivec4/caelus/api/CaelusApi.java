package top.theillusivec4.caelus.api;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public abstract class CaelusApi {

    public static CaelusApi getInstance() {
        throw new IllegalStateException("Missing API implementation from Caelus!");
    }

    public abstract String getModId();

    public abstract Attribute getFlightAttribute();

    public abstract AttributeModifier getElytraModifier();

    public abstract CaelusApi.TriState canFallFly(LivingEntity var1);

    @Deprecated
    @ScheduledForRemoval(inVersion = "1.21")
    public abstract boolean canFly(LivingEntity var1);

    public static enum TriState {

        ALLOW, DEFAULT, DENY
    }
}