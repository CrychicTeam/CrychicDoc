package dev.xkmc.l2weaponry.init.data;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider.IntrinsicImpl;
import dev.xkmc.l2weaponry.init.registrate.LWEntities;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

public class TagGen {

    public static final TagKey<Item> CLAW = ItemTags.create(new ResourceLocation("l2weaponry", "claw"));

    public static final TagKey<Item> DAGGER = ItemTags.create(new ResourceLocation("l2weaponry", "dagger"));

    public static final TagKey<Item> HAMMER = ItemTags.create(new ResourceLocation("l2weaponry", "hammer"));

    public static final TagKey<Item> BATTLE_AXE = ItemTags.create(new ResourceLocation("l2weaponry", "battle_axe"));

    public static final TagKey<Item> SPEAR = ItemTags.create(new ResourceLocation("l2weaponry", "spear"));

    public static final TagKey<Item> MACHETE = ItemTags.create(new ResourceLocation("l2weaponry", "machete"));

    public static final TagKey<Item> ROUND_SHIELD = ItemTags.create(new ResourceLocation("l2weaponry", "round_shield"));

    public static final TagKey<Item> PLATE_SHIELD = ItemTags.create(new ResourceLocation("l2weaponry", "plate_shield"));

    public static final TagKey<Item> THROWING_AXE = ItemTags.create(new ResourceLocation("l2weaponry", "throwing_axe"));

    public static final TagKey<Item> JAVELIN = ItemTags.create(new ResourceLocation("l2weaponry", "javelin"));

    public static final TagKey<Item> THROWABLE = ItemTags.create(new ResourceLocation("l2weaponry", "throwable"));

    public static final TagKey<Item> NUNCHAKU = ItemTags.create(new ResourceLocation("l2weaponry", "nunchaku"));

    private static final List<Pair<TagKey<Item>, ResourceLocation>> LIST = new ArrayList();

    public static void onBlockTagGen(RegistrateTagsProvider<Block> pvd) {
    }

    public static void onItemTagGen(RegistrateTagsProvider<Item> pvd) {
        pvd.addTag(Tags.Items.TOOLS_TRIDENTS).addTags(new TagKey[] { JAVELIN, THROWING_AXE });
        for (Pair<TagKey<Item>, ResourceLocation> e : LIST) {
            pvd.addTag((TagKey) e.getFirst()).addOptional((ResourceLocation) e.getSecond());
        }
        pvd.addTag(Tags.Items.TOOLS_SHIELDS).addTags(new TagKey[] { ROUND_SHIELD, PLATE_SHIELD });
        pvd.addTag(ItemTags.create(new ResourceLocation("skilltree", "melee_weapon"))).addTags(new TagKey[] { ItemTags.SWORDS, ItemTags.AXES, JAVELIN, THROWING_AXE, CLAW, DAGGER, HAMMER, BATTLE_AXE, SPEAR, MACHETE, NUNCHAKU });
    }

    public static void onEntityTagGen(IntrinsicImpl<EntityType<?>> pvd) {
        pvd.addTag(EntityTypeTags.IMPACT_PROJECTILES).add((EntityType) LWEntities.ET_AXE.get(), (EntityType) LWEntities.TE_JAVELIN.get());
    }

    public static void addItem(TagKey<Item> tag, ResourceLocation id) {
        LIST.add(Pair.of(tag, id));
    }
}