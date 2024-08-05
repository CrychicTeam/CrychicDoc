package dev.xkmc.modulargolems.content.entity.common;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.client.override.ModelOverrides;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractGolemRenderer<T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>, M extends EntityModel<T> & IGolemModel<T, P, M>> extends MobRenderer<T, M> {

    public static final List<Function<AbstractGolemRenderer<?, ?, ?>, RenderLayer<? extends AbstractGolemEntity<?, ?>, ?>>> LIST = new ArrayList();

    private static final ResourceLocation GOLEM_LOCATION = new ResourceLocation("textures/entity/iron_golem/iron_golem.png");

    private final ThreadLocal<RenderHandle<T>> handle = new ThreadLocal();

    private final Supplier<P[]> list;

    public AbstractGolemRenderer(EntityRendererProvider.Context ctx, M model, float f, Supplier<P[]> list) {
        super(ctx, model, f);
        this.list = list;
        LIST.forEach(e -> this.m_115326_((RenderLayer) Wrappers.cast((RenderLayer) e.apply(this))));
    }

    protected void scale(T entity, PoseStack pose, float f) {
        float r = entity.getScale();
        pose.scale(r, r, r);
    }

    public ResourceLocation getTextureLocation(T entity) {
        return GOLEM_LOCATION;
    }

    protected boolean delegated(T entity) {
        return false;
    }

    public void render(T entity, float f1, float f2, PoseStack stack, MultiBufferSource source, int i) {
        this.handle.set(new RenderHandle(entity, f1, f2, stack, source, i));
        super.render(entity, f1, f2, stack, source, i);
        this.handle.remove();
    }

    @Nullable
    protected RenderType getRenderType(T entity, boolean b1, boolean b2, boolean b3) {
        if (this.delegated(entity)) {
            return super.m_7225_(entity, b1, b2, b3);
        } else if (this.handle.get() == null) {
            return null;
        } else {
            boolean flag = this.m_5933_(entity);
            boolean flag1 = !flag && !entity.m_20177_(Proxy.getClientPlayer());
            PoseStack pose = ((RenderHandle) this.handle.get()).stack();
            pose.pushPose();
            for (P p : (IGolemPart[]) this.list.get()) {
                this.renderPart(p, entity, b1, b2, b3, flag1);
            }
            pose.popPose();
            return null;
        }
    }

    private void renderPart(P type, T entity, boolean b1, boolean b2, boolean b3, boolean flag1) {
        RenderType rendertype = this.getRenderTypeInternal(type, entity, b1, b2, b3);
        RenderHandle<T> hand = (RenderHandle<T>) this.handle.get();
        if (rendertype != null) {
            VertexConsumer vertexconsumer = hand.source().getBuffer(rendertype);
            int i = m_115338_(entity, this.m_6931_(entity, hand.f2()));
            ((IGolemModel) this.f_115290_).renderToBufferInternal(type, hand.stack(), vertexconsumer, hand.i(), i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
        }
    }

    @Nullable
    private RenderType getRenderTypeInternal(P type, T entity, boolean b1, boolean b2, boolean b3) {
        ArrayList<GolemMaterial> materials = entity.getMaterials();
        int index = type.ordinal();
        ResourceLocation rl = materials.size() > index ? ((GolemMaterial) materials.get(index)).id() : GolemMaterial.EMPTY;
        ResourceLocation resourcelocation = ((IGolemModel) this.f_115290_).getTextureLocationInternal(ModelOverrides.getOverride(rl).getTexture(entity, rl));
        if (b2) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (b1) {
            return this.f_115290_.m_103119_(resourcelocation);
        } else {
            return b3 ? RenderType.outline(resourcelocation) : null;
        }
    }
}