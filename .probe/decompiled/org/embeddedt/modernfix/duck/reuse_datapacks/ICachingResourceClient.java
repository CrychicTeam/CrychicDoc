package org.embeddedt.modernfix.duck.reuse_datapacks;

import java.util.Collection;
import net.minecraft.server.ReloadableServerResources;

public interface ICachingResourceClient {

    void setCachedResources(ReloadableServerResources var1);

    void setCachedDataPackConfig(Collection<String> var1);
}