package noppes.npcs.client.gui.custom;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.ModelData;
import noppes.npcs.ModelEyeData;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.gui.subgui.AssetsGui;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonListWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiEntityDisplayWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiScrollWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiSliderWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.api.wrapper.gui.GuiComponentsScrollableWrapper;
import noppes.npcs.client.gui.custom.components.CustomGuiEntityDisplay;
import noppes.npcs.client.gui.custom.components.CustomGuiScroll;
import noppes.npcs.client.gui.custom.components.CustomGuiSlider;
import noppes.npcs.client.gui.custom.components.CustomGuiTexturedRect;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;
import noppes.npcs.client.gui.model.GuiModelColor;
import noppes.npcs.client.layer.LayerParts;
import noppes.npcs.client.parts.ModelPartWrapper;
import noppes.npcs.client.parts.MpmPart;
import noppes.npcs.client.parts.MpmPartAbstractClient;
import noppes.npcs.client.parts.MpmPartData;
import noppes.npcs.client.parts.MpmPartEyes;
import noppes.npcs.client.parts.MpmPartReader;
import noppes.npcs.client.parts.PartBehaviorType;
import noppes.npcs.client.parts.PartRenderType;
import noppes.npcs.constants.BodyPart;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketCustomGuiParts;
import noppes.npcs.shared.client.gui.components.GuiColorButton;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.common.util.ColorUtil;
import noppes.npcs.shared.common.util.NaturalOrderComparator;
import noppes.npcs.shared.common.util.NopVector2i;
import noppes.npcs.shared.common.util.NopVector3f;

public class GuiCreationNewParts extends AbstractWidget implements IGuiComponent {

    private CustomGuiScroll scroll;

    private CustomGuiSlider slider;

    private CustomGuiEntityDisplay entity;

    private ModelData data;

    private ModelData renderData = new ModelData(null);

    private static String active = "";

    private static PlayerModel biped;

    private GuiCustom parent;

    private EntityCustomNpc npc;

    private Minecraft minecraft;

    private List<GuiCreationNewParts.GuiMpmPart> guiParts = new ArrayList();

    public static final ResourceLocation buttonsResource = new ResourceLocation("moreplayermodels", "textures/gui/arrowbuttons.png");

    private static final ResourceLocation colorWheel = new ResourceLocation("moreplayermodels", "textures/gui/colorwheel.png");

    public GuiCreationNewParts(final GuiCustom parent, EntityCustomNpc npc) {
        super(0, 0, 420, 200, Component.empty());
        this.npc = npc;
        this.parent = parent;
        this.data = npc.modelData;
        this.minecraft = Minecraft.getInstance();
        String[] menus = (String[]) MpmPartReader.PARTS.values().stream().map(p -> p.menu).sorted(new NaturalOrderComparator()).distinct().toArray(String[]::new);
        if (active.isEmpty()) {
            active = menus[0];
        }
        this.scroll = new CustomGuiScroll(parent, new CustomGuiScrollWrapper(-4, 4, 24, 100, 210, menus));
        this.scroll.disabledSearch();
        this.scroll.listener = new ICustomScrollListener() {

            @Override
            public void scrollClicked(double i, double j, int k, GuiCustomScrollNop scroll) {
                if (scroll.getSelectedIndex() >= 0) {
                    GuiCreationNewParts.active = scroll.getSelected();
                    for (GuiCreationNewParts.GuiMpmPart part : GuiCreationNewParts.this.guiParts) {
                        parent.scrollingPanel.comps.removeComponent(part.getID());
                    }
                    GuiCreationNewParts.this.guiParts.clear();
                    parent.init();
                }
            }

            @Override
            public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
            }
        };
        CustomGuiEntityDisplayWrapper wrapper = new CustomGuiEntityDisplayWrapper(-2, npc.wrappedNPC, 106, 90);
        wrapper.setSize(68, 90);
        this.entity = new CustomGuiEntityDisplay(parent, wrapper);
        this.slider = new CustomGuiSlider(parent, new CustomGuiSliderWrapper(-3, "", 106, 186, 68, 20).setMax(360.0F).setDecimals(0).setValue(180.0F).setOnChange((gui, slider) -> {
            this.entity.component.setRotation((int) slider.getValue() - 180);
            this.entity.init();
        })).disablePackets();
        biped = new PlayerModel(this.minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER), true);
    }

    public void openSubgui(GuiCustom parent, GuiCustom subgui) {
        subgui.m_6575_(this.minecraft, parent.f_96543_, parent.f_96544_);
        subgui.parent = parent;
        parent.subgui = subgui;
        if (subgui.guiWrapper != null) {
            subgui.background = new CustomGuiTexturedRect(subgui, (CustomGuiTexturedRectWrapper) subgui.guiWrapper.getBackgroundRect());
        }
        if (subgui.scrollingPanel.comps == null) {
            subgui.scrollingPanel.comps = new GuiComponentsScrollableWrapper(subgui.guiWrapper, null);
        }
    }

    @Override
    public int getID() {
        return -10;
    }

    @Override
    public void onRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void init() {
        this.parent.add(this.scroll);
        this.parent.add(this.entity);
        this.parent.add(this.slider);
        List<MpmPart> list = new ArrayList((Collection) MpmPartReader.PARTS.values().stream().sorted(Comparator.comparing(t -> t.id)).filter(t -> t.menu.equals(active) && t.parentId == null).collect(Collectors.toList()));
        this.scroll.setSelected(active);
        this.entity.setEntity(this.npc);
        if (this.guiParts.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                int column = i % 3;
                MpmPart part = (MpmPart) list.get(i);
                GuiCreationNewParts.GuiMpmPart gui = new GuiCreationNewParts.GuiMpmPart(this.parent, 80 + i, column * 70 + column, i / 3 * 70, part);
                this.guiParts.add(gui);
                this.parent.addPanel(gui);
                this.parent.scrollingPanel.comps.addComponent(new GuiCreationNewParts.PartsWrapper(gui));
            }
        } else {
            for (int i = 0; i < this.guiParts.size(); i++) {
                int column = i % 3;
                GuiCreationNewParts.GuiMpmPart gui = (GuiCreationNewParts.GuiMpmPart) this.guiParts.get(i);
                gui.m_252865_(column * 70 + column);
                gui.m_253211_(i / 3 * 70);
                this.parent.addPanel(gui);
            }
        }
        this.parent.scrollingPanel.setMaxSize(this.guiParts.stream().mapToInt(v -> v.m_252907_() + v.m_93694_()).max().orElse(0));
    }

    @Override
    public ICustomGuiComponent component() {
        return null;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int i, int j, float f) {
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }

    public void save() {
        Packets.sendServer(new SPacketCustomGuiParts(this.data.save()));
    }

    public void openTextureSubgui(GuiCustom parent, MpmPartData data, MpmPart part) {
        GuiCreationNewParts.TexturePart screen = new GuiCreationNewParts.TexturePart(data, part);
        CustomGuiWrapper gui = screen.guiWrapper;
        gui.setBackgroundTexture("customnpcs:textures/gui/components.png");
        gui.setSize(310, 200);
        gui.getBackgroundRect().setTextureOffset(0, 0);
        gui.getBackgroundRect().setRepeatingTexture(64, 64, 4);
        gui.addButton(66, "x", 276, 4, 20, 20).setOnPress((gui2, button) -> screen.onClose()).setDisablePackets();
        if (!part.disableCustomTextures) {
            gui.addLabel(21, "gui.playerskin", 4, 110, 10, 100);
            gui.addButtonList(22, 76, 105, 50, 20).setValues("gui.no", "gui.yes").setSelected(data.usePlayerSkin ? 1 : 0).setOnPress((gui2, button) -> {
                data.usePlayerSkin = ((CustomGuiButtonListWrapper) button).getSelected() == 1;
                gui2.getComponent(23).setVisible(!data.usePlayerSkin);
                gui2.getComponent(24).setVisible(!data.usePlayerSkin);
                gui2.getComponent(25).setVisible(!data.usePlayerSkin);
                gui2.getComponent(26).setVisible(!data.usePlayerSkin);
                gui2.getComponent(27).setVisible(!data.usePlayerSkin);
                this.data.refreshParts();
                this.save();
                screen.init();
            }).setDisablePackets();
            gui.addLabel(23, "gui.texture", 4, 130, 10, 100).setVisible(!data.usePlayerSkin);
            ResourceLocation loc = data.getDefaultTexture();
            CustomGuiTextFieldWrapper tf = (CustomGuiTextFieldWrapper) gui.addTextField(24, 4, 140, 220, 20).setText(loc == null ? "" : loc.toString()).setOnFocusLost((gui2, text) -> data.setTexture(text.getText())).setVisible(!data.usePlayerSkin).setDisablePackets();
            gui.addButton(25, "gui.select", 226, 140, 80, 20).setOnPress((gui2, button) -> this.openSubgui(screen, this.openTextureBasic(data.getDefaultTexture() == null ? "" : data.getDefaultTexture().toString(), resource -> {
                data.setTexture(resource);
                tf.setText(resource);
                this.data.refreshParts();
                this.save();
                screen.init();
            }))).setVisible(!data.usePlayerSkin).setDisablePackets();
            gui.addLabel(26, "config.skinurl", 4, 168, 10, 100).setVisible(!data.usePlayerSkin);
            gui.addTextField(27, 4, 178, 220, 20).setText(data.url).setOnFocusLost((gui2, text) -> {
                data.setUrl(text.getText());
                this.data.refreshParts();
                this.save();
                screen.init();
            }).setVisible(!data.usePlayerSkin).setDisablePackets();
        }
        screen.setGuiWrapper(gui);
        this.openSubgui(parent, screen);
    }

    public GuiCustom openTextureBasic(String resource, AssetsGui.SelectionCallback callback) {
        GuiCustom screen = new GuiCustom((ContainerCustomGui) this.parent.m_6262_(), this.parent.inv, Component.empty());
        CustomGuiWrapper gui = screen.guiWrapper = new CustomGuiWrapper(null);
        gui.setBackgroundTexture("customnpcs:textures/gui/components.png");
        gui.setSize(308, 214);
        gui.getBackgroundRect().setTextureOffset(0, 0);
        gui.getBackgroundRect().setRepeatingTexture(64, 64, 4);
        CustomGuiButtonWrapper b = gui.addTexturedButton(666, "X", 290, -4, 14, 14, "customnpcs:textures/gui/components.png", 0, 64);
        b.getTextureRect().setRepeatingTexture(64, 22, 3).setHoverText("gui.close");
        b.setTextureHoverOffset(22).setOnPress((guii, bb) -> screen.onClose());
        b.setDisablePackets();
        gui.addAssetsSelector(11, 4, 4, 300, 204).setSelected(resource).setOnPress((gui2, assets) -> screen.onClose()).setOnChange((gui2, assets) -> callback.call(assets.getSelected())).setDisablePackets();
        screen.setGuiWrapper(gui);
        return screen;
    }

    public void openEyesSubgui(GuiCustom parent, ModelEyeData data, MpmPartEyes part) {
        GuiCreationNewParts.EyesPart screen = new GuiCreationNewParts.EyesPart(data, part);
        CustomGuiWrapper gui = screen.guiWrapper = new CustomGuiWrapper(null);
        gui.setBackgroundTexture("customnpcs:textures/gui/components.png");
        gui.setSize(310, 200);
        gui.getBackgroundRect().setTextureOffset(0, 0);
        gui.getBackgroundRect().setRepeatingTexture(64, 64, 4);
        int y = 8;
        gui.addLabel(21, "part.eyes", 56, y + 5, 10, 100);
        gui.addButtonList(22, 110, y, 110, 20).setValues("gui.playerskin", "gui.normal", "gui.texture").setSelected(data.skinType).setOnPress((gui2, button) -> {
            data.skinType = ((CustomGuiButtonListWrapper) button).getSelected();
            gui2.getComponent(23).setVisible(data.skinType == 1);
            gui2.getComponent(24).setVisible(data.skinType == 2);
            gui2.getComponent(25).setVisible(data.skinType == 2);
            gui2.getComponent(27).setVisible(data.glint || data.skinType == 1 || data.skinType == 2);
            screen.init();
        }).setDisablePackets();
        gui.addButton(23, "", 230, y, 50, 20).setOnPress((gui2, button) -> this.openSubgui(screen, new GuiModelColor(screen, ColorUtil.rgbToColor(data.color), color -> {
            data.color = ColorUtil.colorToRgb(color);
            screen.init();
        }))).setVisible(data.skinType == 1).setDisablePackets();
        y += 25;
        gui.addLabel(24, "config.skinurl", 56, y + 5, 10, 100).setVisible(data.skinType == 2);
        gui.addTextField(25, 110, y, 195, 20).setText(data.url).setOnFocusLost((gui2, text) -> data.setUrl(text.getText())).setVisible(data.skinType == 2).setDisablePackets();
        y += 25;
        gui.addButtonList(26, 54, y, 100, 20).setValues("gui.normal", "gui.big").setSelected(data.eyeSize).setOnPress((gui2, button) -> {
            data.eyeSize = ((CustomGuiButtonListWrapper) button).getSelected();
            screen.init();
        }).setDisablePackets();
        gui.addButtonList(27, 156, y, 100, 20).setValues("gui.normal", "gui.mirror").setSelected(data.mirror ? 1 : 0).setOnPress((gui2, button) -> {
            data.mirror = ((CustomGuiButtonListWrapper) button).getSelected() == 1;
            screen.init();
        }).setVisible(data.glint || data.skinType == 1 || data.skinType == 2).setDisablePackets();
        gui.addLabel(28, "eye.pupil", 4, y + 5, 10, 100);
        y += 25;
        gui.addButtonList(29, 54, y, 100, 20).setValues(I18n.get("gui.down") + "x2", "gui.down", "gui.normal", "gui.up", I18n.get("gui.up") + "x2").setSelected(data.eyePos.y + 2).setOnPress((gui2, button) -> data.eyePos = new NopVector2i(data.eyePos.x, ((CustomGuiButtonListWrapper) button).getSelected() - 2)).setDisablePackets();
        gui.addButtonList(30, 156, y, 100, 20).setValues("gui.inward", "gui.normal", "gui.outward").setSelected(data.eyePos.x + 1).setOnPress((gui2, button) -> data.eyePos = new NopVector2i(((CustomGuiButtonListWrapper) button).getSelected() - 1, data.eyePos.y)).setDisablePackets();
        gui.addLabel(31, "gui.position", 4, y + 5, 10, 100);
        y += 25;
        gui.addButtonList(32, 54, y, 50, 20).setValues("gui.no", "gui.yes").setSelected(data.glint ? 1 : 0).setOnPress((gui2, button) -> {
            data.glint = ((CustomGuiButtonListWrapper) button).getSelected() == 1;
            gui2.getComponent(27).setVisible(data.glint || data.skinType == 1 || data.skinType == 2);
        }).setDisablePackets();
        gui.addLabel(33, "eye.glint", 4, y + 5, 10, 100);
        gui.addButton(34, "", 162, y, 50, 20).setOnPress((gui2, button) -> this.openSubgui(screen, new GuiModelColor(screen, ColorUtil.rgbToColor(data.browColor), color -> {
            data.browColor = ColorUtil.colorToRgb(color);
            screen.init();
        }))).setDisablePackets();
        gui.addButtonList(35, 214, y, 70, 20).setValues("gui.disabled", "1", "2", "3", "4", "5", "6", "7", "8").setSelected((int) (data.browThickness.y * 10.0F)).setOnPress((gui2, button) -> data.browThickness = new NopVector3f(1.0F, (float) ((CustomGuiButtonListWrapper) button).getSelected() / 10.0F, 1.0F)).setDisablePackets();
        gui.addLabel(36, "eye.lash", 112, y + 5, 10, 100);
        y += 25;
        gui.addButtonList(37, 54, y, 50, 20).setValues("gui.no", "gui.yes").setSelected(data.disableBlink ? 0 : 1).setOnPress((gui2, button) -> {
            data.disableBlink = ((CustomGuiButtonListWrapper) button).getSelected() == 0;
            gui2.getComponent(39).setVisible(!data.disableBlink);
            gui2.getComponent(40).setVisible(!data.disableBlink);
            screen.init();
        }).setDisablePackets();
        gui.addLabel(38, "eye.blink", 4, y + 5, 10, 100);
        gui.addLabel(39, "eye.lid", 112, y + 5, 10, 100).setVisible(!data.disableBlink);
        gui.addButton(40, "", 162, y, 50, 20).setOnPress((gui2, button) -> this.openSubgui(screen, new GuiModelColor(screen, ColorUtil.rgbToColor(data.lidColor), color -> {
            data.lidColor = ColorUtil.colorToRgb(color);
            screen.init();
        }))).setVisible(!data.disableBlink).setDisablePackets();
        gui.addButton(66, "x", 288, 4, 20, 20).setOnPress((gui2, button) -> screen.onClose()).setDisablePackets();
        screen.setGuiWrapper(gui);
        this.openSubgui(parent, screen);
    }

    class EyesPart extends GuiCustom {

        private MpmPartEyes part;

        private ModelEyeData data;

        public EyesPart(ModelEyeData data, MpmPartEyes part) {
            super((ContainerCustomGui) GuiCreationNewParts.this.parent.m_6262_(), GuiCreationNewParts.this.parent.inv, Component.empty());
            this.data = data;
            this.part = part;
        }

        @Override
        public void init() {
            super.init();
            this.components.components.put(23, new GuiColorButton(this, (CustomGuiButtonWrapper) this.guiWrapper.getComponent(23), ColorUtil.rgbToColor(this.data.color)));
            this.components.components.put(34, new GuiColorButton(this, (CustomGuiButtonWrapper) this.guiWrapper.getComponent(34), ColorUtil.rgbToColor(this.data.browColor)));
            this.components.components.put(40, new GuiColorButton(this, (CustomGuiButtonWrapper) this.guiWrapper.getComponent(40), ColorUtil.rgbToColor(this.data.lidColor)));
        }

        @Override
        public void renderBackground(GuiGraphics graphics) {
            super.m_280273_(graphics);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.render(graphics, mouseX, mouseY, partialTicks);
            PoseStack posestack = RenderSystem.getModelViewStack();
            posestack.pushPose();
            posestack.translate((double) (this.f_97735_ + 10), (double) (this.f_97736_ + 10), 150.0);
            posestack.scale(1.0F, 1.0F, -1.0F);
            RenderSystem.applyModelViewMatrix();
            PoseStack matrixstack = new PoseStack();
            matrixstack.pushPose();
            EntityRenderDispatcher entityrenderermanager = this.f_96541_.getEntityRenderDispatcher();
            entityrenderermanager.setRenderShadow(false);
            MultiBufferSource.BufferSource irendertypebuffer$impl = this.f_96541_.renderBuffers().bufferSource();
            VertexConsumer ivertex = irendertypebuffer$impl.getBuffer(RenderType.entityCutoutNoCull(GuiCreationNewParts.this.npc.textureLocation));
            Lighting.setupForEntityInInventory();
            RenderSystem.runAsFancy(() -> {
                GuiCreationNewParts.biped.f_102810_.visible = !this.part.hiddenParts.contains(BodyPart.BODY);
                GuiCreationNewParts.biped.jacket.visible = GuiCreationNewParts.biped.jacket.visible && GuiCreationNewParts.biped.f_102810_.visible;
                GuiCreationNewParts.biped.f_102808_.visible = !this.part.hiddenParts.contains(BodyPart.HEAD);
                GuiCreationNewParts.biped.f_102809_.visible = GuiCreationNewParts.biped.f_102809_.visible && GuiCreationNewParts.biped.f_102808_.visible;
                matrixstack.translate(19.0F, 43.0F, 25.0F);
                matrixstack.scale(100.0F, 100.0F, 100.0F);
                GuiCreationNewParts.biped.f_102808_.render(matrixstack, ivertex, 15728880, OverlayTexture.NO_OVERLAY);
                this.part.pos = NopVector3f.ZERO;
                this.part.rot = NopVector3f.ZERO;
                LayerParts.renderPart(this.data, this.part, matrixstack, irendertypebuffer$impl, 15728880, GuiCreationNewParts.this.npc, GuiCreationNewParts.biped, GuiCreationNewParts.this.renderData);
            });
            irendertypebuffer$impl.endBatch();
            matrixstack.popPose();
            posestack.popPose();
            entityrenderermanager.setRenderShadow(true);
            RenderSystem.applyModelViewMatrix();
        }

        @Override
        public void onClose() {
            super.onClose();
            GuiCreationNewParts.this.save();
        }
    }

    class GuiMpmPart extends AbstractWidget implements IGuiComponent {

        public static final int SIZE = 70;

        public boolean basic = false;

        private List<MpmPart> all = new ArrayList();

        private MpmPart part;

        private MpmPartData data;

        private boolean selected = true;

        private GuiCustom parent;

        boolean colorPickerHovered = false;

        boolean infoHovered = false;

        boolean settingsHovered = false;

        boolean hoverL = false;

        boolean hoverR = false;

        int zPos = 0;

        int id;

        public GuiMpmPart(GuiCustom parent, int id, int x, int y, MpmPart part) {
            super(x, y, 70, 70, Component.empty());
            this.parent = parent;
            this.id = id;
            this.part = part;
            this.all.add(part);
            for (Entry<ResourceLocation, MpmPart> entry : MpmPartReader.PARTS.entrySet()) {
                if (((MpmPart) entry.getValue()).parentId != null && ((MpmPart) entry.getValue()).parentId.equals(part.id)) {
                    this.all.add((MpmPart) entry.getValue());
                }
            }
            for (MpmPart p : this.all) {
                this.data = (MpmPartData) GuiCreationNewParts.this.data.mpmParts.stream().filter(t -> t.partId.equals(p.id)).findFirst().orElse(null);
                if (this.data != null) {
                    this.part = p;
                    break;
                }
            }
            this.all = (List<MpmPart>) this.all.stream().sorted(Comparator.comparing(t -> t.id)).collect(Collectors.toList());
            if (this.data == null) {
                if (!part.id.equals(ModelEyeData.RESOURCE) && !part.id.equals(ModelEyeData.RESOURCE_RIGHT) && !part.id.equals(ModelEyeData.RESOURCE_LEFT)) {
                    this.data = new MpmPartData();
                } else {
                    this.data = new ModelEyeData();
                }
                this.data.partId = part.id;
                this.data.usePlayerSkin = part.defaultUsePlayerSkins;
                this.selected = false;
            }
        }

        public void renderModel(GuiGraphics graphics, int xMouse, int yMouse, float tick) {
            int x1 = this.m_252754_();
            int x2 = this.m_252754_() + 70;
            int y1 = this.m_252907_();
            int y2 = this.m_252907_() + 70 - 1;
            graphics.fill(x1, y1, x2, y2, -3750202);
            GuiCreationNewParts.this.renderData.mpmParts = GuiCreationNewParts.this.data.mpmParts;
            PoseStack posestack = RenderSystem.getModelViewStack();
            posestack.pushPose();
            posestack.translate((double) this.m_252754_(), (double) (this.m_252907_() - this.parent.scrollingPanel.comps.scrollAmount), 100.0 + (double) this.zPos);
            posestack.scale(1.0F, 1.0F, -1.0F);
            RenderSystem.applyModelViewMatrix();
            PoseStack matrixstack = new PoseStack();
            matrixstack.pushPose();
            EntityRenderDispatcher entityrenderermanager = GuiCreationNewParts.this.minecraft.getEntityRenderDispatcher();
            entityrenderermanager.setRenderShadow(false);
            MultiBufferSource.BufferSource irendertypebuffer$impl = GuiCreationNewParts.this.minecraft.renderBuffers().bufferSource();
            VertexConsumer ivertex = irendertypebuffer$impl.getBuffer(RenderType.entityCutoutNoCull(GuiCreationNewParts.this.npc.textureLocation));
            Lighting.setupForEntityInInventory();
            RenderSystem.runAsFancy(() -> {
                GuiCreationNewParts.biped.f_102814_.visible = !this.part.hiddenParts.contains(BodyPart.LEFT_LEG) && !this.part.hiddenParts.contains(BodyPart.LEGS);
                GuiCreationNewParts.biped.leftPants.visible = GuiCreationNewParts.biped.leftPants.visible && GuiCreationNewParts.biped.f_102814_.visible;
                GuiCreationNewParts.biped.f_102813_.visible = !this.part.hiddenParts.contains(BodyPart.RIGHT_LEG) && !this.part.hiddenParts.contains(BodyPart.LEGS);
                GuiCreationNewParts.biped.rightPants.visible = GuiCreationNewParts.biped.rightPants.visible && GuiCreationNewParts.biped.f_102813_.visible;
                GuiCreationNewParts.biped.f_102812_.visible = !this.part.hiddenParts.contains(BodyPart.LEFT_ARM) && !this.part.hiddenParts.contains(BodyPart.ARMS);
                GuiCreationNewParts.biped.leftSleeve.visible = GuiCreationNewParts.biped.leftSleeve.visible && GuiCreationNewParts.biped.f_102812_.visible;
                GuiCreationNewParts.biped.f_102811_.visible = !this.part.hiddenParts.contains(BodyPart.RIGHT_ARM) && !this.part.hiddenParts.contains(BodyPart.ARMS);
                GuiCreationNewParts.biped.rightSleeve.visible = GuiCreationNewParts.biped.rightSleeve.visible && GuiCreationNewParts.biped.f_102811_.visible;
                GuiCreationNewParts.biped.f_102810_.visible = !this.part.hiddenParts.contains(BodyPart.BODY);
                GuiCreationNewParts.biped.jacket.visible = GuiCreationNewParts.biped.jacket.visible && GuiCreationNewParts.biped.f_102810_.visible;
                GuiCreationNewParts.biped.f_102808_.visible = !this.part.hiddenParts.contains(BodyPart.HEAD);
                GuiCreationNewParts.biped.f_102809_.visible = GuiCreationNewParts.biped.f_102809_.visible && GuiCreationNewParts.biped.f_102808_.visible;
                if (this.part.bodyPart == BodyPart.HEAD) {
                    matrixstack.translate(32.0F, 46.0F, 25.0F);
                    matrixstack.scale(36.0F, 36.0F, 36.0F);
                    matrixstack.mulPose(Axis.XP.rotation((float) (Math.PI / 8)));
                    matrixstack.mulPose(Axis.YP.rotation((float) this.part.previewRotation * (float) (Math.PI / 180.0)));
                    GuiCreationNewParts.biped.f_102808_.render(matrixstack, ivertex, 15728880, OverlayTexture.NO_OVERLAY);
                }
                if (this.part.bodyPart == BodyPart.LEGS) {
                    matrixstack.translate(18.0F, 12.0F, 25.0F);
                    matrixstack.scale(36.0F, 36.0F, 36.0F);
                    matrixstack.mulPose(Axis.XP.rotation((float) (Math.PI / 8)));
                    matrixstack.mulPose(Axis.YP.rotation((float) this.part.previewRotation * (float) (Math.PI / 180.0)));
                    GuiCreationNewParts.biped.f_102810_.render(matrixstack, ivertex, 15728880, OverlayTexture.NO_OVERLAY);
                    if (this.part.animationType == PartBehaviorType.LEGS) {
                        ModelPartWrapper modelPart = this.part.getPart("right_leg");
                        if (modelPart != null) {
                            modelPart.setRot(new NopVector3f(GuiCreationNewParts.biped.f_102813_.xRot, GuiCreationNewParts.biped.f_102813_.yRot, GuiCreationNewParts.biped.f_102813_.zRot));
                            modelPart.setPos(new NopVector3f(GuiCreationNewParts.biped.f_102813_.x, GuiCreationNewParts.biped.f_102813_.y, GuiCreationNewParts.biped.f_102813_.z));
                        }
                        modelPart = this.part.getPart("left_leg");
                        if (modelPart != null) {
                            modelPart.setRot(new NopVector3f(GuiCreationNewParts.biped.f_102814_.xRot, GuiCreationNewParts.biped.f_102814_.yRot, GuiCreationNewParts.biped.f_102814_.zRot));
                            modelPart.setPos(new NopVector3f(GuiCreationNewParts.biped.f_102814_.x, GuiCreationNewParts.biped.f_102814_.y, GuiCreationNewParts.biped.f_102814_.z));
                        }
                    }
                    GuiCreationNewParts.biped.f_102813_.render(matrixstack, ivertex, 15728880, OverlayTexture.NO_OVERLAY);
                    GuiCreationNewParts.biped.f_102814_.render(matrixstack, ivertex, 15728880, OverlayTexture.NO_OVERLAY);
                }
                if (this.part.bodyPart == BodyPart.ARMS) {
                    matrixstack.translate(18.0F, 12.0F, 25.0F);
                    matrixstack.scale(36.0F, 36.0F, 36.0F);
                    matrixstack.mulPose(Axis.XP.rotation((float) (Math.PI / 8)));
                    matrixstack.mulPose(Axis.YP.rotation((float) this.part.previewRotation * (float) (Math.PI / 180.0)));
                    GuiCreationNewParts.biped.f_102810_.render(matrixstack, ivertex, 15728880, OverlayTexture.NO_OVERLAY);
                    if (this.part.animationType == PartBehaviorType.ARMS) {
                        ModelPartWrapper modelPartx = this.part.getPart("right_arm");
                        if (modelPartx != null) {
                            modelPartx.setRot(new NopVector3f(GuiCreationNewParts.biped.f_102811_.xRot, GuiCreationNewParts.biped.f_102811_.yRot, GuiCreationNewParts.biped.f_102811_.zRot));
                            modelPartx.setPos(new NopVector3f(GuiCreationNewParts.biped.f_102811_.x, GuiCreationNewParts.biped.f_102811_.y, GuiCreationNewParts.biped.f_102811_.z));
                        }
                        modelPartx = this.part.getPart("left_arm");
                        if (modelPartx != null) {
                            modelPartx.setRot(new NopVector3f(GuiCreationNewParts.biped.f_102812_.xRot, GuiCreationNewParts.biped.f_102812_.yRot, GuiCreationNewParts.biped.f_102812_.zRot));
                            modelPartx.setPos(new NopVector3f(GuiCreationNewParts.biped.f_102812_.x, GuiCreationNewParts.biped.f_102812_.y, GuiCreationNewParts.biped.f_102812_.z));
                        }
                    }
                    GuiCreationNewParts.biped.f_102812_.render(matrixstack, ivertex, 15728880, OverlayTexture.NO_OVERLAY);
                    GuiCreationNewParts.biped.f_102811_.render(matrixstack, ivertex, 15728880, OverlayTexture.NO_OVERLAY);
                }
                if (this.part.bodyPart == BodyPart.BODY) {
                    matrixstack.translate(18.0F, 18.0F, 25.0F);
                    matrixstack.scale(36.0F, 36.0F, 36.0F);
                    matrixstack.mulPose(Axis.XP.rotation((float) (Math.PI / 8)));
                    matrixstack.mulPose(Axis.YP.rotation((float) this.part.previewRotation * (float) (Math.PI / 180.0)));
                    GuiCreationNewParts.biped.f_102810_.render(matrixstack, ivertex, 15728880, OverlayTexture.NO_OVERLAY);
                }
                if (this.part.renderType != PartRenderType.NONE) {
                    MpmPartAbstractClient partc = (MpmPartAbstractClient) this.part;
                    partc.pos = NopVector3f.ZERO;
                    partc.rot = NopVector3f.ZERO;
                    LayerParts.renderPart(this.data, partc, matrixstack, irendertypebuffer$impl, 15728880, GuiCreationNewParts.this.npc, GuiCreationNewParts.biped, GuiCreationNewParts.this.renderData);
                }
            });
            irendertypebuffer$impl.endBatch();
            matrixstack.popPose();
            posestack.popPose();
            entityrenderermanager.setRenderShadow(true);
            RenderSystem.applyModelViewMatrix();
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int xMouse, int yMouse, float tick) {
            if (this.parent.subgui == null) {
            }
        }

        public void renderIcons(GuiGraphics graphics, int xMouse, int yMouse, float tick) {
            int color = -1;
            if (!this.basic) {
                if (this.f_93622_) {
                    color = -65536;
                }
                int x1 = this.m_252754_();
                int x2 = this.m_252754_() + 70;
                int y1 = this.m_252907_();
                int y2 = this.m_252907_() + 70 - 1;
                graphics.hLine(x1, x2, y1, color);
                graphics.hLine(x1, x2, y2, color);
                graphics.hLine(x1, y1, y2, color);
                graphics.hLine(x2, y1, y2, color);
                x1 = this.m_252754_() + 70 - 16;
                x2 = this.m_252754_() + 70;
                y1 = this.m_252907_() + 1;
                y2 = this.m_252907_() + 70 - 1;
                graphics.fill(x1, y1, x2, y2, -3750202);
                int var13 = -1;
                x1 = this.m_252754_() + 70 - 14;
                x2 = this.m_252754_() + 70 - 2;
                y1 = this.m_252907_() + 2;
                y2 = this.m_252907_() + 14;
                graphics.fill(x1, y1, x2, y2, -16777216);
                graphics.hLine(x1, x2, y1, var13);
                graphics.hLine(x1, x2, y2, var13);
                graphics.hLine(x1, y1, y2, var13);
                graphics.hLine(x2, y1, y2, var13);
                if (!this.part.isEnabled) {
                    graphics.drawString(GuiCreationNewParts.this.minecraft.font, Component.literal("X").withStyle(ChatFormatting.BOLD), x1 + 4, y1 + 3, 16711680);
                } else if (this.selected) {
                    char c = (char) Integer.parseInt("2713", 16);
                    graphics.drawString(GuiCreationNewParts.this.minecraft.font, Component.literal(c + "").withStyle(ChatFormatting.BOLD), x1 + 3, y1 + 2, 65280);
                }
            }
            int guiY = this.m_252907_() + 16;
            RenderSystem.setShaderTexture(0, GuiCreationNewParts.colorWheel);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int size = 14;
            int x1 = this.m_252754_() + 70 - 15;
            int x2 = x1 + size;
            int y1 = guiY;
            int y2 = guiY + size;
            this.colorPickerHovered = xMouse >= x1 && yMouse >= guiY && xMouse < x2 && yMouse < y2;
            if (this.colorPickerHovered) {
                x1--;
                y1 = guiY - 1;
                size = 16;
            }
            graphics.blit(GuiCreationNewParts.colorWheel, x1, y1, 0, 0.0F, 0.0F, size, size, size, size);
            guiY += 15;
            RenderSystem.setShaderTexture(0, GuiCreationNewParts.buttonsResource);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.all.size() > 1) {
                x1 = this.m_252754_() + 70 - 17;
                x2 = x1 + 6;
                y2 = guiY + 8;
                RenderSystem.setShaderTexture(0, GuiCreationNewParts.buttonsResource);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                this.hoverL = xMouse >= x1 && yMouse >= guiY && xMouse < x2 && yMouse < y2;
                graphics.blit(GuiCreationNewParts.buttonsResource, x1, guiY, 0, this.hoverL ? 76 : 60, 6, 8);
                String s = this.all.indexOf(this.part) + "";
                graphics.drawString(GuiCreationNewParts.this.minecraft.font, s, (int) ((float) x1 + 9.5F - (float) GuiCreationNewParts.this.minecraft.font.width(s) / 2.0F), (int) ((float) guiY + 0.5F), 0);
                RenderSystem.setShaderTexture(0, GuiCreationNewParts.buttonsResource);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                x1 = this.m_252754_() + 70 - 5;
                x2 = x1 + 6;
                y2 = guiY + 8;
                this.hoverR = xMouse >= x1 && yMouse >= guiY && xMouse < x2 && yMouse < y2;
                graphics.blit(GuiCreationNewParts.buttonsResource, x1, guiY, 6, this.hoverR ? 76 : 60, 6, 8);
                guiY += 11;
            }
            if (!this.basic) {
                if (this.selected) {
                    x1 = this.m_252754_() + 70 - 15;
                    x2 = x1 + 14;
                    y2 = guiY + 14;
                    this.settingsHovered = xMouse >= x1 && yMouse >= guiY && xMouse < x2 && yMouse < y2;
                    graphics.blit(GuiCreationNewParts.buttonsResource, x1, guiY, 0, this.settingsHovered ? 140 : 126, 14, 14);
                }
                int var21 = 8;
                x1 = this.m_252754_() + 70 - 10;
                x2 = x1 + var21;
                y1 = this.m_252907_() + 70 - 12;
                y2 = y1 + var21;
                this.infoHovered = xMouse >= x1 && yMouse >= y1 && xMouse < x2 && yMouse < y2;
                MutableComponent text = Component.literal("i").withStyle(ChatFormatting.BOLD);
                if (this.infoHovered) {
                    text = text.withStyle(ChatFormatting.UNDERLINE);
                }
                graphics.drawString(GuiCreationNewParts.this.minecraft.font, text, x1 + 3, y1 + 2, 0);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
            if (!super.clicked(mouseX, mouseY)) {
                return false;
            } else {
                if (this.colorPickerHovered) {
                    GuiCreationNewParts.this.openSubgui(this.parent, new GuiModelColor(this.parent, this.data.getColor(), color -> {
                        this.data.setColor(color);
                        GuiCreationNewParts.this.save();
                    }));
                } else if (this.hoverL) {
                    int index = (this.all.indexOf(this.part) + this.all.size() - 1) % this.all.size();
                    this.part = (MpmPart) this.all.get(index);
                    this.data.partId = this.part.id;
                } else if (this.hoverR) {
                    int index = (this.all.indexOf(this.part) + 1) % this.all.size();
                    this.part = (MpmPart) this.all.get(index);
                    this.data.partId = this.part.id;
                } else if (this.settingsHovered) {
                    if (this.data instanceof ModelEyeData) {
                        GuiCreationNewParts.this.openEyesSubgui(this.parent, (ModelEyeData) this.data, (MpmPartEyes) this.part);
                    } else {
                        GuiCreationNewParts.this.openTextureSubgui(this.parent, this.data, this.part);
                    }
                } else if (this.part.isEnabled && !this.basic) {
                    this.selected = !this.selected;
                    if (this.selected) {
                        GuiCreationNewParts.this.data.mpmParts.add(this.data);
                    } else {
                        GuiCreationNewParts.this.data.mpmParts.removeIf(t -> t.partId.equals(this.data.partId));
                    }
                }
                GuiCreationNewParts.this.data.refreshParts();
                GuiCreationNewParts.this.save();
                return true;
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
        }

        @Override
        public int getID() {
            return this.id;
        }

        @Override
        public void onRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            if (this.parent.subgui == null) {
                this.renderModel(graphics, mouseX, mouseY, partialTicks);
            }
        }

        @Override
        public void onRenderPost(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            if (this.parent.subgui == null) {
                this.renderIcons(graphics, mouseX, mouseY, partialTicks);
                if (this.infoHovered) {
                    List<Component> text = Arrays.asList(Component.translatable(this.part.name), Component.translatable("message.madeby", this.part.author));
                    if (!this.part.isEnabled) {
                        text = new ArrayList();
                        text.add(Component.translatable("gui.disabled", this.part.author));
                    }
                    this.parent.hoverText = text;
                }
            }
        }

        @Override
        public void init() {
        }

        @Override
        public ICustomGuiComponent component() {
            return null;
        }
    }

    public class PartsWrapper implements ICustomGuiComponent {

        private GuiCreationNewParts.GuiMpmPart part;

        public PartsWrapper(GuiCreationNewParts.GuiMpmPart part) {
            this.part = part;
        }

        @Override
        public int getID() {
            return this.part.getID();
        }

        @Override
        public ICustomGuiComponent setID(int id) {
            return this;
        }

        @Override
        public UUID getUniqueID() {
            return null;
        }

        @Override
        public int getPosX() {
            return this.part.m_252754_();
        }

        @Override
        public int getPosY() {
            return this.part.m_252907_();
        }

        @Override
        public ICustomGuiComponent setPos(int x, int y) {
            return this;
        }

        @Override
        public int getWidth() {
            return this.part.m_5711_();
        }

        @Override
        public int getHeight() {
            return this.part.m_93694_();
        }

        @Override
        public ICustomGuiComponent setSize(int width, int height) {
            return null;
        }

        @Override
        public boolean hasHoverText() {
            return false;
        }

        @Override
        public String[] getHoverText() {
            return new String[0];
        }

        @Override
        public ICustomGuiComponent setHoverText(String text) {
            return null;
        }

        @Override
        public ICustomGuiComponent setHoverText(String[] text) {
            return null;
        }

        @Override
        public ICustomGuiComponent setEnabled(boolean bo) {
            return this;
        }

        @Override
        public boolean getVisible() {
            return true;
        }

        @Override
        public ICustomGuiComponent setVisible(boolean bo) {
            return null;
        }

        @Override
        public boolean getEnabled() {
            return true;
        }

        @Override
        public int getType() {
            return -1;
        }
    }

    class TexturePart extends GuiCustom {

        private MpmPart part;

        private MpmPartData data;

        private GuiCreationNewParts.GuiMpmPart partGui;

        public TexturePart(MpmPartData data, MpmPart part) {
            super((ContainerCustomGui) GuiCreationNewParts.this.parent.m_6262_(), GuiCreationNewParts.this.parent.inv, Component.empty());
            this.data = data;
            this.part = part;
            this.partGui = GuiCreationNewParts.this.new GuiMpmPart(this, 70, 2, 2, part);
            this.partGui.zPos = 250;
            this.partGui.basic = true;
            this.guiWrapper = new CustomGuiWrapper(null);
            this.guiWrapper.addComponent(GuiCreationNewParts.this.new PartsWrapper(this.partGui));
        }

        @Override
        public void init() {
            super.init();
            this.add(this.partGui);
        }

        @Override
        public void onClose() {
            super.onClose();
            GuiCreationNewParts.this.save();
        }
    }
}