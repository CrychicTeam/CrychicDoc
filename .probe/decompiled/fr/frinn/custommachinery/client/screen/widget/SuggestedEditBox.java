package fr.frinn.custommachinery.client.screen.widget;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.lwjgl.opengl.GL11;

public class SuggestedEditBox extends EditBox {

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(\\s+)");

    private final List<String> possibleSuggestions = new ArrayList();

    private final int suggestionLineLimit;

    private final int baseWidth;

    private SuggestedEditBox.SuggestionsList suggestionsList;

    private Suggestions suggestions;

    private List<Suggestion> sorted;

    private int baseX;

    private boolean anchorToBottom = false;

    public SuggestedEditBox(Font font, int x, int y, int width, int height, Component message, int suggestionLineLimit) {
        super(font, x, y, width, height, message);
        this.suggestionLineLimit = suggestionLineLimit;
        this.baseWidth = width;
        this.setResponder(s -> {
        });
        this.updateSuggestions();
    }

    public void addSuggestions(List<String> suggestions) {
        this.possibleSuggestions.addAll(suggestions);
        this.updateSuggestions();
    }

    public void clearSuggestions() {
        this.possibleSuggestions.clear();
        this.updateSuggestions();
    }

    public List<Suggestion> getSuggestions() {
        return this.sorted != null ? this.sorted : Collections.emptyList();
    }

    public void updateSuggestions() {
        SuggestionsBuilder builder = new SuggestionsBuilder(this.m_94155_(), 0);
        this.possibleSuggestions.forEach(builder::suggest);
        this.suggestions = builder.build();
        this.sorted = this.sortSuggestions(this.suggestions);
    }

    public void showSuggestions(boolean narrateFirstSuggestion) {
        if (this.suggestions != null && !this.suggestions.isEmpty()) {
            int i = 0;
            for (Suggestion suggestion : this.suggestions.getList()) {
                i = Math.max(i, Minecraft.getInstance().font.width(suggestion.getText()));
            }
            if (Minecraft.getInstance().screen instanceof BaseScreen baseScreen) {
                i = Mth.clamp(i, 0, baseScreen.xSize);
            }
            int k = this.m_252907_() + (this.anchorToBottom ? 0 : this.f_93619_);
            this.suggestionsList = new SuggestedEditBox.SuggestionsList(this.m_252754_() + 4, k, i, this.sortSuggestions(this.suggestions), narrateFirstSuggestion, this.anchorToBottom, this.suggestionLineLimit);
        }
    }

    public void hideSuggestions() {
        this.suggestionsList = null;
    }

    public void setAnchorToBottom() {
        this.anchorToBottom = true;
    }

    private List<Suggestion> sortSuggestions(Suggestions suggestions) {
        String string = this.m_94155_().substring(0, this.m_94207_());
        int i = getLastWordIndex(string);
        String string2 = string.substring(i).toLowerCase(Locale.ROOT);
        ArrayList<Suggestion> list = Lists.newArrayList();
        ArrayList<Suggestion> list2 = Lists.newArrayList();
        for (Suggestion suggestion : suggestions.getList()) {
            if (!suggestion.getText().startsWith(string2) && !suggestion.getText().startsWith("minecraft:" + string2)) {
                list2.add(suggestion);
            } else {
                list.add(suggestion);
            }
        }
        return list;
    }

    private static int getLastWordIndex(String text) {
        if (Strings.isNullOrEmpty(text)) {
            return 0;
        } else {
            int i = 0;
            Matcher matcher = WHITESPACE_PATTERN.matcher(text);
            while (matcher.find()) {
                i = matcher.end();
            }
            return i;
        }
    }

    @Override
    public void setResponder(Consumer<String> responder) {
        super.setResponder(s -> {
            this.updateSuggestions();
            this.showSuggestions(true);
            responder.accept(s);
        });
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            this.hideSuggestions();
            this.setX(this.baseX);
            this.m_93674_(this.baseWidth);
        } else {
            this.baseX = this.m_252754_();
            this.showSuggestions(false);
            this.setX(this.suggestionsList.rect.getX());
            this.m_93674_(Minecraft.getInstance().getWindow().getScreenWidth() - this.m_252754_());
            this.m_94198_();
        }
    }

    @Override
    public void setX(int x) {
        super.m_252865_(x);
        if (this.suggestionsList != null) {
            this.suggestionsList.rect.setX(Math.min(x + 3, Minecraft.getInstance().getWindow().getGuiScaledWidth() - this.m_5711_()));
        }
    }

    @Override
    public void setY(int y) {
        super.m_253211_(y);
        if (this.suggestionsList != null) {
            int yPos = this.m_252907_() + this.f_93619_ + this.suggestionLineLimit * 12;
            this.suggestionsList.rect.setY(this.suggestionsList.anchorToBottom ? yPos - 3 - Math.min(this.suggestionsList.suggestionList.size(), this.suggestionLineLimit) * 12 : yPos);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.suggestionsList != null) {
            boolean scissor = GL11.glIsEnabled(3089);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
            if (scissor) {
                GlStateManager._disableScissorTest();
            }
            this.suggestionsList.render(guiGraphics, mouseX, mouseY);
            if (scissor) {
                GlStateManager._enableScissorTest();
            }
            guiGraphics.pose().popPose();
        }
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.suggestionsList != null && this.suggestionsList.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (Minecraft.getInstance().screen != null && Minecraft.getInstance().screen.m_7222_() == this && keyCode == 258) {
            this.showSuggestions(true);
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return this.suggestionsList != null && this.suggestionsList.mouseScrolled(Mth.clamp(delta, -1.0, 1.0)) ? true : super.m_6050_(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return this.suggestionsList != null && this.suggestionsList.mouseClicked((int) mouseX, (int) mouseY, mouseButton) ? true : super.m_6375_(mouseX, mouseY, mouseButton);
    }

    public class SuggestionsList {

        private final Rect2i rect;

        private final String originalContents;

        private final List<Suggestion> suggestionList;

        private final int suggestionLineLimit;

        private final int lineStartOffset = 1;

        private final boolean anchorToBottom;

        private final int fillColor = -805306368;

        private final Font font;

        private int offset;

        private int current;

        private Vec2 lastMouse;

        private boolean tabCycles;

        private int lastNarratedEntry;

        private boolean keepSuggestions;

        SuggestionsList(int xPos, int yPos, int width, List<Suggestion> suggestionList, boolean narrateFirstSuggestion, boolean anchorToBottom, int suggestionLineLimit) {
            this.font = Minecraft.getInstance().font;
            this.lastMouse = Vec2.ZERO;
            this.suggestionLineLimit = suggestionLineLimit;
            this.anchorToBottom = anchorToBottom;
            int i = Math.min(xPos + 3, Minecraft.getInstance().getWindow().getGuiScaledWidth() - width);
            int j = this.anchorToBottom ? yPos - 3 - Math.min(suggestionList.size(), this.suggestionLineLimit) * 12 : yPos;
            this.rect = new Rect2i(i, j, width + 1, Math.min(suggestionList.size(), this.suggestionLineLimit) * 12);
            this.originalContents = SuggestedEditBox.this.m_94155_();
            this.lastNarratedEntry = narrateFirstSuggestion ? -1 : 0;
            this.suggestionList = suggestionList;
            this.select(0);
        }

        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY) {
            int i = Math.min(this.suggestionList.size(), this.suggestionLineLimit);
            int j = -5592406;
            boolean bl = this.offset > 0;
            boolean bl2 = this.suggestionList.size() > this.offset + i;
            boolean bl3 = bl || bl2;
            boolean bl4 = this.lastMouse.x != (float) mouseX || this.lastMouse.y != (float) mouseY;
            if (bl4) {
                this.lastMouse = new Vec2((float) mouseX, (float) mouseY);
            }
            if (bl3) {
                guiGraphics.fill(this.rect.getX(), this.rect.getY() - 1, this.rect.getX() + this.rect.getWidth(), this.rect.getY(), -805306368);
                guiGraphics.fill(this.rect.getX(), this.rect.getY() + this.rect.getHeight(), this.rect.getX() + this.rect.getWidth(), this.rect.getY() + this.rect.getHeight() + 1, -805306368);
                if (bl) {
                    for (int k = 0; k < this.rect.getWidth(); k++) {
                        if (k % 2 == 0) {
                            guiGraphics.fill(this.rect.getX() + k, this.rect.getY() - 1, this.rect.getX() + k + 1, this.rect.getY(), -1);
                        }
                    }
                }
                if (bl2) {
                    for (int kx = 0; kx < this.rect.getWidth(); kx++) {
                        if (kx % 2 == 0) {
                            guiGraphics.fill(this.rect.getX() + kx, this.rect.getY() + this.rect.getHeight(), this.rect.getX() + kx + 1, this.rect.getY() + this.rect.getHeight() + 1, -1);
                        }
                    }
                }
            }
            boolean bl52 = false;
            for (int l = 0; l < i; l++) {
                Suggestion suggestion = (Suggestion) this.suggestionList.get(l + this.offset);
                guiGraphics.fill(this.rect.getX(), this.rect.getY() + 12 * l, this.rect.getX() + this.rect.getWidth(), this.rect.getY() + 12 * l + 12, -805306368);
                if (mouseX > this.rect.getX() && mouseX < this.rect.getX() + this.rect.getWidth() && mouseY > this.rect.getY() + 12 * l && mouseY < this.rect.getY() + 12 * l + 12) {
                    if (bl4) {
                        this.select(l + this.offset);
                    }
                    bl52 = true;
                }
                guiGraphics.drawString(this.font, suggestion.getText(), this.rect.getX() + 1, this.rect.getY() + 2 + 12 * l, l + this.offset == this.current ? -256 : -5592406);
            }
            Message message;
            if (bl52 && (message = ((Suggestion) this.suggestionList.get(this.current)).getTooltip()) != null) {
                guiGraphics.renderTooltip(this.font, ComponentUtils.fromMessage(message), mouseX, mouseY);
            }
        }

        public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (!this.rect.contains(mouseX, mouseY)) {
                return false;
            } else {
                int i = (mouseY - this.rect.getY()) / 12 + this.offset;
                if (i >= 0 && i < this.suggestionList.size()) {
                    this.select(i);
                    this.useSuggestion();
                }
                return true;
            }
        }

        public boolean mouseScrolled(double delta) {
            int i = (int) (Minecraft.getInstance().mouseHandler.xpos() * (double) Minecraft.getInstance().getWindow().getGuiScaledWidth() / (double) Minecraft.getInstance().getWindow().getScreenWidth());
            if (this.rect.contains(i, (int) (Minecraft.getInstance().mouseHandler.ypos() * (double) Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double) Minecraft.getInstance().getWindow().getScreenHeight()))) {
                this.offset = Mth.clamp((int) ((double) this.offset - delta), 0, Math.max(this.suggestionList.size() - this.suggestionLineLimit, 0));
                return true;
            } else {
                return false;
            }
        }

        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            return switch(keyCode) {
                case 256 ->
                    {
                    }
                case 257 ->
                    {
                    }
                case 258 ->
                    {
                    }
                default ->
                    false;
                case 264 ->
                    {
                    }
                case 265 ->
                    {
                    }
            };
        }

        public void cycle(int change) {
            this.select(this.current + change);
            int i = this.offset;
            int j = this.offset + this.suggestionLineLimit - 1;
            if (this.current < i) {
                this.offset = Mth.clamp(this.current, 0, Math.max(this.suggestionList.size() - this.suggestionLineLimit, 0));
            } else if (this.current > j) {
                this.offset = Mth.clamp(this.current + 1 - this.suggestionLineLimit, 0, Math.max(this.suggestionList.size() - this.suggestionLineLimit, 0));
            }
        }

        public void select(int index) {
            if (!this.suggestionList.isEmpty()) {
                this.current = index;
                if (this.current < 0) {
                    this.current = this.current + this.suggestionList.size();
                }
                if (this.current >= this.suggestionList.size()) {
                    this.current = this.current - this.suggestionList.size();
                }
                Suggestion suggestion = (Suggestion) this.suggestionList.get(this.current);
                if (this.lastNarratedEntry != this.current) {
                    Minecraft.getInstance().getNarrator().sayNow(this.getNarrationMessage());
                }
            }
        }

        public void useSuggestion() {
            Suggestion suggestion = (Suggestion) this.suggestionList.get(this.current);
            this.keepSuggestions = true;
            SuggestedEditBox.this.m_94144_(suggestion.apply(this.originalContents));
            int i = suggestion.getRange().getStart() + suggestion.getText().length();
            SuggestedEditBox.this.m_94196_(i);
            SuggestedEditBox.this.m_94208_(i);
            this.select(this.current);
            this.keepSuggestions = false;
            this.tabCycles = true;
        }

        Component getNarrationMessage() {
            this.lastNarratedEntry = this.current;
            Suggestion suggestion = (Suggestion) this.suggestionList.get(this.current);
            Message message = suggestion.getTooltip();
            return message != null ? Component.translatable("narration.suggestion.tooltip", this.current + 1, this.suggestionList.size(), suggestion.getText(), message) : Component.translatable("narration.suggestion", this.current + 1, this.suggestionList.size(), suggestion.getText());
        }
    }
}