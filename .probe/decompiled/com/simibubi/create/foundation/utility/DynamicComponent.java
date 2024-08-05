package com.simibubi.create.foundation.utility;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class DynamicComponent {

    private JsonElement rawCustomText;

    private Component parsedCustomText;

    public void displayCustomText(Level level, BlockPos pos, String tagElement) {
        if (tagElement != null) {
            this.rawCustomText = getJsonFromString(tagElement);
            this.parsedCustomText = parseCustomText(level, pos, this.rawCustomText);
        }
    }

    public boolean sameAs(String tagElement) {
        return this.isValid() && this.rawCustomText.equals(getJsonFromString(tagElement));
    }

    public boolean isValid() {
        return this.parsedCustomText != null && this.rawCustomText != null;
    }

    public String resolve() {
        return this.parsedCustomText.getString();
    }

    public MutableComponent get() {
        return this.parsedCustomText == null ? Components.empty() : this.parsedCustomText.copy();
    }

    public void read(Level level, BlockPos pos, CompoundTag nbt) {
        this.rawCustomText = getJsonFromString(nbt.getString("RawCustomText"));
        try {
            this.parsedCustomText = Component.Serializer.fromJson(nbt.getString("CustomText"));
        } catch (JsonParseException var5) {
            this.parsedCustomText = null;
        }
    }

    public void write(CompoundTag nbt) {
        if (this.isValid()) {
            nbt.putString("RawCustomText", this.rawCustomText.toString());
            nbt.putString("CustomText", Component.Serializer.toJson(this.parsedCustomText));
        }
    }

    public static JsonElement getJsonFromString(String string) {
        try {
            return JsonParser.parseString(string);
        } catch (JsonParseException var2) {
            return null;
        }
    }

    public static Component parseCustomText(Level level, BlockPos pos, JsonElement customText) {
        if (level instanceof ServerLevel serverLevel) {
            try {
                return ComponentUtils.updateForEntity(getCommandSource(serverLevel, pos), Component.Serializer.fromJson(customText), null, 0);
            } catch (JsonParseException var5) {
                return null;
            } catch (CommandSyntaxException var6) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static CommandSourceStack getCommandSource(ServerLevel level, BlockPos pos) {
        return new CommandSourceStack(CommandSource.NULL, Vec3.atCenterOf(pos), Vec2.ZERO, level, 2, "create", Components.literal("create"), level.getServer(), null);
    }
}