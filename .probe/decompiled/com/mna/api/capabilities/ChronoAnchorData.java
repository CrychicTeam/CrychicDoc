package com.mna.api.capabilities;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ChronoAnchorData {

    private float health;

    private float mana;

    private int hunger;

    private BlockPos position;

    private ResourceKey<Level> dimension;

    private boolean isValid = false;

    public float getHealth() {
        return this.health;
    }

    public float getMana() {
        return this.mana;
    }

    public int getHunger() {
        return this.hunger;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    protected void setHealth(float health) {
        this.health = health;
    }

    protected void setMana(float mana) {
        this.mana = mana;
    }

    protected void setHunger(int hunger) {
        this.hunger = hunger;
    }

    protected void setPosition(BlockPos position, ResourceKey<Level> dimension) {
        this.position = position;
        this.dimension = dimension;
    }

    public boolean canRevert(Player player) {
        return this.dimension != null && this.dimension.compareTo(player.m_9236_().dimension()) == 0;
    }

    public void fromPlayer(Player player) {
        IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        this.setHealth(player.m_21223_());
        this.setMana(magic == null ? 0.0F : magic.getCastingResource().getAmount());
        this.setPosition(player.m_20183_(), player.m_9236_().dimension());
        this.setHunger(player.getFoodData().getFoodLevel());
        this.isValid = true;
    }

    public void revert(Player player) {
        if (this.isValid) {
            if (this.canRevert(player)) {
                IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                if (magic != null) {
                    magic.getCastingResource().setAmount(this.getMana());
                }
                player.m_21153_(this.getHealth());
                player.getFoodData().setFoodLevel(this.getHunger());
                player.m_6021_((double) this.getPosition().m_123341_(), (double) this.getPosition().m_123342_(), (double) this.getPosition().m_123343_());
                player.f_19789_ = 0.0F;
                this.isValid = false;
            }
        }
    }

    public void writeToNBT(CompoundTag compound) {
        if (this.isValid) {
            CompoundTag nbt = new CompoundTag();
            nbt.putFloat("health", this.getHealth());
            nbt.putFloat("mana", this.getMana());
            nbt.putInt("hunger", this.getHunger());
            nbt.putInt("x", this.getPosition().m_123341_());
            nbt.putInt("y", this.getPosition().m_123342_());
            nbt.putInt("z", this.getPosition().m_123343_());
            nbt.putString("dimension_key_value", this.dimension.location().toString());
            compound.put("chrono_anchor_data", nbt);
        }
    }

    public void readFromNBT(CompoundTag compound) {
        if (compound.contains("chrono_anchor_data")) {
            CompoundTag nbt = compound.getCompound("chrono_anchor_data");
            if (nbt.contains("health") && nbt.contains("mana") && nbt.contains("hunger") && nbt.contains("x") && nbt.contains("y") && nbt.contains("z") && nbt.contains("dimension_key_value")) {
                this.setHealth(nbt.getFloat("health"));
                this.setMana(nbt.getFloat("mana"));
                this.setHunger(nbt.getInt("hunger"));
                BlockPos position = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
                ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(nbt.getString("dimension_key_value")));
                this.setPosition(position, dimension);
                this.isValid = true;
            }
        }
    }
}