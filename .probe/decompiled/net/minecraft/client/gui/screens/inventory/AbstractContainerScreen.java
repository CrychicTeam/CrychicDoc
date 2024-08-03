package net.minecraft.client.gui.screens.inventory;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractContainerScreen<T extends AbstractContainerMenu> extends Screen implements MenuAccess<T> {

    public static final ResourceLocation INVENTORY_LOCATION = new ResourceLocation("textures/gui/container/inventory.png");

    private static final float SNAPBACK_SPEED = 100.0F;

    private static final int QUICKDROP_DELAY = 500;

    public static final int SLOT_ITEM_BLIT_OFFSET = 100;

    private static final int HOVER_ITEM_BLIT_OFFSET = 200;

    protected int imageWidth = 176;

    protected int imageHeight = 166;

    protected int titleLabelX;

    protected int titleLabelY;

    protected int inventoryLabelX;

    protected int inventoryLabelY;

    protected final T menu;

    protected final Component playerInventoryTitle;

    @Nullable
    protected Slot hoveredSlot;

    @Nullable
    private Slot clickedSlot;

    @Nullable
    private Slot snapbackEnd;

    @Nullable
    private Slot quickdropSlot;

    @Nullable
    private Slot lastClickSlot;

    protected int leftPos;

    protected int topPos;

    private boolean isSplittingStack;

    private ItemStack draggingItem = ItemStack.EMPTY;

    private int snapbackStartX;

    private int snapbackStartY;

    private long snapbackTime;

    private ItemStack snapbackItem = ItemStack.EMPTY;

    private long quickdropTime;

    protected final Set<Slot> quickCraftSlots = Sets.newHashSet();

    protected boolean isQuickCrafting;

    private int quickCraftingType;

    private int quickCraftingButton;

    private boolean skipNextRelease;

    private int quickCraftingRemainder;

    private long lastClickTime;

    private int lastClickButton;

    private boolean doubleclick;

    private ItemStack lastQuickMoved = ItemStack.EMPTY;

    public AbstractContainerScreen(T t0, Inventory inventory1, Component component2) {
        super(component2);
        this.menu = t0;
        this.playerInventoryTitle = inventory1.m_5446_();
        this.skipNextRelease = true;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        this.leftPos = (this.f_96543_ - this.imageWidth) / 2;
        this.topPos = (this.f_96544_ - this.imageHeight) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        int $$4 = this.leftPos;
        int $$5 = this.topPos;
        this.renderBg(guiGraphics0, float3, int1, int2);
        RenderSystem.disableDepthTest();
        super.render(guiGraphics0, int1, int2, float3);
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate((float) $$4, (float) $$5, 0.0F);
        this.hoveredSlot = null;
        for (int $$6 = 0; $$6 < this.menu.slots.size(); $$6++) {
            Slot $$7 = this.menu.slots.get($$6);
            if ($$7.isActive()) {
                this.renderSlot(guiGraphics0, $$7);
            }
            if (this.isHovering($$7, (double) int1, (double) int2) && $$7.isActive()) {
                this.hoveredSlot = $$7;
                int $$8 = $$7.x;
                int $$9 = $$7.y;
                if (this.hoveredSlot.isHighlightable()) {
                    renderSlotHighlight(guiGraphics0, $$8, $$9, 0);
                }
            }
        }
        this.renderLabels(guiGraphics0, int1, int2);
        ItemStack $$10 = this.draggingItem.isEmpty() ? this.menu.getCarried() : this.draggingItem;
        if (!$$10.isEmpty()) {
            int $$11 = 8;
            int $$12 = this.draggingItem.isEmpty() ? 8 : 16;
            String $$13 = null;
            if (!this.draggingItem.isEmpty() && this.isSplittingStack) {
                $$10 = $$10.copyWithCount(Mth.ceil((float) $$10.getCount() / 2.0F));
            } else if (this.isQuickCrafting && this.quickCraftSlots.size() > 1) {
                $$10 = $$10.copyWithCount(this.quickCraftingRemainder);
                if ($$10.isEmpty()) {
                    $$13 = ChatFormatting.YELLOW + "0";
                }
            }
            this.renderFloatingItem(guiGraphics0, $$10, int1 - $$4 - 8, int2 - $$5 - $$12, $$13);
        }
        if (!this.snapbackItem.isEmpty()) {
            float $$14 = (float) (Util.getMillis() - this.snapbackTime) / 100.0F;
            if ($$14 >= 1.0F) {
                $$14 = 1.0F;
                this.snapbackItem = ItemStack.EMPTY;
            }
            int $$15 = this.snapbackEnd.x - this.snapbackStartX;
            int $$16 = this.snapbackEnd.y - this.snapbackStartY;
            int $$17 = this.snapbackStartX + (int) ((float) $$15 * $$14);
            int $$18 = this.snapbackStartY + (int) ((float) $$16 * $$14);
            this.renderFloatingItem(guiGraphics0, this.snapbackItem, $$17, $$18, null);
        }
        guiGraphics0.pose().popPose();
        RenderSystem.enableDepthTest();
    }

    public static void renderSlotHighlight(GuiGraphics guiGraphics0, int int1, int int2, int int3) {
        guiGraphics0.fillGradient(RenderType.guiOverlay(), int1, int2, int1 + 16, int2 + 16, -2130706433, -2130706433, int3);
    }

    protected void renderTooltip(GuiGraphics guiGraphics0, int int1, int int2) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            ItemStack $$3 = this.hoveredSlot.getItem();
            guiGraphics0.renderTooltip(this.f_96547_, this.getTooltipFromContainerItem($$3), $$3.getTooltipImage(), int1, int2);
        }
    }

    protected List<Component> getTooltipFromContainerItem(ItemStack itemStack0) {
        return m_280152_(this.f_96541_, itemStack0);
    }

    private void renderFloatingItem(GuiGraphics guiGraphics0, ItemStack itemStack1, int int2, int int3, String string4) {
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate(0.0F, 0.0F, 232.0F);
        guiGraphics0.renderItem(itemStack1, int2, int3);
        guiGraphics0.renderItemDecorations(this.f_96547_, itemStack1, int2, int3 - (this.draggingItem.isEmpty() ? 0 : 8), string4);
        guiGraphics0.pose().popPose();
    }

    protected void renderLabels(GuiGraphics guiGraphics0, int int1, int int2) {
        guiGraphics0.drawString(this.f_96547_, this.f_96539_, this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics0.drawString(this.f_96547_, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    protected abstract void renderBg(GuiGraphics var1, float var2, int var3, int var4);

    private void renderSlot(GuiGraphics guiGraphics0, Slot slot1) {
        int $$2 = slot1.x;
        int $$3 = slot1.y;
        ItemStack $$4 = slot1.getItem();
        boolean $$5 = false;
        boolean $$6 = slot1 == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isSplittingStack;
        ItemStack $$7 = this.menu.getCarried();
        String $$8 = null;
        if (slot1 == this.clickedSlot && !this.draggingItem.isEmpty() && this.isSplittingStack && !$$4.isEmpty()) {
            $$4 = $$4.copyWithCount($$4.getCount() / 2);
        } else if (this.isQuickCrafting && this.quickCraftSlots.contains(slot1) && !$$7.isEmpty()) {
            if (this.quickCraftSlots.size() == 1) {
                return;
            }
            if (AbstractContainerMenu.canItemQuickReplace(slot1, $$7, true) && this.menu.canDragTo(slot1)) {
                $$5 = true;
                int $$9 = Math.min($$7.getMaxStackSize(), slot1.getMaxStackSize($$7));
                int $$10 = slot1.getItem().isEmpty() ? 0 : slot1.getItem().getCount();
                int $$11 = AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, $$7) + $$10;
                if ($$11 > $$9) {
                    $$11 = $$9;
                    $$8 = ChatFormatting.YELLOW.toString() + $$9;
                }
                $$4 = $$7.copyWithCount($$11);
            } else {
                this.quickCraftSlots.remove(slot1);
                this.recalculateQuickCraftRemaining();
            }
        }
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate(0.0F, 0.0F, 100.0F);
        if ($$4.isEmpty() && slot1.isActive()) {
            Pair<ResourceLocation, ResourceLocation> $$12 = slot1.getNoItemIcon();
            if ($$12 != null) {
                TextureAtlasSprite $$13 = (TextureAtlasSprite) this.f_96541_.getTextureAtlas((ResourceLocation) $$12.getFirst()).apply((ResourceLocation) $$12.getSecond());
                guiGraphics0.blit($$2, $$3, 0, 16, 16, $$13);
                $$6 = true;
            }
        }
        if (!$$6) {
            if ($$5) {
                guiGraphics0.fill($$2, $$3, $$2 + 16, $$3 + 16, -2130706433);
            }
            guiGraphics0.renderItem($$4, $$2, $$3, slot1.x + slot1.y * this.imageWidth);
            guiGraphics0.renderItemDecorations(this.f_96547_, $$4, $$2, $$3, $$8);
        }
        guiGraphics0.pose().popPose();
    }

    private void recalculateQuickCraftRemaining() {
        ItemStack $$0 = this.menu.getCarried();
        if (!$$0.isEmpty() && this.isQuickCrafting) {
            if (this.quickCraftingType == 2) {
                this.quickCraftingRemainder = $$0.getMaxStackSize();
            } else {
                this.quickCraftingRemainder = $$0.getCount();
                for (Slot $$1 : this.quickCraftSlots) {
                    ItemStack $$2 = $$1.getItem();
                    int $$3 = $$2.isEmpty() ? 0 : $$2.getCount();
                    int $$4 = Math.min($$0.getMaxStackSize(), $$1.getMaxStackSize($$0));
                    int $$5 = Math.min(AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, $$0) + $$3, $$4);
                    this.quickCraftingRemainder -= $$5 - $$3;
                }
            }
        }
    }

    @Nullable
    private Slot findSlot(double double0, double double1) {
        for (int $$2 = 0; $$2 < this.menu.slots.size(); $$2++) {
            Slot $$3 = this.menu.slots.get($$2);
            if (this.isHovering($$3, double0, double1) && $$3.isActive()) {
                return $$3;
            }
        }
        return null;
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (super.m_6375_(double0, double1, int2)) {
            return true;
        } else {
            boolean $$3 = this.f_96541_.options.keyPickItem.matchesMouse(int2) && this.f_96541_.gameMode.hasInfiniteItems();
            Slot $$4 = this.findSlot(double0, double1);
            long $$5 = Util.getMillis();
            this.doubleclick = this.lastClickSlot == $$4 && $$5 - this.lastClickTime < 250L && this.lastClickButton == int2;
            this.skipNextRelease = false;
            if (int2 != 0 && int2 != 1 && !$$3) {
                this.checkHotbarMouseClicked(int2);
            } else {
                int $$6 = this.leftPos;
                int $$7 = this.topPos;
                boolean $$8 = this.hasClickedOutside(double0, double1, $$6, $$7, int2);
                int $$9 = -1;
                if ($$4 != null) {
                    $$9 = $$4.index;
                }
                if ($$8) {
                    $$9 = -999;
                }
                if (this.f_96541_.options.touchscreen().get() && $$8 && this.menu.getCarried().isEmpty()) {
                    this.onClose();
                    return true;
                }
                if ($$9 != -1) {
                    if (this.f_96541_.options.touchscreen().get()) {
                        if ($$4 != null && $$4.hasItem()) {
                            this.clickedSlot = $$4;
                            this.draggingItem = ItemStack.EMPTY;
                            this.isSplittingStack = int2 == 1;
                        } else {
                            this.clickedSlot = null;
                        }
                    } else if (!this.isQuickCrafting) {
                        if (this.menu.getCarried().isEmpty()) {
                            if ($$3) {
                                this.slotClicked($$4, $$9, int2, ClickType.CLONE);
                            } else {
                                boolean $$10 = $$9 != -999 && (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344));
                                ClickType $$11 = ClickType.PICKUP;
                                if ($$10) {
                                    this.lastQuickMoved = $$4 != null && $$4.hasItem() ? $$4.getItem().copy() : ItemStack.EMPTY;
                                    $$11 = ClickType.QUICK_MOVE;
                                } else if ($$9 == -999) {
                                    $$11 = ClickType.THROW;
                                }
                                this.slotClicked($$4, $$9, int2, $$11);
                            }
                            this.skipNextRelease = true;
                        } else {
                            this.isQuickCrafting = true;
                            this.quickCraftingButton = int2;
                            this.quickCraftSlots.clear();
                            if (int2 == 0) {
                                this.quickCraftingType = 0;
                            } else if (int2 == 1) {
                                this.quickCraftingType = 1;
                            } else if ($$3) {
                                this.quickCraftingType = 2;
                            }
                        }
                    }
                }
            }
            this.lastClickSlot = $$4;
            this.lastClickTime = $$5;
            this.lastClickButton = int2;
            return true;
        }
    }

    private void checkHotbarMouseClicked(int int0) {
        if (this.hoveredSlot != null && this.menu.getCarried().isEmpty()) {
            if (this.f_96541_.options.keySwapOffhand.matchesMouse(int0)) {
                this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 40, ClickType.SWAP);
                return;
            }
            for (int $$1 = 0; $$1 < 9; $$1++) {
                if (this.f_96541_.options.keyHotbarSlots[$$1].matchesMouse(int0)) {
                    this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, $$1, ClickType.SWAP);
                }
            }
        }
    }

    protected boolean hasClickedOutside(double double0, double double1, int int2, int int3, int int4) {
        return double0 < (double) int2 || double1 < (double) int3 || double0 >= (double) (int2 + this.imageWidth) || double1 >= (double) (int3 + this.imageHeight);
    }

    @Override
    public boolean mouseDragged(double double0, double double1, int int2, double double3, double double4) {
        Slot $$5 = this.findSlot(double0, double1);
        ItemStack $$6 = this.menu.getCarried();
        if (this.clickedSlot != null && this.f_96541_.options.touchscreen().get()) {
            if (int2 == 0 || int2 == 1) {
                if (this.draggingItem.isEmpty()) {
                    if ($$5 != this.clickedSlot && !this.clickedSlot.getItem().isEmpty()) {
                        this.draggingItem = this.clickedSlot.getItem().copy();
                    }
                } else if (this.draggingItem.getCount() > 1 && $$5 != null && AbstractContainerMenu.canItemQuickReplace($$5, this.draggingItem, false)) {
                    long $$7 = Util.getMillis();
                    if (this.quickdropSlot == $$5) {
                        if ($$7 - this.quickdropTime > 500L) {
                            this.slotClicked(this.clickedSlot, this.clickedSlot.index, 0, ClickType.PICKUP);
                            this.slotClicked($$5, $$5.index, 1, ClickType.PICKUP);
                            this.slotClicked(this.clickedSlot, this.clickedSlot.index, 0, ClickType.PICKUP);
                            this.quickdropTime = $$7 + 750L;
                            this.draggingItem.shrink(1);
                        }
                    } else {
                        this.quickdropSlot = $$5;
                        this.quickdropTime = $$7;
                    }
                }
            }
        } else if (this.isQuickCrafting && $$5 != null && !$$6.isEmpty() && ($$6.getCount() > this.quickCraftSlots.size() || this.quickCraftingType == 2) && AbstractContainerMenu.canItemQuickReplace($$5, $$6, true) && $$5.mayPlace($$6) && this.menu.canDragTo($$5)) {
            this.quickCraftSlots.add($$5);
            this.recalculateQuickCraftRemaining();
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double double0, double double1, int int2) {
        Slot $$3 = this.findSlot(double0, double1);
        int $$4 = this.leftPos;
        int $$5 = this.topPos;
        boolean $$6 = this.hasClickedOutside(double0, double1, $$4, $$5, int2);
        int $$7 = -1;
        if ($$3 != null) {
            $$7 = $$3.index;
        }
        if ($$6) {
            $$7 = -999;
        }
        if (this.doubleclick && $$3 != null && int2 == 0 && this.menu.canTakeItemForPickAll(ItemStack.EMPTY, $$3)) {
            if (m_96638_()) {
                if (!this.lastQuickMoved.isEmpty()) {
                    for (Slot $$8 : this.menu.slots) {
                        if ($$8 != null && $$8.mayPickup(this.f_96541_.player) && $$8.hasItem() && $$8.container == $$3.container && AbstractContainerMenu.canItemQuickReplace($$8, this.lastQuickMoved, true)) {
                            this.slotClicked($$8, $$8.index, int2, ClickType.QUICK_MOVE);
                        }
                    }
                }
            } else {
                this.slotClicked($$3, $$7, int2, ClickType.PICKUP_ALL);
            }
            this.doubleclick = false;
            this.lastClickTime = 0L;
        } else {
            if (this.isQuickCrafting && this.quickCraftingButton != int2) {
                this.isQuickCrafting = false;
                this.quickCraftSlots.clear();
                this.skipNextRelease = true;
                return true;
            }
            if (this.skipNextRelease) {
                this.skipNextRelease = false;
                return true;
            }
            if (this.clickedSlot != null && this.f_96541_.options.touchscreen().get()) {
                if (int2 == 0 || int2 == 1) {
                    if (this.draggingItem.isEmpty() && $$3 != this.clickedSlot) {
                        this.draggingItem = this.clickedSlot.getItem();
                    }
                    boolean $$9 = AbstractContainerMenu.canItemQuickReplace($$3, this.draggingItem, false);
                    if ($$7 != -1 && !this.draggingItem.isEmpty() && $$9) {
                        this.slotClicked(this.clickedSlot, this.clickedSlot.index, int2, ClickType.PICKUP);
                        this.slotClicked($$3, $$7, 0, ClickType.PICKUP);
                        if (this.menu.getCarried().isEmpty()) {
                            this.snapbackItem = ItemStack.EMPTY;
                        } else {
                            this.slotClicked(this.clickedSlot, this.clickedSlot.index, int2, ClickType.PICKUP);
                            this.snapbackStartX = Mth.floor(double0 - (double) $$4);
                            this.snapbackStartY = Mth.floor(double1 - (double) $$5);
                            this.snapbackEnd = this.clickedSlot;
                            this.snapbackItem = this.draggingItem;
                            this.snapbackTime = Util.getMillis();
                        }
                    } else if (!this.draggingItem.isEmpty()) {
                        this.snapbackStartX = Mth.floor(double0 - (double) $$4);
                        this.snapbackStartY = Mth.floor(double1 - (double) $$5);
                        this.snapbackEnd = this.clickedSlot;
                        this.snapbackItem = this.draggingItem;
                        this.snapbackTime = Util.getMillis();
                    }
                    this.clearDraggingState();
                }
            } else if (this.isQuickCrafting && !this.quickCraftSlots.isEmpty()) {
                this.slotClicked(null, -999, AbstractContainerMenu.getQuickcraftMask(0, this.quickCraftingType), ClickType.QUICK_CRAFT);
                for (Slot $$10 : this.quickCraftSlots) {
                    this.slotClicked($$10, $$10.index, AbstractContainerMenu.getQuickcraftMask(1, this.quickCraftingType), ClickType.QUICK_CRAFT);
                }
                this.slotClicked(null, -999, AbstractContainerMenu.getQuickcraftMask(2, this.quickCraftingType), ClickType.QUICK_CRAFT);
            } else if (!this.menu.getCarried().isEmpty()) {
                if (this.f_96541_.options.keyPickItem.matchesMouse(int2)) {
                    this.slotClicked($$3, $$7, int2, ClickType.CLONE);
                } else {
                    boolean $$11 = $$7 != -999 && (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344));
                    if ($$11) {
                        this.lastQuickMoved = $$3 != null && $$3.hasItem() ? $$3.getItem().copy() : ItemStack.EMPTY;
                    }
                    this.slotClicked($$3, $$7, int2, $$11 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
                }
            }
        }
        if (this.menu.getCarried().isEmpty()) {
            this.lastClickTime = 0L;
        }
        this.isQuickCrafting = false;
        return true;
    }

    public void clearDraggingState() {
        this.draggingItem = ItemStack.EMPTY;
        this.clickedSlot = null;
    }

    private boolean isHovering(Slot slot0, double double1, double double2) {
        return this.isHovering(slot0.x, slot0.y, 16, 16, double1, double2);
    }

    protected boolean isHovering(int int0, int int1, int int2, int int3, double double4, double double5) {
        int $$6 = this.leftPos;
        int $$7 = this.topPos;
        double4 -= (double) $$6;
        double5 -= (double) $$7;
        return double4 >= (double) (int0 - 1) && double4 < (double) (int0 + int2 + 1) && double5 >= (double) (int1 - 1) && double5 < (double) (int1 + int3 + 1);
    }

    protected void slotClicked(Slot slot0, int int1, int int2, ClickType clickType3) {
        if (slot0 != null) {
            int1 = slot0.index;
        }
        this.f_96541_.gameMode.handleInventoryMouseClick(this.menu.containerId, int1, int2, clickType3, this.f_96541_.player);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (super.keyPressed(int0, int1, int2)) {
            return true;
        } else if (this.f_96541_.options.keyInventory.matches(int0, int1)) {
            this.onClose();
            return true;
        } else {
            this.checkHotbarKeyPressed(int0, int1);
            if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
                if (this.f_96541_.options.keyPickItem.matches(int0, int1)) {
                    this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 0, ClickType.CLONE);
                } else if (this.f_96541_.options.keyDrop.matches(int0, int1)) {
                    this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, m_96637_() ? 1 : 0, ClickType.THROW);
                }
            }
            return true;
        }
    }

    protected boolean checkHotbarKeyPressed(int int0, int int1) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null) {
            if (this.f_96541_.options.keySwapOffhand.matches(int0, int1)) {
                this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 40, ClickType.SWAP);
                return true;
            }
            for (int $$2 = 0; $$2 < 9; $$2++) {
                if (this.f_96541_.options.keyHotbarSlots[$$2].matches(int0, int1)) {
                    this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, $$2, ClickType.SWAP);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void removed() {
        if (this.f_96541_.player != null) {
            this.menu.removed(this.f_96541_.player);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public final void tick() {
        super.tick();
        if (this.f_96541_.player.m_6084_() && !this.f_96541_.player.m_213877_()) {
            this.containerTick();
        } else {
            this.f_96541_.player.closeContainer();
        }
    }

    protected void containerTick() {
    }

    @Override
    public T getMenu() {
        return this.menu;
    }

    @Override
    public void onClose() {
        this.f_96541_.player.closeContainer();
        super.onClose();
    }
}