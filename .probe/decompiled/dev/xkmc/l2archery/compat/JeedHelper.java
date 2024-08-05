package dev.xkmc.l2archery.compat;

import com.google.gson.JsonObject;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2archery.content.config.BowArrowStatConfig;
import dev.xkmc.l2archery.content.enchantment.PotionArrowEnchantment;
import dev.xkmc.l2archery.content.item.GenericArrowItem;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.content.upgrade.Upgrade;
import dev.xkmc.l2archery.content.upgrade.UpgradeItem;
import dev.xkmc.l2archery.init.L2Archery;
import dev.xkmc.l2archery.init.registrate.ArcheryItems;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import dev.xkmc.l2library.serial.ingredients.EnchantmentIngredient;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.serialization.codec.JsonCodec;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import dev.xkmc.l2serial.util.Wrappers;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import net.mehvahdjukaar.jeed.recipes.EffectProviderRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class JeedHelper {

    public static RegistryEntry<RecipeSerializer<?>> REC = L2Archery.REGISTRATE.simple("jeed_archery", ForgeRegistries.Keys.RECIPE_SERIALIZERS, JeedHelper.Serializer::new);

    public static void register() {
    }

    public static record ArcheryJeedFinished(JeedHelper.JeedType jeedType, ResourceLocation entryName) {
    }

    @SerialClass
    public static class ArcheryJeedRecipe extends EffectProviderRecipe {

        @SerialField
        private JeedHelper.JeedType jeedType;

        @SerialField
        private ResourceLocation entryName;

        public ArcheryJeedRecipe(ResourceLocation id) {
            super(id, null, NonNullList.create());
        }

        public ArcheryJeedRecipe(ResourceLocation id, JeedHelper.JeedType type, ResourceLocation name) {
            this(id);
            this.jeedType = type;
            this.entryName = name;
        }

        public Collection<MobEffect> getEffects() {
            return (Collection<MobEffect>) this.jeedType.effect.apply(this.entryName);
        }

        public NonNullList<Ingredient> getIngredients() {
            Ingredient e = (Ingredient) this.jeedType.display.apply(this.entryName);
            return NonNullList.of(e, e);
        }

        public RecipeSerializer<?> getSerializer() {
            return (RecipeSerializer<?>) JeedHelper.REC.get();
        }
    }

    public static enum JeedType {

        BOW(id -> BowArrowStatConfig.get().getBowEffects((GenericBowItem) Wrappers.cast(ForgeRegistries.ITEMS.getValue(id))).instances().stream().map(MobEffectInstance::m_19544_).toList(), id -> Ingredient.of(ForgeRegistries.ITEMS.getValue(id))), ARROW(id -> BowArrowStatConfig.get().getArrowEffects((GenericArrowItem) Wrappers.cast(ForgeRegistries.ITEMS.getValue(id))).instances().stream().map(MobEffectInstance::m_19544_).toList(), id -> Ingredient.of(ForgeRegistries.ITEMS.getValue(id))), UPGRADE(id -> BowArrowStatConfig.get().getUpgradeEffects((Upgrade) Wrappers.cast(ArcheryRegister.UPGRADE.get().getValue(id))).instances().stream().map(MobEffectInstance::m_19544_).toList(), id -> Ingredient.of(UpgradeItem.setUpgrade(ArcheryItems.UPGRADE.asStack(), ArcheryRegister.UPGRADE.get().getValue(id)))), ENCHANTMENT(id -> BowArrowStatConfig.get().getEnchEffects((PotionArrowEnchantment) Wrappers.cast(ForgeRegistries.ENCHANTMENTS.getValue(id)), 1).instances().stream().map(MobEffectInstance::m_19544_).toList(), id -> new EnchantmentIngredient(ForgeRegistries.ENCHANTMENTS.getValue(id), 1));

        private final Function<ResourceLocation, List<MobEffect>> effect;

        private final Function<ResourceLocation, Ingredient> display;

        private JeedType(Function<ResourceLocation, List<MobEffect>> effect, Function<ResourceLocation, Ingredient> display) {
            this.effect = effect;
            this.display = display;
        }
    }

    public static class Serializer implements RecipeSerializer<JeedHelper.ArcheryJeedRecipe> {

        public JeedHelper.ArcheryJeedRecipe fromJson(ResourceLocation id, JsonObject json) {
            return (JeedHelper.ArcheryJeedRecipe) JsonCodec.from(json, JeedHelper.ArcheryJeedRecipe.class, new JeedHelper.ArcheryJeedRecipe(id));
        }

        @Nullable
        public JeedHelper.ArcheryJeedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return (JeedHelper.ArcheryJeedRecipe) PacketCodec.from(buf, JeedHelper.ArcheryJeedRecipe.class, new JeedHelper.ArcheryJeedRecipe(id));
        }

        public void toNetwork(FriendlyByteBuf buf, JeedHelper.ArcheryJeedRecipe r) {
            PacketCodec.to(buf, r);
        }
    }
}