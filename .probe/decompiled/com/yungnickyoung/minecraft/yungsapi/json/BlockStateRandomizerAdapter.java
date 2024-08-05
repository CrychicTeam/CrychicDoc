package com.yungnickyoung.minecraft.yungsapi.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.yungnickyoung.minecraft.yungsapi.api.world.randomize.BlockStateRandomizer;
import java.io.IOException;
import java.util.Map.Entry;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateRandomizerAdapter extends TypeAdapter<BlockStateRandomizer> {

    public BlockStateRandomizer read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        } else {
            BlockStateRandomizer randomizer = new BlockStateRandomizer();
            reader.beginObject();
            while (reader.hasNext()) {
                String var3 = reader.nextName();
                switch(var3) {
                    case "entries":
                        reader.beginObject();
                        while (reader.hasNext()) {
                            BlockState blockState = BlockStateAdapter.resolveBlockState(reader.nextName());
                            double probability = reader.nextDouble();
                            randomizer.addBlock(blockState, (float) probability);
                        }
                        reader.endObject();
                        break;
                    case "defaultBlock":
                        BlockState blockState = BlockStateAdapter.resolveBlockState(reader.nextString());
                        randomizer.setDefaultBlockState(blockState);
                }
            }
            reader.endObject();
            return randomizer;
        }
    }

    public void write(JsonWriter writer, BlockStateRandomizer randomizer) throws IOException {
        if (randomizer == null) {
            writer.nullValue();
        } else {
            writer.beginObject();
            writer.name("entries").beginObject();
            for (Entry<BlockState, Float> entry : randomizer.getEntriesAsMap().entrySet()) {
                writer.name(this.trimmedBlockName(String.valueOf(entry.getKey()))).value((Number) entry.getValue());
            }
            writer.endObject();
            String defaultBlockString = String.valueOf(randomizer.getDefaultBlockState());
            defaultBlockString = this.trimmedBlockName(defaultBlockString);
            writer.name("defaultBlock").value(defaultBlockString);
            writer.endObject();
        }
    }

    private String trimmedBlockName(String blockString) {
        if (blockString.startsWith("Block")) {
            blockString = blockString.substring(5);
        }
        blockString = blockString.replace("{", "");
        return blockString.replace("}", "");
    }
}