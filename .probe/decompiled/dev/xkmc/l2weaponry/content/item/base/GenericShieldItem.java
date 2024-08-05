package dev.xkmc.l2weaponry.content.item.base;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.init.materials.LWExtraConfig;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GenericShieldItem extends BaseShieldItem implements LWTieredItem {

    private final Tier tier;

    private final ExtraToolConfig config;

    public GenericShieldItem(Tier tier, Item.Properties prop, ExtraToolConfig config, int maxDefense, double recover, boolean lightWeight) {
        super(prop.defaultDurability(tier.getUses()), maxDefense, 4.0 + recover, lightWeight);
        this.tier = tier;
        this.config = config;
    }

    @Override
    public boolean isHeavy() {
        return !this.lightWeight;
    }

    @Override
    public int getEnchantmentValue() {
        return this.tier.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return this.tier.getRepairIngredient().test(pRepair) || super.m_6832_(pToRepair, pRepair);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.m_6883_(stack, level, entity, slot, selected);
        this.config.inventoryTick(stack, level, entity, slot, selected);
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return this.config.damageItem(stack, super.damageItem(stack, amount, entity, onBroken), entity);
    }

    @Override
    public boolean canBeDepleted() {
        return this.config.canBeDepleted;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
        this.config.onHit(stack, target, user);
        if (this.config.sword_hit > 0) {
            stack.hurtAndBreak(this.config.sword_hit, user, level -> level.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (this.config.sword_mine > 0 && state.m_60800_(level, pos) != 0.0F) {
            stack.hurtAndBreak(this.config.sword_mine, entity, l -> l.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    public ExtraToolConfig getExtraConfig() {
        return this.config;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> parent = super.getAttributeModifiers(slot, stack);
        if (slot != EquipmentSlot.MAINHAND) {
            return parent;
        } else {
            Multimap<Attribute, AttributeModifier> cur = HashMultimap.create();
            cur.putAll(parent);
            return this.config.modify(cur, slot, stack);
        }
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        float old = super.m_8102_(stack, state);
        return this.config.getDestroySpeed(stack, state, old);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        super.m_7373_(stack, level, list, flag);
        this.config.addTooltip(stack, list);
    }

    @Override
    public void onBlock(ItemStack stack, LivingEntity user, LivingEntity target) {
        if (this.getExtraConfig() instanceof LWExtraConfig extra) {
            extra.onShieldBlock(stack, user, target);
        }
    }

    @Override
    public double onReflect(ItemStack stack, LivingEntity user, LivingEntity target, double original, double reflect) {
        return this.getExtraConfig() instanceof LWExtraConfig extra ? extra.onShieldReflect(stack, user, target, original, reflect) : reflect;
    }

    @Override
    protected DamageSource getReflectSource(Player player) {
        if (this.getExtraConfig() instanceof LWExtraConfig extra) {
            DamageSource ans = extra.getReflectSource(player);
            if (ans != null) {
                return ans;
            }
        }
        return super.getReflectSource(player);
    }

    @Override
    public double getDefenseRecover(ItemStack stack) {
        return this.recover;
    }
}