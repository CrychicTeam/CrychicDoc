package net.minecraftforge.client.gui;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringUtil;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.gui.widget.ModListWidget;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeI18n;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.common.util.Size2i;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.VersionChecker.CheckResult;
import net.minecraftforge.fml.VersionChecker.Status;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.StringUtils;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.resource.PathPackResources;
import net.minecraftforge.resource.ResourcePackLoader;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ComparableVersion;

public class ModListScreen extends Screen {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int PADDING = 6;

    private Screen parentScreen;

    private ModListWidget modList;

    private ModListScreen.InfoPanel modInfo;

    private ModListWidget.ModEntry selected = null;

    private int listWidth;

    private List<IModInfo> mods;

    private final List<IModInfo> unsortedMods;

    private Button configButton;

    private Button openModsFolderButton;

    private Button doneButton;

    private int buttonMargin = 1;

    private int numButtons = ModListScreen.SortType.values().length;

    private String lastFilterText = "";

    private EditBox search;

    private boolean sorted = false;

    private ModListScreen.SortType sortType = ModListScreen.SortType.NORMAL;

    private static String stripControlCodes(String value) {
        return StringUtil.stripColor(value);
    }

    public ModListScreen(Screen parentScreen) {
        super(Component.translatable("fml.menu.mods.title"));
        this.parentScreen = parentScreen;
        this.mods = Collections.unmodifiableList(ModList.get().getMods());
        this.unsortedMods = Collections.unmodifiableList(this.mods);
    }

    @Override
    public void init() {
        for (IModInfo mod : this.mods) {
            this.listWidth = Math.max(this.listWidth, this.getFontRenderer().width(mod.getDisplayName()) + 10);
            this.listWidth = Math.max(this.listWidth, this.getFontRenderer().width(MavenVersionStringHelper.artifactVersionToString(mod.getVersion())) + 5);
        }
        this.listWidth = Math.max(Math.min(this.listWidth, this.f_96543_ / 3), 100);
        this.listWidth = this.listWidth + (this.listWidth % this.numButtons != 0 ? this.numButtons - this.listWidth % this.numButtons : 0);
        int modInfoWidth = this.f_96543_ - this.listWidth - 18;
        int doneButtonWidth = Math.min(modInfoWidth, 200);
        int y = this.f_96544_ - 20 - 6;
        int fullButtonHeight = 32;
        this.doneButton = Button.builder(Component.translatable("gui.done"), b -> this.onClose()).bounds((this.listWidth + 6 + this.f_96543_ - doneButtonWidth) / 2, y, doneButtonWidth, 20).build();
        this.openModsFolderButton = Button.builder(Component.translatable("fml.menu.mods.openmodsfolder"), b -> Util.getPlatform().openFile(FMLPaths.MODSDIR.get().toFile())).bounds(6, y, this.listWidth, 20).build();
        y -= 26;
        this.configButton = Button.builder(Component.translatable("fml.menu.mods.config"), b -> this.displayModConfig()).bounds(6, y, this.listWidth, 20).build();
        y -= 20;
        this.search = new EditBox(this.getFontRenderer(), 7, y, this.listWidth - 2, 14, Component.translatable("fml.menu.mods.search"));
        this.modList = new ModListWidget(this, this.listWidth, fullButtonHeight, this.search.m_252907_() - 9 - 6);
        this.modList.m_93507_(6);
        this.modInfo = new ModListScreen.InfoPanel(this.f_96541_, modInfoWidth, this.f_96544_ - 6 - fullButtonHeight, 6);
        this.m_142416_(this.modList);
        this.m_142416_(this.modInfo);
        this.m_142416_(this.search);
        this.m_142416_(this.doneButton);
        this.m_142416_(this.configButton);
        this.m_142416_(this.openModsFolderButton);
        this.search.setFocused(false);
        this.search.setCanLoseFocus(true);
        this.configButton.f_93623_ = false;
        int width = this.listWidth / this.numButtons;
        int x = 6;
        this.m_142416_(ModListScreen.SortType.NORMAL.button = Button.builder(ModListScreen.SortType.NORMAL.getButtonText(), b -> this.resortMods(ModListScreen.SortType.NORMAL)).bounds(x, 6, width - this.buttonMargin, 20).build());
        x += width + this.buttonMargin;
        this.m_142416_(ModListScreen.SortType.A_TO_Z.button = Button.builder(ModListScreen.SortType.A_TO_Z.getButtonText(), b -> this.resortMods(ModListScreen.SortType.A_TO_Z)).bounds(x, 6, width - this.buttonMargin, 20).build());
        x += width + this.buttonMargin;
        this.m_142416_(ModListScreen.SortType.Z_TO_A.button = Button.builder(ModListScreen.SortType.Z_TO_A.getButtonText(), b -> this.resortMods(ModListScreen.SortType.Z_TO_A)).bounds(x, 6, width - this.buttonMargin, 20).build());
        this.resortMods(ModListScreen.SortType.NORMAL);
        this.updateCache();
    }

    private void displayModConfig() {
        if (this.selected != null) {
            try {
                ConfigScreenHandler.getScreenFactoryFor(this.selected.getInfo()).map(f -> (Screen) f.apply(this.f_96541_, this)).ifPresent(newScreen -> this.f_96541_.setScreen(newScreen));
            } catch (Exception var2) {
                LOGGER.error("There was a critical issue trying to build the config GUI for {}", this.selected.getInfo().getModId(), var2);
            }
        }
    }

    @Override
    public void tick() {
        this.search.tick();
        this.modList.m_6987_(this.selected);
        if (!this.search.getValue().equals(this.lastFilterText)) {
            this.reloadMods();
            this.sorted = false;
        }
        if (!this.sorted) {
            this.reloadMods();
            this.mods.sort(this.sortType);
            this.modList.refreshList();
            if (this.selected != null) {
                this.selected = (ModListWidget.ModEntry) this.modList.m_6702_().stream().filter(e -> e.getInfo() == this.selected.getInfo()).findFirst().orElse(null);
                this.updateCache();
            }
            this.sorted = true;
        }
    }

    public <T extends ObjectSelectionList.Entry<T>> void buildModList(Consumer<T> modListViewConsumer, Function<IModInfo, T> newEntry) {
        this.mods.forEach(mod -> modListViewConsumer.accept((ObjectSelectionList.Entry) newEntry.apply(mod)));
    }

    private void reloadMods() {
        this.mods = (List<IModInfo>) this.unsortedMods.stream().filter(mi -> StringUtils.toLowerCase(stripControlCodes(mi.getDisplayName())).contains(StringUtils.toLowerCase(this.search.getValue()))).collect(Collectors.toList());
        this.lastFilterText = this.search.getValue();
    }

    private void resortMods(ModListScreen.SortType newSort) {
        this.sortType = newSort;
        for (ModListScreen.SortType sort : ModListScreen.SortType.values()) {
            if (sort.button != null) {
                sort.button.f_93623_ = this.sortType != sort;
            }
        }
        this.sorted = false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.modList.m_88315_(guiGraphics, mouseX, mouseY, partialTick);
        if (this.modInfo != null) {
            this.modInfo.m_88315_(guiGraphics, mouseX, mouseY, partialTick);
        }
        Component text = Component.translatable("fml.menu.mods.search");
        int x = this.modList.getLeft() + (this.modList.getRight() - this.modList.getLeft()) / 2 - this.getFontRenderer().width(text) / 2;
        this.search.m_88315_(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawString(this.getFontRenderer(), text.getVisualOrderText(), x, this.search.m_252907_() - 9, 16777215, false);
    }

    public Minecraft getMinecraftInstance() {
        return this.f_96541_;
    }

    public Font getFontRenderer() {
        return this.f_96547_;
    }

    public void setSelected(ModListWidget.ModEntry entry) {
        this.selected = entry == this.selected ? null : entry;
        this.updateCache();
    }

    private void updateCache() {
        if (this.selected == null) {
            this.configButton.f_93623_ = false;
            this.modInfo.clearInfo();
        } else {
            IModInfo selectedMod = this.selected.getInfo();
            this.configButton.f_93623_ = ConfigScreenHandler.getScreenFactoryFor(selectedMod).isPresent();
            List<String> lines = new ArrayList();
            CheckResult vercheck = VersionChecker.getResult(selectedMod);
            Pair<ResourceLocation, Size2i> logoData = (Pair<ResourceLocation, Size2i>) selectedMod.getLogoFile().map(logoFile -> {
                TextureManager tm = this.f_96541_.getTextureManager();
                PathPackResources resourcePack = (PathPackResources) ResourcePackLoader.getPackFor(selectedMod.getModId()).orElse((PathPackResources) ResourcePackLoader.getPackFor("forge").orElseThrow(() -> new RuntimeException("Can't find forge, WHAT!")));
                try {
                    NativeImage logo = null;
                    IoSupplier<InputStream> logoResource = resourcePack.getRootResource(logoFile);
                    if (logoResource != null) {
                        logo = NativeImage.read(logoResource.get());
                    }
                    if (logo != null) {
                        return Pair.of(tm.register("modlogo", new DynamicTexture(logo) {

                            @Override
                            public void upload() {
                                this.m_117966_();
                                NativeImage td = this.m_117991_();
                                this.m_117991_().upload(0, 0, 0, 0, 0, td.getWidth(), td.getHeight(), selectedMod.getLogoBlur(), false, false, false);
                            }
                        }), new Size2i(logo.getWidth(), logo.getHeight()));
                    }
                } catch (IOException var7) {
                }
                return Pair.of(null, new Size2i(0, 0));
            }).orElse(Pair.of(null, new Size2i(0, 0)));
            lines.add(selectedMod.getDisplayName());
            lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.version", MavenVersionStringHelper.artifactVersionToString(selectedMod.getVersion())));
            lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.idstate", selectedMod.getModId(), ModList.get().getModContainerById(selectedMod.getModId()).map(ModContainer::getCurrentState).map(Object::toString).orElse("NONE")));
            selectedMod.getConfig().getConfigElement(new String[] { "credits" }).ifPresent(credits -> lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.credits", credits)));
            selectedMod.getConfig().getConfigElement(new String[] { "authors" }).ifPresent(authors -> lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.authors", authors)));
            selectedMod.getConfig().getConfigElement(new String[] { "displayURL" }).ifPresent(displayURL -> lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.displayurl", displayURL)));
            if (selectedMod.getOwningFile() != null && selectedMod.getOwningFile().getMods().size() != 1) {
                lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.childmods", selectedMod.getOwningFile().getMods().stream().map(IModInfo::getDisplayName).collect(Collectors.joining(","))));
            } else {
                lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.nochildmods"));
            }
            if (vercheck.status() == Status.OUTDATED || vercheck.status() == Status.BETA_OUTDATED) {
                lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.updateavailable", vercheck.url() == null ? "" : vercheck.url()));
            }
            lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.license", ((ModFileInfo) selectedMod.getOwningFile()).getLicense()));
            lines.add(null);
            lines.add(selectedMod.getDescription());
            if ((vercheck.status() == Status.OUTDATED || vercheck.status() == Status.BETA_OUTDATED) && vercheck.changes().size() > 0) {
                lines.add(null);
                lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.changelogheader"));
                for (Entry<ComparableVersion, String> entry : vercheck.changes().entrySet()) {
                    lines.add("  " + entry.getKey() + ":");
                    lines.add((String) entry.getValue());
                    lines.add(null);
                }
            }
            this.modInfo.setInfo(lines, (ResourceLocation) logoData.getLeft(), (Size2i) logoData.getRight());
        }
    }

    @Override
    public void resize(Minecraft mc, int width, int height) {
        String s = this.search.getValue();
        ModListScreen.SortType sort = this.sortType;
        ModListWidget.ModEntry selected = this.selected;
        this.m_6575_(mc, width, height);
        this.search.setValue(s);
        this.selected = selected;
        if (!this.search.getValue().isEmpty()) {
            this.reloadMods();
        }
        if (sort != ModListScreen.SortType.NORMAL) {
            this.resortMods(sort);
        }
        this.updateCache();
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.parentScreen);
    }

    class InfoPanel extends ScrollPanel {

        private ResourceLocation logoPath;

        private Size2i logoDims = new Size2i(0, 0);

        private List<FormattedCharSequence> lines = Collections.emptyList();

        InfoPanel(Minecraft mcIn, int widthIn, int heightIn, int topIn) {
            super(mcIn, widthIn, heightIn, topIn, ModListScreen.this.modList.getRight() + 6);
        }

        void setInfo(List<String> lines, ResourceLocation logoPath, Size2i logoDims) {
            this.logoPath = logoPath;
            this.logoDims = logoDims;
            this.lines = this.resizeContent(lines);
        }

        void clearInfo() {
            this.logoPath = null;
            this.logoDims = new Size2i(0, 0);
            this.lines = Collections.emptyList();
        }

        private List<FormattedCharSequence> resizeContent(List<String> lines) {
            List<FormattedCharSequence> ret = new ArrayList();
            for (String line : lines) {
                if (line == null) {
                    ret.add(null);
                } else {
                    Component chat = ForgeHooks.newChatWithLinks(line, false);
                    int maxTextLength = this.width - 12;
                    if (maxTextLength >= 0) {
                        ret.addAll(Language.getInstance().getVisualOrder(ModListScreen.this.f_96547_.getSplitter().splitLines(chat, maxTextLength, Style.EMPTY)));
                    }
                }
            }
            return ret;
        }

        @Override
        public int getContentHeight() {
            int height = 50;
            height += this.lines.size() * 9;
            if (height < this.bottom - this.top - 8) {
                height = this.bottom - this.top - 8;
            }
            return height;
        }

        @Override
        protected int getScrollAmount() {
            return 9 * 3;
        }

        @Override
        protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
            if (this.logoPath != null) {
                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                int headerHeight = 50;
                guiGraphics.blitInscribed(this.logoPath, this.left + 6, relativeY, this.width - 12, headerHeight, this.logoDims.width, this.logoDims.height, false, true);
                relativeY += headerHeight + 6;
            }
            for (FormattedCharSequence line : this.lines) {
                if (line != null) {
                    RenderSystem.enableBlend();
                    guiGraphics.drawString(ModListScreen.this.f_96547_, line, this.left + 6, relativeY, 16777215);
                    RenderSystem.disableBlend();
                }
                relativeY += 9;
            }
            Style component = this.findTextLine(mouseX, mouseY);
            if (component != null) {
                guiGraphics.renderComponentHoverEffect(ModListScreen.this.f_96547_, component, mouseX, mouseY);
            }
        }

        private Style findTextLine(int mouseX, int mouseY) {
            if (!this.m_5953_((double) mouseX, (double) mouseY)) {
                return null;
            } else {
                double offset = (double) ((float) (mouseY - this.top + this.border) + this.scrollDistance + 1.0F);
                if (this.logoPath != null) {
                    offset -= 50.0;
                }
                if (offset <= 0.0) {
                    return null;
                } else {
                    int lineIdx = (int) (offset / 9.0);
                    if (lineIdx < this.lines.size() && lineIdx >= 1) {
                        FormattedCharSequence line = (FormattedCharSequence) this.lines.get(lineIdx - 1);
                        return line != null ? ModListScreen.this.f_96547_.getSplitter().componentStyleAtWidth(line, mouseX - this.left - this.border) : null;
                    } else {
                        return null;
                    }
                }
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            Style component = this.findTextLine((int) mouseX, (int) mouseY);
            if (component != null) {
                ModListScreen.this.m_5561_(component);
                return true;
            } else {
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }

        @Override
        public NarratableEntry.NarrationPriority narrationPriority() {
            return NarratableEntry.NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput0) {
        }
    }

    private static enum SortType implements Comparator<IModInfo> {

        NORMAL, A_TO_Z {

            @Override
            protected int compare(String name1, String name2) {
                return name1.compareTo(name2);
            }
        }
        , Z_TO_A {

            @Override
            protected int compare(String name1, String name2) {
                return name2.compareTo(name1);
            }
        }
        ;

        Button button;

        protected int compare(String name1, String name2) {
            return 0;
        }

        public int compare(IModInfo o1, IModInfo o2) {
            String name1 = StringUtils.toLowerCase(ModListScreen.stripControlCodes(o1.getDisplayName()));
            String name2 = StringUtils.toLowerCase(ModListScreen.stripControlCodes(o2.getDisplayName()));
            return this.compare(name1, name2);
        }

        Component getButtonText() {
            return Component.translatable("fml.menu.mods." + StringUtils.toLowerCase(this.name()));
        }
    }
}