package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemOverrides {

    public static final ItemOverrides EMPTY = new ItemOverrides();

    public static final float NO_OVERRIDE = Float.NEGATIVE_INFINITY;

    private final ItemOverrides.BakedOverride[] overrides;

    private final ResourceLocation[] properties;

    private ItemOverrides() {
        this.overrides = new ItemOverrides.BakedOverride[0];
        this.properties = new ResourceLocation[0];
    }

    public ItemOverrides(ModelBaker modelBaker0, BlockModel blockModel1, List<ItemOverride> listItemOverride2) {
        this.properties = (ResourceLocation[]) listItemOverride2.stream().flatMap(ItemOverride::m_173449_).map(ItemOverride.Predicate::m_173459_).distinct().toArray(ResourceLocation[]::new);
        Object2IntMap<ResourceLocation> $$3 = new Object2IntOpenHashMap();
        for (int $$4 = 0; $$4 < this.properties.length; $$4++) {
            $$3.put(this.properties[$$4], $$4);
        }
        List<ItemOverrides.BakedOverride> $$5 = Lists.newArrayList();
        for (int $$6 = listItemOverride2.size() - 1; $$6 >= 0; $$6--) {
            ItemOverride $$7 = (ItemOverride) listItemOverride2.get($$6);
            BakedModel $$8 = this.bakeModel(modelBaker0, blockModel1, $$7);
            ItemOverrides.PropertyMatcher[] $$9 = (ItemOverrides.PropertyMatcher[]) $$7.getPredicates().map(p_173477_ -> {
                int $$2 = $$3.getInt(p_173477_.getProperty());
                return new ItemOverrides.PropertyMatcher($$2, p_173477_.getValue());
            }).toArray(ItemOverrides.PropertyMatcher[]::new);
            $$5.add(new ItemOverrides.BakedOverride($$9, $$8));
        }
        this.overrides = (ItemOverrides.BakedOverride[]) $$5.toArray(new ItemOverrides.BakedOverride[0]);
    }

    @Nullable
    private BakedModel bakeModel(ModelBaker modelBaker0, BlockModel blockModel1, ItemOverride itemOverride2) {
        UnbakedModel $$3 = modelBaker0.getModel(itemOverride2.getModel());
        return Objects.equals($$3, blockModel1) ? null : modelBaker0.bake(itemOverride2.getModel(), BlockModelRotation.X0_Y0);
    }

    @Nullable
    public BakedModel resolve(BakedModel bakedModel0, ItemStack itemStack1, @Nullable ClientLevel clientLevel2, @Nullable LivingEntity livingEntity3, int int4) {
        if (this.overrides.length != 0) {
            Item $$5 = itemStack1.getItem();
            int $$6 = this.properties.length;
            float[] $$7 = new float[$$6];
            for (int $$8 = 0; $$8 < $$6; $$8++) {
                ResourceLocation $$9 = this.properties[$$8];
                ItemPropertyFunction $$10 = ItemProperties.getProperty($$5, $$9);
                if ($$10 != null) {
                    $$7[$$8] = $$10.call(itemStack1, clientLevel2, livingEntity3, int4);
                } else {
                    $$7[$$8] = Float.NEGATIVE_INFINITY;
                }
            }
            for (ItemOverrides.BakedOverride $$11 : this.overrides) {
                if ($$11.test($$7)) {
                    BakedModel $$12 = $$11.model;
                    if ($$12 == null) {
                        return bakedModel0;
                    }
                    return $$12;
                }
            }
        }
        return bakedModel0;
    }

    static class BakedOverride {

        private final ItemOverrides.PropertyMatcher[] matchers;

        @Nullable
        final BakedModel model;

        BakedOverride(ItemOverrides.PropertyMatcher[] itemOverridesPropertyMatcher0, @Nullable BakedModel bakedModel1) {
            this.matchers = itemOverridesPropertyMatcher0;
            this.model = bakedModel1;
        }

        boolean test(float[] float0) {
            for (ItemOverrides.PropertyMatcher $$1 : this.matchers) {
                float $$2 = float0[$$1.index];
                if ($$2 < $$1.value) {
                    return false;
                }
            }
            return true;
        }
    }

    static class PropertyMatcher {

        public final int index;

        public final float value;

        PropertyMatcher(int int0, float float1) {
            this.index = int0;
            this.value = float1;
        }
    }
}