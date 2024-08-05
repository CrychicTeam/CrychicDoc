package net.minecraft.client.gui.spectator;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import net.minecraft.client.gui.spectator.categories.SpectatorPage;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class SpectatorMenu {

    private static final SpectatorMenuItem CLOSE_ITEM = new SpectatorMenu.CloseSpectatorItem();

    private static final SpectatorMenuItem SCROLL_LEFT = new SpectatorMenu.ScrollMenuItem(-1, true);

    private static final SpectatorMenuItem SCROLL_RIGHT_ENABLED = new SpectatorMenu.ScrollMenuItem(1, true);

    private static final SpectatorMenuItem SCROLL_RIGHT_DISABLED = new SpectatorMenu.ScrollMenuItem(1, false);

    private static final int MAX_PER_PAGE = 8;

    static final Component CLOSE_MENU_TEXT = Component.translatable("spectatorMenu.close");

    static final Component PREVIOUS_PAGE_TEXT = Component.translatable("spectatorMenu.previous_page");

    static final Component NEXT_PAGE_TEXT = Component.translatable("spectatorMenu.next_page");

    public static final SpectatorMenuItem EMPTY_SLOT = new SpectatorMenuItem() {

        @Override
        public void selectItem(SpectatorMenu p_101812_) {
        }

        @Override
        public Component getName() {
            return CommonComponents.EMPTY;
        }

        @Override
        public void renderIcon(GuiGraphics p_283652_, float p_101809_, int p_101810_) {
        }

        @Override
        public boolean isEnabled() {
            return false;
        }
    };

    private final SpectatorMenuListener listener;

    private SpectatorMenuCategory category;

    private int selectedSlot = -1;

    int page;

    public SpectatorMenu(SpectatorMenuListener spectatorMenuListener0) {
        this.category = new RootSpectatorMenuCategory();
        this.listener = spectatorMenuListener0;
    }

    public SpectatorMenuItem getItem(int int0) {
        int $$1 = int0 + this.page * 6;
        if (this.page > 0 && int0 == 0) {
            return SCROLL_LEFT;
        } else if (int0 == 7) {
            return $$1 < this.category.getItems().size() ? SCROLL_RIGHT_ENABLED : SCROLL_RIGHT_DISABLED;
        } else if (int0 == 8) {
            return CLOSE_ITEM;
        } else {
            return $$1 >= 0 && $$1 < this.category.getItems().size() ? (SpectatorMenuItem) MoreObjects.firstNonNull((SpectatorMenuItem) this.category.getItems().get($$1), EMPTY_SLOT) : EMPTY_SLOT;
        }
    }

    public List<SpectatorMenuItem> getItems() {
        List<SpectatorMenuItem> $$0 = Lists.newArrayList();
        for (int $$1 = 0; $$1 <= 8; $$1++) {
            $$0.add(this.getItem($$1));
        }
        return $$0;
    }

    public SpectatorMenuItem getSelectedItem() {
        return this.getItem(this.selectedSlot);
    }

    public SpectatorMenuCategory getSelectedCategory() {
        return this.category;
    }

    public void selectSlot(int int0) {
        SpectatorMenuItem $$1 = this.getItem(int0);
        if ($$1 != EMPTY_SLOT) {
            if (this.selectedSlot == int0 && $$1.isEnabled()) {
                $$1.selectItem(this);
            } else {
                this.selectedSlot = int0;
            }
        }
    }

    public void exit() {
        this.listener.onSpectatorMenuClosed(this);
    }

    public int getSelectedSlot() {
        return this.selectedSlot;
    }

    public void selectCategory(SpectatorMenuCategory spectatorMenuCategory0) {
        this.category = spectatorMenuCategory0;
        this.selectedSlot = -1;
        this.page = 0;
    }

    public SpectatorPage getCurrentPage() {
        return new SpectatorPage(this.getItems(), this.selectedSlot);
    }

    static class CloseSpectatorItem implements SpectatorMenuItem {

        @Override
        public void selectItem(SpectatorMenu spectatorMenu0) {
            spectatorMenu0.exit();
        }

        @Override
        public Component getName() {
            return SpectatorMenu.CLOSE_MENU_TEXT;
        }

        @Override
        public void renderIcon(GuiGraphics guiGraphics0, float float1, int int2) {
            guiGraphics0.blit(SpectatorGui.SPECTATOR_LOCATION, 0, 0, 128.0F, 0.0F, 16, 16, 256, 256);
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }

    static class ScrollMenuItem implements SpectatorMenuItem {

        private final int direction;

        private final boolean enabled;

        public ScrollMenuItem(int int0, boolean boolean1) {
            this.direction = int0;
            this.enabled = boolean1;
        }

        @Override
        public void selectItem(SpectatorMenu spectatorMenu0) {
            spectatorMenu0.page = spectatorMenu0.page + this.direction;
        }

        @Override
        public Component getName() {
            return this.direction < 0 ? SpectatorMenu.PREVIOUS_PAGE_TEXT : SpectatorMenu.NEXT_PAGE_TEXT;
        }

        @Override
        public void renderIcon(GuiGraphics guiGraphics0, float float1, int int2) {
            if (this.direction < 0) {
                guiGraphics0.blit(SpectatorGui.SPECTATOR_LOCATION, 0, 0, 144.0F, 0.0F, 16, 16, 256, 256);
            } else {
                guiGraphics0.blit(SpectatorGui.SPECTATOR_LOCATION, 0, 0, 160.0F, 0.0F, 16, 16, 256, 256);
            }
        }

        @Override
        public boolean isEnabled() {
            return this.enabled;
        }
    }
}