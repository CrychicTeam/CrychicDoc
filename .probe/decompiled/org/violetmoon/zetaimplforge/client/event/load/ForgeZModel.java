package org.violetmoon.zetaimplforge.client.event.load;

import java.util.Map;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import org.violetmoon.zeta.client.event.load.ZModel;

public class ForgeZModel implements ZModel {

    public static class BakingCompleted extends ForgeZModel implements ZModel.BakingCompleted {

        private final ModelEvent.BakingCompleted e;

        public BakingCompleted(ModelEvent.BakingCompleted e) {
            this.e = e;
        }

        @Override
        public ModelManager getModelManager() {
            return this.e.getModelManager();
        }

        @Override
        public Map<ResourceLocation, BakedModel> getModels() {
            return this.e.getModels();
        }

        @Override
        public ModelBakery getModelBakery() {
            return this.e.getModelBakery();
        }
    }

    public static class ModifyBakingResult extends ForgeZModel implements ZModel.ModifyBakingResult {

        private final ModelEvent.ModifyBakingResult e;

        public ModifyBakingResult(ModelEvent.ModifyBakingResult e) {
            this.e = e;
        }

        @Override
        public Map<ResourceLocation, BakedModel> getModels() {
            return this.e.getModels();
        }

        @Override
        public ModelBakery getModelBakery() {
            return this.e.getModelBakery();
        }
    }

    public static class RegisterAdditional extends ForgeZModel implements ZModel.RegisterAdditional {

        private final ModelEvent.RegisterAdditional e;

        public RegisterAdditional(ModelEvent.RegisterAdditional e) {
            this.e = e;
        }

        @Override
        public void register(ResourceLocation model) {
            this.e.register(model);
        }
    }

    public static class RegisterGeometryLoaders extends ForgeZModel implements ZModel.RegisterGeometryLoaders {

        private final ModelEvent.RegisterGeometryLoaders e;

        public RegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders e) {
            this.e = e;
        }

        @Override
        public void register(String name, IGeometryLoader<?> loader) {
            this.e.register(name, loader);
        }
    }
}