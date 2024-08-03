package net.minecraft.world.ticks;

import it.unimi.dsi.fastutil.Hash.Strategy;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.ChunkPos;

public record SavedTick<T>(T f_193311_, BlockPos f_193312_, int f_193313_, TickPriority f_193314_) {

    private final T type;

    private final BlockPos pos;

    private final int delay;

    private final TickPriority priority;

    private static final String TAG_ID = "i";

    private static final String TAG_X = "x";

    private static final String TAG_Y = "y";

    private static final String TAG_Z = "z";

    private static final String TAG_DELAY = "t";

    private static final String TAG_PRIORITY = "p";

    public static final Strategy<SavedTick<?>> UNIQUE_TICK_HASH = new Strategy<SavedTick<?>>() {

        public int hashCode(SavedTick<?> p_193364_) {
            return 31 * p_193364_.pos().hashCode() + p_193364_.type().hashCode();
        }

        public boolean equals(@Nullable SavedTick<?> p_193366_, @Nullable SavedTick<?> p_193367_) {
            if (p_193366_ == p_193367_) {
                return true;
            } else {
                return p_193366_ != null && p_193367_ != null ? p_193366_.type() == p_193367_.type() && p_193366_.pos().equals(p_193367_.pos()) : false;
            }
        }
    };

    public SavedTick(T f_193311_, BlockPos f_193312_, int f_193313_, TickPriority f_193314_) {
        this.type = f_193311_;
        this.pos = f_193312_;
        this.delay = f_193313_;
        this.priority = f_193314_;
    }

    public static <T> void loadTickList(ListTag p_193351_, Function<String, Optional<T>> p_193352_, ChunkPos p_193353_, Consumer<SavedTick<T>> p_193354_) {
        long $$4 = p_193353_.toLong();
        for (int $$5 = 0; $$5 < p_193351_.size(); $$5++) {
            CompoundTag $$6 = p_193351_.getCompound($$5);
            loadTick($$6, p_193352_).ifPresent(p_210665_ -> {
                if (ChunkPos.asLong(p_210665_.pos()) == $$4) {
                    p_193354_.accept(p_210665_);
                }
            });
        }
    }

    public static <T> Optional<SavedTick<T>> loadTick(CompoundTag p_210670_, Function<String, Optional<T>> p_210671_) {
        return ((Optional) p_210671_.apply(p_210670_.getString("i"))).map(p_210668_ -> {
            BlockPos $$2 = new BlockPos(p_210670_.getInt("x"), p_210670_.getInt("y"), p_210670_.getInt("z"));
            return new SavedTick<>(p_210668_, $$2, p_210670_.getInt("t"), TickPriority.byValue(p_210670_.getInt("p")));
        });
    }

    private static CompoundTag saveTick(String p_193339_, BlockPos p_193340_, int p_193341_, TickPriority p_193342_) {
        CompoundTag $$4 = new CompoundTag();
        $$4.putString("i", p_193339_);
        $$4.putInt("x", p_193340_.m_123341_());
        $$4.putInt("y", p_193340_.m_123342_());
        $$4.putInt("z", p_193340_.m_123343_());
        $$4.putInt("t", p_193341_);
        $$4.putInt("p", p_193342_.getValue());
        return $$4;
    }

    public static <T> CompoundTag saveTick(ScheduledTick<T> p_193332_, Function<T, String> p_193333_, long p_193334_) {
        return saveTick((String) p_193333_.apply(p_193332_.type()), p_193332_.pos(), (int) (p_193332_.triggerTick() - p_193334_), p_193332_.priority());
    }

    public CompoundTag save(Function<T, String> p_193344_) {
        return saveTick((String) p_193344_.apply(this.type), this.pos, this.delay, this.priority);
    }

    public ScheduledTick<T> unpack(long p_193329_, long p_193330_) {
        return new ScheduledTick<>(this.type, this.pos, p_193329_ + (long) this.delay, this.priority, p_193330_);
    }

    public static <T> SavedTick<T> probe(T p_193336_, BlockPos p_193337_) {
        return new SavedTick<>(p_193336_, p_193337_, 0, TickPriority.NORMAL);
    }
}