package com.mna.spells.shapes;

import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.entities.sorcery.targeting.SpellWave;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ShapeWave extends Shape {

    public ShapeWave(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DURATION, 3.0F, 1.0F, 10.0F, 0.5F, 10.0F), new AttributeValuePair(Attribute.WIDTH, 1.0F, 1.0F, 9.0F, 1.0F, 3.0F), new AttributeValuePair(Attribute.HEIGHT, 1.0F, 1.0F, 9.0F, 1.0F, 5.0F), new AttributeValuePair(Attribute.SPEED, 0.3F, 0.1F, 1.0F, 0.1F, 2.0F), new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F));
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe) {
        if (!world.isClientSide) {
            SpellWave projectile = new SpellWave(source.getCaster(), recipe, world, modificationData.getValue(Attribute.PRECISION) == 1.0F);
            world.m_7967_(projectile);
        }
        return Arrays.asList(new SpellTarget(source.getCaster()));
    }

    @Override
    public boolean spawnsTargetEntity() {
        return true;
    }

    @Override
    public boolean isChanneled() {
        return true;
    }

    @Override
    public float initialComplexity() {
        return 45.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 12000;
    }
}