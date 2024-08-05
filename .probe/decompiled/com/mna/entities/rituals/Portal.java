package com.mna.entities.rituals;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.items.ItemInit;
import com.mna.tools.TeleportHelper;
import com.mojang.datafixers.util.Pair;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.IForgeRegistry;

public class Portal extends Entity {

    private static final String KEY_AGE = "age";

    private static final String KEY_PERMANENT = "permanent";

    private static final String KEY_RTP = "rtp";

    private static final String KEY_COLOR = "color";

    private static final String KEY_FACTION_ID = "faction_id";

    private static final String KEY_FACTION_IDX = "faction_idx";

    private static final String KEY_TP_X = "teleport_pos_x";

    private static final String KEY_TP_Y = "teleport_pos_y";

    private static final String KEY_TP_Z = "teleport_pos_z";

    private static final String KEY_TP_DIM_KEYTYPE = "dimension_key_type";

    private static final String KEY_TP_DIM_KEY = "dimension_key";

    private static final EntityDataAccessor<BlockPos> TELEPORT_POS = SynchedEntityData.defineId(Portal.class, EntityDataSerializers.BLOCK_POS);

    private static final EntityDataAccessor<Integer> DYE_COLOR = SynchedEntityData.defineId(Portal.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> RTP = SynchedEntityData.defineId(Portal.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<String> FACTION_ID = SynchedEntityData.defineId(Portal.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<Byte> FACTION_IDX = SynchedEntityData.defineId(Portal.class, EntityDataSerializers.BYTE);

    private ResourceKey<Level> dimension;

    private int age = 0;

    private int maxAge = 200;

    private boolean permanent = false;

    private Pair<IFaction, Byte> _renderData = null;

    public Portal(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void tick() {
        this.age++;
        this.m_9236_().getEntities(this, this.m_20191_(), e -> e.isAlive() && e instanceof ItemEntity).stream().map(e -> (ItemEntity) e).forEach(e -> {
            ItemStack stack = e.getItem();
            if (stack.getItem() == ItemInit.RUNE_MARKING.get()) {
                if (this.m_9236_().isClientSide()) {
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.SPARKLE_GRAVITY.get()), e.m_20185_(), e.m_20186_(), e.m_20189_(), 0.0, 0.0, 0.0);
                } else {
                    this.maxAge = this.age + 20;
                    this.permanent = false;
                    e.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        });
        if (!this.m_9236_().isClientSide() && !this.permanent && this.getAge() >= this.maxAge) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public void baseTick() {
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(TELEPORT_POS, BlockPos.ZERO);
        this.f_19804_.define(DYE_COLOR, -1);
        this.f_19804_.define(RTP, false);
        this.f_19804_.define(FACTION_ID, "");
        this.f_19804_.define(FACTION_IDX, (byte) 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("age")) {
            this.age = compound.getInt("age");
        }
        if (compound.contains("teleport_pos_x") && compound.contains("teleport_pos_y") && compound.contains("teleport_pos_z") && compound.contains("dimension_key") && compound.contains("dimension_key_type")) {
            this.setTeleportBlockPos(new BlockPos(compound.getInt("teleport_pos_x"), compound.getInt("teleport_pos_y"), compound.getInt("teleport_pos_z")), ResourceKey.create(Registries.DIMENSION, new ResourceLocation(compound.getString("dimension_key_type"))));
        }
        if (compound.contains("permanent")) {
            this.permanent = compound.getBoolean("permanent");
        }
        if (compound.contains("rtp")) {
            this.f_19804_.set(RTP, compound.getBoolean("rtp"));
        }
        if (compound.contains("color")) {
            this.f_19804_.set(DYE_COLOR, compound.getInt("color"));
        }
        if (compound.contains("faction_id")) {
            this.f_19804_.set(FACTION_ID, compound.getString("faction_id"));
        }
        if (compound.contains("faction_idx")) {
            this.f_19804_.set(FACTION_IDX, compound.getByte("faction_idx"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("age", this.getAge());
        compound.putBoolean("permanent", this.permanent);
        compound.putBoolean("rtp", this.f_19804_.get(RTP));
        compound.putInt("color", this.f_19804_.get(DYE_COLOR));
        compound.putString("faction_id", this.f_19804_.get(FACTION_ID));
        compound.putByte("faction_idx", this.f_19804_.get(FACTION_IDX));
        compound.putInt("teleport_pos_x", this.getTeleportBlockPos().m_123341_());
        compound.putInt("teleport_pos_y", this.getTeleportBlockPos().m_123342_());
        compound.putInt("teleport_pos_z", this.getTeleportBlockPos().m_123343_());
        if (this.dimension != null) {
            compound.putString("dimension_key", this.dimension.registry().toString());
            compound.putString("dimension_key_type", this.dimension.location().toString());
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    public void setTeleportBlockPos(BlockPos pos, ResourceKey<Level> dimension) {
        this.f_19804_.set(TELEPORT_POS, pos);
        this.dimension = dimension;
    }

    public BlockPos getTeleportBlockPos() {
        return this.f_19804_.get(TELEPORT_POS);
    }

    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    @Override
    public void playerTouch(Player entityIn) {
        if (this.getAge() >= 40) {
            if (!entityIn.m_6144_()) {
                LazyOptional<IPlayerMagic> magicContainer = entityIn.getCapability(PlayerMagicProvider.MAGIC);
                if (magicContainer.isPresent()) {
                    if (magicContainer.orElse(null).getPortalCooldown() <= 0) {
                        if (this.f_19804_.get(RTP)) {
                            if (!this.m_9236_().isClientSide() && !TeleportHelper.randomTeleport(entityIn, (float) GeneralConfigValues.RTPDistance, 10)) {
                                entityIn.m_213846_(Component.translatable("gui.mna.portal.rtp_failed"));
                            }
                            magicContainer.orElse(null).setPortalCooldown(100);
                        } else {
                            BlockPos tpPos = this.getTeleportBlockPos();
                            if (tpPos != BlockPos.ZERO) {
                                if (!entityIn.m_9236_().isClientSide()) {
                                    entityIn.m_20194_().getAllLevels().iterator().forEachRemaining(sw -> {
                                        if (this.dimension.toString().equals(sw.m_46472_().toString())) {
                                            this.dimension = sw.m_46472_();
                                        }
                                    });
                                    TeleportHelper.teleportEntity(entityIn, this.dimension, new Vec3((double) ((float) tpPos.m_123341_() + 0.5F), (double) ((float) tpPos.m_123342_() + 0.5F), (double) ((float) tpPos.m_123343_() + 0.5F)));
                                } else {
                                    for (int i = 0; i < 10; i++) {
                                        this.m_9236_().addParticle(new MAParticleType(ParticleInit.SPARKLE_RANDOM.get()), entityIn.m_20185_() - 1.0 + Math.random() * 2.0, entityIn.m_20186_() + Math.random() * 2.0, entityIn.m_20189_() - 1.0 + Math.random() * 2.0, 0.1F, 0.1F, 0.1F);
                                    }
                                }
                                magicContainer.orElse(null).setPortalCooldown(100);
                            }
                        }
                    }
                }
            }
        }
    }

    public int getAge() {
        return this.age;
    }

    public int getMaxAge() {
        return this.maxAge;
    }

    @Nullable
    public DyeColor getDyeColor() {
        int color = this.f_19804_.get(DYE_COLOR);
        return color == -1 ? null : DyeColor.byId(color);
    }

    public void setDyeColor(DyeColor color) {
        this.f_19804_.set(DYE_COLOR, color.getId());
    }

    public void setPermanent() {
        this.permanent = true;
    }

    public void setRTP() {
        this.f_19804_.set(RTP, true);
    }

    public boolean isRTP() {
        return this.f_19804_.get(RTP);
    }

    public Pair<IFaction, Byte> getRenderData() {
        if (this._renderData == null) {
            this._renderData = new Pair((IFaction) ((IForgeRegistry) com.mna.Registries.Factions.get()).getValue(new ResourceLocation(this.f_19804_.get(FACTION_ID))), this.f_19804_.get(FACTION_IDX));
        }
        return this._renderData;
    }

    public void setRenderData(IFaction faction, byte idx) {
        ResourceLocation key = faction == null ? null : ((IForgeRegistry) com.mna.Registries.Factions.get()).getKey(faction);
        this.f_19804_.set(FACTION_ID, key == null ? null : key.toString());
        this.f_19804_.set(FACTION_IDX, idx);
    }
}