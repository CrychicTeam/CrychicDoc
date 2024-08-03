package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
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
import com.mna.blocks.sorcery.PulseBlock;
import com.mna.tools.BlockUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayerFactory;

public class ComponentRedstonePulse extends SpellEffect {

    public ComponentRedstonePulse(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DURATION, 5.0F, 1.0F, 30.0F, 1.0F, 0.5F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 5.0F, 1.0F, 15.0F, 1.0F, 0.5F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        BlockPos blockTarget = target.offsetFace() ? target.getBlock().offset(target.getBlockFace(this).getNormal()) : target.getBlock();
        if (context.getServerLevel().m_46859_(blockTarget) && context.getServerLevel().m_45933_(null, new AABB(blockTarget)).size() == 0) {
            int power = (int) modificationData.getValue(Attribute.LESSER_MAGNITUDE);
            int duration = (int) modificationData.getValue(Attribute.DURATION);
            BlockState placeState = (BlockState) BlockInit.REDSTONE_PULSE.get().m_49966_().m_61124_(PulseBlock.OUTPUT_POWER, power);
            Player player = (Player) (source.isPlayerCaster() ? source.getPlayer() : FakePlayerFactory.getMinecraft(context.getServerLevel()));
            BlockUtils.placeBlock(context.getServerLevel(), blockTarget, target.getBlockFace(this), placeState, player);
            context.getServerLevel().m_186460_(blockTarget, BlockInit.REDSTONE_PULSE.get(), duration * 20);
            return ComponentApplicationResult.SUCCESS;
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.LIGHTNING;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.LIGHTNING;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 10) {
            float particle_spread = 1.0F;
            float v = 0.4F;
            int particleCount = 10;
            for (int i = 0; i < particleCount; i++) {
                Vec3 velocity = new Vec3(0.0, Math.random() * (double) v, 0.0);
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ARCANE.get()), caster), impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public boolean targetsEntities() {
        return false;
    }

    @Override
    public Direction defaultBlockFace() {
        return Direction.DOWN;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ARCANE, Affinity.ENDER, Affinity.WIND, Affinity.ICE);
    }
}