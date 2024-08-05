package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
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
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import com.mna.tools.BlockUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayerFactory;

public class ComponentEarthquake extends PotionEffectComponent {

    public ComponentEarthquake(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.EARTHQUAKE, new AttributeValuePair(Attribute.DURATION, 1.0F, 1.0F, 10.0F, 1.0F, 4.0F), new AttributeValuePair(Attribute.DAMAGE, 10.0F, 10.0F, 30.0F, 5.0F, 15.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isBlock()) {
            BlockPos pos = target.getBlock();
            BlockEntity be = context.getServerLevel().m_7702_(pos);
            if (be == null) {
                BlockState state = context.getServerLevel().m_8055_(pos);
                if (state.m_60819_().isEmpty() && !context.getServerLevel().m_46859_(pos) && context.getServerLevel().m_46859_(pos.above()) && !(state.m_60734_() instanceof EntityBlock) && state.m_60734_() != Blocks.BEDROCK) {
                    Player player = (Player) (source.isPlayerCaster() ? source.getPlayer() : FakePlayerFactory.getMinecraft(context.getServerLevel()));
                    if (BlockUtils.canDestroyBlock(player, context.getServerLevel(), target.getBlock(), Tiers.DIAMOND) && context.getServerLevel().m_45933_(null, new AABB(pos.above())).size() == 0) {
                        FallingBlockEntity fbe = new FallingBlockEntity(EntityType.FALLING_BLOCK, context.getServerLevel());
                        fbe.f_19850_ = true;
                        fbe.m_6034_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5);
                        fbe.f_19854_ = (double) pos.m_123341_() + 0.5;
                        fbe.f_19855_ = (double) pos.m_123342_();
                        fbe.f_19856_ = (double) pos.m_123343_();
                        fbe.setStartPos(pos);
                        fbe.m_20256_(new Vec3(0.0, 0.25 + Math.random() * 0.75, 0.0));
                        fbe.blockState = state;
                        fbe.f_19812_ = true;
                        fbe.dropItem = false;
                        context.getServerLevel().addFreshEntity(fbe);
                        context.getServerLevel().m_7731_(pos, state.m_60819_().createLegacyBlock(), 3);
                        return ComponentApplicationResult.SUCCESS;
                    }
                }
            }
        } else if (target.isLivingEntity()) {
            target.getLivingEntity().hurt(target.getLivingEntity().m_269291_().generic(), modificationData.getValue(Attribute.DAMAGE));
        }
        return super.ApplyEffect(source, target, modificationData, context);
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.AoE.EARTH;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.EARTH;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 10) {
            float particle_spread = 1.0F;
            float v = 0.4F;
            int particleCount = 1;
            for (int i = 0; i < particleCount; i++) {
                Vec3 velocity = new Vec3(0.0, Math.random() * (double) v, 0.0);
                MAParticleType mpt = recipe.colorParticle(new MAParticleType(ParticleInit.DUST.get()), caster);
                world.addParticle(mpt, impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return true;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ARCANE, Affinity.ENDER, Affinity.WATER, Affinity.WIND, Affinity.ICE, Affinity.LIGHTNING, Affinity.EARTH);
    }

    @Override
    public boolean isSilverSpell() {
        return true;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.COUNCIL;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }
}