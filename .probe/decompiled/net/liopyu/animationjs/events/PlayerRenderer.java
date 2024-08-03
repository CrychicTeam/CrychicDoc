package net.liopyu.animationjs.events;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.latvian.mods.kubejs.player.SimplePlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import java.util.function.Consumer;
import net.liopyu.animationjs.events.subevents.client.ClientEventHandlers;
import net.liopyu.animationjs.utils.AnimationJSHelperClass;
import net.liopyu.animationjs.utils.ContextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

@OnlyIn(Dist.CLIENT)
public class PlayerRenderer extends SimplePlayerEventJS {

    public transient ContextUtils.PlayerRenderContext playerRenderContext;

    public transient boolean eventCancelled = false;

    public PlayerRenderer(Player player) {
        super(player);
    }

    @Override
    public Player getEntity() {
        return this.playerRenderContext.entity;
    }

    @Nullable
    @Override
    public Player getPlayer() {
        return this.playerRenderContext.entity;
    }

    @Info("Retrieves a list of model parts.\n\nExample Usage:\n```javascript\nAnimationJS.playerRenderer(event => {\n    let parts = event.getModelParts();\n})\n```\n")
    public Iterable<ModelPart> getModelParts() {
        return ImmutableList.of(((PlayerModel) this.playerRenderContext.renderer.m_7200_()).f_102810_, ((PlayerModel) this.playerRenderContext.renderer.m_7200_()).f_102811_, ((PlayerModel) this.playerRenderContext.renderer.m_7200_()).f_102812_, ((PlayerModel) this.playerRenderContext.renderer.m_7200_()).f_102813_, ((PlayerModel) this.playerRenderContext.renderer.m_7200_()).f_102814_, ((PlayerModel) this.playerRenderContext.renderer.m_7200_()).f_102809_);
    }

    @Info("Retrieves the body model part.\n\nExample Usage:\n```javascript\nAnimationJS.playerRenderer(event => {\n    let body = event.getBody();\n})\n```\n")
    public ModelPart getBody() {
        return ((PlayerModel) this.playerRenderContext.renderer.m_7200_()).f_102810_;
    }

    @Info("Retrieves the head model part.\n\nExample Usage:\n```javascript\nAnimationJS.playerRenderer(event => {\n    let head = event.getHead();\n})\n```\n")
    public ModelPart getHead() {
        return ((PlayerModel) this.playerRenderContext.renderer.m_7200_()).f_102808_;
    }

    @Info("Retrieves the right arm model part.\n\nExample Usage:\n```javascript\nAnimationJS.playerRenderer(event => {\n    let rightArm = event.getRightArm();\n})\n```\n")
    public ModelPart getRightArm() {
        return ((PlayerModel) this.playerRenderContext.renderer.m_7200_()).f_102811_;
    }

    @Info("Retrieves the left arm model part.\n\nExample Usage:\n```javascript\nAnimationJS.playerRenderer(event => {\n    let leftArm = event.getLeftArm();\n})\n```\n")
    public ModelPart getLeftArm() {
        return ((PlayerModel) this.playerRenderContext.renderer.m_7200_()).f_102812_;
    }

    @Info("Retrieves the right leg model part.\n\nExample Usage:\n```javascript\nAnimationJS.playerRenderer(event => {\n    let rightLeg = event.getRightLeg();\n})\n```\n")
    public ModelPart getRightLeg() {
        return ((PlayerModel) this.playerRenderContext.renderer.m_7200_()).f_102813_;
    }

    @Info("Retrieves the left leg model part.\n\nExample Usage:\n```javascript\nAnimationJS.playerRenderer(event => {\n    let leftLeg = event.getLeftLeg();\n})\n```\n")
    public ModelPart getLeftLeg() {
        return ((PlayerModel) this.playerRenderContext.renderer.m_7200_()).f_102814_;
    }

    @Info("Retrieves the hat model part.\n\nExample Usage:\n```javascript\nAnimationJS.playerRenderer(event => {\n    let hat = event.getHat();\n})\n```\n")
    public ModelPart getHat() {
        return ((PlayerModel) this.playerRenderContext.renderer.m_7200_()).f_102809_;
    }

    @Info("Renders an item on the body of a player with customizable position and rotation.\n\nExample Usage:\n```javascript\nAnimationJS.playerRenderer(event => {\n\tevent.renderContext\n\tconst { renderer, entity, entityYaw, partialTicks, poseStack, buffer, packedLight } = event.renderContext;\n})\n```\n")
    public ContextUtils.PlayerRenderContext getRenderContext() {
        return this.playerRenderContext;
    }

    @Info("Used to cancel the default player renderer. Doing this will halt the default minecraft\nrenderer method but will not disable AnimationJS' animation render logic\n")
    public void cancelDefaultRender() {
        this.eventCancelled = true;
    }

    @Info("Used to customize rendering of player entities.\n\nExample Usage:\n```javascript\nevent.render(context => {\n\tconst { renderer, entity, entityYaw, partialTicks, poseStack, buffer, packedLight } = context;\n\t// Your custom rendering logic goes here\n});\n```\n")
    public void render(Consumer<ContextUtils.PlayerRenderContext> c) {
        if (this.playerRenderContext == null) {
            AnimationJSHelperClass.logClientErrorMessageOnce("[AnimationJS]: Render context is null. If you see this message something is wrong, please notify the mod author.");
        } else {
            try {
                c.accept(this.playerRenderContext);
            } catch (Exception var3) {
                AnimationJSHelperClass.logClientErrorMessageOnceCatchable("[AnimationJS]: Error in playerRenderer for field: render.", var3);
            }
        }
    }

    @Info(value = "Renders an item on the body of a player with customizable position and rotation.\n\nExample Usage:\n```javascript\nAnimationJS.playerRenderer(event => {\n\tevent.renderBodyItem(\"minecraft:diamond_axe\", 0, 0.5, 0.25, 180, 0, 0)\n})\n```\n", params = { @Param(name = "itemStack", value = "Object: The item stack to render (String (item ID), ResourceLocation, or ItemStack)"), @Param(name = "xOffset", value = "Float: The offset along the X-axis"), @Param(name = "yOffset", value = "Float: The offset along the Y-axis"), @Param(name = "zOffset", value = "Float: The offset along the Z-axis"), @Param(name = "xRotation", value = "Float: The rotation around the X-axis (in degrees)"), @Param(name = "yRotation", value = "Float: The rotation around the Y-axis (in degrees)"), @Param(name = "zRotation", value = "Float: The rotation around the Z-axis (in degrees)") })
    @Unique
    public void renderBodyItem(Object itemStack, float xOffset, float yOffset, float zOffset, float xRotation, float yRotation, float zRotation) {
        if (!(Minecraft.getInstance().screen instanceof CreativeModeInventoryScreen) && !(Minecraft.getInstance().screen instanceof InventoryScreen)) {
            Object obj = AnimationJSHelperClass.convertObjectToDesired(itemStack, "itemstack");
            PlayerRenderer renderer = (PlayerRenderer) ClientEventHandlers.thisRenderList.get(this.getPlayer().m_20148_());
            if (renderer == null) {
                AnimationJSHelperClass.logClientErrorMessageOnce("[AnimationJS]: Renderer is null. If you see this message something is wrong, please notify the mod author.");
            } else {
                if (obj != null) {
                    ContextUtils.PlayerRenderContext context = renderer.playerRenderContext;
                    PoseStack poseStack = context.poseStack;
                    MultiBufferSource buffer = context.buffer;
                    int packedLight = context.packedLight;
                    AbstractClientPlayer player = context.entity;
                    float yRotationOffset = 90.0F - player.f_20884_;
                    poseStack.pushPose();
                    poseStack.mulPose(Axis.XP.rotationDegrees(0.0F));
                    poseStack.mulPose(Axis.YP.rotationDegrees(yRotationOffset));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(0.0F));
                    poseStack.translate(xOffset, yOffset, zOffset);
                    poseStack.mulPose(Axis.XP.rotationDegrees(xRotation));
                    poseStack.mulPose(Axis.YP.rotationDegrees(yRotation));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(zRotation));
                    Minecraft.getInstance().getItemRenderer().renderStatic((ItemStack) obj, ItemDisplayContext.NONE, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, player.m_9236_(), 0);
                    poseStack.popPose();
                } else {
                    AnimationJSHelperClass.logClientErrorMessageOnce("[AnimationJS]: Error in player renderer for method: renderBodyItem. ItemStack is either null or invalid");
                }
            }
        }
    }

    @Info(value = "Renders an item on the body of a player with customizable position and rotation with\nitem lighting overlay option.\n\nExample Usage:\n```javascript\nAnimationJS.playerRenderer(event => {\n\tevent.renderBodyItem(\"minecraft:diamond_axe\", 0.25, 1, 0, 0, 90, 0,15)\n})\n```\n", params = { @Param(name = "itemStack", value = "Object: The item stack to render (String (item ID), ResourceLocation, or ItemStack)"), @Param(name = "xOffset", value = "Float: The offset along the X-axis"), @Param(name = "yOffset", value = "Float: The offset along the Y-axis"), @Param(name = "zOffset", value = "Float: The offset along the Z-axis"), @Param(name = "xRotation", value = "Float: The rotation around the X-axis (in degrees)"), @Param(name = "yRotation", value = "Float: The rotation around the Y-axis (in degrees)"), @Param(name = "zRotation", value = "Float: The rotation around the Z-axis (in degrees)"), @Param(name = "packedLight", value = "int: The light level of the item's model") })
    @Unique
    public void renderBodyItem(Object itemStack, float xOffset, float yOffset, float zOffset, float xRotation, float yRotation, float zRotation, int packedLight) {
        if (!(Minecraft.getInstance().screen instanceof CreativeModeInventoryScreen) && !(Minecraft.getInstance().screen instanceof InventoryScreen)) {
            Object obj = AnimationJSHelperClass.convertObjectToDesired(itemStack, "itemstack");
            PlayerRenderer renderer = (PlayerRenderer) ClientEventHandlers.thisRenderList.get(this.getPlayer().m_20148_());
            if (renderer == null) {
                AnimationJSHelperClass.logClientErrorMessageOnce("[AnimationJS]: Renderer is null. If you see this message something is wrong, please notify the mod author.");
            } else {
                if (obj != null) {
                    ContextUtils.PlayerRenderContext context = renderer.playerRenderContext;
                    PoseStack poseStack = context.poseStack;
                    MultiBufferSource buffer = context.buffer;
                    int pL = LightTexture.pack(packedLight, packedLight);
                    AbstractClientPlayer player = context.entity;
                    float yRotationOffset = 90.0F - player.f_20884_;
                    poseStack.pushPose();
                    poseStack.mulPose(Axis.XP.rotationDegrees(0.0F));
                    poseStack.mulPose(Axis.YP.rotationDegrees(yRotationOffset));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(0.0F));
                    poseStack.translate(xOffset, yOffset, zOffset);
                    poseStack.mulPose(Axis.XP.rotationDegrees(xRotation));
                    poseStack.mulPose(Axis.YP.rotationDegrees(yRotation));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(zRotation));
                    Minecraft.getInstance().getItemRenderer().renderStatic((ItemStack) obj, ItemDisplayContext.NONE, pL, OverlayTexture.NO_OVERLAY, poseStack, buffer, player.m_9236_(), 0);
                    poseStack.popPose();
                } else {
                    AnimationJSHelperClass.logClientErrorMessageOnce("[AnimationJS]: Error in player renderer for method: renderBodyItem. ItemStack is either null or invalid");
                }
            }
        }
    }
}