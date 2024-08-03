package dev.xkmc.l2artifacts.content.core;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@SerialClass
public class StatEntry {

    @SerialField
    public ResourceLocation type;

    @SerialField
    private double value;

    private String name;

    public UUID id;

    @Deprecated
    public StatEntry() {
    }

    public StatEntry(ArtifactSlot slot, ResourceLocation type, double value) {
        this.type = type;
        this.value = value;
        this.init(slot);
    }

    protected void init(ArtifactSlot slot) {
        this.name = RegistrateLangProvider.toEnglishName(slot.getRegistryName().getPath());
    }

    public StatTypeConfig getType() {
        return StatTypeConfig.get(this.type);
    }

    public Component getTooltip() {
        return this.getType().getTooltip(this.getValue());
    }

    public double getValue() {
        return this.value * this.getType().getBaseValue();
    }

    public void addMultiplier(double value) {
        this.value += value;
    }

    public String getName() {
        return this.name;
    }
}