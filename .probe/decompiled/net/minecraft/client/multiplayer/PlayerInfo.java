package net.minecraft.client.multiplayer;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.chat.SignedMessageValidator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.PlayerTeam;

public class PlayerInfo {

    private final GameProfile profile;

    private final Map<Type, ResourceLocation> textureLocations = Maps.newEnumMap(Type.class);

    private GameType gameMode = GameType.DEFAULT_MODE;

    private int latency;

    private boolean pendingTextures;

    @Nullable
    private String skinModel;

    @Nullable
    private Component tabListDisplayName;

    @Nullable
    private RemoteChatSession chatSession;

    private SignedMessageValidator messageValidator;

    public PlayerInfo(GameProfile gameProfile0, boolean boolean1) {
        this.profile = gameProfile0;
        this.messageValidator = fallbackMessageValidator(boolean1);
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    @Nullable
    public RemoteChatSession getChatSession() {
        return this.chatSession;
    }

    public SignedMessageValidator getMessageValidator() {
        return this.messageValidator;
    }

    public boolean hasVerifiableChat() {
        return this.chatSession != null;
    }

    protected void setChatSession(RemoteChatSession remoteChatSession0) {
        this.chatSession = remoteChatSession0;
        this.messageValidator = remoteChatSession0.createMessageValidator();
    }

    protected void clearChatSession(boolean boolean0) {
        this.chatSession = null;
        this.messageValidator = fallbackMessageValidator(boolean0);
    }

    private static SignedMessageValidator fallbackMessageValidator(boolean boolean0) {
        return boolean0 ? SignedMessageValidator.REJECT_ALL : SignedMessageValidator.ACCEPT_UNSIGNED;
    }

    public GameType getGameMode() {
        return this.gameMode;
    }

    protected void setGameMode(GameType gameType0) {
        this.gameMode = gameType0;
    }

    public int getLatency() {
        return this.latency;
    }

    protected void setLatency(int int0) {
        this.latency = int0;
    }

    public boolean isCapeLoaded() {
        return this.getCapeLocation() != null;
    }

    public boolean isSkinLoaded() {
        return this.getSkinLocation() != null;
    }

    public String getModelName() {
        return this.skinModel == null ? DefaultPlayerSkin.getSkinModelName(this.profile.getId()) : this.skinModel;
    }

    public ResourceLocation getSkinLocation() {
        this.registerTextures();
        return (ResourceLocation) MoreObjects.firstNonNull((ResourceLocation) this.textureLocations.get(Type.SKIN), DefaultPlayerSkin.getDefaultSkin(this.profile.getId()));
    }

    @Nullable
    public ResourceLocation getCapeLocation() {
        this.registerTextures();
        return (ResourceLocation) this.textureLocations.get(Type.CAPE);
    }

    @Nullable
    public ResourceLocation getElytraLocation() {
        this.registerTextures();
        return (ResourceLocation) this.textureLocations.get(Type.ELYTRA);
    }

    @Nullable
    public PlayerTeam getTeam() {
        return Minecraft.getInstance().level.getScoreboard().getPlayersTeam(this.getProfile().getName());
    }

    protected void registerTextures() {
        synchronized (this) {
            if (!this.pendingTextures) {
                this.pendingTextures = true;
                Minecraft.getInstance().getSkinManager().registerSkins(this.profile, (p_105320_, p_105321_, p_105322_) -> {
                    this.textureLocations.put(p_105320_, p_105321_);
                    if (p_105320_ == Type.SKIN) {
                        this.skinModel = p_105322_.getMetadata("model");
                        if (this.skinModel == null) {
                            this.skinModel = "default";
                        }
                    }
                }, true);
            }
        }
    }

    public void setTabListDisplayName(@Nullable Component component0) {
        this.tabListDisplayName = component0;
    }

    @Nullable
    public Component getTabListDisplayName() {
        return this.tabListDisplayName;
    }
}