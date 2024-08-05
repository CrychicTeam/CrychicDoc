package top.theillusivec4.curios.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.client.ICuriosScreen;
import top.theillusivec4.curios.client.CuriosClientConfig;
import top.theillusivec4.curios.client.KeyRegistry;
import top.theillusivec4.curios.common.inventory.CosmeticCurioSlot;
import top.theillusivec4.curios.common.inventory.CurioSlot;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketToggleRender;

public class CuriosScreen extends EffectRenderingInventoryScreen<CuriosContainer> implements RecipeUpdateListener, ICuriosScreen {

    static final ResourceLocation CURIO_INVENTORY = new ResourceLocation("curios", "textures/gui/inventory.png");

    static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("minecraft:textures/gui/recipe_button.png");

    private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    private static float currentScroll;

    private final RecipeBookComponent recipeBookGui = new RecipeBookComponent();

    public boolean hasScrollBar;

    public boolean widthTooNarrow;

    private CuriosButton buttonCurios;

    private boolean isScrolling;

    private boolean buttonClicked;

    private boolean isRenderButtonHovered;

    public CuriosScreen(CuriosContainer curiosContainer, Inventory playerInventory, Component title) {
        super(curiosContainer, playerInventory, title);
    }

    public static Tuple<Integer, Integer> getButtonOffset(boolean isCreative) {
        CuriosClientConfig.Client client = CuriosClientConfig.CLIENT;
        CuriosClientConfig.Client.ButtonCorner corner = (CuriosClientConfig.Client.ButtonCorner) client.buttonCorner.get();
        int x = 0;
        int y = 0;
        if (isCreative) {
            x += corner.getCreativeXoffset() + client.creativeButtonXOffset.get();
            y += corner.getCreativeYoffset() + client.creativeButtonYOffset.get();
        } else {
            x += corner.getXoffset() + client.buttonXOffset.get();
            y += corner.getYoffset() + client.buttonYOffset.get();
        }
        return new Tuple<>(x, y);
    }

    @Override
    public void init() {
        super.m_7856_();
        if (this.f_96541_ != null) {
            if (this.f_96541_.player != null) {
                this.hasScrollBar = (Boolean) CuriosApi.getCuriosInventory(this.f_96541_.player).map(handler -> handler.getVisibleSlots() > 8).orElse(false);
                if (this.hasScrollBar) {
                    ((CuriosContainer) this.f_97732_).scrollTo(currentScroll);
                }
            }
            int neededWidth = 431;
            if (this.hasScrollBar) {
                neededWidth += 30;
            }
            if (((CuriosContainer) this.f_97732_).hasCosmeticColumn()) {
                neededWidth += 40;
            }
            this.widthTooNarrow = this.f_96543_ < neededWidth;
            this.recipeBookGui.init(this.f_96543_, this.f_96544_, this.f_96541_, this.widthTooNarrow, (RecipeBookMenu<?>) this.f_97732_);
            this.updateScreenPosition();
            this.m_7787_(this.recipeBookGui);
            this.m_264313_(this.recipeBookGui);
            if (this.getMinecraft().player != null && this.getMinecraft().player.m_7500_() && this.recipeBookGui.isVisible()) {
                this.recipeBookGui.toggleVisibility();
                this.updateScreenPosition();
            }
            Tuple<Integer, Integer> offsets = getButtonOffset(false);
            this.buttonCurios = new CuriosButton(this, this.getGuiLeft() + offsets.getA(), this.f_96544_ / 2 + offsets.getB(), 14, 14, 50, 0, 14, CURIO_INVENTORY);
            if (CuriosClientConfig.CLIENT.enableButton.get()) {
                this.m_142416_(this.buttonCurios);
            }
            if (!((CuriosContainer) this.f_97732_).player.isCreative()) {
                this.m_142416_(new ImageButton(this.f_97735_ + 104, this.f_96544_ / 2 - 22, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, button -> {
                    this.recipeBookGui.toggleVisibility();
                    this.updateScreenPosition();
                    button.m_264152_(this.f_97735_ + 104, this.f_96544_ / 2 - 22);
                    this.buttonCurios.m_264152_(this.f_97735_ + offsets.getA(), this.f_96544_ / 2 + offsets.getB());
                }));
            }
            this.updateRenderButtons();
        }
    }

    public void updateRenderButtons() {
        this.f_169368_.removeIf(widget -> widget instanceof RenderButton);
        this.f_96540_.removeIf(widget -> widget instanceof RenderButton);
        this.f_169369_.removeIf(widget -> widget instanceof RenderButton);
        for (Slot inventorySlot : ((CuriosContainer) this.f_97732_).f_38839_) {
            if (inventorySlot instanceof CurioSlot) {
                CurioSlot curioSlot = (CurioSlot) inventorySlot;
                if (!(inventorySlot instanceof CosmeticCurioSlot) && curioSlot.canToggleRender()) {
                    this.m_142416_(new RenderButton(curioSlot, this.f_97735_ + inventorySlot.x + 11, this.f_97736_ + inventorySlot.y - 3, 8, 8, 75, 0, 8, CURIO_INVENTORY, button -> NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketToggleRender(curioSlot.getIdentifier(), inventorySlot.getSlotIndex()))));
                }
            }
        }
    }

    private void updateScreenPosition() {
        int i;
        if (this.recipeBookGui.isVisible() && !this.widthTooNarrow) {
            int offset = 148;
            if (this.hasScrollBar) {
                offset -= 30;
            }
            if (((CuriosContainer) this.f_97732_).hasCosmeticColumn()) {
                offset -= 40;
            }
            i = 177 + (this.f_96543_ - this.f_97726_ - offset) / 2;
        } else {
            i = (this.f_96543_ - this.f_97726_) / 2;
        }
        this.f_97735_ = i;
        this.updateRenderButtons();
    }

    @Override
    public void containerTick() {
        super.m_181908_();
        this.recipeBookGui.tick();
    }

    private boolean inScrollBar(double mouseX, double mouseY) {
        int i = this.f_97735_;
        int j = this.f_97736_;
        int k = i - 34;
        int l = j + 12;
        int i1 = k + 14;
        int j1 = l + 139;
        if (((CuriosContainer) this.f_97732_).hasCosmeticColumn()) {
            i1 -= 19;
            k -= 19;
        }
        return mouseX >= (double) k && mouseY >= (double) l && mouseX < (double) i1 && mouseY < (double) j1;
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
            this.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
            this.recipeBookGui.render(guiGraphics, mouseX, mouseY, partialTicks);
        } else {
            this.recipeBookGui.render(guiGraphics, mouseX, mouseY, partialTicks);
            super.render(guiGraphics, mouseX, mouseY, partialTicks);
            this.recipeBookGui.renderGhostRecipe(guiGraphics, this.f_97735_, this.f_97736_, true, partialTicks);
            boolean isButtonHovered = false;
            for (Renderable button : this.f_169369_) {
                if (button instanceof RenderButton) {
                    ((RenderButton) button).renderButtonOverlay(guiGraphics, mouseX, mouseY, partialTicks);
                    if (((RenderButton) button).m_274382_()) {
                        isButtonHovered = true;
                    }
                }
            }
            this.isRenderButtonHovered = isButtonHovered;
            LocalPlayer clientPlayer = Minecraft.getInstance().player;
            if (!this.isRenderButtonHovered && clientPlayer != null && clientPlayer.f_36095_.m_142621_().isEmpty() && this.getSlotUnderMouse() != null) {
                Slot slot = this.getSlotUnderMouse();
                if (slot instanceof CurioSlot slotCurio && !slot.hasItem()) {
                    guiGraphics.renderTooltip(this.f_96547_, Component.literal(slotCurio.getSlotName()), mouseX, mouseY);
                }
            }
        }
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Minecraft mc = this.f_96541_;
        if (mc != null) {
            LocalPlayer clientPlayer = mc.player;
            if (clientPlayer != null && clientPlayer.f_36095_.m_142621_().isEmpty()) {
                if (this.isRenderButtonHovered) {
                    guiGraphics.renderTooltip(this.f_96547_, Component.translatable("gui.curios.toggle"), mouseX, mouseY);
                } else if (this.f_97734_ != null && this.f_97734_.hasItem()) {
                    guiGraphics.renderTooltip(this.f_96547_, this.f_97734_.getItem(), mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
            this.recipeBookGui.toggleVisibility();
            this.updateScreenPosition();
            return true;
        } else if (KeyRegistry.openCurios.isActiveAndMatches(InputConstants.getKey(p_keyPressed_1_, p_keyPressed_2_))) {
            LocalPlayer playerEntity = this.getMinecraft().player;
            if (playerEntity != null) {
                playerEntity.closeContainer();
            }
            return true;
        } else {
            return super.m_7933_(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
        }
    }

    @Override
    protected void renderLabels(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.f_96541_ != null && this.f_96541_.player != null) {
            guiGraphics.drawString(this.f_96547_, this.f_96539_, 97, 6, 4210752, false);
        }
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        if (this.f_96541_ != null && this.f_96541_.player != null) {
            int i = this.f_97735_;
            int j = this.f_97736_;
            guiGraphics.blit(f_97725_, i, j, 0, 0, this.f_97726_, this.f_97727_);
            InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, i + 51, j + 75, 30, (float) (i + 51) - (float) mouseX, (float) (j + 75 - 50) - (float) mouseY, this.f_96541_.player);
            CuriosApi.getCuriosInventory(this.f_96541_.player).ifPresent(handler -> {
                int slotCount = handler.getVisibleSlots();
                if (slotCount > 0) {
                    int upperHeight = 7 + Math.min(slotCount, 9) * 18;
                    int xTexOffset = 0;
                    int width = 27;
                    int xOffset = -26;
                    if (((CuriosContainer) this.f_97732_).hasCosmeticColumn()) {
                        xTexOffset = 92;
                        width = 46;
                        xOffset -= 19;
                    }
                    guiGraphics.blit(CURIO_INVENTORY, i + xOffset, j + 4, xTexOffset, 0, width, upperHeight);
                    if (slotCount <= 8) {
                        guiGraphics.blit(CURIO_INVENTORY, i + xOffset, j + 4 + upperHeight, xTexOffset, 151, width, 7);
                    } else {
                        guiGraphics.blit(CURIO_INVENTORY, i + xOffset - 16, j + 4, 27, 0, 23, 158);
                        guiGraphics.blit(CREATIVE_INVENTORY_TABS, i + xOffset - 8, j + 12 + (int) (127.0F * currentScroll), 232, 0, 12, 15);
                    }
                    for (Slot slot : ((CuriosContainer) this.f_97732_).f_38839_) {
                        if (slot instanceof CosmeticCurioSlot) {
                            int x = this.f_97735_ + slot.x - 1;
                            int y = this.f_97736_ + slot.y - 1;
                            guiGraphics.blit(CURIO_INVENTORY, x, y, 138, 0, 18, 18);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected boolean isHovering(int rectX, int rectY, int rectWidth, int rectHeight, double pointX, double pointY) {
        return this.isRenderButtonHovered ? false : (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.m_6774_(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.recipeBookGui.mouseClicked(mouseX, mouseY, mouseButton)) {
            return true;
        } else if (this.inScrollBar(mouseX, mouseY)) {
            this.isScrolling = this.needsScrollBars();
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookGui.isVisible() || super.m_6375_(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public boolean mouseReleased(double mouseReleased1, double mouseReleased3, int mouseReleased5) {
        if (mouseReleased5 == 0) {
            this.isScrolling = false;
        }
        if (this.buttonClicked) {
            this.buttonClicked = false;
            return true;
        } else {
            return super.m_6348_(mouseReleased1, mouseReleased3, mouseReleased5);
        }
    }

    @Override
    public boolean mouseDragged(double pMouseDragged1, double pMouseDragged3, int pMouseDragged5, double pMouseDragged6, double pMouseDragged8) {
        if (this.isScrolling) {
            int i = this.f_97736_ + 8;
            int j = i + 148;
            currentScroll = ((float) pMouseDragged3 - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            currentScroll = Mth.clamp(currentScroll, 0.0F, 1.0F);
            ((CuriosContainer) this.f_97732_).scrollTo(currentScroll);
            return true;
        } else {
            return super.m_7979_(pMouseDragged1, pMouseDragged3, pMouseDragged5, pMouseDragged6, pMouseDragged8);
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseScrolled1, double pMouseScrolled3, double pMouseScrolled5) {
        if (!this.needsScrollBars()) {
            return false;
        } else {
            int i = (Integer) ((CuriosContainer) this.f_97732_).curiosHandler.map(inv -> (int) Math.floor((double) inv.getVisibleSlots() / 8.0)).orElse(1);
            currentScroll = (float) ((double) currentScroll - pMouseScrolled5 / (double) i);
            currentScroll = Mth.clamp(currentScroll, 0.0F, 1.0F);
            ((CuriosContainer) this.f_97732_).scrollTo(currentScroll);
            return true;
        }
    }

    private boolean needsScrollBars() {
        return ((CuriosContainer) this.f_97732_).canScroll();
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        boolean flag = mouseX < (double) guiLeftIn || mouseY < (double) guiTopIn || mouseX >= (double) (guiLeftIn + this.f_97726_) || mouseY >= (double) (guiTopIn + this.f_97727_);
        return this.recipeBookGui.hasClickedOutside(mouseX, mouseY, this.f_97735_, this.f_97736_, this.f_97726_, this.f_97727_, mouseButton) && flag;
    }

    @Override
    protected void slotClicked(@Nonnull Slot slotIn, int slotId, int mouseButton, @Nonnull ClickType type) {
        super.m_6597_(slotIn, slotId, mouseButton, type);
        this.recipeBookGui.slotClicked(slotIn);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookGui.recipesUpdated();
    }

    @Nonnull
    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookGui;
    }
}