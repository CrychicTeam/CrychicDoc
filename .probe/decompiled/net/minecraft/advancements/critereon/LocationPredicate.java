package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.slf4j.Logger;

public class LocationPredicate {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final LocationPredicate ANY = new LocationPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, null, null, null, null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);

    private final MinMaxBounds.Doubles x;

    private final MinMaxBounds.Doubles y;

    private final MinMaxBounds.Doubles z;

    @Nullable
    private final ResourceKey<Biome> biome;

    @Nullable
    private final ResourceKey<Structure> structure;

    @Nullable
    private final ResourceKey<Level> dimension;

    @Nullable
    private final Boolean smokey;

    private final LightPredicate light;

    private final BlockPredicate block;

    private final FluidPredicate fluid;

    public LocationPredicate(MinMaxBounds.Doubles minMaxBoundsDoubles0, MinMaxBounds.Doubles minMaxBoundsDoubles1, MinMaxBounds.Doubles minMaxBoundsDoubles2, @Nullable ResourceKey<Biome> resourceKeyBiome3, @Nullable ResourceKey<Structure> resourceKeyStructure4, @Nullable ResourceKey<Level> resourceKeyLevel5, @Nullable Boolean boolean6, LightPredicate lightPredicate7, BlockPredicate blockPredicate8, FluidPredicate fluidPredicate9) {
        this.x = minMaxBoundsDoubles0;
        this.y = minMaxBoundsDoubles1;
        this.z = minMaxBoundsDoubles2;
        this.biome = resourceKeyBiome3;
        this.structure = resourceKeyStructure4;
        this.dimension = resourceKeyLevel5;
        this.smokey = boolean6;
        this.light = lightPredicate7;
        this.block = blockPredicate8;
        this.fluid = fluidPredicate9;
    }

    public static LocationPredicate inBiome(ResourceKey<Biome> resourceKeyBiome0) {
        return new LocationPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, resourceKeyBiome0, null, null, null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);
    }

    public static LocationPredicate inDimension(ResourceKey<Level> resourceKeyLevel0) {
        return new LocationPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, null, null, resourceKeyLevel0, null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);
    }

    public static LocationPredicate inStructure(ResourceKey<Structure> resourceKeyStructure0) {
        return new LocationPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, null, resourceKeyStructure0, null, null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);
    }

    public static LocationPredicate atYLocation(MinMaxBounds.Doubles minMaxBoundsDoubles0) {
        return new LocationPredicate(MinMaxBounds.Doubles.ANY, minMaxBoundsDoubles0, MinMaxBounds.Doubles.ANY, null, null, null, null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);
    }

    public boolean matches(ServerLevel serverLevel0, double double1, double double2, double double3) {
        if (!this.x.matches(double1)) {
            return false;
        } else if (!this.y.matches(double2)) {
            return false;
        } else if (!this.z.matches(double3)) {
            return false;
        } else if (this.dimension != null && this.dimension != serverLevel0.m_46472_()) {
            return false;
        } else {
            BlockPos $$4 = BlockPos.containing(double1, double2, double3);
            boolean $$5 = serverLevel0.m_46749_($$4);
            if (this.biome == null || $$5 && serverLevel0.m_204166_($$4).is(this.biome)) {
                if (this.structure == null || $$5 && serverLevel0.structureManager().getStructureWithPieceAt($$4, this.structure).isValid()) {
                    if (this.smokey == null || $$5 && this.smokey == CampfireBlock.isSmokeyPos(serverLevel0, $$4)) {
                        if (!this.light.matches(serverLevel0, $$4)) {
                            return false;
                        } else {
                            return !this.block.matches(serverLevel0, $$4) ? false : this.fluid.matches(serverLevel0, $$4);
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            if (!this.x.m_55327_() || !this.y.m_55327_() || !this.z.m_55327_()) {
                JsonObject $$1 = new JsonObject();
                $$1.add("x", this.x.m_55328_());
                $$1.add("y", this.y.m_55328_());
                $$1.add("z", this.z.m_55328_());
                $$0.add("position", $$1);
            }
            if (this.dimension != null) {
                Level.RESOURCE_KEY_CODEC.encodeStart(JsonOps.INSTANCE, this.dimension).resultOrPartial(LOGGER::error).ifPresent(p_52633_ -> $$0.add("dimension", p_52633_));
            }
            if (this.structure != null) {
                $$0.addProperty("structure", this.structure.location().toString());
            }
            if (this.biome != null) {
                $$0.addProperty("biome", this.biome.location().toString());
            }
            if (this.smokey != null) {
                $$0.addProperty("smokey", this.smokey);
            }
            $$0.add("light", this.light.serializeToJson());
            $$0.add("block", this.block.serializeToJson());
            $$0.add("fluid", this.fluid.serializeToJson());
            return $$0;
        }
    }

    public static LocationPredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "location");
            JsonObject $$2 = GsonHelper.getAsJsonObject($$1, "position", new JsonObject());
            MinMaxBounds.Doubles $$3 = MinMaxBounds.Doubles.fromJson($$2.get("x"));
            MinMaxBounds.Doubles $$4 = MinMaxBounds.Doubles.fromJson($$2.get("y"));
            MinMaxBounds.Doubles $$5 = MinMaxBounds.Doubles.fromJson($$2.get("z"));
            ResourceKey<Level> $$6 = $$1.has("dimension") ? (ResourceKey) ResourceLocation.CODEC.parse(JsonOps.INSTANCE, $$1.get("dimension")).resultOrPartial(LOGGER::error).map(p_52637_ -> ResourceKey.create(Registries.DIMENSION, p_52637_)).orElse(null) : null;
            ResourceKey<Structure> $$7 = $$1.has("structure") ? (ResourceKey) ResourceLocation.CODEC.parse(JsonOps.INSTANCE, $$1.get("structure")).resultOrPartial(LOGGER::error).map(p_207927_ -> ResourceKey.create(Registries.STRUCTURE, p_207927_)).orElse(null) : null;
            ResourceKey<Biome> $$8 = null;
            if ($$1.has("biome")) {
                ResourceLocation $$9 = new ResourceLocation(GsonHelper.getAsString($$1, "biome"));
                $$8 = ResourceKey.create(Registries.BIOME, $$9);
            }
            Boolean $$10 = $$1.has("smokey") ? $$1.get("smokey").getAsBoolean() : null;
            LightPredicate $$11 = LightPredicate.fromJson($$1.get("light"));
            BlockPredicate $$12 = BlockPredicate.fromJson($$1.get("block"));
            FluidPredicate $$13 = FluidPredicate.fromJson($$1.get("fluid"));
            return new LocationPredicate($$3, $$4, $$5, $$8, $$7, $$6, $$10, $$11, $$12, $$13);
        } else {
            return ANY;
        }
    }

    public static class Builder {

        private MinMaxBounds.Doubles x = MinMaxBounds.Doubles.ANY;

        private MinMaxBounds.Doubles y = MinMaxBounds.Doubles.ANY;

        private MinMaxBounds.Doubles z = MinMaxBounds.Doubles.ANY;

        @Nullable
        private ResourceKey<Biome> biome;

        @Nullable
        private ResourceKey<Structure> structure;

        @Nullable
        private ResourceKey<Level> dimension;

        @Nullable
        private Boolean smokey;

        private LightPredicate light = LightPredicate.ANY;

        private BlockPredicate block = BlockPredicate.ANY;

        private FluidPredicate fluid = FluidPredicate.ANY;

        public static LocationPredicate.Builder location() {
            return new LocationPredicate.Builder();
        }

        public LocationPredicate.Builder setX(MinMaxBounds.Doubles minMaxBoundsDoubles0) {
            this.x = minMaxBoundsDoubles0;
            return this;
        }

        public LocationPredicate.Builder setY(MinMaxBounds.Doubles minMaxBoundsDoubles0) {
            this.y = minMaxBoundsDoubles0;
            return this;
        }

        public LocationPredicate.Builder setZ(MinMaxBounds.Doubles minMaxBoundsDoubles0) {
            this.z = minMaxBoundsDoubles0;
            return this;
        }

        public LocationPredicate.Builder setBiome(@Nullable ResourceKey<Biome> resourceKeyBiome0) {
            this.biome = resourceKeyBiome0;
            return this;
        }

        public LocationPredicate.Builder setStructure(@Nullable ResourceKey<Structure> resourceKeyStructure0) {
            this.structure = resourceKeyStructure0;
            return this;
        }

        public LocationPredicate.Builder setDimension(@Nullable ResourceKey<Level> resourceKeyLevel0) {
            this.dimension = resourceKeyLevel0;
            return this;
        }

        public LocationPredicate.Builder setLight(LightPredicate lightPredicate0) {
            this.light = lightPredicate0;
            return this;
        }

        public LocationPredicate.Builder setBlock(BlockPredicate blockPredicate0) {
            this.block = blockPredicate0;
            return this;
        }

        public LocationPredicate.Builder setFluid(FluidPredicate fluidPredicate0) {
            this.fluid = fluidPredicate0;
            return this;
        }

        public LocationPredicate.Builder setSmokey(Boolean boolean0) {
            this.smokey = boolean0;
            return this;
        }

        public LocationPredicate build() {
            return new LocationPredicate(this.x, this.y, this.z, this.biome, this.structure, this.dimension, this.smokey, this.light, this.block, this.fluid);
        }
    }
}