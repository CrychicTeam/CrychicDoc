package dev.xkmc.l2weaponry.content.item.types;

import com.google.common.collect.ImmutableMultimap.Builder;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2weaponry.content.item.base.GenericWeaponItem;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.data.LangData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public class DaggerItem extends GenericWeaponItem {

    public static final AttributeModifier RANGE = new AttributeModifier(MathHelper.getUUIDFromString("dagger_reach"), "dagger_reach", -1.0, AttributeModifier.Operation.ADDITION);

    public DaggerItem(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config, BlockTags.MINEABLE_WITH_HOE);
    }

    @Override
    protected void addModifiers(Builder<Attribute, AttributeModifier> builder) {
        super.addModifiers(builder);
        builder.put(ForgeMod.ENTITY_REACH.get(), RANGE);
    }

    @Override
    public float getMultiplier(AttackCache event) {
        if (event.getAttackTarget() instanceof Mob le && le.getTarget() != event.getAttacker()) {
            return (float) LWConfig.COMMON.dagger_bonus.get().doubleValue();
        }
        return 1.0F;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.TOOL_DAGGER.get());
        super.appendHoverText(pStack, pLevel, list, pIsAdvanced);
    }
}