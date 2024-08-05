package net.liopyu.entityjs.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.latvian.mods.kubejs.script.data.VirtualKubeJSDataPack;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import java.util.LinkedList;
import net.liopyu.entityjs.util.EventHandlers;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { ServerScriptManager.class }, remap = false)
public abstract class ServerScriptManagerMixin {

    @Unique
    private MultiPackResourceManager entityjs$WrappedManager;

    @Unique
    private VirtualKubeJSDataPack entityjs$VirtualDataPack;

    @WrapOperation(method = { "wrapResourceManager" }, at = { @At(value = "INVOKE", target = "Ldev/latvian/mods/kubejs/server/ServerScriptManager;reload(Lnet/minecraft/server/packs/resources/ResourceManager;)V") }, remap = false)
    private void entityjs$captureMultiManager(ServerScriptManager instance, ResourceManager resourceManager, Operation<Void> original) {
        if (original instanceof MultiPackResourceManager multiManager) {
            this.entityjs$WrappedManager = multiManager;
        }
        original.call(new Object[] { instance, resourceManager });
    }

    @WrapOperation(method = { "wrapResourceManager" }, at = { @At(value = "INVOKE", target = "Ljava/util/LinkedList;addFirst(Ljava/lang/Object;)V") }, remap = false)
    private <E> void entityjs$captureVirtualDataPack(LinkedList<E> instance, E e, Operation<Void> original) {
        if (e instanceof VirtualKubeJSDataPack pack) {
            this.entityjs$VirtualDataPack = pack;
        }
        original.call(new Object[] { instance, e });
    }

    @Inject(method = { "wrapResourceManager" }, at = { @At(target = "Ldev/latvian/mods/kubejs/event/EventHandler;post(Ldev/latvian/mods/kubejs/script/ScriptTypeHolder;Ldev/latvian/mods/kubejs/event/EventJS;)Ldev/latvian/mods/kubejs/event/EventResult;", shift = Shift.AFTER, value = "INVOKE", ordinal = 1) }, remap = false)
    private void entityjs$postEvent(CloseableResourceManager original, CallbackInfoReturnable<MultiPackResourceManager> cir) {
        EventHandlers.postDataEvent(this.entityjs$VirtualDataPack, this.entityjs$WrappedManager);
    }
}