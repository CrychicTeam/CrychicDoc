package vazkii.patchouli.xplat;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.patchouli.api.PatchouliAPI;

public interface IClientXplatAbstractions {

    IClientXplatAbstractions INSTANCE = find();

    void renderForMultiblock(BlockState var1, BlockPos var2, BlockAndTintGetter var3, PoseStack var4, MultiBufferSource var5, RandomSource var6);

    private static IClientXplatAbstractions find() {
        List<Provider<IClientXplatAbstractions>> providers = ServiceLoader.load(IClientXplatAbstractions.class).stream().toList();
        if (providers.size() != 1) {
            String names = (String) providers.stream().map(p -> p.type().getName()).collect(Collectors.joining(",", "[", "]"));
            throw new IllegalStateException("There should be exactly one IClientXplatAbstractions implementation on the classpath. Found: " + names);
        } else {
            Provider<IClientXplatAbstractions> provider = (Provider<IClientXplatAbstractions>) providers.get(0);
            PatchouliAPI.LOGGER.debug("Instantiating client xplat impl: " + provider.type().getName());
            return (IClientXplatAbstractions) provider.get();
        }
    }
}