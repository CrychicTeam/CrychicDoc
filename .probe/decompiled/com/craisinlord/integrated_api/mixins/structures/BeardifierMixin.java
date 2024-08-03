package com.craisinlord.integrated_api.mixins.structures;

import com.craisinlord.integrated_api.world.terrainadaptation.beardifier.EnhancedBeardifierData;
import com.craisinlord.integrated_api.world.terrainadaptation.beardifier.EnhancedBeardifierHelper;
import com.craisinlord.integrated_api.world.terrainadaptation.beardifier.EnhancedBeardifierRigid;
import com.craisinlord.integrated_api.world.terrainadaptation.beardifier.EnhancedJigsawJunction;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { Beardifier.class }, priority = 100)
public class BeardifierMixin implements EnhancedBeardifierData {

    @Unique
    private ObjectListIterator<EnhancedJigsawJunction> enhancedJunctionIterator;

    @Unique
    private ObjectListIterator<EnhancedBeardifierRigid> enhancedRigidIterator;

    @Inject(method = { "forStructuresInChunk" }, at = { @At("RETURN") }, cancellable = true)
    private static void intapi_supportCustomTerrainAdaptations(StructureManager structureManager, ChunkPos chunkPos, CallbackInfoReturnable<Beardifier> cir) {
        Beardifier enhancedBeardifier = EnhancedBeardifierHelper.forStructuresInChunk(structureManager, chunkPos, (Beardifier) cir.getReturnValue());
        cir.setReturnValue(enhancedBeardifier);
    }

    @Inject(method = { "compute" }, at = { @At("RETURN") }, cancellable = true)
    public void intapi_calculateDensity(DensityFunction.FunctionContext ctx, CallbackInfoReturnable<Double> cir) {
        double density = (Double) cir.getReturnValue();
        double newDensity = EnhancedBeardifierHelper.computeDensity(ctx, density, this);
        cir.setReturnValue(newDensity);
    }

    @Override
    public ObjectListIterator<EnhancedBeardifierRigid> getEnhancedRigidIterator() {
        return this.enhancedRigidIterator;
    }

    @Override
    public void setEnhancedRigidIterator(ObjectListIterator<EnhancedBeardifierRigid> enhancedRigidIterator) {
        this.enhancedRigidIterator = enhancedRigidIterator;
    }

    @Override
    public ObjectListIterator<EnhancedJigsawJunction> getEnhancedJunctionIterator() {
        return this.enhancedJunctionIterator;
    }

    @Override
    public void setEnhancedJunctionIterator(ObjectListIterator<EnhancedJigsawJunction> enhancedJunctionIterator) {
        this.enhancedJunctionIterator = enhancedJunctionIterator;
    }
}