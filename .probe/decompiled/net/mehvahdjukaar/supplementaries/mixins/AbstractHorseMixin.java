package net.mehvahdjukaar.supplementaries.mixins;

import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ AbstractHorse.class })
public abstract class AbstractHorseMixin extends Animal {

    @Shadow
    protected int temper;

    protected AbstractHorseMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract int getTemper();

    @Shadow
    public abstract int getMaxTemper();

    @Shadow
    public abstract int modifyTemper(int var1);

    @Shadow
    protected abstract void eating();

    @Shadow
    public abstract boolean isTamed();

    @Inject(method = { "addBehaviourGoals" }, at = { @At("HEAD") })
    public void addSugarCube(CallbackInfo ci) {
        if ((Boolean) CommonConfigs.Building.SUGAR_CUBE_ENABLED.get()) {
            this.f_21345_.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of((ItemLike) ModRegistry.SUGAR_CUBE.get()), false));
        }
    }

    @Inject(method = { "handleEating" }, at = { @At("HEAD") }, cancellable = true)
    public void eatSugarCube(Player player, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(((Block) ModRegistry.SUGAR_CUBE.get()).asItem())) {
            int duration = (Integer) CommonConfigs.Building.SUGAR_BLOCK_HORSE_SPEED_DURATION.get();
            boolean eat = false;
            float healing = 1.0F;
            int ageIncrement = 30;
            int newTemper = 5;
            if (this.m_21223_() < this.m_21233_()) {
                this.m_5634_(healing);
                eat = true;
            }
            Level level = this.m_9236_();
            if (this.m_6162_()) {
                level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.m_20208_(1.0), this.m_20187_() + 0.5, this.m_20262_(1.0), 0.0, 0.0, 0.0);
                if (!level.isClientSide) {
                    this.m_146758_(ageIncrement);
                }
                eat = true;
            }
            if ((eat || !this.isTamed()) && this.getTemper() < this.getMaxTemper()) {
                eat = true;
                if (!level.isClientSide) {
                    this.modifyTemper(newTemper);
                }
            }
            if (duration != 0) {
                this.m_7292_(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * duration, 1));
                eat = true;
            }
            if (eat) {
                this.eating();
                this.m_146850_(GameEvent.EAT);
                cir.setReturnValue(true);
            }
        }
    }
}