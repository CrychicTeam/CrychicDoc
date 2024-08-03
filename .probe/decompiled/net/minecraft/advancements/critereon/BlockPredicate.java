package net.minecraft.advancements.critereon;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockPredicate {

    public static final BlockPredicate ANY = new BlockPredicate(null, null, StatePropertiesPredicate.ANY, NbtPredicate.ANY);

    @Nullable
    private final TagKey<Block> tag;

    @Nullable
    private final Set<Block> blocks;

    private final StatePropertiesPredicate properties;

    private final NbtPredicate nbt;

    public BlockPredicate(@Nullable TagKey<Block> tagKeyBlock0, @Nullable Set<Block> setBlock1, StatePropertiesPredicate statePropertiesPredicate2, NbtPredicate nbtPredicate3) {
        this.tag = tagKeyBlock0;
        this.blocks = setBlock1;
        this.properties = statePropertiesPredicate2;
        this.nbt = nbtPredicate3;
    }

    public boolean matches(ServerLevel serverLevel0, BlockPos blockPos1) {
        if (this == ANY) {
            return true;
        } else if (!serverLevel0.m_46749_(blockPos1)) {
            return false;
        } else {
            BlockState $$2 = serverLevel0.m_8055_(blockPos1);
            if (this.tag != null && !$$2.m_204336_(this.tag)) {
                return false;
            } else if (this.blocks != null && !this.blocks.contains($$2.m_60734_())) {
                return false;
            } else if (!this.properties.matches($$2)) {
                return false;
            } else {
                if (this.nbt != NbtPredicate.ANY) {
                    BlockEntity $$3 = serverLevel0.m_7702_(blockPos1);
                    if ($$3 == null || !this.nbt.matches($$3.saveWithFullMetadata())) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    public static BlockPredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "block");
            NbtPredicate $$2 = NbtPredicate.fromJson($$1.get("nbt"));
            Set<Block> $$3 = null;
            JsonArray $$4 = GsonHelper.getAsJsonArray($$1, "blocks", null);
            if ($$4 != null) {
                com.google.common.collect.ImmutableSet.Builder<Block> $$5 = ImmutableSet.builder();
                for (JsonElement $$6 : $$4) {
                    ResourceLocation $$7 = new ResourceLocation(GsonHelper.convertToString($$6, "block"));
                    $$5.add((Block) BuiltInRegistries.BLOCK.m_6612_($$7).orElseThrow(() -> new JsonSyntaxException("Unknown block id '" + $$7 + "'")));
                }
                $$3 = $$5.build();
            }
            TagKey<Block> $$8 = null;
            if ($$1.has("tag")) {
                ResourceLocation $$9 = new ResourceLocation(GsonHelper.getAsString($$1, "tag"));
                $$8 = TagKey.create(Registries.BLOCK, $$9);
            }
            StatePropertiesPredicate $$10 = StatePropertiesPredicate.fromJson($$1.get("state"));
            return new BlockPredicate($$8, $$3, $$10, $$2);
        } else {
            return ANY;
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            if (this.blocks != null) {
                JsonArray $$1 = new JsonArray();
                for (Block $$2 : this.blocks) {
                    $$1.add(BuiltInRegistries.BLOCK.getKey($$2).toString());
                }
                $$0.add("blocks", $$1);
            }
            if (this.tag != null) {
                $$0.addProperty("tag", this.tag.location().toString());
            }
            $$0.add("nbt", this.nbt.serializeToJson());
            $$0.add("state", this.properties.serializeToJson());
            return $$0;
        }
    }

    public static class Builder {

        @Nullable
        private Set<Block> blocks;

        @Nullable
        private TagKey<Block> tag;

        private StatePropertiesPredicate properties = StatePropertiesPredicate.ANY;

        private NbtPredicate nbt = NbtPredicate.ANY;

        private Builder() {
        }

        public static BlockPredicate.Builder block() {
            return new BlockPredicate.Builder();
        }

        public BlockPredicate.Builder of(Block... block0) {
            this.blocks = ImmutableSet.copyOf(block0);
            return this;
        }

        public BlockPredicate.Builder of(Iterable<Block> iterableBlock0) {
            this.blocks = ImmutableSet.copyOf(iterableBlock0);
            return this;
        }

        public BlockPredicate.Builder of(TagKey<Block> tagKeyBlock0) {
            this.tag = tagKeyBlock0;
            return this;
        }

        public BlockPredicate.Builder hasNbt(CompoundTag compoundTag0) {
            this.nbt = new NbtPredicate(compoundTag0);
            return this;
        }

        public BlockPredicate.Builder setProperties(StatePropertiesPredicate statePropertiesPredicate0) {
            this.properties = statePropertiesPredicate0;
            return this;
        }

        public BlockPredicate build() {
            return new BlockPredicate(this.tag, this.blocks, this.properties, this.nbt);
        }
    }
}