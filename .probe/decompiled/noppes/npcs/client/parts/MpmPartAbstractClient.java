package noppes.npcs.client.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.shared.common.util.NopVector3f;

public abstract class MpmPartAbstractClient extends MpmPart {

    public NopVector3f pos = NopVector3f.ZERO;

    public NopVector3f rot = NopVector3f.ZERO;

    protected Map<String, ModelPartWrapper> defaultPose = new HashMap();

    public void render(MpmPartData data, PoseStack mStack, MultiBufferSource typeBuffer, int lightmapUV, LivingEntity player) {
        VertexConsumer c = typeBuffer.getBuffer(RenderType.entityTranslucent(data.usePlayerSkin ? ((EntityCustomNpc) player).textureLocation : data.getTexture()));
        this.render(data, mStack, c, lightmapUV, player);
    }

    public void render(MpmPartData data, PoseStack mStack, VertexConsumer c, int lightmapUV, LivingEntity player) {
    }

    @Override
    public final ModelPartWrapper getPart(String name) {
        return (ModelPartWrapper) this.defaultPose.get(name);
    }
}