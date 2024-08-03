package com.mna.gui.widgets.lodestar;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.entities.construct.ai.ConstructTask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.gui.GuiTextures;
import com.mna.gui.block.GuiLodestarV2;
import com.mna.tools.render.GuiRenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraftforge.registries.IForgeRegistry;

public class LodestarNode extends ImageButton {

    private final ConstructTask action;

    public static final int ICON_SIZE = 16;

    public static final int ICON_TEXTURE_SIZE = 16;

    public static final int NODE_SIZE = 8;

    public static final int MAIN_TEXTURE_SIZE = 256;

    public static final int DELETE_INDEX = 97;

    public static final int INPUT_INDEX = 98;

    public static final int DRAG_INDEX = 99;

    public static final int WIDGET_WIDTH = 48;

    public static final int WIDGET_HEIGHT = 40;

    public static final int COLOR_GREY = FastColor.ARGB32.color(255, 220, 220, 220);

    public static final int COLOR_BLUE = FastColor.ARGB32.color(255, 0, 0, 255);

    public static final int COLOR_WHITE = FastColor.ARGB32.color(255, 255, 255, 255);

    public static final int COLOR_RED = FastColor.ARGB32.color(255, 255, 0, 0);

    public static final int COLOR_GREEN = FastColor.ARGB32.color(255, 0, 150, 0);

    public static final int COLOR_YELLOW = FastColor.ARGB32.color(255, 255, 255, 0);

    private int subClickIndex = -1;

    private HashMap<Integer, String> connections;

    Function<String, LodestarNode> nodeResolver;

    private ListTag parameterData;

    private String id;

    private boolean isStart = false;

    private double clickX;

    private double clickY;

    private double clickXOffset;

    private double clickYOffset;

    public LodestarNode(int x, int y, boolean lowTier, ConstructTask action, Button.OnPress pressHandler, Function<String, LodestarNode> resolver) {
        super(x, y, 48, 40, action.isCondition() ? 56 : 8, 171, 45, lowTier ? GuiTextures.Blocks.LODESTAR_LESSER_MAIN : GuiTextures.Blocks.LODESTAR_MAIN, 256, 256, pressHandler);
        this.action = action;
        this.connections = new HashMap();
        this.id = UUID.randomUUID().toString();
        this.parameterData = new ListTag();
        this.nodeResolver = resolver;
    }

    public void select() {
        this.m_93692_(true);
    }

    public void deselect() {
        this.m_93692_(false);
    }

    public ConstructTask getTask() {
        return this.action;
    }

    public String getId() {
        return this.id;
    }

    public boolean isStart() {
        return this.isStart;
    }

    public boolean isCondition() {
        return this.action.isCondition();
    }

    public void setStart(boolean start) {
        this.isStart = start;
    }

    public ListTag getParameterData() {
        return this.parameterData;
    }

    public void saveParameters(List<LodestarParameter<?>> parameterInputs) {
        List<ConstructAITaskParameter> params = this.getParameters();
        assert params.size() == parameterInputs.size();
        this.parameterData.clear();
        for (int i = 0; i < params.size(); i++) {
            ConstructAITaskParameter param = (ConstructAITaskParameter) params.get(i);
            LodestarParameter<?> paramInput = (LodestarParameter<?>) parameterInputs.get(i);
            paramInput.saveTo(param);
            this.parameterData.add(param.saveData());
        }
    }

    public void loadParameters(List<LodestarParameter<?>> parameterInputs) {
        List<ConstructAITaskParameter> params = this.getParameters();
        ListTag parameterData = this.getParameterData();
        assert params.size() == parameterInputs.size();
        for (int i = 0; i < params.size() && i < parameterData.size(); i++) {
            ((ConstructAITaskParameter) params.get(i)).loadData(parameterData.getCompound(i));
        }
        for (int i = 0; i < params.size(); i++) {
            ConstructAITaskParameter param = (ConstructAITaskParameter) params.get(i);
            LodestarParameter<?> paramInput = (LodestarParameter<?>) parameterInputs.get(i);
            paramInput.loadFrom(param);
        }
    }

    public CompoundTag toCompoundTag(int guiTop, int guiLeft) {
        CompoundTag tag = new CompoundTag();
        tag.put("parameters", this.parameterData);
        ListTag connectionData = new ListTag();
        this.connections.entrySet().forEach(c -> {
            CompoundTag connection = new CompoundTag();
            connection.putInt("index", (Integer) c.getKey());
            connection.putString("target", (String) c.getValue());
            connectionData.add(connection);
        });
        tag.put("connections", connectionData);
        tag.putInt("x", this.m_252754_() - guiLeft);
        tag.putInt("y", this.m_252907_() - guiTop);
        tag.putString("id", this.id);
        tag.putBoolean("start", this.isStart);
        tag.putString("task", ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(this.action).toString());
        return tag;
    }

    @Nullable
    public static LodestarNode fromCompoundTag(CompoundTag tag, int guiTop, int guiLeft, boolean lowTier, Button.OnPress pressHandler, Function<String, LodestarNode> resolver) {
        if (tag.contains("x") && tag.contains("y") && tag.contains("task") && tag.contains("id")) {
            int x = tag.getInt("x") + guiLeft;
            int y = tag.getInt("y") + guiTop;
            ResourceLocation taskID = new ResourceLocation(tag.getString("task"));
            ConstructTask action = (ConstructTask) ((IForgeRegistry) Registries.ConstructTasks.get()).getValue(taskID);
            if (action == null) {
                ManaAndArtifice.LOGGER.error("Failed to look up task (" + taskID.toString() + "); Node skipped.  This will likely break connections.");
                return null;
            } else {
                LodestarNode newNode = new LodestarNode(x, y, lowTier, action, pressHandler, resolver);
                if (tag.contains("parameters", 9)) {
                    newNode.parameterData = tag.getList("parameters", 10);
                }
                if (tag.contains("connections")) {
                    ListTag connections = tag.getList("connections", 10);
                    connections.forEach(t -> {
                        if (t instanceof CompoundTag connection && connection.contains("index") && connection.contains("target")) {
                            String id = connection.getString("target");
                            int index = connection.getInt("index");
                            newNode.connections.put(index, id);
                        }
                    });
                }
                newNode.id = tag.getString("id");
                newNode.isStart = tag.getBoolean("start");
                return newNode;
            }
        } else {
            return null;
        }
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.enableBlend();
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (this.f_93622_) {
            this.renderDeleteButton(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
        if (this.action != null) {
            this.renderInputNode(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            this.renderOutputNodeSuccess(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            for (int i = 1; i < this.action.getOutputs(); i++) {
                this.renderOutputNodeFailure(pGuiGraphics, pMouseX, pMouseY, i - 1, pPartialTick);
            }
            pGuiGraphics.blit(this.action.getIconTexture(), this.m_252754_() + 16, this.m_252907_() + 15, 0.0F, 0.0F, 16, 16, 16, 16);
        }
    }

    public void renderMisconfigured(GuiGraphics pGuiGraphics, List<String> misconfiguredNodeIDs) {
        if (misconfiguredNodeIDs.contains(this.id)) {
            pGuiGraphics.blit(this.f_94223_, this.m_252754_() + 4, this.m_252907_() + this.m_93694_() - 8, 8, 8, 154.0F, 232.0F, 12, 12, 256, 256);
        }
    }

    public void renderConnections(GuiGraphics pGuiGraphics, int x, int y, float partialTick) {
        if (this.isStart && this.m_252754_() > x) {
            GuiRenderUtils.bezierLineBetween(pGuiGraphics, (float) (x + 4 + 4 + 3) / GuiLodestarV2.Zoom, ((float) (y + 10 + 4) + 0.5F) / GuiLodestarV2.Zoom, (float) (this.m_252754_() + 8), (float) this.m_252907_() + 14.5F, 50.0F, 3.0F, COLOR_BLUE, COLOR_GREY);
        }
        this.connections.forEach((i, n) -> {
            if (n != null) {
                this.renderConnectingLine(pGuiGraphics, i, n);
            }
        });
    }

    private void renderDeleteButton(GuiGraphics pGuiGraphics, int mouse_x, int mouse_y, float partialTick) {
        pGuiGraphics.blit(this.f_94223_, this.m_252754_() + 39, this.m_252907_() + 1, 0.0F, 224.0F, 8, 8, 256, 256);
    }

    private void renderInputNode(GuiGraphics pGuiGraphics, int mouse_x, int mouse_y, float partialTick) {
        pGuiGraphics.blit(this.f_94223_, this.m_252754_() + 4, this.m_252907_() + 10, 0.0F, 232.0F, 8, 8, 256, 256);
    }

    private void renderOutputNodeSuccess(GuiGraphics pGuiGraphics, int mouse_x, int mouse_y, float partialTick) {
        pGuiGraphics.blit(this.f_94223_, this.m_252754_() + 36, this.m_252907_() + 10, 0.0F, 248.0F, 8, 8, 256, 256);
    }

    private void renderOutputNodeFailure(GuiGraphics pGuiGraphics, int x, int y, int index, float partialTick) {
        pGuiGraphics.blit(this.f_94223_, this.m_252754_() + 36, this.m_252907_() + 18 + index * 8, 0.0F, 240.0F, 8, 8, 256, 256);
    }

    public void renderConnectingLine(GuiGraphics pGuiGraphics, int nodeIndex, String other) {
        LodestarNode otherResolved = (LodestarNode) this.nodeResolver.apply(other);
        if (otherResolved != null) {
            float endX = (float) (otherResolved.m_252754_() + 8);
            float endY = (float) otherResolved.m_252907_() + 14.5F;
            if (nodeIndex == 0) {
                if (otherResolved.equals(this)) {
                    this.renderSelfConnectingLine(pGuiGraphics, nodeIndex, COLOR_GREEN, COLOR_GREY);
                } else {
                    this.renderConnectingLine(pGuiGraphics, nodeIndex, endX, endY, COLOR_GREEN, COLOR_GREY);
                }
            } else if (otherResolved.equals(this)) {
                this.renderSelfConnectingLine(pGuiGraphics, nodeIndex, COLOR_RED, COLOR_GREY);
            } else {
                this.renderConnectingLine(pGuiGraphics, nodeIndex, endX, endY, COLOR_RED, COLOR_GREY);
            }
        }
    }

    public void renderConnectingLine(GuiGraphics pGuiGraphics, int nodeIndex, float endX, float endY, int color) {
        this.renderConnectingLine(pGuiGraphics, nodeIndex, endX, endY, color, color);
    }

    private void renderSelfConnectingLine(GuiGraphics pGuiGraphics, int nodeIndex, int startColor, int endColor) {
        float startX = (float) (this.m_252754_() + 44);
        float startY = (float) this.m_252907_() + 14.5F + (float) (nodeIndex * 8);
        float endX = (float) (this.m_252754_() + 8);
        float endY = (float) this.m_252907_() + 14.5F;
        float midX = (startX + endX) / 2.0F;
        float midY = (float) (this.m_252907_() - 20);
        GuiRenderUtils.bezierLineBetween(pGuiGraphics, startX, startY, midX, midY, 0.0F, 3.0F, startColor, endColor, false);
        GuiRenderUtils.bezierLineBetween(pGuiGraphics, midX, midY, endX, endY, 0.0F, 3.0F, startColor, endColor, false);
    }

    public void renderConnectingLine(GuiGraphics pGuiGraphics, int nodeIndex, float endX, float endY, int startColor, int endColor) {
        float startX = (float) (this.m_252754_() + 44);
        float startY = (float) this.m_252907_() + 14.5F + (float) (nodeIndex * 8);
        GuiRenderUtils.bezierLineBetween(pGuiGraphics, startX, startY, endX, endY, 0.0F, 3.0F, startColor, endColor);
    }

    public void connectToNode(LodestarNode other, int index) {
        this.connections.put(index, other.getId());
    }

    public void disconnectFromNode(LodestarNode node) {
        ArrayList<Integer> indicesToRemove = new ArrayList();
        this.connections.forEach((i, c) -> {
            if (c.equals(node.getId())) {
                indicesToRemove.add(i);
            }
        });
        indicesToRemove.forEach(i -> this.connections.remove(i));
    }

    public int getSubWidgetAt(double mouse_x, double mouse_y) {
        if (this.mouseWithinWidgetCoords(mouse_x, mouse_y, 39, 1)) {
            return 97;
        } else if (this.mouseWithinWidgetCoords(mouse_x, mouse_y, 4, 10)) {
            return 98;
        } else {
            for (int i = 0; i < this.action.getOutputs(); i++) {
                if (this.mouseWithinWidgetCoords(mouse_x, mouse_y, 36, 10 + i * 8)) {
                    return i;
                }
            }
            return 99;
        }
    }

    public int getSubClickIndex() {
        return this.subClickIndex;
    }

    @Override
    public void onClick(double mouse_x, double mouse_y) {
        this.clickX = mouse_x;
        this.clickY = mouse_y;
        this.clickXOffset = mouse_x - (double) this.m_252754_();
        this.clickYOffset = mouse_y - (double) this.m_252907_();
        this.subClickIndex = this.getSubWidgetAt(mouse_x, mouse_y);
        super.m_5716_(mouse_x, mouse_y);
    }

    private boolean mouseWithinWidgetCoords(double mouse_x, double mouse_y, int x, int y) {
        return mouse_x >= (double) (this.m_252754_() + x) && mouse_x <= (double) (this.m_252754_() + x + 8) && mouse_y >= (double) (this.m_252907_() + y) && mouse_y <= (double) (this.m_252907_() + y + 8);
    }

    public List<ConstructAITaskParameter> getParameters() {
        return this.action.getParameters();
    }

    @Override
    public void playDownSound(SoundManager soundManager0) {
    }

    public double getClickX() {
        return this.clickX;
    }

    public double getClickY() {
        return this.clickY;
    }

    public double getClickXOffset() {
        return this.clickXOffset;
    }

    public double getClickYOffset() {
        return this.clickYOffset;
    }
}