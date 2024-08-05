package net.minecraft.data.models.blockstates;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class MultiPartGenerator implements BlockStateGenerator {

    private final Block block;

    private final List<MultiPartGenerator.Entry> parts = Lists.newArrayList();

    private MultiPartGenerator(Block block0) {
        this.block = block0;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    public static MultiPartGenerator multiPart(Block block0) {
        return new MultiPartGenerator(block0);
    }

    public MultiPartGenerator with(List<Variant> listVariant0) {
        this.parts.add(new MultiPartGenerator.Entry(listVariant0));
        return this;
    }

    public MultiPartGenerator with(Variant variant0) {
        return this.with(ImmutableList.of(variant0));
    }

    public MultiPartGenerator with(Condition condition0, List<Variant> listVariant1) {
        this.parts.add(new MultiPartGenerator.ConditionalEntry(condition0, listVariant1));
        return this;
    }

    public MultiPartGenerator with(Condition condition0, Variant... variant1) {
        return this.with(condition0, ImmutableList.copyOf(variant1));
    }

    public MultiPartGenerator with(Condition condition0, Variant variant1) {
        return this.with(condition0, ImmutableList.of(variant1));
    }

    public JsonElement get() {
        StateDefinition<Block, BlockState> $$0 = this.block.getStateDefinition();
        this.parts.forEach(p_125208_ -> p_125208_.validate($$0));
        JsonArray $$1 = new JsonArray();
        this.parts.stream().map(MultiPartGenerator.Entry::get).forEach($$1::add);
        JsonObject $$2 = new JsonObject();
        $$2.add("multipart", $$1);
        return $$2;
    }

    static class ConditionalEntry extends MultiPartGenerator.Entry {

        private final Condition condition;

        ConditionalEntry(Condition condition0, List<Variant> listVariant1) {
            super(listVariant1);
            this.condition = condition0;
        }

        @Override
        public void validate(StateDefinition<?, ?> stateDefinition0) {
            this.condition.validate(stateDefinition0);
        }

        @Override
        public void decorate(JsonObject jsonObject0) {
            jsonObject0.add("when", (JsonElement) this.condition.get());
        }
    }

    static class Entry implements Supplier<JsonElement> {

        private final List<Variant> variants;

        Entry(List<Variant> listVariant0) {
            this.variants = listVariant0;
        }

        public void validate(StateDefinition<?, ?> stateDefinition0) {
        }

        public void decorate(JsonObject jsonObject0) {
        }

        public JsonElement get() {
            JsonObject $$0 = new JsonObject();
            this.decorate($$0);
            $$0.add("apply", Variant.convertList(this.variants));
            return $$0;
        }
    }
}