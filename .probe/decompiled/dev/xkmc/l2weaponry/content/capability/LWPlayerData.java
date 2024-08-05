package dev.xkmc.l2weaponry.content.capability;

import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2weaponry.content.item.base.BaseShieldItem;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class LWPlayerData extends PlayerCapabilityTemplate<LWPlayerData> implements IShieldData {

    public static final Capability<LWPlayerData> CAPABILITY = CapabilityManager.get(new CapabilityToken<LWPlayerData>() {
    });

    public static final PlayerCapabilityHolder<LWPlayerData> HOLDER = new PlayerCapabilityHolder<>(new ResourceLocation("l2weaponry", "main"), CAPABILITY, LWPlayerData.class, LWPlayerData::new, PlayerCapabilityNetworkHandler::new);

    @SerialField
    private double shieldDefense = 0.0;

    @SerialField
    private int reflectTimer = 0;

    private double shieldRetain = 0.0;

    public static void register() {
    }

    @Override
    public double getShieldDefense() {
        return this.shieldDefense;
    }

    @Override
    public void setShieldDefense(double shieldDefense) {
        this.shieldDefense = shieldDefense;
        if (this.player instanceof ServerPlayer sp) {
            HOLDER.network.toClientSyncAll(sp);
        }
    }

    @Override
    public int getReflectTimer() {
        return this.reflectTimer;
    }

    public double getRecoverRate() {
        ItemStack stack = this.player.m_21211_();
        double recover = 0.01;
        if (stack.getItem() instanceof BaseShieldItem shield) {
            recover *= shield.getDefenseRecover(stack);
        }
        return recover;
    }

    @Override
    public void tick() {
        double recover = this.getRecoverRate();
        if (this.shieldDefense > 0.0) {
            this.shieldDefense -= recover;
            if (this.shieldDefense < 0.0) {
                this.shieldDefense = 0.0;
            }
        }
        if (this.reflectTimer > 0) {
            this.reflectTimer--;
            if (this.reflectTimer == 0) {
                this.shieldRetain = 0.0;
            }
        }
    }

    @Override
    public boolean canReflect() {
        return !this.player.m_6144_() && this.player.m_21133_((Attribute) LWItems.REFLECT_TIME.get()) > 0.0;
    }

    public void startReflectTimer() {
        if (this.canReflect() && !(this.shieldDefense > 0.0)) {
            this.shieldDefense = this.shieldDefense + LWConfig.COMMON.reflectCost.get();
            this.shieldRetain = this.shieldDefense * this.player.m_21133_((Attribute) LWItems.SHIELD_DEFENSE.get());
            this.reflectTimer = (int) this.player.m_21133_((Attribute) LWItems.REFLECT_TIME.get());
        }
    }

    public void clearReflectTimer() {
        this.reflectTimer = 0;
        this.shieldRetain = 0.0;
    }

    @Override
    public double popRetain() {
        double ans = this.shieldRetain;
        this.shieldRetain = 0.0;
        this.reflectTimer = 0;
        return ans;
    }
}