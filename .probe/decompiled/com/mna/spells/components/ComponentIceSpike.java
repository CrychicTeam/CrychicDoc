package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.entities.DamageHelper;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.blocks.BlockInit;
import com.mna.config.GeneralConfig;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ComponentIceSpike extends SpellEffect {

    private static final Predicate<? super Entity> TARGET_PREDICATE = e -> e instanceof LivingEntity && e.isAlive();

    public ComponentIceSpike(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DAMAGE, 10.0F, 1.0F, 20.0F, 1.0F, 3.0F), new AttributeValuePair(Attribute.DURATION, 10.0F, 5.0F, 60.0F, 5.0F, 3.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        BlockPos targetPos = target.getBlock();
        Direction targetFace = target.getBlockFace(this);
        if (target.isEntity()) {
            targetPos = target.getEntity().blockPosition().below();
            targetFace = Direction.UP;
        }
        SpellEffect.BlockPlaceResult bpr = this.tryPlaceBlock(source.getPlayer(), context.getServerLevel(), BlockInit.ICE_SPIKE.get(), targetPos, targetFace, true, null);
        if (bpr.success) {
            context.getServerLevel().m_6249_(source.getCaster(), new AABB(bpr.position), TARGET_PREDICATE).stream().map(e -> (LivingEntity) e).forEach(e -> e.hurt(DamageHelper.createSourcedType(DamageHelper.FROST, context.getLevel().registryAccess(), source.getCaster()), modificationData.getValue(Attribute.DAMAGE) * GeneralConfig.getDamageMultiplier()));
            int duration = (int) modificationData.getValue(Attribute.DURATION);
            context.getServerLevel().m_186460_(bpr.position, BlockInit.ICE_SPIKE.get(), duration * 20 + (int) (Math.random() * 20.0));
            return ComponentApplicationResult.SUCCESS;
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 2) {
            normal = normal.normalize().scale(0.2);
            for (int i = 0; i < 10; i++) {
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.FROST.get()), caster).setGravity(0.01F).setScale(0.1F), impact_position.x - 0.5 + Math.random(), impact_position.y - 0.5 + Math.random(), impact_position.z - 0.5 + Math.random(), normal.x, normal.y, normal.z);
            }
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ICE;
    }

    @Override
    public float initialComplexity() {
        return 15.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }
}