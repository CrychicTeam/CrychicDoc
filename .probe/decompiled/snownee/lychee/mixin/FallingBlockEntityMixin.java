package snownee.lychee.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.lychee.RecipeTypes;
import snownee.lychee.block_crushing.LycheeFallingBlockEntity;

@Mixin({ FallingBlockEntity.class })
public abstract class FallingBlockEntityMixin extends Entity implements LycheeFallingBlockEntity {

    @Unique
    private boolean lychee$matched;

    @Unique
    private float lychee$anvilDamageChance = -1.0F;

    @Shadow
    private boolean cancelDrop;

    @Shadow
    private BlockState blockState;

    public FallingBlockEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(at = { @At("HEAD") }, method = { "causeFallDamage" })
    private void lychee_causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource, CallbackInfoReturnable<Boolean> ci) {
        FallingBlockEntity entity = (FallingBlockEntity) this;
        if (!entity.m_9236_().isClientSide) {
            RecipeTypes.BLOCK_CRUSHING.process(entity);
        }
    }

    @ModifyVariable(at = @At("STORE"), method = { "tick" }, index = 9)
    private boolean lychee_modifyFlag3(boolean original) {
        return this.lychee$matched ? false : original;
    }

    @ModifyVariable(at = @At("STORE"), method = { "causeFallDamage" }, index = 10)
    private boolean lychee_overrideDamageAnvil(boolean original) {
        if (original && this.lychee$anvilDamageChance >= 0.0F) {
            if (this.f_19796_.nextFloat() < this.lychee$anvilDamageChance) {
                BlockState blockstate = AnvilBlock.damage(this.blockState);
                if (blockstate == null) {
                    this.cancelDrop = true;
                } else {
                    this.blockState = blockstate;
                }
            }
            return false;
        } else {
            return original;
        }
    }

    @Override
    public void lychee$cancelDrop() {
        this.cancelDrop = true;
    }

    @Override
    public void lychee$matched() {
        this.lychee$matched = true;
    }

    @Override
    public void lychee$anvilDamageChance(float chance) {
        this.lychee$anvilDamageChance = Math.max(chance, this.lychee$anvilDamageChance);
    }
}