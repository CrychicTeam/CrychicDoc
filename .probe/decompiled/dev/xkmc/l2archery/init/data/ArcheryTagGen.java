package dev.xkmc.l2archery.init.data;

import com.tterrag.registrate.providers.RegistrateTagsProvider.IntrinsicImpl;
import dev.xkmc.l2archery.init.registrate.ArcheryEffects;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import dev.xkmc.l2complements.init.data.TagGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class ArcheryTagGen {

    public static final String TAG_ENERGY = "Energy";

    public static final String ID_FORGE = "forge";

    public static final TagKey<Item> FORGE_BOWS = Tags.Items.TOOLS_BOWS;

    public static final TagKey<Item> FORGE_ARROWS = ItemTags.ARROWS;

    public static final TagKey<Item> PROF_BOWS = ItemTags.create(new ResourceLocation("l2archery", "bows"));

    public static final TagKey<Item> PROF_ARROWS = ItemTags.create(new ResourceLocation("l2archery", "arrows"));

    public static void onEntityTagGen(IntrinsicImpl<EntityType<?>> pvd) {
        pvd.addTag(EntityTypeTags.ARROWS).add((EntityType) ArcheryRegister.ET_ARROW.get());
    }

    public static void onEffectTagGen(IntrinsicImpl<MobEffect> pvd) {
        pvd.addTag(TagGen.SKILL_EFFECT).add((MobEffect) ArcheryEffects.QUICK_PULL.get(), (MobEffect) ArcheryEffects.RUN_BOW.get());
    }
}