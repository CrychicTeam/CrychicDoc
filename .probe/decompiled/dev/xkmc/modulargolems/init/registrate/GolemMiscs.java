package dev.xkmc.modulargolems.init.registrate;

import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.serial.recipe.AbstractShapedRecipe;
import dev.xkmc.modulargolems.content.menu.config.ToggleGolemConfigMenu;
import dev.xkmc.modulargolems.content.menu.config.ToggleGolemConfigScreen;
import dev.xkmc.modulargolems.content.menu.equipment.EquipmentsMenu;
import dev.xkmc.modulargolems.content.menu.equipment.EquipmentsScreen;
import dev.xkmc.modulargolems.content.menu.filter.ItemConfigMenu;
import dev.xkmc.modulargolems.content.menu.filter.ItemConfigScreen;
import dev.xkmc.modulargolems.content.menu.path.PathConfigMenu;
import dev.xkmc.modulargolems.content.menu.path.PathConfigScreen;
import dev.xkmc.modulargolems.content.menu.target.TargetConfigMenu;
import dev.xkmc.modulargolems.content.menu.target.TargetConfigScreen;
import dev.xkmc.modulargolems.content.recipe.GolemAssembleRecipe;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class GolemMiscs {

    public static final RegistryEntry<AbstractShapedRecipe.Serializer<GolemAssembleRecipe>> ASSEMBLE = reg("golem_assemble", () -> new AbstractShapedRecipe.Serializer<>(GolemAssembleRecipe::new));

    public static final MenuEntry<EquipmentsMenu> EQUIPMENTS = ModularGolems.REGISTRATE.menu("equipments", EquipmentsMenu::fromNetwork, () -> EquipmentsScreen::new).register();

    public static final MenuEntry<ToggleGolemConfigMenu> CONFIG_TOGGLE = ModularGolems.REGISTRATE.menu("config_toggle", ToggleGolemConfigMenu::fromNetwork, () -> ToggleGolemConfigScreen::new).register();

    public static final MenuEntry<ItemConfigMenu> CONFIG_PICKUP = ModularGolems.REGISTRATE.menu("config_pickup", ItemConfigMenu::fromNetwork, () -> ItemConfigScreen::new).register();

    public static final MenuEntry<TargetConfigMenu> CONFIG_TARGET = ModularGolems.REGISTRATE.menu("config_target", TargetConfigMenu::fromNetwork, () -> TargetConfigScreen::new).register();

    public static final MenuEntry<PathConfigMenu> CONFIG_PATH = ModularGolems.REGISTRATE.menu("config_path", PathConfigMenu::fromNetwork, () -> PathConfigScreen::new).register();

    private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
        return ModularGolems.REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
    }

    public static void register() {
    }
}