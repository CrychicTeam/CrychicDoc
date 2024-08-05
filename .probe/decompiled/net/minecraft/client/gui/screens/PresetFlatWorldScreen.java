package net.minecraft.client.gui.screens;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FlatLevelGeneratorPresetTags;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.slf4j.Logger;

public class PresetFlatWorldScreen extends Screen {

    static final Logger LOGGER = LogUtils.getLogger();

    private static final int SLOT_TEX_SIZE = 128;

    private static final int SLOT_BG_SIZE = 18;

    private static final int SLOT_STAT_HEIGHT = 20;

    private static final int SLOT_BG_X = 1;

    private static final int SLOT_BG_Y = 1;

    private static final int SLOT_FG_X = 2;

    private static final int SLOT_FG_Y = 2;

    private static final ResourceKey<Biome> DEFAULT_BIOME = Biomes.PLAINS;

    public static final Component UNKNOWN_PRESET = Component.translatable("flat_world_preset.unknown");

    private final CreateFlatWorldScreen parent;

    private Component shareText;

    private Component listText;

    private PresetFlatWorldScreen.PresetsList list;

    private Button selectButton;

    EditBox export;

    FlatLevelGeneratorSettings settings;

    public PresetFlatWorldScreen(CreateFlatWorldScreen createFlatWorldScreen0) {
        super(Component.translatable("createWorld.customize.presets.title"));
        this.parent = createFlatWorldScreen0;
    }

    @Nullable
    private static FlatLayerInfo getLayerInfoFromString(HolderGetter<Block> holderGetterBlock0, String string1, int int2) {
        List<String> $$3 = Splitter.on('*').limit(2).splitToList(string1);
        int $$5;
        String $$4;
        if ($$3.size() == 2) {
            $$4 = (String) $$3.get(1);
            try {
                $$5 = Math.max(Integer.parseInt((String) $$3.get(0)), 0);
            } catch (NumberFormatException var11) {
                LOGGER.error("Error while parsing flat world string", var11);
                return null;
            }
        } else {
            $$4 = (String) $$3.get(0);
            $$5 = 1;
        }
        int $$9 = Math.min(int2 + $$5, DimensionType.Y_SIZE);
        int $$10 = $$9 - int2;
        Optional<Holder.Reference<Block>> $$11;
        try {
            $$11 = holderGetterBlock0.get(ResourceKey.create(Registries.BLOCK, new ResourceLocation($$4)));
        } catch (Exception var10) {
            LOGGER.error("Error while parsing flat world string", var10);
            return null;
        }
        if ($$11.isEmpty()) {
            LOGGER.error("Error while parsing flat world string => Unknown block, {}", $$4);
            return null;
        } else {
            return new FlatLayerInfo($$10, (Block) ((Holder.Reference) $$11.get()).value());
        }
    }

    private static List<FlatLayerInfo> getLayersInfoFromString(HolderGetter<Block> holderGetterBlock0, String string1) {
        List<FlatLayerInfo> $$2 = Lists.newArrayList();
        String[] $$3 = string1.split(",");
        int $$4 = 0;
        for (String $$5 : $$3) {
            FlatLayerInfo $$6 = getLayerInfoFromString(holderGetterBlock0, $$5, $$4);
            if ($$6 == null) {
                return Collections.emptyList();
            }
            $$2.add($$6);
            $$4 += $$6.getHeight();
        }
        return $$2;
    }

    public static FlatLevelGeneratorSettings fromString(HolderGetter<Block> holderGetterBlock0, HolderGetter<Biome> holderGetterBiome1, HolderGetter<StructureSet> holderGetterStructureSet2, HolderGetter<PlacedFeature> holderGetterPlacedFeature3, String string4, FlatLevelGeneratorSettings flatLevelGeneratorSettings5) {
        Iterator<String> $$6 = Splitter.on(';').split(string4).iterator();
        if (!$$6.hasNext()) {
            return FlatLevelGeneratorSettings.getDefault(holderGetterBiome1, holderGetterStructureSet2, holderGetterPlacedFeature3);
        } else {
            List<FlatLayerInfo> $$7 = getLayersInfoFromString(holderGetterBlock0, (String) $$6.next());
            if ($$7.isEmpty()) {
                return FlatLevelGeneratorSettings.getDefault(holderGetterBiome1, holderGetterStructureSet2, holderGetterPlacedFeature3);
            } else {
                Holder.Reference<Biome> $$8 = holderGetterBiome1.getOrThrow(DEFAULT_BIOME);
                Holder<Biome> $$9 = $$8;
                if ($$6.hasNext()) {
                    String $$10 = (String) $$6.next();
                    $$9 = (Holder<Biome>) Optional.ofNullable(ResourceLocation.tryParse($$10)).map(p_258126_ -> ResourceKey.create(Registries.BIOME, p_258126_)).flatMap(holderGetterBiome1::m_254902_).orElseGet(() -> {
                        LOGGER.warn("Invalid biome: {}", $$10);
                        return $$8;
                    });
                }
                return flatLevelGeneratorSettings5.withBiomeAndLayers($$7, flatLevelGeneratorSettings5.structureOverrides(), $$9);
            }
        }
    }

    static String save(FlatLevelGeneratorSettings flatLevelGeneratorSettings0) {
        StringBuilder $$1 = new StringBuilder();
        for (int $$2 = 0; $$2 < flatLevelGeneratorSettings0.getLayersInfo().size(); $$2++) {
            if ($$2 > 0) {
                $$1.append(",");
            }
            $$1.append(flatLevelGeneratorSettings0.getLayersInfo().get($$2));
        }
        $$1.append(";");
        $$1.append(flatLevelGeneratorSettings0.getBiome().unwrapKey().map(ResourceKey::m_135782_).orElseThrow(() -> new IllegalStateException("Biome not registered")));
        return $$1.toString();
    }

    @Override
    protected void init() {
        this.shareText = Component.translatable("createWorld.customize.presets.share");
        this.listText = Component.translatable("createWorld.customize.presets.list");
        this.export = new EditBox(this.f_96547_, 50, 40, this.f_96543_ - 100, 20, this.shareText);
        this.export.setMaxLength(1230);
        WorldCreationContext $$0 = this.parent.parent.getUiState().getSettings();
        RegistryAccess $$1 = $$0.worldgenLoadContext();
        FeatureFlagSet $$2 = $$0.dataConfiguration().enabledFeatures();
        HolderGetter<Biome> $$3 = $$1.m_255025_(Registries.BIOME);
        HolderGetter<StructureSet> $$4 = $$1.m_255025_(Registries.STRUCTURE_SET);
        HolderGetter<PlacedFeature> $$5 = $$1.m_255025_(Registries.PLACED_FEATURE);
        HolderGetter<Block> $$6 = $$1.m_255025_(Registries.BLOCK).filterFeatures($$2);
        this.export.setValue(save(this.parent.settings()));
        this.settings = this.parent.settings();
        this.m_7787_(this.export);
        this.list = new PresetFlatWorldScreen.PresetsList($$1, $$2);
        this.m_7787_(this.list);
        this.selectButton = (Button) this.m_142416_(Button.builder(Component.translatable("createWorld.customize.presets.select"), p_280822_ -> {
            FlatLevelGeneratorSettings $$5x = fromString($$6, $$3, $$4, $$5, this.export.getValue(), this.settings);
            this.parent.setConfig($$5x);
            this.f_96541_.setScreen(this.parent);
        }).bounds(this.f_96543_ / 2 - 155, this.f_96544_ - 28, 150, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_280823_ -> this.f_96541_.setScreen(this.parent)).bounds(this.f_96543_ / 2 + 5, this.f_96544_ - 28, 150, 20).build());
        this.updateButtonValidity(this.list.m_93511_() != null);
    }

    @Override
    public boolean mouseScrolled(double double0, double double1, double double2) {
        return this.list.m_6050_(double0, double1, double2);
    }

    @Override
    public void resize(Minecraft minecraft0, int int1, int int2) {
        String $$3 = this.export.getValue();
        this.m_6575_(minecraft0, int1, int2);
        this.export.setValue($$3);
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.parent);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.list.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate(0.0F, 0.0F, 400.0F);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 8, 16777215);
        guiGraphics0.drawString(this.f_96547_, this.shareText, 50, 30, 10526880);
        guiGraphics0.drawString(this.f_96547_, this.listText, 50, 70, 10526880);
        guiGraphics0.pose().popPose();
        this.export.m_88315_(guiGraphics0, int1, int2, float3);
        super.render(guiGraphics0, int1, int2, float3);
    }

    @Override
    public void tick() {
        this.export.tick();
        super.tick();
    }

    public void updateButtonValidity(boolean boolean0) {
        this.selectButton.f_93623_ = boolean0 || this.export.getValue().length() > 1;
    }

    class PresetsList extends ObjectSelectionList<PresetFlatWorldScreen.PresetsList.Entry> {

        public PresetsList(RegistryAccess registryAccess0, FeatureFlagSet featureFlagSet1) {
            super(PresetFlatWorldScreen.this.f_96541_, PresetFlatWorldScreen.this.f_96543_, PresetFlatWorldScreen.this.f_96544_, 80, PresetFlatWorldScreen.this.f_96544_ - 37, 24);
            for (Holder<FlatLevelGeneratorPreset> $$2 : registryAccess0.registryOrThrow(Registries.FLAT_LEVEL_GENERATOR_PRESET).getTagOrEmpty(FlatLevelGeneratorPresetTags.VISIBLE)) {
                Set<Block> $$3 = (Set<Block>) $$2.value().settings().getLayersInfo().stream().map(p_259579_ -> p_259579_.getBlockState().m_60734_()).filter(p_259421_ -> !p_259421_.m_245993_(featureFlagSet1)).collect(Collectors.toSet());
                if (!$$3.isEmpty()) {
                    PresetFlatWorldScreen.LOGGER.info("Discarding flat world preset {} since it contains experimental blocks {}", $$2.unwrapKey().map(p_259357_ -> p_259357_.location().toString()).orElse("<unknown>"), $$3);
                } else {
                    this.m_7085_(new PresetFlatWorldScreen.PresetsList.Entry($$2));
                }
            }
        }

        public void setSelected(@Nullable PresetFlatWorldScreen.PresetsList.Entry presetFlatWorldScreenPresetsListEntry0) {
            super.m_6987_(presetFlatWorldScreenPresetsListEntry0);
            PresetFlatWorldScreen.this.updateButtonValidity(presetFlatWorldScreenPresetsListEntry0 != null);
        }

        @Override
        public boolean keyPressed(int int0, int int1, int int2) {
            if (super.m_7933_(int0, int1, int2)) {
                return true;
            } else {
                if (CommonInputs.selected(int0) && this.m_93511_() != null) {
                    ((PresetFlatWorldScreen.PresetsList.Entry) this.m_93511_()).select();
                }
                return false;
            }
        }

        public class Entry extends ObjectSelectionList.Entry<PresetFlatWorldScreen.PresetsList.Entry> {

            private static final ResourceLocation STATS_ICON_LOCATION = new ResourceLocation("textures/gui/container/stats_icons.png");

            private final FlatLevelGeneratorPreset preset;

            private final Component name;

            public Entry(Holder<FlatLevelGeneratorPreset> holderFlatLevelGeneratorPreset0) {
                this.preset = holderFlatLevelGeneratorPreset0.value();
                this.name = (Component) holderFlatLevelGeneratorPreset0.unwrapKey().map(p_232760_ -> Component.translatable(p_232760_.location().toLanguageKey("flat_world_preset"))).orElse(PresetFlatWorldScreen.UNKNOWN_PRESET);
            }

            @Override
            public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
                this.blitSlot(guiGraphics0, int3, int2, this.preset.displayItem().value());
                guiGraphics0.drawString(PresetFlatWorldScreen.this.f_96547_, this.name, int3 + 18 + 5, int2 + 6, 16777215, false);
            }

            @Override
            public boolean mouseClicked(double double0, double double1, int int2) {
                if (int2 == 0) {
                    this.select();
                }
                return false;
            }

            void select() {
                PresetsList.this.setSelected(this);
                PresetFlatWorldScreen.this.settings = this.preset.settings();
                PresetFlatWorldScreen.this.export.setValue(PresetFlatWorldScreen.save(PresetFlatWorldScreen.this.settings));
                PresetFlatWorldScreen.this.export.moveCursorToStart();
            }

            private void blitSlot(GuiGraphics guiGraphics0, int int1, int int2, Item item3) {
                this.blitSlotBg(guiGraphics0, int1 + 1, int2 + 1);
                guiGraphics0.renderFakeItem(new ItemStack(item3), int1 + 2, int2 + 2);
            }

            private void blitSlotBg(GuiGraphics guiGraphics0, int int1, int int2) {
                guiGraphics0.blit(STATS_ICON_LOCATION, int1, int2, 0, 0.0F, 0.0F, 18, 18, 128, 128);
            }

            @Override
            public Component getNarration() {
                return Component.translatable("narrator.select", this.name);
            }
        }
    }
}