package net.minecraft.commands.synchronization.brigadier;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentUtils;
import net.minecraft.network.FriendlyByteBuf;

public class FloatArgumentInfo implements ArgumentTypeInfo<FloatArgumentType, FloatArgumentInfo.Template> {

    public void serializeToNetwork(FloatArgumentInfo.Template floatArgumentInfoTemplate0, FriendlyByteBuf friendlyByteBuf1) {
        boolean $$2 = floatArgumentInfoTemplate0.min != -Float.MAX_VALUE;
        boolean $$3 = floatArgumentInfoTemplate0.max != Float.MAX_VALUE;
        friendlyByteBuf1.writeByte(ArgumentUtils.createNumberFlags($$2, $$3));
        if ($$2) {
            friendlyByteBuf1.writeFloat(floatArgumentInfoTemplate0.min);
        }
        if ($$3) {
            friendlyByteBuf1.writeFloat(floatArgumentInfoTemplate0.max);
        }
    }

    public FloatArgumentInfo.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf0) {
        byte $$1 = friendlyByteBuf0.readByte();
        float $$2 = ArgumentUtils.numberHasMin($$1) ? friendlyByteBuf0.readFloat() : -Float.MAX_VALUE;
        float $$3 = ArgumentUtils.numberHasMax($$1) ? friendlyByteBuf0.readFloat() : Float.MAX_VALUE;
        return new FloatArgumentInfo.Template($$2, $$3);
    }

    public void serializeToJson(FloatArgumentInfo.Template floatArgumentInfoTemplate0, JsonObject jsonObject1) {
        if (floatArgumentInfoTemplate0.min != -Float.MAX_VALUE) {
            jsonObject1.addProperty("min", floatArgumentInfoTemplate0.min);
        }
        if (floatArgumentInfoTemplate0.max != Float.MAX_VALUE) {
            jsonObject1.addProperty("max", floatArgumentInfoTemplate0.max);
        }
    }

    public FloatArgumentInfo.Template unpack(FloatArgumentType floatArgumentType0) {
        return new FloatArgumentInfo.Template(floatArgumentType0.getMinimum(), floatArgumentType0.getMaximum());
    }

    public final class Template implements ArgumentTypeInfo.Template<FloatArgumentType> {

        final float min;

        final float max;

        Template(float float0, float float1) {
            this.min = float0;
            this.max = float1;
        }

        public FloatArgumentType instantiate(CommandBuildContext commandBuildContext0) {
            return FloatArgumentType.floatArg(this.min, this.max);
        }

        @Override
        public ArgumentTypeInfo<FloatArgumentType, ?> type() {
            return FloatArgumentInfo.this;
        }
    }
}