package com.mna.spells.shapes;

import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ShapeSelf extends Shape {

    public ShapeSelf(ResourceLocation guiIcon) {
        super(guiIcon);
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modifiedData, ISpellDefinition recipe) {
        return source.getCaster() == null ? Arrays.asList(SpellTarget.NONE) : Arrays.asList(new SpellTarget(source.getCaster()));
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
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }

    @Override
    public boolean affectsCaster() {
        return true;
    }
}