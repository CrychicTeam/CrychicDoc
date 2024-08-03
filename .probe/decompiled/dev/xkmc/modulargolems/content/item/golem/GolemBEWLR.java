package dev.xkmc.modulargolems.content.item.golem;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.core.ModelProvider;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.IGolemModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class GolemBEWLR extends BlockEntityWithoutLevelRenderer {

    public static final Supplier<BlockEntityWithoutLevelRenderer> INSTANCE = Suppliers.memoize(() -> new GolemBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

    public static final IClientItemExtensions EXTENSIONS = new IClientItemExtensions() {

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return (BlockEntityWithoutLevelRenderer) GolemBEWLR.INSTANCE.get();
        }
    };

    private final EntityModelSet entityModelSet;

    private final HashMap<ResourceLocation, IGolemModel<?, ?, ?>> map = new HashMap();

    public GolemBEWLR(BlockEntityRenderDispatcher dispatcher, EntityModelSet set) {
        super(dispatcher, set);
        this.entityModelSet = set;
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        this.map.clear();
        GolemType.GOLEM_TYPE_TO_MODEL.forEach((k, v) -> this.map.put(k, ((ModelProvider) v.get()).generateModel(this.entityModelSet)));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext type, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        BEWLRHandle handle = new BEWLRHandle(stack, type, poseStack, bufferSource, light, overlay);
        poseStack.pushPose();
        if (stack.getItem() instanceof IGolemPartItem part) {
            this.render(handle, part.asPart());
        }
        if (stack.getItem() instanceof GolemHolder<?, ?> holder && !this.renderEntity(handle, holder)) {
            this.render(handle, holder);
        }
        poseStack.popPose();
    }

    private <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> void render(BEWLRHandle handle, GolemHolder<T, P> item) {
        ArrayList<GolemMaterial> list = GolemHolder.getMaterial(handle.stack());
        P[] parts = item.getEntityType().values();
        PoseStack stack = handle.poseStack();
        parts[0].setupItemRender(stack, handle.type(), null);
        for (int i = 0; i < parts.length; i++) {
            ResourceLocation id = list.size() > i ? ((GolemMaterial) list.get(i)).id() : GolemMaterial.EMPTY;
            this.renderPart(handle, id, item.getEntityType(), parts[i]);
        }
    }

    private <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> void render(BEWLRHandle handle, GolemPart<T, P> item) {
        PoseStack stack = handle.poseStack();
        P part = item.getPart();
        part.setupItemRender(stack, handle.type(), part);
        Optional<ResourceLocation> id = GolemPart.getMaterial(handle.stack());
        this.renderPart(handle, (ResourceLocation) id.orElse(GolemMaterial.EMPTY), item.getEntityType(), part);
    }

    private <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>, M extends EntityModel<T> & IGolemModel<T, P, M>> void renderPart(BEWLRHandle handle, ResourceLocation id, GolemType<T, P> type, P part) {
        M model = (M) Wrappers.cast((IGolemModel) this.map.get(type.getRegistryName()));
        RenderType render = model.m_103119_(model.getTextureLocationInternal(id));
        VertexConsumer vc = ItemRenderer.getFoilBufferDirect(handle.bufferSource(), render, false, handle.stack().hasFoil());
        model.renderToBufferInternal(part, handle.poseStack(), vc, handle.light(), handle.overlay(), 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> boolean renderEntity(BEWLRHandle handle, GolemHolder<T, P> item) {
        T golem = ClientHolderManager.getEntityForDisplay(item, handle.stack());
        if (golem == null) {
            return false;
        } else {
            P[] parts = item.getEntityType().values();
            PoseStack stack = handle.poseStack();
            parts[0].setupItemRender(stack, handle.type(), null);
            stack.translate(0.0, 1.501, 0.0);
            stack.scale(1.0F, -1.0F, -1.0F);
            EntityRenderer<? super T> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(golem);
            golem.f_19797_ = Proxy.getClientPlayer().f_19797_;
            renderer.render(golem, 0.0F, Minecraft.getInstance().getPartialTick(), handle.poseStack(), handle.bufferSource(), handle.light());
            return true;
        }
    }
}