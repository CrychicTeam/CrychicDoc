package net.minecraft.client.gui.screens.worldselection;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.FileUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.WorldPresetTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;

public class WorldCreationUiState {

    private static final Component DEFAULT_WORLD_NAME = Component.translatable("selectWorld.newWorld");

    private final List<Consumer<WorldCreationUiState>> listeners = new ArrayList();

    private String name = DEFAULT_WORLD_NAME.getString();

    private WorldCreationUiState.SelectedGameMode gameMode = WorldCreationUiState.SelectedGameMode.SURVIVAL;

    private Difficulty difficulty = Difficulty.NORMAL;

    @Nullable
    private Boolean allowCheats;

    private String seed;

    private boolean generateStructures;

    private boolean bonusChest;

    private final Path savesFolder;

    private String targetFolder;

    private WorldCreationContext settings;

    private WorldCreationUiState.WorldTypeEntry worldType;

    private final List<WorldCreationUiState.WorldTypeEntry> normalPresetList = new ArrayList();

    private final List<WorldCreationUiState.WorldTypeEntry> altPresetList = new ArrayList();

    private GameRules gameRules = new GameRules();

    public WorldCreationUiState(Path path0, WorldCreationContext worldCreationContext1, Optional<ResourceKey<WorldPreset>> optionalResourceKeyWorldPreset2, OptionalLong optionalLong3) {
        this.savesFolder = path0;
        this.settings = worldCreationContext1;
        this.worldType = new WorldCreationUiState.WorldTypeEntry((Holder<WorldPreset>) findPreset(worldCreationContext1, optionalResourceKeyWorldPreset2).orElse(null));
        this.updatePresetLists();
        this.seed = optionalLong3.isPresent() ? Long.toString(optionalLong3.getAsLong()) : "";
        this.generateStructures = worldCreationContext1.options().generateStructures();
        this.bonusChest = worldCreationContext1.options().generateBonusChest();
        this.targetFolder = this.findResultFolder(this.name);
    }

    public void addListener(Consumer<WorldCreationUiState> consumerWorldCreationUiState0) {
        this.listeners.add(consumerWorldCreationUiState0);
    }

    public void onChanged() {
        boolean $$0 = this.isBonusChest();
        if ($$0 != this.settings.options().generateBonusChest()) {
            this.settings = this.settings.withOptions(p_268360_ -> p_268360_.withBonusChest($$0));
        }
        boolean $$1 = this.isGenerateStructures();
        if ($$1 != this.settings.options().generateStructures()) {
            this.settings = this.settings.withOptions(p_267945_ -> p_267945_.withStructures($$1));
        }
        for (Consumer<WorldCreationUiState> $$2 : this.listeners) {
            $$2.accept(this);
        }
    }

    public void setName(String string0) {
        this.name = string0;
        this.targetFolder = this.findResultFolder(string0);
        this.onChanged();
    }

    private String findResultFolder(String string0) {
        String $$1 = string0.trim();
        try {
            return FileUtil.findAvailableName(this.savesFolder, !$$1.isEmpty() ? $$1 : DEFAULT_WORLD_NAME.getString(), "");
        } catch (Exception var5) {
            try {
                return FileUtil.findAvailableName(this.savesFolder, "World", "");
            } catch (IOException var4) {
                throw new RuntimeException("Could not create save folder", var4);
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public String getTargetFolder() {
        return this.targetFolder;
    }

    public void setGameMode(WorldCreationUiState.SelectedGameMode worldCreationUiStateSelectedGameMode0) {
        this.gameMode = worldCreationUiStateSelectedGameMode0;
        this.onChanged();
    }

    public WorldCreationUiState.SelectedGameMode getGameMode() {
        return this.isDebug() ? WorldCreationUiState.SelectedGameMode.DEBUG : this.gameMode;
    }

    public void setDifficulty(Difficulty difficulty0) {
        this.difficulty = difficulty0;
        this.onChanged();
    }

    public Difficulty getDifficulty() {
        return this.isHardcore() ? Difficulty.HARD : this.difficulty;
    }

    public boolean isHardcore() {
        return this.getGameMode() == WorldCreationUiState.SelectedGameMode.HARDCORE;
    }

    public void setAllowCheats(boolean boolean0) {
        this.allowCheats = boolean0;
        this.onChanged();
    }

    public boolean isAllowCheats() {
        if (this.isDebug()) {
            return true;
        } else if (this.isHardcore()) {
            return false;
        } else {
            return this.allowCheats == null ? this.getGameMode() == WorldCreationUiState.SelectedGameMode.CREATIVE : this.allowCheats;
        }
    }

    public void setSeed(String string0) {
        this.seed = string0;
        this.settings = this.settings.withOptions(p_267957_ -> p_267957_.withSeed(WorldOptions.parseSeed(this.getSeed())));
        this.onChanged();
    }

    public String getSeed() {
        return this.seed;
    }

    public void setGenerateStructures(boolean boolean0) {
        this.generateStructures = boolean0;
        this.onChanged();
    }

    public boolean isGenerateStructures() {
        return this.isDebug() ? false : this.generateStructures;
    }

    public void setBonusChest(boolean boolean0) {
        this.bonusChest = boolean0;
        this.onChanged();
    }

    public boolean isBonusChest() {
        return !this.isDebug() && !this.isHardcore() ? this.bonusChest : false;
    }

    public void setSettings(WorldCreationContext worldCreationContext0) {
        this.settings = worldCreationContext0;
        this.updatePresetLists();
        this.onChanged();
    }

    public WorldCreationContext getSettings() {
        return this.settings;
    }

    public void updateDimensions(WorldCreationContext.DimensionsUpdater worldCreationContextDimensionsUpdater0) {
        this.settings = this.settings.withDimensions(worldCreationContextDimensionsUpdater0);
        this.onChanged();
    }

    protected boolean tryUpdateDataConfiguration(WorldDataConfiguration worldDataConfiguration0) {
        WorldDataConfiguration $$1 = this.settings.dataConfiguration();
        if ($$1.dataPacks().getEnabled().equals(worldDataConfiguration0.dataPacks().getEnabled()) && $$1.enabledFeatures().equals(worldDataConfiguration0.enabledFeatures())) {
            this.settings = new WorldCreationContext(this.settings.options(), this.settings.datapackDimensions(), this.settings.selectedDimensions(), this.settings.worldgenRegistries(), this.settings.dataPackResources(), worldDataConfiguration0);
            return true;
        } else {
            return false;
        }
    }

    public boolean isDebug() {
        return this.settings.selectedDimensions().isDebug();
    }

    public void setWorldType(WorldCreationUiState.WorldTypeEntry worldCreationUiStateWorldTypeEntry0) {
        this.worldType = worldCreationUiStateWorldTypeEntry0;
        Holder<WorldPreset> $$1 = worldCreationUiStateWorldTypeEntry0.preset();
        if ($$1 != null) {
            this.updateDimensions((p_268134_, p_268035_) -> $$1.value().createWorldDimensions());
        }
    }

    public WorldCreationUiState.WorldTypeEntry getWorldType() {
        return this.worldType;
    }

    @Nullable
    public PresetEditor getPresetEditor() {
        Holder<WorldPreset> $$0 = this.getWorldType().preset();
        return $$0 != null ? (PresetEditor) PresetEditor.EDITORS.get($$0.unwrapKey()) : null;
    }

    public List<WorldCreationUiState.WorldTypeEntry> getNormalPresetList() {
        return this.normalPresetList;
    }

    public List<WorldCreationUiState.WorldTypeEntry> getAltPresetList() {
        return this.altPresetList;
    }

    private void updatePresetLists() {
        Registry<WorldPreset> $$0 = this.getSettings().worldgenLoadContext().m_175515_(Registries.WORLD_PRESET);
        this.normalPresetList.clear();
        this.normalPresetList.addAll((Collection) getNonEmptyList($$0, WorldPresetTags.NORMAL).orElseGet(() -> $$0.holders().map(WorldCreationUiState.WorldTypeEntry::new).toList()));
        this.altPresetList.clear();
        this.altPresetList.addAll((Collection) getNonEmptyList($$0, WorldPresetTags.EXTENDED).orElse(this.normalPresetList));
        Holder<WorldPreset> $$1 = this.worldType.preset();
        if ($$1 != null) {
            this.worldType = (WorldCreationUiState.WorldTypeEntry) findPreset(this.getSettings(), $$1.unwrapKey()).map(WorldCreationUiState.WorldTypeEntry::new).orElse((WorldCreationUiState.WorldTypeEntry) this.normalPresetList.get(0));
        }
    }

    private static Optional<Holder<WorldPreset>> findPreset(WorldCreationContext worldCreationContext0, Optional<ResourceKey<WorldPreset>> optionalResourceKeyWorldPreset1) {
        return optionalResourceKeyWorldPreset1.flatMap(p_267974_ -> worldCreationContext0.worldgenLoadContext().m_175515_(Registries.WORLD_PRESET).getHolder(p_267974_));
    }

    private static Optional<List<WorldCreationUiState.WorldTypeEntry>> getNonEmptyList(Registry<WorldPreset> registryWorldPreset0, TagKey<WorldPreset> tagKeyWorldPreset1) {
        return registryWorldPreset0.getTag(tagKeyWorldPreset1).map(p_268149_ -> p_268149_.m_203614_().map(WorldCreationUiState.WorldTypeEntry::new).toList()).filter(p_268066_ -> !p_268066_.isEmpty());
    }

    public void setGameRules(GameRules gameRules0) {
        this.gameRules = gameRules0;
        this.onChanged();
    }

    public GameRules getGameRules() {
        return this.gameRules;
    }

    public static enum SelectedGameMode {

        SURVIVAL("survival", GameType.SURVIVAL), HARDCORE("hardcore", GameType.SURVIVAL), CREATIVE("creative", GameType.CREATIVE), DEBUG("spectator", GameType.SPECTATOR);

        public final GameType gameType;

        public final Component displayName;

        private final Component info;

        private SelectedGameMode(String p_268033_, GameType p_268252_) {
            this.gameType = p_268252_;
            this.displayName = Component.translatable("selectWorld.gameMode." + p_268033_);
            this.info = Component.translatable("selectWorld.gameMode." + p_268033_ + ".info");
        }

        public Component getInfo() {
            return this.info;
        }
    }

    public static record WorldTypeEntry(@Nullable Holder<WorldPreset> f_267398_) {

        @Nullable
        private final Holder<WorldPreset> preset;

        private static final Component CUSTOM_WORLD_DESCRIPTION = Component.translatable("generator.custom");

        public WorldTypeEntry(@Nullable Holder<WorldPreset> f_267398_) {
            this.preset = f_267398_;
        }

        public Component describePreset() {
            return (Component) Optional.ofNullable(this.preset).flatMap(Holder::m_203543_).map(p_268048_ -> Component.translatable(p_268048_.location().toLanguageKey("generator"))).orElse(CUSTOM_WORLD_DESCRIPTION);
        }

        public boolean isAmplified() {
            return Optional.ofNullable(this.preset).flatMap(Holder::m_203543_).filter(p_268224_ -> p_268224_.equals(WorldPresets.AMPLIFIED)).isPresent();
        }
    }
}