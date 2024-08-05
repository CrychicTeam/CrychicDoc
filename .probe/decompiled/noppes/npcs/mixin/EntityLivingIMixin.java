package noppes.npcs.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ LivingEntity.class })
public interface EntityLivingIMixin {

    @Accessor("jumping")
    boolean jumping();

    @Accessor("useItemRemaining")
    void useItemRemaining(int var1);

    @Accessor("animStep")
    float animStep();

    @Accessor("animStep")
    void animStep(float var1);

    @Accessor("animStepO")
    float animStepO();

    @Accessor("animStepO")
    void animStepO(float var1);

    @Accessor("swimAmount")
    float swimAmount();

    @Accessor("swimAmount")
    void swimAmount(float var1);

    @Accessor("swimAmountO")
    float swimAmountO();

    @Accessor("swimAmountO")
    void swimAmountO(float var1);

    @Accessor("lastHurtByPlayerTime")
    int lastHurtByPlayerTime();

    @Accessor("lastHurtByPlayerTime")
    void lastHurtByPlayerTime(int var1);
}