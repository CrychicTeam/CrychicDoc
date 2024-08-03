package snownee.kiwi.contributor.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collection;
import java.util.Locale;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.kiwi.contributor.Contributors;
import snownee.kiwi.contributor.ITierProvider;

@OnlyIn(Dist.CLIENT)
public class CosmeticLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public static final Collection<CosmeticLayer> ALL_LAYERS = Lists.newLinkedList();

    private final Cache<String, RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>> player2renderer;

    public final RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer;

    public CosmeticLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> entityRendererIn) {
        super(entityRendererIn);
        this.renderer = entityRendererIn;
        if (this.getClass() == CosmeticLayer.class) {
            this.player2renderer = CacheBuilder.newBuilder().build();
        } else {
            this.player2renderer = null;
        }
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.player2renderer != null) {
            RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = (RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) this.player2renderer.getIfPresent(entitylivingbaseIn.m_36316_().getName());
            if (renderer == null) {
                String name = entitylivingbaseIn.m_36316_().getName();
                ResourceLocation id = (ResourceLocation) Contributors.PLAYER_COSMETICS.get(name);
                if (id != null) {
                    ITierProvider provider = (ITierProvider) Contributors.REWARD_PROVIDERS.get(id.getNamespace().toLowerCase(Locale.ENGLISH));
                    if (provider == null) {
                        Contributors.PLAYER_COSMETICS.remove(name);
                    } else {
                        renderer = provider.createRenderer(this.renderer, id.getPath());
                        if (renderer != null) {
                            this.player2renderer.put(name, renderer);
                        }
                    }
                }
            }
            if (renderer != null) {
                renderer.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }

    public Cache<String, RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>> getCache() {
        return this.player2renderer;
    }
}