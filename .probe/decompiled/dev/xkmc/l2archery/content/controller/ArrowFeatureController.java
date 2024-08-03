package dev.xkmc.l2archery.content.controller;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.FeatureList;
import dev.xkmc.l2archery.content.feature.types.OnShootFeature;
import dev.xkmc.l2archery.content.item.ArrowData;
import dev.xkmc.l2archery.content.item.BowData;
import dev.xkmc.l2archery.content.item.GenericArrowItem;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2library.util.code.GenericItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;

public class ArrowFeatureController {

    public static boolean canBowUseArrow(GenericBowItem bow, GenericItemStack<GenericArrowItem> arrow) {
        return FeatureList.canMerge(bow.getFeatures(null), arrow.item().getFeatures());
    }

    @Nullable
    public static AbstractArrow createArrowEntity(ArrowFeatureController.BowArrowUseContext ctx, BowData bow, ArrowData arrow) {
        FeatureList features = FeatureList.merge(bow.getFeatures(), arrow.getFeatures());
        List<Consumer<GenericArrowEntity>> list = new ArrayList();
        for (OnShootFeature e : features.shot()) {
            if (!e.onShoot(ctx.user, list::add)) {
                return null;
            }
        }
        GenericArrowEntity ans = new GenericArrowEntity(ctx.level(), ctx.user(), new GenericArrowEntity.ArrowEntityData(bow, arrow, ctx.no_consume, ctx.power), features);
        list.forEach(ex -> ex.accept(ans));
        return ans;
    }

    public static record BowArrowUseContext(Level level, LivingEntity user, boolean no_consume, float power) {
    }
}