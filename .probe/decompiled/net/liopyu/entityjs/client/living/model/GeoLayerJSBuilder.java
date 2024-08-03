package net.liopyu.entityjs.client.living.model;

import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import java.util.function.Function;
import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.liopyu.entityjs.client.living.KubeJSEntityRenderer;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class GeoLayerJSBuilder<T extends LivingEntity & IAnimatableJS> {

    public transient Function<T, Object> textureResource;

    public BaseLivingEntityBuilder<T> builder;

    public transient Consumer<ContextUtils.PreRenderContext<T>> render;

    public transient Consumer<ContextUtils.PreRenderContext<T>> preRender;

    public GeoLayerJSBuilder(BaseLivingEntityBuilder<T> builder) {
        this.builder = builder;
    }

    public GeoLayerJS<T> build(KubeJSEntityRenderer<T> entityRendererIn, BaseLivingEntityBuilder<T> builder) {
        return new GeoLayerJS<>(entityRendererIn, this, builder);
    }

    public BaseLivingEntityBuilder<T> getBuilder() {
        return this.builder;
    }

    @Info("Defines logic to preRender the newGeoLayer.\n\nExample usage:\n```javascript\ngeoBuilder.preRender(context => {\n    // Define logic to render the newGeoLayer\n    if (context.entity.isBaby()) {\n        context.poseStack.scale(0.5, 0.5, 0.5);\n    }\n});\n```\n")
    public GeoLayerJSBuilder<T> preRender(Consumer<ContextUtils.PreRenderContext<T>> preRender) {
        this.preRender = preRender;
        return this;
    }

    @Info("Defines logic to render the newGeoLayer.\nBy default this will render the flat texture set in textureResource\nonto the entity as an overlay. This method overrides the render method completely\nallowing scripters to define their own render logic.\n\nExample usage:\n```javascript\ngeoBuilder.render(context => {\n    // Define logic to render the newGeoLayer\n    if (context.entity.isBaby()) {\n        context.poseStack.scale(0.5, 0.5, 0.5);\n    }\n});\n```\n")
    public GeoLayerJSBuilder<T> render(Consumer<ContextUtils.PreRenderContext<T>> render) {
        this.render = render;
        return this;
    }

    @Info("Sets a function to determine the texture resource for the entity.\nThe provided Function accepts a parameter of type T (the entity),\nallowing changing the texture based on information about the entity.\nThe default behavior returns <namespace>:textures/entity/<path>.png.\n\nExample usage:\n```javascript\nentityBuilder.textureResource(entity => {\n    // Define logic to determine the texture resource for the entity\n    // Use information about the entity provided by the context.\n    return \"kubejs:textures/entity/wyrm.png\" // Some ResourceLocation representing the texture resource;\n});\n```\n")
    public GeoLayerJSBuilder<T> textureResource(Function<T, Object> function) {
        this.textureResource = entity -> {
            Object obj = function.apply(entity);
            if (obj instanceof String && !obj.toString().equals("undefined")) {
                return new ResourceLocation((String) obj);
            } else if (obj instanceof ResourceLocation) {
                return (ResourceLocation) obj;
            } else {
                EntityJSHelperClass.logWarningMessageOnce("Invalid return value for textureResource in newGeoLayer builder: " + obj + ". Defaulting to " + ((IAnimatableJS) entity).getBuilder().newID("textures/entity/", ".png"));
                return ((IAnimatableJS) entity).getBuilder().newID("textures/entity/", ".png");
            }
        };
        return this;
    }
}