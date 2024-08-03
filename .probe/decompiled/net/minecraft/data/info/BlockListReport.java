package net.minecraft.data.info;

import com.google.common.collect.UnmodifiableIterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockListReport implements DataProvider {

    private final PackOutput output;

    public BlockListReport(PackOutput packOutput0) {
        this.output = packOutput0;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput0) {
        JsonObject $$1 = new JsonObject();
        for (Block $$2 : BuiltInRegistries.BLOCK) {
            ResourceLocation $$3 = BuiltInRegistries.BLOCK.getKey($$2);
            JsonObject $$4 = new JsonObject();
            StateDefinition<Block, BlockState> $$5 = $$2.getStateDefinition();
            if (!$$5.getProperties().isEmpty()) {
                JsonObject $$6 = new JsonObject();
                for (Property<?> $$7 : $$5.getProperties()) {
                    JsonArray $$8 = new JsonArray();
                    for (Comparable<?> $$9 : $$7.getPossibleValues()) {
                        $$8.add(Util.getPropertyName($$7, $$9));
                    }
                    $$6.add($$7.getName(), $$8);
                }
                $$4.add("properties", $$6);
            }
            JsonArray $$10 = new JsonArray();
            UnmodifiableIterator var17 = $$5.getPossibleStates().iterator();
            while (var17.hasNext()) {
                BlockState $$11 = (BlockState) var17.next();
                JsonObject $$12 = new JsonObject();
                JsonObject $$13 = new JsonObject();
                for (Property<?> $$14 : $$5.getProperties()) {
                    $$13.addProperty($$14.getName(), Util.getPropertyName($$14, $$11.m_61143_($$14)));
                }
                if ($$13.size() > 0) {
                    $$12.add("properties", $$13);
                }
                $$12.addProperty("id", Block.getId($$11));
                if ($$11 == $$2.defaultBlockState()) {
                    $$12.addProperty("default", true);
                }
                $$10.add($$12);
            }
            $$4.add("states", $$10);
            $$1.add($$3.toString(), $$4);
        }
        Path $$15 = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("blocks.json");
        return DataProvider.saveStable(cachedOutput0, $$1, $$15);
    }

    @Override
    public final String getName() {
        return "Block List";
    }
}