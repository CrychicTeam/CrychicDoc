package noppes.npcs.client.renderer;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.io.File;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.MatrixStackMixin;
import noppes.npcs.shared.client.util.ImageDownloadAlt;
import noppes.npcs.shared.client.util.ResourceDownloader;
import noppes.npcs.shared.common.util.LogWriter;
import org.joml.Matrix4f;

public class RenderNPCInterface<T extends EntityNPCInterface, M extends EntityModel<T>> extends LivingEntityRenderer<T, M> {

    public static int LastTextureTick;

    public static EntityNPCInterface currentNpc;

    public RenderNPCInterface(EntityRendererProvider.Context manager, M model, float f) {
        super(manager, model, f);
    }

    public void renderNameTag(T npc, Component text, PoseStack matrixStack, MultiBufferSource buffer, int light) {
        if (npc != null && this.m_6512_(npc) && this.f_114476_ != null) {
            double d0 = this.f_114476_.distanceToSqr(npc);
            if (!(d0 > 512.0)) {
                matrixStack.pushPose();
                Vec3 renderOffset = this.m_7860_(npc, 0.0F);
                matrixStack.translate(-renderOffset.x(), -renderOffset.y(), -renderOffset.z());
                if (npc.messages != null) {
                    float height = npc.baseSize.height / 5.0F * (float) npc.display.getSize();
                    float offset = npc.m_20206_() * (1.2F + (!npc.display.showName() ? 0.0F : (npc.display.getTitle().isEmpty() ? 0.15F : 0.25F)));
                    matrixStack.translate(0.0F, offset, 0.0F);
                    npc.messages.renderMessages(matrixStack, buffer, 0.666667F * height, npc.isInRange(this.f_114476_.camera.getEntity(), 4.0), light);
                    matrixStack.translate(0.0F, -offset, 0.0F);
                }
                if (npc.display.showName()) {
                    this.renderLivingLabel(npc, matrixStack, buffer, light);
                }
                matrixStack.popPose();
            }
        }
    }

    protected void renderLivingLabel(T npc, PoseStack matrixStack, MultiBufferSource buffer, int light) {
        float scale = npc.baseSize.height / 5.0F * (float) npc.display.getSize();
        float height = npc.m_20206_() - 0.06F * scale;
        matrixStack.pushPose();
        Font fontrenderer = this.m_114481_();
        float f2 = 0.01666667F * scale;
        matrixStack.translate(0.0F, height, 0.0F);
        matrixStack.mulPose(this.f_114476_.cameraOrientation());
        int color = npc.getFaction().color;
        matrixStack.translate(0.0F, scale / 6.5F * 2.0F, 0.0F);
        float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int j = (int) (f1 * 255.0F) << 24;
        matrixStack.scale(-f2, -f2, f2);
        Matrix4f matrix4f = matrixStack.last().pose();
        float y = 0.0F;
        boolean nearby = npc.isInRange(this.f_114476_.camera.getEntity(), 8.0);
        if (!npc.display.getTitle().isEmpty() && nearby) {
            Component title = Component.literal("<").append(Component.translatable(npc.display.getTitle())).append(">");
            float f3 = 0.6F;
            matrixStack.translate(0.0F, 4.0F, 0.0F);
            matrixStack.scale(f3, f3, f3);
            fontrenderer.drawInBatch(title, (float) (-fontrenderer.width(title) / 2), 0.0F, color, false, matrix4f, buffer, Font.DisplayMode.NORMAL, j, light);
            matrixStack.scale(1.0F / f3, 1.0F / f3, 1.0F / f3);
            y = -10.0F;
        }
        Component name = npc.getName();
        fontrenderer.drawInBatch(name, (float) (-fontrenderer.width(name) / 2), y, color, false, matrix4f, buffer, nearby ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, light);
        if (nearby) {
            fontrenderer.drawInBatch(name, (float) (-fontrenderer.width(name) / 2), y, color, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, light);
        }
        matrixStack.popPose();
    }

    protected void renderColor(EntityNPCInterface npc) {
        if (npc.f_20916_ <= 0 && npc.f_20919_ <= 0) {
            float red = (float) (npc.display.getTint() >> 16 & 0xFF) / 255.0F;
            float green = (float) (npc.display.getTint() >> 8 & 0xFF) / 255.0F;
            float blue = (float) (npc.display.getTint() & 0xFF) / 255.0F;
            RenderSystem.setShaderColor(red, green, blue, 1.0F);
        }
    }

    protected void setupRotations(T npc, PoseStack matrixScale, float f, float f1, float f2) {
        if (npc.isAlive() && npc.isSleeping()) {
            matrixScale.mulPose(Axis.YP.rotationDegrees((float) npc.ais.orientation));
            matrixScale.mulPose(Axis.ZP.rotationDegrees(this.m_6441_(npc)));
            matrixScale.mulPose(Axis.YP.rotationDegrees(270.0F));
        } else if (npc.isAlive() && npc.currentAnimation == 7) {
            matrixScale.mulPose(Axis.YP.rotationDegrees(270.0F - f1));
            float scale = (float) ((EntityCustomNpc) npc).display.getSize() / 5.0F;
            matrixScale.translate(-scale + ((EntityCustomNpc) npc).modelData.getLegsY() * scale, 0.14F, 0.0F);
            matrixScale.mulPose(Axis.ZP.rotationDegrees(270.0F));
            matrixScale.mulPose(Axis.YP.rotationDegrees(270.0F));
        } else {
            super.setupRotations(npc, matrixScale, f, f1, f2);
        }
    }

    protected void scale(T npc, PoseStack matrixScale, float f) {
        this.renderColor(npc);
        int size = npc.display.getSize();
        matrixScale.scale(npc.scaleX / 5.0F * (float) size, npc.scaleY / 5.0F * (float) size, npc.scaleZ / 5.0F * (float) size);
    }

    public void render(T npc, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        if (npc.isKilled()) {
            this.f_114477_ = 0.0F;
        }
        if (!npc.isKilled() || !npc.stats.hideKilledBody || npc.f_20919_ <= 20) {
            float xOffset = 0.0F;
            float yOffset = npc.currentAnimation == 0 ? npc.ais.bodyOffsetY / 10.0F - 0.5F : 0.0F;
            float zOffset = 0.0F;
            if (npc.isAlive()) {
                if (npc.isSleeping()) {
                    xOffset = (float) (-Math.cos(Math.toRadians((double) (180 - npc.ais.orientation))));
                    zOffset = (float) (-Math.sin(Math.toRadians((double) npc.ais.orientation)));
                    yOffset += 0.14F;
                } else if (npc.currentAnimation == 1 || npc.m_20159_()) {
                    yOffset -= 0.5F - ((EntityCustomNpc) npc).modelData.getLegsY() * 0.8F;
                }
            }
            xOffset = xOffset / 5.0F * (float) npc.display.getSize();
            yOffset = yOffset / 5.0F * (float) npc.display.getSize();
            zOffset = zOffset / 5.0F * (float) npc.display.getSize();
            if ((npc.display.getBossbar() == 1 || npc.display.getBossbar() == 2 && npc.isAttacking()) && !npc.isKilled() && npc.f_20919_ <= 20 && npc.canNpcSee(Minecraft.getInstance().player)) {
            }
            if (npc.ais.getStandingType() == 3 && !npc.isWalking() && !npc.isInteracting()) {
                npc.f_20884_ = npc.f_20883_ = (float) npc.ais.orientation;
            }
            this.f_114477_ = npc.m_20205_() * 0.8F;
            int stackSize = ((MatrixStackMixin) matrixStack).getStack().size();
            try {
                currentNpc = npc;
                super.render(npc, entityYaw, partialTicks, matrixStack, buffer, packedLight);
            } catch (Throwable var15) {
                while (((MatrixStackMixin) matrixStack).getStack().size() > stackSize) {
                    matrixStack.popPose();
                }
                LogWriter.except(var15);
            } finally {
                currentNpc = null;
            }
        }
    }

    protected float getBob(T npc, float limbSwingAmount) {
        return !npc.isKilled() && npc.display.getHasLivingAnimation() ? super.getBob(npc, limbSwingAmount) : 0.0F;
    }

    public ResourceLocation getTextureLocation(T npc) {
        if (npc.textureLocation == null) {
            if (npc.display.skinType == 0) {
                npc.textureLocation = new ResourceLocation(npc.display.getSkinTexture());
            } else {
                if (LastTextureTick < 5) {
                    return DefaultPlayerSkin.getDefaultSkin();
                }
                if (npc.display.skinType == 1 && npc.display.playerProfile != null) {
                    Minecraft minecraft = Minecraft.getInstance();
                    Map map = minecraft.getSkinManager().getInsecureSkinInformation(npc.display.playerProfile);
                    if (map.containsKey(Type.SKIN)) {
                        npc.textureLocation = minecraft.getSkinManager().registerTexture((MinecraftProfileTexture) map.get(Type.SKIN), Type.SKIN);
                    }
                } else if (npc.display.skinType == 2 && !npc.display.getSkinUrl().isEmpty()) {
                    try {
                        boolean fixSkin = npc instanceof EntityCustomNpc && ((EntityCustomNpc) npc).modelData.getEntity(npc) != null;
                        File file = ResourceDownloader.getUrlFile(npc.display.getSkinUrl(), fixSkin);
                        npc.textureLocation = ResourceDownloader.getUrlResourceLocation(npc.display.getSkinUrl(), fixSkin);
                        this.loadSkin(file, npc.textureLocation, npc.display.getSkinUrl(), fixSkin);
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }
                }
            }
        }
        return npc.textureLocation == null ? DefaultPlayerSkin.getDefaultSkin() : npc.textureLocation;
    }

    private void loadSkin(File file, ResourceLocation resource, String par1Str, boolean fix64) {
        TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
        AbstractTexture object = texturemanager.getTexture(resource, null);
        if (object == null) {
            ResourceDownloader.load(new ImageDownloadAlt(file, par1Str, resource, DefaultPlayerSkin.getDefaultSkin(), fix64, () -> {
            }));
        }
    }
}