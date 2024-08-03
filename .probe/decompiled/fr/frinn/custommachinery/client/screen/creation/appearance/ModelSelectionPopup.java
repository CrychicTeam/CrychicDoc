package fr.frinn.custommachinery.client.screen.creation.appearance;

import com.mojang.blaze3d.platform.Lighting;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.SuggestedEditBox;
import fr.frinn.custommachinery.common.util.MachineModelLocation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class ModelSelectionPopup extends PopupScreen {

    private static final ResourceLocation EXIT_TEXTURE = new ResourceLocation("custommachinery", "textures/gui/config/exit_button.png");

    private final Supplier<MachineModelLocation> supplier;

    private final Consumer<MachineModelLocation> consumer;

    private final boolean isBlock;

    private SuggestedEditBox box;

    private ModelSelectionPopup.ModelSelectionList selectionList;

    private Checkbox blocks;

    private Checkbox items;

    private Checkbox models;

    public ModelSelectionPopup(BaseScreen parent, Supplier<MachineModelLocation> supplier, Consumer<MachineModelLocation> consumer, boolean isBlock) {
        super(parent, 200, 230);
        this.supplier = supplier;
        this.consumer = consumer;
        this.isBlock = isBlock;
    }

    public void refreshBoxSuggestions() {
        this.box.clearSuggestions();
        if (this.blocks.selected()) {
            this.box.addSuggestions(BuiltInRegistries.BLOCK.m_6566_().stream().map(ResourceLocation::toString).toList());
        }
        if (this.items.selected()) {
            this.box.addSuggestions(BuiltInRegistries.ITEM.m_6566_().stream().map(ResourceLocation::toString).toList());
        }
        if (this.models.selected()) {
            this.box.addSuggestions(Minecraft.getInstance().getModelManager().bakedRegistry.keySet().stream().map(ResourceLocation::toString).toList());
        }
        this.selectionList.setList(this.box.getSuggestions().stream().limit(100L).map(s -> MachineModelLocation.of(s.getText())).toList());
    }

    @Override
    protected void init() {
        super.init();
        this.m_142416_(new ImageButton(this.x + 5, this.y + 5, 9, 9, 0, 0, 9, EXIT_TEXTURE, 9, 18, button -> this.parent.closePopup(this)));
        this.blocks = (Checkbox) this.m_142416_(new Checkbox(this.x + 5, this.y + 45, 20, 20, Component.literal("Blocks"), this.isBlock) {

            @Override
            public void onPress() {
                super.onPress();
                ModelSelectionPopup.this.refreshBoxSuggestions();
            }
        });
        this.items = (Checkbox) this.m_142416_(new Checkbox(this.x + 65, this.y + 45, 20, 20, Component.literal("Items"), !this.isBlock) {

            @Override
            public void onPress() {
                super.onPress();
                ModelSelectionPopup.this.refreshBoxSuggestions();
            }
        });
        this.models = (Checkbox) this.m_142416_(new Checkbox(this.x + 125, this.y + 45, 20, 20, Component.literal("Models"), false) {

            @Override
            public void onPress() {
                super.onPress();
                ModelSelectionPopup.this.refreshBoxSuggestions();
            }
        });
        int var10005 = this.x + 5;
        int var10006 = this.y + 70;
        int var10007 = this.xSize - 10;
        this.box = (SuggestedEditBox) this.m_142416_(new SuggestedEditBox(Minecraft.getInstance().font, var10005, var10006, var10007, 20, this.f_96539_, 5));
        this.box.m_94199_(Integer.MAX_VALUE);
        this.box.m_94144_(((MachineModelLocation) this.supplier.get()).toString());
        this.box.m_94198_();
        this.selectionList = (ModelSelectionPopup.ModelSelectionList) this.m_142416_(new ModelSelectionPopup.ModelSelectionList(this.x + 5, this.y + 90, this.xSize - 10, this.ySize - 95));
        this.selectionList.setResponder(this.consumer);
        this.box.setResponder(value -> this.selectionList.setList(this.box.getSuggestions().stream().limit(100L).map(s -> MachineModelLocation.of(s.getText())).toList()));
        this.refreshBoxSuggestions();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderModel(graphics, (float) this.x + (float) this.xSize / 2.0F - 8.0F, (float) (this.y + 20), (MachineModelLocation) this.supplier.get(), 32.0F);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    public static void renderModel(GuiGraphics graphics, float x, float y, MachineModelLocation loc, float scale) {
        BakedModel model = Minecraft.getInstance().getModelManager().getMissingModel();
        if (loc.getState() != null) {
            model = Minecraft.getInstance().getBlockRenderer().getBlockModel(loc.getState());
        } else if (loc.getItem() != null) {
            model = Minecraft.getInstance().getItemRenderer().getModel(loc.getItem().getDefaultInstance(), Minecraft.getInstance().level, Minecraft.getInstance().player, 42);
        } else if (loc.getLoc() != null && loc.getProperties() != null) {
            model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(loc.getLoc(), loc.getProperties()));
        } else if (loc.getLoc() != null) {
            model = (BakedModel) Minecraft.getInstance().getModelManager().bakedRegistry.getOrDefault(loc.getLoc(), model);
        }
        RenderType renderType = RenderType.translucent();
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 50.0F);
        graphics.pose().scale(scale, scale, scale);
        model.getTransforms().getTransform(ItemDisplayContext.GUI).apply(false, graphics.pose());
        if (loc.getState() != null) {
            graphics.pose().mulPose(new Quaternionf().fromAxisAngleDeg(0.0F, 1.0F, 0.0F, 90.0F));
            ItemBlockRenderTypes.getRenderType(loc.getState(), true);
            Lighting.setupFor3DItems();
        }
        graphics.pose().translate(-0.5F, -0.5F, -0.5F);
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(graphics.pose().last(), graphics.bufferSource().getBuffer(renderType), null, model, 1.0F, 1.0F, 1.0F, 15728880, OverlayTexture.NO_OVERLAY);
        if (loc.getState() != null) {
            Lighting.setupForFlatItems();
        }
        graphics.pose().popPose();
    }

    public static class ModelSelectionList extends AbstractWidget {

        private final List<MachineModelLocation> list = new ArrayList();

        private final int maxColumns;

        private Consumer<MachineModelLocation> responder;

        private MachineModelLocation selected;

        private double scrollAmount;

        private boolean scrolling = false;

        public ModelSelectionList(int x, int y, int width, int height) {
            super(x, y, width, height, Component.empty());
            this.maxColumns = width / 20;
        }

        public void setList(List<MachineModelLocation> list) {
            this.list.clear();
            this.list.addAll(list);
        }

        public void setResponder(Consumer<MachineModelLocation> responder) {
            this.responder = responder;
        }

        @Nullable
        public MachineModelLocation getElementUnderMouse(double mouseX, double mouseY) {
            if (!(mouseX < (double) this.m_252754_()) && !(mouseX > (double) (this.m_252754_() + this.maxColumns * 20)) && !(mouseY < (double) this.m_252907_()) && !(mouseY > (double) (this.m_252907_() + this.m_93694_()))) {
                int index = (int) ((mouseY - (double) this.m_252907_() + this.scrollAmount) / 20.0) * this.maxColumns + (int) ((mouseX - (double) this.m_252754_()) / 20.0);
                return index >= 0 && index < this.list.size() ? (MachineModelLocation) this.list.get(index) : null;
            } else {
                return null;
            }
        }

        private void scroll(int scroll) {
            this.setScrollAmount(this.getScrollAmount() + (double) scroll);
        }

        public double getScrollAmount() {
            return this.scrollAmount;
        }

        public void setScrollAmount(double scroll) {
            this.scrollAmount = Mth.clamp(scroll, 0.0, (double) this.getMaxScroll());
        }

        public int getMaxScroll() {
            return Math.max(0, this.getMaxPosition() - this.m_93694_() - 4);
        }

        protected int getMaxPosition() {
            return this.list.size() / this.maxColumns * 20;
        }

        public int getScrollBottom() {
            return (int) this.getScrollAmount() - this.m_93694_();
        }

        protected void updateScrollingState(double mouseX, double mouseY, int button) {
            this.scrolling = button == 0 && mouseX >= (double) this.getScrollbarPosition() && mouseX < (double) (this.getScrollbarPosition() + 6);
        }

        protected int getScrollbarPosition() {
            return this.f_93618_ / 2 + 124;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.enableScissor(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.m_5711_(), this.m_252907_() + this.m_93694_());
            graphics.pose().pushPose();
            graphics.pose().translate((float) this.m_252754_(), (float) this.m_252907_(), 0.0F);
            for (int i = 0; i < this.list.size(); i++) {
                int x = i % this.maxColumns * 20;
                int y = i / this.maxColumns * 20 - (int) this.getScrollAmount();
                graphics.pose().pushPose();
                graphics.pose().translate((float) x, (float) y, 100.0F);
                MachineModelLocation loc = (MachineModelLocation) this.list.get(i);
                if (this.selected == loc) {
                    graphics.fill(0, 0, 20, 20, FastColor.ARGB32.color(255, 255, 0, 0));
                }
                ModelSelectionPopup.renderModel(graphics, 10.0F, 10.0F, loc, 16.0F);
                graphics.pose().popPose();
            }
            graphics.pose().popPose();
            if (this.getMaxScroll() > 0) {
                int i = this.m_252754_() + this.m_5711_() - 10;
                int j = i + 6;
                int n = this.m_93694_() * this.m_93694_() / this.getMaxPosition();
                n = Mth.clamp(n, 32, this.m_93694_() - 8);
                int o = (int) this.getScrollAmount() * (this.m_93694_() - n) / this.getMaxScroll() + this.m_252907_();
                if (o < this.m_252907_()) {
                    o = this.m_252907_();
                }
                graphics.fill(i, this.m_252907_(), j, this.m_252907_() + this.m_93694_(), -16777216);
                graphics.fill(i, o, j, o + n, -8355712);
                graphics.fill(i, o, j - 1, o + n - 1, -4144960);
            }
            graphics.disableScissor();
            MachineModelLocation hovered = this.getElementUnderMouse((double) mouseX, (double) mouseY);
            if (hovered != null) {
                graphics.renderTooltip(Minecraft.getInstance().font, Component.literal(hovered.toString()), mouseX, mouseY);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.updateScrollingState(mouseX, mouseY, button);
            MachineModelLocation selected = this.getElementUnderMouse(mouseX, mouseY);
            if (selected != null) {
                this.selected = selected;
                this.responder.accept(selected);
                return true;
            } else {
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
            if (super.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
                return true;
            } else if (button == 0 && this.scrolling) {
                if (mouseY < (double) this.m_252907_()) {
                    this.setScrollAmount(0.0);
                } else if (mouseY > (double) (this.m_252907_() + this.m_93694_())) {
                    this.setScrollAmount((double) this.getMaxScroll());
                } else {
                    double d = (double) Math.max(1, this.getMaxScroll());
                    int i = this.m_93694_();
                    int j = Mth.clamp((int) ((float) (i * i) / (float) this.getMaxPosition()), 32, i - 8);
                    double e = Math.max(1.0, d / (double) (i - j));
                    this.setScrollAmount(this.getScrollAmount() + dragY * e);
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
            this.setScrollAmount(this.getScrollAmount() - delta * 10.0);
            return super.m_6050_(mouseX, mouseY, delta);
        }
    }
}