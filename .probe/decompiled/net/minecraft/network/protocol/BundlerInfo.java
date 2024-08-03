package net.minecraft.network.protocol;

import io.netty.util.AttributeKey;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.network.PacketListener;

public interface BundlerInfo {

    AttributeKey<BundlerInfo.Provider> BUNDLER_PROVIDER = AttributeKey.valueOf("bundler");

    int BUNDLE_SIZE_LIMIT = 4096;

    BundlerInfo EMPTY = new BundlerInfo() {

        @Override
        public void unbundlePacket(Packet<?> p_265538_, Consumer<Packet<?>> p_265064_) {
            p_265064_.accept(p_265538_);
        }

        @Nullable
        @Override
        public BundlerInfo.Bundler startPacketBundling(Packet<?> p_265749_) {
            return null;
        }
    };

    static <T extends PacketListener, P extends BundlePacket<T>> BundlerInfo createForPacket(final Class<P> classP0, final Function<Iterable<Packet<T>>, P> functionIterablePacketTP1, final BundleDelimiterPacket<T> bundleDelimiterPacketT2) {
        return new BundlerInfo() {

            @Override
            public void unbundlePacket(Packet<?> p_265337_, Consumer<Packet<?>> p_265615_) {
                if (p_265337_.getClass() == classP0) {
                    P $$2 = (P) p_265337_;
                    p_265615_.accept(bundleDelimiterPacketT2);
                    $$2.subPackets().forEach(p_265615_);
                    p_265615_.accept(bundleDelimiterPacketT2);
                } else {
                    p_265615_.accept(p_265337_);
                }
            }

            @Nullable
            @Override
            public BundlerInfo.Bundler startPacketBundling(Packet<?> p_265097_) {
                return p_265097_ == bundleDelimiterPacketT2 ? new BundlerInfo.Bundler() {

                    private final List<Packet<T>> bundlePackets = new ArrayList();

                    @Nullable
                    @Override
                    public Packet<?> addPacket(Packet<?> p_265205_) {
                        if (p_265205_ == bundleDelimiterPacketT2) {
                            return (Packet<?>) functionIterablePacketTP1.apply(this.bundlePackets);
                        } else if (this.bundlePackets.size() >= 4096) {
                            throw new IllegalStateException("Too many packets in a bundle");
                        } else {
                            this.bundlePackets.add(p_265205_);
                            return null;
                        }
                    }
                } : null;
            }
        };
    }

    void unbundlePacket(Packet<?> var1, Consumer<Packet<?>> var2);

    @Nullable
    BundlerInfo.Bundler startPacketBundling(Packet<?> var1);

    public interface Bundler {

        @Nullable
        Packet<?> addPacket(Packet<?> var1);
    }

    public interface Provider {

        BundlerInfo getBundlerInfo(PacketFlow var1);
    }
}