package dev.xkmc.l2artifacts.init.registrate;

import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2artifacts.content.search.augment.AugmentMenu;
import dev.xkmc.l2artifacts.content.search.augment.AugmentMenuScreen;
import dev.xkmc.l2artifacts.content.search.dissolve.DissolveMenu;
import dev.xkmc.l2artifacts.content.search.dissolve.DissolveMenuScreen;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredMenu;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredMenuScreen;
import dev.xkmc.l2artifacts.content.search.recycle.RecycleMenu;
import dev.xkmc.l2artifacts.content.search.recycle.RecycleMenuScreen;
import dev.xkmc.l2artifacts.content.search.shape.ShapeMenu;
import dev.xkmc.l2artifacts.content.search.shape.ShapeMenuScreen;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeMenu;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeMenuScreen;
import dev.xkmc.l2artifacts.content.swap.ArtifactSwapMenu;
import dev.xkmc.l2artifacts.content.swap.ArtifactSwapScreen;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;

public class ArtifactMenuRegistry {

    public static final MenuEntry<FilteredMenu> MT_FILTER = ((MenuBuilder) L2Artifacts.REGISTRATE.menu("filtered", FilteredMenu::fromNetwork, () -> FilteredMenuScreen::new).lang(ArtifactMenuRegistry::getLangKey, "Artifact Pocket")).register();

    public static final MenuEntry<RecycleMenu> MT_RECYCLE = ((MenuBuilder) L2Artifacts.REGISTRATE.menu("recycle", RecycleMenu::fromNetwork, () -> RecycleMenuScreen::new).lang(ArtifactMenuRegistry::getLangKey, "Artifact Exp Conversion")).register();

    public static final MenuEntry<UpgradeMenu> MT_UPGRADE = ((MenuBuilder) L2Artifacts.REGISTRATE.menu("upgrade", UpgradeMenu::fromNetwork, () -> UpgradeMenuScreen::new).lang(ArtifactMenuRegistry::getLangKey, "Artifact Upgrade")).register();

    public static final MenuEntry<DissolveMenu> MT_DISSOLVE = ((MenuBuilder) L2Artifacts.REGISTRATE.menu("dissolve", DissolveMenu::fromNetwork, () -> DissolveMenuScreen::new).lang(ArtifactMenuRegistry::getLangKey, "Artifact Dissolve")).register();

    public static final MenuEntry<AugmentMenu> MT_AUGMENT = ((MenuBuilder) L2Artifacts.REGISTRATE.menu("augment", AugmentMenu::fromNetwork, () -> AugmentMenuScreen::new).lang(ArtifactMenuRegistry::getLangKey, "Artifact Augment")).register();

    public static final MenuEntry<ShapeMenu> MT_SHAPE = ((MenuBuilder) L2Artifacts.REGISTRATE.menu("shape", ShapeMenu::fromNetwork, () -> ShapeMenuScreen::new).lang(ArtifactMenuRegistry::getLangKey, "Artifact Genesis")).register();

    public static final MenuEntry<ArtifactSwapMenu> MT_SWAP = ((MenuBuilder) L2Artifacts.REGISTRATE.menu("swap", ArtifactSwapMenu::fromNetwork, () -> ArtifactSwapScreen::new).lang(ArtifactMenuRegistry::getLangKey, "Artifact Quick Swap")).register();

    public static String getLangKey(MenuType<?> menu) {
        ResourceLocation rl = ForgeRegistries.MENU_TYPES.getKey(menu);
        assert rl != null;
        return "container." + rl.getNamespace() + "." + rl.getPath();
    }

    public static void register() {
    }
}