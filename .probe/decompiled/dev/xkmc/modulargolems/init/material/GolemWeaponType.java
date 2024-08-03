package dev.xkmc.modulargolems.init.material;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemWeaponItem;
import dev.xkmc.modulargolems.init.ModularGolems;
import java.util.Locale;
import java.util.function.BiFunction;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public enum GolemWeaponType {

    SPEAR("item/long_weapon", (p, i) -> new MetalGolemWeaponItem(p, i, 0.0, 2.0F, 0.0F), "TII", " SI", "S T"), AXE("item/battle_axe", (p, i) -> new MetalGolemWeaponItem(p, 0, (double) i.intValue() * 0.05, 0.0F, 2.0F), "III", "IS ", "TST"), SWORD("item/sword", (p, i) -> new MetalGolemWeaponItem(p, i, 0.0, 1.0F, 2.0F), "TII", "ISI", "SIT");

    private final BiFunction<Item.Properties, Integer, MetalGolemWeaponItem> factory;

    private final String[] pattern;

    private final String model;

    private GolemWeaponType(String model, BiFunction<Item.Properties, Integer, MetalGolemWeaponItem> factory, String... pattern) {
        this.model = model;
        this.factory = factory;
        this.pattern = pattern;
    }

    public String getName() {
        return "golem_" + this.name().toLowerCase(Locale.ROOT);
    }

    public ItemEntry<MetalGolemWeaponItem> buildItem(IGolemWeaponMaterial material) {
        return ModularGolems.REGISTRATE.item(material.getName() + "_" + this.getName(), p -> (MetalGolemWeaponItem) this.factory.apply(material.modify(p.stacksTo(1)), material.getDamage())).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile(pvd.modLoc(this.model))).texture("layer0", pvd.modLoc("item/equipments/" + ctx.getName()))).defaultLang().register();
    }

    public static ItemEntry<MetalGolemWeaponItem>[][] build(IGolemWeaponMaterial[] values) {
        ItemEntry<MetalGolemWeaponItem>[][] ans = new ItemEntry[values().length][values.length];
        for (int i = 0; i < values().length; i++) {
            GolemWeaponType type = values()[i];
            for (int j = 0; j < values.length; j++) {
                IGolemWeaponMaterial mat = values[j];
                ans[i][j] = type.buildItem(mat);
            }
        }
        return ans;
    }

    public ShapedRecipeBuilder pattern(ShapedRecipeBuilder unlock) {
        for (String str : this.pattern) {
            unlock.pattern(str);
        }
        return unlock;
    }
}