package com.simibubi.create.foundation.config.ui;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import java.util.Objects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.network.NetworkEvent;

public class CConfigureConfigPacket<T> extends SimplePacketBase {

    private String modID;

    private String path;

    private String value;

    public CConfigureConfigPacket(String modID, String path, T value) {
        this.modID = (String) Objects.requireNonNull(modID);
        this.path = path;
        this.value = this.serialize(value);
    }

    public CConfigureConfigPacket(FriendlyByteBuf buffer) {
        this.modID = buffer.readUtf(32767);
        this.path = buffer.readUtf(32767);
        this.value = buffer.readUtf(32767);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.modID);
        buffer.writeUtf(this.path);
        buffer.writeUtf(this.value);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            try {
                ServerPlayer sender = context.getSender();
                if (sender == null || !sender.m_20310_(2)) {
                    return;
                }
                ForgeConfigSpec spec = ConfigHelper.findForgeConfigSpecFor(Type.SERVER, this.modID);
                if (spec == null) {
                    return;
                }
                ForgeConfigSpec.ValueSpec valueSpec = (ForgeConfigSpec.ValueSpec) spec.getRaw(this.path);
                ForgeConfigSpec.ConfigValue<T> configValue = (ForgeConfigSpec.ConfigValue<T>) spec.getValues().get(this.path);
                T v = (T) deserialize(configValue.get(), this.value);
                if (!valueSpec.test(v)) {
                    return;
                }
                configValue.set(v);
            } catch (Exception var7) {
                Create.LOGGER.warn("Unable to handle ConfigureConfig Packet. ", var7);
            }
        });
        return true;
    }

    public String serialize(T value) {
        if (value instanceof Boolean) {
            return Boolean.toString((Boolean) value);
        } else if (value instanceof Enum) {
            return ((Enum) value).name();
        } else if (value instanceof Integer) {
            return Integer.toString((Integer) value);
        } else if (value instanceof Float) {
            return Float.toString((Float) value);
        } else if (value instanceof Double) {
            return Double.toString((Double) value);
        } else {
            throw new IllegalArgumentException("unknown type " + value + ": " + value.getClass().getSimpleName());
        }
    }

    public static Object deserialize(Object type, String sValue) {
        if (type instanceof Boolean) {
            return Boolean.parseBoolean(sValue);
        } else if (type instanceof Enum) {
            return Enum.valueOf(((Enum) type).getClass(), sValue);
        } else if (type instanceof Integer) {
            return Integer.parseInt(sValue);
        } else if (type instanceof Float) {
            return Float.parseFloat(sValue);
        } else if (type instanceof Double) {
            return Double.parseDouble(sValue);
        } else {
            throw new IllegalArgumentException("unknown type " + type + ": " + type.getClass().getSimpleName());
        }
    }
}