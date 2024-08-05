package noppes.npcs.api.wrapper;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.EventHooks;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IDimension;
import noppes.npcs.api.INbt;
import noppes.npcs.api.IPos;
import noppes.npcs.api.IScoreboard;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.EntityProjectile;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketPlaySound;

public class WorldWrapper implements IWorld {

    public static Map<String, Object> tempData = new HashMap();

    public ServerLevel level;

    public IDimension dimension;

    private IData tempdata = new IData() {

        @Override
        public void put(String key, Object value) {
            WorldWrapper.tempData.put(key, value);
        }

        @Override
        public Object get(String key) {
            return WorldWrapper.tempData.get(key);
        }

        @Override
        public void remove(String key) {
            WorldWrapper.tempData.remove(key);
        }

        @Override
        public boolean has(String key) {
            return WorldWrapper.tempData.containsKey(key);
        }

        @Override
        public void clear() {
            WorldWrapper.tempData.clear();
        }

        @Override
        public String[] getKeys() {
            return (String[]) WorldWrapper.tempData.keySet().toArray(new String[WorldWrapper.tempData.size()]);
        }
    };

    private IData storeddata = new IData() {

        @Override
        public void put(String key, Object value) {
            CompoundTag compound = ScriptController.Instance.compound;
            if (value instanceof Number) {
                compound.putDouble(key, ((Number) value).doubleValue());
            } else if (value instanceof String) {
                compound.putString(key, (String) value);
            }
            ScriptController.Instance.shouldSave = true;
        }

        @Override
        public Object get(String key) {
            CompoundTag compound = ScriptController.Instance.compound;
            if (!compound.contains(key)) {
                return null;
            } else {
                Tag base = compound.get(key);
                return base instanceof NumericTag ? ((NumericTag) base).getAsDouble() : base.getAsString();
            }
        }

        @Override
        public void remove(String key) {
            ScriptController.Instance.compound.remove(key);
            ScriptController.Instance.shouldSave = true;
        }

        @Override
        public boolean has(String key) {
            return ScriptController.Instance.compound.contains(key);
        }

        @Override
        public void clear() {
            ScriptController.Instance.compound = new CompoundTag();
            ScriptController.Instance.shouldSave = true;
        }

        @Override
        public String[] getKeys() {
            return (String[]) ScriptController.Instance.compound.getAllKeys().toArray(new String[ScriptController.Instance.compound.getAllKeys().size()]);
        }
    };

    private WorldWrapper(Level level) {
        this.level = (ServerLevel) level;
        this.dimension = new DimensionWrapper(level.dimension().location(), level.dimensionType());
    }

    @Override
    public ServerLevel getMCLevel() {
        return this.level;
    }

    @Override
    public IEntity[] getNearbyEntities(int x, int y, int z, int range, int type) {
        return this.getNearbyEntities(new BlockPosWrapper(new BlockPos(x, y, z)), range, type);
    }

    @Override
    public IEntity[] getNearbyEntities(IPos pos, int range, int type) {
        AABB bb = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).move(pos.getMCBlockPos()).inflate((double) range, (double) range, (double) range);
        List<Entity> entities = this.level.m_45976_(this.getClassForType(type), bb);
        List<IEntity> list = new ArrayList();
        for (Entity living : entities) {
            list.add(NpcAPI.Instance().getIEntity(living));
        }
        return (IEntity[]) list.toArray(new IEntity[list.size()]);
    }

    @Override
    public IEntity[] getAllEntities(int type) {
        List<Entity> entities = this.getEntities(this.getClassForType(type), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
        List<IEntity> list = new ArrayList();
        for (Entity living : entities) {
            list.add(NpcAPI.Instance().getIEntity(living));
        }
        return (IEntity[]) list.toArray(new IEntity[list.size()]);
    }

    public List<Entity> getEntities(Class<?> entityTypeIn, Predicate<? super Entity> predicateIn) {
        List<Entity> list = Lists.newArrayList();
        ServerChunkCache serverchunkprovider = this.level.getChunkSource();
        for (Entity entity : this.level.getEntities().getAll()) {
            if (entityTypeIn.isAssignableFrom(entity.getClass()) && serverchunkprovider.hasChunk(Mth.floor(entity.getX()) >> 4, Mth.floor(entity.getZ()) >> 4) && predicateIn.test(entity)) {
                list.add(entity);
            }
        }
        return list;
    }

    @Override
    public IEntity getClosestEntity(int x, int y, int z, int range, int type) {
        return this.getClosestEntity(new BlockPosWrapper(new BlockPos(x, y, z)), range, type);
    }

    @Override
    public IEntity getClosestEntity(IPos pos, int range, int type) {
        AABB bb = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).move(pos.getMCBlockPos()).inflate((double) range, (double) range, (double) range);
        List<Entity> entities = this.level.m_45976_(this.getClassForType(type), bb);
        double distance = (double) (range * range * range);
        Entity entity = null;
        for (Entity e : entities) {
            double r = pos.getMCBlockPos().m_123331_(e.blockPosition());
            if (entity == null) {
                distance = r;
                entity = e;
            } else if (r < distance) {
                distance = r;
                entity = e;
            }
        }
        return NpcAPI.Instance().getIEntity(entity);
    }

    @Override
    public IEntity getEntity(String uuid) {
        try {
            UUID id = UUID.fromString(uuid);
            Entity e = this.level.getEntity(id);
            if (e == null) {
                e = this.level.m_46003_(id);
            }
            return e == null ? null : NpcAPI.Instance().getIEntity(e);
        } catch (Exception var4) {
            throw new CustomNPCsException("Given uuid was invalid " + uuid);
        }
    }

    @Override
    public IEntity createEntityFromNBT(INbt nbt) {
        Entity entity = (Entity) EntityType.create(nbt.getMCNBT(), this.level).orElse(null);
        if (entity == null) {
            throw new CustomNPCsException("Failed to create an entity from given NBT");
        } else {
            return NpcAPI.Instance().getIEntity(entity);
        }
    }

    @Override
    public IEntity createEntity(String id) {
        ResourceLocation resource = new ResourceLocation(id);
        EntityType type = ForgeRegistries.ENTITY_TYPES.getValue(resource);
        Entity entity = type.create(this.level);
        if (entity == null) {
            throw new CustomNPCsException("Failed to create an entity from given id: " + id);
        } else {
            entity.setPos(0.0, 1.0, 0.0);
            return NpcAPI.Instance().getIEntity(entity);
        }
    }

    @Override
    public IPlayer getPlayer(String name) {
        for (Player entityplayer : this.level.players()) {
            if (name.equals(entityplayer.getName().getString())) {
                return (IPlayer) NpcAPI.Instance().getIEntity(entityplayer);
            }
        }
        return null;
    }

    private Class getClassForType(int type) {
        if (type == -1) {
            return Entity.class;
        } else if (type == 5) {
            return LivingEntity.class;
        } else if (type == 1) {
            return Player.class;
        } else if (type == 4) {
            return Animal.class;
        } else if (type == 3) {
            return Monster.class;
        } else if (type == 2) {
            return EntityNPCInterface.class;
        } else if (type == 6) {
            return ItemEntity.class;
        } else if (type == 7) {
            return EntityProjectile.class;
        } else if (type == 11) {
            return ThrowableProjectile.class;
        } else if (type == 10) {
            return AbstractArrow.class;
        } else if (type == 8) {
            return PixelmonHelper.getPixelmonClass();
        } else {
            return type == 9 ? Villager.class : Entity.class;
        }
    }

    @Override
    public long getTime() {
        return this.level.m_46468_();
    }

    @Override
    public void setTime(long time) {
        this.level.setDayTime(time);
    }

    @Override
    public long getTotalTime() {
        return this.level.m_46467_();
    }

    @Override
    public IBlock getBlock(int x, int y, int z) {
        return NpcAPI.Instance().getIBlock(this.level, new BlockPos(x, y, z));
    }

    @Override
    public IBlock getBlock(IPos pos) {
        return NpcAPI.Instance().getIBlock(this.level, pos.getMCBlockPos());
    }

    public boolean isChunkLoaded(int x, int z) {
        return this.level.getChunkSource().hasChunk(x >> 4, z >> 4);
    }

    @Override
    public void setBlock(int x, int y, int z, String name, int meta) {
        this.setBlock(NpcAPI.Instance().getIPos((double) x, (double) y, (double) z), name);
    }

    @Override
    public IBlock setBlock(IPos pos, String name) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
        if (block == null) {
            throw new CustomNPCsException("There is no such block: %s", name);
        } else {
            this.level.m_7731_(pos.getMCBlockPos(), block.defaultBlockState(), 2);
            return NpcAPI.Instance().getIBlock(this.level, pos.getMCBlockPos());
        }
    }

    @Override
    public void removeBlock(int x, int y, int z) {
        this.level.m_7471_(new BlockPos(x, y, z), false);
    }

    @Override
    public void removeBlock(IPos pos) {
        this.level.m_7471_(pos.getMCBlockPos(), false);
    }

    @Override
    public float getLightValue(int x, int y, int z) {
        return (float) this.level.m_7146_(new BlockPos(x, y, z)) / 16.0F;
    }

    @Override
    public IBlock getSpawnPoint() {
        BlockPos pos = this.level.m_220360_();
        if (pos == null) {
            pos = this.level.m_220360_();
        }
        return NpcAPI.Instance().getIBlock(this.level, pos);
    }

    @Override
    public void setSpawnPoint(IBlock block) {
        ServerLevelData info = (ServerLevelData) this.level.m_6106_();
        info.m_7250_(new BlockPos(block.getX(), block.getY(), block.getZ()), 0.0F);
    }

    @Override
    public boolean isDay() {
        return this.level.m_46468_() % 24000L < 12000L;
    }

    @Override
    public boolean isRaining() {
        return this.level.m_6106_().isRaining();
    }

    @Override
    public void setRaining(boolean bo) {
        ServerLevelData data = (ServerLevelData) this.level.m_6106_();
        if (bo) {
            data.m_5565_(true);
            data.setRainTime(120000000);
        } else {
            data.m_5565_(false);
            data.setRainTime(0);
        }
    }

    @Override
    public void thunderStrike(double x, double y, double z) {
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.level);
        bolt.m_6027_(x, y, z);
        bolt.setVisualOnly(false);
        this.level.addFreshEntity(bolt);
    }

    @Override
    public void spawnParticle(String particle, double x, double y, double z, double dx, double dy, double dz, double speed, int count) {
        ParticleType type = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(particle));
        if (type == null) {
            throw new CustomNPCsException("Unknown particle type: " + particle);
        } else {
            this.level.sendParticles((ParticleOptions) type, x, y, z, count, dx, dy, dz, speed);
        }
    }

    @Override
    public IData getTempdata() {
        return this.tempdata;
    }

    @Override
    public IData getStoreddata() {
        return this.storeddata;
    }

    @Override
    public IItemStack createItem(String name, int size) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
        if (item == null) {
            throw new CustomNPCsException("Unknown item id: " + name);
        } else {
            return NpcAPI.Instance().getIItemStack(new ItemStack(item, size));
        }
    }

    @Override
    public IItemStack createItemFromNbt(INbt nbt) {
        ItemStack item = ItemStack.of(nbt.getMCNBT());
        if (item.isEmpty()) {
            throw new CustomNPCsException("Failed to create an item from given NBT");
        } else {
            return NpcAPI.Instance().getIItemStack(item);
        }
    }

    @Override
    public void explode(double x, double y, double z, float range, boolean fire, boolean grief) {
        this.level.m_255391_(null, x, y, z, range, fire, grief ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE);
    }

    @Override
    public IPlayer[] getAllPlayers() {
        List<ServerPlayer> list = this.level.getServer().getPlayerList().getPlayers();
        IPlayer[] arr = new IPlayer[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = (IPlayer) NpcAPI.Instance().getIEntity((Entity) list.get(i));
        }
        return arr;
    }

    @Override
    public String getBiomeName(int x, int z) {
        try {
            return ((ResourceKey) this.level.m_204166_(new BlockPos(x, 0, z)).unwrapKey().get()).location().toString();
        } catch (Exception var4) {
            return "";
        }
    }

    @Override
    public IEntity spawnClone(double x, double y, double z, int tab, String name) {
        return NpcAPI.Instance().getClones().spawn(x, y, z, tab, name, this);
    }

    @Override
    public void spawnEntity(IEntity entity) {
        if (entity == null) {
            throw new CustomNPCsException("Entity given was null");
        } else {
            Entity e = entity.getMCEntity();
            if (this.level.getEntity(e.getUUID()) != null) {
                throw new CustomNPCsException("Entity with this UUID already exists");
            } else {
                e.setPos(e.getX(), e.getY(), e.getZ());
                this.level.addFreshEntity(e);
            }
        }
    }

    @Override
    public IEntity getClone(int tab, String name) {
        return NpcAPI.Instance().getClones().get(tab, name, this);
    }

    @Override
    public IScoreboard getScoreboard() {
        return new ScoreboardWrapper(this.level.getServer());
    }

    @Override
    public void broadcast(String message) {
        Component text = Component.literal(message);
        for (Player p : this.level.getPlayers(e -> true)) {
            p.m_213846_(text);
        }
    }

    @Override
    public int getRedstonePower(int x, int y, int z) {
        return this.level.m_277173_(new BlockPos(x, y, z));
    }

    @Deprecated
    public static WorldWrapper createNew(ServerLevel level) {
        return new WorldWrapper(level);
    }

    @Override
    public IDimension getDimension() {
        return this.dimension;
    }

    @Override
    public String getName() {
        return ((ServerLevelData) this.level.m_6106_()).getLevelName();
    }

    @Override
    public BlockPos getMCBlockPos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    @Override
    public void playSoundAt(IPos pos, String sound, float volume, float pitch) {
        BlockPos bp = pos.getMCBlockPos();
        Packets.sendNearby(this.level, bp, 16, new PacketPlaySound(sound, bp, volume, pitch));
    }

    @Override
    public void trigger(int id, Object... arguments) {
        EventHooks.onScriptTriggerEvent(ScriptController.Instance.forgeScripts, id, this, BlockPosWrapper.ZERO, null, arguments);
    }
}