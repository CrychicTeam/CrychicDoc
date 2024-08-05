package io.github.lightman314.lightmanscurrency.common.menus.validation;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public abstract class MenuValidatorType {

    private static final Map<ResourceLocation, MenuValidatorType> TYPES = new HashMap();

    public final ResourceLocation type;

    public static void register(MenuValidatorType validator) {
        if (TYPES.containsKey(validator.type)) {
            LightmansCurrency.LogWarning("Attempted to register duplicate validator type '" + validator.type + "'!");
        } else {
            TYPES.put(validator.type, validator);
        }
    }

    public static MenuValidatorType getType(ResourceLocation type) {
        return (MenuValidatorType) TYPES.get(type);
    }

    protected MenuValidatorType(ResourceLocation type) {
        this.type = type;
    }

    @Nonnull
    public abstract MenuValidator decode(@Nonnull FriendlyByteBuf var1);

    @Nonnull
    public abstract MenuValidator load(@Nonnull CompoundTag var1);
}