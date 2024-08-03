package net.minecraftforge.client.model.geometry;

import com.mojang.math.Transformation;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.Util;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BuiltInModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.ElementsModel;
import net.minecraftforge.client.model.ForgeFaceData;
import net.minecraftforge.client.model.IModelBuilder;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.SimpleModelState;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.joml.Vector3f;

public class UnbakedGeometryHelper {

    private static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();

    private static final FaceBakery FACE_BAKERY = new FaceBakery();

    private static final Pattern FILESYSTEM_PATH_TO_RESLOC = Pattern.compile("(?:.*[\\\\/]assets[\\\\/](?<namespace>[a-z_-]+)[\\\\/]textures[\\\\/])?(?<path>[a-z_\\\\/-]+)\\.png");

    public static Material resolveDirtyMaterial(@Nullable String tex, IGeometryBakingContext owner) {
        if (tex == null) {
            return new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation());
        } else if (tex.startsWith("#")) {
            return owner.getMaterial(tex);
        } else {
            Matcher match = FILESYSTEM_PATH_TO_RESLOC.matcher(tex);
            if (match.matches()) {
                String namespace = match.group("namespace");
                String path = match.group("path").replace("\\", "/");
                tex = namespace != null ? namespace + ":" + path : path;
            }
            return new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(tex));
        }
    }

    @Internal
    public static BakedModel bake(BlockModel blockModel, ModelBaker modelBaker, BlockModel owner, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation, boolean guiLight3d) {
        IUnbakedGeometry<?> customModel = blockModel.customData.getCustomGeometry();
        if (customModel != null) {
            return customModel.bake(blockModel.customData, modelBaker, spriteGetter, modelState, blockModel.getOverrides(modelBaker, owner, spriteGetter), modelLocation);
        } else if (blockModel.getRootModel() == ModelBakery.GENERATION_MARKER) {
            return ITEM_MODEL_GENERATOR.generateBlockModel(spriteGetter, blockModel).bake(modelBaker, blockModel, spriteGetter, modelState, modelLocation, guiLight3d);
        } else if (blockModel.getRootModel() == ModelBakery.BLOCK_ENTITY_MARKER) {
            TextureAtlasSprite particleSprite = (TextureAtlasSprite) spriteGetter.apply(blockModel.getMaterial("particle"));
            return new BuiltInModel(blockModel.getTransforms(), blockModel.getOverrides(modelBaker, owner, spriteGetter), particleSprite, blockModel.getGuiLight().lightLikeBlock());
        } else {
            ElementsModel elementsModel = new ElementsModel(blockModel.getElements());
            return elementsModel.bake(blockModel.customData, modelBaker, spriteGetter, modelState, blockModel.getOverrides(modelBaker, owner, spriteGetter), modelLocation);
        }
    }

    public static List<BlockElement> createUnbakedItemElements(int layerIndex, SpriteContents spriteContents) {
        return createUnbakedItemElements(layerIndex, spriteContents, null);
    }

    public static List<BlockElement> createUnbakedItemElements(int layerIndex, SpriteContents spriteContents, @Nullable ForgeFaceData faceData) {
        List<BlockElement> elements = ITEM_MODEL_GENERATOR.processFrames(layerIndex, "layer" + layerIndex, spriteContents);
        if (faceData != null) {
            elements.forEach(element -> element.setFaceData(faceData));
        }
        return elements;
    }

    public static List<BlockElement> createUnbakedItemMaskElements(int layerIndex, SpriteContents spriteContents) {
        return createUnbakedItemMaskElements(layerIndex, spriteContents, null);
    }

    public static List<BlockElement> createUnbakedItemMaskElements(int layerIndex, SpriteContents spriteContents, @Nullable ForgeFaceData faceData) {
        List<BlockElement> elements = createUnbakedItemElements(layerIndex, spriteContents, faceData);
        elements.remove(0);
        int width = spriteContents.width();
        int height = spriteContents.height();
        BitSet bits = new BitSet(width * height);
        spriteContents.getUniqueFrames().forEach(frame -> {
            for (int xx = 0; xx < width; xx++) {
                for (int yx = 0; yx < height; yx++) {
                    if (!spriteContents.isTransparent(frame, xx, yx)) {
                        bits.set(xx + yx * width);
                    }
                }
            }
        });
        for (int y = 0; y < height; y++) {
            int xStart = -1;
            for (int x = 0; x < width; x++) {
                boolean opaque = bits.get(x + y * width);
                if (opaque == (xStart == -1)) {
                    if (xStart == -1) {
                        xStart = x;
                    } else {
                        int yEnd;
                        label63: for (yEnd = y + 1; yEnd < height; yEnd++) {
                            for (int x2 = xStart; x2 <= x; x2++) {
                                if (!bits.get(x2 + yEnd * width)) {
                                    break label63;
                                }
                            }
                        }
                        for (int i = xStart; i < x; i++) {
                            for (int j = y; j < yEnd; j++) {
                                bits.clear(i + j * width);
                            }
                        }
                        elements.add(new BlockElement(new Vector3f((float) (16 * xStart) / (float) width, 16.0F - (float) (16 * yEnd) / (float) height, 7.5F), new Vector3f((float) (16 * x) / (float) width, 16.0F - (float) (16 * y) / (float) height, 8.5F), Util.make(new HashMap(), map -> {
                            for (Direction direction : Direction.values()) {
                                map.put(direction, new BlockElementFace(null, layerIndex, "layer" + layerIndex, new BlockFaceUV(null, 0)));
                            }
                        }), null, true));
                        xStart = -1;
                    }
                }
            }
        }
        return elements;
    }

    public static void bakeElements(IModelBuilder<?> builder, List<BlockElement> elements, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
        for (BlockElement element : elements) {
            element.faces.forEach((side, face) -> {
                TextureAtlasSprite sprite = (TextureAtlasSprite) spriteGetter.apply(new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(face.texture)));
                BakedQuad quad = bakeElementFace(element, face, sprite, side, modelState, modelLocation);
                if (face.cullForDirection == null) {
                    builder.addUnculledFace(quad);
                } else {
                    builder.addCulledFace(Direction.rotate(modelState.getRotation().getMatrix(), face.cullForDirection), quad);
                }
            });
        }
    }

    public static List<BakedQuad> bakeElements(List<BlockElement> elements, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
        if (elements.isEmpty()) {
            return List.of();
        } else {
            ArrayList<BakedQuad> list = new ArrayList();
            bakeElements(IModelBuilder.collecting(list), elements, spriteGetter, modelState, modelLocation);
            return list;
        }
    }

    public static BakedQuad bakeElementFace(BlockElement element, BlockElementFace face, TextureAtlasSprite sprite, Direction direction, ModelState state, ResourceLocation modelLocation) {
        return FACE_BAKERY.bakeQuad(element.from, element.to, face, sprite, direction, state, element.rotation, element.shade, modelLocation);
    }

    public static IQuadTransformer applyRootTransform(ModelState modelState, Transformation rootTransform) {
        Transformation transform = modelState.getRotation().applyOrigin(new Vector3f(0.5F, 0.5F, 0.5F));
        return QuadTransformers.applying(transform.compose(rootTransform).compose(transform.inverse()));
    }

    public static ModelState composeRootTransformIntoModelState(ModelState modelState, Transformation rootTransform) {
        rootTransform = rootTransform.applyOrigin(new Vector3f(-0.5F, -0.5F, -0.5F));
        return new SimpleModelState(modelState.getRotation().compose(rootTransform), modelState.isUvLocked());
    }
}