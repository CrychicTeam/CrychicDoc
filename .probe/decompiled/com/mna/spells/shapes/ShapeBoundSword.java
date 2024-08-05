package com.mna.spells.shapes;

import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.items.ItemInit;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ShapeBoundSword extends Shape {

    public ShapeBoundSword(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DAMAGE, 6.0F, 0.0F, 11.0F, 0.5F, 3.0F), new AttributeValuePair(Attribute.RANGE, 0.0F, 0.0F, 3.0F, 1.0F, 8.0F));
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modifiedData, ISpellDefinition recipe) {
        if (source.getCaster() != null && source.isPlayerCaster()) {
            ItemStack held = source.getPlayer().m_21120_(source.getHand());
            ItemStack replacement = ItemInit.BOUND_SWORD.get().createFromSpell(held, recipe);
            int slot = source.getHand() == InteractionHand.MAIN_HAND ? source.getPlayer().getInventory().selected : 40;
            source.getPlayer().getInventory().setItem(slot, replacement);
            return Arrays.asList(new SpellTarget(source.getCaster()));
        } else {
            return Arrays.asList(SpellTarget.NONE);
        }
    }

    @Override
    public float initialComplexity() {
        return 5.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public boolean spawnsTargetEntity() {
        return true;
    }

    @Override
    public boolean canBeOnRandomStaff() {
        return false;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }

    @Override
    public boolean grantComponentRoteXP() {
        return false;
    }
}