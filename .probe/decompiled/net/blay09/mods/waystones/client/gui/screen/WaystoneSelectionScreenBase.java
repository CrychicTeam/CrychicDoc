package net.blay09.mods.waystones.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.mixin.ScreenAccessor;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.client.gui.widget.ITooltipProvider;
import net.blay09.mods.waystones.client.gui.widget.RemoveWaystoneButton;
import net.blay09.mods.waystones.client.gui.widget.SortWaystoneButton;
import net.blay09.mods.waystones.client.gui.widget.WaystoneButton;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneEditPermissions;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.blay09.mods.waystones.network.message.RemoveWaystoneMessage;
import net.blay09.mods.waystones.network.message.RequestEditWaystoneMessage;
import net.blay09.mods.waystones.network.message.SelectWaystoneMessage;
import net.blay09.mods.waystones.network.message.SortWaystoneMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public abstract class WaystoneSelectionScreenBase extends AbstractContainerScreen<WaystoneSelectionMenu> {

    private final List<IWaystone> waystones;

    private List<IWaystone> filteredWaystones;

    private final List<ITooltipProvider> tooltipProviders = new ArrayList();

    private String searchText = "";

    private Button btnPrevPage;

    private Button btnNextPage;

    private EditBox searchBox;

    private int pageOffset;

    private int headerY;

    private boolean isLocationHeaderHovered;

    private int buttonsPerPage;

    private static final int headerHeight = 64;

    private static final int footerHeight = 25;

    private static final int entryHeight = 25;

    public WaystoneSelectionScreenBase(WaystoneSelectionMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.waystones = container.getWaystones();
        this.filteredWaystones = new ArrayList(this.waystones);
        this.f_97726_ = 270;
        this.f_97727_ = 200;
    }

    @Override
    public void init() {
        int maxContentHeight = (int) ((float) this.f_96544_ * 0.6F);
        int maxButtonsPerPage = (maxContentHeight - 64 - 25) / 25;
        this.buttonsPerPage = Math.max(4, Math.min(maxButtonsPerPage, this.waystones.size()));
        int contentHeight = 64 + this.buttonsPerPage * 25 + 25;
        this.f_97726_ = this.f_96543_;
        this.f_97727_ = contentHeight;
        super.init();
        this.tooltipProviders.clear();
        this.btnPrevPage = Button.builder(Component.translatable("gui.waystones.waystone_selection.previous_page"), button -> {
            this.pageOffset = Screen.hasShiftDown() ? 0 : this.pageOffset - 1;
            this.updateList();
        }).pos(this.f_96543_ / 2 - 100, this.f_96544_ / 2 + 40).size(95, 20).build();
        this.addRenderableWidget(this.btnPrevPage);
        this.btnNextPage = Button.builder(Component.translatable("gui.waystones.waystone_selection.next_page"), button -> {
            this.pageOffset = Screen.hasShiftDown() ? (this.waystones.size() - 1) / this.buttonsPerPage : this.pageOffset + 1;
            this.updateList();
        }).pos(this.f_96543_ / 2 + 5, this.f_96544_ / 2 + 40).size(95, 20).build();
        this.addRenderableWidget(this.btnNextPage);
        this.updateList();
        this.searchBox = new EditBox(this.f_96547_, this.f_96543_ / 2 - 99, this.f_97736_ + 64 - 24, 198, 20, Component.empty());
        this.searchBox.setResponder(text -> {
            this.pageOffset = 0;
            this.searchText = text;
            this.updateList();
        });
        this.addRenderableWidget(this.searchBox);
    }

    @Override
    protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
        if (widget instanceof ITooltipProvider) {
            this.tooltipProviders.add((ITooltipProvider) widget);
        }
        return (T) super.m_142416_(widget);
    }

    private void updateList() {
        this.filteredWaystones = (List<IWaystone>) this.waystones.stream().filter(waystonex -> waystonex.getName().toLowerCase().contains(this.searchText.toLowerCase())).collect(Collectors.toList());
        this.headerY = 0;
        this.btnPrevPage.f_93623_ = this.pageOffset > 0;
        this.btnNextPage.f_93623_ = this.pageOffset < (this.filteredWaystones.size() - 1) / this.buttonsPerPage;
        this.tooltipProviders.clear();
        Predicate<Object> removePredicate = button -> button instanceof WaystoneButton || button instanceof SortWaystoneButton || button instanceof RemoveWaystoneButton;
        ((ScreenAccessor) this).balm_getChildren().removeIf(removePredicate);
        ((ScreenAccessor) this).balm_getNarratables().removeIf(removePredicate);
        ((ScreenAccessor) this).balm_getRenderables().removeIf(removePredicate);
        int y = this.f_97736_ + 64 + this.headerY;
        for (int i = 0; i < this.buttonsPerPage; i++) {
            int entryIndex = this.pageOffset * this.buttonsPerPage + i;
            if (entryIndex >= 0 && entryIndex < this.filteredWaystones.size()) {
                IWaystone waystone = (IWaystone) this.filteredWaystones.get(entryIndex);
                this.addRenderableWidget(this.createWaystoneButton(y, waystone));
                if (this.allowSorting()) {
                    SortWaystoneButton sortUpButton = new SortWaystoneButton(this.f_96543_ / 2 + 108, y + 2, -1, y, 20, it -> this.sortWaystone(entryIndex, -1));
                    if (entryIndex == 0) {
                        sortUpButton.f_93623_ = false;
                    }
                    this.addRenderableWidget(sortUpButton);
                    SortWaystoneButton sortDownButton = new SortWaystoneButton(this.f_96543_ / 2 + 108, y + 13, 1, y, 20, it -> this.sortWaystone(entryIndex, 1));
                    if (entryIndex == this.filteredWaystones.size() - 1) {
                        sortDownButton.f_93623_ = false;
                    }
                    this.addRenderableWidget(sortDownButton);
                }
                if (this.allowDeletion()) {
                    RemoveWaystoneButton removeButton = new RemoveWaystoneButton(this.f_96543_ / 2 + 122, y + 4, y, 20, waystone, button -> {
                        Player player = Minecraft.getInstance().player;
                        PlayerWaystoneManager.deactivateWaystone((Player) Objects.requireNonNull(player), waystone);
                        Balm.getNetworking().sendToServer(new RemoveWaystoneMessage(waystone.getWaystoneUid()));
                        this.updateList();
                    });
                    if (!waystone.isGlobal() || Minecraft.getInstance().player.m_150110_().instabuild) {
                        this.addRenderableWidget(removeButton);
                    }
                }
                y += 22;
            }
        }
        this.btnPrevPage.m_253211_(this.f_97736_ + this.headerY + 64 + this.buttonsPerPage * 22 + (this.filteredWaystones.size() > 0 ? 10 : 0));
        this.btnNextPage.m_253211_(this.f_97736_ + this.headerY + 64 + this.buttonsPerPage * 22 + (this.filteredWaystones.size() > 0 ? 10 : 0));
    }

    private WaystoneButton createWaystoneButton(int y, IWaystone waystone) {
        IWaystone waystoneFrom = ((WaystoneSelectionMenu) this.f_97732_).getWaystoneFrom();
        Player player = Minecraft.getInstance().player;
        int xpLevelCost = Math.round((float) PlayerWaystoneManager.predictExperienceLevelCost((Entity) Objects.requireNonNull(player), waystone, ((WaystoneSelectionMenu) this.f_97732_).getWarpMode(), waystoneFrom));
        WaystoneButton btnWaystone = new WaystoneButton(this.f_96543_ / 2 - 100, y, waystone, xpLevelCost, button -> this.onWaystoneSelected(waystone));
        if (waystoneFrom != null && waystone.getWaystoneUid().equals(waystoneFrom.getWaystoneUid())) {
            btnWaystone.f_93623_ = false;
        }
        return btnWaystone;
    }

    protected void onWaystoneSelected(IWaystone waystone) {
        Balm.getNetworking().sendToServer(new SelectWaystoneMessage(waystone.getWaystoneUid()));
    }

    private void sortWaystone(int index, int sortDir) {
        if (index >= 0 && index < this.waystones.size()) {
            int otherIndex;
            if (Screen.hasShiftDown()) {
                otherIndex = sortDir == -1 ? -1 : this.waystones.size();
            } else {
                otherIndex = index + sortDir;
                if (otherIndex < 0 || otherIndex >= this.waystones.size()) {
                    return;
                }
            }
            PlayerWaystoneManager.swapWaystoneSorting(Minecraft.getInstance().player, index, otherIndex);
            Balm.getNetworking().sendToServer(new SortWaystoneMessage(index, otherIndex));
            this.updateList();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.isLocationHeaderHovered && ((WaystoneSelectionMenu) this.f_97732_).getWaystoneFrom() != null) {
            Balm.getNetworking().sendToServer(new RequestEditWaystoneMessage(((WaystoneSelectionMenu) this.f_97732_).getWaystoneFrom().getWaystoneUid()));
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(guiGraphics, mouseX, mouseY);
        for (ITooltipProvider tooltipProvider : this.tooltipProviders) {
            if (tooltipProvider.shouldShowTooltip()) {
                guiGraphics.renderTooltip(Minecraft.getInstance().font, tooltipProvider.getTooltipComponents(), Optional.empty(), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        IWaystone fromWaystone = ((WaystoneSelectionMenu) this.f_97732_).getWaystoneFrom();
        guiGraphics.drawCenteredString(font, this.m_96636_(), this.f_97726_ / 2, this.headerY + (fromWaystone != null ? 20 : 0), 16777215);
        if (fromWaystone != null) {
            this.drawLocationHeader(guiGraphics, fromWaystone, mouseX, mouseY, this.f_97726_ / 2, this.headerY);
        }
        if (this.waystones.size() == 0) {
            guiGraphics.drawCenteredString(font, ChatFormatting.RED + I18n.get("gui.waystones.waystone_selection.no_waystones_activated"), this.f_97726_ / 2, this.f_97727_ / 2 - 20, 16777215);
        }
    }

    private void drawLocationHeader(GuiGraphics guiGraphics, IWaystone waystone, int mouseX, int mouseY, int x, int y) {
        Font font = Minecraft.getInstance().font;
        String locationPrefix = ChatFormatting.YELLOW + I18n.get("gui.waystones.waystone_selection.current_location") + " ";
        int locationPrefixWidth = font.width(locationPrefix);
        String effectiveName = waystone.getName();
        if (effectiveName.isEmpty()) {
            effectiveName = I18n.get("gui.waystones.waystone_selection.unnamed_waystone");
        }
        int locationWidth = font.width(effectiveName);
        int fullWidth = locationPrefixWidth + locationWidth;
        int startX = x - fullWidth / 2 + locationPrefixWidth;
        int startY = y + this.f_97736_;
        this.isLocationHeaderHovered = mouseX >= startX && mouseX < startX + locationWidth + 16 && mouseY >= startY && mouseY < startY + 9;
        Player player = Minecraft.getInstance().player;
        WaystoneEditPermissions waystoneEditPermissions = PlayerWaystoneManager.mayEditWaystone(player, player.m_9236_(), waystone);
        String fullText = locationPrefix + ChatFormatting.WHITE;
        if (this.isLocationHeaderHovered && waystoneEditPermissions == WaystoneEditPermissions.ALLOW) {
            fullText = fullText + ChatFormatting.UNDERLINE;
        }
        fullText = fullText + effectiveName;
        guiGraphics.drawString(font, fullText, x - fullWidth / 2, y, 16777215);
        if (this.isLocationHeaderHovered && waystoneEditPermissions == WaystoneEditPermissions.ALLOW) {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            float scale = 0.5F;
            poseStack.translate((float) x + (float) fullWidth / 2.0F + 4.0F, (float) y, 0.0F);
            poseStack.scale(scale, scale, scale);
            guiGraphics.renderItem(new ItemStack(Items.WRITABLE_BOOK), 0, 0);
            poseStack.popPose();
        }
    }

    protected boolean allowSorting() {
        return true;
    }

    protected boolean allowDeletion() {
        return true;
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        return this.searchBox.m_93696_() && (key != 256 || !this.m_6913_()) ? this.searchBox.keyPressed(key, scanCode, modifiers) : super.keyPressed(key, scanCode, modifiers);
    }
}