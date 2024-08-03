package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.magic.IMagicManager;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.network.ClientboundSyncCooldown;
import io.redspace.ironsspellbooks.network.ClientboundSyncMana;
import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class MagicManager implements IMagicManager {

    public static final int MANA_REGEN_TICKS = 10;

    public static final int CONTINUOUS_CAST_TICK_INTERVAL = 10;

    public boolean regenPlayerMana(ServerPlayer serverPlayer, MagicData playerMagicData) {
        int playerMaxMana = (int) serverPlayer.m_21133_(AttributeRegistry.MAX_MANA.get());
        float mana = playerMagicData.getMana();
        if (mana != (float) playerMaxMana) {
            float playerManaRegenMultiplier = (float) serverPlayer.m_21133_(AttributeRegistry.MANA_REGEN.get());
            float increment = (float) playerMaxMana * 0.01F * playerManaRegenMultiplier;
            playerMagicData.setMana(Mth.clamp(playerMagicData.getMana() + increment, 0.0F, (float) playerMaxMana));
            return true;
        } else {
            return false;
        }
    }

    public void tick(Level level) {
        boolean doManaRegen = level.getServer().getTickCount() % 10 == 0;
        level.m_6907_().stream().toList().forEach(player -> {
            if (player instanceof ServerPlayer serverPlayer) {
                MagicData playerMagicData = MagicData.getPlayerMagicData(serverPlayer);
                playerMagicData.getPlayerCooldowns().tick(1);
                playerMagicData.getPlayerRecasts().tick(2);
                if (playerMagicData.isCasting()) {
                    playerMagicData.handleCastDuration();
                    AbstractSpell spell = SpellRegistry.getSpell(playerMagicData.getCastingSpellId());
                    if ((spell.getCastType() != CastType.LONG || serverPlayer.m_6117_()) && spell.getCastType() != CastType.INSTANT) {
                        if (spell.getCastType() == CastType.CONTINUOUS && (playerMagicData.getCastDurationRemaining() + 1) % 10 == 0) {
                            if (playerMagicData.getCastDurationRemaining() < 10 || playerMagicData.getCastSource().consumesMana() && playerMagicData.getMana() - (float) (spell.getManaCost(playerMagicData.getCastingSpellLevel()) * 2) < 0.0F) {
                                spell.castSpell(serverPlayer.f_19853_, playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData.getCastSource(), true);
                                if (playerMagicData.getCastSource() == CastSource.SCROLL) {
                                    Scroll.attemptRemoveScrollAfterCast(serverPlayer);
                                }
                                spell.onServerCastComplete(serverPlayer.f_19853_, playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData, false);
                            } else {
                                spell.castSpell(serverPlayer.f_19853_, playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData.getCastSource(), false);
                            }
                        }
                    } else if (playerMagicData.getCastDurationRemaining() <= 0) {
                        spell.castSpell(serverPlayer.f_19853_, playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData.getCastSource(), true);
                        if (playerMagicData.getCastSource() == CastSource.SCROLL) {
                            Scroll.attemptRemoveScrollAfterCast(serverPlayer);
                        }
                        spell.onServerCastComplete(serverPlayer.f_19853_, playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData, false);
                    }
                    if (playerMagicData.isCasting()) {
                        spell.onServerCastTick(serverPlayer.f_19853_, playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData);
                    }
                }
                if (doManaRegen && this.regenPlayerMana(serverPlayer, playerMagicData)) {
                    Messages.sendToPlayer(new ClientboundSyncMana(playerMagicData), serverPlayer);
                }
            }
        });
    }

    @Override
    public void addCooldown(ServerPlayer serverPlayer, AbstractSpell spell, CastSource castSource) {
        if (castSource != CastSource.SCROLL) {
            int effectiveCooldown = getEffectiveSpellCooldown(spell, serverPlayer, castSource);
            MagicData.getPlayerMagicData(serverPlayer).getPlayerCooldowns().addCooldown(spell, effectiveCooldown);
            Messages.sendToPlayer(new ClientboundSyncCooldown(spell.getSpellId(), effectiveCooldown), serverPlayer);
        }
    }

    public void clearCooldowns(ServerPlayer serverPlayer) {
        MagicData.getPlayerMagicData(serverPlayer).getPlayerCooldowns().clearCooldowns();
        MagicData.getPlayerMagicData(serverPlayer).getPlayerCooldowns().syncToPlayer(serverPlayer);
    }

    public static int getEffectiveSpellCooldown(AbstractSpell spell, Player player, CastSource castSource) {
        double playerCooldownModifier = player.m_21133_(AttributeRegistry.COOLDOWN_REDUCTION.get());
        float itemCoolDownModifer = 1.0F;
        if (castSource == CastSource.SWORD) {
            itemCoolDownModifer = ServerConfigs.SWORDS_CD_MULTIPLIER.get().floatValue();
        }
        return (int) ((double) spell.getSpellCooldown() * (2.0 - Utils.softCapFormula(playerCooldownModifier)) * (double) itemCoolDownModifer);
    }

    public static void spawnParticles(Level level, ParticleOptions particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed, boolean force) {
        level.getServer().getPlayerList().getPlayers().forEach(player -> ((ServerLevel) level).sendParticles(player, particle, force, x, y, z, count, deltaX, deltaY, deltaZ, speed));
    }
}