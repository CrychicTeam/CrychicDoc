package top.theillusivec4.caelus.mixin.core;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import top.theillusivec4.caelus.mixin.util.MixinHooks;

@Mixin({ LivingEntity.class })
public abstract class MixinLivingEntity extends Entity {

    public MixinLivingEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.setSharedFlag(IZ)V"), method = { "updateFallFlying" })
    private boolean caelus$setFlag(boolean value) {
        return MixinHooks.canFly((LivingEntity) this, this.m_20291_(7), value);
    }
}