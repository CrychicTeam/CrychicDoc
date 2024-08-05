package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.effects.EffectInit;
import com.mna.tools.math.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ComponentTelekinesis extends SpellEffect {

    public ComponentTelekinesis(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RANGE, 4.0F, 1.0F, 16.0F, 1.0F, 6.0F), new AttributeValuePair(Attribute.DURATION, 120.0F, 30.0F, 600.0F, 30.0F, 3.0F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 0.0F, 0.0F, 1.0F, 1.0F, 50.0F), new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 1.0F, 1.0F, 50.0F), new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 3.0F, 1.0F, 50.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (source.isPlayerCaster() && target.isLivingEntity() && target.getEntity() == source.getCaster()) {
            if (!context.getLevel().isClientSide()) {
                source.getCaster().addEffect(new MobEffectInstance(EffectInit.TELEKINESIS.get(), (int) (modificationData.getValue(Attribute.DURATION) * 20.0F), (int) modificationData.getValue(Attribute.RANGE), false, false, true));
            }
        } else {
            boolean autoPickup = source.isPlayerCaster() && modificationData.getValue(Attribute.LESSER_MAGNITUDE) == 1.0F;
            boolean isChanneled = context.getSpell().getShape().getPart().isChanneled();
            double movespeed = isChanneled ? 0.25 : 1.0;
            Vec3 targetPos = target.getPosition();
            if (target.isBlock()) {
                Vec3i pos = target.getBlockFace(this).getNormal();
                targetPos = targetPos.add(new Vec3((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()));
            }
            Vec3 finalPos = targetPos;
            context.getLevel().getEntities((Entity) null, new AABB(BlockPos.containing(finalPos)).inflate((double) modificationData.getValue(Attribute.RANGE)), e -> {
                SpellTarget predicateTarget = new SpellTarget(e);
                if (!this.targetClassPredicateMatches(e, (int) modificationData.getValue(Attribute.PRECISION), (int) modificationData.getValue(Attribute.MAGNITUDE), source, predicateTarget)) {
                    return false;
                } else {
                    ClipContext clipContext = new ClipContext(e.getEyePosition(), finalPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null);
                    return e.isAlive() && context.getLevel().m_45547_(clipContext).getType() == HitResult.Type.MISS;
                }
            }).stream().forEach(item -> {
                if (!(item instanceof ItemEntity) || !autoPickup || !source.getPlayer().addItem(((ItemEntity) item).getItem())) {
                    double dist = target.getPosition().distanceTo(item.position());
                    double distDecay = 1.0 - Math.pow(1.0 - MathUtils.clamp01(dist), 4.0);
                    Vec3 motion = target.getPosition().subtract(item.position()).normalize().scale(movespeed * distDecay);
                    item.hasImpulse = true;
                    item.setOnGround(false);
                    item.setDeltaMovement(motion);
                }
            });
        }
        return ComponentApplicationResult.SUCCESS;
    }

    private boolean targetClassPredicateMatches(Entity e, int precision, int magnitude, SpellSource source, SpellTarget target) {
        return precision == 1 ? e instanceof LivingEntity && ((LivingEntity) e).isAlive() && ((LivingEntity) e).canChangeDimensions() && this.magnitudeHealthCheck(source, target, magnitude, 20) : e instanceof ItemEntity && !((ItemEntity) e).hasPickUpDelay();
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ENDER;
    }

    @Override
    public float initialComplexity() {
        return 15.0F;
    }

    @Override
    public boolean applyAtChanneledEntityPos(boolean clientSide) {
        return true;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.NEUTRAL;
    }
}