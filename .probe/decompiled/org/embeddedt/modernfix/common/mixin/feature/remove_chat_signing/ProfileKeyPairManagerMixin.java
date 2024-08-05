package org.embeddedt.modernfix.common.mixin.feature.remove_chat_signing;

import com.mojang.authlib.minecraft.UserApiService;
import java.nio.file.Path;
import net.minecraft.client.User;
import net.minecraft.client.multiplayer.ProfileKeyPairManager;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ ProfileKeyPairManager.class })
@ClientOnlyMixin
public interface ProfileKeyPairManagerMixin {

    @Overwrite
    static ProfileKeyPairManager create(UserApiService userApiService, User user, Path gameDirectory) {
        return ProfileKeyPairManager.EMPTY_KEY_MANAGER;
    }
}