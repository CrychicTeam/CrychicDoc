package com.mna.entities.utility;

import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.spells.crafting.SpellRecipe;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class SpellFX extends Entity {

    private static final EntityDataAccessor<CompoundTag> SPELL_RECIPE = SynchedEntityData.defineId(SpellFX.class, EntityDataSerializers.COMPOUND_TAG);

    private static final EntityDataAccessor<Integer> SFX = SynchedEntityData.defineId(SpellFX.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<String> CASTER_UUID = SynchedEntityData.defineId(SpellFX.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<Integer> CASTER_ID = SynchedEntityData.defineId(SpellFX.class, EntityDataSerializers.INT);

    private SpellRecipe _cachedRecipe;

    private int age = 0;

    private boolean playedSound = false;

    public SpellFX(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void tick() {
        if (!this.playedSound) {
            this.getRecipe().iterateComponents(c -> {
                if (((SpellEffect) c.getPart()).SoundEffect() != null) {
                    this.m_9236_().playSound(this.getPlayer(this.m_9236_()), this.m_20183_(), ((SpellEffect) c.getPart()).SoundEffect(), SoundSource.PLAYERS, 0.15F, (float) (0.9F + Math.random() * 0.1F));
                }
            });
            this.playedSound = true;
        }
        if (this.m_9236_().isClientSide() && this.getRecipe() != null) {
            this.getRecipe().iterateComponents(c -> ((SpellEffect) c.getPart()).SpawnParticles(this.m_9236_(), this.m_20182_(), this.m_20184_(), this.age, this.getCaster(this.m_9236_()), this.getRecipe()));
        }
        this.age++;
        if (this.age >= 100) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(SFX, -1);
        this.f_19804_.define(CASTER_UUID, "");
        this.f_19804_.define(CASTER_ID, -1);
        this.f_19804_.define(SPELL_RECIPE, new CompoundTag());
    }

    public void setRecipe(ISpellDefinition recipe) {
        CompoundTag nbt = new CompoundTag();
        recipe.writeToNBT(nbt);
        this.f_19804_.set(SPELL_RECIPE, nbt);
    }

    @Nullable
    private SpellRecipe getRecipe() {
        if (this._cachedRecipe == null) {
            CompoundTag nbt = this.f_19804_.get(SPELL_RECIPE);
            this._cachedRecipe = SpellRecipe.fromNBT(nbt);
        }
        return this._cachedRecipe;
    }

    public void setCasterUUID(@Nullable LivingEntity caster) {
        if (caster != null) {
            if (caster instanceof Player && ((Player) caster).getGameProfile() != null) {
                this.f_19804_.set(CASTER_UUID, ((Player) caster).getGameProfile().getId().toString());
            }
            this.f_19804_.set(CASTER_ID, caster.m_19879_());
        }
    }

    @Nullable
    public LivingEntity getCaster(Level world) {
        int id = this.f_19804_.get(CASTER_ID);
        try {
            Entity e = world.getEntity(id);
            if (e instanceof LivingEntity) {
                return (LivingEntity) e;
            }
        } catch (Exception var4) {
        }
        return null;
    }

    @Nullable
    public Player getPlayer(Level world) {
        String s = this.f_19804_.get(CASTER_UUID);
        try {
            UUID uuid = UUID.fromString(s);
            return world.m_46003_(uuid);
        } catch (Exception var4) {
            return null;
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}