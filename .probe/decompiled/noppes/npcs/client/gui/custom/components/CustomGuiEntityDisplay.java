package noppes.npcs.client.gui.custom.components;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiEntityDisplayWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;
import noppes.npcs.entity.EntityNPCInterface;

public class CustomGuiEntityDisplay extends AbstractWidget implements IGuiComponent {

    private GuiCustom parent;

    public CustomGuiEntityDisplayWrapper component;

    private Entity entity;

    public int id;

    public CustomGuiEntityDisplay(GuiCustom parent, CustomGuiEntityDisplayWrapper component) {
        super(component.getPosX(), component.getPosY(), component.getWidth(), component.getHeight(), Component.empty());
        this.component = component;
        this.parent = parent;
        this.init();
    }

    @Override
    public void init() {
        this.id = this.component.getID();
        this.m_252865_(this.component.getPosX());
        this.m_253211_(this.component.getPosY());
        this.m_93674_(this.component.getWidth());
        this.setHeight(this.component.getHeight());
        if (!this.component.getEntityData().isEmpty()) {
            this.entity = (Entity) EntityType.create(this.component.getEntityData().getMCNBT(), Minecraft.getInstance().level).orElse(null);
        }
        this.f_93623_ = this.component.getEnabled() && this.component.getVisible();
        this.f_93624_ = this.component.getVisible();
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void onRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            if (this.component.getBackground()) {
                graphics.fillGradient(this.m_252754_(), this.m_252907_(), this.f_93618_ + this.m_252754_(), this.f_93619_ + this.m_252907_(), -1072689136, -804253680);
            }
            if (this.entity != null) {
                drawEntity(graphics, this.entity, this.m_252754_(), this.m_252907_(), this.component.getScale(), this.component.getRotation() / 2 + 180, mouseX, mouseY, (float) this.f_93618_ / 2.0F, (float) this.f_93619_ * 0.9F);
            }
            boolean hovered = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            if (hovered && this.component.hasHoverText()) {
                this.parent.hoverText = this.component.getHoverTextList();
            }
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dx, double dy) {
        return true;
    }

    public static CustomGuiEntityDisplay fromComponent(GuiCustom parent, CustomGuiEntityDisplayWrapper component) {
        return new CustomGuiEntityDisplay(parent, component);
    }

    public static void drawEntity(GuiGraphics graphics, Entity entity, int x, int y, float zoomed, int rotation, int xMouse, int yMouse, float guiLeft, float guiTop) {
        EntityNPCInterface npc = null;
        if (entity instanceof EntityNPCInterface) {
            npc = (EntityNPCInterface) entity;
        }
        LivingEntity livingEntity = null;
        if (entity instanceof LivingEntity) {
            livingEntity = (LivingEntity) entity;
        }
        float f3 = entity.getYRot();
        float f4 = entity.getXRot();
        float f2 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        if (livingEntity != null) {
            f2 = livingEntity.yBodyRot;
            f5 = livingEntity.yHeadRotO;
            f6 = livingEntity.yHeadRot;
        }
        float scale = 1.0F;
        if ((double) entity.getBbHeight() > 2.4) {
            scale = 2.0F / entity.getBbHeight();
        }
        float f7 = guiLeft + (float) x - (float) xMouse;
        float f8 = (guiTop + (float) y - 50.0F * scale * zoomed) * (entity.getBbHeight() / entity.getEyeHeight()) - (float) yMouse;
        entity.setYRot((float) Math.atan((double) (f7 / 80.0F)) * 40.0F + (float) rotation);
        entity.setXRot(-((float) Math.atan((double) (f8 / 40.0F))) * 20.0F);
        if (livingEntity != null) {
            livingEntity.yHeadRotO = livingEntity.yHeadRot = livingEntity.yBodyRot = entity.getYRot();
        }
        int orientation = 0;
        if (npc != null) {
            orientation = npc.ais.orientation;
            npc.ais.orientation = (int) entity.getYRot();
        }
        float fs = 30.0F * scale * zoomed;
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.translate(0.0F, 0.0F, 1050.0F);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack matrixStack = new PoseStack();
        matrixStack.translate(guiLeft + (float) x, guiTop + (float) y, 0.0F);
        matrixStack.scale(fs, fs, fs);
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        matrixStack.mulPose(Axis.YN.rotationDegrees((float) rotation));
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityrenderdispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, matrixStack, graphics.bufferSource(), 15728880));
        graphics.flush();
        entityrenderdispatcher.setRenderShadow(true);
        posestack.scale(1.0F, 1.0F, -1.0F);
        posestack.translate(0.0F, 0.0F, -1050.0F);
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
        matrixStack.popPose();
        entity.setYRot(f3);
        entity.setXRot(f4);
        if (livingEntity != null) {
            livingEntity.yBodyRot = f2;
            livingEntity.yHeadRotO = f5;
            livingEntity.yHeadRot = f6;
        }
        if (npc != null) {
            npc.ais.orientation = orientation;
        }
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }

    @Override
    public ICustomGuiComponent component() {
        return this.component;
    }
}