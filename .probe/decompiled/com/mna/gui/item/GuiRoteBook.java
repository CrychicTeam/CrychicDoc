package com.mna.gui.item;

import com.google.common.collect.UnmodifiableIterator;
import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.sound.SFX;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiable;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.gui.GuiTextures;
import com.mna.gui.base.SearchableGuiJeiDisable;
import com.mna.gui.block.GuiInscriptionTable;
import com.mna.gui.containers.item.ContainerRoteBook;
import com.mna.gui.widgets.AttributeButton;
import com.mna.gui.widgets.BorderedImageButton;
import com.mna.gui.widgets.ImageButtonWithAlphaBlend;
import com.mna.gui.widgets.RGBPicker;
import com.mna.gui.widgets.SpellPartList;
import com.mna.items.ItemInit;
import com.mna.items.SpellIconList;
import com.mna.items.sorcery.ItemSpell;
import com.mna.network.ClientMessageDispatcher;
import com.mna.tools.DidYouKnowHelper;
import com.mojang.datafixers.util.Pair;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.AbstractMap.SimpleEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

public class GuiRoteBook extends SearchableGuiJeiDisable<ContainerRoteBook> {

    private static final int MAX_COMPONENTS = 5;

    ImageButton[] pieceWidgets;

    ImageButton activeShapeButton;

    ImageButton[] activeComponentButtons;

    ArrayList<ImageButton> shapeAttributeButtons;

    HashMap<Integer, ArrayList<ImageButton>> componentAttributeButtons;

    ArrayList<ImageButton> inactiveCategoryButtons;

    ArrayList<ImageButton> activeCategoryButtons;

    private SpellPartList list;

    private Button nameAndIcon;

    final float textScaleFactor = 0.5F;

    static final int textColor = FastColor.ARGB32.color(255, 49, 49, 49);

    static final int textColorLight = FastColor.ARGB32.color(255, 200, 200, 200);

    final int colWidth = 38;

    final int rowHeight = 13;

    final int texSize = 32;

    final List<Component> currentTooltip;

    IPlayerRoteSpells playerRote = null;

    IPlayerProgression playerProgression = null;

    private boolean namingSpell = true;

    private int page = 0;

    private int numPages = 0;

    private int hoveredIndex;

    EditBox nameBox;

    Component nameValue;

    GuiRoteBook.ModelButton currentButton;

    RGBPicker colors;

    private static ArrayList<Pair<Integer, Integer>> pipLocations = new ArrayList();

    ArrayList<GuiRoteBook.ModelButton> iconButtons;

    private Button prev;

    private Button next;

    private Button clear;

    public GuiRoteBook(ContainerRoteBook screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.f_97726_ = 256;
        this.f_97727_ = 256;
        this.activeComponentButtons = new ImageButton[5];
        this.inactiveCategoryButtons = new ArrayList();
        this.activeCategoryButtons = new ArrayList();
        this.iconButtons = new ArrayList();
        this.numPages = (int) Math.floor((double) ((float) SpellIconList.ALL.length / 100.0F));
        this.currentTooltip = new ArrayList();
    }

    public void addTooltipLine(Component comp) {
        this.currentTooltip.add(comp);
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        return this.list.m_6050_(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
    }

    @Override
    protected void init() {
        this.m_169413_();
        this.activeCategoryButtons.clear();
        this.inactiveCategoryButtons.clear();
        super.m_7856_();
        this.initSpellIcons();
        int tbWidth = 130;
        int x = this.f_97735_ + this.f_97726_ / 2 - tbWidth / 2;
        int y = this.f_97736_ + this.f_97727_ - 95;
        this.initSearch(this.f_97735_ + 6, this.f_97736_ + 185, 59, 18);
        this.playerRote = (IPlayerRoteSpells) this.f_96541_.player.getCapability(PlayerRoteSpellsProvider.ROTE).orElse(null);
        this.playerProgression = (IPlayerProgression) this.f_96541_.player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        this.nameAndIcon = (Button) this.m_142416_(new ImageButton(this.f_97735_ + 228, this.f_97736_ + 12, 16, 16, 228, 12, 0, GuiTextures.Items.BOOK_OF_ROTE, 256, 256, button -> this.toggleNaming()));
        this.list = new SpellPartList(false, true, this.f_97735_, this.f_97736_, 66, 190, 117, 57, s -> this.OnShapeClicked(s), c -> this.OnComponentClicked(c), m -> {
        }, tt -> this.currentTooltip.add(tt));
        this.m_7787_(this.list);
        this.shapeAttributeButtons = new ArrayList();
        this.componentAttributeButtons = new HashMap();
        for (int i = 0; i < 5; i++) {
            this.componentAttributeButtons.put(i, new ArrayList());
        }
        int sliderX = this.f_97735_ - 35;
        this.colors = new RGBPicker(sliderX, this.f_97736_ + 211, this::adjustSpellColors);
        this.m_142416_(this.colors);
        this.mapUIToCurrentSpell();
        x = this.f_97735_ + 256;
        y = this.f_97736_ + 6;
        for (int i = 0; i < ((ContainerRoteBook) this.f_97732_).getSize(); i++) {
            int idx = i;
            if (i < 8) {
                this.inactiveCategoryButtons.add((ImageButton) this.m_142416_(new GuiRoteBook.IndexButton(x, y, 37, 22, 0, 0, 22, GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, 256, 256, btn -> {
                    this.setActiveIndex(idx);
                    ((ContainerRoteBook) this.f_97732_).changeIndex(idx);
                    ItemSpell.setCustomIcon(this.currentButton.icon, ((ContainerRoteBook) this.f_97732_).getIconIndex());
                    this.mapUIToCurrentSpell();
                }, i + 1, false)));
                this.activeCategoryButtons.add((ImageButton) this.m_142416_(new GuiRoteBook.IndexButton(x, y, 37, 22, 0, 44, 22, GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, 256, 256, btn -> {
                }, i + 1, true)));
            } else {
                this.inactiveCategoryButtons.add((ImageButton) this.m_142416_(new GuiRoteBook.IndexButton(x, y, 37, 22, 37, 0, 22, GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, 256, 256, btn -> {
                    this.setActiveIndex(idx);
                    ((ContainerRoteBook) this.f_97732_).changeIndex(idx);
                    ItemSpell.setCustomIcon(this.currentButton.icon, ((ContainerRoteBook) this.f_97732_).getIconIndex());
                    this.mapUIToCurrentSpell();
                }, i + 1, false)));
                this.activeCategoryButtons.add((ImageButton) this.m_142416_(new GuiRoteBook.IndexButton(x, y, 37, 22, 37, 44, 22, GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, 256, 256, btn -> {
                }, i + 1, true)));
            }
            y += 22;
            if (i == 7) {
                x = this.f_97735_ - 37;
                y = this.f_97736_ + 6;
            }
        }
        this.setActiveIndex(((ContainerRoteBook) this.f_97732_).getActiveIndex());
        this.namingSpell = true;
        this.toggleNaming();
    }

    private void initSpellIcons() {
        int nameWidth = 218;
        int nameHeight = 256;
        int i = this.f_97735_ + this.f_97726_ / 2 - nameWidth / 2;
        int j = this.f_97736_ + this.f_97727_ / 2 - nameHeight / 2;
        this.currentButton = new GuiRoteBook.ModelButton(this.f_97735_ + 12, j + 12, -1, btn -> {
        });
        this.m_142416_(this.currentButton);
        this.nameBox = new EditBox(this.f_96541_.font, i + nameWidth / 2 - 80, j + 10, 160, 20, this.nameValue);
        this.nameBox.setValue(((ContainerRoteBook) this.f_97732_).getName());
        this.nameBox.setMaxLength(60);
        this.nameBox.setResponder(this::nameChanged);
        this.m_142416_(this.nameBox);
        this.nameBox.setCanLoseFocus(false);
        this.nameBox.setFocused(true);
        this.m_7522_(this.nameBox);
        this.page = 0;
        this.clear = (Button) this.m_142416_(new ImageButton(this.f_97735_ + this.f_97726_ - 28, this.f_97736_ + 12, 15, 18, 222, 7, 0, GuiTextures.Items.SPELL_CUSTOMIZE, 256, 256, button -> this.toggleNaming()));
        this.prev = (Button) this.m_142416_(new ImageButton(this.f_97735_ + 9, this.f_97736_ + this.f_97727_ - 18, 9, 14, 247, 31, 14, GuiTextures.Items.SPELL_CUSTOMIZE, 256, 256, button -> {
            this.page--;
            if (this.page <= 0) {
                this.page = 0;
                this.prev.f_93623_ = false;
            }
            this.next.f_93623_ = true;
            this.initIconButtons();
            this.m_7522_(this.nameBox);
        }));
        this.next = (Button) this.m_142416_(new ImageButton(this.f_97735_ + this.f_97726_ - 17, this.f_97736_ + this.f_97727_ - 18, 9, 14, 247, 60, 14, GuiTextures.Items.SPELL_CUSTOMIZE, 256, 256, button -> {
            this.page++;
            if (this.page >= this.numPages) {
                this.page = this.numPages;
                this.next.f_93623_ = false;
            }
            this.prev.f_93623_ = true;
            this.initIconButtons();
            this.m_7522_(this.nameBox);
        }));
        this.prev.f_93623_ = false;
        i += 3;
        j += 20;
        this.initIconButtons();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            if (this.namingSpell) {
                this.toggleNaming();
            } else {
                this.m_7379_();
            }
            return true;
        } else if (this.nameBox.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            return this.nameBox.m_93696_() && this.nameBox.isVisible() && keyCode != 256 ? true : super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    private void adjustSpellColors(Integer[] argb) {
        ((ContainerRoteBook) this.f_97732_).setCurColor(FastColor.ARGB32.color(argb[3], argb[0], argb[1], argb[2]));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (this.namingSpell) {
            this.m_7522_(this.nameBox);
            if (button == 1 && this.nameBox.isMouseOver(mouseX, mouseY)) {
                this.nameBox.setValue("");
            }
        } else if (button == 1 && this.searchBox.isMouseOver(mouseX, mouseY)) {
            this.searchBox.setValue("");
        }
        return false;
    }

    private void toggleNaming() {
        if (!this.namingSpell) {
            this.namingSpell = true;
            this.resetActiveSpell();
            this.list._active = false;
            this.nameAndIcon.f_93623_ = false;
            this.nameAndIcon.f_93624_ = false;
            this.searchBox.f_93623_ = false;
            this.searchBox.f_93624_ = false;
            this.colors.f_93623_ = false;
            this.colors.f_93624_ = false;
            this.page = 0;
            this.prev.f_93623_ = false;
            this.prev.f_93624_ = true;
            this.next.f_93623_ = true;
            this.next.f_93624_ = true;
            this.clear.f_93623_ = true;
            this.clear.f_93624_ = true;
            for (GuiRoteBook.ModelButton btn : this.iconButtons) {
                btn.f_93623_ = true;
                btn.f_93624_ = true;
            }
            this.nameBox.f_93623_ = true;
            this.nameBox.f_93624_ = true;
            if (this.activeShapeButton != null) {
                this.activeShapeButton.f_93623_ = false;
                this.activeShapeButton.f_93624_ = false;
            }
            if (this.activeComponentButtons != null) {
                for (int i = 0; i < this.activeComponentButtons.length; i++) {
                    if (this.activeComponentButtons[i] != null) {
                        this.activeComponentButtons[i].f_93623_ = false;
                        this.activeComponentButtons[i].f_93624_ = false;
                    }
                }
            }
        } else {
            this.namingSpell = false;
            this.page = 0;
            this.prev.f_93623_ = false;
            this.prev.f_93624_ = false;
            this.next.f_93623_ = false;
            this.next.f_93624_ = false;
            this.clear.f_93623_ = false;
            this.clear.f_93624_ = false;
            for (GuiRoteBook.ModelButton btn : this.iconButtons) {
                btn.f_93623_ = false;
                btn.f_93624_ = false;
            }
            this.nameBox.f_93623_ = false;
            this.nameBox.f_93624_ = false;
            this.nameAndIcon.f_93623_ = true;
            this.nameAndIcon.f_93624_ = true;
            this.searchBox.f_93623_ = true;
            this.searchBox.f_93624_ = true;
            this.list._active = true;
            this.colors.f_93623_ = true;
            this.colors.f_93624_ = true;
            this.mapUIToCurrentSpell();
        }
    }

    private void nameChanged(String newName) {
        ((ContainerRoteBook) this.f_97732_).setName(newName);
    }

    @Override
    protected void searchTermChanged(String newName) {
        this.currentSearchTerm = newName.toLowerCase();
        this.list.clear();
        this.list.reInit(this.currentSearchTerm);
    }

    private void initIconButtons() {
        int nameHeight = 256;
        int top = this.f_97736_ + this.f_97727_ / 2 - nameHeight / 2;
        for (GuiRoteBook.ModelButton button : this.iconButtons) {
            this.m_169411_(button);
        }
        this.iconButtons.clear();
        int x = this.f_97735_ + 26;
        int y = top + 45;
        int count = 1;
        for (int i = this.page * 100; i < SpellIconList.ALL.length; i++) {
            int idx = i;
            GuiRoteBook.ModelButton btn = new GuiRoteBook.ModelButton(x, y, i, button -> {
                ((ContainerRoteBook) this.f_97732_).setIconIndex(idx);
                ItemSpell.setCustomIcon(this.currentButton.icon, idx);
            });
            this.m_142416_(btn);
            this.iconButtons.add(btn);
            x += 21;
            if (count % 10 == 0 && count != 0) {
                x = this.f_97735_ + 26;
                y += 21;
            }
            if (count == 100) {
                break;
            }
            count++;
        }
        ItemSpell.setCustomIcon(this.currentButton.icon, ((ContainerRoteBook) this.f_97732_).getIconIndex());
    }

    private void setActiveIndex(int index) {
        if (index >= 0 && index < this.activeCategoryButtons.size()) {
            this.activeCategoryButtons.forEach(b -> {
                b.f_93623_ = false;
                b.f_93624_ = false;
            });
            this.inactiveCategoryButtons.forEach(b -> {
                b.f_93623_ = true;
                b.f_93624_ = true;
            });
            ((ImageButton) this.activeCategoryButtons.get(index)).f_93623_ = true;
            ((ImageButton) this.activeCategoryButtons.get(index)).f_93624_ = true;
            ((ImageButton) this.inactiveCategoryButtons.get(index)).f_93623_ = false;
            ((ImageButton) this.inactiveCategoryButtons.get(index)).f_93624_ = false;
        }
    }

    private void resetActiveSpell() {
        this.setupShapeAttributeButtons(null);
        if (this.activeShapeButton != null) {
            this.m_169411_(this.activeShapeButton);
        }
        for (int index : this.componentAttributeButtons.keySet()) {
            this.clearAttributeButtons((ArrayList<ImageButton>) this.componentAttributeButtons.get(index));
        }
    }

    private void mapUIToCurrentSpell() {
        this.resetActiveSpell();
        for (int i = 0; i < this.activeComponentButtons.length; i++) {
            this.m_169411_(this.activeComponentButtons[i]);
            this.activeComponentButtons[i] = null;
        }
        if (((ContainerRoteBook) this.f_97732_).getShape() != null) {
            this.setupShapeWidgetsFor(((ContainerRoteBook) this.f_97732_).getShape().getPart());
        }
        for (int i = 0; i < 5; i++) {
            if (((ContainerRoteBook) this.f_97732_).getComponent(i) != null) {
                this.setupComponentWidgetsFor(((ContainerRoteBook) this.f_97732_).getComponent(i).getPart(), i);
            }
        }
        this.nameBox.setValue(((ContainerRoteBook) this.f_97732_).getName());
        int color = ((ContainerRoteBook) this.f_97732_).getCurColor();
        this.colors.setValue(FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), FastColor.ARGB32.alpha(color));
    }

    private void setupShapeAttributeButtons(Shape shape) {
        this.clearAttributeButtons(this.shapeAttributeButtons);
        if (shape != null) {
            this.setupAttributeButtons(60, 48, this.shapeAttributeButtons, shape);
        }
    }

    private void setupAttributeButtons(int xStart, int yStart, ArrayList<ImageButton> addTo, IModifiable<? extends ISpellComponent> modifiable) {
        int count = 0;
        for (UnmodifiableIterator var6 = modifiable.getModifiableAttributes().iterator(); var6.hasNext(); count++) {
            AttributeValuePair attribute = (AttributeValuePair) var6.next();
            boolean attributeModifiable = false;
            for (Modifier m : ((IForgeRegistry) Registries.Modifier.get()).getValues()) {
                if (m.modifiesType(attribute.getAttribute()) && (this.playerRote.isRote(m) && this.playerProgression.getTier() >= m.getTier(ManaAndArtifice.instance.proxy.getClientWorld()) || this.f_96541_.player.m_7500_())) {
                    attributeModifiable = true;
                    break;
                }
            }
            int row = (int) Math.floor((double) (count / 2));
            int col = count % 2;
            int xOffset = xStart + 38 * col;
            int yOffset = yStart + 13 * row;
            if (attributeModifiable) {
                ImageButtonWithAlphaBlend upButton = (ImageButtonWithAlphaBlend) this.m_142416_(new ImageButtonWithAlphaBlend(this.f_97735_ + xOffset + 1, this.f_97736_ + yOffset - 3, 8, 5, 0, 48, 0, GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, 128, 128, button -> {
                    ((ContainerRoteBook) this.f_97732_).increaseAttribute(this.f_96541_.player, modifiable, attribute.getAttribute(), this.f_96541_.level, Screen.hasShiftDown());
                    GuiInscriptionTable.checkAndShowShiftTooltip();
                }));
                ImageButtonWithAlphaBlend downButton = (ImageButtonWithAlphaBlend) this.m_142416_(new ImageButtonWithAlphaBlend(this.f_97735_ + xOffset + 1, this.f_97736_ + yOffset + 2, 8, 5, 0, 53, 0, GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, 128, 128, button -> {
                    ((ContainerRoteBook) this.f_97732_).decreaseAttribute(this.f_96541_.player, modifiable, attribute.getAttribute(), this.f_96541_.level, Screen.hasShiftDown());
                    GuiInscriptionTable.checkAndShowShiftTooltip();
                }));
                addTo.add(upButton);
                addTo.add(downButton);
            }
            Point texCoord = (Point) GuiTextures.Attribute_Icon_Mappings.get(attribute.getAttribute());
            int blitSize = 8;
            float scaleFactor = (float) blitSize / 52.0F;
            ImageButton iconButton = (ImageButton) this.m_142416_(new AttributeButton(this.f_97735_ + xOffset - 8, this.f_97736_ + yOffset - 2, blitSize, blitSize, (int) ((float) texCoord.x * scaleFactor), (int) ((float) texCoord.y * scaleFactor), 0, GuiTextures.Widgets.ATTRIBUTE_ICONS, (int) (208.0F * scaleFactor), (int) (208.0F * scaleFactor), button -> {
            }, new String[] { attribute.getAttribute().getLocaleKey(), ((ISpellComponent) modifiable).getDescriptionTooltip(attribute.getAttribute()) }, this::addTooltipLine));
            addTo.add(iconButton);
        }
    }

    private void clearAttributeButtons(ArrayList<ImageButton> attributeButtons) {
        for (ImageButton button : attributeButtons) {
            this.m_169411_(button);
        }
        attributeButtons.clear();
    }

    private void setupShapeWidgetsFor(Shape shape) {
        int texSize = 32;
        if (shape != null) {
            if (this.activeShapeButton != null) {
                this.m_169411_(this.activeShapeButton);
            }
            this.activeShapeButton = (ImageButton) this.m_142416_(new BorderedImageButton(this.f_97735_ + 13, this.f_97736_ + 47, texSize, texSize, 0, 0, 0, shape.getGuiIcon(), texSize, texSize, button -> {
                ((ContainerRoteBook) this.f_97732_).setShape(null);
                this.mapUIToCurrentSpell();
            }).setBorder(shape.isSilverSpell()));
        }
        this.setupShapeAttributeButtons(shape);
    }

    private void OnShapeClicked(Shape shape) {
        ((ContainerRoteBook) this.f_97732_).setShape(shape);
        this.mapUIToCurrentSpell();
        if (this.searchBox != null) {
            this.searchBox.setHighlightPos(0);
        }
    }

    private void setupComponentWidgetsFor(SpellEffect component, int index) {
        int texSize = 32;
        if (component != null) {
            if (this.activeComponentButtons[index] != null) {
                this.m_169411_(this.activeComponentButtons[index]);
            }
            SimpleEntry<Integer, Integer> coords = this.getComponentRenderCoordinates(index);
            this.activeComponentButtons[index] = (ImageButton) this.m_142416_(new BorderedImageButton((Integer) coords.getKey(), (Integer) coords.getValue(), 32, 32, 0, 0, 0, component.getGuiIcon(), 32, 32, button -> {
                ((ContainerRoteBook) this.f_97732_).removeComponent(index);
                this.mapUIToCurrentSpell();
            }).setBorder(component.isSilverSpell()));
        }
        this.setupComponentAttributeButtons(component, index);
    }

    private void setupComponentAttributeButtons(SpellEffect component, int index) {
        this.clearAttributeButtons((ArrayList<ImageButton>) this.componentAttributeButtons.get(index));
        if (component != null) {
            SimpleEntry<Integer, Integer> coords = this.getComponentRenderCoordinates(index, -this.f_97735_ + 47, -this.f_97736_ + 1);
            this.setupAttributeButtons((Integer) coords.getKey(), (Integer) coords.getValue(), (ArrayList<ImageButton>) this.componentAttributeButtons.get(index), component);
        }
    }

    private void OnComponentClicked(SpellEffect component) {
        if (this.searchBox != null) {
            this.searchBox.setHighlightPos(0);
        }
        for (int i = 0; i < 5; i++) {
            if (((ContainerRoteBook) this.f_97732_).getComponent(i) == null) {
                ((ContainerRoteBook) this.f_97732_).addComponent(component);
                this.mapUIToCurrentSpell();
                return;
            }
        }
    }

    private SimpleEntry<Integer, Integer> getComponentRenderCoordinates(int index) {
        return this.getComponentRenderCoordinates(index, 0, 0);
    }

    private SimpleEntry<Integer, Integer> getComponentRenderCoordinates(int index, int offsetX, int offsetY) {
        int x = this.f_97735_ + 13;
        int y = this.f_97736_ + 94;
        if (index == 1 || index == 4) {
            y += 47;
        }
        if (index == 2) {
            y -= 47;
        }
        if (index > 1) {
            x += 124;
        }
        return new SimpleEntry(x + offsetX, y + offsetY);
    }

    @Override
    public void removed() {
        this.checkAndShowDamageStackingTip();
        super.m_7861_();
        ((ContainerRoteBook) this.f_97732_).copySpellChangesToInventory();
        ClientMessageDispatcher.sendRoteSpellsUpdate((ContainerRoteBook) this.f_97732_);
    }

    private void checkAndShowDamageStackingTip() {
        Minecraft mc = Minecraft.getInstance();
        if (((ContainerRoteBook) this.f_97732_).countNonDelayedDamageComponents() > 1) {
            DidYouKnowHelper.CheckAndShowDidYouKnow(mc.player, "helptip.mna.damage_type_stacking");
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.currentTooltip.clear();
        this.hoveredIndex = -1;
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        if (this.currentTooltip.size() > 0) {
            pGuiGraphics.renderTooltip(this.f_96547_, this.currentTooltip, Optional.empty(), mouseX, mouseY);
        }
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        if (!this.namingSpell) {
            int xPos = 71;
            int yPos = 48;
            int count = 0;
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
            if (((ContainerRoteBook) this.f_97732_).getShape() != null) {
                for (UnmodifiableIterator header = ((ContainerRoteBook) this.f_97732_).getShape().getContainedAttributes().iterator(); header.hasNext(); count++) {
                    Attribute attribute = (Attribute) header.next();
                    int row = (int) Math.floor((double) (count / 2));
                    int col = count % 2;
                    this.drawAttributeValue(pGuiGraphics, xPos + col * 38, yPos + row * 13, ((ContainerRoteBook) this.f_97732_).getShape().getValueWithoutMultipliers(attribute));
                }
            }
            for (int i = 0; i < 5; i++) {
                SimpleEntry<Integer, Integer> coords = this.getComponentRenderCoordinates(i, -this.f_97735_ + 58, -this.f_97736_ + 1);
                xPos = (Integer) coords.getKey();
                yPos = (Integer) coords.getValue();
                count = 0;
                if (((ContainerRoteBook) this.f_97732_).getComponent(i) != null) {
                    for (UnmodifiableIterator var28 = ((ContainerRoteBook) this.f_97732_).getComponent(i).getContainedAttributes().iterator(); var28.hasNext(); count++) {
                        Attribute attribute = (Attribute) var28.next();
                        int row = (int) Math.floor((double) (count / 2));
                        int col = count % 2;
                        this.drawAttributeValue(pGuiGraphics, xPos + col * 38, yPos + row * 13, ((ContainerRoteBook) this.f_97732_).getComponent(i).getValueWithoutMultipliers(attribute));
                    }
                }
            }
            String header = I18n.get("gui.mna.mana_cost") + ":";
            String manaCost = String.format("%.1f", ((ContainerRoteBook) this.f_97732_).getManaCost(this.f_96541_.player));
            String complexityHeader = I18n.get("gui.mna.complexity") + ":";
            String complexity = String.format("%.1f / %d", ((ContainerRoteBook) this.f_97732_).getComplexity(this.f_96541_.player), this.playerProgression.getTierMaxComplexity());
            float manaCostWidth = (float) this.f_96547_.width(manaCost) * 0.5F;
            float headerWidth = (float) this.f_96547_.width(header) * 0.5F;
            float complexityHeaderWidth = (float) this.f_96547_.width(complexityHeader) * 0.5F;
            float padding = 2.0F;
            float summaryXPos = 35.0F;
            float summaryYPos = 25.0F;
            pGuiGraphics.drawString(this.f_96547_, complexityHeader, (int) (summaryXPos / 0.5F), (int) (summaryYPos / 0.5F), textColor, false);
            summaryXPos += complexityHeaderWidth + padding;
            if (((ContainerRoteBook) this.f_97732_).getComplexity(this.f_96541_.player) <= (float) this.playerProgression.getTierMaxComplexity()) {
                pGuiGraphics.drawString(this.f_96547_, complexity, (int) (summaryXPos / 0.5F), (int) (summaryYPos / 0.5F), textColor, false);
            } else {
                pGuiGraphics.drawString(this.f_96547_, complexity, (int) (summaryXPos / 0.5F), (int) (summaryYPos / 0.5F), ChatFormatting.RED.getColor(), false);
            }
            summaryXPos = (float) (this.f_97726_ - 33) - headerWidth - manaCostWidth - padding;
            summaryYPos = 25.0F;
            pGuiGraphics.drawString(this.f_96547_, header, (int) (summaryXPos / 0.5F), (int) (summaryYPos / 0.5F), textColor, false);
            summaryXPos += headerWidth + padding;
            pGuiGraphics.drawString(this.f_96547_, manaCost, (int) (summaryXPos / 0.5F), (int) (summaryYPos / 0.5F), textColor, false);
            pGuiGraphics.pose().popPose();
            MutableComponent curSpellName = Component.literal(((ContainerRoteBook) this.f_97732_).getName());
            int curNameWidth = this.f_96547_.width(curSpellName);
            pGuiGraphics.drawString(this.f_96547_, curSpellName, this.f_97726_ / 2 - curNameWidth / 2, 12, textColor, false);
            for (int d = 0; d < 16; d++) {
                Pair<Integer, Integer> pip = (Pair<Integer, Integer>) pipLocations.get(d);
                if (((ContainerRoteBook) this.f_97732_).getActiveIndex() == d) {
                    pGuiGraphics.blit(GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, (Integer) pip.getFirst(), (Integer) pip.getSecond(), 0.0F, 126.0F, 2, 2, 256, 256);
                }
                if (this.hoveredIndex == d) {
                    pGuiGraphics.blit(GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, (Integer) pip.getFirst(), (Integer) pip.getSecond(), 0.0F, 124.0F, 2, 2, 256, 256);
                }
            }
        }
    }

    private void drawAttributeValue(GuiGraphics pGuiGraphics, int x, int y, float value) {
        String valueString = String.format("%.1f", value);
        pGuiGraphics.drawString(this.f_96547_, valueString, (int) ((float) x / 0.5F), (int) ((float) y / 0.5F), textColor, false);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        int i = this.f_97735_;
        int j = this.f_97736_;
        pGuiGraphics.blit(GuiTextures.Items.BOOK_OF_ROTE, i, j, 0, 0, this.f_97726_, this.f_97727_);
        pGuiGraphics.blit(GuiTextures.Items.BOOK_OF_ROTE, i + 54, j + 178, 54, 178, 148, 78);
        this.list.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        if (this.namingSpell) {
            int bannerWidth = 246;
            int bannerHeight = 30;
            pGuiGraphics.blit(GuiTextures.Items.SPELL_CUSTOMIZE, i + this.f_97726_ / 2 - bannerWidth / 2, j + 5, 0.0F, 0.0F, bannerWidth, bannerHeight, this.f_97726_, this.f_97727_);
            int iconsWidth = 219;
            int iconsHeight = 219;
            pGuiGraphics.blit(GuiTextures.Items.SPELL_CUSTOMIZE, i + this.f_97726_ / 2 - iconsWidth / 2, j + 38, 0.0F, 31.0F, iconsWidth, iconsHeight, this.f_97726_, this.f_97727_);
        }
    }

    @Override
    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        if (this.list.isScrolling()) {
            return this.list.m_7979_(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
        } else {
            return this.colors.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_) ? true : super.m_7979_(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
        }
    }

    static {
        pipLocations.add(new Pair(224, 202));
        pipLocations.add(new Pair(230, 203));
        pipLocations.add(new Pair(236, 206));
        pipLocations.add(new Pair(239, 212));
        pipLocations.add(new Pair(240, 218));
        pipLocations.add(new Pair(239, 224));
        pipLocations.add(new Pair(236, 230));
        pipLocations.add(new Pair(230, 233));
        pipLocations.add(new Pair(224, 234));
        pipLocations.add(new Pair(218, 233));
        pipLocations.add(new Pair(212, 230));
        pipLocations.add(new Pair(209, 224));
        pipLocations.add(new Pair(208, 218));
        pipLocations.add(new Pair(209, 212));
        pipLocations.add(new Pair(212, 206));
        pipLocations.add(new Pair(218, 203));
    }

    public class IndexButton extends ImageButton {

        boolean isActive;

        int index;

        public IndexButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int hoverOffset, ResourceLocation textureFile, int texWidth, int texHeight, Button.OnPress clickHandler, int index, boolean active) {
            super(x, y, width, height, xTexStart, yTexStart, hoverOffset, textureFile, texWidth, texHeight, clickHandler, Component.literal(""));
            this.index = index;
            this.isActive = active;
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int mousex, int mousey, float partialTick) {
            super.renderWidget(pGuiGraphics, mousex, mousey, partialTick);
            if (this.f_93623_ && this.f_93624_) {
                boolean left = this.index > 8;
                ItemStack spellStack = ((ContainerRoteBook) GuiRoteBook.this.f_97732_).getStack(this.index - 1);
                pGuiGraphics.renderItem(spellStack, left ? this.m_252754_() + 18 : this.m_252754_() + 3, this.m_252907_() + 3);
                if (this.m_198029_()) {
                    GuiRoteBook.this.hoveredIndex = this.index - 1;
                }
            }
        }

        @Override
        public void playDownSound(SoundManager soundHandler) {
            soundHandler.play(SimpleSoundInstance.forUI(SFX.Gui.PAGE_FLIP, (float) (0.8 + Math.random() * 0.4)));
        }
    }

    class ModelButton extends Button {

        public ItemStack icon = new ItemStack(ItemInit.SPELL.get());

        public ModelButton(int x, int y, int index, Button.OnPress pressedAction) {
            super(x, y, 16, 16, Component.literal(""), pressedAction, f_252438_);
            ItemSpell.setCustomIcon(this.icon, index);
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
            pGuiGraphics.renderItem(this.icon, this.m_252754_(), this.m_252907_());
        }

        @Override
        public void setFocused(boolean focus) {
        }
    }
}