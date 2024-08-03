package snownee.lychee.core.input;

import com.google.common.collect.Lists;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.util.CommonProxy;

public abstract class ItemHolderCollection {

    public static final ItemHolderCollection EMPTY = ItemHolderCollection.InWorld.of();

    protected final ItemHolder[] holders;

    public final List<ItemStack> tempList = Lists.newArrayList();

    public final BitSet ignoreConsumptionFlags;

    public ItemHolderCollection(ItemHolder... holders) {
        this.holders = holders;
        this.ignoreConsumptionFlags = new BitSet(holders.length);
    }

    public ItemHolder get(int index) {
        return this.holders[index];
    }

    public ItemHolder split(int index, int amount) {
        ItemHolder holder = this.get(index).split(amount, this.tempList::add);
        this.holders[index] = holder;
        return holder;
    }

    public ItemHolder replace(int index, ItemStack item) {
        ItemHolder holder = this.get(index).replace(item, this.tempList::add);
        this.holders[index] = holder;
        return holder;
    }

    protected int consumeInputs(int times) {
        int total = 0;
        for (int i = 0; i < this.holders.length; i++) {
            ItemHolder holder = this.holders[i];
            if (!this.ignoreConsumptionFlags.get(i) && !holder.get().isEmpty()) {
                this.holders[i].get().shrink(times);
                total += times;
            }
        }
        return total;
    }

    public abstract int postApply(boolean var1, int var2);

    public int size() {
        return this.holders.length;
    }

    public static class InWorld extends ItemHolderCollection {

        private ItemEntity itemEntity;

        public InWorld(ItemHolder.InWorld... holders) {
            super(holders);
            if (holders.length > 0) {
                this.itemEntity = holders[0].getEntity();
            }
        }

        public static ItemHolderCollection of(ItemEntity... entities) {
            return new ItemHolderCollection.InWorld((ItemHolder.InWorld[]) Stream.of(entities).map(ItemHolder.InWorld::new).toArray(ItemHolder.InWorld[]::new));
        }

        @Override
        public int postApply(boolean consumeInputs, int times) {
            for (ItemStack stack : this.tempList) {
                if (!stack.isEmpty()) {
                    Vec3 pos = this.itemEntity.m_20182_();
                    ItemEntity newEntity = new ItemEntity(this.itemEntity.m_9236_(), pos.x, pos.y, pos.z, stack);
                    this.itemEntity.m_9236_().m_7967_(newEntity);
                }
            }
            return consumeInputs ? this.consumeInputs(times) : 0;
        }
    }

    public static class Inventory extends ItemHolderCollection {

        private LycheeContext ctx;

        public Inventory(LycheeContext ctx, ItemHolder.Simple... holders) {
            super(holders);
            this.ctx = ctx;
        }

        public static ItemHolderCollection of(LycheeContext ctx, ItemStack... items) {
            return new ItemHolderCollection.Inventory(ctx, (ItemHolder.Simple[]) Stream.of(items).map(ItemHolder.Simple::new).toArray(ItemHolder.Simple[]::new));
        }

        @Override
        public int postApply(boolean consumeInputs, int times) {
            Entity entity = this.ctx.getParamOrNull(LootContextParams.THIS_ENTITY);
            Player player = null;
            if (entity instanceof Player) {
                player = (Player) entity;
            }
            Vec3 pos = this.ctx.getParamOrNull(LootContextParams.ORIGIN);
            for (ItemStack stack : this.tempList) {
                if (player != null) {
                    if (!player.addItem(stack)) {
                        player.drop(stack, false);
                    }
                } else if (pos != null) {
                    CommonProxy.dropItemStack(this.ctx.getLevel(), pos.x, pos.y, pos.z, stack, null);
                }
            }
            return consumeInputs ? this.consumeInputs(times) : 0;
        }
    }
}