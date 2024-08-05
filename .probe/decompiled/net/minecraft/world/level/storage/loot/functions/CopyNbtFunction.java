package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;

public class CopyNbtFunction extends LootItemConditionalFunction {

    final NbtProvider source;

    final List<CopyNbtFunction.CopyOperation> operations;

    CopyNbtFunction(LootItemCondition[] lootItemCondition0, NbtProvider nbtProvider1, List<CopyNbtFunction.CopyOperation> listCopyNbtFunctionCopyOperation2) {
        super(lootItemCondition0);
        this.source = nbtProvider1;
        this.operations = ImmutableList.copyOf(listCopyNbtFunctionCopyOperation2);
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.COPY_NBT;
    }

    static NbtPathArgument.NbtPath compileNbtPath(String string0) {
        try {
            return new NbtPathArgument().parse(new StringReader(string0));
        } catch (CommandSyntaxException var2) {
            throw new IllegalArgumentException("Failed to parse path " + string0, var2);
        }
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.source.getReferencedContextParams();
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        Tag $$2 = this.source.get(lootContext1);
        if ($$2 != null) {
            this.operations.forEach(p_80255_ -> p_80255_.apply(itemStack0::m_41784_, $$2));
        }
        return itemStack0;
    }

    public static CopyNbtFunction.Builder copyData(NbtProvider nbtProvider0) {
        return new CopyNbtFunction.Builder(nbtProvider0);
    }

    public static CopyNbtFunction.Builder copyData(LootContext.EntityTarget lootContextEntityTarget0) {
        return new CopyNbtFunction.Builder(ContextNbtProvider.forContextEntity(lootContextEntityTarget0));
    }

    public static class Builder extends LootItemConditionalFunction.Builder<CopyNbtFunction.Builder> {

        private final NbtProvider source;

        private final List<CopyNbtFunction.CopyOperation> ops = Lists.newArrayList();

        Builder(NbtProvider nbtProvider0) {
            this.source = nbtProvider0;
        }

        public CopyNbtFunction.Builder copy(String string0, String string1, CopyNbtFunction.MergeStrategy copyNbtFunctionMergeStrategy2) {
            this.ops.add(new CopyNbtFunction.CopyOperation(string0, string1, copyNbtFunctionMergeStrategy2));
            return this;
        }

        public CopyNbtFunction.Builder copy(String string0, String string1) {
            return this.copy(string0, string1, CopyNbtFunction.MergeStrategy.REPLACE);
        }

        protected CopyNbtFunction.Builder getThis() {
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new CopyNbtFunction(this.m_80699_(), this.source, this.ops);
        }
    }

    static class CopyOperation {

        private final String sourcePathText;

        private final NbtPathArgument.NbtPath sourcePath;

        private final String targetPathText;

        private final NbtPathArgument.NbtPath targetPath;

        private final CopyNbtFunction.MergeStrategy op;

        CopyOperation(String string0, String string1, CopyNbtFunction.MergeStrategy copyNbtFunctionMergeStrategy2) {
            this.sourcePathText = string0;
            this.sourcePath = CopyNbtFunction.compileNbtPath(string0);
            this.targetPathText = string1;
            this.targetPath = CopyNbtFunction.compileNbtPath(string1);
            this.op = copyNbtFunctionMergeStrategy2;
        }

        public void apply(Supplier<Tag> supplierTag0, Tag tag1) {
            try {
                List<Tag> $$2 = this.sourcePath.get(tag1);
                if (!$$2.isEmpty()) {
                    this.op.merge((Tag) supplierTag0.get(), this.targetPath, $$2);
                }
            } catch (CommandSyntaxException var4) {
            }
        }

        public JsonObject toJson() {
            JsonObject $$0 = new JsonObject();
            $$0.addProperty("source", this.sourcePathText);
            $$0.addProperty("target", this.targetPathText);
            $$0.addProperty("op", this.op.name);
            return $$0;
        }

        public static CopyNbtFunction.CopyOperation fromJson(JsonObject jsonObject0) {
            String $$1 = GsonHelper.getAsString(jsonObject0, "source");
            String $$2 = GsonHelper.getAsString(jsonObject0, "target");
            CopyNbtFunction.MergeStrategy $$3 = CopyNbtFunction.MergeStrategy.getByName(GsonHelper.getAsString(jsonObject0, "op"));
            return new CopyNbtFunction.CopyOperation($$1, $$2, $$3);
        }
    }

    public static enum MergeStrategy {

        REPLACE("replace") {

            @Override
            public void merge(Tag p_80362_, NbtPathArgument.NbtPath p_80363_, List<Tag> p_80364_) throws CommandSyntaxException {
                p_80363_.set(p_80362_, (Tag) Iterables.getLast(p_80364_));
            }
        }
        , APPEND("append") {

            @Override
            public void merge(Tag p_80373_, NbtPathArgument.NbtPath p_80374_, List<Tag> p_80375_) throws CommandSyntaxException {
                List<Tag> $$3 = p_80374_.getOrCreate(p_80373_, ListTag::new);
                $$3.forEach(p_80371_ -> {
                    if (p_80371_ instanceof ListTag) {
                        p_80375_.forEach(p_165187_ -> ((ListTag) p_80371_).add(p_165187_.copy()));
                    }
                });
            }
        }
        , MERGE("merge") {

            @Override
            public void merge(Tag p_80387_, NbtPathArgument.NbtPath p_80388_, List<Tag> p_80389_) throws CommandSyntaxException {
                List<Tag> $$3 = p_80388_.getOrCreate(p_80387_, CompoundTag::new);
                $$3.forEach(p_80385_ -> {
                    if (p_80385_ instanceof CompoundTag) {
                        p_80389_.forEach(p_165190_ -> {
                            if (p_165190_ instanceof CompoundTag) {
                                ((CompoundTag) p_80385_).merge((CompoundTag) p_165190_);
                            }
                        });
                    }
                });
            }
        }
        ;

        final String name;

        public abstract void merge(Tag var1, NbtPathArgument.NbtPath var2, List<Tag> var3) throws CommandSyntaxException;

        MergeStrategy(String p_80341_) {
            this.name = p_80341_;
        }

        public static CopyNbtFunction.MergeStrategy getByName(String p_80350_) {
            for (CopyNbtFunction.MergeStrategy $$1 : values()) {
                if ($$1.name.equals(p_80350_)) {
                    return $$1;
                }
            }
            throw new IllegalArgumentException("Invalid merge strategy" + p_80350_);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<CopyNbtFunction> {

        public void serialize(JsonObject jsonObject0, CopyNbtFunction copyNbtFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, copyNbtFunction1, jsonSerializationContext2);
            jsonObject0.add("source", jsonSerializationContext2.serialize(copyNbtFunction1.source));
            JsonArray $$3 = new JsonArray();
            copyNbtFunction1.operations.stream().map(CopyNbtFunction.CopyOperation::m_80302_).forEach($$3::add);
            jsonObject0.add("ops", $$3);
        }

        public CopyNbtFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            NbtProvider $$3 = GsonHelper.getAsObject(jsonObject0, "source", jsonDeserializationContext1, NbtProvider.class);
            List<CopyNbtFunction.CopyOperation> $$4 = Lists.newArrayList();
            for (JsonElement $$6 : GsonHelper.getAsJsonArray(jsonObject0, "ops")) {
                JsonObject $$7 = GsonHelper.convertToJsonObject($$6, "op");
                $$4.add(CopyNbtFunction.CopyOperation.fromJson($$7));
            }
            return new CopyNbtFunction(lootItemCondition2, $$3, $$4);
        }
    }
}