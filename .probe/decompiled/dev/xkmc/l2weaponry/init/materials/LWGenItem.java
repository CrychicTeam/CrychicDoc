package dev.xkmc.l2weaponry.init.materials;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2weaponry.init.L2Weaponry;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import java.util.Locale;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.SeparateTransformsModelBuilder;

public class LWGenItem {

    public static ItemEntry<Item>[][] generate(ILWToolMats... values) {
        ItemEntry[][] ans = new ItemEntry[values.length][LWToolTypes.values().length];
        for (int j = 0; j < LWToolTypes.values().length; j++) {
            for (int i = 0; i < values.length; i++) {
                ILWToolMats mat = values[i];
                LWToolTypes type = LWToolTypes.values()[j];
                if (mat.hasTool(type)) {
                    String mat_name = mat.name().toLowerCase(Locale.ROOT);
                    String english = mat.englishName();
                    String tool_name = type.name().toLowerCase(Locale.ROOT);
                    ans[i][j] = L2Weaponry.REGISTRATE.item(mat_name + "_" + tool_name, p -> mat.type().getToolConfig().sup().get(mat.type(), type, mat.fireRes() ? p.fireResistant() : p)).optionalTag(mat.isOptional(), type.tag).model((ctx, pvd) -> model(type, mat, ctx, pvd, mat_name, tool_name, mat.is3D(type))).tab(LWItems.TAB.getKey(), e -> e.m_246342_(mat.getToolEnchanted(type))).lang(mat.prefix() + RegistrateLangProvider.toEnglishName(english + "_" + tool_name)).register();
                }
            }
        }
        return ans;
    }

    public static <T extends Item> void model(LWToolTypes type, ILWToolMats mat, DataGenContext<Item, T> ctx, RegistrateItemModelProvider pvd, String matName, String toolName, boolean is3D) {
        if (is3D) {
            if (matName.equals("legendary")) {
                String parent = "3d_legendary/" + toolName;
                ItemModelBuilder model = (ItemModelBuilder) pvd.getBuilder(ctx.getName());
                ResourceLocation icon = pvd.modLoc("item/generated/" + matName + "/" + toolName);
                model.guiLight(BlockModel.GuiLight.FRONT);
                ((SeparateTransformsModelBuilder) model.customLoader(SeparateTransformsModelBuilder::begin)).base(mat.model(type, new ItemModelBuilder(null, pvd.existingFileHelper).parent(pvd.getExistingFile(pvd.modLoc("item/" + parent))))).perspective(ItemDisplayContext.GUI, mat.model(type, new ItemModelBuilder(null, pvd.existingFileHelper).parent(pvd.getExistingFile(pvd.mcLoc("item/generated"))).texture("layer0", icon)));
            } else {
                ResourceLocation texture = pvd.modLoc("item/3d/" + toolName + "/" + matName);
                String parent = "3d_" + toolName;
                ItemModelBuilder model = (ItemModelBuilder) pvd.getBuilder(ctx.getName());
                if (type == LWToolTypes.JAVELIN) {
                    model.override().predicate(pvd.modLoc("throwing"), 1.0F).model(new ModelFile.UncheckedModelFile(pvd.modLoc("item/" + pvd.name(ctx) + "_throwing"))).end();
                    mat.model(type, ((ItemModelBuilder) pvd.withExistingParent(pvd.name(ctx) + "_throwing", pvd.modLoc("item/" + parent + "_throwing"))).texture("layer0", texture));
                }
                ResourceLocation icon = pvd.modLoc("item/generated/" + matName + "/" + toolName);
                model.guiLight(BlockModel.GuiLight.FRONT);
                ((SeparateTransformsModelBuilder) model.customLoader(SeparateTransformsModelBuilder::begin)).base(mat.model(type, new ItemModelBuilder(null, pvd.existingFileHelper).parent(pvd.getExistingFile(pvd.modLoc("item/" + parent))).texture("layer0", texture))).perspective(ItemDisplayContext.GUI, mat.model(type, new ItemModelBuilder(null, pvd.existingFileHelper).parent(pvd.getExistingFile(pvd.mcLoc("item/generated"))).texture("layer0", icon)));
            }
        } else {
            ResourceLocation texture = pvd.modLoc("item/generated/" + matName + "/" + toolName);
            if (type == LWToolTypes.ROUND_SHIELD) {
                String str = mat.emissive() ? "_emissive" : "";
                mat.model(type, ((ItemModelBuilder) pvd.withExistingParent(pvd.name(ctx), pvd.modLoc("item/round_shield" + str))).texture("0", texture)).override().predicate(pvd.modLoc("blocking"), 1.0F).model(new ModelFile.UncheckedModelFile(pvd.modLoc("item/" + pvd.name(ctx) + "_blocking"))).end();
                mat.model(type, ((ItemModelBuilder) pvd.withExistingParent(pvd.name(ctx) + "_blocking", pvd.modLoc("item/round_shield_blocking" + str))).texture("0", texture));
            } else if (type == LWToolTypes.PLATE_SHIELD) {
                String str = mat.emissive() ? "_emissive" : "";
                mat.model(type, ((ItemModelBuilder) pvd.withExistingParent(pvd.name(ctx), pvd.modLoc("item/plate_shield" + str))).texture("0", texture)).override().predicate(pvd.modLoc("blocking"), 1.0F).model(new ModelFile.UncheckedModelFile(pvd.modLoc("item/" + pvd.name(ctx) + "_blocking"))).end();
                mat.model(type, ((ItemModelBuilder) pvd.withExistingParent(pvd.name(ctx) + "_blocking", pvd.modLoc("item/plate_shield_blocking" + str))).texture("0", texture));
            } else if (type == LWToolTypes.THROWING_AXE) {
                mat.model(type, pvd.handheld(ctx, texture)).override().predicate(pvd.modLoc("throwing"), 1.0F).model(new ModelFile.UncheckedModelFile(pvd.modLoc("item/" + pvd.name(ctx) + "_throwing"))).end();
                mat.model(type, ((ItemModelBuilder) pvd.withExistingParent(pvd.name(ctx) + "_throwing", pvd.modLoc("item/handheld_throwing"))).texture("layer0", texture));
            } else if (type == LWToolTypes.JAVELIN) {
                mat.model(type, ((ItemModelBuilder) pvd.withExistingParent(pvd.name(ctx), pvd.modLoc("item/long_weapon"))).texture("layer0", texture)).override().predicate(pvd.modLoc("throwing"), 1.0F).model(new ModelFile.UncheckedModelFile(pvd.modLoc("item/" + pvd.name(ctx) + "_throwing"))).end();
                mat.model(type, ((ItemModelBuilder) pvd.withExistingParent(pvd.name(ctx) + "_throwing", pvd.modLoc("item/long_weapon_throwing"))).texture("layer0", texture));
            } else if (type == LWToolTypes.NUNCHAKU) {
                mat.model(type, ((ItemModelBuilder) pvd.withExistingParent(pvd.name(ctx), pvd.modLoc("item/nunchaku"))).texture("layer0", texture)).override().predicate(pvd.modLoc("spinning"), 1.0F).model(new ModelFile.UncheckedModelFile(pvd.modLoc("item/nunchaku_spinning"))).end();
                mat.model(type, ((ItemModelBuilder) pvd.withExistingParent(pvd.name(ctx) + "_roll", pvd.modLoc("item/nunchaku_roll"))).texture("layer0", pvd.modLoc("item/generated/" + matName + "/" + toolName + "_roll")));
                mat.model(type, ((ItemModelBuilder) pvd.withExistingParent(pvd.name(ctx) + "_unroll", pvd.modLoc("item/nunchaku_unroll"))).texture("layer0", pvd.modLoc("item/generated/" + matName + "/" + toolName + "_unroll")));
            } else if (type.customModel() != null) {
                mat.model(type, ((ItemModelBuilder) pvd.withExistingParent(pvd.name(ctx), pvd.modLoc("item/" + type.customModel()))).texture("layer0", texture));
            } else {
                mat.model(type, pvd.handheld(ctx, texture));
            }
        }
    }
}