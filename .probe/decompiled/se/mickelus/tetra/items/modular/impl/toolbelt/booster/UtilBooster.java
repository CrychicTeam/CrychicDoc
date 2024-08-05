package se.mickelus.tetra.items.modular.impl.toolbelt.booster;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltHelper;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.QuickslotInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.StorageInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.ToolbeltInventory;

@ParametersAreNonnullByDefault
public class UtilBooster {

    public static final String activeKey = "booster.active";

    public static final String chargedKey = "booster.charged";

    public static final String fuelKey = "booster.fuel";

    public static final String bufferKey = "booster.buffer";

    public static final String cooldownKey = "booster.cooldown";

    public static final int fuelCapacity = 110;

    public static final int fuelCost = 1;

    public static final int fuelCostCharged = 40;

    public static final int fuelRecharge = 1;

    public static final int cooldownTicks = 20;

    public static final int gunpowderGain = 80;

    public static final float boostStrength = 0.04F;

    public static final float chargedBoostStrength = 1.2F;

    public static final float boostLevelMultiplier = 0.4F;

    public static boolean hasBooster(Player player) {
        ItemStack itemStack = ToolbeltHelper.findToolbelt(player);
        return canBoost(itemStack);
    }

    public static boolean canBoost(ItemStack itemStack) {
        return getBoosterLevel(itemStack) > 0;
    }

    public static int getBoosterLevel(ItemStack itemStack) {
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IModularItem) {
            IModularItem item = (IModularItem) itemStack.getItem();
            return item.getEffectLevel(itemStack, ItemEffect.booster);
        } else {
            return 0;
        }
    }

    public static boolean hasFuel(CompoundTag tag, boolean charged) {
        return charged ? tag.getInt("booster.fuel") >= 40 : tag.getInt("booster.fuel") >= 1;
    }

    public static int getFuel(CompoundTag tag) {
        return tag.getInt("booster.fuel");
    }

    public static float getFuelPercent(CompoundTag tag) {
        return (float) tag.getInt("booster.fuel") * 1.0F / 110.0F;
    }

    public static void boostPlayer(Player player, CompoundTag tag, int level) {
        float boostBase = 0.04F + 0.04F * (float) (level - 1) * 0.4F;
        if (player.m_21255_()) {
            Vec3 Vector3d = player.m_20154_();
            player.m_5997_(Vector3d.x * 0.01F + (Vector3d.x * 1.5 - player.m_20184_().x) * 0.05F, Vector3d.y * 0.01F + (Vector3d.y * 1.5 - player.m_20184_().y) * 0.05F, Vector3d.z * 0.01F + (Vector3d.z * 1.5 - player.m_20184_().z) * 0.05F);
        } else if (player.m_20184_().y > -0.1) {
            if (player.m_6047_()) {
                player.m_5997_(0.0, (double) boostBase / 1.5, 0.0);
            } else {
                player.m_5997_(0.0, (double) boostBase, 0.0);
            }
            player.f_19789_ = 0.0F;
        } else {
            player.m_5997_(0.0, (double) boostBase + 0.8 * -player.m_20184_().y, 0.0);
        }
        if (player.m_9236_() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, player.m_20185_() - 0.2 + Math.random() * 0.4, player.m_20186_() + Math.random() * 0.2, player.m_20189_() - 0.2 + Math.random() * 0.4, 8, 0.0, -0.3, 0.0, 0.1);
            if (Math.random() > 0.3) {
                serverLevel.sendParticles(ParticleTypes.FLAME, player.m_20185_() - 0.2 + Math.random() * 0.4, player.m_20186_() + Math.random() * 0.2, player.m_20189_() - 0.2 + Math.random() * 0.4, 1, 0.0, -0.3, 0.0, 0.1);
            }
        }
    }

    public static void boostHorizontal(Player player) {
        if (player.f_20902_ != 0.0F || player.f_20900_ != 0.0F) {
            ItemStack itemStack = ToolbeltHelper.findToolbelt(player);
            int level = getBoosterLevel(itemStack);
            if (level > 0) {
                CastOptional.cast(player, LocalPlayer.class).ifPresent(cp -> cp.connection.send(new ServerboundPlayerInputPacket(cp.f_20900_, cp.f_20902_, cp.input.jumping, cp.input.shiftKeyDown)));
                CompoundTag tag = itemStack.getOrCreateTag();
                if (hasFuel(tag, false)) {
                    consumeFuel(tag, false);
                    player.m_19920_(0.05F, new Vec3((double) player.f_20900_, (double) player.f_20901_, (double) player.f_20902_));
                    if (player.m_9236_().isClientSide) {
                        Vec3 direction = getAbsoluteMotion(-player.f_20900_, -player.f_20902_, player.m_146908_());
                        for (int i = 0; i < 8; i++) {
                            player.m_20193_().addParticle(ParticleTypes.SMOKE, player.m_20185_(), player.m_20186_() + (double) player.m_20206_() * 0.4, player.m_20189_(), Math.random() * (0.2 * direction.x + 0.07) - 0.05, Math.random() * 0.1 - 0.05, Math.random() * (0.2 * direction.z + 0.07) - 0.05);
                        }
                        if (Math.random() > 0.3) {
                            player.m_20193_().addParticle(ParticleTypes.FLAME, player.m_20185_(), player.m_20186_() + (double) player.m_20206_() * 0.4, player.m_20189_(), Math.random() * (0.2 * direction.x + 0.07) - 0.05, Math.random() * 0.1 - 0.05, Math.random() * (0.2 * direction.z + 0.07) - 0.05);
                        }
                    }
                }
            }
        }
    }

    private static Vec3 getAbsoluteMotion(float strafe, float forward, float facing) {
        float sin = Mth.sin(facing * (float) (Math.PI / 180.0));
        float cos = Mth.cos(facing * (float) (Math.PI / 180.0));
        return new Vec3((double) (strafe * cos - forward * sin), 0.0, (double) (forward * cos + strafe * sin));
    }

    public static void boostPlayerCharged(Player player, CompoundTag tag, int level) {
        float boostBase = 1.2F + 1.2F * (float) (level - 1) * 0.4F;
        Vec3 lookVector = player.m_20154_();
        player.m_20256_(lookVector.scale(player.m_20184_().dot(lookVector) / lookVector.dot(lookVector)));
        player.m_5997_(lookVector.x * (double) boostBase, Math.max(lookVector.y * (double) boostBase / 2.0 + 0.3, 0.1), lookVector.z * (double) boostBase);
        player.f_19864_ = true;
        player.m_6478_(MoverType.SELF, new Vec3(0.0, 0.4, 0.0));
        if (player.m_9236_() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, player.m_20185_(), player.m_20186_() + (double) player.m_20206_() * 0.4, player.m_20189_(), 10, 0.0, -0.1, 0.0, 0.1);
            serverLevel.sendParticles(ParticleTypes.FLAME, player.m_20185_(), player.m_20186_() + (double) player.m_20206_() * 0.4, player.m_20189_(), 3, 0.0, -0.1, 0.0, 0.1);
        }
    }

    public static void consumeFuel(CompoundTag tag, boolean charged) {
        if (charged) {
            tag.putInt("booster.fuel", tag.getInt("booster.fuel") - 40);
        } else {
            tag.putInt("booster.fuel", tag.getInt("booster.fuel") - 1);
        }
        tag.putInt("booster.cooldown", 20);
    }

    public static void consumeFuel(CompoundTag tag, int amount) {
        tag.putInt("booster.fuel", tag.getInt("booster.fuel") - amount);
        tag.putInt("booster.cooldown", 20);
    }

    public static void rechargeFuel(CompoundTag tag, ItemStack itemStack) {
        int fuel = tag.getInt("booster.fuel");
        int buffer = tag.getInt("booster.buffer");
        int cooldown = tag.getInt("booster.cooldown");
        if (cooldown > 0) {
            tag.putInt("booster.cooldown", cooldown - 1);
        } else if (fuel + 1 < 110) {
            if (buffer > 0) {
                tag.putInt("booster.fuel", fuel + 1);
                tag.putInt("booster.buffer", buffer - 1);
            } else {
                refuelBuffer(tag, itemStack);
            }
        }
    }

    private static void refuelBuffer(CompoundTag tag, ItemStack itemStack) {
        ToolbeltInventory inventory = new QuickslotInventory(itemStack);
        int index = inventory.getFirstIndexForItem(Items.GUNPOWDER);
        if (index != -1) {
            inventory.removeItem(index, 1);
            tag.putInt("booster.buffer", 80);
        } else {
            inventory = new StorageInventory(itemStack);
            index = inventory.getFirstIndexForItem(Items.GUNPOWDER);
            if (index != -1) {
                inventory.removeItem(index, 1);
                tag.putInt("booster.buffer", 80);
            } else {
                tag.putInt("booster.cooldown", 20);
            }
        }
    }

    public static boolean isActive(CompoundTag tag) {
        return tag.getBoolean("booster.active");
    }

    public static void setActive(CompoundTag tag, boolean active, boolean charged) {
        tag.putBoolean("booster.active", active);
        if (charged) {
            tag.putBoolean("booster.charged", charged);
        }
    }
}