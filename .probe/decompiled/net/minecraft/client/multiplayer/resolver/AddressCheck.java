package net.minecraft.client.multiplayer.resolver;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.mojang.blocklist.BlockListSupplier;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Predicate;

public interface AddressCheck {

    boolean isAllowed(ResolvedServerAddress var1);

    boolean isAllowed(ServerAddress var1);

    static AddressCheck createFromService() {
        final ImmutableList<Predicate<String>> $$0 = (ImmutableList<Predicate<String>>) Streams.stream(ServiceLoader.load(BlockListSupplier.class)).map(BlockListSupplier::createBlockList).filter(Objects::nonNull).collect(ImmutableList.toImmutableList());
        return new AddressCheck() {

            @Override
            public boolean isAllowed(ResolvedServerAddress p_171835_) {
                String $$1 = p_171835_.getHostName();
                String $$2 = p_171835_.getHostIp();
                return $$0.stream().noneMatch(p_171841_ -> p_171841_.test($$1) || p_171841_.test($$2));
            }

            @Override
            public boolean isAllowed(ServerAddress p_171837_) {
                String $$1 = p_171837_.getHost();
                return $$0.stream().noneMatch(p_171844_ -> p_171844_.test($$1));
            }
        };
    }
}