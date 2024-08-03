package net.minecraftforge.client.model.geometry;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.NamedRenderTypeManager;
import net.minecraftforge.client.RenderTypeGroup;
import org.jetbrains.annotations.Nullable;

public interface IGeometryBakingContext {

    String getModelName();

    boolean hasMaterial(String var1);

    Material getMaterial(String var1);

    boolean isGui3d();

    boolean useBlockLight();

    boolean useAmbientOcclusion();

    ItemTransforms getTransforms();

    Transformation getRootTransform();

    @Nullable
    ResourceLocation getRenderTypeHint();

    boolean isComponentVisible(String var1, boolean var2);

    default RenderTypeGroup getRenderType(ResourceLocation name) {
        return NamedRenderTypeManager.get(name);
    }
}