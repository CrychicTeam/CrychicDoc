package noppes.npcs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.client.model.ModelPony;
import noppes.npcs.client.model.ModelPonyArmor;
import noppes.npcs.entity.EntityNpcPony;

public class RenderNPCPony<T extends EntityNpcPony, M extends ModelPony<T>> extends RenderNPCInterface<T, M> {

    private ModelPony modelBipedMain;

    private ModelPonyArmor modelArmorChestplate;

    private ModelPonyArmor modelArmor;

    public RenderNPCPony(EntityRendererProvider.Context manager, M model) {
        super(manager, model, 0.5F);
        this.modelBipedMain = model;
        this.modelArmorChestplate = new ModelPonyArmor(1.0F);
        this.modelArmor = new ModelPonyArmor(0.5F);
    }

    public ResourceLocation getTextureLocation(T pony) {
        boolean check = pony.textureLocation == null || pony.textureLocation != pony.checked;
        ResourceLocation loc = super.getTextureLocation(pony);
        if (check) {
            Resource resource = (Resource) Minecraft.getInstance().getResourceManager().m_213713_(loc).orElse(null);
            if (resource != null) {
                try {
                    BufferedImage bufferedimage = ImageIO.read(resource.open());
                    pony.isPegasus = false;
                    pony.isUnicorn = false;
                    Color color = new Color(bufferedimage.getRGB(0, 0), true);
                    Color color1 = new Color(249, 177, 49, 255);
                    Color color2 = new Color(136, 202, 240, 255);
                    Color color3 = new Color(209, 159, 228, 255);
                    Color color4 = new Color(254, 249, 252, 255);
                    if (color.equals(color1)) {
                    }
                    if (color.equals(color2)) {
                        pony.isPegasus = true;
                    }
                    if (color.equals(color3)) {
                        pony.isUnicorn = true;
                    }
                    if (color.equals(color4)) {
                        pony.isPegasus = true;
                        pony.isUnicorn = true;
                    }
                    pony.checked = loc;
                } catch (IOException var11) {
                }
            }
        }
        return loc;
    }

    public void render(T pony, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        ItemStack itemstack = pony.m_21205_();
        this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = itemstack == null ? 0 : 1;
        this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = pony.m_6047_();
        this.modelArmorChestplate.f_102609_ = this.modelArmor.f_102609_ = this.modelBipedMain.f_102609_ = false;
        this.modelArmorChestplate.isSleeping = this.modelArmor.isSleeping = this.modelBipedMain.isSleeping = pony.m_5803_();
        this.modelArmorChestplate.isUnicorn = this.modelArmor.isUnicorn = this.modelBipedMain.isUnicorn = pony.isUnicorn;
        this.modelArmorChestplate.isPegasus = this.modelArmor.isPegasus = this.modelBipedMain.isPegasus = pony.isPegasus;
        super.render(pony, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = false;
        this.modelArmorChestplate.f_102609_ = this.modelArmor.f_102609_ = this.modelBipedMain.f_102609_ = false;
        this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = false;
        this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 0;
    }
}