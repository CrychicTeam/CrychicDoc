package org.embeddedt.modernfix.forge.mixin.perf.resourcepacks;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.forge.load.ModResourcePackPathFixer;
import org.embeddedt.modernfix.resources.ICachingResourcePack;
import org.embeddedt.modernfix.resources.NewResourcePackAdapter;
import org.embeddedt.modernfix.resources.PackResourcesCacheEngine;
import org.embeddedt.modernfix.util.PackTypeHelper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { PathPackResources.class }, priority = 1100)
public abstract class ForgePathPackResourcesMixin implements ICachingResourcePack {

    private PackResourcesCacheEngine cacheEngine;

    private IModFile mfix$resolveFileOverride;

    @Shadow(remap = false)
    protected abstract Path resolve(String... var1);

    @Shadow(remap = false)
    @NotNull
    protected abstract Set<String> getNamespacesFromDisk(PackType var1);

    @Shadow(remap = false)
    private static String[] getPathFromLocation(PackType type, ResourceLocation location) {
        throw new AssertionError();
    }

    @Inject(method = { "<init>" }, at = { @At("TAIL") })
    private void cacheResources(String packId, boolean isBuiltin, Path source, CallbackInfo ci) {
        if (this.getClass() == PathPackResources.class) {
            this.mfix$resolveFileOverride = ModResourcePackPathFixer.getModFileByRootPath(source);
        }
        if (this.mfix$resolveFileOverride != null) {
            ModernFix.LOGGER.warn("PathResourcePack base class instantiated with root path of mod file {}. This probably means a mod should be calling ResourcePackLoader.createPackForMod instead. Applying workaround.", this.mfix$resolveFileOverride.getFileName());
        }
        this.invalidateCache();
        PackResourcesCacheEngine.track(this);
    }

    @Inject(method = { "resolve" }, at = { @At("HEAD") }, cancellable = true, remap = false)
    private void resolveViaModFile(String[] paths, CallbackInfoReturnable<Path> cir) {
        if (this.mfix$resolveFileOverride != null) {
            cir.setReturnValue(this.mfix$resolveFileOverride.findResource(paths));
        }
    }

    private PackResourcesCacheEngine generateResourceCache() {
        synchronized (this) {
            PackResourcesCacheEngine engine = this.cacheEngine;
            if (engine != null) {
                return engine;
            } else {
                this.cacheEngine = engine = new PackResourcesCacheEngine(this::getNamespacesFromDisk, (type, namespace) -> this.resolve(type.getDirectory(), namespace));
                return engine;
            }
        }
    }

    @Override
    public void invalidateCache() {
        this.cacheEngine = null;
    }

    @Redirect(method = { "getNamespaces" }, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/resource/PathPackResources;getNamespacesFromDisk(Lnet/minecraft/server/packs/PackType;)Ljava/util/Set;"))
    private Set<String> useCacheForNamespaces(PathPackResources instance, PackType type) {
        PackResourcesCacheEngine engine = this.cacheEngine;
        if (engine != null) {
            Set<String> namespaces = engine.getNamespaces(type);
            if (namespaces != null) {
                return namespaces;
            }
        }
        return this.getNamespacesFromDisk(type);
    }

    @Redirect(method = { "getRootResource" }, at = @At(value = "INVOKE", target = "Ljava/nio/file/Files;exists(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Z"))
    private boolean useCacheForExistence(Path path, LinkOption[] options, String[] originalPaths) {
        return originalPaths.length >= 3 && (Objects.equals(originalPaths[0], "assets") || Objects.equals(originalPaths[0], "data")) ? this.generateResourceCache().hasResource(originalPaths) : Files.exists(path, options);
    }

    @Inject(method = { "listResources" }, at = { @At("HEAD") }, cancellable = true)
    private void fastGetResources(PackType type, String namespace, String path, PackResources.ResourceOutput resourceOutput, CallbackInfo ci) {
        if (PackTypeHelper.isVanillaPackType(type)) {
            ci.cancel();
            Collection<ResourceLocation> allPossibleResources = this.generateResourceCache().getResources(type, namespace, path, Integer.MAX_VALUE, p -> true);
            NewResourcePackAdapter.sendToOutput(location -> {
                Path target = this.resolve(getPathFromLocation(location.getPath().startsWith("lang/") ? PackType.CLIENT_RESOURCES : type, location));
                return () -> Files.newInputStream(target);
            }, resourceOutput, allPossibleResources);
        }
    }
}