package icyllis.modernui.mc.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import icyllis.modernui.mc.ModernUIMod;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.BakedModelWrapper;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
final class ProjectBuilderModel extends BakedModelWrapper<BakedModel> {

    public final BakedModel main;

    public final BakedModel cube;

    ProjectBuilderModel(BakedModel originalModel, Map<ResourceLocation, BakedModel> models) {
        super(originalModel);
        this.main = bakeCustomModel(models, "item/project_builder_main");
        this.cube = bakeCustomModel(models, "item/project_builder_cube");
    }

    private static BakedModel bakeCustomModel(@Nonnull Map<ResourceLocation, BakedModel> models, String path) {
        return (BakedModel) models.get(ModernUIMod.location(path));
    }

    @Nonnull
    @Override
    public BakedModel applyTransform(@Nonnull ItemDisplayContext transformType, @Nonnull PoseStack poseStack, boolean applyLeftHandTransform) {
        super.applyTransform(transformType, poseStack, applyLeftHandTransform);
        return this;
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }
}