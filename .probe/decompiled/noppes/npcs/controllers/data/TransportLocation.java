package noppes.npcs.controllers.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import noppes.npcs.api.entity.data.role.IRoleTransporter;

public class TransportLocation implements IRoleTransporter.ITransportLocation {

    public int id = -1;

    public String name = "default name";

    public BlockPos pos;

    public int type = 0;

    public ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, Level.OVERWORLD.location());

    public TransportCategory category;

    public void readNBT(CompoundTag compound) {
        if (compound != null) {
            this.id = compound.getInt("Id");
            this.pos = new BlockPos((int) compound.getDouble("PosX"), (int) compound.getDouble("PosY"), (int) compound.getDouble("PosZ"));
            this.type = compound.getInt("Type");
            this.dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(compound.getString("DimensionType")));
            this.name = compound.getString("Name");
        }
    }

    public CompoundTag writeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("Id", this.id);
        compound.putDouble("PosX", (double) this.pos.m_123341_());
        compound.putDouble("PosY", (double) this.pos.m_123342_());
        compound.putDouble("PosZ", (double) this.pos.m_123343_());
        compound.putInt("Type", this.type);
        compound.putString("DimensionType", this.dimension.location().toString());
        compound.putString("Name", this.name);
        return compound;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getDimension() {
        return this.dimension.location().toString();
    }

    @Override
    public int getX() {
        return this.pos.m_123341_();
    }

    @Override
    public int getY() {
        return this.pos.m_123342_();
    }

    @Override
    public int getZ() {
        return this.pos.m_123343_();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getType() {
        return this.type;
    }

    public boolean isDefault() {
        return this.type == 1;
    }
}