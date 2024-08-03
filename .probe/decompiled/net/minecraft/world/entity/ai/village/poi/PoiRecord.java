package net.minecraft.world.entity.ai.village.poi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.VisibleForDebug;

public class PoiRecord {

    private final BlockPos pos;

    private final Holder<PoiType> poiType;

    private int freeTickets;

    private final Runnable setDirty;

    public static Codec<PoiRecord> codec(Runnable runnable0) {
        return RecordCodecBuilder.create(p_258948_ -> p_258948_.group(BlockPos.CODEC.fieldOf("pos").forGetter(p_148673_ -> p_148673_.pos), RegistryFixedCodec.create(Registries.POINT_OF_INTEREST_TYPE).fieldOf("type").forGetter(p_218017_ -> p_218017_.poiType), Codec.INT.fieldOf("free_tickets").orElse(0).forGetter(p_148669_ -> p_148669_.freeTickets), RecordCodecBuilder.point(runnable0)).apply(p_258948_, PoiRecord::new));
    }

    private PoiRecord(BlockPos blockPos0, Holder<PoiType> holderPoiType1, int int2, Runnable runnable3) {
        this.pos = blockPos0.immutable();
        this.poiType = holderPoiType1;
        this.freeTickets = int2;
        this.setDirty = runnable3;
    }

    public PoiRecord(BlockPos blockPos0, Holder<PoiType> holderPoiType1, Runnable runnable2) {
        this(blockPos0, holderPoiType1, holderPoiType1.value().maxTickets(), runnable2);
    }

    @Deprecated
    @VisibleForDebug
    public int getFreeTickets() {
        return this.freeTickets;
    }

    protected boolean acquireTicket() {
        if (this.freeTickets <= 0) {
            return false;
        } else {
            this.freeTickets--;
            this.setDirty.run();
            return true;
        }
    }

    protected boolean releaseTicket() {
        if (this.freeTickets >= this.poiType.value().maxTickets()) {
            return false;
        } else {
            this.freeTickets++;
            this.setDirty.run();
            return true;
        }
    }

    public boolean hasSpace() {
        return this.freeTickets > 0;
    }

    public boolean isOccupied() {
        return this.freeTickets != this.poiType.value().maxTickets();
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Holder<PoiType> getPoiType() {
        return this.poiType;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return object0 != null && this.getClass() == object0.getClass() ? Objects.equals(this.pos, ((PoiRecord) object0).pos) : false;
        }
    }

    public int hashCode() {
        return this.pos.hashCode();
    }
}