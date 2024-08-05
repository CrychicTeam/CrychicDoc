package dev.xkmc.modulargolems.content.item.card;

import com.mojang.datafixers.util.Either;
import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import dev.xkmc.modulargolems.init.data.MGLangData;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.Nullable;

public class NameFilterCard extends TargetFilterCard {

    private static final String KEY = "filterList";

    private static List<String> getStrings(ItemStack stack) {
        List<String> ans = new ArrayList();
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("filterList")) {
            for (Tag e : tag.getList("filterList", 8)) {
                ans.add(e.getAsString());
            }
            return ans;
        } else {
            return ans;
        }
    }

    private static List<Either<EntityType<?>, TagKey<EntityType<?>>>> getList(List<String> strs) {
        List<Either<EntityType<?>, TagKey<EntityType<?>>>> ans = new ArrayList();
        for (String s : strs) {
            String str = s.trim();
            if (str.startsWith("#")) {
                ResourceLocation rl = getRL(str.substring(1));
                if (rl != null) {
                    TagKey<EntityType<?>> key = TagKey.create(Registries.ENTITY_TYPE, rl);
                    ITagManager<EntityType<?>> manager = ForgeRegistries.ENTITY_TYPES.tags();
                    if (manager != null && manager.isKnownTagName(key)) {
                        ans.add(Either.right(key));
                    }
                }
            } else {
                ResourceLocation rl = getRL(str);
                if (rl != null && ForgeRegistries.ENTITY_TYPES.containsKey(rl)) {
                    EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(rl);
                    if (type != null) {
                        ans.add(Either.left(type));
                    }
                }
            }
        }
        return ans;
    }

    @Nullable
    private static ResourceLocation getRL(String str) {
        try {
            return new ResourceLocation(str);
        } catch (Exception var2) {
            return null;
        }
    }

    public static void setList(ItemStack stack, List<Either<EntityType<?>, TagKey<EntityType<?>>>> list) {
        ListTag tag = ItemCompoundTag.of(stack).getSubList("filterList", 8).getOrCreate();
        tag.clear();
        for (Either<EntityType<?>, TagKey<EntityType<?>>> e : list) {
            ((Optional) e.map(l -> Optional.ofNullable(ForgeRegistries.ENTITY_TYPES.getKey(l)).map(ResourceLocation::toString), r -> Optional.of("#" + r.location()))).map(StringTag::m_129297_).ifPresent(tag::add);
        }
    }

    public NameFilterCard(Item.Properties properties) {
        super(properties);
    }

    public static ItemStack getFriendly() {
        ItemStack friendly = GolemItems.CARD_NAME.asStack();
        setList(friendly, List.of(Either.right(MGTagGen.GOLEM_FRIENDLY)));
        return friendly;
    }

    @Override
    public Predicate<LivingEntity> mayTarget(ItemStack stack) {
        List<Either<EntityType<?>, TagKey<EntityType<?>>>> list = getList(getStrings(stack));
        return e -> {
            for (Either<EntityType<?>, TagKey<EntityType<?>>> x : list) {
                if ((Boolean) x.map(l -> e.m_6095_() == l, r -> e.m_6095_().is(r))) {
                    return true;
                }
            }
            return false;
        };
    }

    @Override
    protected InteractionResultHolder<ItemStack> removeLast(Player player, ItemStack stack) {
        List<String> list = getStrings(stack);
        if (list.size() == 0) {
            return InteractionResultHolder.fail(stack);
        } else {
            if (!player.m_9236_().isClientSide()) {
                String e = (String) list.remove(list.size() - 1);
                setList(stack, getList(list));
                player.m_213846_(MGLangData.TARGET_MSG_REMOVED.get(Component.literal(e)));
            }
            return InteractionResultHolder.success(stack);
        }
    }

    @Override
    protected InteractionResultHolder<ItemStack> onUse(Player player, ItemStack stack) {
        List<String> strs = getStrings(stack);
        String name = stack.getHoverName().getString();
        if (strs.contains(name)) {
            return InteractionResultHolder.success(stack);
        } else {
            if (!player.m_9236_().isClientSide()) {
                strs.add(name);
                setList(stack, getList(strs));
                stack.setHoverName(null);
                player.m_213846_(MGLangData.TARGET_MSG_ADDED.get(Component.literal(name)));
            }
            return InteractionResultHolder.success(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        List<String> strs = getStrings(stack);
        if (strs.size() > 0 && !Screen.hasShiftDown()) {
            for (String e : strs) {
                list.add(Component.literal(e));
            }
            list.add(MGLangData.TARGET_SHIFT.get());
        } else {
            list.add(MGLangData.TARGET_NAME.get());
            list.add(MGLangData.TARGET_REMOVE.get());
        }
    }
}