package me.jellysquid.mods.lithium.mixin.alloc.nbt;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ CompoundTag.class })
public class NbtCompoundMixin {

    @Shadow
    @Final
    private Map<String, Tag> tags;

    @ModifyArg(method = { "<init>()V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;<init>(Ljava/util/Map;)V"))
    private static Map<String, Tag> useFasterCollection(Map<String, Tag> oldMap) {
        return new Object2ObjectOpenHashMap();
    }

    @Redirect(method = { "<init>()V" }, at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Maps;newHashMap()Ljava/util/HashMap;", remap = false))
    private static HashMap<?, ?> removeOldMapAlloc() {
        return null;
    }

    @Overwrite
    public CompoundTag copy() {
        Object2ObjectOpenHashMap<String, Tag> map = new Object2ObjectOpenHashMap(Maps.transformValues(this.tags, Tag::m_6426_));
        return new CompoundTag(map);
    }

    @Mixin(targets = { "net.minecraft.nbt.NbtCompound$1" })
    static class Type {

        @ModifyVariable(method = { "read(Ljava/io/DataInput;ILnet/minecraft/nbt/NbtTagSizeTracker;)Lnet/minecraft/nbt/NbtCompound;" }, at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/common/collect/Maps;newHashMap()Ljava/util/HashMap;", remap = false))
        private Map<String, Tag> useFasterCollection(Map<String, Tag> map) {
            return new Object2ObjectOpenHashMap();
        }

        @Redirect(method = { "read(Ljava/io/DataInput;ILnet/minecraft/nbt/NbtTagSizeTracker;)Lnet/minecraft/nbt/NbtCompound;" }, at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Maps;newHashMap()Ljava/util/HashMap;", remap = false))
        private HashMap<?, ?> removeOldMapAlloc() {
            return null;
        }
    }
}