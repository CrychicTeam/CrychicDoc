package dev.ftb.mods.ftbquests.quest.loot;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;

public class EntityWeight {

    public int passive = 0;

    public int monster = 0;

    public int boss = 0;

    public int getWeight(Entity entity) {
        if (!entity.canChangeDimensions()) {
            return this.boss;
        } else {
            return entity instanceof Enemy ? this.monster : this.passive;
        }
    }

    public void writeData(CompoundTag nbt) {
        nbt.putInt("passive", this.passive);
        nbt.putInt("monster", this.monster);
        nbt.putInt("boss", this.boss);
    }

    public void readData(CompoundTag nbt) {
        this.passive = nbt.getInt("passive");
        this.monster = nbt.getInt("monster");
        this.boss = nbt.getInt("boss");
    }

    public void writeNetData(FriendlyByteBuf data) {
        data.writeVarInt(this.passive);
        data.writeVarInt(this.monster);
        data.writeVarInt(this.boss);
    }

    public void readNetData(FriendlyByteBuf data) {
        this.passive = data.readVarInt();
        this.monster = data.readVarInt();
        this.boss = data.readVarInt();
    }
}