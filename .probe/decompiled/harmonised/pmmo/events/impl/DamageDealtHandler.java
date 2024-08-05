package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.CoreUtils;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.util.Functions;
import harmonised.pmmo.util.Messenger;
import harmonised.pmmo.util.MsLoggy;
import harmonised.pmmo.util.Reference;
import harmonised.pmmo.util.RegistryUtil;
import harmonised.pmmo.util.TagBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class DamageDealtHandler {

    public static void handle(LivingAttackEvent event) {
        if (event.getSource().getEntity() != null) {
            if (event.getSource().getEntity() instanceof Player player) {
                LivingEntity target = event.getEntity();
                if (target == null) {
                    return;
                }
                if (target.equals(player)) {
                    return;
                }
                Core core = Core.get(player.m_9236_());
                MsLoggy.INFO.log(MsLoggy.LOG_CODE.EVENT, "Attack Type: " + EventType.DEAL_DAMAGE.name() + " | TargetType: " + target.m_6095_().toString());
                if (!core.isActionPermitted(ReqType.WEAPON, player.m_21205_(), player)) {
                    event.setCanceled(true);
                    Messenger.sendDenialMsg(ReqType.WEAPON, player, player.m_21205_().getDisplayName());
                    return;
                }
                if (!core.isActionPermitted(ReqType.KILL, target, player)) {
                    event.setCanceled(true);
                    Messenger.sendDenialMsg(ReqType.KILL, player, target.m_5446_());
                    return;
                }
                boolean serverSide = !player.m_9236_().isClientSide;
                new CompoundTag();
                if (serverSide) {
                    CompoundTag eventHookOutput = core.getEventTriggerRegistry().executeEventListeners(EventType.DEAL_DAMAGE, event, new CompoundTag());
                    if (eventHookOutput.getBoolean("is_cancelled")) {
                        event.setCanceled(true);
                        return;
                    }
                }
            }
        }
    }

    public static void handle(LivingDamageEvent event) {
        if (event.getSource().getEntity() != null) {
            if (event.getSource().getEntity() instanceof Player player) {
                LivingEntity target = event.getEntity();
                if (target == null) {
                    return;
                }
                if (target.equals(player)) {
                    return;
                }
                Core core = Core.get(player.m_9236_());
                String damageType = RegistryUtil.getId(event.getSource()).toString();
                MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.EVENT, "Source Type: " + damageType + " | Source Raw: " + event.getSource().getMsgId());
                CompoundTag dataIn = TagBuilder.start().withFloat("damageIn", event.getAmount()).withFloat("damage", event.getAmount()).withString("damage_type", RegistryUtil.getId(event.getSource()).toString()).build();
                CompoundTag perkOutput = core.getPerkRegistry().executePerk(EventType.DEAL_DAMAGE, player, dataIn);
                MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.EVENT, "Pre-Perk Damage:" + event.getAmount());
                if (perkOutput.contains("damage")) {
                    float damageOut = perkOutput.getFloat("damage");
                    MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.EVENT, "Damage Modified from %s to %s".formatted(event.getAmount(), damageOut));
                    event.setAmount(damageOut);
                }
                MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.EVENT, "Attack Type: " + damageType + " | Damage Out: " + event.getAmount());
                if (!player.m_9236_().isClientSide) {
                    perkOutput.putString("damage_type", damageType);
                    Map<String, Long> xpAward = getExperienceAwards(core, target, event.getAmount(), event.getSource(), player, perkOutput);
                    List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) player);
                    core.awardXP(partyMembersInRange, xpAward);
                }
            }
        }
    }

    private static Map<String, Long> getExperienceAwards(Core core, LivingEntity target, float damage, DamageSource source, Player player, CompoundTag dataIn) {
        Map<String, Long> mapOut = new HashMap();
        if (target.m_6095_().is(Reference.NO_XP_DAMAGE_DEALT)) {
            return mapOut;
        } else {
            float ultimateDamage = Math.min(damage, target.getHealth());
            ItemStack weapon = player.m_21205_();
            Entity srcEntity = (Entity) (source.getDirectEntity() != null ? source.getDirectEntity() : player);
            Functions.mergeMaps(core.getExperienceAwards(EventType.DEAL_DAMAGE, weapon, player, dataIn), core.getExperienceAwards(EventType.DEAL_DAMAGE, srcEntity, player, dataIn), core.getExperienceAwards(EventType.DEAL_DAMAGE, target, player, dataIn)).forEach((skill, xp) -> mapOut.put(skill, (long) (xp.floatValue() * ultimateDamage)));
            Map<String, Map<String, Long>> config = Config.DEAL_DAMAGE_XP.get();
            List<String> tags = config.keySet().stream().filter(str -> {
                if (!str.contains("#")) {
                    return false;
                } else {
                    Registry<DamageType> registry = player.m_9236_().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
                    Optional<HolderSet.Named<DamageType>> tag = registry.getTag(TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(str.substring(1))));
                    return (Boolean) tag.map(type -> type.contains(source.typeHolder())).orElse(false);
                }
            }).toList();
            Map<String, Long> tagXp = (Map<String, Long>) tags.stream().map(str -> (Map) config.get(str)).reduce((mapA, mapB) -> Functions.mergeMaps(mapA, mapB)).orElse(new HashMap());
            Functions.mergeMaps((Map) config.getOrDefault(RegistryUtil.getId(source).toString(), new HashMap()), tagXp).forEach((skill, xp) -> mapOut.putIfAbsent(skill, (long) (xp.floatValue() * ultimateDamage)));
            CoreUtils.applyXpModifiers(mapOut, core.getConsolidatedModifierMap(player));
            return mapOut;
        }
    }
}