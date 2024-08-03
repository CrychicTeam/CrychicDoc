package snownee.jade.api.view;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.util.CommonProxy;

public class ItemView {

    public ItemStack item;

    @Nullable
    public String text;

    public ItemView(ItemStack item) {
        this(item, null);
    }

    public ItemView(ItemStack item, @Nullable String text) {
        this.item = item;
        this.text = text;
    }

    @Deprecated
    public static ViewGroup<ItemStack> fromContainer(Container container, int maxSize, int startIndex) {
        return compacted(IntStream.range(startIndex, container.getContainerSize()).limit((long) (maxSize * 3)).mapToObj(container::m_8020_), maxSize);
    }

    @Deprecated
    public static ViewGroup<ItemStack> compacted(Stream<ItemStack> stream, int maxSize) {
        List<ItemStack> stacks = Lists.newArrayList();
        MutableInt start = new MutableInt();
        stream.filter(stack -> !stack.isEmpty()).filter(stack -> {
            if (stack.hasTag() && stack.getTag().contains("CustomModelData")) {
                for (String key : stack.getTag().getAllKeys()) {
                    if (key.toLowerCase(Locale.ENGLISH).endsWith("clear") && stack.getTag().getBoolean(key)) {
                        return false;
                    }
                }
            }
            return true;
        }).forEach(stack -> {
            int size = stacks.size();
            if (size <= maxSize) {
                for (int i = 0; i < size; i++) {
                    int j = (i + start.intValue()) % size;
                    if (ItemStack.isSameItemSameTags(stack, (ItemStack) stacks.get(j))) {
                        ((ItemStack) stacks.get(j)).grow(stack.getCount());
                        start.setValue(j);
                        return;
                    }
                }
                stacks.add(stack.copy());
            }
        });
        if (stacks.size() > maxSize) {
            stacks.remove(maxSize);
        }
        return new ViewGroup<>(stacks);
    }

    public static List<ViewGroup<ItemStack>> groupOf(Container container, Accessor<?> accessor) {
        return CommonProxy.containerGroup(container, accessor);
    }

    public static List<ViewGroup<ItemStack>> groupOf(Object storage, Accessor<?> accessor) {
        return CommonProxy.storageGroup(storage, accessor);
    }
}