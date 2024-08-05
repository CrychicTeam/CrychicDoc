package io.redspace.ironsspellbooks.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.attribute.IMagicAttribute;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.weapons.IMultihandWeapon;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LivingEntity.class })
public abstract class LivingEntityMixin {

    @Unique
    private static final List<EquipmentSlot> handSlots = List.of(EquipmentSlot.OFFHAND, EquipmentSlot.MAINHAND);

    @Unique
    private static final Predicate<Attribute> allNonBaseAttackAttributes = attribute -> attribute != ForgeMod.ENTITY_REACH.get() && attribute != Attributes.ATTACK_DAMAGE && attribute != Attributes.ATTACK_SPEED && attribute != Attributes.ATTACK_KNOCKBACK;

    @Unique
    private static final Predicate<Attribute> onlyIronAttributes = attribute -> attribute instanceof IMagicAttribute;

    @Inject(method = { "updateInvisibilityStatus" }, at = { @At("TAIL") })
    public void updateInvisibilityStatus(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) this;
        if (self.hasEffect(MobEffectRegistry.TRUE_INVISIBILITY.get())) {
            self.m_6842_(true);
        }
    }

    @Inject(method = { "getArmorCoverPercentage" }, at = { @At("HEAD") }, cancellable = true)
    public void getArmorCoverPercentage(CallbackInfoReturnable<Float> cir) {
        if (((LivingEntity) this).hasEffect(MobEffectRegistry.TRUE_INVISIBILITY.get())) {
            cir.setReturnValue(0.0F);
        }
    }

    @Inject(method = { "isCurrentlyGlowing" }, at = { @At("HEAD") }, cancellable = true)
    public void isCurrentlyGlowing(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) this;
        if (!self.f_19853_.isClientSide() && self.hasEffect(MobEffectRegistry.GUIDING_BOLT.get())) {
            cir.setReturnValue(true);
        }
    }

    @Shadow
    abstract ItemStack getLastHandItem(EquipmentSlot var1);

    @Inject(method = { "collectEquipmentChanges" }, at = { @At("RETURN") })
    public void handleEquipmentChanges(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
        Map<EquipmentSlot, ItemStack> changedEquipment = (Map<EquipmentSlot, ItemStack>) cir.getReturnValue();
        if (changedEquipment != null) {
            LivingEntity self = (LivingEntity) this;
            for (EquipmentSlot slot : handSlots) {
                ItemStack currentStack = (ItemStack) changedEquipment.get(slot);
                if (currentStack != null) {
                    ItemStack oldStack = this.getLastHandItem(slot);
                    boolean selected = currentStack.getItem() instanceof IMultihandWeapon;
                    boolean deselected = oldStack.getItem() instanceof IMultihandWeapon;
                    if (selected || deselected) {
                        if (slot == EquipmentSlot.MAINHAND) {
                            ItemStack offhandStack = self.getOffhandItem();
                            if (offhandStack.getItem() instanceof IMultihandWeapon && !ItemStack.isSameItem(offhandStack, currentStack)) {
                                if (selected) {
                                    self.getAttributes().removeAttributeModifiers(filterApplicableAttributes(offhandStack.getAttributeModifiers(EquipmentSlot.MAINHAND)));
                                }
                                if (deselected) {
                                    self.getAttributes().addTransientAttributeModifiers(filterApplicableAttributes(offhandStack.getAttributeModifiers(EquipmentSlot.MAINHAND)));
                                }
                            }
                        } else if (slot == EquipmentSlot.OFFHAND) {
                            ItemStack mainhandStack = self.getMainHandItem();
                            if (selected && !(mainhandStack.getItem() instanceof IMultihandWeapon)) {
                                self.getAttributes().addTransientAttributeModifiers(filterApplicableAttributes(currentStack.getAttributeModifiers(EquipmentSlot.MAINHAND)));
                            }
                            if (deselected && !ItemStack.isSameItem(mainhandStack, oldStack)) {
                                self.getAttributes().removeAttributeModifiers(filterApplicableAttributes(oldStack.getAttributeModifiers(EquipmentSlot.MAINHAND)));
                            }
                        }
                    }
                }
            }
        }
    }

    @Unique
    private static Multimap<Attribute, AttributeModifier> filterApplicableAttributes(Multimap<Attribute, AttributeModifier> attributeModifierMap) {
        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
        for (Attribute attribute : attributeModifierMap.keySet()) {
            Predicate<Attribute> predicate = ServerConfigs.APPLY_ALL_MULTIHAND_ATTRIBUTES.get() ? allNonBaseAttackAttributes : onlyIronAttributes;
            if (predicate.test(attribute)) {
                map.putAll(attribute, attributeModifierMap.get(attribute));
            }
        }
        return map;
    }
}