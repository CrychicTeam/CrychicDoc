package io.redspace.ironsspellbooks.gui.scroll_forge;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.gui.scroll_forge.network.ServerboundScrollForgeSelectSpell;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.util.ModTags;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ScrollForgeScreen extends AbstractContainerScreen<ScrollForgeMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/gui/scroll_forge.png");

    private static final int SPELL_LIST_X = 89;

    private static final int SPELL_LIST_Y = 15;

    private static final int SCROLL_BAR_X = 199;

    private static final int SCROLL_BAR_Y = 15;

    private static final int SCROLL_BAR_WIDTH = 12;

    private static final int SCROLL_BAR_HEIGHT = 56;

    public static final ResourceLocation RUNIC_FONT = new ResourceLocation("illageralt");

    public static final ResourceLocation ENCHANT_FONT = new ResourceLocation("alt");

    private List<ScrollForgeScreen.SpellCardInfo> availableSpells;

    private ItemStack[] oldMenuSlots = new ItemStack[] { ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY };

    private AbstractSpell selectedSpell = SpellRegistry.none();

    private int scrollOffset;

    private boolean isScrollbarHeld;

    public ScrollForgeScreen(ScrollForgeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.f_97726_ = 218;
        this.f_97727_ = 166;
    }

    @Override
    protected void init() {
        this.availableSpells = new ArrayList();
        this.generateSpellList();
        super.init();
    }

    @Override
    public void onClose() {
        this.setSelectedSpell(SpellRegistry.none());
        this.resetList();
        super.onClose();
    }

    private void resetList() {
        if (((ScrollForgeMenu) this.f_97732_).getInkSlot().getItem().isEmpty() || !(((ScrollForgeMenu) this.f_97732_).getInkSlot().getItem().getItem() instanceof InkItem inkItem) || inkItem.getRarity().compareRarity(ServerConfigs.getSpellConfig(this.selectedSpell).minRarity()) < 0) {
            this.setSelectedSpell(SpellRegistry.none());
        }
        this.scrollOffset = 0;
        for (ScrollForgeScreen.SpellCardInfo s : this.availableSpells) {
            this.m_169411_(s.button);
        }
        this.availableSpells.clear();
    }

    @Override
    public void render(GuiGraphics guiHelper, int mouseX, int mouseY, float delta) {
        this.m_280273_(guiHelper);
        super.render(guiHelper, mouseX, mouseY, delta);
        this.m_280072_(guiHelper, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiHelper, float partialTick, int mouseX, int mouseY) {
        guiHelper.blit(TEXTURE, this.f_97735_, this.f_97736_, 0, 0, this.f_97726_, this.f_97727_);
        float scrollOffset = Mth.clamp((float) this.scrollOffset / (float) (this.totalRowCount() - 3), 0.0F, 1.0F);
        guiHelper.blit(TEXTURE, this.f_97735_ + 199, (int) ((float) (this.f_97736_ + 15) + scrollOffset * 41.0F), this.f_97726_ + (this.isScrollbarHeld ? 12 : 0), 0, 12, 15);
        if (this.menuSlotsChanged()) {
            this.generateSpellList();
        }
        this.renderSpellList(guiHelper, partialTick, mouseX, mouseY);
    }

    private boolean menuSlotsChanged() {
        if (((ScrollForgeMenu) this.f_97732_).getInkSlot().getItem().getItem() == this.oldMenuSlots[0].getItem() && ((ScrollForgeMenu) this.f_97732_).getFocusSlot().getItem().getItem() == this.oldMenuSlots[2].getItem()) {
            return false;
        } else {
            this.oldMenuSlots = new ItemStack[] { ((ScrollForgeMenu) this.f_97732_).getInkSlot().getItem(), ((ScrollForgeMenu) this.f_97732_).getBlankScrollSlot().getItem(), ((ScrollForgeMenu) this.f_97732_).getFocusSlot().getItem() };
            return true;
        }
    }

    private void renderSpellList(GuiGraphics guiHelper, float partialTick, int mouseX, int mouseY) {
        ItemStack inkStack = ((ScrollForgeMenu) this.f_97732_).getInkSlot().getItem();
        SpellRarity inkRarity = this.getRarityFromInk(inkStack.getItem());
        this.availableSpells.sort((a, b) -> ServerConfigs.getSpellConfig(a.spell).minRarity().compareRarity(ServerConfigs.getSpellConfig(b.spell).minRarity()));
        List<FormattedCharSequence> additionalTooltip = null;
        for (int i = 0; i < this.availableSpells.size(); i++) {
            ScrollForgeScreen.SpellCardInfo spellCard = (ScrollForgeScreen.SpellCardInfo) this.availableSpells.get(i);
            if (i - this.scrollOffset >= 0 && i - this.scrollOffset < 3) {
                if (inkRarity == null || spellCard.spell.getMinRarity() > inkRarity.getValue()) {
                    spellCard.activityState = ScrollForgeScreen.SpellCardInfo.ActivityState.INK_ERROR;
                } else if (this.f_96541_ != null && !spellCard.spell.canBeCraftedBy(this.f_96541_.player)) {
                    spellCard.activityState = ScrollForgeScreen.SpellCardInfo.ActivityState.UNLEARNED_ERROR;
                } else {
                    spellCard.activityState = ScrollForgeScreen.SpellCardInfo.ActivityState.ENABLED;
                }
                int x = this.f_97735_ + 89;
                int y = this.f_97736_ + 15 + (i - this.scrollOffset) * 19;
                spellCard.button.m_252865_(x);
                spellCard.button.m_253211_(y);
                spellCard.draw(this, guiHelper, x, y, mouseX, mouseY);
                if (additionalTooltip == null) {
                    additionalTooltip = spellCard.getTooltip(x, y, mouseX, mouseY);
                }
            } else {
                spellCard.activityState = ScrollForgeScreen.SpellCardInfo.ActivityState.DISABLED;
            }
            spellCard.button.f_93623_ = spellCard.activityState == ScrollForgeScreen.SpellCardInfo.ActivityState.ENABLED;
        }
        if (additionalTooltip != null) {
            guiHelper.renderTooltip(this.f_96547_, additionalTooltip, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double direction) {
        int length = this.availableSpells.size();
        int newScroll = this.scrollOffset - (int) direction;
        if (newScroll <= length - 3 && newScroll >= 0) {
            this.scrollOffset = (int) ((double) this.scrollOffset - direction);
            return true;
        } else {
            return false;
        }
    }

    public void generateSpellList() {
        this.resetList();
        ItemStack focusStack = ((ScrollForgeMenu) this.f_97732_).getFocusSlot().getItem();
        IronsSpellbooks.LOGGER.info("ScrollForgeMenu.generateSpellSlots.focus: {}", focusStack.getItem());
        if (!focusStack.isEmpty() && focusStack.is(ModTags.SCHOOL_FOCUS)) {
            SchoolType school = SchoolRegistry.getSchoolFromFocus(focusStack);
            List<AbstractSpell> spells = SpellRegistry.getSpellsForSchool(school).stream().filter(AbstractSpell::allowCrafting).toList();
            for (int i = 0; i < spells.size(); i++) {
                int tempIndex = i;
                if (((AbstractSpell) spells.get(i)).isEnabled() && this.f_96541_ != null) {
                    this.availableSpells.add(new ScrollForgeScreen.SpellCardInfo((AbstractSpell) spells.get(i), i + 1, i, (Button) this.m_7787_(new Button.Builder(((AbstractSpell) spells.get(i)).getDisplayName(this.f_96541_.player), b -> this.setSelectedSpell((AbstractSpell) spells.get(tempIndex))).pos(0, 0).size(108, 19).build())));
                }
            }
        }
    }

    private void setSelectedSpell(AbstractSpell spell) {
        this.selectedSpell = spell;
        Messages.sendToServer(new ServerboundScrollForgeSelectSpell(((ScrollForgeMenu) this.f_97732_).blockEntity.m_58899_(), spell.getSpellId()));
    }

    private SpellRarity getRarityFromInk(Item ink) {
        return ink instanceof InkItem inkItem ? inkItem.getRarity() : null;
    }

    public AbstractSpell getSelectedSpell() {
        return this.selectedSpell;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        this.isScrollbarHeld = this.m_6774_(199, 15, 12, 56, pMouseX, pMouseY);
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        this.isScrollbarHeld = false;
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        int i = this.totalRowCount() - 3;
        if (this.isScrollbarHeld) {
            int j = this.f_97736_ + 15;
            int k = j + 56;
            float scrollOffs = ((float) pMouseY - (float) j - 7.5F) / ((float) (k - j) - 15.0F);
            scrollOffs = Mth.clamp(scrollOffs, 0.0F, 1.0F);
            this.scrollOffset = Math.max((int) ((double) (scrollOffs * (float) i) + 0.5), 0);
            return true;
        } else {
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }

    private int totalRowCount() {
        return this.availableSpells.size();
    }

    private class SpellCardInfo {

        ScrollForgeScreen.SpellCardInfo.ActivityState activityState = ScrollForgeScreen.SpellCardInfo.ActivityState.DISABLED;

        AbstractSpell spell;

        int spellLevel;

        SpellRarity rarity;

        Button button;

        int index;

        SpellCardInfo(AbstractSpell spell, int spellLevel, int index, Button button) {
            this.spell = spell;
            this.spellLevel = spellLevel;
            this.index = index;
            this.button = button;
            this.rarity = spell.getRarity(spellLevel);
        }

        void draw(ScrollForgeScreen screen, GuiGraphics guiHelper, int x, int y, int mouseX, int mouseY) {
            if (this.activityState != ScrollForgeScreen.SpellCardInfo.ActivityState.ENABLED && this.activityState != ScrollForgeScreen.SpellCardInfo.ActivityState.UNLEARNED_ERROR) {
                guiHelper.blit(ScrollForgeScreen.TEXTURE, x, y, 0, 185, 108, 19);
            } else if (this.spell == screen.getSelectedSpell()) {
                guiHelper.blit(ScrollForgeScreen.TEXTURE, x, y, 0, 204, 108, 19);
            } else {
                guiHelper.blit(ScrollForgeScreen.TEXTURE, x, y, 0, 166, 108, 19);
            }
            ResourceLocation texture = this.activityState == ScrollForgeScreen.SpellCardInfo.ActivityState.ENABLED ? this.spell.getSpellIconResource() : SpellRegistry.none().getSpellIconResource();
            guiHelper.blit(texture, x + 108 - 18, y + 1, 0.0F, 0.0F, 16, 16, 16, 16);
            int maxWidth = 88;
            FormattedText text = this.trimText(ScrollForgeScreen.this.f_96547_, this.getDisplayName().withStyle(this.activityState == ScrollForgeScreen.SpellCardInfo.ActivityState.ENABLED ? Style.EMPTY : Style.EMPTY.withFont(ScrollForgeScreen.RUNIC_FONT)), maxWidth);
            int textX = x + 2;
            int textY = y + 3;
            guiHelper.drawWordWrap(ScrollForgeScreen.this.f_96547_, text, textX, textY, maxWidth, 16777215);
        }

        @Nullable
        List<FormattedCharSequence> getTooltip(int x, int y, int mouseX, int mouseY) {
            MutableComponent text = this.getDisplayName();
            int textX = x + 2;
            int textY = y + 3;
            return mouseX >= textX && mouseY >= textY && mouseX < textX + ScrollForgeScreen.this.f_96547_.width(text) && mouseY < textY + 9 ? this.getHoverText() : null;
        }

        List<FormattedCharSequence> getHoverText() {
            if (this.activityState == ScrollForgeScreen.SpellCardInfo.ActivityState.INK_ERROR) {
                return List.of(FormattedCharSequence.forward(Component.translatable("ui.irons_spellbooks.ink_rarity_error").getString(), Style.EMPTY));
            } else {
                return this.activityState == ScrollForgeScreen.SpellCardInfo.ActivityState.UNLEARNED_ERROR ? List.of(FormattedCharSequence.forward(Component.translatable("ui.irons_spellbooks.unlearned_error").getString(), Style.EMPTY)) : TooltipsUtils.createSpellDescriptionTooltip(this.spell, ScrollForgeScreen.this.f_96547_);
            }
        }

        private FormattedText trimText(Font font, Component component, int maxWidth) {
            FormattedText text = (FormattedText) font.getSplitter().splitLines(component, maxWidth, component.getStyle()).get(0);
            if (text.getString().length() < component.getString().length()) {
                text = FormattedText.composite(text, FormattedText.of("..."));
            }
            return text;
        }

        MutableComponent getDisplayName() {
            return this.spell.getDisplayName(ScrollForgeScreen.this.f_96541_.player);
        }

        static enum ActivityState {

            DISABLED, ENABLED, INK_ERROR, UNLEARNED_ERROR
        }
    }
}