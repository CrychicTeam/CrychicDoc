package top.theillusivec4.curios.api.type.capability;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import top.theillusivec4.curios.api.SlotContext;

public interface ICurio {

    ItemStack getStack();

    default void curioTick(SlotContext slotContext) {
        this.curioTick(slotContext.identifier(), slotContext.index(), slotContext.entity());
    }

    default void onEquip(SlotContext slotContext, ItemStack prevStack) {
        this.onEquip(slotContext.identifier(), slotContext.index(), slotContext.entity());
    }

    default void onUnequip(SlotContext slotContext, ItemStack newStack) {
        this.onUnequip(slotContext.identifier(), slotContext.index(), slotContext.entity());
    }

    default boolean canEquip(SlotContext slotContext) {
        return true;
    }

    default boolean canUnequip(SlotContext slotContext) {
        return true;
    }

    default List<Component> getSlotsTooltip(List<Component> tooltips) {
        return this.getTagsTooltip(tooltips);
    }

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
        return this.getAttributeModifiers(slotContext.identifier());
    }

    default void onEquipFromUse(SlotContext slotContext) {
        this.playRightClickEquipSound(slotContext.entity());
    }

    @Nonnull
    default ICurio.SoundInfo getEquipSound(SlotContext slotContext) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
    }

    default boolean canEquipFromUse(SlotContext slotContext) {
        return this.canRightClickEquip();
    }

    default void curioBreak(SlotContext slotContext) {
        this.curioBreak(this.getStack(), slotContext.entity());
    }

    default boolean canSync(SlotContext slotContext) {
        return this.canSync(slotContext.identifier(), slotContext.index(), slotContext.entity());
    }

    @Nullable
    default CompoundTag writeSyncData(SlotContext slotContext) {
        return this.writeSyncData();
    }

    default void readSyncData(SlotContext slotContext, CompoundTag compound) {
        this.readSyncData(compound);
    }

    @Nonnull
    default ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit) {
        return this.getDropRule(slotContext.entity());
    }

    default List<Component> getAttributesTooltip(List<Component> tooltips) {
        return (List<Component>) (this.showAttributesTooltip("") ? tooltips : new ArrayList());
    }

    default int getFortuneLevel(SlotContext slotContext, @Nullable LootContext lootContext) {
        return this.getFortuneBonus(slotContext.identifier(), slotContext.entity(), this.getStack(), slotContext.index());
    }

    default int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting) {
        return this.getLootingBonus(slotContext.identifier(), slotContext.entity(), this.getStack(), slotContext.index());
    }

    default boolean makesPiglinsNeutral(SlotContext slotContext) {
        return this.getStack().makesPiglinsNeutral(slotContext.entity());
    }

    default boolean canWalkOnPowderedSnow(SlotContext slotContext) {
        return this.getStack().canWalkOnPowderedSnow(slotContext.entity());
    }

    default boolean isEnderMask(SlotContext slotContext, EnderMan enderMan) {
        return slotContext.entity() instanceof Player player ? this.getStack().isEnderMask(player, enderMan) : false;
    }

    static void playBreakAnimation(ItemStack stack, LivingEntity livingEntity) {
        if (!stack.isEmpty()) {
            if (!livingEntity.m_20067_()) {
                livingEntity.m_9236_().playLocalSound(livingEntity.m_20185_(), livingEntity.m_20186_(), livingEntity.m_20189_(), SoundEvents.ITEM_BREAK, livingEntity.m_5720_(), 0.8F, 0.8F + livingEntity.m_9236_().random.nextFloat() * 0.4F, false);
            }
            for (int i = 0; i < 5; i++) {
                Vec3 vec3d = new Vec3(((double) livingEntity.getRandom().nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
                vec3d = vec3d.xRot(-livingEntity.m_146909_() * (float) (Math.PI / 180.0));
                vec3d = vec3d.yRot(-livingEntity.m_146908_() * (float) (Math.PI / 180.0));
                double d0 = (double) (-livingEntity.getRandom().nextFloat()) * 0.6 - 0.3;
                Vec3 vec3d1 = new Vec3(((double) livingEntity.getRandom().nextFloat() - 0.5) * 0.3, d0, 0.6);
                vec3d1 = vec3d1.xRot(-livingEntity.m_146909_() * (float) (Math.PI / 180.0));
                vec3d1 = vec3d1.yRot(-livingEntity.m_146908_() * (float) (Math.PI / 180.0));
                vec3d1 = vec3d1.add(livingEntity.m_20185_(), livingEntity.m_20186_() + (double) livingEntity.m_20192_(), livingEntity.m_20189_());
                livingEntity.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05, vec3d.z);
            }
        }
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void curioTick(String identifier, int index, LivingEntity livingEntity) {
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void curioAnimate(String identifier, int index, LivingEntity livingEntity) {
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void curioBreak(ItemStack stack, LivingEntity livingEntity) {
        playBreakAnimation(stack, livingEntity);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void onEquip(String identifier, int index, LivingEntity livingEntity) {
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void onUnequip(String identifier, int index, LivingEntity livingEntity) {
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default boolean canEquip(String identifier, LivingEntity livingEntity) {
        return true;
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default boolean canUnequip(String identifier, LivingEntity livingEntity) {
        return true;
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default List<Component> getTagsTooltip(List<Component> tagTooltips) {
        return tagTooltips;
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default int getFortuneBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, curio);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default int getLootingBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, curio);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default boolean showAttributesTooltip(String identifier) {
        return true;
    }

    @Nonnull
    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default ICurio.DropRule getDropRule(LivingEntity livingEntity) {
        return ICurio.DropRule.DEFAULT;
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default boolean canSync(String identifier, int index, LivingEntity livingEntity) {
        return false;
    }

    @Deprecated(forRemoval = true)
    @Nullable
    @ScheduledForRemoval(inVersion = "1.21")
    default CompoundTag writeSyncData() {
        return new CompoundTag();
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void readSyncData(CompoundTag compound) {
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default boolean canRightClickEquip() {
        return false;
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void playRightClickEquipSound(LivingEntity livingEntity) {
        ICurio.SoundInfo soundInfo = this.getEquipSound(new SlotContext("", livingEntity, 0, false, true));
        livingEntity.m_9236_().playSound(null, livingEntity.m_20183_(), soundInfo.getSoundEvent(), livingEntity.m_5720_(), soundInfo.getVolume(), soundInfo.getPitch());
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier) {
        return HashMultimap.create();
    }

    public static enum DropRule {

        DEFAULT, ALWAYS_DROP, ALWAYS_KEEP, DESTROY
    }

    public static record SoundInfo(SoundEvent soundEvent, float volume, float pitch) {

        @Deprecated(forRemoval = true, since = "1.20.1")
        @ScheduledForRemoval(inVersion = "1.22")
        public SoundEvent getSoundEvent() {
            return this.soundEvent;
        }

        @Deprecated(forRemoval = true, since = "1.20.1")
        @ScheduledForRemoval(inVersion = "1.22")
        public float getVolume() {
            return this.volume;
        }

        @Deprecated(forRemoval = true, since = "1.20.1")
        @ScheduledForRemoval(inVersion = "1.22")
        public float getPitch() {
            return this.pitch;
        }
    }
}