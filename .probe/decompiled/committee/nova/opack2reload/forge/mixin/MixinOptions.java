package committee.nova.opack2reload.forge.mixin;

import java.util.List;
import net.minecraft.client.Options;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Options.class })
public abstract class MixinOptions {

    @Shadow
    public List<String> resourcePacks;

    @Shadow
    public List<String> incompatibleResourcePacks;

    @Inject(method = { "updateResourcePacks" }, at = { @At("HEAD") })
    private void inject$updateResourcePacks(PackRepository repo, CallbackInfo ci) {
        if (this.resourcePacks.isEmpty()) {
            for (Pack pack : ((InvokerPackRepository) repo).callRebuildSelected(List.of())) {
                if (!pack.isFixedPosition()) {
                    this.resourcePacks.add(pack.getId());
                    if (!pack.getCompatibility().isCompatible()) {
                        this.incompatibleResourcePacks.add(pack.getId());
                    }
                }
            }
        }
    }
}