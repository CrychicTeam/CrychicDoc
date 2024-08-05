package dev.xkmc.l2artifacts.content.effects.core;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

public class PlayerOnlySetEffect extends SetEffect {

    public PlayerOnlySetEffect(int ids) {
        super(ids);
    }

    @Override
    public final void update(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (le instanceof Player pl) {
            this.update(pl, ent, rank, enabled);
        }
    }

    @Override
    public final void tick(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (le instanceof Player pl) {
            this.tick(pl, ent, rank, enabled);
        }
    }

    @Override
    public final boolean playerAttackModifyEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, CriticalHitEvent event) {
        return le instanceof Player pl ? this.playerAttackModifyEvent(pl, ent, rank, event) : false;
    }

    @Override
    public final void playerHurtOpponentEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        if (le instanceof Player pl) {
            this.playerHurtOpponentEvent(pl, ent, rank, event);
        }
    }

    @Override
    public final void playerDamageOpponentEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        if (le instanceof Player pl) {
            this.playerDamageOpponentEvent(pl, ent, rank, event);
        }
    }

    @Override
    public final void playerKillOpponentEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
        if (le instanceof Player pl) {
            this.playerKillOpponentEvent(pl, ent, rank, event);
        }
    }

    @Override
    public final void playerShieldBlock(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, ShieldBlockEvent event) {
        if (le instanceof Player pl) {
            this.playerShieldBlock(pl, ent, rank, event);
        }
    }

    @Override
    public final boolean playerAttackedCancel(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        return le instanceof Player pl ? this.playerAttackedCancel(pl, ent, rank, source, cache) : false;
    }

    @Override
    public final void playerReduceDamage(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        if (le instanceof Player pl) {
            this.playerReduceDamage(pl, ent, rank, source, cache);
        }
    }

    public void update(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
    }

    public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
    }

    public boolean playerAttackModifyEvent(Player player, ArtifactSetConfig.Entry ent, int rank, CriticalHitEvent event) {
        return false;
    }

    public boolean playerAttackedCancel(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        return false;
    }

    public void playerReduceDamage(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
    }

    public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
    }

    public void playerDamageOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
    }

    public void playerKillOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
    }

    public void playerShieldBlock(Player player, ArtifactSetConfig.Entry entry, int i, ShieldBlockEvent event) {
    }
}