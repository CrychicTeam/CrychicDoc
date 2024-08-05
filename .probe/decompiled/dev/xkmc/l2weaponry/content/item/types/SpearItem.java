package dev.xkmc.l2weaponry.content.item.types;

import com.google.common.collect.ImmutableMultimap.Builder;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2weaponry.content.item.base.GenericWeaponItem;
import dev.xkmc.l2weaponry.init.data.LangData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public class SpearItem extends GenericWeaponItem {

    public static final AttributeModifier RANGE = new AttributeModifier(MathHelper.getUUIDFromString("spear_range"), "spear_range", 2.0, AttributeModifier.Operation.ADDITION);

    public static final AttributeModifier REACH = new AttributeModifier(MathHelper.getUUIDFromString("spear_reach"), "spear_reach", 2.0, AttributeModifier.Operation.ADDITION);

    public SpearItem(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config, BlockTags.MINEABLE_WITH_SHOVEL);
    }

    @Override
    protected void addModifiers(Builder<Attribute, AttributeModifier> builder) {
        super.addModifiers(builder);
        builder.put(ForgeMod.ENTITY_REACH.get(), RANGE);
        builder.put(ForgeMod.BLOCK_REACH.get(), REACH);
    }

    @Override
    protected boolean canSweep() {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.TOOL_SPEAR.get());
        super.appendHoverText(pStack, pLevel, list, pIsAdvanced);
    }

    @Override
    public AABB getSweepHitBoxImpl(ItemStack stack, LivingEntity player, Entity target) {
        return target.getBoundingBox().inflate(3.0, 0.5, 3.0);
    }
}