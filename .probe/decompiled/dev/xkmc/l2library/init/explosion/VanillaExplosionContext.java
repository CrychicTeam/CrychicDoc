package dev.xkmc.l2library.init.explosion;

import javax.annotation.Nullable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

public record VanillaExplosionContext(@Nullable Entity entity, @Nullable DamageSource source, @Nullable ExplosionDamageCalculator calculator, boolean fire, Explosion.BlockInteraction type) {

    public VanillaExplosionContext(Level level, @Nullable Entity entity, @Nullable DamageSource source, @Nullable ExplosionDamageCalculator calculator, boolean fire, Level.ExplosionInteraction type) {
        this(entity, source, calculator, fire, getType(level, entity, type));
    }

    private static Explosion.BlockInteraction getType(Level level, @Nullable Entity entity, Level.ExplosionInteraction type) {
        return switch(type) {
            case NONE ->
                Explosion.BlockInteraction.KEEP;
            case BLOCK ->
                level.getDestroyType(GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY);
            case MOB ->
                ForgeEventFactory.getMobGriefingEvent(level, entity instanceof LivingEntity le ? le : null) ? level.getDestroyType(GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) : Explosion.BlockInteraction.KEEP;
            case TNT ->
                level.getDestroyType(GameRules.RULE_TNT_EXPLOSION_DROP_DECAY);
        };
    }
}