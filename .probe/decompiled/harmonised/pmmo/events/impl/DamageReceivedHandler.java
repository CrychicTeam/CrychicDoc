package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.CoreUtils;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.util.Functions;
import harmonised.pmmo.util.MsLoggy;
import harmonised.pmmo.util.RegistryUtil;
import harmonised.pmmo.util.TagUtils;
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
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class DamageReceivedHandler {

    public static void handle(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.equals(event.getSource().getEntity())) {
                return;
            }
            Core core = Core.get(player.m_9236_());
            String damageType = RegistryUtil.getId(event.getSource()).toString();
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.EVENT, "Source Type: " + damageType + " | Source Raw: " + event.getSource().getMsgId());
            boolean serverSide = !player.m_9236_().isClientSide;
            CompoundTag eventHookOutput = new CompoundTag();
            if (serverSide) {
                eventHookOutput = core.getEventTriggerRegistry().executeEventListeners(EventType.RECEIVE_DAMAGE, event, new CompoundTag());
                if (eventHookOutput.getBoolean("is_cancelled")) {
                    event.setCanceled(true);
                    return;
                }
            }
            CompoundTag perkDataIn = eventHookOutput.copy();
            perkDataIn.putString("damage_type", damageType);
            perkDataIn.putFloat("damageIn", event.getAmount());
            perkDataIn.putFloat("damage", event.getAmount());
            CompoundTag perkOutput = TagUtils.mergeTags(perkDataIn, core.getPerkRegistry().executePerk(EventType.RECEIVE_DAMAGE, player, perkDataIn));
            if (perkOutput.contains("damage")) {
                float damageOut = perkOutput.getFloat("damage");
                MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.EVENT, "Damage Modified from %s to %s".formatted(event.getAmount(), damageOut));
                event.setAmount(damageOut);
            }
            if (serverSide) {
                perkOutput.putString("damage_type", damageType);
                Map<String, Long> xpAward = getExperienceAwards(core, event.getSource(), event.getAmount(), player, perkOutput);
                List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) player);
                core.awardXP(partyMembersInRange, xpAward);
            }
        }
    }

    private static Map<String, Long> getExperienceAwards(Core core, DamageSource source, float damage, Player player, CompoundTag dataIn) {
        Map<String, Long> mapOut = new HashMap();
        float ultimateDamage = Mth.clamp(damage, 0.0F, player.m_21223_());
        if (source.getEntity() != null) {
            core.getExperienceAwards(EventType.RECEIVE_DAMAGE, source.getEntity(), player, dataIn).forEach((skill, value) -> mapOut.put(skill, (long) (value.floatValue() * ultimateDamage)));
        }
        Map<String, Map<String, Long>> config = Config.RECEIVE_DAMAGE_XP.get();
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