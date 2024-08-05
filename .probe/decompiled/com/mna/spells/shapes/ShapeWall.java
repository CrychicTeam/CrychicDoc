package com.mna.spells.shapes;

import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.entities.sorcery.targeting.SpellWall;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ShapeWall extends ShapeRaytrace {

    public ShapeWall(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DURATION, 3.0F, 1.0F, 10.0F, 0.5F, 10.0F), new AttributeValuePair(Attribute.WIDTH, 3.0F, 1.0F, 9.0F, 1.0F, 3.0F), new AttributeValuePair(Attribute.HEIGHT, 2.0F, 1.0F, 9.0F, 1.0F, 5.0F), new AttributeValuePair(Attribute.RANGE, 8.0F, 8.0F, 32.0F, 1.0F, 2.0F), new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F));
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe) {
        List<SpellTarget> tgts = super.Target(source, world, modificationData, recipe);
        SpellTarget tgt = (SpellTarget) tgts.get(0);
        if (tgt == SpellTarget.NONE) {
            return tgts;
        } else {
            if (!world.isClientSide) {
                Entity projectile = new SpellWall(source.getCaster(), recipe, world, modificationData.getValue(Attribute.PRECISION) == 1.0F);
                if (tgt.isBlock()) {
                    BlockPos pos = tgt.getBlock().relative(tgt.getBlockFace(null));
                    projectile.setPos((double) ((float) pos.m_123341_() + 0.5F), (double) pos.m_123342_(), (double) ((float) pos.m_123343_() + 0.5F));
                } else if (tgt.isEntity()) {
                    projectile.setPos(tgt.getEntity().getX(), tgt.getEntity().getY(), tgt.getEntity().getZ());
                }
                world.m_7967_(projectile);
            }
            return Arrays.asList(new SpellTarget(source.getCaster()));
        }
    }

    @Override
    public boolean spawnsTargetEntity() {
        return true;
    }

    @Override
    public boolean isChanneled() {
        return false;
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public int baselineCooldown() {
        return 40;
    }
}