package org.embeddedt.modernfix.dynamicresources;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.embeddedt.modernfix.api.entrypoint.ModernFixClientIntegration;

public class ModelBakeryHelpers {

    public static final int MAX_BAKED_MODEL_COUNT = 10000;

    public static final int MAX_UNBAKED_MODEL_COUNT = 10000;

    public static final int MAX_MODEL_LIFETIME_SECS = 300;

    private static final Splitter COMMA_SPLITTER = Splitter.on(',');

    private static final Splitter EQUAL_SPLITTER = Splitter.on('=').limit(2);

    public static String[] getExtraTextureFolders() {
        return new String[] { "attachment", "bettergrass", "block", "blocks", "cape", "entity/bed", "entity/chest", "item", "items", "model", "models", "part", "pipe", "ropebridge", "runes", "solid_block", "spell_effect", "spell_projectile" };
    }

    private static <T extends Comparable<T>, V extends T> BlockState setPropertyGeneric(BlockState state, Property<T> prop, Object o) {
        return (BlockState) state.m_61124_(prop, (Comparable) o);
    }

    private static <T extends Comparable<T>> T getValueHelper(Property<T> property, String value) {
        return (T) property.getValue(value).orElse(null);
    }

    public static ImmutableList<BlockState> getBlockStatesForMRL(StateDefinition<Block, BlockState> stateDefinition, ModelResourceLocation location) {
        if (Objects.equals(location.getVariant(), "inventory")) {
            return ImmutableList.of();
        } else {
            Set<Property<?>> fixedProperties = new HashSet();
            BlockState fixedState = stateDefinition.any();
            for (String s : COMMA_SPLITTER.split(location.getVariant())) {
                Iterator<String> iterator = EQUAL_SPLITTER.split(s).iterator();
                if (iterator.hasNext()) {
                    String s1 = (String) iterator.next();
                    Property<?> property = stateDefinition.getProperty(s1);
                    if (property != null && iterator.hasNext()) {
                        String s2 = (String) iterator.next();
                        Object value = getValueHelper(property, s2);
                        if (value == null) {
                            throw new RuntimeException("Unknown value: '" + s2 + "' for blockstate property: '" + s1 + "' " + property.getPossibleValues());
                        }
                        fixedState = setPropertyGeneric(fixedState, property, value);
                        fixedProperties.add(property);
                    } else if (!s1.isEmpty()) {
                        throw new RuntimeException("Unknown blockstate property: '" + s1 + "'");
                    }
                }
            }
            if (fixedProperties.size() == stateDefinition.getProperties().size()) {
                return ImmutableList.of(fixedState);
            } else {
                ArrayList<Property<?>> anyProperties = new ArrayList(stateDefinition.getProperties());
                anyProperties.removeAll(fixedProperties);
                ArrayList<BlockState> finalList = new ArrayList();
                finalList.add(fixedState);
                for (Property<?> property : anyProperties) {
                    ArrayList<BlockState> newPermutations = new ArrayList();
                    for (BlockState state : finalList) {
                        for (Comparable<?> value : property.getPossibleValues()) {
                            newPermutations.add(setPropertyGeneric(state, property, value));
                        }
                    }
                    finalList = newPermutations;
                }
                return ImmutableList.copyOf(finalList);
            }
        }
    }

    public static ModernFixClientIntegration bakedModelWrapper(BiFunction<ResourceLocation, Pair<UnbakedModel, BakedModel>, BakedModel> consumer) {
        return new ModernFixClientIntegration() {

            @Override
            public BakedModel onBakedModelLoad(ResourceLocation location, UnbakedModel baseModel, BakedModel originalModel, ModelState state, ModelBakery bakery) {
                return (BakedModel) consumer.apply(location, Pair.of(baseModel, originalModel));
            }
        };
    }
}