package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.util.GsonHelper;

public class BlockElementFace {

    public static final int NO_TINT = -1;

    public final Direction cullForDirection;

    public final int tintIndex;

    public final String texture;

    public final BlockFaceUV uv;

    public BlockElementFace(@Nullable Direction direction0, int int1, String string2, BlockFaceUV blockFaceUV3) {
        this.cullForDirection = direction0;
        this.tintIndex = int1;
        this.texture = string2;
        this.uv = blockFaceUV3;
    }

    protected static class Deserializer implements JsonDeserializer<BlockElementFace> {

        private static final int DEFAULT_TINT_INDEX = -1;

        public BlockElementFace deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            JsonObject $$3 = jsonElement0.getAsJsonObject();
            Direction $$4 = this.getCullFacing($$3);
            int $$5 = this.getTintIndex($$3);
            String $$6 = this.getTexture($$3);
            BlockFaceUV $$7 = (BlockFaceUV) jsonDeserializationContext2.deserialize($$3, BlockFaceUV.class);
            return new BlockElementFace($$4, $$5, $$6, $$7);
        }

        protected int getTintIndex(JsonObject jsonObject0) {
            return GsonHelper.getAsInt(jsonObject0, "tintindex", -1);
        }

        private String getTexture(JsonObject jsonObject0) {
            return GsonHelper.getAsString(jsonObject0, "texture");
        }

        @Nullable
        private Direction getCullFacing(JsonObject jsonObject0) {
            String $$1 = GsonHelper.getAsString(jsonObject0, "cullface", "");
            return Direction.byName($$1);
        }
    }
}