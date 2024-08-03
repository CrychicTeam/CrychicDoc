package com.mna.effects.neutral;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.resource.CastingResourceIDs;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.interfaces.INoCreeperLingering;
import com.mna.factions.Factions;
import java.lang.reflect.Field;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class EffectColdDark extends MobEffect implements INoCreeperLingering {

    private static final Field SLEEP_TIMER = ObfuscationReflectionHelper.findField(Player.class, "f_36110_");

    public static final String PD_KEY = "coldDarkPos";

    public EffectColdDark() {
        super(MobEffectCategory.NEUTRAL, 0);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    private void trySetSleepTimer(Player player) {
        try {
            SLEEP_TIMER.setInt(player, 0);
        } catch (Exception var3) {
            ManaAndArtifice.LOGGER.error("Error setting player sleep timer");
        }
    }

    @Override
    public void addAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.addAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        if (entityLivingBaseIn instanceof Player && entityLivingBaseIn.getPersistentData().contains("coldDarkPos")) {
            long lpos = entityLivingBaseIn.getPersistentData().getLong("coldDarkPos");
            ((Player) entityLivingBaseIn).m_5802_(BlockPos.of(lpos));
            this.trySetSleepTimer((Player) entityLivingBaseIn);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.removeAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        if (entityLivingBaseIn instanceof Player && entityLivingBaseIn.getPersistentData().contains("coldDarkPos")) {
            ((Player) entityLivingBaseIn).getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                if (p.getAlliedFaction() == null) {
                    p.setAlliedFaction(Factions.UNDEAD, (Player) entityLivingBaseIn);
                    ((Player) entityLivingBaseIn).m_213846_(Component.translatable("event.mna.faction_ally_undead").withStyle(ChatFormatting.AQUA));
                }
                if (p.getAlliedFaction() == Factions.UNDEAD) {
                    p.setTier(p.getTier() + 1, (Player) entityLivingBaseIn);
                    ((Player) entityLivingBaseIn).m_213846_(Component.translatable("mna:progresscondition.advanced", p.getTier()).withStyle(ChatFormatting.AQUA));
                }
            });
            ((Player) entityLivingBaseIn).getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.setCastingResourceType(CastingResourceIDs.SOULS));
            entityLivingBaseIn.getPersistentData().remove("coldDarkPos");
            entityLivingBaseIn.stopSleeping();
        }
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn instanceof Player && entityLivingBaseIn.getPersistentData().contains("coldDarkPos")) {
            this.trySetSleepTimer((Player) entityLivingBaseIn);
        }
    }
}