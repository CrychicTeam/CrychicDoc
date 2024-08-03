package net.mehvahdjukaar.supplementaries.mixins;

import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.supplementaries.common.block.IConvertableHorse;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ SkeletonHorse.class })
public abstract class SkellyHorseMixin extends AbstractHorse implements IConvertableHorse {

    @Unique
    private int supplementaries$fleshCount = 0;

    @Unique
    private int supplementaries$conversionTime = -1;

    protected SkellyHorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = { "addAdditionalSaveData" }, at = { @At("TAIL") })
    public void addAdditionalSaveData(CompoundTag compoundNBT, CallbackInfo ci) {
        compoundNBT.putInt("FleshCount", this.supplementaries$fleshCount);
        compoundNBT.putInt("ConversionTime", this.supplementaries$conversionTime);
    }

    @Inject(method = { "readAdditionalSaveData" }, at = { @At("TAIL") })
    public void readAdditionalSaveData(CompoundTag compoundNBT, CallbackInfo ci) {
        this.supplementaries$fleshCount = compoundNBT.getInt("FleshCount");
        this.supplementaries$conversionTime = compoundNBT.getInt("ConversionTime");
    }

    @Inject(method = { "mobInteract" }, at = { @At("HEAD") }, cancellable = true)
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if ((Boolean) CommonConfigs.Tweaks.ZOMBIE_HORSE.get() && this.m_30614_() && !this.m_6162_()) {
            ItemStack stack = player.m_21120_(hand);
            if (stack.getItem() == Items.ROTTEN_FLESH && this.supplementaries$fleshCount < (Integer) CommonConfigs.Tweaks.ZOMBIE_HORSE_COST.get()) {
                this.feedRottenFlesh(player, hand, stack);
                cir.cancel();
                cir.setReturnValue(InteractionResult.sidedSuccess(player.m_9236_().isClientSide));
            }
        }
    }

    @Nullable
    @Override
    protected SoundEvent getEatingSound() {
        return SoundEvents.HORSE_EAT;
    }

    public void feedRottenFlesh(Player player, InteractionHand hand, ItemStack stack) {
        float heal = 0.5F;
        if (this.m_21223_() < this.m_21233_()) {
            this.m_5634_(heal);
        }
        this.m_30661_(true);
        this.supplementaries$fleshCount++;
        if (this.supplementaries$fleshCount >= (Integer) CommonConfigs.Tweaks.ZOMBIE_HORSE_COST.get()) {
            this.supplementaries$conversionTime = 200;
            this.m_9236_().broadcastEntityEvent(this, (byte) 16);
        }
        if (!player.isCreative()) {
            stack.shrink(1);
        }
    }

    @Override
    public boolean supp$isConverting() {
        return this.supplementaries$conversionTime > 0;
    }

    @Unique
    private void doZombieConversion() {
        float yBodyRot = this.f_20883_;
        float yHeadRot = this.f_20885_;
        float yBodyRotO = this.f_20884_;
        float yHeadRotO = this.f_20886_;
        AbstractHorse newHorse = (AbstractHorse) this.m_21406_(EntityType.ZOMBIE_HORSE, true);
        if (newHorse != null) {
            newHorse.f_20883_ = yBodyRot;
            newHorse.f_20885_ = yHeadRot;
            newHorse.f_20886_ = yHeadRotO;
            newHorse.setOwnerUUID(this.m_21805_());
            newHorse.setTamed(this.m_30614_());
            newHorse.m_7292_(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            if (this.m_6254_()) {
                newHorse.equipSaddle(null);
            }
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack itemstack = this.m_6844_(slot);
                if (!itemstack.isEmpty()) {
                    if (EnchantmentHelper.hasBindingCurse(itemstack)) {
                        newHorse.getSlot(slot.getIndex() + 300).set(itemstack);
                    } else {
                        double d0 = (double) this.m_21519_(slot);
                        if (d0 > 1.0) {
                            this.m_19983_(itemstack);
                        }
                    }
                }
            }
            ForgeHelper.onLivingConvert(this, newHorse);
        }
        if (!this.m_20067_()) {
            this.m_9236_().m_5898_(null, 1027, this.m_20183_(), 0);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 16) {
            if (!this.m_20067_()) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20188_(), this.m_20189_(), SoundEvents.ZOMBIE_VILLAGER_CURE, this.m_5720_(), 1.0F + this.f_19796_.nextFloat(), this.f_19796_.nextFloat() * 0.7F + 0.3F, false);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.m_9236_().isClientSide && this.m_6084_() && !this.m_21525_() && this.supp$isConverting()) {
            this.supplementaries$conversionTime--;
            if (this.supplementaries$conversionTime <= 0 && ForgeHelper.canLivingConvert(this, EntityType.ZOMBIE_HORSE, timer -> this.supplementaries$conversionTime = timer)) {
                this.doZombieConversion();
            }
        }
    }
}