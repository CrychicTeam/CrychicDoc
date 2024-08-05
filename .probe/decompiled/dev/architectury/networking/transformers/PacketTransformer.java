package dev.architectury.networking.transformers;

import dev.architectury.networking.NetworkManager;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
public interface PacketTransformer {

    void inbound(NetworkManager.Side var1, ResourceLocation var2, FriendlyByteBuf var3, NetworkManager.PacketContext var4, PacketTransformer.TransformationSink var5);

    void outbound(NetworkManager.Side var1, ResourceLocation var2, FriendlyByteBuf var3, PacketTransformer.TransformationSink var4);

    static PacketTransformer none() {
        return new PacketTransformer() {

            @Override
            public void inbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, NetworkManager.PacketContext context, PacketTransformer.TransformationSink sink) {
                sink.accept(side, id, buf);
            }

            @Override
            public void outbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, PacketTransformer.TransformationSink sink) {
                sink.accept(side, id, buf);
            }
        };
    }

    static PacketTransformer concat(Iterable<? extends PacketTransformer> transformers) {
        if (transformers instanceof Collection && ((Collection) transformers).isEmpty()) {
            return none();
        } else {
            return transformers instanceof Collection && ((Collection) transformers).size() == 1 ? (PacketTransformer) transformers.iterator().next() : new PacketTransformer() {

                @Override
                public void inbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, NetworkManager.PacketContext context, PacketTransformer.TransformationSink sink) {
                    this.traverse(side, id, buf, context, sink, true, 0);
                }

                @Override
                public void outbound(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, PacketTransformer.TransformationSink sink) {
                    this.traverse(side, id, buf, null, sink, false, 0);
                }

                private void traverse(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf, @Nullable NetworkManager.PacketContext context, PacketTransformer.TransformationSink outerSink, boolean inbound, int index) {
                    if (transformers instanceof List) {
                        if (((List) transformers).size() > index) {
                            PacketTransformer transformer = (PacketTransformer) ((List) transformers).get(index);
                            PacketTransformer.TransformationSink sink = (side1, id1, buf1) -> this.traverse(side1, id1, buf1, context, outerSink, inbound, index + 1);
                            if (inbound) {
                                transformer.inbound(side, id, buf, context, sink);
                            } else {
                                transformer.outbound(side, id, buf, sink);
                            }
                        } else {
                            outerSink.accept(side, id, buf);
                        }
                    } else {
                        Iterator<? extends PacketTransformer> iterator = transformers.iterator();
                        for (int i = 0; i < index; i++) {
                            iterator.next();
                        }
                        PacketTransformer transformer = iterator.hasNext() ? (PacketTransformer) iterator.next() : PacketTransformer.none();
                        PacketTransformer.TransformationSink sink = (side1, id1, buf1) -> {
                            if (iterator.hasNext()) {
                                this.traverse(side1, id1, buf1, context, outerSink, inbound, index + 1);
                            } else {
                                outerSink.accept(side1, id1, buf1);
                            }
                        };
                        if (inbound) {
                            transformer.inbound(side, id, buf, context, sink);
                        } else {
                            transformer.outbound(side, id, buf, sink);
                        }
                    }
                }
            };
        }
    }

    @FunctionalInterface
    public interface TransformationSink {

        void accept(NetworkManager.Side var1, ResourceLocation var2, FriendlyByteBuf var3);
    }
}