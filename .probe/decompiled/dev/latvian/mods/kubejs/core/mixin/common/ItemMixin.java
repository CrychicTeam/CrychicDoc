package dev.latvian.mods.kubejs.core.mixin.common;

import dev.architectury.registry.fuel.FuelRegistry;
import dev.latvian.mods.kubejs.core.ItemKJS;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.ItemStackKey;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.mod.util.color.Color;
import dev.latvian.mods.rhino.util.RemapForJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@RemapPrefixForJS("kjs$")
@Mixin(value = { Item.class }, priority = 1001)
public abstract class ItemMixin implements ItemKJS {

    private ItemBuilder kjs$itemBuilder;

    private CompoundTag kjs$typeData;

    private Ingredient kjs$asIngredient;

    private ItemStackKey kjs$typeItemStackKey;

    private ResourceLocation kjs$id;

    private String kjs$idString;

    @Nullable
    @Override
    public ItemBuilder kjs$getItemBuilder() {
        return this.kjs$itemBuilder;
    }

    @RemapForJS("getItem")
    @Override
    public Item kjs$self() {
        return (Item) this;
    }

    @Override
    public ResourceLocation kjs$getIdLocation() {
        if (this.kjs$id == null) {
            ResourceLocation id = RegistryInfo.ITEM.getId(this.kjs$self());
            this.kjs$id = id == null ? UtilsJS.UNKNOWN_ID : id;
        }
        return this.kjs$id;
    }

    @Override
    public String kjs$getId() {
        if (this.kjs$idString == null) {
            this.kjs$idString = this.kjs$getIdLocation().toString();
        }
        return this.kjs$idString;
    }

    @Override
    public void kjs$setItemBuilder(ItemBuilder b) {
        this.kjs$itemBuilder = b;
    }

    @Override
    public CompoundTag kjs$getTypeData() {
        if (this.kjs$typeData == null) {
            this.kjs$typeData = new CompoundTag();
        }
        return this.kjs$typeData;
    }

    @Accessor("maxStackSize")
    @Mutable
    @Override
    public abstract void kjs$setMaxStackSize(int var1);

    @Accessor("maxDamage")
    @Mutable
    @Override
    public abstract void kjs$setMaxDamage(int var1);

    @Accessor("craftingRemainingItem")
    @Mutable
    @Override
    public abstract void kjs$setCraftingRemainder(Item var1);

    @Accessor("isFireResistant")
    @Mutable
    @Override
    public abstract void kjs$setFireResistant(boolean var1);

    @Accessor("rarity")
    @Mutable
    @Override
    public abstract void kjs$setRarity(Rarity var1);

    @RemapForJS("setBurnTime")
    @Override
    public void kjs$setBurnTime(int i) {
        FuelRegistry.register(i, (Item) this);
    }

    @Accessor("foodProperties")
    @Mutable
    @Override
    public abstract void kjs$setFoodProperties(FoodProperties var1);

    @Inject(method = { "isFoil" }, at = { @At("HEAD") }, cancellable = true)
    private void isFoilKJS(ItemStack itemStack, CallbackInfoReturnable<Boolean> ci) {
        if (this.kjs$itemBuilder != null && this.kjs$itemBuilder.glow) {
            ci.setReturnValue(true);
        }
    }

    @Inject(method = { "appendHoverText" }, at = { @At("RETURN") })
    private void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn, CallbackInfo ci) {
        if (this.kjs$itemBuilder != null && !this.kjs$itemBuilder.tooltip.isEmpty()) {
            tooltip.addAll(this.kjs$itemBuilder.tooltip);
        }
    }

    @Inject(method = { "isBarVisible" }, at = { @At("HEAD") }, cancellable = true)
    private void isBarVisible(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
        if (this.kjs$itemBuilder != null && this.kjs$itemBuilder.barWidth != null && this.kjs$itemBuilder.barWidth.applyAsInt(stack) <= 13) {
            ci.setReturnValue(true);
        }
    }

    @Inject(method = { "getBarWidth" }, at = { @At("HEAD") }, cancellable = true)
    private void getBarWidth(ItemStack stack, CallbackInfoReturnable<Integer> ci) {
        if (this.kjs$itemBuilder != null && this.kjs$itemBuilder.barWidth != null) {
            ci.setReturnValue(this.kjs$itemBuilder.barWidth.applyAsInt(stack));
        }
    }

    @Inject(method = { "getBarColor" }, at = { @At("HEAD") }, cancellable = true)
    private void getBarColor(ItemStack stack, CallbackInfoReturnable<Integer> ci) {
        if (this.kjs$itemBuilder != null && this.kjs$itemBuilder.barColor != null) {
            ci.setReturnValue(((Color) this.kjs$itemBuilder.barColor.apply(stack)).getRgbJS());
        }
    }

    @Inject(method = { "getUseDuration" }, at = { @At("HEAD") }, cancellable = true)
    private void getUseDuration(ItemStack itemStack, CallbackInfoReturnable<Integer> ci) {
        if (this.kjs$itemBuilder != null && this.kjs$itemBuilder.useDuration != null) {
            ci.setReturnValue(this.kjs$itemBuilder.useDuration.applyAsInt(itemStack));
        }
    }

    @Inject(method = { "getUseAnimation" }, at = { @At("HEAD") }, cancellable = true)
    private void getUseAnimation(ItemStack itemStack, CallbackInfoReturnable<UseAnim> ci) {
        if (this.kjs$itemBuilder != null && this.kjs$itemBuilder.anim != null) {
            ci.setReturnValue(this.kjs$itemBuilder.anim);
        }
    }

    @Inject(method = { "getName" }, at = { @At("HEAD") }, cancellable = true)
    private void getName(ItemStack itemStack, CallbackInfoReturnable<Component> ci) {
        if (this.kjs$itemBuilder != null && this.kjs$itemBuilder.nameGetter != null) {
            ci.setReturnValue(this.kjs$itemBuilder.nameGetter.apply(itemStack));
        }
    }

    @Inject(method = { "use" }, at = { @At("HEAD") }, cancellable = true)
    private void use(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> ci) {
        if (this.kjs$itemBuilder != null && this.kjs$itemBuilder.use != null) {
            ItemStack itemStack = player.m_21120_(interactionHand);
            if (this.kjs$itemBuilder.use.use(level, player, interactionHand)) {
                ci.setReturnValue(ItemUtils.startUsingInstantly(level, player, interactionHand));
            } else {
                ci.setReturnValue(InteractionResultHolder.fail(itemStack));
            }
        }
    }

    @Inject(method = { "finishUsingItem" }, at = { @At("HEAD") }, cancellable = true)
    private void finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> ci) {
        if (this.kjs$itemBuilder != null && this.kjs$itemBuilder.finishUsing != null) {
            ci.setReturnValue(this.kjs$itemBuilder.finishUsing.finishUsingItem(itemStack, level, livingEntity));
        }
    }

    @Inject(method = { "releaseUsing" }, at = { @At("HEAD") })
    private void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i, CallbackInfo ci) {
        if (this.kjs$itemBuilder != null && this.kjs$itemBuilder.releaseUsing != null) {
            this.kjs$itemBuilder.releaseUsing.releaseUsing(itemStack, level, livingEntity, i);
        }
    }

    @Inject(method = { "hurtEnemy" }, at = { @At("HEAD") }, cancellable = true)
    private void hurtEnemy(ItemStack itemStack, LivingEntity livingEntity, LivingEntity livingEntity2, CallbackInfoReturnable<Boolean> cir) {
        if (this.kjs$itemBuilder != null && this.kjs$itemBuilder.hurtEnemy != null) {
            cir.setReturnValue(this.kjs$itemBuilder.hurtEnemy.test(new ItemBuilder.HurtEnemyContext(itemStack, livingEntity, livingEntity2)));
        }
    }

    @Override
    public Ingredient kjs$asIngredient() {
        if (this.kjs$asIngredient == null) {
            ItemStack is = new ItemStack(this.kjs$self());
            this.kjs$asIngredient = is.isEmpty() ? Ingredient.EMPTY : Ingredient.of(Stream.of(is));
        }
        return this.kjs$asIngredient;
    }

    @Accessor("descriptionId")
    @Mutable
    @Override
    public abstract void kjs$setNameKey(String var1);

    @Override
    public ItemStackKey kjs$getTypeItemStackKey() {
        if (this.kjs$typeItemStackKey == null) {
            this.kjs$typeItemStackKey = new ItemStackKey(this.kjs$self(), null);
        }
        return this.kjs$typeItemStackKey;
    }
}