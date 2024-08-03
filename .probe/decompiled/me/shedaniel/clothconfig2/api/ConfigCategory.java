package me.shedaniel.clothconfig2.api;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public interface ConfigCategory {

    Component getCategoryKey();

    @Deprecated
    List<Object> getEntries();

    ConfigCategory addEntry(AbstractConfigListEntry var1);

    ConfigCategory setCategoryBackground(ResourceLocation var1);

    void setBackground(@Nullable ResourceLocation var1);

    @Nullable
    ResourceLocation getBackground();

    @Nullable
    Supplier<Optional<FormattedText[]>> getDescription();

    void setDescription(@Nullable Supplier<Optional<FormattedText[]>> var1);

    default void setDescription(@Nullable FormattedText[] description) {
        this.setDescription(() -> Optional.ofNullable(description));
    }

    void removeCategory();
}