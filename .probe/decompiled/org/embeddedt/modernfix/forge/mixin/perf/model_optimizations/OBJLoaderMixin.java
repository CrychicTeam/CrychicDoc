package org.embeddedt.modernfix.forge.mixin.perf.model_optimizations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.obj.ObjLoader;
import net.minecraftforge.client.model.obj.ObjMaterialLibrary;
import net.minecraftforge.client.model.obj.ObjModel;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ObjLoader.class })
@ClientOnlyMixin
public class OBJLoaderMixin {

    @Final
    @Mutable
    @Shadow(remap = false)
    private Map<ResourceLocation, ObjMaterialLibrary> materialCache;

    @Final
    @Mutable
    @Shadow(remap = false)
    private Map<ObjModel.ModelSettings, ObjModel> modelCache;

    @Redirect(method = { "<init>" }, at = @At(value = "FIELD", opcode = 181, target = "Lnet/minecraftforge/client/model/obj/ObjLoader;materialCache:Ljava/util/Map;", remap = false))
    private void useConcMap1(ObjLoader instance, Map<ResourceLocation, ObjMaterialLibrary> value) {
        this.materialCache = new ConcurrentHashMap();
    }

    @Redirect(method = { "<init>" }, at = @At(value = "FIELD", opcode = 181, target = "Lnet/minecraftforge/client/model/obj/ObjLoader;modelCache:Ljava/util/Map;", remap = false))
    private void useConcMap2(ObjLoader instance, Map<ResourceLocation, ObjMaterialLibrary> value) {
        this.modelCache = new ConcurrentHashMap();
    }
}