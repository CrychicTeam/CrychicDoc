package snownee.kiwi.mixin.customization;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.duck.KBlockProperties;

@Mixin({ BlockBehaviour.Properties.class })
public class BlockPropertiesMixin implements KBlockProperties {

    @Unique
    private KBlockSettings settings;

    @Nullable
    @Override
    public KBlockSettings kiwi$getSettings() {
        return this.settings;
    }

    @Override
    public void kiwi$setSettings(KBlockSettings settings) {
        this.settings = settings;
    }
}