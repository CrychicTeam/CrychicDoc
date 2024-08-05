package net.minecraft.commands.synchronization.brigadier;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentUtils;
import net.minecraft.network.FriendlyByteBuf;

public class DoubleArgumentInfo implements ArgumentTypeInfo<DoubleArgumentType, DoubleArgumentInfo.Template> {

    public void serializeToNetwork(DoubleArgumentInfo.Template doubleArgumentInfoTemplate0, FriendlyByteBuf friendlyByteBuf1) {
        boolean $$2 = doubleArgumentInfoTemplate0.min != -Double.MAX_VALUE;
        boolean $$3 = doubleArgumentInfoTemplate0.max != Double.MAX_VALUE;
        friendlyByteBuf1.writeByte(ArgumentUtils.createNumberFlags($$2, $$3));
        if ($$2) {
            friendlyByteBuf1.writeDouble(doubleArgumentInfoTemplate0.min);
        }
        if ($$3) {
            friendlyByteBuf1.writeDouble(doubleArgumentInfoTemplate0.max);
        }
    }

    public DoubleArgumentInfo.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf0) {
        byte $$1 = friendlyByteBuf0.readByte();
        double $$2 = ArgumentUtils.numberHasMin($$1) ? friendlyByteBuf0.readDouble() : -Double.MAX_VALUE;
        double $$3 = ArgumentUtils.numberHasMax($$1) ? friendlyByteBuf0.readDouble() : Double.MAX_VALUE;
        return new DoubleArgumentInfo.Template($$2, $$3);
    }

    public void serializeToJson(DoubleArgumentInfo.Template doubleArgumentInfoTemplate0, JsonObject jsonObject1) {
        if (doubleArgumentInfoTemplate0.min != -Double.MAX_VALUE) {
            jsonObject1.addProperty("min", doubleArgumentInfoTemplate0.min);
        }
        if (doubleArgumentInfoTemplate0.max != Double.MAX_VALUE) {
            jsonObject1.addProperty("max", doubleArgumentInfoTemplate0.max);
        }
    }

    public DoubleArgumentInfo.Template unpack(DoubleArgumentType doubleArgumentType0) {
        return new DoubleArgumentInfo.Template(doubleArgumentType0.getMinimum(), doubleArgumentType0.getMaximum());
    }

    public final class Template implements ArgumentTypeInfo.Template<DoubleArgumentType> {

        final double min;

        final double max;

        Template(double double0, double double1) {
            this.min = double0;
            this.max = double1;
        }

        public DoubleArgumentType instantiate(CommandBuildContext commandBuildContext0) {
            return DoubleArgumentType.doubleArg(this.min, this.max);
        }

        @Override
        public ArgumentTypeInfo<DoubleArgumentType, ?> type() {
            return DoubleArgumentInfo.this;
        }
    }
}