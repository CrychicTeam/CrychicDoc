package dev.architectury.mixin.forge;

import dev.architectury.extensions.network.EntitySpawnExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntitySpawnExtension.class })
public interface MixinEntitySpawnExtension extends IEntityAdditionalSpawnData {

    @Override
    default void writeSpawnData(FriendlyByteBuf buf) {
        ((EntitySpawnExtension) this).saveAdditionalSpawnData(buf);
    }

    @Override
    default void readSpawnData(FriendlyByteBuf buf) {
        ((EntitySpawnExtension) this).loadAdditionalSpawnData(buf);
    }
}