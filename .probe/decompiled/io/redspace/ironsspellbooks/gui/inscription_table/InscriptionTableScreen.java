package io.redspace.ironsspellbooks.gui.inscription_table;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.player.ClientRenderCache;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

public class InscriptionTableScreen extends AbstractContainerScreen<InscriptionTableMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/gui/inscription_table.png");

    private static final int INSCRIBE_BUTTON_X = 43;

    private static final int INSCRIBE_BUTTON_Y = 35;

    private static final int EXTRACT_BUTTON_X = 188;

    private static final int EXTRACT_BUTTON_Y = 137;

    private static final int SPELLBOOK_SLOT = 36;

    private static final int SCROLL_SLOT = 37;

    private static final int EXTRACTION_SLOT = 38;

    private static final int SPELL_BG_X = 67;

    private static final int SPELL_BG_Y = 15;

    private static final int SPELL_BG_WIDTH = 95;

    private static final int SPELL_BG_HEIGHT = 57;

    private static final int LORE_PAGE_X = 176;

    private static final int LORE_PAGE_WIDTH = 80;

    private boolean isDirty;

    protected Button inscribeButton;

    private ItemStack lastSpellBookItem = ItemStack.EMPTY;

    protected ArrayList<InscriptionTableScreen.SpellSlotInfo> spellSlots;

    private int selectedSpellIndex = -1;

    private int inscriptionErrorCode = 0;

    private final int[][] LAYOUT = ClientRenderCache.SPELL_LAYOUT;

    public InscriptionTableScreen(InscriptionTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.f_97726_ = 256;
        this.f_97727_ = 166;
    }

    @Override
    protected void init() {
        super.init();
        this.inscribeButton = (Button) this.m_7787_(Button.builder(CommonComponents.GUI_DONE, p_169820_ -> this.onInscription()).bounds(0, 0, 14, 14).build());
        this.spellSlots = new ArrayList();
        this.generateSpellSlots();
    }

    @Override
    public void onClose() {
        super.onClose();
        this.resetSelectedSpell();
    }

    @Override
    public void render(GuiGraphics guiHelper, int mouseX, int mouseY, float delta) {
        try {
            this.m_280273_(guiHelper);
            super.render(guiHelper, mouseX, mouseY, delta);
            this.m_280072_(guiHelper, mouseX, mouseY);
        } catch (Exception var6) {
            this.onClose();
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiHelper, float partialTick, int mouseX, int mouseY) {
        guiHelper.blit(TEXTURE, this.f_97735_, this.f_97736_, 0, 0, this.f_97726_, this.f_97727_);
        this.inscribeButton.f_93623_ = this.isValidInscription() && this.inscriptionErrorCode == 0;
        this.renderButtons(guiHelper, mouseX, mouseY);
        if (((Slot) ((InscriptionTableMenu) this.f_97732_).f_38839_.get(36)).getItem() != this.lastSpellBookItem) {
            this.onSpellBookSlotChanged();
            this.lastSpellBookItem = ((Slot) ((InscriptionTableMenu) this.f_97732_).f_38839_.get(36)).getItem();
        }
        this.renderSpells(guiHelper, mouseX, mouseY);
        this.renderLorePage(guiHelper, partialTick, mouseX, mouseY);
        if (((Slot) ((InscriptionTableMenu) this.f_97732_).f_38839_.get(36)).hasItem()) {
            this.inscriptionErrorCode = this.getErrorCode();
        } else {
            this.inscriptionErrorCode = 0;
        }
        if (this.inscriptionErrorCode > 0) {
            guiHelper.blit(TEXTURE, this.f_97735_ + 35, this.f_97736_ + 51, 0, 213, 28, 22);
            if (this.isHovering(this.f_97735_ + 35, this.f_97736_ + 51, 28, 22, mouseX, mouseY)) {
                guiHelper.renderTooltip(this.f_96547_, this.getErrorMessage(this.inscriptionErrorCode), mouseX, mouseY);
            }
        }
    }

    private int getErrorCode() {
        if (((InscriptionTableMenu) this.f_97732_).getSpellBookSlot().getItem().getItem() instanceof SpellBook spellbook && ((InscriptionTableMenu) this.f_97732_).getScrollSlot().getItem().getItem() instanceof Scroll scroll) {
            ISpellContainer scrollContainer = ISpellContainer.get(((InscriptionTableMenu) this.f_97732_).getScrollSlot().getItem());
            SpellData spellSlot = scrollContainer.getSpellAtIndex(0);
            if (spellbook.getRarity().compareRarity(spellSlot.getSpell().getRarity(spellSlot.getLevel())) < 0) {
                return 1;
            }
        }
        return 0;
    }

    private Component getErrorMessage(int code) {
        return code == 1 ? Component.translatable("ui.irons_spellbooks.inscription_table_rarity_error") : Component.empty();
    }

    private void renderSpells(GuiGraphics guiHelper, int mouseX, int mouseY) {
        if (this.isDirty) {
            this.generateSpellSlots();
        }
        Vec2 center = new Vec2((float) (67 + this.f_97735_ + 47), (float) (15 + this.f_97736_ + 28));
        for (int i = 0; i < this.spellSlots.size(); i++) {
            Button spellSlot = ((InscriptionTableScreen.SpellSlotInfo) this.spellSlots.get(i)).button;
            Vec2 pos = ((InscriptionTableScreen.SpellSlotInfo) this.spellSlots.get(i)).relativePosition.add(center);
            spellSlot.m_252865_((int) pos.x);
            spellSlot.m_253211_((int) pos.y);
            this.renderSpellSlot(guiHelper, pos, mouseX, mouseY, i, (InscriptionTableScreen.SpellSlotInfo) this.spellSlots.get(i));
        }
    }

    private void renderButtons(GuiGraphics guiHelper, int mouseX, int mouseY) {
        this.inscribeButton.m_252865_(this.f_97735_ + 43);
        this.inscribeButton.m_253211_(this.f_97736_ + 35);
        if (this.inscribeButton.f_93623_) {
            if (this.isHovering(this.inscribeButton.m_252754_(), this.inscribeButton.m_252907_(), 14, 14, mouseX, mouseY)) {
                guiHelper.blit(TEXTURE, this.inscribeButton.m_252754_(), this.inscribeButton.m_252907_(), 28, 185, 14, 14);
            } else {
                guiHelper.blit(TEXTURE, this.inscribeButton.m_252754_(), this.inscribeButton.m_252907_(), 14, 185, 14, 14);
            }
        } else {
            guiHelper.blit(TEXTURE, this.inscribeButton.m_252754_(), this.inscribeButton.m_252907_(), 0, 185, 14, 14);
        }
    }

    private void renderSpellSlot(GuiGraphics guiHelper, Vec2 pos, int mouseX, int mouseY, int index, InscriptionTableScreen.SpellSlotInfo slot) {
        boolean hovering = this.isHovering((int) pos.x, (int) pos.y, 19, 19, mouseX, mouseY);
        int iconToDraw = hovering ? 38 : (slot.hasSpell() ? 19 : 0);
        guiHelper.blit(TEXTURE, (int) pos.x, (int) pos.y, iconToDraw, 166, 19, 19);
        if (slot.hasSpell()) {
            this.drawSpellIcon(guiHelper, pos, slot);
            if (hovering && !slot.spellData.canRemove()) {
                guiHelper.blit(TEXTURE, (int) pos.x, (int) pos.y, 76, 166, 19, 19);
            }
        }
        if (index == this.selectedSpellIndex) {
            guiHelper.blit(TEXTURE, (int) pos.x, (int) pos.y, 57, 166, 19, 19);
        }
    }

    private void drawSpellIcon(GuiGraphics guiHelper, Vec2 pos, InscriptionTableScreen.SpellSlotInfo slot) {
        guiHelper.blit(slot.spellData.getSpell().getSpellIconResource(), (int) pos.x + 2, (int) pos.y + 2, 0.0F, 0.0F, 15, 15, 16, 16);
    }

    private void renderLorePage(GuiGraphics guiHelper, float partialTick, int mouseX, int mouseY) {
        int x = this.f_97735_ + 176;
        int y = this.f_97736_;
        int margin = 2;
        Style textColor = Style.EMPTY.withColor(3288106);
        PoseStack poseStack = guiHelper.pose();
        boolean spellSelected = this.selectedSpellIndex >= 0 && this.selectedSpellIndex < this.spellSlots.size() && ((InscriptionTableScreen.SpellSlotInfo) this.spellSlots.get(this.selectedSpellIndex)).hasSpell();
        MutableComponent title = this.selectedSpellIndex < 0 ? Component.translatable("ui.irons_spellbooks.no_selection") : (spellSelected ? ((InscriptionTableScreen.SpellSlotInfo) this.spellSlots.get(this.selectedSpellIndex)).spellData.getSpell().getDisplayName(Minecraft.getInstance().player) : Component.translatable("ui.irons_spellbooks.empty_slot"));
        List<FormattedCharSequence> titleLines = this.f_96547_.split(title.withStyle(ChatFormatting.UNDERLINE).withStyle(textColor), 80);
        int titleY = this.f_97736_ + 10;
        for (FormattedCharSequence line : titleLines) {
            int titleWidth = this.f_96547_.width(line);
            int titleX = x + (80 - titleWidth) / 2;
            guiHelper.drawString(this.f_96547_, line, titleX, titleY, 16777215, false);
            if (spellSelected && this.isHovering(titleX, titleY, titleWidth, 9, mouseX, mouseY)) {
                guiHelper.renderTooltip(this.f_96547_, TooltipsUtils.createSpellDescriptionTooltip(((InscriptionTableScreen.SpellSlotInfo) this.spellSlots.get(this.selectedSpellIndex)).spellData.getSpell(), this.f_96547_), mouseX, mouseY);
            }
            titleY += 9;
        }
        int titleHeight = this.f_96547_.wordWrapHeight(title.withStyle(ChatFormatting.UNDERLINE).withStyle(textColor), 80);
        int descLine = titleY + 4;
        if (this.selectedSpellIndex >= 0 && this.selectedSpellIndex < this.spellSlots.size() && ((InscriptionTableScreen.SpellSlotInfo) this.spellSlots.get(this.selectedSpellIndex)).hasSpell()) {
            Style colorMana = Style.EMPTY.withColor(17577);
            Style colorCast = Style.EMPTY.withColor(1135889);
            Style colorCooldown = Style.EMPTY.withColor(1135889);
            AbstractSpell spell = ((InscriptionTableScreen.SpellSlotInfo) this.spellSlots.get(this.selectedSpellIndex)).spellData.getSpell();
            int spellLevel = ((InscriptionTableScreen.SpellSlotInfo) this.spellSlots.get(this.selectedSpellIndex)).spellData.getLevel();
            float textScale = 1.0F;
            float reverseScale = 1.0F / textScale;
            Component school = spell.getSchoolType().getDisplayName();
            poseStack.scale(textScale, textScale, textScale);
            this.drawTextWithShadow(this.f_96547_, guiHelper, school, x + (80 - this.f_96547_.width(school.getString())) / 2, descLine, 16777215, 1.0F);
            descLine = (int) ((float) descLine + 9.0F * textScale);
            MutableComponent levelText = Component.translatable("ui.irons_spellbooks.level", spellLevel).withStyle(textColor);
            guiHelper.drawString(this.f_96547_, levelText, x + (80 - this.f_96547_.width(levelText.getString())) / 2, descLine, 16777215, false);
            descLine = (int) ((float) descLine + 9.0F * textScale * 2.0F);
            descLine += this.drawStatText(this.f_96547_, guiHelper, x + margin, descLine, "ui.irons_spellbooks.mana_cost", textColor, Component.translatable(spell.getManaCost(spellLevel) + ""), colorMana, textScale);
            descLine += this.drawText(this.f_96547_, guiHelper, TooltipsUtils.getCastTimeComponent(spell.getCastType(), Utils.timeFromTicks((float) spell.getEffectiveCastTime(spellLevel, null), 1)), x + margin, descLine, textColor.getColor().getValue(), textScale);
            descLine += this.drawStatText(this.f_96547_, guiHelper, x + margin, descLine, "ui.irons_spellbooks.cooldown", textColor, Component.translatable(Utils.timeFromTicks((float) spell.getSpellCooldown(), 1)), colorCooldown, textScale);
            for (MutableComponent component : spell.getUniqueInfo(spellLevel, null)) {
                descLine += this.drawText(this.f_96547_, guiHelper, component, x + margin, descLine, textColor.getColor().getValue(), 1.0F);
            }
            poseStack.scale(reverseScale, reverseScale, reverseScale);
        }
    }

    private void drawTextWithShadow(Font font, GuiGraphics guiHelper, Component text, int x, int y, int color, float scale) {
        x = (int) ((float) x / scale);
        y = (int) ((float) y / scale);
        guiHelper.drawString(font, text, x, y, color);
    }

    private int drawText(Font font, GuiGraphics guiHelper, Component text, int x, int y, int color, float scale) {
        x = (int) ((float) x / scale);
        y = (int) ((float) y / scale);
        guiHelper.drawWordWrap(font, text, x, y, 80, color);
        return font.wordWrapHeight(text, 80);
    }

    private int drawStatText(Font font, GuiGraphics guiHelper, int x, int y, String translationKey, Style textStyle, MutableComponent stat, Style statStyle, float scale) {
        return this.drawText(font, guiHelper, Component.translatable(translationKey, stat.withStyle(statStyle)).withStyle(textStyle), x, y, 16777215, scale);
    }

    private void generateSpellSlots() {
        for (InscriptionTableScreen.SpellSlotInfo s : this.spellSlots) {
            this.m_169411_(s.button);
        }
        this.spellSlots.clear();
        if (this.isSpellBookSlotted()) {
            Slot spellBookSlot = (Slot) ((InscriptionTableMenu) this.f_97732_).f_38839_.get(36);
            ItemStack spellBookItemStack = spellBookSlot.getItem();
            ISpellContainer spellBookContainer = ISpellContainer.get(spellBookItemStack);
            SpellData[] storedSpells = spellBookContainer.getAllSpells();
            int spellCount = spellBookContainer.getMaxSpellCount();
            if (spellCount > 15) {
                spellCount = 15;
            }
            if (spellCount > 0) {
                int boxSize = 19;
                int[] rowCounts = ClientRenderCache.getRowCounts(spellCount);
                int[] row1 = new int[rowCounts[0]];
                int[] row2 = new int[rowCounts[1]];
                int[] row3 = new int[rowCounts[2]];
                int[] rowWidth = new int[] { boxSize * row1.length, boxSize * row2.length, boxSize * row3.length };
                int[] rowHeight = new int[] { row1.length > 0 ? boxSize : 0, row2.length > 0 ? boxSize : 0, row3.length > 0 ? boxSize : 0 };
                int overallHeight = rowHeight[0] + rowHeight[1] + rowHeight[2];
                int[][] display = new int[][] { row1, row2, row3 };
                int index = 0;
                for (int row = 0; row < display.length; row++) {
                    for (int column = 0; column < display[row].length; column++) {
                        int offset = -rowWidth[row] / 2;
                        Vec2 location = new Vec2((float) (offset + column * boxSize), (float) (row * boxSize - overallHeight / 2));
                        location.add(-9.0F);
                        int temp_index = index;
                        this.spellSlots.add(new InscriptionTableScreen.SpellSlotInfo(storedSpells[index], location, (Button) this.m_7787_(Button.builder(Component.translatable(temp_index + ""), p_169820_ -> this.setSelectedIndex(temp_index)).pos((int) location.x, (int) location.y).size(boxSize, boxSize).build())));
                        index++;
                    }
                }
                this.isDirty = false;
            }
        }
    }

    private void onSpellBookSlotChanged() {
        this.isDirty = true;
        ItemStack spellBookStack = ((Slot) ((InscriptionTableMenu) this.f_97732_).f_38839_.get(36)).getItem();
        if (spellBookStack.getItem() instanceof SpellBook) {
            ISpellContainer spellBookContainer = ISpellContainer.get(spellBookStack);
            if (spellBookContainer.getMaxSpellCount() <= this.selectedSpellIndex) {
                this.resetSelectedSpell();
            }
        } else {
            this.resetSelectedSpell();
        }
    }

    private void onInscription() {
        if (((InscriptionTableMenu) this.f_97732_).getSpellBookSlot().getItem().getItem() instanceof SpellBook spellBook && ((InscriptionTableMenu) this.f_97732_).getScrollSlot().getItem().getItem() instanceof Scroll scroll) {
            if (this.spellSlots.isEmpty()) {
                return;
            }
            ISpellContainer scrollContainer = ISpellContainer.get(((InscriptionTableMenu) this.f_97732_).getScrollSlot().getItem());
            SpellData scrollSlot = scrollContainer.getSpellAtIndex(0);
            if (spellBook.getRarity().compareRarity(scrollSlot.getSpell().getRarity(scrollSlot.getLevel())) < 0) {
                return;
            }
            if (this.selectedSpellIndex < 0 || ((InscriptionTableScreen.SpellSlotInfo) this.spellSlots.get(this.selectedSpellIndex)).hasSpell()) {
                for (int i = this.selectedSpellIndex + 1; i < this.spellSlots.size(); i++) {
                    if (!((InscriptionTableScreen.SpellSlotInfo) this.spellSlots.get(i)).hasSpell()) {
                        this.setSelectedIndex(i);
                        break;
                    }
                }
            }
            this.setSelectedIndex(Mth.clamp(this.selectedSpellIndex, 0, this.spellSlots.size() - 1));
            if (((InscriptionTableScreen.SpellSlotInfo) this.spellSlots.get(this.selectedSpellIndex)).hasSpell()) {
                return;
            }
            this.isDirty = true;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1.0F));
            this.f_96541_.gameMode.handleInventoryButtonClick(((InscriptionTableMenu) this.f_97732_).f_38840_, -1);
        }
    }

    private void setSelectedIndex(int index) {
        this.selectedSpellIndex = index;
        this.f_96541_.gameMode.handleInventoryButtonClick(((InscriptionTableMenu) this.f_97732_).f_38840_, index);
    }

    private void resetSelectedSpell() {
        this.setSelectedIndex(-1);
    }

    private boolean isValidInscription() {
        return this.isSpellBookSlotted() && this.isScrollSlotted();
    }

    private boolean isValidExtraction() {
        return this.selectedSpellIndex >= 0 && ((InscriptionTableScreen.SpellSlotInfo) this.spellSlots.get(this.selectedSpellIndex)).hasSpell() && !((Slot) ((InscriptionTableMenu) this.f_97732_).f_38839_.get(38)).hasItem();
    }

    private boolean isSpellBookSlotted() {
        return ((Slot) ((InscriptionTableMenu) this.f_97732_).f_38839_.get(36)).getItem().getItem() instanceof SpellBook;
    }

    private boolean isScrollSlotted() {
        return ((Slot) ((InscriptionTableMenu) this.f_97732_).f_38839_.get(37)).hasItem() && ((Slot) ((InscriptionTableMenu) this.f_97732_).f_38839_.get(37)).getItem().getItem() instanceof Scroll;
    }

    private boolean isHovering(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    private static class SpellSlotInfo {

        public SpellData spellData;

        public Vec2 relativePosition;

        public Button button;

        SpellSlotInfo(SpellData spellData, Vec2 relativePosition, Button button) {
            this.spellData = spellData;
            this.relativePosition = relativePosition;
            this.button = button;
        }

        public boolean hasSpell() {
            return this.spellData != null && !this.spellData.equals(SpellData.EMPTY);
        }
    }
}