package dev.xkmc.l2library.init.data;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider.IntrinsicImpl;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class L2TagGen {

    public static final ProviderType<IntrinsicImpl<Enchantment>> ENCH_TAGS = ProviderType.register("tags/enchantment", type -> (p, e) -> new IntrinsicImpl(p, type, "enchantments", e.getGenerator().getPackOutput(), Registries.ENCHANTMENT, e.getLookupProvider(), ench -> ResourceKey.create(ForgeRegistries.ENCHANTMENTS.getRegistryKey(), ForgeRegistries.ENCHANTMENTS.getKey(ench)), e.getExistingFileHelper()));

    public static final ProviderType<IntrinsicImpl<MobEffect>> EFF_TAGS = ProviderType.register("tags/mob_effect", type -> (p, e) -> new IntrinsicImpl(p, type, "mob_effects", e.getGenerator().getPackOutput(), Registries.MOB_EFFECT, e.getLookupProvider(), ench -> ResourceKey.create(ForgeRegistries.MOB_EFFECTS.getRegistryKey(), ForgeRegistries.MOB_EFFECTS.getKey(ench)), e.getExistingFileHelper()));

    public static final TagKey<MobEffect> TRACKED_EFFECTS = effectTag(new ResourceLocation("l2library", "tracked_effects"));

    public static void onEffectTagGen(IntrinsicImpl<MobEffect> pvd) {
        pvd.addTag(TRACKED_EFFECTS);
    }

    public static TagKey<MobEffect> effectTag(ResourceLocation id) {
        return TagKey.create(ForgeRegistries.MOB_EFFECTS.getRegistryKey(), id);
    }
}