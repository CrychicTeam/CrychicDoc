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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ComponentActivate extends SpellEffect {

    public ComponentActivate(ResourceLocation guiIcon) {
        super(guiIcon);
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isBlock() && source.isPlayerCaster()) {
            BlockHitResult result = new BlockHitResult(target.getPosition(), target.getBlockFace(this), target.getBlock(), false);
            BlockState state = context.getServerLevel().m_8055_(target.getBlock());
            PlayerInteractEvent.RightClickBlock event = new PlayerInteractEvent.RightClickBlock(source.getPlayer(), source.getHand(), target.getBlock(), result);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return ComponentApplicationResult.FAIL;
            }
            if (state.m_60664_(context.getServerLevel(), source.getPlayer(), InteractionHand.MAIN_HAND, result) == InteractionResult.SUCCESS) {
                return ComponentApplicationResult.SUCCESS;
            }
        }
        return ComponentApplicationResult.FAIL;
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Buff.ARCANE;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ARCANE;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 1) {
            float particle_spread = 0.2F;
            float v = 0.1F;
            int particleCount = 10;
            for (int i = 0; i < particleCount; i++) {
                Vec3 position = new Vec3(impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0);
                Vec3 velocity = new Vec3(position.x - impact_position.x, position.y - impact_position.y, position.z - impact_position.z).normalize().scale((double) v);
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.SPARKLE_VELOCITY.get()), caster), position.x, position.y, position.z, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return true;
    }

    @Override
    public boolean targetsEntities() {
        return false;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }
}