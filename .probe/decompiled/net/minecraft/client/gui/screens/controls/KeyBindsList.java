package net.minecraft.client.gui.screens.controls;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.commons.lang3.ArrayUtils;

public class KeyBindsList extends ContainerObjectSelectionList<KeyBindsList.Entry> {

    final KeyBindsScreen keyBindsScreen;

    int maxNameWidth;

    public KeyBindsList(KeyBindsScreen keyBindsScreen0, Minecraft minecraft1) {
        super(minecraft1, keyBindsScreen0.f_96543_ + 45, keyBindsScreen0.f_96544_, 20, keyBindsScreen0.f_96544_ - 32, 20);
        this.keyBindsScreen = keyBindsScreen0;
        KeyMapping[] $$2 = (KeyMapping[]) ArrayUtils.clone(minecraft1.options.keyMappings);
        Arrays.sort($$2);
        String $$3 = null;
        for (KeyMapping $$4 : $$2) {
            String $$5 = $$4.getCategory();
            if (!$$5.equals($$3)) {
                $$3 = $$5;
                this.m_7085_(new KeyBindsList.CategoryEntry(Component.translatable($$5)));
            }
            Component $$6 = Component.translatable($$4.getName());
            int $$7 = minecraft1.font.width($$6);
            if ($$7 > this.maxNameWidth) {
                this.maxNameWidth = $$7;
            }
            this.m_7085_(new KeyBindsList.KeyEntry($$4, $$6));
        }
    }

    public void resetMappingAndUpdateButtons() {
        KeyMapping.resetMapping();
        this.refreshEntries();
    }

    public void refreshEntries() {
        this.m_6702_().forEach(KeyBindsList.Entry::m_264257_);
    }

    @Override
    protected int getScrollbarPosition() {
        return super.m_5756_() + 15;
    }

    @Override
    public int getRowWidth() {
        return super.m_5759_() + 32;
    }

    public class CategoryEntry extends KeyBindsList.Entry {

        final Component name;

        private final int width;

        public CategoryEntry(Component component0) {
            this.name = component0;
            this.width = KeyBindsList.this.f_93386_.font.width(this.name);
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            guiGraphics0.drawString(KeyBindsList.this.f_93386_.font, this.name, KeyBindsList.this.f_93386_.screen.width / 2 - this.width / 2, int2 + int5 - 9 - 1, 16777215, false);
        }

        @Nullable
        @Override
        public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent0) {
            return null;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {

                @Override
                public NarratableEntry.NarrationPriority narrationPriority() {
                    return NarratableEntry.NarrationPriority.HOVERED;
                }

                @Override
                public void updateNarration(NarrationElementOutput p_193906_) {
                    p_193906_.add(NarratedElementType.TITLE, CategoryEntry.this.name);
                }
            });
        }

        @Override
        protected void refreshEntry() {
        }
    }

    public abstract static class Entry extends ContainerObjectSelectionList.Entry<KeyBindsList.Entry> {

        abstract void refreshEntry();
    }

    public class KeyEntry extends KeyBindsList.Entry {

        private final KeyMapping key;

        private final Component name;

        private final Button changeButton;

        private final Button resetButton;

        private boolean hasCollision = false;

        KeyEntry(KeyMapping keyMapping0, Component component1) {
            this.key = keyMapping0;
            this.name = component1;
            this.changeButton = Button.builder(component1, p_269618_ -> {
                KeyBindsList.this.keyBindsScreen.selectedKey = keyMapping0;
                KeyBindsList.this.resetMappingAndUpdateButtons();
            }).bounds(0, 0, 75, 20).createNarration(p_253311_ -> keyMapping0.isUnbound() ? Component.translatable("narrator.controls.unbound", component1) : Component.translatable("narrator.controls.bound", component1, p_253311_.get())).build();
            this.resetButton = Button.builder(Component.translatable("controls.reset"), p_269616_ -> {
                KeyBindsList.this.f_93386_.options.setKey(keyMapping0, keyMapping0.getDefaultKey());
                KeyBindsList.this.resetMappingAndUpdateButtons();
            }).bounds(0, 0, 50, 20).createNarration(p_253313_ -> Component.translatable("narrator.controls.reset", component1)).build();
            this.refreshEntry();
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            int var10003 = int3 + 90 - KeyBindsList.this.maxNameWidth;
            guiGraphics0.drawString(KeyBindsList.this.f_93386_.font, this.name, var10003, int2 + int5 / 2 - 9 / 2, 16777215, false);
            this.resetButton.m_252865_(int3 + 190);
            this.resetButton.m_253211_(int2);
            this.resetButton.m_88315_(guiGraphics0, int6, int7, float9);
            this.changeButton.m_252865_(int3 + 105);
            this.changeButton.m_253211_(int2);
            if (this.hasCollision) {
                int $$10 = 3;
                int $$11 = this.changeButton.m_252754_() - 6;
                guiGraphics0.fill($$11, int2 + 2, $$11 + 3, int2 + int5 + 2, ChatFormatting.RED.getColor() | 0xFF000000);
            }
            this.changeButton.m_88315_(guiGraphics0, int6, int7, float9);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.changeButton, this.resetButton);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(this.changeButton, this.resetButton);
        }

        @Override
        protected void refreshEntry() {
            this.changeButton.m_93666_(this.key.getTranslatedKeyMessage());
            this.resetButton.f_93623_ = !this.key.isDefault();
            this.hasCollision = false;
            MutableComponent $$0 = Component.empty();
            if (!this.key.isUnbound()) {
                for (KeyMapping $$1 : KeyBindsList.this.f_93386_.options.keyMappings) {
                    if ($$1 != this.key && this.key.same($$1)) {
                        if (this.hasCollision) {
                            $$0.append(", ");
                        }
                        this.hasCollision = true;
                        $$0.append(Component.translatable($$1.getName()));
                    }
                }
            }
            if (this.hasCollision) {
                this.changeButton.m_93666_(Component.literal("[ ").append(this.changeButton.m_6035_().copy().withStyle(ChatFormatting.WHITE)).append(" ]").withStyle(ChatFormatting.RED));
                this.changeButton.m_257544_(Tooltip.create(Component.translatable("controls.keybinds.duplicateKeybinds", $$0)));
            } else {
                this.changeButton.m_257544_(null);
            }
            if (KeyBindsList.this.keyBindsScreen.selectedKey == this.key) {
                this.changeButton.m_93666_(Component.literal("> ").append(this.changeButton.m_6035_().copy().withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE)).append(" <").withStyle(ChatFormatting.YELLOW));
            }
        }
    }
}