package noppes.npcs.roles;

import java.util.EnumSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.api.entity.data.INPCJob;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class JobInterface implements INPCJob {

    public static final JobInterface NONE = new JobInterface(null) {

        @Override
        public CompoundTag save(CompoundTag compound) {
            return null;
        }

        @Override
        public void load(CompoundTag compound) {
        }

        @Override
        public int getType() {
            return 0;
        }
    };

    public EntityNPCInterface npc;

    public boolean overrideMainHand = false;

    public boolean overrideOffHand = false;

    public JobInterface(EntityNPCInterface npc) {
        this.npc = npc;
    }

    public abstract CompoundTag save(CompoundTag var1);

    public abstract void load(CompoundTag var1);

    public void killed() {
    }

    public void delete() {
    }

    public boolean aiShouldExecute() {
        return false;
    }

    public boolean aiContinueExecute() {
        return this.aiShouldExecute();
    }

    public void aiStartExecuting() {
    }

    public void aiUpdateTask() {
    }

    public void reset() {
    }

    public void stop() {
    }

    public IItemStack getMainhand() {
        return null;
    }

    public IItemStack getOffhand() {
        return null;
    }

    public boolean isFollowing() {
        return false;
    }

    public EnumSet<Goal.Flag> getFlags() {
        return EnumSet.noneOf(Goal.Flag.class);
    }

    public ItemStack stringToItem(String s) {
        return s.isEmpty() ? ItemStack.EMPTY : new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(s)));
    }

    public String itemToString(ItemStack item) {
        return item != null && !item.isEmpty() ? ForgeRegistries.ITEMS.getKey(item.getItem()).toString() : "";
    }
}