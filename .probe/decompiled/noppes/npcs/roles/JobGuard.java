package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.NBTTags;
import noppes.npcs.entity.EntityNPCInterface;

public class JobGuard extends JobInterface {

    public List<String> targets = new ArrayList();

    public JobGuard(EntityNPCInterface npc) {
        super(npc);
    }

    public boolean isEntityApplicable(Entity entity) {
        return !(entity instanceof Player) && !(entity instanceof EntityNPCInterface) ? this.targets.contains(entity.getType().getDescriptionId()) : false;
    }

    @Override
    public CompoundTag save(CompoundTag nbttagcompound) {
        nbttagcompound.put("GuardTargets", NBTTags.nbtStringList(this.targets));
        return nbttagcompound;
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
        this.targets = NBTTags.getStringList(nbttagcompound.getList("GuardTargets", 10));
    }

    @Override
    public int getType() {
        return 3;
    }
}