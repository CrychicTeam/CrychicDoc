package dev.xkmc.l2weaponry.content.item.types;

import com.google.common.collect.ImmutableMultimap.Builder;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2weaponry.content.item.base.BaseClawItem;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.data.LangData;
import dev.xkmc.l2weaponry.init.registrate.LWEnchantments;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public class ClawItem extends BaseClawItem {

    public static final AttributeModifier RANGE = new AttributeModifier(MathHelper.getUUIDFromString("claw_reach"), "claw_reach", -1.0, AttributeModifier.Operation.ADDITION);

    public ClawItem(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config);
    }

    @Override
    protected void addModifiers(Builder<Attribute, AttributeModifier> builder) {
        super.addModifiers(builder);
        builder.put(ForgeMod.ENTITY_REACH.get(), RANGE);
    }

    @Override
    public float getBlockTime(LivingEntity player) {
        int ans = LWConfig.COMMON.claw_block_time.get();
        ans += player.getMainHandItem().getEnchantmentLevel((Enchantment) LWEnchantments.CLAW_BLOCK.get());
        if (player.getOffhandItem().getItem() == this) {
            ans += player.getOffhandItem().getEnchantmentLevel((Enchantment) LWEnchantments.CLAW_BLOCK.get());
        }
        return (float) ans;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.TOOL_CLAW.get());
        list.add(LangData.TOOL_CLAW_EXTRA.get());
        super.m_7373_(pStack, pLevel, list, pIsAdvanced);
    }
}