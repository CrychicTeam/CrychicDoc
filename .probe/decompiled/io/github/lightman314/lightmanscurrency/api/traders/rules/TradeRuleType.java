package io.github.lightman314.lightmanscurrency.api.traders.rules;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.common.traders.rules.TradeRule;
import javax.annotation.Nonnull;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.NonNullSupplier;

public final class TradeRuleType<T extends TradeRule> {

    public final ResourceLocation type;

    private final NonNullSupplier<T> generator;

    public TradeRuleType(@Nonnull ResourceLocation type, @Nonnull NonNullSupplier<T> generator) {
        this.type = type;
        this.generator = generator;
    }

    @Nonnull
    public T createNew() {
        return this.generator.get();
    }

    public T load(@Nonnull CompoundTag tag) {
        try {
            T rule = this.createNew();
            rule.load(tag);
            return rule;
        } catch (Throwable var3) {
            LightmansCurrency.LogError("Error loading Trade Rule!", var3);
            return null;
        }
    }

    public T loadFromJson(@Nonnull JsonObject json) throws JsonSyntaxException, ResourceLocationException {
        T rule = this.createNew();
        rule.loadFromJson(json);
        rule.setActive(true);
        return rule;
    }

    public String toString() {
        return this.type.toString();
    }
}