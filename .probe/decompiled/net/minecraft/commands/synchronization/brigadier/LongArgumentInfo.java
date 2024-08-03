package net.minecraft.commands.synchronization.brigadier;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.LongArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentUtils;
import net.minecraft.network.FriendlyByteBuf;

public class LongArgumentInfo implements ArgumentTypeInfo<LongArgumentType, LongArgumentInfo.Template> {

    public void serializeToNetwork(LongArgumentInfo.Template longArgumentInfoTemplate0, FriendlyByteBuf friendlyByteBuf1) {
        boolean $$2 = longArgumentInfoTemplate0.min != Long.MIN_VALUE;
        boolean $$3 = longArgumentInfoTemplate0.max != Long.MAX_VALUE;
        friendlyByteBuf1.writeByte(ArgumentUtils.createNumberFlags($$2, $$3));
        if ($$2) {
            friendlyByteBuf1.writeLong(longArgumentInfoTemplate0.min);
        }
        if ($$3) {
            friendlyByteBuf1.writeLong(longArgumentInfoTemplate0.max);
        }
    }

    public LongArgumentInfo.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf0) {
        byte $$1 = friendlyByteBuf0.readByte();
        long $$2 = ArgumentUtils.numberHasMin($$1) ? friendlyByteBuf0.readLong() : Long.MIN_VALUE;
        long $$3 = ArgumentUtils.numberHasMax($$1) ? friendlyByteBuf0.readLong() : Long.MAX_VALUE;
        return new LongArgumentInfo.Template($$2, $$3);
    }

    public void serializeToJson(LongArgumentInfo.Template longArgumentInfoTemplate0, JsonObject jsonObject1) {
        if (longArgumentInfoTemplate0.min != Long.MIN_VALUE) {
            jsonObject1.addProperty("min", longArgumentInfoTemplate0.min);
        }
        if (longArgumentInfoTemplate0.max != Long.MAX_VALUE) {
            jsonObject1.addProperty("max", longArgumentInfoTemplate0.max);
        }
    }

    public LongArgumentInfo.Template unpack(LongArgumentType longArgumentType0) {
        return new LongArgumentInfo.Template(longArgumentType0.getMinimum(), longArgumentType0.getMaximum());
    }

    public final class Template implements ArgumentTypeInfo.Template<LongArgumentType> {

        final long min;

        final long max;

        Template(long long0, long long1) {
            this.min = long0;
            this.max = long1;
        }

        public LongArgumentType instantiate(CommandBuildContext commandBuildContext0) {
            return LongArgumentType.longArg(this.min, this.max);
        }

        @Override
        public ArgumentTypeInfo<LongArgumentType, ?> type() {
            return LongArgumentInfo.this;
        }
    }
}