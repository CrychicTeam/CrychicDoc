package snownee.loquat.mixin;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.loquat.Hooks;
import snownee.loquat.core.AreaManager;
import snownee.loquat.core.area.Area;

@Mixin(value = { StructureTemplate.class }, priority = 800)
public class StructureTemplateMixin {

    @Unique
    private final List<Area> loquat$areas = Lists.newArrayList();

    @Shadow
    private Vec3i size;

    @Inject(method = { "load" }, at = { @At("HEAD") })
    private void loquat$load(HolderGetter<Block> holderGetter, CompoundTag tag, CallbackInfo ci) {
        this.loquat$areas.clear();
        if (tag.contains("Loquat", 10)) {
            CompoundTag loquat = tag.getCompound("Loquat");
            this.loquat$areas.addAll(AreaManager.loadAreas(loquat.getList("Areas", 10)));
        }
    }

    @Inject(method = { "save" }, at = { @At("HEAD") })
    private void loquat$save(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if (!this.loquat$areas.isEmpty()) {
            CompoundTag loquat = new CompoundTag();
            loquat.put("Areas", AreaManager.saveAreas(this.loquat$areas));
            tag.put("Loquat", loquat);
        }
    }

    @Inject(method = { "placeInWorld" }, at = { @At("HEAD") })
    private void loquat$placeInWorld(ServerLevelAccessor serverLevel, BlockPos pos, BlockPos blockPos, StructurePlaceSettings settings, RandomSource random, int flags, CallbackInfoReturnable<Boolean> cir) {
        if (!this.loquat$areas.isEmpty()) {
            AreaManager manager = AreaManager.of(serverLevel.getLevel());
            Hooks.placeInWorld(manager, pos, blockPos, this.loquat$areas, settings, this.size);
        }
    }

    @Inject(method = { "fillFromWorld" }, at = { @At("HEAD") })
    private void loquat$fillFromWorld(Level level, BlockPos pos, Vec3i size, boolean withEntities, Block toIgnore, CallbackInfo ci) {
        this.loquat$areas.clear();
        if (level instanceof ServerLevel serverLevel) {
            AreaManager manager = AreaManager.of(serverLevel);
            Hooks.fillFromWorld(manager, pos, size, this.loquat$areas);
        }
    }
}