package net.minecraft.commands.synchronization.brigadier;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentUtils;
import net.minecraft.network.FriendlyByteBuf;

public class IntegerArgumentInfo implements ArgumentTypeInfo<IntegerArgumentType, IntegerArgumentInfo.Template> {

    public void serializeToNetwork(IntegerArgumentInfo.Template integerArgumentInfoTemplate0, FriendlyByteBuf friendlyByteBuf1) {
        boolean $$2 = integerArgumentInfoTemplate0.min != Integer.MIN_VALUE;
        boolean $$3 = integerArgumentInfoTemplate0.max != Integer.MAX_VALUE;
        friendlyByteBuf1.writeByte(ArgumentUtils.createNumberFlags($$2, $$3));
        if ($$2) {
            friendlyByteBuf1.writeInt(integerArgumentInfoTemplate0.min);
        }
        if ($$3) {
            friendlyByteBuf1.writeInt(integerArgumentInfoTemplate0.max);
        }
    }

    public IntegerArgumentInfo.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf0) {
        byte $$1 = friendlyByteBuf0.readByte();
        int $$2 = ArgumentUtils.numberHasMin($$1) ? friendlyByteBuf0.readInt() : Integer.MIN_VALUE;
        int $$3 = ArgumentUtils.numberHasMax($$1) ? friendlyByteBuf0.readInt() : Integer.MAX_VALUE;
        return new IntegerArgumentInfo.Template($$2, $$3);
    }

    public void serializeToJson(IntegerArgumentInfo.Template integerArgumentInfoTemplate0, JsonObject jsonObject1) {
        if (integerArgumentInfoTemplate0.min != Integer.MIN_VALUE) {
            jsonObject1.addProperty("min", integerArgumentInfoTemplate0.min);
        }
        if (integerArgumentInfoTemplate0.max != Integer.MAX_VALUE) {
            jsonObject1.addProperty("max", integerArgumentInfoTemplate0.max);
        }
    }

    public IntegerArgumentInfo.Template unpack(IntegerArgumentType integerArgumentType0) {
        return new IntegerArgumentInfo.Template(integerArgumentType0.getMinimum(), integerArgumentType0.getMaximum());
    }

    public final class Template implements ArgumentTypeInfo.Template<IntegerArgumentType> {

        final int min;

        final int max;

        Template(int int0, int int1) {
            this.min = int0;
            this.max = int1;
        }

        public IntegerArgumentType instantiate(CommandBuildContext commandBuildContext0) {
            return IntegerArgumentType.integer(this.min, this.max);
        }

        @Override
        public ArgumentTypeInfo<IntegerArgumentType, ?> type() {
            return IntegerArgumentInfo.this;
        }
    }
}