package harmonised.pmmo.client.gui;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.client.gui.component.GuiEnumGroup;
import harmonised.pmmo.client.gui.component.SelectionWidget;
import harmonised.pmmo.config.SkillsConfig;
import harmonised.pmmo.core.CoreUtils;
import harmonised.pmmo.setup.datagen.LangProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class GlossarySelectScreen extends Screen {

    private static final ResourceLocation GUI_BG = new ResourceLocation("pmmo", "textures/gui/screenboxy.png");

    private SelectionWidget<SelectionWidget.SelectionEntry<GlossarySelectScreen.SELECTION>> selectSection;

    private SelectionWidget<SelectionWidget.SelectionEntry<GlossarySelectScreen.OBJECT>> selectObject;

    private SelectionWidget<SelectionWidget.SelectionEntry<String>> selectSkills;

    private SelectionWidget<SelectionWidget.SelectionEntry<GuiEnumGroup>> selectEnum;

    private Button viewButton;

    private GlossarySelectScreen.SELECTION selection;

    private GlossarySelectScreen.OBJECT object;

    private String skill = "";

    private GuiEnumGroup type;

    private int renderX;

    private int renderY;

    public GlossarySelectScreen() {
        super(Component.literal("pmmo_glossary"));
        this.init();
    }

    @Override
    protected void init() {
        this.renderX = this.f_96543_ / 2 - 128;
        this.renderY = this.f_96544_ / 2 - 128;
        this.selectSection = new SelectionWidget<>(this.f_96543_ / 2 - 100, this.renderY + 25, 200, LangProvider.GLOSSARY_DEFAULT_SECTION.asComponent(), this::updateSelection);
        this.selectSection.setEntries(GlossarySelectScreen.SELECTION.CHOICE_LIST);
        this.selectObject = new SelectionWidget<>(this.f_96543_ / 2 - 100, this.renderY + 50, 200, LangProvider.GLOSSARY_DEFAULT_OBJECT.asComponent(), sel -> {
            this.object = (GlossarySelectScreen.OBJECT) sel.reference;
            this.updateEnum(sel);
        });
        this.selectObject.f_93624_ = false;
        this.selectSkills = new SelectionWidget<>(this.f_96543_ / 2 - 100, this.renderY + 75, 200, LangProvider.GLOSSARY_DEFAULT_SKILL.asComponent(), sel -> this.skill = (String) sel.reference);
        this.selectSkills.setEntries(SkillsConfig.SKILLS.get().keySet().stream().sorted().map(skill -> new SelectionWidget.SelectionEntry<>(Component.translatable("pmmo." + skill).setStyle(CoreUtils.getSkillStyle(skill)), skill)).toList());
        this.selectSkills.f_93624_ = false;
        this.selectEnum = new SelectionWidget<>(this.f_96543_ / 2 - 100, this.renderY + 100, 200, LangProvider.GLOSSARY_DEFAULT_ENUM.asComponent(), sel -> this.type = (GuiEnumGroup) sel.reference);
        this.selectEnum.f_93624_ = false;
        this.viewButton = Button.builder(LangProvider.GLOSSARY_VIEW_BUTTON.asComponent(), button -> {
            if (this.selection != null && this.object != null) {
                Minecraft.getInstance().setScreen(new StatsScreen(this.selection, this.object, this.skill, this.type));
            }
        }).bounds(this.f_96543_ / 2 - 40, this.renderY + 125, 80, 20).build();
        this.viewButton.f_93624_ = false;
        this.m_142416_(this.viewButton);
        this.m_142416_(this.selectEnum);
        this.m_142416_(this.selectSkills);
        this.m_142416_(this.selectObject);
        this.m_142416_(this.selectSection);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        graphics.blit(GUI_BG, this.renderX, this.renderY, 0, 0, 256, 256);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrolled) {
        if (this.selectSection.isExtended()) {
            return this.selectSection.mouseScrolled(mouseX, mouseY, scrolled) || super.m_6050_(mouseX, mouseY, scrolled);
        } else if (this.selectObject.isExtended()) {
            return this.selectObject.mouseScrolled(mouseX, mouseY, scrolled) || super.m_6050_(mouseX, mouseY, scrolled);
        } else if (this.selectSkills.isExtended()) {
            return this.selectSkills.mouseScrolled(mouseX, mouseY, scrolled) || super.m_6050_(mouseX, mouseY, scrolled);
        } else {
            return !this.selectEnum.isExtended() ? super.m_6050_(mouseX, mouseY, scrolled) : this.selectEnum.mouseScrolled(mouseX, mouseY, scrolled) || super.m_6050_(mouseX, mouseY, scrolled);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
        if (this.selectSection.isExtended()) {
            return this.selectSection.mouseClicked(mouseX, mouseY, partialTicks) || super.m_6375_(mouseX, mouseY, partialTicks);
        } else if (this.selectObject.isExtended()) {
            return this.selectObject.mouseClicked(mouseX, mouseY, partialTicks) || super.m_6375_(mouseX, mouseY, partialTicks);
        } else if (this.selectSkills.isExtended()) {
            return this.selectSkills.mouseClicked(mouseX, mouseY, partialTicks) || super.m_6375_(mouseX, mouseY, partialTicks);
        } else {
            return this.selectEnum.isExtended() ? this.selectEnum.mouseClicked(mouseX, mouseY, partialTicks) || super.m_6375_(mouseX, mouseY, partialTicks) : this.viewButton.m_6375_(mouseX, mouseY, partialTicks) || super.m_6375_(mouseX, mouseY, partialTicks);
        }
    }

    private void updateSelection(SelectionWidget.SelectionEntry<GlossarySelectScreen.SELECTION> sel) {
        this.selection = sel.reference;
        this.selectObject.f_93624_ = true;
        this.selectObject.setEntries(this.selection.validObjects);
        this.selectSkills.f_93624_ = true;
        this.selectEnum.f_93624_ = this.selection != GlossarySelectScreen.SELECTION.SALVAGE && this.selection != GlossarySelectScreen.SELECTION.VEIN && this.selection != GlossarySelectScreen.SELECTION.PERKS;
        this.viewButton.f_93624_ = true;
    }

    private void updateEnum(SelectionWidget.SelectionEntry<GlossarySelectScreen.OBJECT> sel) {
        if (this.selection != null) {
            this.selectEnum.setEntries((Collection<SelectionWidget.SelectionEntry<GuiEnumGroup>>) (switch((GlossarySelectScreen.OBJECT) sel.reference) {
                case ITEMS ->
                    this.selection == GlossarySelectScreen.SELECTION.REQS ? this.enumToList(ReqType.ITEM_APPLICABLE_EVENTS) : (this.selection == GlossarySelectScreen.SELECTION.XP ? this.enumToList(EventType.ITEM_APPLICABLE_EVENTS) : (this.selection == GlossarySelectScreen.SELECTION.BONUS ? this.enumToList(new ModifierDataType[] { ModifierDataType.HELD, ModifierDataType.WORN }) : new ArrayList()));
                case BLOCKS ->
                    this.selection == GlossarySelectScreen.SELECTION.REQS ? this.enumToList(ReqType.BLOCK_APPLICABLE_EVENTS) : (this.selection == GlossarySelectScreen.SELECTION.XP ? this.enumToList(EventType.BLOCK_APPLICABLE_EVENTS) : new ArrayList());
                case ENTITY ->
                    this.selection == GlossarySelectScreen.SELECTION.REQS ? this.enumToList(ReqType.ENTITY_APPLICABLE_EVENTS) : (this.selection == GlossarySelectScreen.SELECTION.XP ? this.enumToList(EventType.ENTITY_APPLICABLE_EVENTS) : new ArrayList());
                case DIMENSIONS ->
                    this.selection == GlossarySelectScreen.SELECTION.REQS ? this.enumToList(new ReqType[] { ReqType.TRAVEL }) : (this.selection == GlossarySelectScreen.SELECTION.BONUS ? this.enumToList(new ModifierDataType[] { ModifierDataType.DIMENSION }) : new ArrayList());
                case BIOMES ->
                    this.selection == GlossarySelectScreen.SELECTION.REQS ? this.enumToList(new ReqType[] { ReqType.TRAVEL }) : (this.selection == GlossarySelectScreen.SELECTION.BONUS ? this.enumToList(new ModifierDataType[] { ModifierDataType.BIOME }) : new ArrayList());
                case ENCHANTS ->
                    this.selection == GlossarySelectScreen.SELECTION.REQS ? this.enumToList(new ReqType[] { ReqType.USE_ENCHANTMENT }) : new ArrayList();
                default ->
                    new ArrayList();
            }));
        }
    }

    private List<SelectionWidget.SelectionEntry<GuiEnumGroup>> enumToList(GuiEnumGroup[] array) {
        return Arrays.stream(array).map(val -> new SelectionWidget.SelectionEntry<>(Component.translatable("pmmo.enum." + val.getName()), val)).toList();
    }

    public static enum OBJECT {

        ITEMS(LangProvider.GLOSSARY_OBJECT_ITEMS.asComponent()),
        BLOCKS(LangProvider.GLOSSARY_OBJECT_BLOCKS.asComponent()),
        ENTITY(LangProvider.GLOSSARY_OBJECT_ENTITIES.asComponent()),
        DIMENSIONS(LangProvider.GLOSSARY_OBJECT_DIMENSIONS.asComponent()),
        BIOMES(LangProvider.GLOSSARY_OBJECT_BIOMES.asComponent()),
        ENCHANTS(LangProvider.GLOSSARY_OBJECT_ENCHANTS.asComponent()),
        EFFECTS(LangProvider.GLOSSARY_OBJECT_EFFECTS.asComponent()),
        PERKS(LangProvider.GLOSSARY_OBJECT_PERKS.asComponent());

        MutableComponent text;

        public static final List<SelectionWidget.SelectionEntry<GlossarySelectScreen.OBJECT>> CHOICE_LIST = Arrays.stream(values()).map(val -> new SelectionWidget.SelectionEntry<>(val.text, val)).toList();

        private OBJECT(MutableComponent text) {
            this.text = text;
        }
    }

    public static enum SELECTION {

        REQS(LangProvider.GLOSSARY_SECTION_REQ.asComponent(), Arrays.stream(GlossarySelectScreen.OBJECT.values()).map(obj -> new SelectionWidget.SelectionEntry<>(obj.text, obj)).toList()),
        XP(LangProvider.GLOSSARY_SECTION_XP.asComponent(), Arrays.stream(new GlossarySelectScreen.OBJECT[] { GlossarySelectScreen.OBJECT.ITEMS, GlossarySelectScreen.OBJECT.BLOCKS, GlossarySelectScreen.OBJECT.ENTITY, GlossarySelectScreen.OBJECT.EFFECTS }).map(obj -> new SelectionWidget.SelectionEntry<>(obj.text, obj)).toList()),
        BONUS(LangProvider.GLOSSARY_SECTION_BONUS.asComponent(), Arrays.stream(new GlossarySelectScreen.OBJECT[] { GlossarySelectScreen.OBJECT.ITEMS, GlossarySelectScreen.OBJECT.DIMENSIONS, GlossarySelectScreen.OBJECT.BIOMES }).map(obj -> new SelectionWidget.SelectionEntry<>(obj.text, obj)).toList()),
        SALVAGE,
        VEIN(LangProvider.GLOSSARY_SECTION_VEIN.asComponent(), Arrays.stream(new GlossarySelectScreen.OBJECT[] { GlossarySelectScreen.OBJECT.ITEMS, GlossarySelectScreen.OBJECT.BLOCKS, GlossarySelectScreen.OBJECT.DIMENSIONS, GlossarySelectScreen.OBJECT.BIOMES }).map(obj -> new SelectionWidget.SelectionEntry<>(obj.text, obj)).toList()),
        MOB_SCALING(LangProvider.GLOSSARY_SECTION_MOB.asComponent(), Arrays.stream(new GlossarySelectScreen.OBJECT[] { GlossarySelectScreen.OBJECT.DIMENSIONS, GlossarySelectScreen.OBJECT.BIOMES }).map(obj -> new SelectionWidget.SelectionEntry<>(obj.text, obj)).toList()),
        PERKS(LangProvider.GLOSSARY_SECTION_PERKS.asComponent(), Arrays.stream(new GlossarySelectScreen.OBJECT[] { GlossarySelectScreen.OBJECT.PERKS }).map(obj -> new SelectionWidget.SelectionEntry<>(obj.text, obj)).toList());

        MutableComponent text;

        List<SelectionWidget.SelectionEntry<GlossarySelectScreen.OBJECT>> validObjects;

        public static final List<SelectionWidget.SelectionEntry<GlossarySelectScreen.SELECTION>> CHOICE_LIST = Arrays.stream(values()).map(val -> new SelectionWidget.SelectionEntry<>(val.text, val)).toList();

        private SELECTION(MutableComponent text, List<SelectionWidget.SelectionEntry<GlossarySelectScreen.OBJECT>> validObjects) {
            this.text = text;
            this.validObjects = validObjects;
        }

        // $VF: Failed to inline enum fields
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        static {
            SALVAGE = new GlossarySelectScreen.SELECTION(LangProvider.GLOSSARY_SECTION_SALVAGE.asComponent(), List.of(new SelectionWidget.SelectionEntry<>(GlossarySelectScreen.OBJECT.ITEMS.text, GlossarySelectScreen.OBJECT.ITEMS)));
        }
    }
}