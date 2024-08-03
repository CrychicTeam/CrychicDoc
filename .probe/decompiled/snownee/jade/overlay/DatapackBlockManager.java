package snownee.jade.overlay;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.impl.BlockAccessorImpl;

public class DatapackBlockManager {

    private static final Set<BlockPos> itemFrames = Sets.newConcurrentHashSet();

    public static void onEntityJoin(Entity entity) {
        if (entity.getType() == EntityType.ITEM_FRAME || entity.getType() == EntityType.GLOW_ITEM_FRAME) {
            itemFrames.add(entity.blockPosition());
        }
    }

    public static void onEntityLeave(Entity entity) {
        if (entity.getType() == EntityType.ITEM_FRAME || entity.getType() == EntityType.GLOW_ITEM_FRAME) {
            BlockPos pos = entity.blockPosition();
            getFakeBlock(entity.level(), pos);
        }
    }

    public static ItemStack getFakeBlock(LevelAccessor level, BlockPos pos) {
        if (itemFrames.contains(pos)) {
            List<ItemFrame> entities = level.m_6443_(ItemFrame.class, new AABB(pos), $ -> $.m_20145_() && $.m_6084_());
            if (!entities.isEmpty()) {
                ItemStack stack = ((ItemFrame) entities.get(0)).getItem();
                if (stack.hasTag() && stack.getTag().contains("storedItem")) {
                    stack = ItemStack.of(stack.getTagElement("storedItem"));
                }
                return stack;
            }
            itemFrames.remove(pos);
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    public static Accessor<?> override(HitResult hitResult, @Nullable Accessor<?> accessor, @Nullable Accessor<?> originalAccessor) {
        if (accessor instanceof BlockAccessorImpl target && target.getFakeBlock().isEmpty()) {
            target.setFakeBlock(getFakeBlock(target.getLevel(), target.getPosition()));
        }
        return accessor;
    }
}