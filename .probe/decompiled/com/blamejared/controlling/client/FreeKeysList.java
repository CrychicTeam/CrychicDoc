package com.blamejared.controlling.client;

import com.blamejared.controlling.ControllingConstants;
import com.blamejared.controlling.mixin.AccessInputConstantsKey;
import com.blamejared.controlling.mixin.AccessKeyMapping;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;

public class FreeKeysList extends CustomList {

    private final KeyBindsScreen controlsScreen;

    private final Minecraft mc;

    private int maxListLabelWidth;

    private final List<KeyMapping> keyBindings;

    public FreeKeysList(KeyBindsScreen controls, Minecraft mcIn) {
        super(controls, mcIn);
        this.f_93388_ = controls.f_96543_ + 45;
        this.f_93389_ = controls.f_96544_;
        this.f_93390_ = 48;
        this.f_93391_ = controls.f_96544_ - 56;
        this.f_93392_ = controls.f_96543_ + 45;
        this.controlsScreen = controls;
        this.mc = mcIn;
        this.m_6702_().clear();
        this.allEntries = new ArrayList();
        this.keyBindings = (List<KeyMapping>) Arrays.stream(this.mc.options.keyMappings).collect(Collectors.toList());
        this.recalculate();
    }

    public void recalculate() {
        this.m_6702_().clear();
        this.allEntries.clear();
        this.addEntry(new FreeKeysList.HeaderEntry("Available Keys"));
        AccessInputConstantsKey.controlling$getNAME_MAP().values().stream().filter(input -> !input.getName().startsWith("key.keyboard.world")).filter(Predicate.not(InputConstants.UNKNOWN::equals)).sorted(Comparator.comparing(o -> o.getDisplayName().getString())).forEach(input -> {
            if (this.keyBindings.stream().noneMatch(keyBinding -> ((AccessKeyMapping) keyBinding).controlling$getKey().equals(input))) {
                int i = this.mc.font.width(input.getDisplayName().getString());
                if (i > this.maxListLabelWidth) {
                    this.maxListLabelWidth = i;
                }
                this.addEntry(new FreeKeysList.InputEntry(input));
            }
        });
    }

    @Override
    protected int getScrollbarPosition() {
        return super.m_5756_() + 15 + 20;
    }

    @Override
    public int getRowWidth() {
        return super.m_5759_() + 32;
    }

    public class HeaderEntry extends KeyBindsList.Entry {

        private final String text;

        public HeaderEntry(String text) {
            this.text = text;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of();
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of();
        }

        @Override
        public void render(GuiGraphics guiGraphics, int slotIndex, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            guiGraphics.drawCenteredString(FreeKeysList.this.mc.font, ControllingConstants.COMPONENT_OPTIONS_AVAILABLE_KEYS, ((Screen) Objects.requireNonNull(FreeKeysList.this.mc.screen)).width / 2 - this.text.length() / 2, y + height - 9 - 1, 16777215);
        }

        @Override
        protected void refreshEntry() {
        }
    }

    public class InputEntry extends KeyBindsList.Entry {

        private final InputConstants.Key input;

        public InputEntry(InputConstants.Key input) {
            this.input = input;
        }

        public InputConstants.Key getInput() {
            return this.input;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int slotIndex, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            String str = this.input.toString() + " - " + this.input.getValue();
            int length = FreeKeysList.this.mc.font.width(this.input.getDisplayName().getString());
            guiGraphics.drawString(FreeKeysList.this.mc.font, str, x, y + height / 2 - 4, 16777215);
            guiGraphics.renderTooltip(FreeKeysList.this.mc.font, this.input.getDisplayName(), x + width - length, y + height);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of();
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of();
        }

        @Override
        protected void refreshEntry() {
        }
    }
}