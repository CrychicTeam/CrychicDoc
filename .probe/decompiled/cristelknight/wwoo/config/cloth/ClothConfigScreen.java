package cristelknight.wwoo.config.cloth;

import cristelknight.wwoo.ExpandedEcosphere;
import cristelknight.wwoo.config.configs.EEConfig;
import cristelknight.wwoo.config.configs.ReplaceBiomesConfig;
import cristelknight.wwoo.terra.TerraInit;
import cristelknight.wwoo.utils.BiomeReplace;
import cristelknight.wwoo.utils.Util;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.cristellib.CristelLib;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ClothConfigScreen {

    public Screen create(Screen parent) {
        EEConfig config = EEConfig.DEFAULT.getConfig();
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setDefaultBackgroundTexture(new ResourceLocation(getIdentifier(config.backGroundBlock().m_60734_()))).setTitle(Component.translatable("expanded_ecosphere.config.title").withStyle(ChatFormatting.BOLD));
        ClothConfigScreen.ConfigEntries entries = new ClothConfigScreen.ConfigEntries(builder.entryBuilder(), builder.getOrCreateCategory(mainName("main")), builder.getOrCreateCategory(mainName("biomes")), builder.getOrCreateCategory(mainName("modes")));
        builder.setSavingRunnable(() -> {
            EEConfig.DEFAULT.setInstance(entries.createConfig());
            EEConfig.DEFAULT.getConfig(true, true);
            ReplaceBiomesConfig.DEFAULT.setInstance(entries.createBiomesConfig());
            ReplaceBiomesConfig config2 = ReplaceBiomesConfig.DEFAULT.getConfig(true, true);
            if (ExpandedEcosphere.isTerraBlenderLoaded()) {
                TerraInit.terraEnableDisable();
            }
            if (config2.enableBiomes() && ExpandedEcosphere.currentMode.equals(ExpandedEcosphere.Mode.DEFAULT)) {
                BiomeReplace.replace();
            } else {
                CristelLib.DATA_PACK.removeData(new ResourceLocation("minecraft", "dimension/overworld.json"));
            }
        });
        return builder.build();
    }

    private static Component fieldName(String id) {
        return Component.translatable("expanded_ecosphere.config.entry." + id);
    }

    private static Component mainName(String id) {
        return Component.translatable("expanded_ecosphere.config.category." + id);
    }

    private static Component fieldToolTip(String id) {
        return Component.translatable("expanded_ecosphere.config.entry." + id + ".toolTip");
    }

    private static String getIdentifier(Block b) {
        Stream<String> s = Arrays.stream(BuiltInRegistries.BLOCK.getKey(b).toString().split(":"));
        List<String> l = s.toList();
        String s2 = (String) l.get(1);
        if (b instanceof SnowyDirtBlock || b instanceof DoorBlock || b.equals(Blocks.CAKE) || b.equals(Blocks.LOOM)) {
            s2 = s2 + "_top";
        } else if (b.equals(Blocks.TNT)) {
            s2 = s2 + "_side";
        } else if (b.equals(Blocks.LAVA) || b.equals(Blocks.WATER)) {
            s2 = s2 + "_still";
        } else if (b instanceof FireBlock) {
            s2 = s2 + "_0";
        }
        return (String) l.get(0) + ":textures/block/" + s2 + ".png";
    }

    private static class ConfigEntries {

        private final ConfigEntryBuilder builder;

        private final BooleanListEntry removeOreBlobs;

        private final BooleanListEntry showUpdates;

        private final BooleanListEntry showBigUpdates;

        private final BooleanListEntry forceLargeBiomes;

        private final BooleanListEntry enableBiomes;

        @NotNull
        private final DropdownBoxEntry<Block> backgroundBlock;

        private final EnumListEntry<ExpandedEcosphere.Mode> mode;

        private final StringListListEntry biomeList;

        public ConfigEntries(ConfigEntryBuilder builder, ConfigCategory category1, ConfigCategory category2, ConfigCategory category3) {
            this.builder = builder;
            EEConfig config = EEConfig.DEFAULT.getConfig();
            if (!ExpandedEcosphere.isTerraBlenderLoaded()) {
                this.textListEntry(Component.translatable("expanded_ecosphere.config.text.requiresTerrablender", "3.0.0.169"), category3);
                this.textListEntry(Component.translatable("expanded_ecosphere.config.text.downloadTB").withStyle(s -> s.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/mod/terrablender"))), category3);
            }
            this.mode = builder.startEnumSelector(ClothConfigScreen.fieldName("selectMode"), ExpandedEcosphere.Mode.class, ExpandedEcosphere.currentMode).setDefaultValue(ExpandedEcosphere.Mode.DEFAULT).build();
            category3.addEntry(this.mode);
            this.textListEntry(Component.translatable("expanded_ecosphere.config.text.defaultMode").withStyle(ChatFormatting.GRAY), category3);
            this.textListEntry(Component.translatable("expanded_ecosphere.config.text.compatibleMode").withStyle(ChatFormatting.GRAY), category3);
            this.enableBiomes = this.createBooleanField("enableBiomes", ReplaceBiomesConfig.DEFAULT.getConfig().enableBiomes(), ReplaceBiomesConfig.DEFAULT.enableBiomes(), category2, new Component[0]);
            this.biomeList = builder.startStrList(ClothConfigScreen.fieldName("biomeList"), convertMapToList(ReplaceBiomesConfig.DEFAULT.getConfig().bannedBiomes())).setTooltip(ClothConfigScreen.fieldToolTip("biomeList")).setDefaultValue(List.of()).build();
            category2.addEntry(this.biomeList);
            this.textListEntry(Component.translatable("expanded_ecosphere.config.text.replaceBiomes").withStyle(ChatFormatting.GRAY), category2);
            this.textListEntry(Component.translatable("expanded_ecosphere.config.text.modes", Component.literal(ExpandedEcosphere.currentMode.toString()).withStyle(ChatFormatting.DARK_PURPLE)).withStyle(ChatFormatting.GRAY), category1);
            this.backgroundBlock = this.createBlockField("bB", config.backGroundBlock().m_60734_(), EEConfig.DEFAULT.backGroundBlock().m_60734_(), category1, List.of(ClothConfigScreen.FT.NO_BLOCK_ENTITY, ClothConfigScreen.FT.NO_BUTTON));
            this.showUpdates = this.createBooleanField("showUpdates", config.showUpdates(), EEConfig.DEFAULT.showUpdates(), category1, new Component[0]);
            this.showBigUpdates = this.createBooleanField("showBigUpdates", config.showBigUpdates(), EEConfig.DEFAULT.showBigUpdates(), category1, new Component[0]);
            this.removeOreBlobs = this.createBooleanField("removeOreBlobs", config.removeOreBlobs(), EEConfig.DEFAULT.removeOreBlobs(), category1, new Component[] { ClothConfigScreen.fieldToolTip("removeOreBlobs") });
            this.forceLargeBiomes = this.createBooleanField("forceLargeBiomes", config.forceLargeBiomes(), EEConfig.DEFAULT.forceLargeBiomes(), category1, new Component[0]);
            this.textListEntry(Util.translatableText("forceLargeBiomes").withStyle(ChatFormatting.GRAY), category1);
        }

        public EEConfig createConfig() {
            ExpandedEcosphere.Mode currentMode = (ExpandedEcosphere.Mode) this.mode.getValue();
            ExpandedEcosphere.currentMode = currentMode;
            return new EEConfig(currentMode.toString(), this.forceLargeBiomes.getValue(), this.removeOreBlobs.getValue(), this.showUpdates.getValue(), this.showBigUpdates.getValue(), this.backgroundBlock.getValue().defaultBlockState());
        }

        public ReplaceBiomesConfig createBiomesConfig() {
            return new ReplaceBiomesConfig(this.enableBiomes.getValue(), convertListToMap(this.biomeList.getValue()));
        }

        private static List<String> convertMapToList(Map<String, String> stringMap) {
            return (List<String>) stringMap.entrySet().stream().map(entry -> (String) entry.getKey() + "/" + (String) entry.getValue()).collect(Collectors.toList());
        }

        private static Map<String, String> convertListToMap(List<String> stringList) {
            return (Map<String, String>) stringList.stream().map(s -> s.split("/")).filter(parts -> parts.length == 2).collect(Collectors.toMap(parts -> parts[0], parts -> parts[1]));
        }

        private BooleanListEntry createBooleanField(String id, boolean value, boolean defaultValue, ConfigCategory category, Component[] tooltip) {
            BooleanListEntry e = this.builder.startBooleanToggle(ClothConfigScreen.fieldName(id), value).setDefaultValue(defaultValue).setTooltip(tooltip).build();
            category.addEntry(e);
            return e;
        }

        @NotNull
        private DropdownBoxEntry<Block> createBlockField(String id, Block value, Block defaultValue, ConfigCategory category, List<ClothConfigScreen.FT> filter) {
            DropdownMenuBuilder<Block> e = this.builder.startDropdownMenu(ClothConfigScreen.fieldName(id), DropdownMenuBuilder.TopCellElementBuilder.ofBlockObject(value), DropdownMenuBuilder.CellCreatorBuilder.ofBlockObject()).setDefaultValue(defaultValue).setSelections((Iterable<Block>) BuiltInRegistries.BLOCK.m_123024_().sorted(Comparator.comparing(Block::toString)).filter(new ClothConfigScreen.ConfigEntries.BlockPredicate(filter)).collect(Collectors.toCollection(LinkedHashSet::new)));
            DropdownBoxEntry<Block> entry = e.build();
            category.addEntry(entry);
            return entry;
        }

        public void textListEntry(Component component, ConfigCategory category) {
            TextListEntry tle = this.builder.startTextDescription(component).build();
            category.addEntry(tle);
        }

        static class BlockPredicate implements Predicate<Block> {

            private final List<ClothConfigScreen.FT> filters;

            public BlockPredicate(List<ClothConfigScreen.FT> filters) {
                this.filters = filters;
            }

            public boolean test(Block block) {
                boolean b = true;
                for (ClothConfigScreen.FT filter : this.filters) {
                    if (block instanceof AirBlock) {
                        b = false;
                    }
                    if (filter.equals(ClothConfigScreen.FT.NO_BUTTON) && block instanceof ButtonBlock) {
                        b = false;
                    }
                    if (filter.equals(ClothConfigScreen.FT.PILLAR) && !(block instanceof RotatedPillarBlock)) {
                        b = false;
                    }
                    if (filter.equals(ClothConfigScreen.FT.NO_BLOCK_ENTITY) && block.defaultBlockState().m_155947_()) {
                        b = false;
                    }
                }
                return b;
            }
        }
    }

    public static enum FT {

        NO_BUTTON, PILLAR, NO_BLOCK_ENTITY, NONE
    }
}