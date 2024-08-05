package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.CoreUtils;
import harmonised.pmmo.features.anticheese.CheeseTracker;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.features.penalties.EffectManager;
import harmonised.pmmo.features.veinmining.VeinMiningLogic;
import harmonised.pmmo.util.RegistryUtil;
import harmonised.pmmo.util.TagUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;

public class PlayerTickHandler {

    private static final Map<UUID, Integer> airLast = new HashMap();

    private static final Map<UUID, Float> healthLast = new HashMap();

    private static final Map<UUID, Vec3> moveLast = new HashMap();

    private static Map<Player, Short> ticksIgnoredSinceLastProcess = new HashMap();

    public static void handle(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            short current = (Short) ticksIgnoredSinceLastProcess.getOrDefault(event.player, (short) 0);
            ticksIgnoredSinceLastProcess.put(event.player, ++current);
            if (current >= 10) {
                Player player = event.player;
                Core core = Core.get(event.side);
                if (player instanceof ServerPlayer) {
                    VeinMiningLogic.regenerateVein((ServerPlayer) player);
                    EffectManager.applyEffects(core, player);
                }
                if (!airLast.containsKey(player.m_20148_())) {
                    airLast.put(player.m_20148_(), player.m_20146_());
                }
                if (!healthLast.containsKey(player.m_20148_())) {
                    healthLast.put(player.m_20148_(), player.m_21223_());
                }
                if (player.m_20146_() != (Integer) airLast.getOrDefault(player.m_20148_(), 0)) {
                    processEvent(EventType.BREATH_CHANGE, core, event);
                }
                float healthDiff = player.m_21223_() - (Float) healthLast.getOrDefault(player.m_20148_(), 0.0F);
                if ((double) Math.abs(healthDiff) >= 0.01) {
                    processEvent(EventType.HEALTH_CHANGE, core, event);
                    processEvent(healthDiff > 0.0F ? EventType.HEALTH_INCREASE : EventType.HEALTH_DECREASE, core, event);
                }
                if (player.m_20159_()) {
                    processEvent(EventType.RIDING, core, event);
                }
                if (!player.m_21220_().isEmpty()) {
                    processEvent(EventType.EFFECT, core, event);
                }
                if (player.m_5842_()) {
                    processEvent(EventType.SUBMERGED, core, event);
                    Vec3 vec = player.m_20184_();
                    if (player.m_20142_()) {
                        processEvent(EventType.SWIM_SPRINTING, core, event);
                    }
                    double sinkingRate = 0.01;
                    if (vec.y() > sinkingRate) {
                        processEvent(EventType.SURFACING, core, event);
                    } else if (vec.y() < -sinkingRate) {
                        processEvent(EventType.DIVING, core, event);
                    }
                } else if (player.m_20069_()) {
                    processEvent(EventType.SWIMMING, core, event);
                } else if (player.m_20142_()) {
                    processEvent(EventType.SPRINTING, core, event);
                } else if (player.m_6047_()) {
                    processEvent(EventType.CROUCH, core, event);
                }
                airLast.put(player.m_20148_(), player.m_20146_());
                healthLast.put(player.m_20148_(), player.m_21223_());
                moveLast.put(player.m_20148_(), player.m_20182_());
                ticksIgnoredSinceLastProcess.put(event.player, (short) 0);
            }
        }
    }

    private static void processEvent(EventType type, Core core, TickEvent.PlayerTickEvent event) {
        CompoundTag eventHookOutput = new CompoundTag();
        boolean serverSide = core.getSide().equals(LogicalSide.SERVER);
        if (serverSide) {
            eventHookOutput = core.getEventTriggerRegistry().executeEventListeners(type, event, new CompoundTag());
        }
        CompoundTag perkOutput = TagUtils.mergeTags(eventHookOutput, core.getPerkRegistry().executePerk(type, event.player, eventHookOutput));
        if (serverSide) {
            ResourceLocation source = new ResourceLocation("player");
            Map<String, Long> xpAward = (Map<String, Long>) (perkOutput.contains("serialized_award_map") ? CoreUtils.deserializeAwardMap(perkOutput.getCompound("serialized_award_map")) : new HashMap());
            switch(type) {
                case BREATH_CHANGE:
                    {
                        int diff = Math.abs((Integer) airLast.getOrDefault(event.player.m_20148_(), 0) - event.player.m_20146_());
                        Map<String, Double> ratio = Config.BREATH_CHANGE_XP.get();
                        ratio.keySet().forEach(skill -> {
                            Double value = (Double) ratio.getOrDefault(skill, 0.0) * (double) diff * (Double) core.getConsolidatedModifierMap(event.player).getOrDefault(skill, 1.0);
                            xpAward.put(skill, value.longValue());
                        });
                        break;
                    }
                case HEALTH_CHANGE:
                    processHealthChange(Config.HEALTH_CHANGE_XP.get(), core, event.player, xpAward);
                    break;
                case HEALTH_INCREASE:
                    processHealthChange(Config.HEALTH_INCREASE_XP.get(), core, event.player, xpAward);
                    break;
                case HEALTH_DECREASE:
                    processHealthChange(Config.HEALTH_DECREASE_XP.get(), core, event.player, xpAward);
                    break;
                case RIDING:
                    source = RegistryUtil.getId(event.player.m_20202_());
                    core.getExperienceAwards(type, event.player.m_20202_(), event.player, perkOutput).forEach((skill, value) -> xpAward.put(skill, value));
                    break;
                case EFFECT:
                    for (MobEffectInstance mei : event.player.m_21220_()) {
                        source = RegistryUtil.getId(mei.getEffect());
                        core.getExperienceAwards(mei, event.player, perkOutput).forEach((skill, value) -> xpAward.put(skill, value));
                    }
                    break;
                case SPRINTING:
                    {
                        Vec3 vec = event.player.m_20182_();
                        Vec3 old = (Vec3) moveLast.getOrDefault(event.player.m_20148_(), vec);
                        double magnitude = Math.sqrt(Math.pow(Math.abs(vec.x() - old.x()), 2.0) + Math.pow(Math.abs(vec.y() - old.y()), 2.0) + Math.pow(Math.abs(vec.z() - old.z()), 2.0));
                        Map<String, Double> ratio = Config.SPRINTING_XP.get();
                        ratio.keySet().forEach(skill -> {
                            Double value = (Double) ratio.getOrDefault(skill, 0.0) * magnitude * (Double) core.getConsolidatedModifierMap(event.player).getOrDefault(skill, 1.0);
                            xpAward.put(skill, value.longValue());
                        });
                        break;
                    }
                case SUBMERGED:
                    {
                        Map<String, Double> ratio = Config.SUBMERGED_XP.get();
                        ratio.keySet().forEach(skill -> xpAward.put(skill, ((Double) ratio.getOrDefault(skill, 0.0)).longValue()));
                        break;
                    }
                case SWIMMING:
                    {
                        Vec3 vec = event.player.m_20184_();
                        double magnitude = Math.sqrt(Math.pow(vec.x(), 2.0) + Math.pow(vec.y(), 2.0) + Math.pow(vec.z(), 2.0));
                        Map<String, Double> ratio = Config.SWIMMING_XP.get();
                        ratio.keySet().forEach(skill -> {
                            Double value = (Double) ratio.getOrDefault(skill, 0.0) * magnitude * (Double) core.getConsolidatedModifierMap(event.player).getOrDefault(skill, 1.0);
                            xpAward.put(skill, value.longValue());
                        });
                        break;
                    }
                case DIVING:
                    {
                        Vec3 vec = event.player.m_20184_();
                        double magnitude = Math.sqrt(Math.pow(vec.x(), 2.0) + Math.pow(vec.y(), 2.0) + Math.pow(vec.z(), 2.0));
                        Map<String, Double> ratio = Config.DIVING_XP.get();
                        ratio.keySet().forEach(skill -> {
                            Double value = (Double) ratio.getOrDefault(skill, 0.0) * magnitude * (Double) core.getConsolidatedModifierMap(event.player).getOrDefault(skill, 1.0);
                            xpAward.put(skill, value.longValue());
                        });
                        break;
                    }
                case SURFACING:
                    {
                        Vec3 vec = event.player.m_20184_();
                        double magnitude = Math.sqrt(Math.pow(vec.x(), 2.0) + Math.pow(vec.y(), 2.0) + Math.pow(vec.z(), 2.0));
                        Map<String, Double> ratio = Config.SURFACING_XP.get();
                        ratio.keySet().forEach(skill -> {
                            Double value = (Double) ratio.getOrDefault(skill, 0.0) * magnitude * (Double) core.getConsolidatedModifierMap(event.player).getOrDefault(skill, 1.0);
                            xpAward.put(skill, value.longValue());
                        });
                        break;
                    }
                case SWIM_SPRINTING:
                    {
                        Vec3 vec = event.player.m_20184_();
                        double magnitude = Math.sqrt(Math.pow(vec.x(), 2.0) + Math.pow(vec.y(), 2.0) + Math.pow(vec.z(), 2.0));
                        Map<String, Double> ratio = Config.SWIM_SPRINTING_XP.get();
                        ratio.keySet().forEach(skill -> {
                            Double value = (Double) ratio.getOrDefault(skill, 0.0) * magnitude * (Double) core.getConsolidatedModifierMap(event.player).getOrDefault(skill, 1.0);
                            xpAward.put(skill, value.longValue());
                        });
                    }
            }
            CheeseTracker.applyAntiCheese(type, source, event.player, xpAward);
            List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) event.player);
            core.awardXP(partyMembersInRange, xpAward);
        }
    }

    private static void processHealthChange(Map<String, Double> ratio, Core core, Player player, Map<String, Long> xpAward) {
        float diff = Math.abs((Float) healthLast.getOrDefault(player.m_20148_(), 0.0F) - player.m_21223_());
        ratio.keySet().forEach(skill -> {
            Double value = (Double) ratio.getOrDefault(skill, 0.0) * (double) diff * (Double) core.getConsolidatedModifierMap(player).getOrDefault(skill, 1.0);
            xpAward.put(skill, value.longValue());
        });
    }
}