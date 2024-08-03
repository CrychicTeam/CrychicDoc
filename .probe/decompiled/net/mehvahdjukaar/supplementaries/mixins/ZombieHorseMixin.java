package net.mehvahdjukaar.supplementaries.mixins;

import net.mehvahdjukaar.moonlight.api.misc.OptionalMixin;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.supplementaries.common.block.IConvertableHorse;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OptionalMixin(value = "com.github.alexthe668.domesticationinnovation.DomesticationMod", classLoaded = false)
@Mixin({ ZombieHorse.class })
public abstract class ZombieHorseMixin extends AbstractHorse implements IConvertableHorse {

    @Unique
    private static final int CONV_TIME = 4600;

    @Unique
    private int supplementaries$conversionTime = -1;

    protected ZombieHorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean dismountsUnderwater() {
        return false;
    }

    public void startConverting() {
        if (!this.supp$isConverting()) {
            this.supplementaries$conversionTime = 4600;
            this.m_9236_().broadcastEntityEvent(this, (byte) 16);
            this.m_7292_(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 4600, 2));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ConversionTime", this.supplementaries$conversionTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        this.addAdditionalSaveData(compoundNBT);
        this.supplementaries$conversionTime = compoundNBT.getInt("ConversionTime");
    }

    @Override
    public boolean supp$isConverting() {
        return this.supplementaries$conversionTime > 0;
    }

    private void doHorseConvertion() {
        float yBodyRot = this.f_20883_;
        float yHeadRot = this.f_20885_;
        float yBodyRotO = this.f_20884_;
        float yHeadRotO = this.f_20886_;
        AbstractHorse newHorse = (AbstractHorse) this.m_21406_(EntityType.HORSE, true);
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
    public void handleEntityEvent(byte pId) {
        if (pId == 16) {
            this.supplementaries$conversionTime = 4600;
            if (!this.m_20067_()) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20188_(), this.m_20189_(), SoundEvents.ZOMBIE_VILLAGER_CURE, this.m_5720_(), 1.0F + this.f_19796_.nextFloat(), this.f_19796_.nextFloat() * 0.7F + 0.3F, false);
            }
        } else {
            super.handleEntityEvent(pId);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.m_9236_().isClientSide && this.m_6084_() && !this.m_21525_() && this.supp$isConverting()) {
            this.supplementaries$conversionTime--;
            if (this.supplementaries$conversionTime == 0) {
                this.doHorseConvertion();
            }
        }
    }

    @Inject(method = { "mobInteract" }, at = { @At("HEAD") }, cancellable = true)
    public void interact(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemstack = pPlayer.m_21120_(pHand);
        if (itemstack.is(Items.GOLDEN_CARROT) && this.m_21023_(MobEffects.WEAKNESS) && (Boolean) CommonConfigs.Tweaks.ZOMBIE_HORSE_CONVERSION.get()) {
            if (!pPlayer.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            Level level = this.m_9236_();
            this.m_5584_(level, itemstack);
            if (!level.isClientSide) {
                this.startConverting();
            }
            cir.cancel();
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
        }
    }
}