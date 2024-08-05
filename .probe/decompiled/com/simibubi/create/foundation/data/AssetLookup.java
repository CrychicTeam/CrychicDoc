package com.simibubi.create.foundation.data;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class AssetLookup {

    public static ModelFile partialBaseModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov, String... suffix) {
        String string = "/block";
        for (String suf : suffix) {
            string = string + "_" + suf;
        }
        String location = "block/" + ctx.getName() + string;
        return prov.models().getExistingFile(prov.modLoc(location));
    }

    public static ModelFile standardModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov) {
        return prov.models().getExistingFile(prov.modLoc("block/" + ctx.getName()));
    }

    public static <I extends BlockItem> ItemModelBuilder customItemModel(DataGenContext<Item, I> ctx, RegistrateItemModelProvider prov) {
        return prov.blockItem(() -> ((BlockItem) ctx.getEntry()).getBlock(), "/item");
    }

    public static <I extends BlockItem> NonNullBiConsumer<DataGenContext<Item, I>, RegistrateItemModelProvider> customBlockItemModel(String... folders) {
        return (c, p) -> {
            String path = "block";
            for (String string : folders) {
                path = path + "/" + ("_".equals(string) ? c.getName() : string);
            }
            p.withExistingParent(c.getName(), p.modLoc(path));
        };
    }

    public static <I extends Item> NonNullBiConsumer<DataGenContext<Item, I>, RegistrateItemModelProvider> customGenericItemModel(String... folders) {
        return (c, p) -> {
            String path = "block";
            for (String string : folders) {
                path = path + "/" + ("_".equals(string) ? c.getName() : string);
            }
            p.withExistingParent(c.getName(), p.modLoc(path));
        };
    }

    public static Function<BlockState, ModelFile> forPowered(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov) {
        return state -> state.m_61143_(BlockStateProperties.POWERED) ? partialBaseModel(ctx, prov, "powered") : partialBaseModel(ctx, prov);
    }

    public static Function<BlockState, ModelFile> forPowered(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov, String path) {
        return state -> prov.models().getExistingFile(prov.modLoc("block/" + path + (state.m_61143_(BlockStateProperties.POWERED) ? "_powered" : "")));
    }

    public static Function<BlockState, ModelFile> withIndicator(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov, Function<BlockState, ModelFile> baseModelFunc, IntegerProperty property) {
        return state -> {
            ResourceLocation baseModel = ((ModelFile) baseModelFunc.apply(state)).getLocation();
            Integer integer = (Integer) state.m_61143_(property);
            return prov.models().withExistingParent(ctx.getName() + "_" + integer, baseModel).texture("indicator", "block/indicator/" + integer);
        };
    }

    public static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> existingItemModel() {
        return (c, p) -> p.getExistingFile(p.modLoc("item/" + c.getName()));
    }

    public static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> itemModel(String name) {
        return (c, p) -> p.getExistingFile(p.modLoc("item/" + name));
    }

    public static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> itemModelWithPartials() {
        return (c, p) -> p.withExistingParent("item/" + c.getName(), p.modLoc("item/" + c.getName() + "/item"));
    }
}