package noppes.npcs.roles;

import java.util.HashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.api.entity.data.INPCRole;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class RoleInterface implements INPCRole {

    public static final RoleInterface NONE = new RoleInterface(null) {

        @Override
        public CompoundTag save(CompoundTag compound) {
            return compound;
        }

        @Override
        public void load(CompoundTag compound) {
        }

        @Override
        public void interact(Player player) {
        }

        @Override
        public int getType() {
            return 0;
        }
    };

    public EntityNPCInterface npc;

    public HashMap<String, String> dataString = new HashMap();

    public RoleInterface(EntityNPCInterface npc) {
        this.npc = npc;
    }

    public abstract CompoundTag save(CompoundTag var1);

    public abstract void load(CompoundTag var1);

    public abstract void interact(Player var1);

    public void killed() {
    }

    public void delete() {
    }

    public boolean aiShouldExecute() {
        return false;
    }

    public boolean aiContinueExecute() {
        return false;
    }

    public void aiStartExecuting() {
    }

    public void aiUpdateTask() {
    }

    public boolean defendOwner() {
        return false;
    }

    public boolean isFollowing() {
        return false;
    }

    public void clientUpdate() {
    }
}