package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.misc.ExpItem;
import dev.xkmc.l2artifacts.content.misc.RandomArtifactItem;
import dev.xkmc.l2artifacts.content.misc.RandomArtifactSetItem;
import dev.xkmc.l2artifacts.content.misc.SelectArtifactItem;
import dev.xkmc.l2artifacts.content.swap.ArtifactSwapItem;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.content.upgrades.UpgradeBoostItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class ArtifactItems {

    public static final String[] RANK_NAME = new String[] { " -Common-", " =Rare=", " >Epic<", " »Legendary«", " -»Godly«-" };

    public static final ItemEntry<SelectArtifactItem> SELECT = L2Artifacts.REGISTRATE.item("select", SelectArtifactItem::new).defaultModel().lang("Artifact Selector (Creative)").register();

    public static final ItemEntry<ArtifactChestItem> FILTER = L2Artifacts.REGISTRATE.item("filter", ArtifactChestItem::new).defaultModel().lang("Artifact Pocket").register();

    public static final ItemEntry<ArtifactChestItem> UPGRADED_POCKET = L2Artifacts.REGISTRATE.item("upgraded_pocket", ArtifactChestItem::new).defaultModel().lang("Upgraded Artifact Pocket").register();

    public static final ItemEntry<ArtifactSwapItem> SWAP = L2Artifacts.REGISTRATE.item("swap", ArtifactSwapItem::new).defaultModel().lang("Artifact Quick Swap").register();

    public static final ItemEntry<RandomArtifactItem>[] RANDOM;

    public static final ItemEntry<RandomArtifactSetItem>[] RANDOM_SET;

    public static final ItemEntry<ExpItem>[] ITEM_EXP;

    public static final ItemEntry<StatContainerItem>[] ITEM_STAT;

    public static final ItemEntry<UpgradeBoostItem>[] ITEM_BOOST_MAIN;

    public static final ItemEntry<UpgradeBoostItem>[] ITEM_BOOST_SUB;

    public static void register() {
    }

    static {
        L2Artifacts.REGISTRATE.buildL2CreativeTab("artifacts", "L2 Artifacts", b -> b.icon(SELECT::asStack));
        ITagManager<Item> manager = (ITagManager<Item>) Objects.requireNonNull(ForgeRegistries.ITEMS.tags());
        int n = 5;
        RANDOM = new ItemEntry[n];
        for (int i = 0; i < n; i++) {
            int r = i + 1;
            TagKey<Item> artifact = manager.createTagKey(new ResourceLocation("l2artifacts", "artifact"));
            TagKey<Item> rank_tag = manager.createTagKey(new ResourceLocation("l2artifacts", "rank_" + r));
            RANDOM[i] = L2Artifacts.REGISTRATE.item("random_" + r, p -> new RandomArtifactItem(p, r)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation("l2artifacts", "item/rank/" + r)).texture("layer1", new ResourceLocation("l2artifacts", "item/random"))).tag(new TagKey[] { rank_tag, artifact }).lang("Random Artifact" + RANK_NAME[i]).register();
        }
        RANDOM_SET = new ItemEntry[n];
        for (int i = 0; i < n; i++) {
            int r = i + 1;
            RANDOM_SET[i] = L2Artifacts.REGISTRATE.item("random_set_" + r, p -> new RandomArtifactSetItem(p, r)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation("l2artifacts", "item/rank/" + r)).texture("layer1", new ResourceLocation("l2artifacts", "item/random_set"))).lang("Random Artifact Set" + RANK_NAME[i]).register();
        }
        ITEM_EXP = new ItemEntry[n];
        for (int i = 0; i < n; i++) {
            int r = i + 1;
            ITEM_EXP[i] = L2Artifacts.REGISTRATE.item("artifact_experience_" + r, p -> new ExpItem(p, r)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation("l2artifacts", "item/rank/" + r)).texture("layer1", new ResourceLocation("l2artifacts", "item/artifact_experience"))).lang("Artifact Experience" + RANK_NAME[i]).register();
        }
        ITEM_STAT = new ItemEntry[n];
        for (int i = 0; i < n; i++) {
            int r = i + 1;
            ITEM_STAT[i] = L2Artifacts.REGISTRATE.item("stat_container_" + r, p -> new StatContainerItem(p, r)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation("l2artifacts", "item/rank/" + r)).texture("layer1", new ResourceLocation("l2artifacts", "item/stat_container"))).lang("Stat Container" + RANK_NAME[i]).register();
        }
        ITEM_BOOST_MAIN = new ItemEntry[n];
        for (int i = 0; i < n; i++) {
            int r = i + 1;
            ITEM_BOOST_MAIN[i] = L2Artifacts.REGISTRATE.item("boost_main_" + r, p -> new UpgradeBoostItem(p, r, Upgrade.Type.BOOST_MAIN_STAT)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation("l2artifacts", "item/rank/" + r)).texture("layer1", new ResourceLocation("l2artifacts", "item/boost_main"))).lang("Main Stat Booster" + RANK_NAME[i]).register();
        }
        ITEM_BOOST_SUB = new ItemEntry[n];
        for (int i = 0; i < n; i++) {
            int r = i + 1;
            ITEM_BOOST_SUB[i] = L2Artifacts.REGISTRATE.item("boost_sub_" + r, p -> new UpgradeBoostItem(p, r, Upgrade.Type.BOOST_SUB_STAT)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation("l2artifacts", "item/rank/" + r)).texture("layer1", new ResourceLocation("l2artifacts", "item/boost_sub"))).lang("Sub Stat Booster" + RANK_NAME[i]).register();
        }
        LAItem0.register();
        LAItem1.register();
        LAItem2.register();
        LAItem3.register();
        LAItem4.register();
        LAItem5.register();
        LAItem6.register();
        LAItemMisc.register();
    }
}