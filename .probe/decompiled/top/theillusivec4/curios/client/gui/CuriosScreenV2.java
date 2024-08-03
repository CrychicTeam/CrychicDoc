package top.theillusivec4.curios.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
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
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketPage;
import top.theillusivec4.curios.common.network.client.CPacketToggleRender;

public class CuriosScreenV2 extends EffectRenderingInventoryScreen<CuriosContainerV2> implements RecipeUpdateListener, ICuriosScreen {

    static final ResourceLocation CURIO_INVENTORY = new ResourceLocation("curios", "textures/gui/inventory_revamp.png");

    private final RecipeBookComponent recipeBookGui = new RecipeBookComponent();

    public boolean widthTooNarrow;

    private ImageButton recipeBookButton;

    private CuriosButton buttonCurios;

    private CosmeticButton cosmeticButton;

    private PageButton nextPage;

    private PageButton prevPage;

    private boolean buttonClicked;

    private boolean isRenderButtonHovered;

    public int panelWidth = 0;

    private static int scrollCooldown = 0;

    public CuriosScreenV2(CuriosContainerV2 curiosContainer, Inventory playerInventory, Component title) {
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
        if (this.f_96541_ != null) {
            this.panelWidth = ((CuriosContainerV2) this.f_97732_).panelWidth;
            this.f_97735_ = (this.f_96543_ - this.f_97726_) / 2;
            this.f_97736_ = (this.f_96544_ - this.f_97727_) / 2;
            this.widthTooNarrow = true;
            this.recipeBookGui.init(this.f_96543_, this.f_96544_, this.f_96541_, true, (RecipeBookMenu<?>) this.f_97732_);
            this.m_7787_(this.recipeBookGui);
            this.m_264313_(this.recipeBookGui);
            if (this.getMinecraft().player != null && this.getMinecraft().player.m_7500_() && this.recipeBookGui.isVisible()) {
                this.recipeBookGui.toggleVisibility();
            }
            Tuple<Integer, Integer> offsets = getButtonOffset(false);
            this.buttonCurios = new CuriosButton(this, this.getGuiLeft() + offsets.getA(), this.f_96544_ / 2 + offsets.getB(), 14, 14, 50, 0, 14, CuriosScreen.CURIO_INVENTORY);
            if (CuriosClientConfig.CLIENT.enableButton.get()) {
                this.m_142416_(this.buttonCurios);
            }
            if (!((CuriosContainerV2) this.f_97732_).player.isCreative()) {
                this.recipeBookButton = new ImageButton(this.f_97735_ + 104, this.f_96544_ / 2 - 22, 20, 18, 0, 0, 19, CuriosScreen.RECIPE_BUTTON_TEXTURE, button -> {
                    this.recipeBookGui.toggleVisibility();
                    button.m_264152_(this.f_97735_ + 104, this.f_96544_ / 2 - 22);
                    this.buttonCurios.m_264152_(this.f_97735_ + offsets.getA(), this.f_96544_ / 2 + offsets.getB());
                });
                this.m_142416_(this.recipeBookButton);
            }
            this.updateRenderButtons();
        }
    }

    public void updateRenderButtons() {
        this.f_169368_.removeIf(widget -> widget instanceof RenderButton || widget instanceof CosmeticButton || widget instanceof PageButton);
        this.f_96540_.removeIf(widget -> widget instanceof RenderButton || widget instanceof CosmeticButton || widget instanceof PageButton);
        this.f_169369_.removeIf(widget -> widget instanceof RenderButton || widget instanceof CosmeticButton || widget instanceof PageButton);
        this.panelWidth = ((CuriosContainerV2) this.f_97732_).panelWidth;
        if (((CuriosContainerV2) this.f_97732_).hasCosmetics) {
            this.cosmeticButton = new CosmeticButton(this, this.getGuiLeft() + 17, this.getGuiTop() - 18, 20, 17);
            this.m_142416_(this.cosmeticButton);
        }
        if (((CuriosContainerV2) this.f_97732_).totalPages > 1) {
            this.nextPage = new PageButton(this, this.getGuiLeft() + 17, this.getGuiTop() + 2, 11, 12, PageButton.Type.NEXT);
            this.m_142416_(this.nextPage);
            this.prevPage = new PageButton(this, this.getGuiLeft() + 17, this.getGuiTop() + 2, 11, 12, PageButton.Type.PREVIOUS);
            this.m_142416_(this.prevPage);
        }
        for (Slot inventorySlot : ((CuriosContainerV2) this.f_97732_).f_38839_) {
            if (inventorySlot instanceof CurioSlot) {
                CurioSlot curioSlot = (CurioSlot) inventorySlot;
                if (!(inventorySlot instanceof CosmeticCurioSlot) && curioSlot.canToggleRender()) {
                    this.m_142416_(new RenderButton(curioSlot, this.f_97735_ + inventorySlot.x + 11, this.f_97736_ + inventorySlot.y - 3, 8, 8, 75, 0, 8, CURIO_INVENTORY, button -> NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketToggleRender(curioSlot.getIdentifier(), inventorySlot.getSlotIndex()))));
                }
            }
        }
    }

    @Override
    public void containerTick() {
        super.m_181908_();
        this.recipeBookGui.tick();
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
            if (scrollCooldown > 0 && this.f_96541_.player.f_19797_ % 5 == 0) {
                scrollCooldown--;
            }
            this.panelWidth = ((CuriosContainerV2) this.f_97732_).panelWidth;
            int i = this.f_97735_;
            int j = this.f_97736_;
            guiGraphics.blit(f_97725_, i, j, 0, 0, 176, this.f_97727_);
            InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, i + 51, j + 75, 30, (float) (i + 51) - (float) mouseX, (float) (j + 75 - 50) - (float) mouseY, this.f_96541_.player);
            CuriosApi.getCuriosInventory(this.f_96541_.player).ifPresent(handler -> {
                int xOffset = -33;
                int yOffset = j;
                boolean pageOffset = ((CuriosContainerV2) this.f_97732_).totalPages > 1;
                if (((CuriosContainerV2) this.f_97732_).hasCosmetics) {
                    guiGraphics.blit(CURIO_INVENTORY, i + xOffset + 2, j - 23, 32, 0, 28, 24);
                }
                List<Integer> grid = ((CuriosContainerV2) this.f_97732_).grid;
                xOffset -= (grid.size() - 1) * 18;
                for (int r = 0; r < grid.size(); r++) {
                    int rows = (Integer) grid.get(0);
                    int upperHeight = 7 + rows * 18;
                    int xTexOffset = 91;
                    if (pageOffset) {
                        upperHeight += 8;
                    }
                    if (r != 0) {
                        xTexOffset += 7;
                    }
                    guiGraphics.blit(CURIO_INVENTORY, i + xOffset, yOffset, xTexOffset, 0, 25, upperHeight);
                    guiGraphics.blit(CURIO_INVENTORY, i + xOffset, yOffset + upperHeight, xTexOffset, 159, 25, 7);
                    if (grid.size() == 1) {
                        xTexOffset += 7;
                        guiGraphics.blit(CURIO_INVENTORY, i + xOffset + 7, yOffset, xTexOffset, 0, 25, upperHeight);
                        guiGraphics.blit(CURIO_INVENTORY, i + xOffset + 7, yOffset + upperHeight, xTexOffset, 159, 25, 7);
                    }
                    if (r == 0) {
                        xOffset += 25;
                    } else {
                        xOffset += 18;
                    }
                }
                xOffset -= grid.size() * 18;
                if (pageOffset) {
                    yOffset += 8;
                }
                for (int rowsx : grid) {
                    int upperHeightx = rowsx * 18;
                    guiGraphics.blit(CURIO_INVENTORY, i + xOffset, yOffset + 7, 7, 7, 18, upperHeightx);
                    xOffset += 18;
                }
                RenderSystem.enableBlend();
                for (Slot slot : ((CuriosContainerV2) this.f_97732_).f_38839_) {
                    if (slot instanceof CurioSlot) {
                        CurioSlot curioSlot = (CurioSlot) slot;
                        if (curioSlot.isCosmetic()) {
                            guiGraphics.blit(CURIO_INVENTORY, slot.x + this.getGuiLeft() - 1, slot.y + this.getGuiTop() - 1, 32, 50, 18, 18);
                        }
                    }
                }
                RenderSystem.disableBlend();
            });
        }
    }

    @Override
    protected boolean isHovering(int rectX, int rectY, int rectWidth, int rectHeight, double pointX, double pointY) {
        return this.isRenderButtonHovered ? false : (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.m_6774_(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return this.recipeBookGui.mouseClicked(mouseX, mouseY, mouseButton) ? true : this.widthTooNarrow && this.recipeBookGui.isVisible() || super.m_6375_(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double mouseReleased1, double mouseReleased3, int mouseReleased5) {
        if (this.buttonClicked) {
            this.buttonClicked = false;
            return true;
        } else {
            return super.m_6348_(mouseReleased1, mouseReleased3, mouseReleased5);
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseScrolled1, double pMouseScrolled3, double pMouseScrolled5) {
        if (((CuriosContainerV2) this.f_97732_).totalPages > 1 && pMouseScrolled1 < (double) this.getGuiLeft() && pMouseScrolled1 > (double) (this.getGuiLeft() - this.panelWidth) && pMouseScrolled3 > (double) this.getGuiTop() && pMouseScrolled3 < (double) (this.getGuiTop() + this.f_97727_) && scrollCooldown <= 0) {
            NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketPage(((CuriosContainerV2) this.m_6262_()).f_38840_, pMouseScrolled5 < 0.0));
            scrollCooldown = 2;
        }
        return super.m_6050_(pMouseScrolled1, pMouseScrolled3, pMouseScrolled5);
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