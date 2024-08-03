package net.minecraft.client.multiplayer.resolver;

import com.google.common.annotations.VisibleForTesting;
import java.util.Optional;

public class ServerNameResolver {

    public static final ServerNameResolver DEFAULT = new ServerNameResolver(ServerAddressResolver.SYSTEM, ServerRedirectHandler.createDnsSrvRedirectHandler(), AddressCheck.createFromService());

    private final ServerAddressResolver resolver;

    private final ServerRedirectHandler redirectHandler;

    private final AddressCheck addressCheck;

    @VisibleForTesting
    ServerNameResolver(ServerAddressResolver serverAddressResolver0, ServerRedirectHandler serverRedirectHandler1, AddressCheck addressCheck2) {
        this.resolver = serverAddressResolver0;
        this.redirectHandler = serverRedirectHandler1;
        this.addressCheck = addressCheck2;
    }

    public Optional<ResolvedServerAddress> resolveAddress(ServerAddress serverAddress0) {
        Optional<ResolvedServerAddress> $$1 = this.resolver.resolve(serverAddress0);
        if ((!$$1.isPresent() || this.addressCheck.isAllowed((ResolvedServerAddress) $$1.get())) && this.addressCheck.isAllowed(serverAddress0)) {
            Optional<ServerAddress> $$2 = this.redirectHandler.lookupRedirect(serverAddress0);
            if ($$2.isPresent()) {
                $$1 = this.resolver.resolve((ServerAddress) $$2.get()).filter(this.addressCheck::m_142649_);
            }
            return $$1;
        } else {
            return Optional.empty();
        }
    }
}