package dev.xkmc.l2library.serial.advancements;

import com.google.gson.JsonObject;
import java.util.List;
import net.minecraft.advancements.Advancement;

public class AdvBuilderWrapper extends Advancement {

    private final Advancement adv;

    private final List<IAdvBuilder> list;

    public AdvBuilderWrapper(Advancement adv, List<IAdvBuilder> list) {
        super(adv.getId(), adv.getParent(), adv.getDisplay(), adv.getRewards(), adv.getCriteria(), adv.getRequirements(), adv.sendsTelemetryEvent());
        this.adv = adv;
        this.list = list;
    }

    @Override
    public Advancement.Builder deconstruct() {
        return new AdvBuilderWrapper.FakeBuilder(this.adv.deconstruct());
    }

    private class FakeBuilder extends Advancement.Builder {

        private final Advancement.Builder parent;

        public FakeBuilder(Advancement.Builder parent) {
            super(false);
            this.parent = parent;
        }

        @Override
        public JsonObject serializeToJson() {
            JsonObject ans = this.parent.serializeToJson();
            for (IAdvBuilder e : AdvBuilderWrapper.this.list) {
                e.modifyJson(ans);
            }
            return ans;
        }
    }
}