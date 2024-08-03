package net.minecraft.client;

import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class User {

    private final String name;

    private final String uuid;

    private final String accessToken;

    private final Optional<String> xuid;

    private final Optional<String> clientId;

    private final User.Type type;

    public User(String string0, String string1, String string2, Optional<String> optionalString3, Optional<String> optionalString4, User.Type userType5) {
        this.name = string0;
        this.uuid = string1;
        this.accessToken = string2;
        this.xuid = optionalString3;
        this.clientId = optionalString4;
        this.type = userType5;
    }

    public String getSessionId() {
        return "token:" + this.accessToken + ":" + this.uuid;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public Optional<String> getClientId() {
        return this.clientId;
    }

    public Optional<String> getXuid() {
        return this.xuid;
    }

    @Nullable
    public UUID getProfileId() {
        try {
            return UUIDTypeAdapter.fromString(this.getUuid());
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public GameProfile getGameProfile() {
        return new GameProfile(this.getProfileId(), this.getName());
    }

    public User.Type getType() {
        return this.type;
    }

    public static enum Type {

        LEGACY("legacy"), MOJANG("mojang"), MSA("msa");

        private static final Map<String, User.Type> BY_NAME = (Map<String, User.Type>) Arrays.stream(values()).collect(Collectors.toMap(p_92560_ -> p_92560_.name, Function.identity()));

        private final String name;

        private Type(String p_92558_) {
            this.name = p_92558_;
        }

        @Nullable
        public static User.Type byName(String p_92562_) {
            return (User.Type) BY_NAME.get(p_92562_.toLowerCase(Locale.ROOT));
        }

        public String getName() {
            return this.name;
        }
    }
}