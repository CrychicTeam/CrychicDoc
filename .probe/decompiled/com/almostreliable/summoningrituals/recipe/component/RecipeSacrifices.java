package com.almostreliable.summoningrituals.recipe.component;

import com.almostreliable.summoningrituals.platform.Platform;
import com.almostreliable.summoningrituals.util.SerializeUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;

public class RecipeSacrifices {

    private static final Vec3i DEFAULT_ZONE = new Vec3i(3, 2, 3);

    private final NonNullList<RecipeSacrifices.Sacrifice> sacrifices;

    private Vec3i region;

    public RecipeSacrifices() {
        this.sacrifices = NonNullList.create();
        this.region = DEFAULT_ZONE;
    }

    private RecipeSacrifices(NonNullList<RecipeSacrifices.Sacrifice> sacrifices, Vec3i region) {
        this.sacrifices = sacrifices;
        this.region = region;
    }

    public static RecipeSacrifices fromJson(JsonObject json) {
        JsonArray mobs = json.getAsJsonArray("mobs");
        NonNullList<RecipeSacrifices.Sacrifice> sacrifices = NonNullList.create();
        for (JsonElement entity : mobs) {
            sacrifices.add(RecipeSacrifices.Sacrifice.fromJson(entity.getAsJsonObject()));
        }
        Vec3i zone = json.has("region") ? SerializeUtils.vec3FromJson(json.getAsJsonObject("region")) : DEFAULT_ZONE;
        return new RecipeSacrifices(sacrifices, zone);
    }

    public static RecipeSacrifices fromNetwork(FriendlyByteBuf buffer) {
        int length = buffer.readVarInt();
        NonNullList<RecipeSacrifices.Sacrifice> sacrifices = NonNullList.create();
        for (int i = 0; i < length; i++) {
            sacrifices.add(RecipeSacrifices.Sacrifice.fromNetwork(buffer));
        }
        Vec3i zone = SerializeUtils.vec3FromNetwork(buffer);
        return new RecipeSacrifices(sacrifices, zone);
    }

    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        JsonArray mobs = new JsonArray();
        for (RecipeSacrifices.Sacrifice sacrifice : this.sacrifices) {
            mobs.add(sacrifice.toJson());
        }
        json.add("mobs", mobs);
        json.add("region", SerializeUtils.vec3ToJson(this.region));
        return json;
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.sacrifices.size());
        for (RecipeSacrifices.Sacrifice sacrifice : this.sacrifices) {
            sacrifice.toNetwork(buffer);
        }
        SerializeUtils.vec3ToNetwork(buffer, this.region);
    }

    public void add(EntityType<?> mob, int count) {
        this.sacrifices.add(new RecipeSacrifices.Sacrifice(mob, count));
    }

    public AABB getRegion(BlockPos pos) {
        return new AABB(pos.offset(this.region.multiply(-1)), pos.offset(this.region));
    }

    public boolean test(Predicate<? super RecipeSacrifices.Sacrifice> predicate) {
        return this.sacrifices.stream().allMatch(predicate);
    }

    public int size() {
        return this.sacrifices.size();
    }

    public RecipeSacrifices.Sacrifice get(int index) {
        return this.sacrifices.get(index);
    }

    public String getDisplayRegion() {
        return String.format("%dx%dx%d", this.region.getX(), this.region.getY(), this.region.getZ());
    }

    public boolean isEmpty() {
        return this.sacrifices.isEmpty();
    }

    public void setRegion(Vec3i region) {
        this.region = region;
    }

    public static record Sacrifice(EntityType<?> mob, int count) implements Predicate<Entity> {

        private static RecipeSacrifices.Sacrifice fromJson(JsonObject json) {
            EntityType<?> mob = Platform.mobFromJson(json);
            int count = GsonHelper.getAsInt(json, "count", 1);
            return new RecipeSacrifices.Sacrifice(mob, count);
        }

        private static RecipeSacrifices.Sacrifice fromNetwork(FriendlyByteBuf buffer) {
            EntityType<?> mob = SerializeUtils.mobFromNetwork(buffer);
            int count = buffer.readVarInt();
            return new RecipeSacrifices.Sacrifice(mob, count);
        }

        public boolean test(Entity entity) {
            return this.mob.equals(entity.getType());
        }

        private JsonElement toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("mob", Platform.getId(this.mob).toString());
            if (this.count > 1) {
                json.addProperty("count", this.count);
            }
            return json;
        }

        private void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeUtf(Platform.getId(this.mob).toString());
            buffer.writeVarInt(this.count);
        }
    }
}