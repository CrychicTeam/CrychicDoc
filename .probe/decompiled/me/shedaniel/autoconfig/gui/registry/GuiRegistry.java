package me.shedaniel.autoconfig.gui.registry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.shedaniel.autoconfig.gui.registry.api.GuiProvider;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.autoconfig.gui.registry.api.GuiTransformer;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiRegistry implements GuiRegistryAccess {

    private Map<GuiRegistry.Priority, List<GuiRegistry.ProviderEntry>> providers = new HashMap();

    private List<GuiRegistry.TransformerEntry> transformers = new ArrayList();

    public GuiRegistry() {
        for (GuiRegistry.Priority priority : GuiRegistry.Priority.values()) {
            this.providers.put(priority, new ArrayList());
        }
    }

    private static <T> Optional<T> firstPresent(Stream<Supplier<Optional<T>>> optionals) {
        return (Optional<T>) optionals.map(Supplier::get).filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }

    @Override
    public List<AbstractConfigListEntry> get(String i18n, Field field, Object config, Object defaults, GuiRegistryAccess registry) {
        return (List<AbstractConfigListEntry>) firstPresent(Arrays.stream(GuiRegistry.Priority.values()).map(priority -> () -> ((List) this.providers.get(priority)).stream().filter(entry -> entry.predicate.test(field)).findFirst())).map(entry -> entry.provider.get(i18n, field, config, defaults, registry)).orElse(null);
    }

    @Override
    public List<AbstractConfigListEntry> transform(List<AbstractConfigListEntry> guis, String i18n, Field field, Object config, Object defaults, GuiRegistryAccess registry) {
        for (GuiTransformer transformer : (List) this.transformers.stream().filter(entry -> entry.predicate.test(field)).map(entry -> entry.transformer).collect(Collectors.toList())) {
            guis = transformer.transform(guis, i18n, field, config, defaults, registry);
        }
        return guis;
    }

    private void registerProvider(GuiRegistry.Priority priority, GuiProvider provider, Predicate<Field> predicate) {
        ((List) this.providers.computeIfAbsent(priority, p -> new ArrayList())).add(new GuiRegistry.ProviderEntry(predicate, provider));
    }

    public final void registerTypeProvider(GuiProvider provider, Class... types) {
        for (Class type : types) {
            this.registerProvider(GuiRegistry.Priority.LAST, provider, field -> type == field.getType());
        }
    }

    public final void registerPredicateProvider(GuiProvider provider, Predicate<Field> predicate) {
        this.registerProvider(GuiRegistry.Priority.NORMAL, provider, predicate);
    }

    @SafeVarargs
    public final void registerAnnotationProvider(GuiProvider provider, Class<? extends Annotation>... types) {
        for (Class<? extends Annotation> type : types) {
            this.registerProvider(GuiRegistry.Priority.FIRST, provider, field -> field.isAnnotationPresent(type));
        }
    }

    @SafeVarargs
    public final void registerAnnotationProvider(GuiProvider provider, Predicate<Field> predicate, Class<? extends Annotation>... types) {
        for (Class<? extends Annotation> type : types) {
            this.registerProvider(GuiRegistry.Priority.FIRST, provider, field -> predicate.test(field) && field.isAnnotationPresent(type));
        }
    }

    public void registerPredicateTransformer(GuiTransformer transformer, Predicate<Field> predicate) {
        this.transformers.add(new GuiRegistry.TransformerEntry(predicate, transformer));
    }

    @SafeVarargs
    public final void registerAnnotationTransformer(GuiTransformer transformer, Class<? extends Annotation>... types) {
        this.registerAnnotationTransformer(transformer, field -> true, types);
    }

    @SafeVarargs
    public final void registerAnnotationTransformer(GuiTransformer transformer, Predicate<Field> predicate, Class<? extends Annotation>... types) {
        for (Class<? extends Annotation> type : types) {
            this.registerPredicateTransformer(transformer, field -> predicate.test(field) && field.isAnnotationPresent(type));
        }
    }

    private static enum Priority {

        FIRST, NORMAL, LAST
    }

    private static class ProviderEntry {

        final Predicate<Field> predicate;

        final GuiProvider provider;

        ProviderEntry(Predicate<Field> predicate, GuiProvider provider) {
            this.predicate = predicate;
            this.provider = provider;
        }
    }

    private static class TransformerEntry {

        final Predicate<Field> predicate;

        final GuiTransformer transformer;

        TransformerEntry(Predicate<Field> predicate, GuiTransformer transformer) {
            this.predicate = predicate;
            this.transformer = transformer;
        }
    }
}