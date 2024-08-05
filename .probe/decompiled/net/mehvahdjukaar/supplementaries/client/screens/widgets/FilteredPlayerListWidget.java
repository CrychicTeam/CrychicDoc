package net.mehvahdjukaar.supplementaries.client.screens.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class FilteredPlayerListWidget implements NarratableEntry, GuiEventListener, Renderable {

    private static final int ENTRY_PER_SCREEN = 3;

    private static final int ITEM_HEIGHT = 12;

    private static final int WIDTH = 101;

    private static final int HEIGHT = 36;

    private static final int SCROLLER_W = 6;

    private static final int SCROLLER_H = 17;

    private static final int SCROLLER_X = 102;

    private final List<FilteredPlayerListWidget.SimplePlayerEntry> allPlayers = new ArrayList();

    private final List<FilteredPlayerListWidget.SimplePlayerEntry> filtered = new ArrayList();

    private final Minecraft minecraft;

    protected final int x;

    protected final int y;

    protected final int x1;

    protected final int y1;

    private final Consumer<String> onClick;

    private String filter;

    private int scrollOff;

    private boolean isDragging;

    private boolean focused;

    public FilteredPlayerListWidget(Minecraft minecraft, int x, int y, Consumer<String> onClick) {
        this.minecraft = minecraft;
        this.x = x;
        this.y = y;
        this.x1 = x + 101;
        this.y1 = y + 36;
        this.onClick = onClick;
        for (UUID uuid : this.minecraft.player.connection.getOnlinePlayerIds()) {
            PlayerInfo playerinfo = this.minecraft.player.connection.getPlayerInfo(uuid);
            if (playerinfo != null) {
                this.allPlayers.add(new FilteredPlayerListWidget.SimplePlayerEntry(playerinfo, this.minecraft.font));
            }
        }
        this.filtered.addAll(this.allPlayers);
    }

    @Override
    public void setFocused(boolean bl) {
        this.focused = bl;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }

    public List<String> setFilter(@Nullable String filter) {
        if (filter == null) {
            filter = "";
        }
        this.filter = filter.toLowerCase(Locale.ROOT);
        this.updateFilteredEntries();
        return this.filtered.stream().map(FilteredPlayerListWidget.SimplePlayerEntry::getName).toList();
    }

    private void updateFilteredEntries() {
        this.filtered.clear();
        this.filtered.addAll(this.allPlayers.stream().filter(s -> s.getName().toLowerCase(Locale.ROOT).startsWith(this.filter)).toList());
    }

    public void addPlayer(PlayerInfo info) {
        this.allPlayers.add(new FilteredPlayerListWidget.SimplePlayerEntry(info, this.minecraft.font));
        this.updateFilteredEntries();
    }

    public void removePlayer(UUID id) {
        for (FilteredPlayerListWidget.SimplePlayerEntry simplePlayerEntry : this.allPlayers) {
            if (simplePlayerEntry.getId().equals(id)) {
                this.allPlayers.remove(simplePlayerEntry);
                this.updateFilteredEntries();
                return;
            }
        }
    }

    private boolean canScroll() {
        return this.filtered.size() > 3;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int key) {
        this.isDragging = this.canScroll() && mouseX > (double) (this.x + 102) && mouseX < (double) (this.x + 102 + 6) && mouseY > (double) this.y && mouseY <= (double) (this.y + 36 + 1);
        if (this.isMouseOver(mouseX, mouseY)) {
            FilteredPlayerListWidget.SimplePlayerEntry e = this.getEntryAtPosition(mouseX, mouseY);
            if (e != null) {
                this.onClick.accept(e.playerName);
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double dx, double dy, int key, double mouseX, double mouseY) {
        if (this.isDragging) {
            int j = this.filtered.size() - 3;
            float f = ((float) dy - (float) this.y - 13.5F) / ((float) (this.y1 - this.y) - 17.0F);
            f = f * (float) j + 0.5F;
            this.scrollOff = Mth.clamp((int) f, 0, j);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseScrolled(double a, double b, double c) {
        if (this.canScroll()) {
            int j = this.filtered.size() - 3;
            this.scrollOff = (int) ((double) this.scrollOff - c);
            this.scrollOff = Mth.clamp(this.scrollOff, 0, j);
        }
        return true;
    }

    @Override
    public boolean keyPressed(int key, int a, int b) {
        if (key == 265) {
            this.scrollOff = Math.max(0, this.scrollOff - 1);
            return true;
        } else if (key == 264) {
            this.scrollOff = Mth.clamp(this.scrollOff + 1, 0, this.filtered.size() - 3);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseY >= (double) this.y && mouseY <= (double) this.y1 && mouseX >= (double) this.x && mouseX <= (double) this.x1;
    }

    @Nullable
    protected final FilteredPlayerListWidget.SimplePlayerEntry getEntryAtPosition(double mouseX, double mouseY) {
        if (mouseX > (double) this.x1) {
            return null;
        } else {
            int rel = Mth.floor(mouseY - (double) this.y);
            int ind = this.scrollOff + rel / 12;
            return rel >= 0 && ind < this.filtered.size() ? (FilteredPlayerListWidget.SimplePlayerEntry) this.filtered.get(ind) : null;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int size = this.filtered.size();
        this.renderScroller(graphics, size);
        if (size != 0) {
            FilteredPlayerListWidget.SimplePlayerEntry hovered = this.isMouseOver((double) mouseX, (double) mouseY) ? this.getEntryAtPosition((double) mouseX, (double) mouseY) : null;
            int currentY = this.y;
            for (int i = 0; this.scrollOff + i < size && i < 3; i++) {
                FilteredPlayerListWidget.SimplePlayerEntry e = (FilteredPlayerListWidget.SimplePlayerEntry) this.filtered.get(this.scrollOff + i);
                e.render(graphics, this.scrollOff + i, this.x, currentY, 101, 12, mouseX, mouseY, Objects.equals(hovered, e), partialTicks);
                currentY += 12;
            }
        }
    }

    private void renderScroller(GuiGraphics graphics, int size) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int currentIndex = size + 1 - 3;
        if (currentIndex > 1) {
            int a = 36 - (17 + (currentIndex - 1) * 36 / currentIndex);
            int b = 1 + a / currentIndex + 36 / currentIndex;
            int scroll = Math.min(19, this.scrollOff * b);
            if (this.scrollOff == currentIndex - 1) {
                scroll = 19;
            }
            graphics.blit(ModTextures.PRESENT_GUI_TEXTURE, this.x + 102, this.y + scroll, 0, 0.0F, 232.0F, 6, 17, 256, 256);
        } else {
            graphics.blit(ModTextures.PRESENT_GUI_TEXTURE, this.x + 102, this.y, 0, 6.0F, 232.0F, 6, 17, 256, 256);
        }
    }

    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
    }

    public void setState(boolean hasItem, boolean packed) {
        this.filtered.clear();
        if (!packed && hasItem) {
            this.filtered.addAll(this.allPlayers);
        }
    }

    public static class SimplePlayerEntry {

        private static final int SKIN_SIZE = 8;

        private final Font font;

        private final UUID id;

        private final String playerName;

        private final Supplier<ResourceLocation> skinGetter;

        public SimplePlayerEntry(PlayerInfo playerInfo, Font font) {
            this.id = playerInfo.getProfile().getId();
            this.playerName = playerInfo.getProfile().getName();
            this.skinGetter = playerInfo::m_105337_;
            this.font = font;
        }

        public UUID getId() {
            return this.id;
        }

        public String getName() {
            return this.playerName;
        }

        public void render(GuiGraphics graphics, int pIndex, int pLeft, int pTop, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean hovered, float pPartialTicks) {
            int i = pLeft + 2;
            int j = pTop + (pHeight - 8) / 2;
            int k = i + 8 + 2;
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.blit(ModTextures.PRESENT_GUI_TEXTURE, pLeft, pTop, 0, 0.0F, 220.0F, pWidth, pHeight, 256, 256);
            ResourceLocation resourceLocation = (ResourceLocation) this.skinGetter.get();
            graphics.blit(resourceLocation, i, j, 8, 8, 8.0F, 8.0F, 8, 8, 64, 64);
            RenderSystem.enableBlend();
            graphics.blit(resourceLocation, i, j, 8, 8, 40.0F, 8.0F, 8, 8, 64, 64);
            RenderSystem.disableBlend();
            graphics.drawString(this.font, this.playerName, k, j, hovered ? -1 : 0);
        }
    }
}