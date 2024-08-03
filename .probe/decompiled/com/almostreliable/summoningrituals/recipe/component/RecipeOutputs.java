package com.almostreliable.summoningrituals.recipe.component;

import com.almostreliable.summoningrituals.platform.Platform;
import com.almostreliable.summoningrituals.util.MathUtils;
import com.almostreliable.summoningrituals.util.SerializeUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import extensions.net.minecraft.world.entity.Entity.EntityExt;
import extensions.net.minecraft.world.entity.item.ItemEntity.ItemEntityExt;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.util.TriConsumer;

public final class RecipeOutputs {

    private static final Vec3i DEFAULT_OFFSET = new Vec3i(0, 2, 0);

    private static final Vec3i DEFAULT_SPREAD = new Vec3i(1, 0, 1);

    private static final Random RANDOM = new Random();

    private final NonNullList<RecipeOutputs.RecipeOutput<?>> outputs;

    private RecipeOutputs(NonNullList<RecipeOutputs.RecipeOutput<?>> outputs) {
        this.outputs = outputs;
    }

    public RecipeOutputs() {
        this(NonNullList.create());
    }

    public static RecipeOutputs fromJson(JsonArray json) {
        NonNullList<RecipeOutputs.RecipeOutput<?>> recipeOutputs = NonNullList.create();
        for (JsonElement output : json) {
            recipeOutputs.add(RecipeOutputs.RecipeOutput.fromJson(output.getAsJsonObject()));
        }
        return new RecipeOutputs(recipeOutputs);
    }

    public static RecipeOutputs fromNetwork(FriendlyByteBuf buffer) {
        int length = buffer.readVarInt();
        NonNullList<RecipeOutputs.RecipeOutput<?>> outputs = NonNullList.create();
        for (int i = 0; i < length; i++) {
            outputs.add(RecipeOutputs.RecipeOutput.fromNetwork(buffer));
        }
        return new RecipeOutputs(outputs);
    }

    public JsonArray toJson() {
        JsonArray json = new JsonArray();
        for (RecipeOutputs.RecipeOutput<?> output : this.outputs) {
            json.add(output.toJson());
        }
        return json;
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.outputs.size());
        for (RecipeOutputs.RecipeOutput<?> output : this.outputs) {
            output.toNetwork(buffer);
        }
    }

    public void add(RecipeOutputs.RecipeOutput<?> output) {
        this.outputs.add(output);
    }

    public void handleRecipe(ServerLevel level, BlockPos origin) {
        for (RecipeOutputs.RecipeOutput<?> output : this.outputs) {
            output.spawn(level, origin);
        }
    }

    public int size() {
        return this.outputs.size();
    }

    public void forEach(TriConsumer<RecipeOutputs.OutputType, RecipeOutputs.RecipeOutput<?>, Integer> consumer) {
        for (int i = 0; i < this.outputs.size(); i++) {
            RecipeOutputs.RecipeOutput<?> output = this.outputs.get(i);
            consumer.accept(output.type, output, i);
        }
    }

    private static final class ItemOutput extends RecipeOutputs.RecipeOutput<ItemStack> {

        private ItemOutput(ItemStack stack) {
            super(RecipeOutputs.OutputType.ITEM, stack);
        }

        private static RecipeOutputs.ItemOutput fromJson(JsonObject json) {
            ItemStack stack = Platform.itemStackFromJson(json);
            return new RecipeOutputs.ItemOutput(stack);
        }

        private static RecipeOutputs.ItemOutput fromNetwork(FriendlyByteBuf buffer) {
            return new RecipeOutputs.ItemOutput(buffer.readItem());
        }

        @Override
        JsonObject toJson() {
            JsonObject json = SerializeUtils.stackToJson(this.output);
            this.writeJsonDefaults(json);
            return json;
        }

        @Override
        void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeVarInt(0);
            buffer.writeItem(this.output);
            super.toNetwork(buffer);
        }

        @Override
        void spawn(ServerLevel level, BlockPos origin) {
            int toSpawn = this.getCount();
            ArrayList<ItemStack> stacks = new ArrayList();
            while (toSpawn > 0) {
                ItemStack stack = this.output.copyWithCount(Math.min(toSpawn, 4));
                stacks.add(stack);
                toSpawn -= stack.getCount();
            }
            for (ItemStack stack : stacks) {
                EntityExt.spawn(ItemEntityExt.of(level, stack), level, this.getRandomPos(origin), this::writeDataToEntity);
            }
        }

        @Override
        public int getCount() {
            return this.output.getCount();
        }
    }

    public static class ItemOutputBuilder extends RecipeOutputs.RecipeOutputBuilder {

        private ItemStack stack;

        public ItemOutputBuilder(ItemStack stack) {
            this.stack = stack;
        }

        public RecipeOutputs.ItemOutputBuilder item(ItemStack item) {
            this.stack = item;
            return this;
        }

        public RecipeOutputs.ItemOutput build() {
            RecipeOutputs.ItemOutput output = new RecipeOutputs.ItemOutput(this.stack);
            output.data = this.data;
            output.offset = this.offset;
            output.spread = this.spread;
            return output;
        }
    }

    private static final class MobOutput extends RecipeOutputs.RecipeOutput<EntityType<?>> {

        private final int mobCount;

        private MobOutput(EntityType<?> mob, int mobCount) {
            super(RecipeOutputs.OutputType.MOB, mob);
            this.mobCount = mobCount;
        }

        private static RecipeOutputs.MobOutput fromJson(JsonObject json) {
            EntityType<?> mob = Platform.mobFromJson(json);
            int count = GsonHelper.getAsInt(json, "count", 1);
            return new RecipeOutputs.MobOutput(mob, count);
        }

        private static RecipeOutputs.MobOutput fromNetwork(FriendlyByteBuf buffer) {
            EntityType<?> mob = SerializeUtils.mobFromNetwork(buffer);
            int count = buffer.readVarInt();
            return new RecipeOutputs.MobOutput(mob, count);
        }

        @Override
        JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("mob", Platform.getId(this.output).toString());
            if (this.mobCount > 1) {
                json.addProperty("count", this.mobCount);
            }
            this.writeJsonDefaults(json);
            return json;
        }

        @Override
        void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeVarInt(1);
            buffer.writeUtf(Platform.getId(this.output).toString());
            buffer.writeVarInt(this.mobCount);
            super.toNetwork(buffer);
        }

        @Override
        void spawn(ServerLevel level, BlockPos origin) {
            for (int i = 0; i < this.mobCount; i++) {
                Entity mobEntity = this.output.create(level);
                if (mobEntity == null) {
                    return;
                }
                EntityExt.spawn(mobEntity, level, this.getRandomPos(origin), this::writeDataToEntity);
            }
        }

        @Override
        public int getCount() {
            return this.mobCount;
        }
    }

    public static class MobOutputBuilder extends RecipeOutputs.RecipeOutputBuilder {

        private EntityType<?> mob;

        private int count;

        public MobOutputBuilder(EntityType<?> mob) {
            this.mob = mob;
            this.count = 1;
        }

        public RecipeOutputs.MobOutputBuilder mob(EntityType<?> mob) {
            this.mob = mob;
            return this;
        }

        public RecipeOutputs.MobOutputBuilder count(int count) {
            this.count = count;
            return this;
        }

        public RecipeOutputs.MobOutput build() {
            RecipeOutputs.MobOutput output = new RecipeOutputs.MobOutput(this.mob, this.count);
            output.data = this.data;
            output.offset = this.offset;
            output.spread = this.spread;
            return output;
        }
    }

    public static enum OutputType {

        ITEM, MOB
    }

    public abstract static class RecipeOutput<T> {

        private final RecipeOutputs.OutputType type;

        protected final T output;

        protected CompoundTag data;

        protected Vec3i offset = RecipeOutputs.DEFAULT_OFFSET;

        protected Vec3i spread = RecipeOutputs.DEFAULT_SPREAD;

        private RecipeOutput(RecipeOutputs.OutputType type, T output) {
            this.type = type;
            this.output = output;
            this.data = new CompoundTag();
        }

        private static RecipeOutputs.RecipeOutput<?> fromJson(JsonObject json) {
            RecipeOutputs.RecipeOutput<?> output;
            if (json.has("item")) {
                output = RecipeOutputs.ItemOutput.fromJson(json);
            } else {
                if (!json.has("mob")) {
                    throw new IllegalArgumentException("Invalid recipe output");
                }
                output = RecipeOutputs.MobOutput.fromJson(json);
            }
            if (json.has("data")) {
                output.data = SerializeUtils.nbtFromString(GsonHelper.getAsString(json, "data"));
            }
            if (json.has("offset")) {
                output.offset = SerializeUtils.vec3FromJson(json.getAsJsonObject("offset"));
            }
            if (json.has("spread")) {
                output.spread = SerializeUtils.vec3FromJson(json.getAsJsonObject("spread"));
            }
            return output;
        }

        private static RecipeOutputs.RecipeOutput<?> fromNetwork(FriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            RecipeOutputs.RecipeOutput<?> output;
            if (i == 0) {
                output = RecipeOutputs.ItemOutput.fromNetwork(buffer);
            } else {
                if (i != 1) {
                    throw new IllegalArgumentException("Invalid recipe output");
                }
                output = RecipeOutputs.MobOutput.fromNetwork(buffer);
            }
            if (buffer.readBoolean()) {
                output.data = buffer.readNbt();
            }
            output.offset = SerializeUtils.vec3FromNetwork(buffer);
            output.spread = SerializeUtils.vec3FromNetwork(buffer);
            return output;
        }

        abstract JsonObject toJson();

        void writeJsonDefaults(JsonObject json) {
            if (!this.data.isEmpty()) {
                json.addProperty("data", this.data.toString());
            }
            if (!this.offset.equals(RecipeOutputs.DEFAULT_OFFSET)) {
                json.add("offset", SerializeUtils.vec3ToJson(this.offset));
            }
            if (!this.spread.equals(RecipeOutputs.DEFAULT_SPREAD)) {
                json.add("spread", SerializeUtils.vec3ToJson(this.spread));
            }
        }

        void toNetwork(FriendlyByteBuf buffer) {
            if (this.data.isEmpty()) {
                buffer.writeBoolean(false);
            } else {
                buffer.writeBoolean(true);
                buffer.writeNbt(this.data);
            }
            SerializeUtils.vec3ToNetwork(buffer, this.offset);
            SerializeUtils.vec3ToNetwork(buffer, this.spread);
        }

        Entity writeDataToEntity(Entity entity) {
            if (this.data.isEmpty()) {
                return entity;
            } else {
                CompoundTag entityData = Platform.serializeEntity(entity);
                for (String prop : this.data.getAllKeys()) {
                    entityData.put(prop, (Tag) Objects.requireNonNull(this.data.get(prop)));
                }
                entity.load(entityData);
                return entity;
            }
        }

        Vec3 getRandomPos(BlockPos origin) {
            double x = this.spread.getX() > 0 ? RecipeOutputs.RANDOM.nextDouble((double) (-this.spread.getX()), (double) this.spread.getX()) / 2.0 : 0.0;
            double y = this.spread.getY() > 0 ? RecipeOutputs.RANDOM.nextDouble((double) (-this.spread.getY()), (double) this.spread.getY()) / 2.0 : 0.0;
            double z = this.spread.getZ() > 0 ? RecipeOutputs.RANDOM.nextDouble((double) (-this.spread.getZ()), (double) this.spread.getZ()) / 2.0 : 0.0;
            return MathUtils.shiftToCenter(origin).add(MathUtils.vectorFromPos(this.offset)).add(x, y, z);
        }

        abstract void spawn(ServerLevel var1, BlockPos var2);

        public T getOutput() {
            return this.output;
        }

        public abstract int getCount();

        public CompoundTag getData() {
            return this.data;
        }
    }

    private abstract static class RecipeOutputBuilder {

        CompoundTag data = new CompoundTag();

        Vec3i offset = new Vec3i(0, 2, 0);

        Vec3i spread = new Vec3i(1, 0, 1);

        public abstract RecipeOutputs.RecipeOutput<?> build();

        public RecipeOutputs.RecipeOutputBuilder data(CompoundTag data) {
            this.data = data;
            return this;
        }

        public RecipeOutputs.RecipeOutputBuilder offset(int x, int y, int z) {
            this.offset = new Vec3i(x, y, z);
            return this;
        }

        public RecipeOutputs.RecipeOutputBuilder spread(int x, int y, int z) {
            this.spread = new Vec3i(x, y, z);
            return this;
        }
    }
}