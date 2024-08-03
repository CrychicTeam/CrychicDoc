package com.yungnickyoung.minecraft.paxi.mixin;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.yungnickyoung.minecraft.paxi.PaxiRepositorySource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ PackRepository.class })
public abstract class MixinPackRepositoryForge {

    @Shadow
    private Map<String, Pack> available;

    @Final
    @Shadow
    private Set<RepositorySource> sources;

    @Shadow
    private Stream<Pack> getAvailablePacks(Collection<String> names) {
        throw new AssertionError();
    }

    @Inject(at = { @At("HEAD") }, method = { "rebuildSelected" }, cancellable = true)
    private void paxi_buildEnabledProfilesForge(Collection<String> enabledNames, CallbackInfoReturnable<List<Pack>> cir) {
        Optional<RepositorySource> paxiRepositorySource = this.sources.stream().filter(provider -> provider instanceof PaxiRepositorySource).findFirst();
        List<Pack> allEnabledPacks = (List<Pack>) this.getAvailablePacks(enabledNames).collect(Collectors.toList());
        List<Pack> paxiPacks = new ArrayList();
        if (paxiRepositorySource.isPresent() && ((PaxiRepositorySource) paxiRepositorySource.get()).orderedPaxiPacks.size() > 0) {
            paxiPacks = (List<Pack>) this.getAvailablePacks(((PaxiRepositorySource) paxiRepositorySource.get()).orderedPaxiPacks).collect(Collectors.toList());
            allEnabledPacks.removeAll(paxiPacks);
        }
        for (Pack pack : paxiPacks) {
            if (pack.isRequired() && !allEnabledPacks.contains(pack)) {
                pack.getDefaultPosition().insert(allEnabledPacks, pack, Functions.identity(), false);
            }
        }
        for (Pack packx : this.available.values()) {
            if (packx.isRequired() && !allEnabledPacks.contains(packx)) {
                packx.getDefaultPosition().insert(allEnabledPacks, packx, Functions.identity(), false);
            }
        }
        cir.setReturnValue(ImmutableList.copyOf(allEnabledPacks));
    }
}