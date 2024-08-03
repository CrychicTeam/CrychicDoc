package io.github.lightman314.lightmanscurrency.api.events;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.common.loot.tiers.ChestPoolLevel;
import io.github.lightman314.lightmanscurrency.common.loot.tiers.EntityPoolLevel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public abstract class DroplistConfigGenerator {

    private static final List<Consumer<DroplistConfigGenerator.Entity>> entityListeners = new ArrayList();

    private static final List<Consumer<DroplistConfigGenerator.Chest>> chestListeners = new ArrayList();

    private String defaultNamespace = "minecraft";

    private final List<String> entries = new ArrayList();

    public static void registerEntityListener(@Nonnull Consumer<DroplistConfigGenerator.Entity> listener) {
        if (!entityListeners.contains(listener)) {
            entityListeners.add(listener);
        }
    }

    public static void registerChestListener(@Nonnull Consumer<DroplistConfigGenerator.Chest> listener) {
        if (!chestListeners.contains(listener)) {
            chestListeners.add(listener);
        }
    }

    public static List<String> CollectDefaultEntityDrops(EntityPoolLevel level) {
        DroplistConfigGenerator.Entity generator = new DroplistConfigGenerator.Entity(level);
        for (Consumer<DroplistConfigGenerator.Entity> listener : entityListeners) {
            try {
                listener.accept(generator);
            } catch (Throwable var5) {
                LightmansCurrency.LogError("Error collecting default entity drops.", var5);
            }
        }
        return debugEntries(generator.getEntries(), "Collected Default Entity drops of type '" + level.toString() + "'!\n_VALUE_");
    }

    public static List<String> CollectDefaultChestDrops(ChestPoolLevel level) {
        DroplistConfigGenerator.Chest generator = new DroplistConfigGenerator.Chest(level);
        for (Consumer<DroplistConfigGenerator.Chest> listener : chestListeners) {
            try {
                listener.accept(generator);
            } catch (Throwable var5) {
                LightmansCurrency.LogError("Error collecting default chest drops.", var5);
            }
        }
        return debugEntries(generator.getEntries(), "Collected Default Chest drops of type '" + level.toString() + "'!\n_VALUE_");
    }

    private static List<String> debugEntries(List<String> results, String message) {
        StringBuilder builder = new StringBuilder("[");
        for (String result : results) {
            if (results.indexOf(result) > 0) {
                builder.append(",");
            }
            builder.append('"').append(result).append('"');
        }
        builder.append(']');
        LightmansCurrency.LogDebug(message.replace("_VALUE_", builder.toString()));
        return results;
    }

    public final void resetDefaultNamespace() {
        this.defaultNamespace = "minecraft";
    }

    public final void setDefaultNamespace(@Nonnull String namespace) {
        this.defaultNamespace = namespace;
    }

    public final String getDefaultNamespace() {
        return this.defaultNamespace;
    }

    public final ImmutableList<String> getEntries() {
        return ImmutableList.copyOf(this.entries);
    }

    protected DroplistConfigGenerator() {
    }

    protected abstract ResourceLocation createEntry(String var1, String var2);

    public final void addVanillaEntry(String entry) throws ResourceLocationException {
        this.addEntry("minecraft", entry);
    }

    public final void addEntry(String modid, String entry) throws ResourceLocationException {
        this.forceAddEntry(this.createEntry(modid, entry));
    }

    public final void addEntry(String entry) throws ResourceLocationException {
        this.addEntry(this.defaultNamespace, entry);
    }

    public final void forceAddEntry(@Nonnull ResourceLocation entry) {
        this.forceAdd(entry.toString());
    }

    protected final void forceAdd(@Nonnull String entry) {
        if (!this.entries.contains(entry)) {
            this.entries.add(entry);
        }
    }

    public final void removeEntry(@Nonnull ResourceLocation entry) {
        this.entries.remove(entry);
    }

    public static class Chest extends DroplistConfigGenerator {

        private final ChestPoolLevel level;

        public final ChestPoolLevel getTier() {
            return this.level;
        }

        protected Chest(ChestPoolLevel level) {
            this.level = level;
        }

        @Override
        protected ResourceLocation createEntry(String modid, String entry) {
            return new ResourceLocation(modid, "chests/" + entry);
        }
    }

    public static class Entity extends DroplistConfigGenerator {

        private final EntityPoolLevel level;

        public final EntityPoolLevel getTier() {
            return this.level;
        }

        protected Entity(EntityPoolLevel level) {
            this.level = level;
        }

        @Override
        protected ResourceLocation createEntry(String modid, String entry) {
            return new ResourceLocation(modid, entry);
        }

        public final void forceAddTag(@Nonnull TagKey<EntityType<?>> tag) {
            this.forceAdd("#" + tag.location());
        }

        public final void forceAddTag(@Nonnull ResourceLocation tag) {
            this.forceAdd("#" + tag);
        }

        public final void addTag(@Nonnull String tagID) throws ResourceLocationException {
            this.forceAddTag(new ResourceLocation(this.getDefaultNamespace(), tagID));
        }
    }
}