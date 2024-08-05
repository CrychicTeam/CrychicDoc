package org.violetmoon.zeta.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.event.play.loading.ZGatherAdditionalFlags;
import org.violetmoon.zeta.module.ZetaCategory;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.module.ZetaModuleManager;

public class ConfigManager {

    private final Zeta z;

    private final ConfigFlagManager cfm;

    private final SectionDefinition rootConfig;

    private final List<Consumer<IZetaConfigInternals>> databindings = new ArrayList();

    private Consumer<IZetaConfigInternals> onConfigReloadJEI;

    @Nullable
    private final SectionDefinition generalSection;

    private final Map<ZetaCategory, SectionDefinition> categoriesToSections = new HashMap();

    private final Map<ZetaCategory, ValueDefinition<Boolean>> categoryEnabledOptions = new HashMap();

    private final Map<ZetaModule, ValueDefinition<Boolean>> ignoreAntiOverlapOptions = new HashMap();

    private final Map<ZetaModule, ValueDefinition<Boolean>> moduleEnabledOptions = new HashMap();

    private final Set<ZetaCategory> enabledCategories = new HashSet();

    public ConfigManager(Zeta z, Object rootPojo) {
        this.z = z;
        this.cfm = new ConfigFlagManager(z);
        ZetaModuleManager modules = z.modules;
        this.enabledCategories.addAll(modules.getCategories());
        SectionDefinition.Builder rootConfigBuilder = new SectionDefinition.Builder().name("");
        if (rootPojo == null) {
            this.generalSection = null;
        } else {
            z.loadBus.subscribe(rootPojo).subscribe(rootPojo.getClass());
            z.playBus.subscribe(rootPojo).subscribe(rootPojo.getClass());
            this.generalSection = rootConfigBuilder.addSubsection(general -> ConfigObjectMapper.readInto(general.name("general"), rootPojo, this.databindings, this.cfm));
        }
        rootConfigBuilder.addSubsection(categories -> {
            categories.name("categories");
            for (ZetaCategory categoryx : modules.getInhabitedCategories()) {
                this.categoryEnabledOptions.put(categoryx, categories.addValue(b -> ((ValueDefinition.Builder) b.name(categoryx.name)).defaultValue(true)));
            }
        });
        for (ZetaCategory category : modules.getInhabitedCategories()) {
            this.categoriesToSections.put(category, rootConfigBuilder.addSubsection(categorySectionBuilder -> {
                categorySectionBuilder.name(category.name);
                for (ZetaModule module : modules.modulesInCategory(category)) {
                    this.cfm.putModuleFlag(module);
                    this.moduleEnabledOptions.put(module, categorySectionBuilder.addValue(moduleEnabledOptionBuilder -> ((ValueDefinition.Builder) ((ValueDefinition.Builder) ((ValueDefinition.Builder) moduleEnabledOptionBuilder.name(module.displayName)).englishDisplayName(module.displayName)).comment(module.description)).defaultValue(module.enabledByDefault)));
                    categorySectionBuilder.addSubsection(moduleSectionBuilder -> {
                        moduleSectionBuilder.name(module.lowercaseName).englishDisplayName(module.displayName).comment(module.description);
                        ConfigObjectMapper.readInto(moduleSectionBuilder, module, this.databindings, this.cfm);
                        if (!module.antiOverlap.isEmpty()) {
                            this.ignoreAntiOverlapOptions.put(module, moduleSectionBuilder.addValue(antiOverlapOptionBuilder -> {
                                ((ValueDefinition.Builder) ((ValueDefinition.Builder) antiOverlapOptionBuilder.name("Ignore Anti Overlap")).comment("This feature disables itself if any of the following mods are loaded:")).defaultValue(false);
                                for (String modid : module.antiOverlap) {
                                    antiOverlapOptionBuilder.comment(" - " + modid);
                                }
                                ((ValueDefinition.Builder) antiOverlapOptionBuilder.comment("This is done to prevent content overlap.")).comment("You can turn this on to force the feature to be loaded even if the above mods are also loaded.");
                            }));
                        }
                    });
                }
            }));
        }
        z.playBus.fire(new ZGatherAdditionalFlags(this.cfm));
        this.databindings.add(0, (Consumer) i -> {
            this.categoryEnabledOptions.forEach((categoryx, option) -> this.setCategoryEnabled(categoryx, i.<Boolean>get(option)));
            this.ignoreAntiOverlapOptions.forEach((module, option) -> module.ignoreAntiOverlap = !ZetaGeneralConfig.useAntiOverlap || i.<Boolean>get(option));
            this.moduleEnabledOptions.forEach((module, option) -> {
                this.setModuleEnabled(module, i.<Boolean>get(option));
                this.cfm.putModuleFlag(module);
            });
            z.playBus.fire(new ZGatherAdditionalFlags(this.cfm));
        });
        this.rootConfig = rootConfigBuilder.build();
        this.rootConfig.finish();
    }

    public SectionDefinition getRootConfig() {
        return this.rootConfig;
    }

    @Nullable
    public SectionDefinition getGeneralSection() {
        return this.generalSection;
    }

    public SectionDefinition getCategorySection(ZetaCategory cat) {
        return (SectionDefinition) this.categoriesToSections.get(cat);
    }

    public ValueDefinition<Boolean> getCategoryEnabledOption(ZetaCategory cat) {
        return (ValueDefinition<Boolean>) this.categoryEnabledOptions.get(cat);
    }

    public ValueDefinition<Boolean> getModuleEnabledOption(ZetaModule module) {
        return (ValueDefinition<Boolean>) this.moduleEnabledOptions.get(module);
    }

    private void setCategoryEnabled(ZetaCategory cat, boolean enabled) {
        if (enabled) {
            this.enabledCategories.add(cat);
        } else {
            this.enabledCategories.remove(cat);
        }
        for (ZetaModule mod : this.z.modules.modulesInCategory(cat)) {
            mod.setEnabled(this.z, mod.enabled);
        }
    }

    private void setModuleEnabled(ZetaModule module, boolean enabled) {
        module.setEnabled(this.z, enabled);
    }

    public boolean isCategoryEnabled(ZetaCategory cat) {
        return this.enabledCategories.contains(cat);
    }

    public ConfigFlagManager getConfigFlagManager() {
        return this.cfm;
    }

    public void onReload() {
        IZetaConfigInternals internals = this.z.configInternals;
        this.databindings.forEach(c -> c.accept(internals));
        if (this.onConfigReloadJEI != null) {
            this.onConfigReloadJEI.accept(internals);
        }
    }

    public void setJeiReloadListener(Consumer<IZetaConfigInternals> consumer) {
        this.onConfigReloadJEI = consumer;
        consumer.accept(this.z.configInternals);
    }
}