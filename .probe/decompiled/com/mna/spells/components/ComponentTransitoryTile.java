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
import com.mna.blocks.sorcery.TransitoryTileBlock;
import com.mna.tools.BlockUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayerFactory;

public class ComponentTransitoryTile extends SpellEffect {

    public ComponentTransitoryTile(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DURATION, 5.0F, 5.0F, 15.0F, 5.0F, 3.5F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        BlockPos blockTarget = null;
        if (target.isEntity() && source.isPlayerCaster() && target.getEntity() == source.getCaster()) {
            blockTarget = source.getPlayer().m_20183_().below(1);
        } else if (target.isBlock()) {
            blockTarget = target.getBlock().offset(target.getBlockFace(this).getNormal());
        }
        if (blockTarget == null) {
            return ComponentApplicationResult.FAIL;
        } else if (context.getServerLevel().m_46859_(blockTarget) && context.getServerLevel().m_6249_((Entity) null, new AABB(blockTarget), e -> e instanceof LivingEntity).size() == 0) {
            BlockState placeState = (BlockState) BlockInit.TRANSITORY_TILE.get().m_49966_().m_61124_(TransitoryTileBlock.DURATION, (int) modificationData.getValue(Attribute.DURATION));
            Player player = (Player) (source.isPlayerCaster() ? source.getPlayer() : FakePlayerFactory.getMinecraft(context.getServerLevel()));
            BlockUtils.placeBlock(context.getServerLevel(), blockTarget, target.getBlockFace(this), placeState, player);
            context.getServerLevel().m_186460_(blockTarget, BlockInit.TRANSITORY_TILE.get(), 20);
            this.setColor(player, source.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, context.getServerLevel(), blockTarget, context.getSpell().getParticleColorOverride());
            return ComponentApplicationResult.SUCCESS;
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    private void setColor(Player player, InteractionHand hand, Level world, BlockPos pos, int overrideColor) {
        if (overrideColor == -1) {
            ItemStack heldItem = player.m_21120_(hand);
            if (heldItem.getItem() instanceof DyeItem) {
                BlockInit.TRANSITORY_TILE.get().setColor(world, pos, ((DyeItem) heldItem.getItem()).getDyeColor().getFireworkColor());
            }
        } else {
            BlockInit.TRANSITORY_TILE.get().setColor(world, pos, overrideColor);
        }
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Buff.ENDER;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ENDER;
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
        return true;
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