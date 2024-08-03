package noobanidus.mods.lootr.advancement;

import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import noobanidus.mods.lootr.api.advancement.IGenericPredicate;

public class AdvancementPredicate implements IGenericPredicate<ResourceLocation> {

    private ResourceLocation advancementId;

    public AdvancementPredicate() {
    }

    public AdvancementPredicate(ResourceLocation advancementId) {
        this.advancementId = advancementId;
    }

    public boolean test(ServerPlayer player, ResourceLocation location) {
        return this.advancementId != null && this.advancementId.equals(location);
    }

    @Override
    public IGenericPredicate<ResourceLocation> deserialize(@Nullable JsonObject element) {
        if (element == null) {
            throw new IllegalArgumentException("AdvancementPredicate requires an object");
        } else {
            ResourceLocation rl = new ResourceLocation(element.getAsJsonPrimitive("advancement").getAsString());
            return new AdvancementPredicate(rl);
        }
    }
}