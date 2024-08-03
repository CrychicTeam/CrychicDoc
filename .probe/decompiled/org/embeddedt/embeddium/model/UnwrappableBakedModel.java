package org.embeddedt.embeddium.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public interface UnwrappableBakedModel {

    @Nullable
    BakedModel embeddium$getInnerModel(RandomSource var1);

    static BakedModel unwrapIfPossible(BakedModel incoming, RandomSource rand) {
        if (incoming instanceof UnwrappableBakedModel) {
            BakedModel m = ((UnwrappableBakedModel) incoming).embeddium$getInnerModel(rand);
            if (m != null) {
                return m;
            }
        }
        return incoming;
    }
}