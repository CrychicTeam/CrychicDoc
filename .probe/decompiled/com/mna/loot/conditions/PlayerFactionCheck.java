package com.mna.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.faction.IFaction;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class PlayerFactionCheck implements LootItemCondition {

    public static final LootItemConditionType PLAYER_FACTION = new LootItemConditionType(new PlayerFactionCheck.Serializer());

    private final ResourceLocation factionID;

    private PlayerFactionCheck(ResourceLocation factionID) {
        this.factionID = factionID;
    }

    @Override
    public LootItemConditionType getType() {
        return PLAYER_FACTION;
    }

    public boolean test(LootContext context) {
        Player lastPlayer = context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
        if (lastPlayer != null && this.factionID != null) {
            MutableBoolean factionMatch = new MutableBoolean(false);
            lastPlayer.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                if (p.getAlliedFaction() != null) {
                    IFaction faction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(this.factionID);
                    factionMatch.setValue(faction == p.getAlliedFaction());
                }
            });
            return factionMatch.booleanValue();
        } else {
            return false;
        }
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<PlayerFactionCheck> {

        public void serialize(JsonObject json, PlayerFactionCheck check, JsonSerializationContext context) {
            json.addProperty("faction", check.factionID.toString());
        }

        public PlayerFactionCheck deserialize(JsonObject p_230423_1_, JsonDeserializationContext p_230423_2_) {
            try {
                ResourceLocation factionID = new ResourceLocation(GsonHelper.getAsString(p_230423_1_, "faction"));
                return new PlayerFactionCheck(factionID);
            } catch (Exception var4) {
                ManaAndArtifice.LOGGER.catching(var4);
                return new PlayerFactionCheck(null);
            }
        }
    }
}