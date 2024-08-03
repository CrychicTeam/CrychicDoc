package dev.xkmc.l2artifacts.content.effects.core;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2library.base.NamedEntry;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

public abstract class SetEffect extends NamedEntry<SetEffect> {

    public final int ids;

    public SetEffect(int ids) {
        super(ArtifactTypeRegistry.SET_EFFECT);
        this.ids = ids;
    }

    public List<MutableComponent> getDetailedDescription(int rank) {
        return List.of(Component.translatable(this.getDescriptionId() + ".desc"));
    }

    public void update(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
    }

    public void tick(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
    }

    public boolean playerAttackModifyEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, CriticalHitEvent event) {
        return false;
    }

    public final void playerAttackedEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache cache) {
        LivingAttackEvent event = cache.getLivingAttackEvent();
        assert event != null;
        DamageSource source = event.getSource();
        if (!event.isCanceled() && !source.is(DamageTypeTags.BYPASSES_EFFECTS) && this.playerAttackedCancel(player, ent, rank, source, cache)) {
            event.setCanceled(true);
        }
    }

    public final void playerHurtEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache cache) {
        LivingDamageEvent event = cache.getLivingDamageEvent();
        assert event != null;
        DamageSource source = event.getSource();
        if (!source.is(DamageTypeTags.BYPASSES_EFFECTS)) {
            this.playerReduceDamage(player, ent, rank, source, cache);
        }
    }

    public boolean playerAttackedCancel(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        return false;
    }

    public void playerReduceDamage(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
    }

    public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
    }

    public void playerDamageOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
    }

    public void playerKillOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
    }

    public void playerShieldBlock(LivingEntity player, ArtifactSetConfig.Entry entry, int i, ShieldBlockEvent event) {
    }
}