package vazkii.patchouli.client.base;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.item.ItemModBook;

public class BookModel implements BakedModel {

    private final BakedModel original;

    private final ItemOverrides itemHandler;

    public BookModel(BakedModel original, ModelBakery loader) {
        this.original = original;
        BlockModel missing = (BlockModel) loader.getModel(ModelBakery.MISSING_MODEL_LOCATION);
        this.itemHandler = new ItemOverrides(new ModelBaker() {

            public Function<Material, TextureAtlasSprite> getModelTextureGetter() {
                return null;
            }

            public BakedModel bake(ResourceLocation location, ModelState state, Function<Material, TextureAtlasSprite> sprites) {
                return null;
            }

            @Override
            public UnbakedModel getModel(ResourceLocation resourceLocation) {
                return null;
            }

            @Nullable
            @Override
            public BakedModel bake(ResourceLocation resourceLocation, ModelState modelState) {
                return null;
            }
        }, missing, Collections.emptyList()) {

            @Override
            public BakedModel resolve(@NotNull BakedModel original, @NotNull ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed) {
                Book book = ItemModBook.getBook(stack);
                if (book != null) {
                    ModelResourceLocation modelPath = new ModelResourceLocation(book.model, "inventory");
                    return Minecraft.getInstance().getModelManager().getModel(modelPath);
                } else {
                    return original;
                }
            }
        };
    }

    @NotNull
    @Override
    public ItemOverrides getOverrides() {
        return this.itemHandler;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        return this.original.getQuads(state, side, rand);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.original.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.original.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.original.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return this.original.isCustomRenderer();
    }

    @NotNull
    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.original.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.original.getTransforms();
    }
}