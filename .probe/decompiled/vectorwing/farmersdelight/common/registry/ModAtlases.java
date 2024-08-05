package vectorwing.farmersdelight.common.registry;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class ModAtlases {

    public static final Material BLANK_CANVAS_SIGN_MATERIAL = new Material(Sheets.SIGN_SHEET, new ResourceLocation("farmersdelight", "entity/signs/canvas"));

    public static final Material BLANK_HANGING_CANVAS_SIGN_MATERIAL = new Material(Sheets.SIGN_SHEET, new ResourceLocation("farmersdelight", "entity/signs/hanging/canvas"));

    public static final Map<DyeColor, Material> DYED_CANVAS_SIGN_MATERIALS = (Map<DyeColor, Material>) Arrays.stream(DyeColor.values()).collect(Collectors.toMap(Function.identity(), ModAtlases::createCanvasSignMaterial));

    public static final Map<DyeColor, Material> DYED_HANGING_CANVAS_SIGN_MATERIALS = (Map<DyeColor, Material>) Arrays.stream(DyeColor.values()).collect(Collectors.toMap(Function.identity(), ModAtlases::createHangingCanvasSignMaterial));

    public static Material createCanvasSignMaterial(DyeColor dyeType) {
        return new Material(Sheets.SIGN_SHEET, new ResourceLocation("farmersdelight", "entity/signs/canvas_" + dyeType.getName()));
    }

    public static Material createHangingCanvasSignMaterial(DyeColor dyeType) {
        return new Material(Sheets.SIGN_SHEET, new ResourceLocation("farmersdelight", "entity/signs/hanging/canvas_" + dyeType.getName()));
    }

    public static Material getCanvasSignMaterial(@Nullable DyeColor dyeColor) {
        return dyeColor != null ? (Material) DYED_CANVAS_SIGN_MATERIALS.get(dyeColor) : BLANK_CANVAS_SIGN_MATERIAL;
    }

    public static Material getHangingCanvasSignMaterial(@Nullable DyeColor dyeColor) {
        return dyeColor != null ? (Material) DYED_HANGING_CANVAS_SIGN_MATERIALS.get(dyeColor) : BLANK_HANGING_CANVAS_SIGN_MATERIAL;
    }
}