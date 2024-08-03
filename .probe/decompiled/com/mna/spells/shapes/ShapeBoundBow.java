package com.mna.spells.shapes;

import com.mna.api.spells.SpellPartTags;
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

public class ShapeBoundBow extends Shape {

    public ShapeBoundBow(ResourceLocation guiIcon) {
        super(guiIcon);
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modifiedData, ISpellDefinition recipe) {
        if (source.getCaster() != null && source.isPlayerCaster()) {
            ItemStack held = source.getPlayer().m_21120_(source.getHand());
            ItemStack replacement = ItemInit.BOUND_BOW.get().createFromSpell(held, recipe);
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