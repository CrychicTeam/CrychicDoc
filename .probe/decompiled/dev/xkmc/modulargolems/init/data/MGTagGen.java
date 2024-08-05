package dev.xkmc.modulargolems.init.data;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider.IntrinsicImpl;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;

public class MGTagGen {

    public static final TagKey<Item> SCULK_MATS = createItemTag("sculk_materials");

    public static final TagKey<Item> GOLEM_PARTS = createItemTag("parts");

    public static final TagKey<Item> GOLEM_HOLDERS = createItemTag("holders");

    public static final TagKey<Item> GOLEM_UPGRADES = createItemTag("upgrades");

    public static final TagKey<Item> BLUE_UPGRADES = createItemTag("blue_upgrades");

    public static final TagKey<Item> POTION_UPGRADES = createItemTag("potion_upgrades");

    public static final TagKey<Item> CONFIG_CARD = createItemTag("config_card");

    public static final TagKey<Item> SPECIAL_CRAFT = createItemTag("special_crafting_material");

    public static final TagKey<Item> GOLEM_INTERACT = createItemTag("golem_interact");

    public static final TagKey<Item> CURIO_SKIN = ItemTags.create(new ResourceLocation("curios", "golem_skin"));

    public static final TagKey<Item> CURIO_PATH = ItemTags.create(new ResourceLocation("curios", "golem_route"));

    public static final TagKey<Item> PLAYER_SKIN = createItemTag("player_skin");

    public static final TagKey<EntityType<?>> GOLEM_FRIENDLY = createEntityTag("friendly");

    public static final TagKey<Block> POTENTIAL_DST = createBlockTag("potential_destination");

    public static final List<Consumer<RegistrateItemTagsProvider>> OPTIONAL_ITEM = new ArrayList();

    public static final List<Consumer<RegistrateTagsProvider<Block>>> OPTIONAL_BLOCK = new ArrayList();

    public static final List<Consumer<RegistrateTagsProvider<MobEffect>>> OPTIONAL_EFF = new ArrayList();

    public static void onBlockTagGen(IntrinsicImpl<Block> pvd) {
        pvd.addTag(POTENTIAL_DST).addTag(BlockTags.SHULKER_BOXES).addTag(Tags.Blocks.CHESTS).addTag(Tags.Blocks.BARRELS);
        OPTIONAL_BLOCK.forEach(e -> e.accept(pvd));
    }

    public static void onEffTagGen(IntrinsicImpl<MobEffect> pvd) {
        OPTIONAL_EFF.forEach(e -> e.accept(pvd));
    }

    public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
        pvd.addTag(SCULK_MATS).add(Items.ECHO_SHARD);
        pvd.addTag(SPECIAL_CRAFT);
        pvd.addTag(GOLEM_INTERACT).addTag(CONFIG_CARD).addTag(GOLEM_HOLDERS);
        OPTIONAL_ITEM.forEach(e -> e.accept(pvd));
        pvd.addTag(BLUE_UPGRADES).add((Item) GolemItems.BELL.get(), (Item) GolemItems.ENDER_SIGHT.get(), (Item) GolemItems.FLOAT.get(), (Item) GolemItems.PICKUP.get(), (Item) GolemItems.PICKUP_MENDING.get(), (Item) GolemItems.PICKUP_NO_DESTROY.get(), (Item) GolemItems.RECYCLE.get(), (Item) GolemItems.SWIM.get(), (Item) GolemItems.FIRE_IMMUNE.get(), (Item) GolemItems.THUNDER_IMMUNE.get(), (Item) GolemItems.PLAYER_IMMUNE.get(), (Item) GolemItems.MOUNT_UPGRADE.get(), (Item) GolemItems.SIZE_UPGRADE.get());
        pvd.addTag(POTION_UPGRADES).add((Item) GolemItems.WEAK.get(), (Item) GolemItems.SLOW.get(), (Item) GolemItems.WITHER.get());
        pvd.addTag(PLAYER_SKIN).add(Items.ZOMBIE_HEAD, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL);
        IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item> skin = pvd.addTag(CURIO_SKIN);
        skin.addTag(PLAYER_SKIN).add(Items.PLAYER_HEAD, Items.PIGLIN_HEAD);
        if (ModList.get().isLoaded("touhou_little_maid")) {
            skin.m_176839_(new ResourceLocation("touhou_little_maid", "garage_kit"));
        }
    }

    public static void onEntityTagGen(IntrinsicImpl<EntityType<?>> pvd) {
        pvd.addTag(GOLEM_FRIENDLY).add(EntityType.PLAYER, EntityType.SNOW_GOLEM);
        pvd.addTag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add((EntityType) GolemTypes.ENTITY_GOLEM.get(), (EntityType) GolemTypes.ENTITY_HUMANOID.get(), (EntityType) GolemTypes.ENTITY_DOG.get());
    }

    private static TagKey<Item> createItemTag(String id) {
        return ItemTags.create(new ResourceLocation("modulargolems", id));
    }

    private static TagKey<EntityType<?>> createEntityTag(String id) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("modulargolems", id));
    }

    private static TagKey<Block> createBlockTag(String id) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation("modulargolems", id));
    }
}