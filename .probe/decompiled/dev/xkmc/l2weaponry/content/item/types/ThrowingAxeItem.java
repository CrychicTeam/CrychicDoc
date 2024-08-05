package dev.xkmc.l2weaponry.content.item.types;

import com.google.common.collect.ImmutableMultimap.Builder;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2weaponry.content.entity.ThrowingAxeEntity;
import dev.xkmc.l2weaponry.content.item.base.BaseThrowableWeaponItem;
import dev.xkmc.l2weaponry.init.data.LangData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public class ThrowingAxeItem extends BaseThrowableWeaponItem {

    public static final AttributeModifier RANGE = new AttributeModifier(MathHelper.getUUIDFromString("throwing_axe_reach"), "throwing_axe_reach", -0.5, AttributeModifier.Operation.ADDITION);

    public ThrowingAxeItem(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config, BlockTags.MINEABLE_WITH_AXE);
    }

    @Override
    protected void addModifiers(Builder<Attribute, AttributeModifier> builder) {
        super.addModifiers(builder);
        builder.put(ForgeMod.ENTITY_REACH.get(), RANGE);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.TOOL_THROWING_AXE.get());
        super.m_7373_(pStack, pLevel, list, pIsAdvanced);
    }

    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return true;
    }

    public ThrowingAxeEntity getProjectile(Level level, LivingEntity player, ItemStack stack, int slot) {
        return new ThrowingAxeEntity(level, player, stack, slot);
    }

    @Override
    public boolean isHeavy() {
        return true;
    }
}