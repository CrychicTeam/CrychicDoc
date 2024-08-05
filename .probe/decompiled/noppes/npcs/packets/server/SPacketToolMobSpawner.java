package noppes.npcs.packets.server;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NBTTags;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.BaseSpawnerMixin;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketToolMobSpawner extends PacketServerBasic {

    private boolean createSpawner;

    private boolean server;

    private BlockPos pos;

    private String name = "";

    private int tab = -1;

    private CompoundTag clone = new CompoundTag();

    public SPacketToolMobSpawner(boolean createSpawner, BlockPos pos, String name, int tab) {
        this.server = true;
        this.createSpawner = createSpawner;
        this.pos = pos;
        this.name = name;
        this.tab = tab;
    }

    public SPacketToolMobSpawner(boolean createSpawner, BlockPos pos, CompoundTag clone) {
        this.server = false;
        this.createSpawner = createSpawner;
        this.pos = pos;
        this.clone = clone;
    }

    public SPacketToolMobSpawner(boolean createSpawner, boolean server, BlockPos pos, String name, int tab, CompoundTag clone) {
        this.createSpawner = createSpawner;
        this.server = server;
        this.pos = pos;
        this.name = name;
        this.tab = tab;
        this.clone = clone;
    }

    public SPacketToolMobSpawner(FriendlyByteBuf buf) {
        this.createSpawner = buf.readBoolean();
        this.server = buf.readBoolean();
        this.pos = buf.readBlockPos();
        this.name = buf.readUtf(32767);
        this.tab = buf.readInt();
        this.clone = buf.readNbt();
    }

    public static SPacketToolMobSpawner decode(FriendlyByteBuf buf) {
        return new SPacketToolMobSpawner(buf);
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.cloner;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return this.createSpawner ? CustomNpcsPermissions.SPAWNER_CREATE : CustomNpcsPermissions.SPAWNER_MOB;
    }

    @Override
    public void handle() {
        if (this.server) {
            this.clone = ServerCloneController.Instance.getCloneData(this.player.m_20203_(), this.name, this.tab);
        }
        if (this.clone != null && !this.clone.isEmpty()) {
            if (this.createSpawner) {
                createMobSpawner(this.pos, this.clone, this.player);
            } else {
                Entity entity = spawnClone(this.clone, (double) this.pos.m_123341_() + 0.5, (double) (this.pos.m_123342_() + 1), (double) this.pos.m_123343_() + 0.5, this.player.m_9236_());
                if (entity == null) {
                    this.player.sendSystemMessage(Component.literal("Failed to create an entity out of your clone"));
                }
            }
        }
    }

    public static Entity spawnClone(CompoundTag compound, double x, double y, double z, Level world) {
        ServerCloneController.Instance.cleanTags(compound);
        compound.put("Pos", NBTTags.nbtDoubleList(x, y, z));
        Entity entity = (Entity) EntityType.create(compound, world).get();
        if (entity == null) {
            return null;
        } else {
            if (entity instanceof EntityNPCInterface npc) {
                npc.ais.setStartPos(npc.m_20183_());
            }
            world.m_7967_(entity);
            return entity;
        }
    }

    public static void createMobSpawner(BlockPos pos, CompoundTag comp, Player player) {
        ServerCloneController.Instance.cleanTags(comp);
        if (comp.getString("id").equalsIgnoreCase("entityhorse")) {
            player.m_213846_(Component.literal("Currently you cant create horse spawner, its a minecraft bug"));
        } else {
            player.m_9236_().setBlockAndUpdate(pos, Blocks.SPAWNER.defaultBlockState());
            SpawnerBlockEntity tile = (SpawnerBlockEntity) player.m_9236_().getBlockEntity(pos);
            BaseSpawner logic = tile.getSpawner();
            if (!comp.contains("id", 8)) {
                comp.putString("id", "Pig");
            }
            comp.putIntArray("StartPosNew", new int[] { pos.m_123341_(), pos.m_123342_(), pos.m_123343_() });
            ((BaseSpawnerMixin) logic).callSetNextSpawnData(player.m_9236_(), pos, new SpawnData(comp, Optional.empty()));
        }
    }

    public static void encode(SPacketToolMobSpawner msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.createSpawner);
        buf.writeBoolean(msg.server);
        buf.writeBlockPos(msg.pos);
        buf.writeUtf(msg.name);
        buf.writeInt(msg.tab);
        buf.writeNbt(msg.clone);
    }
}