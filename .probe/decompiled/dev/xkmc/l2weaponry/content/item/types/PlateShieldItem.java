package dev.xkmc.l2weaponry.content.item.types;

import com.google.common.collect.ImmutableMultimap.Builder;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2weaponry.content.item.base.DoubleHandItem;
import dev.xkmc.l2weaponry.content.item.base.GenericShieldItem;
import dev.xkmc.l2weaponry.init.data.LangData;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class PlateShieldItem extends GenericShieldItem implements DoubleHandItem {

    private static final String NAME_ATTR = "shield_defense";

    private static final String NAME_KB = "shield_knockback";

    private static final String NAME_CRIT = "shield_crit";

    public PlateShieldItem(Tier tier, int maxDefense, float recover, Item.Properties prop, ExtraToolConfig config) {
        super(tier, prop, config, maxDefense, (double) recover, false);
    }

    @Override
    public void modifySource(LivingEntity attacker, CreateSourceEvent event, ItemStack item, @Nullable Entity target) {
        event.enable(DefaultDamageState.BYPASS_ARMOR);
    }

    @Override
    protected void buildAttributes(EquipmentSlot slot, Builder<Attribute, AttributeModifier> builder) {
        if (slot != EquipmentSlot.OFFHAND) {
            super.buildAttributes(slot, builder);
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", (double) Math.round((double) this.maxDefense * 0.035), AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", 16.0, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(MathHelper.getUUIDFromString("slow_wield"), "slow_wield", -0.95, AttributeModifier.Operation.MULTIPLY_TOTAL));
            builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(MathHelper.getUUIDFromString("shield_knockback"), "shield_knockback", 4.0, AttributeModifier.Operation.ADDITION));
            builder.put((Attribute) LWItems.REFLECT_TIME.get(), new AttributeModifier(MathHelper.getUUIDFromString("shield_defense"), "shield_defense", 20.0, AttributeModifier.Operation.ADDITION));
            builder.put((Attribute) L2DamageTracker.CRIT_DMG.get(), new AttributeModifier(MathHelper.getUUIDFromString("shield_crit"), "shield_crit", 1.5, AttributeModifier.Operation.ADDITION));
        }
    }

    @Override
    public boolean disableOffHand(Player player, ItemStack stack) {
        return !this.lightWeight(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (!this.lightWeight(newStack)) {
            ItemStack off = Proxy.getClientPlayer().m_21206_().copy();
            Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().offHandItem = off;
            off.getOrCreateTag().putBoolean("reequip", true);
        }
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.TOOL_PLATE_SHIELD.get());
        list.add(LangData.TOOL_PLATE_SHIELD_EXTRA.get());
        super.appendHoverText(pStack, pLevel, list, pIsAdvanced);
    }
}