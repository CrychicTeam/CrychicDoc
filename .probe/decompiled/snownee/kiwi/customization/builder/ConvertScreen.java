package snownee.kiwi.customization.builder;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.BelowOrAboveWidgetTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import snownee.kiwi.customization.network.CConvertItemPacket;
import snownee.kiwi.loader.Platform;
import snownee.kiwi.util.KHolder;
import snownee.kiwi.util.LerpedFloat;
import snownee.kiwi.util.MultilineTooltip;
import snownee.kiwi.util.NotNullByDefault;

@NotNullByDefault
public class ConvertScreen extends Screen {

    private static ConvertScreen lingeringScreen;

    private final boolean inContainer;

    private final boolean inCreativeContainer;

    @Nullable
    private final Slot slot;

    private final int slotIndex;

    private final Collection<CConvertItemPacket.Group> groups;

    private final LerpedFloat openProgress = LerpedFloat.linear();

    private PanelLayout layout;

    private final Vector2i originalMousePos;

    private final ItemStack sourceItem;

    private static Vector2i getMousePos() {
        Minecraft mc = Minecraft.getInstance();
        MouseHandler mouseHandler = mc.mouseHandler;
        return new Vector2i((int) mouseHandler.xpos(), (int) mouseHandler.ypos());
    }

    public ConvertScreen(@Nullable Screen parent, @Nullable Slot slot, int slotIndex, List<CConvertItemPacket.Group> groups) {
        super(Component.translatable("gui.kiwi.builder.convert"));
        this.slot = slot;
        this.slotIndex = slotIndex;
        this.groups = groups;
        this.inContainer = parent instanceof AbstractContainerScreen;
        this.inCreativeContainer = parent instanceof CreativeModeInventoryScreen;
        this.originalMousePos = getMousePos();
        this.openProgress.setValue(0.2F);
        this.openProgress.chase(1.0, 0.8, LerpedFloat.Chaser.EXP);
        this.sourceItem = this.getSourceItem();
    }

    private ItemStack getSourceItem() {
        if (this.slot != null) {
            return this.slot.getItem();
        } else {
            Inventory inventory = ((LocalPlayer) Objects.requireNonNull(Minecraft.getInstance().player)).m_150109_();
            return inventory.getItem(this.slotIndex);
        }
    }

    @Override
    protected void init() {
        this.layout = new PanelLayout(2);
        int step = this.inContainer ? 19 : 21;
        int xStart = 0;
        int yStart = 0;
        int curX = xStart;
        int curY = yStart;
        Set<CConvertItemPacket.Entry> accepted = Sets.newHashSet();
        LocalPlayer player = (LocalPlayer) Objects.requireNonNull(this.getMinecraft().player);
        for (CConvertItemPacket.Group group : this.groups) {
            accepted.addAll(group.entries());
        }
        int itemsPerLine = accepted.size() > 30 ? 11 : 4;
        Button cursorOn = null;
        for (CConvertItemPacket.Group group : this.groups) {
            for (CConvertItemPacket.Entry entry : group.entries()) {
                if (accepted.contains(entry)) {
                    ItemStack itemStack = new ItemStack(entry.item());
                    Button button = ItemButton.builder(itemStack, this.inContainer, btn -> {
                        Item from = this.sourceItem.getItem();
                        Item to = ((ItemButton) btn).getItem().getItem();
                        if (from == to) {
                            this.onClose();
                        } else {
                            boolean convertOne = m_96637_();
                            LocalPlayer player0 = (LocalPlayer) Objects.requireNonNull(this.getMinecraft().player);
                            if (this.inCreativeContainer && convertOne) {
                                CConvertItemPacket.send(false, -500, entry, from, true);
                            } else if (this.inCreativeContainer) {
                                Objects.requireNonNull(this.slot);
                                ItemStack newItem = to.getDefaultInstance();
                                newItem.setCount(this.slot.getItem().getCount());
                                newItem.setPopTime(5);
                                this.slot.setByPlayer(newItem);
                                NonNullList<Slot> slots = player0.f_36095_.f_38839_;
                                for (int i = 0; i < slots.size(); i++) {
                                    if (slots.get(i).getItem() == newItem) {
                                        ((MultiPlayerGameMode) Objects.requireNonNull(this.getMinecraft().gameMode)).handleCreativeModeItemAdd(newItem, i);
                                        CConvertItemPacket.playPickupSound(player0);
                                        break;
                                    }
                                }
                            } else {
                                CConvertItemPacket.send(this.inContainer, this.slotIndex, entry, from, convertOne);
                            }
                            if (!convertOne || !player0.m_7500_() && this.sourceItem.getCount() <= 1) {
                                if (this.inContainer) {
                                    GLFW.glfwSetCursorPos(this.getMinecraft().getWindow().getWindow(), (double) this.originalMousePos.x, (double) this.originalMousePos.y);
                                }
                                this.onClose();
                            }
                        }
                    }).m_252987_(curX, curY, 21, 21).build();
                    button.m_93650_(this.inContainer ? 0.2F : 0.8F);
                    List<Component> tooltip;
                    if (Platform.isProduction()) {
                        tooltip = List.of(itemStack.getHoverName());
                    } else {
                        String steps = String.join(" -> ", entry.steps().stream().map(Pair::getFirst).map(KHolder::key).map(Objects::toString).toList());
                        tooltip = List.of(itemStack.getHoverName(), Component.literal(steps).withStyle(ChatFormatting.GRAY));
                    }
                    button.m_257544_(MultilineTooltip.create(tooltip));
                    if (cursorOn == null && itemStack.is(this.sourceItem.getItem())) {
                        cursorOn = button;
                    }
                    this.layout.addWidget(button);
                    curX += step;
                    if (curX >= xStart + itemsPerLine * step) {
                        curX = xStart;
                        curY += step;
                    }
                }
            }
        }
        int x;
        int y;
        Vector2f anchor;
        if (this.inContainer) {
            x = this.f_96543_ / 2;
            y = this.f_96544_ / 2;
            anchor = new Vector2f(0.5F, 0.5F);
        } else {
            if (this.slotIndex == 40) {
                HumanoidArm humanoidarm = player.m_5737_().getOpposite();
                if (humanoidarm == HumanoidArm.LEFT) {
                    x = this.f_96543_ / 2 - 91 - 29 + 11;
                } else {
                    x = this.f_96543_ / 2 + 91 + 17;
                }
            } else {
                x = this.f_96543_ / 2 - 91 + 11 + player.m_150109_().selected * 20;
            }
            y = this.f_96544_ - 24;
            anchor = new Vector2f(0.5F, 1.0F);
        }
        this.layout.bind(this, new Vector2i(x, y), anchor);
        if (cursorOn != null) {
            Window window = this.getMinecraft().getWindow();
            double scale = window.getGuiScale();
            GLFW.glfwSetCursorPos(window.getWindow(), (double) (cursorOn.m_252754_() + 15) * scale, (double) (cursorOn.m_252907_() + 15) * scale);
        }
        Rect2i bounds = this.layout.bounds();
        StringWidget dummySpacer = new StringWidget(bounds.getX() - 2, bounds.getY() - 2, 10000, 10000, Component.empty(), this.getMinecraft().font);
        ClientTooltipPositioner tooltipPositioner = new BelowOrAboveWidgetTooltipPositioner(dummySpacer);
        for (AbstractWidget widget : this.layout.widgets()) {
            if (widget instanceof ItemButton buttonx) {
                buttonx.setTooltipPositioner(tooltipPositioner);
            }
        }
    }

    @Override
    public void tick() {
        this.openProgress.tickChaser();
        if (!this.isClosing() && !ItemStack.isSameItemSameTags(this.sourceItem, this.getSourceItem())) {
            this.onClose();
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (super.m_6375_(pMouseX, pMouseY, pButton)) {
            return true;
        } else {
            if (pButton == 0) {
                Rect2i bounds = this.layout.bounds();
                Rect2i tolerance = new Rect2i(bounds.getX() - 10, bounds.getY() - 10, bounds.getWidth() + 20, bounds.getHeight() + 20);
                if (!tolerance.contains((int) pMouseX, (int) pMouseY)) {
                    this.onClose();
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Objects.requireNonNull(this.f_96541_);
        PoseStack pose = pGuiGraphics.pose();
        this.layout.update();
        Vector2i pos = this.layout.getAnchoredPos();
        float openValue = this.openProgress.getValue(this.f_96541_.getPartialTick());
        pose.pushPose();
        pose.translate((float) pos.x, (float) pos.y, 0.0F);
        pose.scale(openValue, openValue, openValue);
        pose.translate((float) (-pos.x), (float) (-pos.y), 0.0F);
        if (this.inContainer) {
            Rect2i bounds = this.layout.bounds();
            pGuiGraphics.blitNineSliced(new ResourceLocation("textures/gui/demo_background.png"), bounds.getX() - 2, bounds.getY() - 2, bounds.getWidth() + 4, bounds.getHeight() + 4, 4, 4, 248, 166, 0, 0);
        }
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pose.popPose();
    }

    @Override
    public void setTooltipForNextRenderPass(List<FormattedCharSequence> list, ClientTooltipPositioner tooltipPositioner, boolean force) {
        float openValue = this.openProgress.getValue(((Minecraft) Objects.requireNonNull(this.f_96541_)).getPartialTick());
        if (openValue > 0.95F) {
            super.setTooltipForNextRenderPass(list, tooltipPositioner, force);
        }
    }

    @Override
    public void onClose() {
        this.openProgress.chase(0.0, 0.8, LerpedFloat.Chaser.EXP);
        lingeringScreen = this;
        super.onClose();
    }

    public boolean isClosing() {
        return this.openProgress.getChaseTarget() == 0.0F;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static void renderLingering(GuiGraphics pGuiGraphics) {
        if (lingeringScreen != null) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen == null && mc.getOverlay() == null && !lingeringScreen.openProgress.settled()) {
                lingeringScreen.render(pGuiGraphics, Integer.MAX_VALUE, Integer.MAX_VALUE, mc.getDeltaFrameTime());
            } else {
                lingeringScreen = null;
            }
        }
    }

    public static void tickLingering() {
        if (lingeringScreen != null) {
            lingeringScreen.tick();
        }
    }
}