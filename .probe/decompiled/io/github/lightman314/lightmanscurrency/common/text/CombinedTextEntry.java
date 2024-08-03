package io.github.lightman314.lightmanscurrency.common.text;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

public final class CombinedTextEntry {

    private final List<Supplier<String>> keys;

    public void forEachKey(@Nonnull Consumer<String> consumer) {
        this.keys.forEach(s -> consumer.accept((String) s.get()));
    }

    public CombinedTextEntry(@Nonnull List<Supplier<String>> keys) {
        this.keys = ImmutableList.copyOf(keys);
    }

    @SafeVarargs
    public static CombinedTextEntry items(@Nonnull RegistryObject<? extends ItemLike>... items) {
        List<Supplier<String>> list = new ArrayList();
        for (RegistryObject<? extends ItemLike> item : items) {
            list.add((Supplier) () -> item.get().asItem().getDescriptionId());
        }
        return new CombinedTextEntry(list);
    }
}