package net.minecraft.advancements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class AdvancementList {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<ResourceLocation, Advancement> advancements = Maps.newHashMap();

    private final Set<Advancement> roots = Sets.newLinkedHashSet();

    private final Set<Advancement> tasks = Sets.newLinkedHashSet();

    @Nullable
    private AdvancementList.Listener listener;

    private void remove(Advancement advancement0) {
        for (Advancement $$1 : advancement0.getChildren()) {
            this.remove($$1);
        }
        LOGGER.info("Forgot about advancement {}", advancement0.getId());
        this.advancements.remove(advancement0.getId());
        if (advancement0.getParent() == null) {
            this.roots.remove(advancement0);
            if (this.listener != null) {
                this.listener.onRemoveAdvancementRoot(advancement0);
            }
        } else {
            this.tasks.remove(advancement0);
            if (this.listener != null) {
                this.listener.onRemoveAdvancementTask(advancement0);
            }
        }
    }

    public void remove(Set<ResourceLocation> setResourceLocation0) {
        for (ResourceLocation $$1 : setResourceLocation0) {
            Advancement $$2 = (Advancement) this.advancements.get($$1);
            if ($$2 == null) {
                LOGGER.warn("Told to remove advancement {} but I don't know what that is", $$1);
            } else {
                this.remove($$2);
            }
        }
    }

    public void add(Map<ResourceLocation, Advancement.Builder> mapResourceLocationAdvancementBuilder0) {
        Map<ResourceLocation, Advancement.Builder> $$1 = Maps.newHashMap(mapResourceLocationAdvancementBuilder0);
        while (!$$1.isEmpty()) {
            boolean $$2 = false;
            Iterator<Entry<ResourceLocation, Advancement.Builder>> $$3 = $$1.entrySet().iterator();
            while ($$3.hasNext()) {
                Entry<ResourceLocation, Advancement.Builder> $$4 = (Entry<ResourceLocation, Advancement.Builder>) $$3.next();
                ResourceLocation $$5 = (ResourceLocation) $$4.getKey();
                Advancement.Builder $$6 = (Advancement.Builder) $$4.getValue();
                if ($$6.canBuild(this.advancements::get)) {
                    Advancement $$7 = $$6.build($$5);
                    this.advancements.put($$5, $$7);
                    $$2 = true;
                    $$3.remove();
                    if ($$7.getParent() == null) {
                        this.roots.add($$7);
                        if (this.listener != null) {
                            this.listener.onAddAdvancementRoot($$7);
                        }
                    } else {
                        this.tasks.add($$7);
                        if (this.listener != null) {
                            this.listener.onAddAdvancementTask($$7);
                        }
                    }
                }
            }
            if (!$$2) {
                for (Entry<ResourceLocation, Advancement.Builder> $$8 : $$1.entrySet()) {
                    LOGGER.error("Couldn't load advancement {}: {}", $$8.getKey(), $$8.getValue());
                }
                break;
            }
        }
        LOGGER.info("Loaded {} advancements", this.advancements.size());
    }

    public void clear() {
        this.advancements.clear();
        this.roots.clear();
        this.tasks.clear();
        if (this.listener != null) {
            this.listener.onAdvancementsCleared();
        }
    }

    public Iterable<Advancement> getRoots() {
        return this.roots;
    }

    public Collection<Advancement> getAllAdvancements() {
        return this.advancements.values();
    }

    @Nullable
    public Advancement get(ResourceLocation resourceLocation0) {
        return (Advancement) this.advancements.get(resourceLocation0);
    }

    public void setListener(@Nullable AdvancementList.Listener advancementListListener0) {
        this.listener = advancementListListener0;
        if (advancementListListener0 != null) {
            for (Advancement $$1 : this.roots) {
                advancementListListener0.onAddAdvancementRoot($$1);
            }
            for (Advancement $$2 : this.tasks) {
                advancementListListener0.onAddAdvancementTask($$2);
            }
        }
    }

    public interface Listener {

        void onAddAdvancementRoot(Advancement var1);

        void onRemoveAdvancementRoot(Advancement var1);

        void onAddAdvancementTask(Advancement var1);

        void onRemoveAdvancementTask(Advancement var1);

        void onAdvancementsCleared();
    }
}