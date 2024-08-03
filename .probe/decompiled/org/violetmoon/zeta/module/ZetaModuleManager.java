package org.violetmoon.zeta.module;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.event.load.ZModulesReady;
import org.violetmoon.zeta.util.ZetaSide;

public class ZetaModuleManager {

    private final Zeta z;

    private final Map<Class<? extends ZetaModule>, ZetaModule> modulesByKey = new LinkedHashMap();

    private final Map<String, ZetaCategory> categoriesById = new LinkedHashMap();

    private final Map<ZetaCategory, List<ZetaModule>> modulesInCategory = new HashMap();

    public ZetaModuleManager(Zeta z) {
        this.z = z;
    }

    public Collection<ZetaModule> getModules() {
        return this.modulesByKey.values();
    }

    public <M extends ZetaModule> M get(Class<M> keyClass) {
        return (M) this.modulesByKey.get(keyClass);
    }

    public <M extends ZetaModule> Optional<M> getOptional(Class<M> keyClass) {
        return Optional.ofNullable(this.get(keyClass));
    }

    public boolean isEnabled(Class<? extends ZetaModule> keyClass) {
        return this.get(keyClass).enabled;
    }

    public boolean isEnabledOrOverlapping(Class<? extends ZetaModule> keyClass) {
        ZetaModule module = this.get(keyClass);
        return module.enabled || module.disabledByOverlap;
    }

    public ZetaCategory getCategory(String id) {
        if (id == null || id.isEmpty()) {
            id = "Unknown";
        }
        return (ZetaCategory) this.categoriesById.computeIfAbsent(id, ZetaCategory::unknownCategory);
    }

    public Collection<ZetaCategory> getCategories() {
        return this.categoriesById.values();
    }

    public List<ZetaCategory> getInhabitedCategories() {
        return this.categoriesById.values().stream().filter(c -> !this.modulesInCategory(c).isEmpty()).toList();
    }

    public List<ZetaModule> modulesInCategory(ZetaCategory cat) {
        return (List<ZetaModule>) this.modulesInCategory.computeIfAbsent(cat, __ -> new ArrayList());
    }

    public void initCategories(Iterable<ZetaCategory> cats) {
        for (ZetaCategory cat : cats) {
            this.categoriesById.put(cat.name, cat);
        }
    }

    public void load(ModuleFinder finder) {
        Collection<TentativeModule> tentative = ((Stream) finder.get()).map(data -> TentativeModule.from(data, this::getCategory)).filter(tmx -> tmx.appliesTo(this.z.side)).sorted(Comparator.comparing(TentativeModule::loadPhase).thenComparing(TentativeModule::displayName)).toList();
        if (this.z.side == ZetaSide.CLIENT) {
            Map<Class<? extends ZetaModule>, TentativeModule> byClazz = new LinkedHashMap();
            for (TentativeModule tm : tentative) {
                if (!tm.clientReplacement()) {
                    byClazz.put(tm.clazz(), tm);
                }
            }
            for (TentativeModule tmx : tentative) {
                if (tmx.clientReplacement()) {
                    Class<? extends ZetaModule> superclass = tmx.clazz().getSuperclass();
                    TentativeModule existing = (TentativeModule) byClazz.get(superclass);
                    if (existing == null) {
                        throw new RuntimeException("Module " + tmx.clazz().getName() + " wants to replace " + superclass.getName() + ", but that module isn't registered");
                    }
                    byClazz.put(superclass, existing.replaceWith(tmx));
                }
            }
            tentative = byClazz.values();
        }
        this.z.log.info("Discovered " + tentative.size() + " modules to load.");
        for (TentativeModule t : tentative) {
            this.modulesByKey.put(t.keyClass(), this.constructAndSetup(t));
        }
        this.z.log.info("Constructed {} modules.", this.modulesByKey.size());
        this.z.loadBus.fire(new ZModulesReady());
    }

    private ZetaModule constructAndSetup(TentativeModule t) {
        this.z.log.info("Constructing module {}...", t.displayName());
        ZetaModule module = this.construct(t.clazz());
        module.zeta = this.z;
        module.category = t.category();
        module.displayName = t.displayName();
        module.lowercaseName = t.lowercaseName();
        module.description = t.description();
        module.antiOverlap = t.antiOverlap();
        module.enabledByDefault = t.enabledByDefault();
        module.setEnabled(this.z, t.enabledByDefault());
        this.z.loadBus.subscribe(module.getClass()).subscribe(module);
        ((List) this.modulesInCategory.computeIfAbsent(module.category, __ -> new ArrayList())).add(module);
        module.postConstruct();
        return module;
    }

    private <Z extends ZetaModule> Z construct(Class<Z> clazz) {
        try {
            Constructor<Z> cons = clazz.getConstructor();
            return (Z) cons.newInstance();
        } catch (Exception var3) {
            throw new RuntimeException("Could not construct ZetaModule '" + clazz.getName() + "', does it have a public zero-argument constructor?", var3);
        }
    }
}