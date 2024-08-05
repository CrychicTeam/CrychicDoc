package se.mickelus.tetra.blocks.holo;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.tetra.ServerScheduler;
import se.mickelus.tetra.TetraSounds;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.impl.holo.ModularHolosphereItem;

public class HolosphereBlockEntity extends BlockEntity {

    public static final int maxRange = 8;

    public static RegistryObject<BlockEntityType<HolosphereBlockEntity>> type;

    private List<HolosphereBlockEntity.ScanResult> scanResults;

    private long scanModeTimestamp = 0L;

    private CompoundTag itemTag;

    private LazyOptional<Boolean> canScan = LazyOptional.<CompoundTag>of(() -> this.itemTag).lazyMap(tag -> {
        ItemStack itemStack = new ItemStack(ModularHolosphereItem.instance);
        itemStack.setTag(tag);
        return (Boolean) Optional.ofNullable(ModularHolosphereItem.instance.getEffectData(itemStack)).map(effects -> effects.getLevel(ItemEffect.percussionScanner) > 0).orElse(false);
    });

    public HolosphereBlockEntity(BlockPos pos, BlockState blockState) {
        super(type.get(), pos, blockState);
        this.scanResults = new ArrayList();
    }

    public AABB getRenderBoundingBox() {
        return Shapes.block().bounds().inflate(1.0, 0.5, 1.0).move(this.f_58858_);
    }

    public List<HolosphereBlockEntity.ScanResult> getScanResults() {
        return this.scanResults;
    }

    public boolean canScan() {
        return this.canScan.orElse(false);
    }

    public long getScanModeTimestamp() {
        return this.scanModeTimestamp;
    }

    public boolean inScanMode() {
        return this.scanModeTimestamp > 0L;
    }

    public void toggleScanMode(boolean enable) {
        long diff = Math.abs(Math.abs(this.scanModeTimestamp) - this.f_58857_.getGameTime());
        long time = diff < 5L ? this.f_58857_.getGameTime() - diff : this.f_58857_.getGameTime();
        this.scanModeTimestamp = enable ? time : -time;
        this.m_6596_();
        this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
    }

    private String[] getScannableStructures() {
        return new String[] { "#tetra:forged_ruins" };
    }

    private long getTimestamp(long gametime, int x, int y) {
        return (long) ((double) gametime + Mth.length((double) x, (double) y) * 1.5 + Math.random() * 5.0);
    }

    public void use(int hammerLevel, float hammerEfficiency, float angle) {
        ServerLevel serverLevel = (ServerLevel) this.m_58904_();
        int count = 4 + (int) hammerEfficiency / 4;
        double cone = (double) (20 + hammerLevel * 6) * Math.PI / 180.0;
        int ox = SectionPos.blockToSectionCoord(this.m_58899_().m_123341_());
        int oz = SectionPos.blockToSectionCoord(this.m_58899_().m_123343_());
        int preResultSize = this.scanResults.size();
        AtomicInteger stagger = new AtomicInteger();
        ChunkPos.rangeClosed(new ChunkPos(-32, -32), new ChunkPos(32, 32)).filter(pos -> Math.abs(Mth.atan2((double) pos.x, (double) pos.z) - (double) angle) < cone).sorted(Comparator.comparingInt(pos -> pos.x * pos.x + pos.z * pos.z)).map(pos -> new ChunkPos(pos.x + ox, pos.z + oz)).filter(pos -> Math.abs(pos.x - ox) + Math.abs(pos.z - oz) < 20).filter(pos -> this.scanResults.stream().noneMatch(result -> result.chunkX == pos.x && result.chunkZ == pos.z)).limit((long) count).forEach(pos -> {
            long timestamp = this.getTimestamp(serverLevel.m_46467_(), pos.x - ox, pos.z - oz) + (long) stagger.getAndIncrement() * 3L;
            int height = serverLevel.m_46819_(pos.x, pos.z, ChunkStatus.SURFACE).getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.x, pos.z);
            BlockPos centerPos = pos.getMiddleBlockPosition(height);
            float temperature = ((Biome) this.f_58857_.m_204166_(centerPos).value()).getTemperature(centerPos);
            List<String> structures = Arrays.stream(this.getScannableStructures()).filter(id -> ScanHelper.hasStructure(id, serverLevel, pos)).toList();
            this.scanResults.add(new HolosphereBlockEntity.ScanResult(pos.x, pos.z, height, temperature, structures, timestamp));
            ServerScheduler.schedule((int) (timestamp - serverLevel.m_46467_()), () -> serverLevel.m_5594_(null, this.m_58899_(), TetraSounds.scanMiss, SoundSource.PLAYERS, 0.01F, 1.0F + (float) Math.random() * 0.0F));
            if (!structures.isEmpty()) {
                ServerScheduler.schedule((int) (timestamp - serverLevel.m_46467_() + 20L), () -> serverLevel.m_5594_(null, this.m_58899_(), TetraSounds.scanHit, SoundSource.PLAYERS, 0.1F, 1.0F));
            }
        });
        if (preResultSize != this.scanResults.size()) {
            this.m_6596_();
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
        }
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(ModularHolosphereItem.instance);
        itemStack.setTag(this.getItemTag());
        return itemStack;
    }

    public CompoundTag getItemTag() {
        return this.itemTag;
    }

    public void setItemTag(CompoundTag tag) {
        this.itemTag = tag;
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.itemTag = compound.getCompound("item");
        this.scanModeTimestamp = compound.getLong("timestamp");
        this.scanResults = (List<HolosphereBlockEntity.ScanResult>) compound.getList("scan", 10).stream().map(nbt -> HolosphereBlockEntity.ScanResult.codec.decode(NbtOps.INSTANCE, nbt)).map(DataResult::result).filter(Optional::isPresent).map(Optional::get).map(Pair::getFirst).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("item", this.itemTag);
        compound.putLong("timestamp", this.scanModeTimestamp);
        ListTag list = (ListTag) this.scanResults.stream().map(scroll -> HolosphereBlockEntity.ScanResult.codec.encodeStart(NbtOps.INSTANCE, scroll)).map(DataResult::result).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toCollection(ListTag::new));
        compound.put("scan", list);
    }

    static record ScanResult(int chunkX, int chunkZ, int height, float temperature, List<String> structures, long timestamp) {

        static final Codec<HolosphereBlockEntity.ScanResult> codec = RecordCodecBuilder.create(instance -> instance.group(Codec.INT.fieldOf("chunkX").forGetter(i -> i.chunkX), Codec.INT.fieldOf("chunkZ").forGetter(i -> i.chunkZ), Codec.INT.fieldOf("height").forGetter(i -> i.height), Codec.FLOAT.fieldOf("temperature").forGetter(i -> i.temperature), Codec.STRING.listOf().fieldOf("structures").forGetter(i -> i.structures), Codec.LONG.fieldOf("timestamp").forGetter(i -> i.timestamp)).apply(instance, HolosphereBlockEntity.ScanResult::new));
    }
}