package net.minecraft.server;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.authlib.yggdrasil.ServicesKeyType;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.io.File;
import javax.annotation.Nullable;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.util.SignatureValidator;

public record Services(MinecraftSessionService f_214333_, ServicesKeySet f_283795_, GameProfileRepository f_214335_, GameProfileCache f_214336_) {

    private final MinecraftSessionService sessionService;

    private final ServicesKeySet servicesKeySet;

    private final GameProfileRepository profileRepository;

    private final GameProfileCache profileCache;

    private static final String USERID_CACHE_FILE = "usercache.json";

    public Services(MinecraftSessionService f_214333_, ServicesKeySet f_283795_, GameProfileRepository f_214335_, GameProfileCache f_214336_) {
        this.sessionService = f_214333_;
        this.servicesKeySet = f_283795_;
        this.profileRepository = f_214335_;
        this.profileCache = f_214336_;
    }

    public static Services create(YggdrasilAuthenticationService p_214345_, File p_214346_) {
        MinecraftSessionService $$2 = p_214345_.createMinecraftSessionService();
        GameProfileRepository $$3 = p_214345_.createProfileRepository();
        GameProfileCache $$4 = new GameProfileCache($$3, new File(p_214346_, "usercache.json"));
        return new Services($$2, p_214345_.getServicesKeySet(), $$3, $$4);
    }

    @Nullable
    public SignatureValidator profileKeySignatureValidator() {
        return SignatureValidator.from(this.servicesKeySet, ServicesKeyType.PROFILE_KEY);
    }
}