package dev.latvian.mods.kubejs.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.bindings.ItemWrapper;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.mod.util.color.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class ItemBuilder extends BuilderBase<Item> {

    public static final Map<String, Tier> TOOL_TIERS = new HashMap();

    public static final Map<String, ArmorMaterial> ARMOR_TIERS = new HashMap();

    public transient int maxStackSize = 64;

    public transient int maxDamage = 0;

    public transient int burnTime = 0;

    private ResourceLocation containerItem = null;

    public transient Function<ItemStack, Collection<ItemStack>> subtypes = null;

    public transient Rarity rarity = Rarity.COMMON;

    public transient boolean fireResistant;

    public transient boolean glow = false;

    public final transient List<Component> tooltip = new ArrayList();

    @Nullable
    public transient ItemTintFunction tint;

    public transient FoodBuilder foodBuilder;

    public transient Function<ItemStack, Color> barColor;

    public transient ToIntFunction<ItemStack> barWidth;

    public transient ItemBuilder.NameCallback nameGetter;

    public transient Multimap<ResourceLocation, AttributeModifier> attributes;

    public transient UseAnim anim;

    public transient ToIntFunction<ItemStack> useDuration;

    public transient ItemBuilder.UseCallback use;

    public transient ItemBuilder.FinishUsingCallback finishUsing;

    public transient ItemBuilder.ReleaseUsingCallback releaseUsing;

    public transient Predicate<ItemBuilder.HurtEnemyContext> hurtEnemy;

    public String texture;

    public String parentModel;

    public JsonObject textureJson = new JsonObject();

    public JsonObject modelJson;

    public static ArmorMaterial toArmorMaterial(Object o) {
        if (o instanceof ArmorMaterial) {
            return (ArmorMaterial) o;
        } else {
            String asString = String.valueOf(o);
            ArmorMaterial armorMaterial = (ArmorMaterial) ARMOR_TIERS.get(asString);
            if (armorMaterial != null) {
                return armorMaterial;
            } else {
                String withKube = KubeJS.appendModId(asString);
                return (ArmorMaterial) ARMOR_TIERS.getOrDefault(withKube, ArmorMaterials.IRON);
            }
        }
    }

    public static Tier toToolTier(Object o) {
        if (o instanceof Tier) {
            return (Tier) o;
        } else {
            String asString = String.valueOf(o);
            Tier toolTier = (Tier) TOOL_TIERS.get(asString);
            if (toolTier != null) {
                return toolTier;
            } else {
                String withKube = KubeJS.appendModId(asString);
                return (Tier) TOOL_TIERS.getOrDefault(withKube, Tiers.IRON);
            }
        }
    }

    public ItemBuilder(ResourceLocation i) {
        super(i);
        this.parentModel = "";
        this.foodBuilder = null;
        this.modelJson = null;
        this.attributes = ArrayListMultimap.create();
        this.anim = null;
        this.useDuration = null;
        this.use = null;
        this.finishUsing = null;
        this.releaseUsing = null;
        this.fireResistant = false;
        this.hurtEnemy = null;
    }

    @Override
    public final RegistryInfo getRegistryType() {
        return RegistryInfo.ITEM;
    }

    public Item transformObject(Item obj) {
        obj.kjs$setItemBuilder(this);
        return obj;
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (this.modelJson != null) {
            generator.json(AssetJsonGenerator.asItemModelLocation(this.id), this.modelJson);
        } else {
            generator.itemModel(this.id, m -> {
                if (!this.parentModel.isEmpty()) {
                    m.parent(this.parentModel);
                } else {
                    m.parent("minecraft:item/generated");
                }
                if (this.textureJson.size() == 0) {
                    this.texture(this.newID("item/", "").toString());
                }
                m.textures(this.textureJson);
            });
        }
    }

    @Info("Sets the item's max stack size. Default is 64.")
    public ItemBuilder maxStackSize(int v) {
        this.maxStackSize = v;
        return this;
    }

    @Info("Makes the item not stackable, equivalent to setting the item's max stack size to 1.")
    public ItemBuilder unstackable() {
        return this.maxStackSize(1);
    }

    @Info("Sets the item's max damage. Default is 0 (No durability).")
    public ItemBuilder maxDamage(int v) {
        this.maxDamage = v;
        return this;
    }

    @Info("Sets the item's burn time. Default is 0 (Not a fuel).")
    public ItemBuilder burnTime(int v) {
        this.burnTime = v;
        return this;
    }

    @Info("Sets the item's container item, e.g. a bucket for a milk bucket.")
    public ItemBuilder containerItem(ResourceLocation id) {
        this.containerItem = id;
        return this;
    }

    @Info("Adds subtypes to the item. The function should return a collection of item stacks, each with a different subtype.\n\nEach subtype will appear as a separate item in JEI and the creative inventory.\n")
    public ItemBuilder subtypes(Function<ItemStack, Collection<ItemStack>> fn) {
        this.subtypes = fn;
        return this;
    }

    @Info("Sets the item's rarity.")
    public ItemBuilder rarity(Rarity v) {
        this.rarity = v;
        return this;
    }

    @Info("Makes the item glow like enchanted, even if it's not enchanted.")
    public ItemBuilder glow(boolean v) {
        this.glow = v;
        return this;
    }

    @Info("Adds a tooltip to the item.")
    public ItemBuilder tooltip(Component text) {
        this.tooltip.add(text);
        return this;
    }

    @Deprecated
    public ItemBuilder group(@Nullable String g) {
        ConsoleJS.STARTUP.error("Item builder .group() is no longer supported, use StartupEvents.modifyCreativeTab!");
        return this;
    }

    @Info("Colorizes item's texture of the given index. Index is used when you have multiple layers, e.g. a crushed ore (of rock + ore).")
    public ItemBuilder color(int index, ItemTintFunction color) {
        if (!(this.tint instanceof ItemTintFunction.Mapped)) {
            this.tint = new ItemTintFunction.Mapped();
        }
        ((ItemTintFunction.Mapped) this.tint).map.put(index, color);
        return this;
    }

    @Info("Colorizes item's texture of the given index. Useful for coloring items, like GT ores ore dusts.")
    public ItemBuilder color(ItemTintFunction callback) {
        this.tint = callback;
        return this;
    }

    @Info("Sets the item's texture (layer0).")
    public ItemBuilder texture(String tex) {
        this.textureJson.addProperty("layer0", tex);
        return this;
    }

    @Info("Sets the item's texture by given key.")
    public ItemBuilder texture(String key, String tex) {
        this.textureJson.addProperty(key, tex);
        return this;
    }

    @Info("Directlys set the item's texture json.")
    public ItemBuilder textureJson(JsonObject json) {
        this.textureJson = json;
        return this;
    }

    @Info("Directly set the item's model json.")
    public ItemBuilder modelJson(JsonObject json) {
        this.modelJson = json;
        return this;
    }

    @Info("Sets the item's model (parent).")
    public ItemBuilder parentModel(String m) {
        this.parentModel = m;
        return this;
    }

    @Info("Determines the color of the item's durability bar. Defaulted to vanilla behavior.")
    public ItemBuilder barColor(Function<ItemStack, Color> barColor) {
        this.barColor = barColor;
        return this;
    }

    @Info("Determines the width of the item's durability bar. Defaulted to vanilla behavior.\n\nThe function should return a value between 0 and 13 (max width of the bar).\n")
    public ItemBuilder barWidth(ToIntFunction<ItemStack> barWidth) {
        this.barWidth = barWidth;
        return this;
    }

    @Info("Sets the item's name dynamically.\n")
    public ItemBuilder name(ItemBuilder.NameCallback name) {
        this.nameGetter = name;
        return this;
    }

    @Info("Set the food properties of the item.\n")
    public ItemBuilder food(Consumer<FoodBuilder> b) {
        this.foodBuilder = new FoodBuilder();
        b.accept(this.foodBuilder);
        return this;
    }

    @Info("Makes the item fire resistant like netherite tools (or not).")
    public ItemBuilder fireResistant(boolean isFireResistant) {
        this.fireResistant = isFireResistant;
        return this;
    }

    @Info("Makes the item fire resistant like netherite tools.")
    public ItemBuilder fireResistant() {
        return this.fireResistant(true);
    }

    public Item.Properties createItemProperties() {
        KubeJSItemProperties properties = new KubeJSItemProperties(this);
        if (this.maxDamage > 0) {
            properties.m_41503_(this.maxDamage);
        } else {
            properties.m_41487_(this.maxStackSize);
        }
        properties.m_41497_(this.rarity);
        Item item = this.containerItem == null ? Items.AIR : ItemWrapper.getItem(this.containerItem);
        if (item != Items.AIR) {
            properties.m_41495_(item);
        }
        if (this.foodBuilder != null) {
            properties.m_41489_(this.foodBuilder.build());
        }
        if (this.fireResistant) {
            properties.m_41486_();
        }
        return properties;
    }

    @Info(value = "Adds an attribute modifier to the item.\n\nAn attribute modifier is something like a damage boost or a speed boost.\nOn tools, they're applied when the item is held, on armor, they're\napplied when the item is worn.\n", params = { @Param(name = "attribute", value = "The resource location of the attribute, e.g. 'generic.attack_damage'"), @Param(name = "identifier", value = "A unique identifier for the modifier. Modifiers are considered the same if they have the same identifier."), @Param(name = "d", value = "The amount of the modifier."), @Param(name = "operation", value = "The operation to apply the modifier with. Can be ADDITION, MULTIPLY_BASE, or MULTIPLY_TOTAL.") })
    public ItemBuilder modifyAttribute(ResourceLocation attribute, String identifier, double d, AttributeModifier.Operation operation) {
        this.attributes.put(attribute, new AttributeModifier(new UUID((long) identifier.hashCode(), (long) identifier.hashCode()), identifier, d, operation));
        return this;
    }

    @Info("Determines the animation of the item when used, e.g. eating food.")
    public ItemBuilder useAnimation(UseAnim animation) {
        this.anim = animation;
        return this;
    }

    @Info("The duration when the item is used.\n\nFor example, when eating food, this is the time it takes to eat the food.\nThis can change the eating speed, or be used for other things (like making a custom bow).\n")
    public ItemBuilder useDuration(ToIntFunction<ItemStack> useDuration) {
        this.useDuration = useDuration;
        return this;
    }

    @Info("Determines if player will start using the item.\n\nFor example, when eating food, returning true will make the player start eating the food.\n")
    public ItemBuilder use(ItemBuilder.UseCallback use) {
        this.use = use;
        return this;
    }

    @Info("When players finish using the item.\n\nThis is called only when `useDuration` ticks have passed.\n\nFor example, when eating food, this is called when the player has finished eating the food, so hunger is restored.\n")
    public ItemBuilder finishUsing(ItemBuilder.FinishUsingCallback finishUsing) {
        this.finishUsing = finishUsing;
        return this;
    }

    @Info("When players did not finish using the item but released the right mouse button halfway through.\n\nAn example is the bow, where the arrow is shot when the player releases the right mouse button.\n\nTo ensure the bow won't finish using, Minecraft sets the `useDuration` to a very high number (1h).\n")
    public ItemBuilder releaseUsing(ItemBuilder.ReleaseUsingCallback releaseUsing) {
        this.releaseUsing = releaseUsing;
        return this;
    }

    @Info("Gets called when the item is used to hurt an entity.\n\nFor example, when using a sword to hit a mob, this is called.\n")
    public ItemBuilder hurtEnemy(Predicate<ItemBuilder.HurtEnemyContext> context) {
        this.hurtEnemy = context;
        return this;
    }

    static {
        for (Tiers tier : Tiers.values()) {
            TOOL_TIERS.put(tier.toString().toLowerCase(), tier);
        }
        for (ArmorMaterials tier : ArmorMaterials.values()) {
            ARMOR_TIERS.put(tier.toString().toLowerCase(), tier);
        }
    }

    @FunctionalInterface
    public interface FinishUsingCallback {

        ItemStack finishUsingItem(ItemStack var1, Level var2, LivingEntity var3);
    }

    public static record HurtEnemyContext(ItemStack getItem, LivingEntity getTarget, LivingEntity getAttacker) {
    }

    @FunctionalInterface
    public interface NameCallback {

        Component apply(ItemStack var1);
    }

    @FunctionalInterface
    public interface ReleaseUsingCallback {

        void releaseUsing(ItemStack var1, Level var2, LivingEntity var3, int var4);
    }

    @FunctionalInterface
    public interface UseCallback {

        boolean use(Level var1, Player var2, InteractionHand var3);
    }
}