package com.mna.items.constructs.parts.arms;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ConstructPartShieldArmRight extends ItemConstructPart {

    public ConstructPartShieldArmRight(ConstructMaterial material) {
        super(material, ConstructSlot.RIGHT_ARM, 32);
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.MELEE_ATTACK, ConstructCapability.BLOCK };
    }

    @Override
    public float getAttackDamage() {
        return this.getMaterial().getDamageBonus();
    }

    @Override
    public int getAttackSpeedModifier() {
        return 20;
    }

    @Override
    public int getArmor() {
        return this.getMaterial().getArmorBonus(this.getSlot());
    }

    @Override
    public int getToughness() {
        return this.getMaterial().getToughnessBonus(this.getSlot());
    }

    @Override
    public float getKnockbackBonus() {
        return 0.5F;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack itemstack = player1.m_21120_(interactionHand2);
        player1.m_6672_(interactionHand2);
        return InteractionResultHolder.consume(itemstack);
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
    }
}