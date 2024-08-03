package com.mna.items.manaweaving;

import com.mna.api.items.TieredItem;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.entities.manaweaving.ThrownManaweaveBottle;
import com.mna.items.renderers.ManaweaveBottleRenderer;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternSerializer;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;

public class ItemManaweaveBottle extends TieredItem {

    private static final String KEY_PATTERN_ID = "pattern";

    public ItemManaweaveBottle() {
        super(new Item.Properties());
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new ManaweaveBottleRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        });
    }

    public static boolean hasPattern(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("pattern");
    }

    @Nullable
    public static ManaweavingPattern getPattern(ItemStack stack) {
        if (!hasPattern(stack)) {
            return null;
        } else {
            String patternID = stack.getTag().getString("pattern");
            Optional<ManaweavingPattern> pattern = ManaweavingPatternSerializer.ALL_RECIPES.values().stream().filter(p -> p.m_6423_().toString().equals(patternID)).findFirst();
            return (ManaweavingPattern) pattern.orElse(null);
        }
    }

    public static void setPattern(IManaweavePattern pattern, ItemStack stack) {
        stack.getOrCreateTag().putString("pattern", pattern.getRegistryId().toString());
    }

    @Override
    public Component getName(ItemStack stack) {
        MutableComponent baseline = Component.translatable(this.m_5671_(stack));
        if (hasPattern(stack)) {
            String patternName = Component.translatable(stack.getTag().getString("pattern")).getString();
            baseline.append(Component.translatable("item.mna.manaweave_bottle.suffix", patternName));
        }
        return baseline;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        if (!worldIn.isClientSide) {
            ThrownManaweaveBottle potionentity = new ThrownManaweaveBottle(worldIn, playerIn);
            potionentity.m_37446_(itemstack);
            potionentity.m_37251_(playerIn, playerIn.m_146909_(), playerIn.m_146908_(), -20.0F, 0.5F, 1.0F);
            worldIn.m_7967_(potionentity);
        }
        if (!playerIn.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
    }

    public static void setRandomPattern(ItemStack is) {
        List<ManaweavingPattern> patterns = (List<ManaweavingPattern>) ManaweavingPatternSerializer.ALL_RECIPES.values().stream().collect(Collectors.toList());
        if (patterns.size() != 0) {
            setPattern((IManaweavePattern) patterns.get((int) ((double) patterns.size() * Math.random())), is);
        }
    }
}