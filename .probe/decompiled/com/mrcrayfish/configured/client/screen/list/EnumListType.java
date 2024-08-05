package com.mrcrayfish.configured.client.screen.list;

import com.mrcrayfish.configured.api.IAllowedEnums;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.network.chat.Component;

public class EnumListType<T extends Enum<T>> implements IListType<T>, IAllowedEnums<T> {

    private final Class<T> enumClass;

    private final Set<T> allowedValues;

    public EnumListType(Class<T> enumClass) {
        this.enumClass = enumClass;
        this.allowedValues = Set.of((Enum[]) enumClass.getEnumConstants());
    }

    @Override
    public Function<T, String> getStringParser() {
        return Enum::name;
    }

    @Override
    public Function<String, T> getValueParser() {
        return s -> {
            try {
                return Enum.valueOf(this.enumClass, s);
            } catch (IllegalArgumentException var3) {
                return null;
            }
        };
    }

    @Override
    public Component getHint() {
        return Component.translatable("configured.parser.not_a_value");
    }

    @Override
    public Set<T> getAllowedValues() {
        return this.allowedValues;
    }
}