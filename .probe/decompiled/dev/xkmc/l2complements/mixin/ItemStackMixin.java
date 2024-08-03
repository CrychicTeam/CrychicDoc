package dev.xkmc.l2complements.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2complements.content.enchantment.special.LifeSyncEnchantment;
import dev.xkmc.l2complements.events.SpecialEquipmentEvents;
import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import dev.xkmc.l2library.util.Proxy;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Consumer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ItemStack.class })
public abstract class ItemStackMixin implements IForgeItemStack {

    @ModifyVariable(at = @At("LOAD"), method = { "hurtAndBreak" }, argsOnly = true)
    public int l2complements_hurtAndBreak_hardened(int pAmount) {
        ItemStack self = (ItemStack) this;
        return pAmount > 1 && self.getEnchantmentLevel((Enchantment) LCEnchantments.HARDENED.get()) > 0 ? 1 : pAmount;
    }

    @Inject(at = { @At("HEAD") }, method = { "hurtAndBreak" }, cancellable = true)
    public <T extends LivingEntity> void l2complements_hurtAndBreak_lifeSync(int pAmount, T pEntity, Consumer<T> pOnBroken, CallbackInfo ci) {
        ItemStack self = (ItemStack) this;
        if (!pEntity.m_9236_().isClientSide()) {
            if (self.getEnchantmentLevel((Enchantment) LCEnchantments.ETERNAL.get()) > 0) {
                ci.cancel();
            }
            if (self.getEnchantmentLevel((Enchantment) LCEnchantments.LIFE_SYNC.get()) > 0) {
                GeneralEventHandler.schedule(() -> pEntity.hurt(LifeSyncEnchantment.getSource(pEntity.m_9236_()), (float) ((double) pAmount * LCConfig.COMMON.lifeSyncFactor.get())));
                ci.cancel();
            }
            if (!((Stack) SpecialEquipmentEvents.PLAYER.get()).isEmpty()) {
                BlockState state = (BlockState) ((Pair) ((Stack) SpecialEquipmentEvents.PLAYER.get()).peek()).getSecond();
                if (self.getEnchantmentLevel((Enchantment) LCEnchantments.TREE.get()) >= 2 && state.m_204336_(TagGen.AS_LEAF)) {
                    ci.cancel();
                }
            }
        }
    }

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V") }, method = { "hurt" })
    public void l2complements_hurt_safeguard_setDamage(ItemStack self, int val, Operation<Void> op) {
        int max = self.getMaxDamage();
        if (max <= val + 1 && self.getEnchantmentLevel((Enchantment) LCEnchantments.SAFEGUARD.get()) > 0) {
            Optional<MinecraftServer> opt = Proxy.getServer();
            if (opt.isPresent()) {
                int old = self.getDamageValue();
                String key = LCEnchantments.SAFEGUARD.getId().toString();
                long time = self.getOrCreateTag().getLong(key);
                long current = ((MinecraftServer) opt.get()).overworld().m_46467_();
                if (max <= val) {
                    if (current == time) {
                        val = old;
                    } else if (max > old + 1) {
                        val = max - 1;
                        self.getOrCreateTag().putLong(key, current);
                    }
                } else if (max == val + 1) {
                    self.getOrCreateTag().putLong(key, current);
                }
            }
        }
        op.call(new Object[] { self, val });
    }

    @ModifyReturnValue(at = { @At("RETURN") }, method = { "hurt" })
    public boolean l2complements_hurt_safeguard_preventBreaking(boolean broken) {
        ItemStack self = (ItemStack) this;
        return broken && self.getDamageValue() >= self.getMaxDamage();
    }

    @ModifyReturnValue(at = { @At("RETURN") }, method = { "getMaxDamage" })
    public int l2complements_getMaxDamage_durabilityEnchantment(int max) {
        ItemStack self = (ItemStack) this;
        int lv = self.getEnchantmentLevel((Enchantment) LCEnchantments.DURABLE_ARMOR.get());
        return max * (1 + lv);
    }

    @NotNull
    @Override
    public AABB getSweepHitBox(@NotNull Player player, @NotNull Entity target) {
        ItemStack self = (ItemStack) this;
        return self.getItem().getSweepHitBox(self, player, target);
    }

    @ModifyReturnValue(at = { @At("RETURN") }, method = { "getSweepHitBox" }, remap = false)
    public AABB l2complements_getSweepHitBox_enchantOverride(AABB box) {
        int lv = this.getEnchantmentLevel((Enchantment) LCEnchantments.WIND_SWEEP.get());
        if (lv > 0) {
            double amount = LCConfig.COMMON.windSweepIncrement.get();
            box = box.inflate(amount * (double) lv, amount * (double) lv, amount * (double) lv);
        }
        return box;
    }

    @Override
    public boolean makesPiglinsNeutral(LivingEntity wearer) {
        ItemStack self = (ItemStack) this;
        return self.getItem().makesPiglinsNeutral(self, wearer);
    }

    @Inject(at = { @At("HEAD") }, method = { "makesPiglinsNeutral" }, cancellable = true, remap = false)
    public void l2complements_makesPiglinsNeutral_enchantOverride(LivingEntity wearer, CallbackInfoReturnable<Boolean> cir) {
        if (this.getEnchantmentLevel((Enchantment) LCEnchantments.SHINNY.get()) > 0) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public boolean isPiglinCurrency() {
        ItemStack self = (ItemStack) this;
        return self.getItem().isPiglinCurrency(self);
    }

    @Inject(at = { @At("HEAD") }, method = { "isPiglinCurrency" }, cancellable = true, remap = false)
    public void l2complements_isPiglinCurrency_enchantOverride(CallbackInfoReturnable<Boolean> cir) {
        if (this.getEnchantmentLevel((Enchantment) LCEnchantments.SHINNY.get()) > 0) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public boolean isEnderMask(Player player, EnderMan endermanEntity) {
        ItemStack self = (ItemStack) this;
        return self.getItem().isEnderMask(self, player, endermanEntity);
    }

    @Inject(at = { @At("HEAD") }, method = { "isEnderMask" }, cancellable = true, remap = false)
    public void l2complements_isEnderMask_enchantOverride(CallbackInfoReturnable<Boolean> cir) {
        if (this.getEnchantmentLevel((Enchantment) LCEnchantments.ENDER_MASK.get()) > 0) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public boolean canWalkOnPowderedSnow(LivingEntity wearer) {
        ItemStack self = (ItemStack) this;
        return self.getItem().canWalkOnPowderedSnow(self, wearer);
    }

    @Inject(at = { @At("HEAD") }, method = { "canWalkOnPowderedSnow" }, cancellable = true, remap = false)
    public void l2complements_canWalkOnPowderedSnow_enchantOverride(CallbackInfoReturnable<Boolean> cir) {
        if (this.getEnchantmentLevel((Enchantment) LCEnchantments.SNOW_WALKER.get()) > 0) {
            cir.setReturnValue(true);
        }
    }
}