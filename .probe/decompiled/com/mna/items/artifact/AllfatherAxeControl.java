package com.mna.items.artifact;

import com.mna.api.entities.DamageHelper;
import com.mna.api.items.IShowHud;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.entities.boss.attacks.ThrownAllfatherAxe;
import com.mna.items.base.INoCreativeTab;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class AllfatherAxeControl extends Item implements IShowHud, INoCreativeTab {

    private static final int TELEPORT_MANA_COST = 125;

    private static final int RADIUS = 5;

    public AllfatherAxeControl() {
        super(new Item.Properties().fireResistant().setNoRepair().stacksTo(1).rarity(Rarity.EPIC));
    }

    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        return false;
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        recallAxe(context.getLevel(), stack);
        return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack held = pPlayer.m_21120_(pUsedHand);
        recallAxe(pLevel, held);
        return InteractionResultHolder.sidedSuccess(held, pLevel.isClientSide());
    }

    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        MutableBoolean consumed = new MutableBoolean();
        if (entity instanceof Player p) {
            if (!p.getCooldowns().isOnCooldown(this)) {
                p.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                    if (m.getCastingResource().hasEnough(entity, 125.0F)) {
                        m.getCastingResource().consume(entity, 125.0F);
                        consumed.setTrue();
                    }
                });
            }
        } else {
            consumed.setTrue();
        }
        if (consumed.getValue()) {
            ThrownAllfatherAxe axe = getAxe(entity.m_9236_(), stack);
            stack.shrink(1);
            if (axe != null) {
                if (entity.m_9236_().isClientSide()) {
                    spawnParticles(entity, axe);
                }
                BlockPos pos = BlockPos.containing(axe.m_20182_());
                if (entity.m_9236_().m_46859_(pos) && entity.m_9236_().m_46859_(pos.above())) {
                    DelayedEventQueue.pushEvent(entity.m_9236_(), new TimedDelayedEvent<>("damage_burst", 10, new Pair(entity, axe.m_20182_()), AllfatherAxeControl::teleportAndDamage));
                }
                axe.setReturning();
            }
        }
        return false;
    }

    private static void recallAxe(Level world, ItemStack stack) {
        ThrownAllfatherAxe axe = getAxe(world, stack);
        stack.shrink(1);
        if (axe != null) {
            axe.setReturning();
        }
    }

    private static ThrownAllfatherAxe getAxe(Level world, ItemStack stack) {
        if (stack.getItem() instanceof AllfatherAxeControl && stack.hasTag()) {
            int entityID = stack.getTag().getInt("axe_id");
            Entity e = world.getEntity(entityID);
            if (e instanceof ThrownAllfatherAxe) {
                return (ThrownAllfatherAxe) e;
            }
        }
        return null;
    }

    public static void setAxe(ItemStack stack, ThrownAllfatherAxe entity) {
        if (stack.getItem() instanceof AllfatherAxeControl) {
            stack.getOrCreateTag().putInt("axe_id", entity.m_19879_());
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void spawnParticles(LivingEntity entity, ThrownAllfatherAxe axe) {
        for (int i = 0; i < 50; i++) {
            Vec3 pos = entity.m_20182_().add(new Vec3(0.0, Math.random(), 0.0));
            entity.m_9236_().addParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setColor((float) Math.random() * 255.0F, (float) Math.random() * 255.0F, (float) Math.random() * 255.0F).setScale(0.08F).setMaxAge(50), pos.x, pos.y, pos.z, 0.6F * Math.random() + 0.1F, 0.2F, 0.5);
        }
        for (int i = 0; i < 50; i++) {
            Vec3 pos = axe.m_20182_().add(new Vec3(0.0, 1.0 + Math.random(), 0.0));
            entity.m_9236_().addParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setColor((float) Math.random() * 255.0F, (float) Math.random() * 255.0F, (float) Math.random() * 255.0F).setScale(0.08F).setMaxAge(50), pos.x, pos.y, pos.z, 0.6F * Math.random() + 0.1F, -0.2F, 0.5);
        }
        DelayedEventQueue.pushEvent(entity.m_9236_(), new TimedDelayedEvent<>("frost_burst", 10, new Pair(axe.m_9236_(), axe.m_20182_()), AllfatherAxeControl::frostBurst));
    }

    private static void frostBurst(String identifier, Pair<Level, Vec3> data) {
        MAParticleType particle = ParticleInit.FROST.get();
        int count = 180;
        double angleRads = 0.0;
        double step = (Math.PI * 2) / (double) count;
        for (int i = 0; i < count; i++) {
            angleRads += step;
            Vec3 dir = new Vec3(Math.cos(angleRads), Math.random() * 0.1, Math.sin(angleRads)).normalize();
            Vec3 vel = dir.scale(1.5);
            dir = dir.scale(0.2);
            ((Level) data.getFirst()).addParticle(new MAParticleType(particle).setScale(0.5F), ((Vec3) data.getSecond()).x() + dir.x, ((Vec3) data.getSecond()).y() + Math.random(), ((Vec3) data.getSecond()).z() + dir.z, vel.x, vel.y, vel.z);
        }
    }

    private static void teleportAndDamage(String identifier, Pair<LivingEntity, Vec3> data) {
        BlockPos soundPos = BlockPos.containing((Position) data.getSecond());
        ((LivingEntity) data.getFirst()).m_9236_().playSound(null, soundPos, SFX.Spell.Cast.ICE, SoundSource.PLAYERS, 1.0F, 1.0F);
        ((LivingEntity) data.getFirst()).m_9236_().playSound(null, soundPos, SFX.Spell.Impact.Single.ICE, SoundSource.PLAYERS, 1.0F, 1.0F);
        ((LivingEntity) data.getFirst()).m_9236_().m_45933_(null, new AABB(soundPos).inflate(5.0)).forEach(e -> {
            if (e instanceof LivingEntity) {
                ((LivingEntity) e).hurt(DamageHelper.createSourcedType(DamageHelper.FROST, e.level().registryAccess(), (Entity) data.getFirst()), 15.0F);
            }
        });
        ((LivingEntity) data.getFirst()).m_146884_((Vec3) data.getSecond());
        ((LivingEntity) data.getFirst()).addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 1, true, false));
    }
}