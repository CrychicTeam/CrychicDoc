package org.violetmoon.zeta.module;

import java.util.Set;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.play.loading.ZGatherHints;

public class ZetaModule {

    public Zeta zeta;

    public ZetaCategory category;

    public String displayName = "";

    public String lowercaseName = "";

    public String description = "";

    public Set<String> antiOverlap = Set.of();

    public boolean enabled = false;

    public boolean enabledByDefault = false;

    public boolean disabledByOverlap = false;

    public boolean ignoreAntiOverlap = false;

    public void postConstruct() {
    }

    public final void setEnabled(Zeta z, boolean willEnable) {
        if (z.configManager != null && !z.configManager.isCategoryEnabled(this.category)) {
            willEnable = false;
        }
        if (this.category != null && !this.category.requiredModsLoaded(z)) {
            willEnable = false;
        }
        if (!this.ignoreAntiOverlap && this.antiOverlap.stream().anyMatch(z::isModLoaded)) {
            this.disabledByOverlap = true;
            willEnable = false;
        } else {
            this.disabledByOverlap = false;
        }
        this.setEnabledAndManageSubscriptions(z, willEnable);
    }

    private void setEnabledAndManageSubscriptions(Zeta z, boolean nowEnabled) {
        if (this.enabled != nowEnabled) {
            this.enabled = nowEnabled;
            if (nowEnabled) {
                z.playBus.subscribe(this.getClass()).subscribe(this);
            } else {
                z.playBus.unsubscribe(this.getClass()).unsubscribe(this);
            }
        }
    }

    @PlayEvent
    public final void addAnnotationHints(ZGatherHints event) {
        event.gatherHintsFromModule(this, this.zeta.configManager.getConfigFlagManager());
    }
}