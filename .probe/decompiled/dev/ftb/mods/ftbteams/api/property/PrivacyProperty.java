package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class PrivacyProperty extends TeamProperty<PrivacyMode> {

    public PrivacyProperty(ResourceLocation id, Supplier<PrivacyMode> def) {
        super(id, def);
    }

    public PrivacyProperty(ResourceLocation id, PrivacyMode def) {
        this(id, () -> def);
    }

    static PrivacyProperty fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        return new PrivacyProperty(id, buf.readEnum(PrivacyMode.class));
    }

    @Override
    public TeamPropertyType<PrivacyMode> getType() {
        return TeamPropertyType.PRIVACY_MODE;
    }

    @Override
    public Optional<PrivacyMode> fromString(String string) {
        return Optional.ofNullable(PrivacyMode.NAME_MAP.getNullable(string));
    }

    public String toString(PrivacyMode value) {
        return value.getSerializedName();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.getDefaultValue());
    }

    @Override
    public void config(ConfigGroup config, TeamPropertyValue<PrivacyMode> value) {
        config.addEnum(this.id.getPath(), value.value, value.consumer, PrivacyMode.NAME_MAP);
    }
}