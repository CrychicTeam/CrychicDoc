package snownee.kiwi.mixin.customization;

import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Set;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import snownee.kiwi.customization.CustomizationHooks;

@Mixin({ BlockEntityType.class })
public class BlockEntityTypeMixin {

    @Shadow
    @Final
    private Set<Block> validBlocks;

    @Unique
    private volatile Set<Block> lenientValidBlocks;

    @WrapOperation(method = { "isValid" }, at = { @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z") })
    public boolean isValid(Set<Block> instance, Object object, Operation<Boolean> original) {
        if (!CustomizationHooks.isEnabled()) {
            return (Boolean) original.call(new Object[] { instance, object });
        } else if (object == null) {
            return false;
        } else if (this.lenientValidBlocks != null && this.lenientValidBlocks.contains(object)) {
            return true;
        } else if ((Boolean) original.call(new Object[] { instance, object })) {
            return true;
        } else {
            for (Block validBlock : this.validBlocks) {
                if (validBlock.getClass() == object.getClass()) {
                    if (this.lenientValidBlocks == null) {
                        synchronized (this.validBlocks) {
                            if (this.lenientValidBlocks == null) {
                                this.lenientValidBlocks = Sets.newHashSet(this.validBlocks);
                            }
                        }
                    }
                    this.lenientValidBlocks.add((Block) object);
                    return true;
                }
            }
            return false;
        }
    }
}