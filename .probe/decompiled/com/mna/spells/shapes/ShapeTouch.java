package com.mna.spells.shapes;

import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

public class ShapeTouch extends ShapeRaytrace {

    public ShapeTouch(ResourceLocation icon) {
        super(icon, new AttributeValuePair(Attribute.RADIUS, 0.0F, 0.0F, 3.0F, 1.0F));
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe) {
        List<SpellTarget> targets = super.Target(source, world, modificationData, recipe);
        return this.target_internal(source, world, modificationData, recipe, (SpellTarget) targets.get(0));
    }

    @Override
    public List<SpellTarget> TargetNPCCast(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe, SpellTarget targetHint) {
        return this.target_internal(source, world, modificationData, recipe, targetHint);
    }

    private List<SpellTarget> target_internal(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe, SpellTarget target) {
        int radius = (int) Math.floor((double) modificationData.getValue(Attribute.RADIUS));
        if (radius > 0 && target != SpellTarget.NONE) {
            if (target.isBlock()) {
                return this.targetBlocksRadius(target, radius);
            }
            if (target.isEntity()) {
                return this.targetBlocksEntity(source, target, radius, world);
            }
        }
        return Arrays.asList(target);
    }

    @Override
    protected float getRange(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe) {
        return source.isPlayerCaster() ? (float) source.getPlayer().m_21051_(ForgeMod.BLOCK_REACH.get()).getValue() : 8.0F;
    }

    private List<SpellTarget> targetBlocksRadius(SpellTarget origin, int radius) {
        Direction face = origin.getBlockFace(null);
        BlockPos targetPos = origin.getBlock();
        if (face == null) {
            return Arrays.asList(SpellTarget.NONE);
        } else {
            ArrayList<SpellTarget> targets = new ArrayList();
            if (face.getAxis() != Direction.Axis.X && face.getAxis() != Direction.Axis.Z) {
                for (int h = -radius; h <= radius; h++) {
                    for (int v = -radius; v <= radius; v++) {
                        targets.add(new SpellTarget(targetPos.offset(h, 0, v), face));
                    }
                }
            } else {
                for (int h = -radius; h <= radius; h++) {
                    for (int y = -1; y <= 2 * radius - 1; y++) {
                        if (face.getAxis() == Direction.Axis.X) {
                            targets.add(new SpellTarget(targetPos.offset(0, y, h), face));
                        } else {
                            targets.add(new SpellTarget(targetPos.offset(h, y, 0), face));
                        }
                    }
                }
            }
            return targets;
        }
    }

    private List<SpellTarget> targetBlocksEntity(SpellSource source, SpellTarget center, int radius, Level world) {
        return (List<SpellTarget>) world.getEntities(source.getCaster(), center.getEntity().getBoundingBox().inflate((double) radius), entity -> entity.isPickable() && entity.isAlive() && entity != source.getCaster()).stream().map(e -> new SpellTarget(e)).collect(Collectors.toList());
    }
}