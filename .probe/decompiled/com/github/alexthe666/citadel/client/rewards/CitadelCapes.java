package com.github.alexthe666.citadel.client.rewards;

import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CitadelCapes {

    private static final List<CitadelCapes.Cape> CAPES = new ArrayList();

    private static final Map<UUID, Boolean> HAS_CAPES_ENABLED = new LinkedHashMap();

    public static void addCapeFor(List<UUID> uuids, String translationKey, ResourceLocation texture) {
        CAPES.add(new CitadelCapes.Cape(uuids, translationKey, texture));
    }

    public static List<CitadelCapes.Cape> getCapesFor(UUID uuid) {
        return CAPES.isEmpty() ? CAPES : CAPES.stream().filter(cape -> cape.isFor(uuid)).toList();
    }

    public static CitadelCapes.Cape getNextCape(String currentID, UUID playerUUID) {
        if (CAPES.isEmpty()) {
            return null;
        } else {
            int currentIndex = -1;
            for (int i = 0; i < CAPES.size(); i++) {
                if (((CitadelCapes.Cape) CAPES.get(i)).getIdentifier().equals(currentID)) {
                    currentIndex = i;
                    break;
                }
            }
            boolean flag = false;
            for (int ix = currentIndex + 1; ix < CAPES.size(); ix++) {
                if (((CitadelCapes.Cape) CAPES.get(ix)).isFor(playerUUID)) {
                    return (CitadelCapes.Cape) CAPES.get(ix);
                }
            }
            return null;
        }
    }

    @Nullable
    public static CitadelCapes.Cape getById(String identifier) {
        for (int i = 0; i < CAPES.size(); i++) {
            if (((CitadelCapes.Cape) CAPES.get(i)).getIdentifier().equals(identifier)) {
                return (CitadelCapes.Cape) CAPES.get(i);
            }
        }
        return null;
    }

    @Nullable
    private static CitadelCapes.Cape getFirstApplicable(Player player) {
        for (int i = 0; i < CAPES.size(); i++) {
            if (((CitadelCapes.Cape) CAPES.get(i)).isFor(player.m_20148_())) {
                return (CitadelCapes.Cape) CAPES.get(i);
            }
        }
        return null;
    }

    public static CitadelCapes.Cape getCurrentCape(Player player) {
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(player);
        if (tag.getBoolean("CitadelCapeDisabled")) {
            return null;
        } else if (tag.contains("CitadelCapeType")) {
            return tag.getString("CitadelCapeType").isEmpty() ? getFirstApplicable(player) : getById(tag.getString("CitadelCapeType"));
        } else {
            return null;
        }
    }

    public static class Cape {

        private List<UUID> isFor;

        private String identifier;

        private ResourceLocation texture;

        public Cape(List<UUID> isFor, String identifier, ResourceLocation texture) {
            this.isFor = isFor;
            this.identifier = identifier;
            this.texture = texture;
        }

        public List<UUID> getIsFor() {
            return this.isFor;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public ResourceLocation getTexture() {
            return this.texture;
        }

        public boolean isFor(UUID uuid) {
            return this.isFor.contains(uuid);
        }
    }
}