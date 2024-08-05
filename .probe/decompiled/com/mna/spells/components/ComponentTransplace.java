package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.tools.TeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ComponentTransplace extends SpellEffect {

    public ComponentTransplace(ResourceLocation guiIcon) {
        super(guiIcon);
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (source.getCaster() == null) {
            return ComponentApplicationResult.FAIL;
        } else if (target.isLivingEntity() && source.getCaster().m_9236_().dimension().equals(target.getLivingEntity().m_9236_().dimension())) {
            Vec3 casterPos = source.getOrigin();
            Vec3 targetPos = target.getLivingEntity().m_20182_();
            TeleportHelper.teleportEntity(source.getCaster(), context.getServerLevel().m_46472_(), targetPos);
            TeleportHelper.teleportEntity(target.getLivingEntity(), context.getServerLevel().m_46472_(), casterPos);
            return ComponentApplicationResult.SUCCESS;
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ENDER;
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.ENDER;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age < 5) {
            Vec3 rotationOffset = new Vec3(1.5, 0.0, 0.0);
            BlockPos bp = BlockPos.containing(impact_position).offset(0, -1, 0);
            for (int angle = 0; angle < 360; angle += 30) {
                Vec3 point = rotationOffset.yRot((float) ((double) angle * Math.PI / 180.0));
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ENDER.get()), caster), (double) ((float) bp.m_123341_() + 0.5F) + point.x, (double) bp.m_123342_(), (double) ((float) bp.m_123343_() + 0.5F) + point.z, (double) ((float) bp.m_123341_() + 0.5F), (double) (bp.m_123342_() + 2), (double) ((float) bp.m_123343_() + 0.5F));
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.NEUTRAL;
    }
}