package com.mna.items.constructs.parts.arms;

import com.mna.ManaAndArtifice;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.api.spells.ICanContainSpell;
import com.mna.effects.EffectInit;
import com.mna.items.sorcery.ItemSpell;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ConstructPartCasterArmLeft extends ItemConstructPart implements ICanContainSpell {

    public ConstructPartCasterArmLeft(ConstructMaterial material) {
        super(material, ConstructSlot.LEFT_ARM, 128);
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.CAST_SPELL };
    }

    @Override
    public float getAttackDamage() {
        return 0.0F;
    }

    @Override
    public int getAttackSpeedModifier() {
        return 10;
    }

    @Override
    public float getRangedAttackDamage() {
        return 0.0F;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> lines, TooltipFlag flags) {
        super.m_7373_(stack, world, lines, flags);
        ICanContainSpell.super.getSpell(stack).addItemTooltip(stack, world, lines, ManaAndArtifice.instance.proxy.getClientPlayer());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack held = player.m_21120_(hand);
        return ItemSpell.castSpellOnUse(held, world, player, hand, this::shouldConsumeMana);
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 99999;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity player, ItemStack stack, int count) {
        if (player instanceof Player) {
            ItemSpell.castSpellOnChannelTick(stack, (Player) player, count, this::shouldConsumeChanneledMana);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (entityLiving instanceof Player) {
            ((Player) entityLiving).m_21253_();
        }
        return stack;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            SpellRecipe recipe = SpellRecipe.fromNBT(stack.getTag());
            SpellCaster.setCooldown(stack, player, recipe.getCooldown(player));
            if (this.shouldConsumeMana(stack)) {
                if (entityLiving.hasEffect(EffectInit.MANA_STUNT.get()) && entityLiving.getEffect(EffectInit.MANA_STUNT.get()).getAmplifier() == 5) {
                    entityLiving.removeEffect(EffectInit.MANA_STUNT.get());
                }
                SpellCaster.AddAffinityAndMagicXP(recipe, player, recipe.getMaxChannelTime() - timeLeft);
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack itemStack0) {
        return SpellRecipe.stackContainsSpell(itemStack0);
    }

    protected boolean shouldConsumeMana(ItemStack stack) {
        return true;
    }

    protected boolean shouldConsumeChanneledMana(Player player, ItemStack stack) {
        return true;
    }
}