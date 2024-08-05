package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.logging.LogUtils;
import java.util.Locale;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class ExplorationMapFunction extends LootItemConditionalFunction {

    static final Logger LOGGER = LogUtils.getLogger();

    public static final TagKey<Structure> DEFAULT_DESTINATION = StructureTags.ON_TREASURE_MAPS;

    public static final String DEFAULT_DECORATION_NAME = "mansion";

    public static final MapDecoration.Type DEFAULT_DECORATION = MapDecoration.Type.MANSION;

    public static final byte DEFAULT_ZOOM = 2;

    public static final int DEFAULT_SEARCH_RADIUS = 50;

    public static final boolean DEFAULT_SKIP_EXISTING = true;

    final TagKey<Structure> destination;

    final MapDecoration.Type mapDecoration;

    final byte zoom;

    final int searchRadius;

    final boolean skipKnownStructures;

    ExplorationMapFunction(LootItemCondition[] lootItemCondition0, TagKey<Structure> tagKeyStructure1, MapDecoration.Type mapDecorationType2, byte byte3, int int4, boolean boolean5) {
        super(lootItemCondition0);
        this.destination = tagKeyStructure1;
        this.mapDecoration = mapDecorationType2;
        this.zoom = byte3;
        this.searchRadius = int4;
        this.skipKnownStructures = boolean5;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.EXPLORATION_MAP;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.ORIGIN);
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        if (!itemStack0.is(Items.MAP)) {
            return itemStack0;
        } else {
            Vec3 $$2 = lootContext1.getParamOrNull(LootContextParams.ORIGIN);
            if ($$2 != null) {
                ServerLevel $$3 = lootContext1.getLevel();
                BlockPos $$4 = $$3.findNearestMapStructure(this.destination, BlockPos.containing($$2), this.searchRadius, this.skipKnownStructures);
                if ($$4 != null) {
                    ItemStack $$5 = MapItem.create($$3, $$4.m_123341_(), $$4.m_123343_(), this.zoom, true, true);
                    MapItem.renderBiomePreviewMap($$3, $$5);
                    MapItemSavedData.addTargetDecoration($$5, $$4, "+", this.mapDecoration);
                    return $$5;
                }
            }
            return itemStack0;
        }
    }

    public static ExplorationMapFunction.Builder makeExplorationMap() {
        return new ExplorationMapFunction.Builder();
    }

    public static class Builder extends LootItemConditionalFunction.Builder<ExplorationMapFunction.Builder> {

        private TagKey<Structure> destination = ExplorationMapFunction.DEFAULT_DESTINATION;

        private MapDecoration.Type mapDecoration = ExplorationMapFunction.DEFAULT_DECORATION;

        private byte zoom = 2;

        private int searchRadius = 50;

        private boolean skipKnownStructures = true;

        protected ExplorationMapFunction.Builder getThis() {
            return this;
        }

        public ExplorationMapFunction.Builder setDestination(TagKey<Structure> tagKeyStructure0) {
            this.destination = tagKeyStructure0;
            return this;
        }

        public ExplorationMapFunction.Builder setMapDecoration(MapDecoration.Type mapDecorationType0) {
            this.mapDecoration = mapDecorationType0;
            return this;
        }

        public ExplorationMapFunction.Builder setZoom(byte byte0) {
            this.zoom = byte0;
            return this;
        }

        public ExplorationMapFunction.Builder setSearchRadius(int int0) {
            this.searchRadius = int0;
            return this;
        }

        public ExplorationMapFunction.Builder setSkipKnownStructures(boolean boolean0) {
            this.skipKnownStructures = boolean0;
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new ExplorationMapFunction(this.m_80699_(), this.destination, this.mapDecoration, this.zoom, this.searchRadius, this.skipKnownStructures);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<ExplorationMapFunction> {

        public void serialize(JsonObject jsonObject0, ExplorationMapFunction explorationMapFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, explorationMapFunction1, jsonSerializationContext2);
            if (!explorationMapFunction1.destination.equals(ExplorationMapFunction.DEFAULT_DESTINATION)) {
                jsonObject0.addProperty("destination", explorationMapFunction1.destination.location().toString());
            }
            if (explorationMapFunction1.mapDecoration != ExplorationMapFunction.DEFAULT_DECORATION) {
                jsonObject0.add("decoration", jsonSerializationContext2.serialize(explorationMapFunction1.mapDecoration.toString().toLowerCase(Locale.ROOT)));
            }
            if (explorationMapFunction1.zoom != 2) {
                jsonObject0.addProperty("zoom", explorationMapFunction1.zoom);
            }
            if (explorationMapFunction1.searchRadius != 50) {
                jsonObject0.addProperty("search_radius", explorationMapFunction1.searchRadius);
            }
            if (!explorationMapFunction1.skipKnownStructures) {
                jsonObject0.addProperty("skip_existing_chunks", explorationMapFunction1.skipKnownStructures);
            }
        }

        public ExplorationMapFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            TagKey<Structure> $$3 = readStructure(jsonObject0);
            String $$4 = jsonObject0.has("decoration") ? GsonHelper.getAsString(jsonObject0, "decoration") : "mansion";
            MapDecoration.Type $$5 = ExplorationMapFunction.DEFAULT_DECORATION;
            try {
                $$5 = MapDecoration.Type.valueOf($$4.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException var10) {
                ExplorationMapFunction.LOGGER.error("Error while parsing loot table decoration entry. Found {}. Defaulting to {}", $$4, ExplorationMapFunction.DEFAULT_DECORATION);
            }
            byte $$7 = GsonHelper.getAsByte(jsonObject0, "zoom", (byte) 2);
            int $$8 = GsonHelper.getAsInt(jsonObject0, "search_radius", 50);
            boolean $$9 = GsonHelper.getAsBoolean(jsonObject0, "skip_existing_chunks", true);
            return new ExplorationMapFunction(lootItemCondition2, $$3, $$5, $$7, $$8, $$9);
        }

        private static TagKey<Structure> readStructure(JsonObject jsonObject0) {
            if (jsonObject0.has("destination")) {
                String $$1 = GsonHelper.getAsString(jsonObject0, "destination");
                return TagKey.create(Registries.STRUCTURE, new ResourceLocation($$1));
            } else {
                return ExplorationMapFunction.DEFAULT_DESTINATION;
            }
        }
    }
}