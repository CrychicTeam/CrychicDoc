package dev.xkmc.l2library.capability.conditionals;

import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2library.init.L2LibraryConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.util.Wrappers;
import java.util.HashMap;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class ConditionalData extends PlayerCapabilityTemplate<ConditionalData> {

    public static final Capability<ConditionalData> CAPABILITY = CapabilityManager.get(new CapabilityToken<ConditionalData>() {
    });

    public static final PlayerCapabilityHolder<ConditionalData> HOLDER = new PlayerCapabilityHolder<>(new ResourceLocation("l2library", "conditionals"), CAPABILITY, ConditionalData.class, ConditionalData::new, PlayerCapabilityNetworkHandler::new);

    @SerialField
    public HashMap<TokenKey<?>, ConditionalToken> data = new HashMap();

    @SerialField
    public int tickSinceDeath = 0;

    private HashMap<TokenKey<?>, ConditionalToken> copy = null;

    public static void register() {
    }

    @Override
    public void onClone(boolean isWasDeath) {
        this.tickSinceDeath = 0;
    }

    public synchronized <T extends ConditionalToken, C extends Context> T getOrCreateData(TokenProvider<T, C> setEffect, C ent) {
        return (T) Wrappers.cast((ConditionalToken) this.data.computeIfAbsent(setEffect.getKey(), e -> setEffect.getData(ent)));
    }

    @Nullable
    public synchronized <T extends ConditionalToken> T getData(TokenKey<T> setEffect) {
        if (this.copy != null) {
            ConditionalToken ans = (ConditionalToken) this.copy.get(setEffect);
            if (ans != null) {
                return (T) Wrappers.cast(ans);
            }
        }
        return (T) Wrappers.cast((ConditionalToken) this.data.get(setEffect));
    }

    @Override
    public synchronized void tick() {
        this.tickSinceDeath++;
        if (L2LibraryConfig.COMMON.restoreFullHealthOnRespawn.get() && this.tickSinceDeath < 60 && this.player.m_21223_() < this.player.m_21233_()) {
            this.player.m_21153_(this.player.m_21233_());
        }
        this.copy = this.data;
        this.data = new HashMap();
        this.copy.entrySet().removeIf(e -> ((ConditionalToken) e.getValue()).tick(this.player));
        this.copy.putAll(this.data);
        this.data = this.copy;
        this.copy = null;
    }

    public synchronized boolean hasData(TokenKey<?> eff) {
        return this.copy != null && this.copy.containsKey(eff) || this.data.containsKey(eff);
    }
}