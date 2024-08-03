package net.minecraft.client.player;

import com.google.common.hash.Hashing;
import com.mojang.authlib.GameProfile;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractClientPlayer extends Player {

    private static final String SKIN_URL_TEMPLATE = "http://skins.minecraft.net/MinecraftSkins/%s.png";

    @Nullable
    private PlayerInfo playerInfo;

    protected Vec3 deltaMovementOnPreviousTick = Vec3.ZERO;

    public float elytraRotX;

    public float elytraRotY;

    public float elytraRotZ;

    public final ClientLevel clientLevel;

    public AbstractClientPlayer(ClientLevel clientLevel0, GameProfile gameProfile1) {
        super(clientLevel0, clientLevel0.m_220360_(), clientLevel0.m_220361_(), gameProfile1);
        this.clientLevel = clientLevel0;
    }

    @Override
    public boolean isSpectator() {
        PlayerInfo $$0 = this.getPlayerInfo();
        return $$0 != null && $$0.getGameMode() == GameType.SPECTATOR;
    }

    @Override
    public boolean isCreative() {
        PlayerInfo $$0 = this.getPlayerInfo();
        return $$0 != null && $$0.getGameMode() == GameType.CREATIVE;
    }

    public boolean isCapeLoaded() {
        return this.getPlayerInfo() != null;
    }

    @Nullable
    protected PlayerInfo getPlayerInfo() {
        if (this.playerInfo == null) {
            this.playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(this.m_20148_());
        }
        return this.playerInfo;
    }

    @Override
    public void tick() {
        this.deltaMovementOnPreviousTick = this.m_20184_();
        super.tick();
    }

    public Vec3 getDeltaMovementLerped(float float0) {
        return this.deltaMovementOnPreviousTick.lerp(this.m_20184_(), (double) float0);
    }

    public boolean isSkinLoaded() {
        PlayerInfo $$0 = this.getPlayerInfo();
        return $$0 != null && $$0.isSkinLoaded();
    }

    public ResourceLocation getSkinTextureLocation() {
        PlayerInfo $$0 = this.getPlayerInfo();
        return $$0 == null ? DefaultPlayerSkin.getDefaultSkin(this.m_20148_()) : $$0.getSkinLocation();
    }

    @Nullable
    public ResourceLocation getCloakTextureLocation() {
        PlayerInfo $$0 = this.getPlayerInfo();
        return $$0 == null ? null : $$0.getCapeLocation();
    }

    public boolean isElytraLoaded() {
        return this.getPlayerInfo() != null;
    }

    @Nullable
    public ResourceLocation getElytraTextureLocation() {
        PlayerInfo $$0 = this.getPlayerInfo();
        return $$0 == null ? null : $$0.getElytraLocation();
    }

    public static void registerSkinTexture(ResourceLocation resourceLocation0, String string1) {
        TextureManager $$2 = Minecraft.getInstance().getTextureManager();
        AbstractTexture $$3 = $$2.getTexture(resourceLocation0, MissingTextureAtlasSprite.getTexture());
        if ($$3 == MissingTextureAtlasSprite.getTexture()) {
            AbstractTexture var4 = new HttpTexture(null, String.format(Locale.ROOT, "http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtil.stripColor(string1)), DefaultPlayerSkin.getDefaultSkin(UUIDUtil.createOfflinePlayerUUID(string1)), true, null);
            $$2.register(resourceLocation0, var4);
        }
    }

    public static ResourceLocation getSkinLocation(String string0) {
        return new ResourceLocation("skins/" + Hashing.sha1().hashUnencodedChars(StringUtil.stripColor(string0)));
    }

    public String getModelName() {
        PlayerInfo $$0 = this.getPlayerInfo();
        return $$0 == null ? DefaultPlayerSkin.getSkinModelName(this.m_20148_()) : $$0.getModelName();
    }

    public float getFieldOfViewModifier() {
        float $$0 = 1.0F;
        if (this.m_150110_().flying) {
            $$0 *= 1.1F;
        }
        $$0 *= ((float) this.m_21133_(Attributes.MOVEMENT_SPEED) / this.m_150110_().getWalkingSpeed() + 1.0F) / 2.0F;
        if (this.m_150110_().getWalkingSpeed() == 0.0F || Float.isNaN($$0) || Float.isInfinite($$0)) {
            $$0 = 1.0F;
        }
        ItemStack $$1 = this.m_21211_();
        if (this.m_6117_()) {
            if ($$1.is(Items.BOW)) {
                int $$2 = this.m_21252_();
                float $$3 = (float) $$2 / 20.0F;
                if ($$3 > 1.0F) {
                    $$3 = 1.0F;
                } else {
                    $$3 *= $$3;
                }
                $$0 *= 1.0F - $$3 * 0.15F;
            } else if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && this.m_150108_()) {
                return 0.1F;
            }
        }
        return Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get().floatValue(), 1.0F, $$0);
    }
}