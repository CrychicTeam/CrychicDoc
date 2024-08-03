package org.violetmoon.quark.addons.oddities.module;

import java.util.Collection;
import java.util.Objects;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.violetmoon.quark.addons.oddities.client.render.entity.TotemOfHoldingRenderer;
import org.violetmoon.quark.addons.oddities.entity.TotemOfHoldingEntity;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.tweaks.compat.TotemOfHoldingCuriosCompat;
import org.violetmoon.zeta.client.event.load.ZAddModels;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.living.ZLivingDrops;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "oddities")
public class TotemOfHoldingModule extends ZetaModule {

    private static final String TAG_LAST_TOTEM = "quark:lastTotemOfHolding";

    private static final String TAG_DEATH_X = "quark:deathX";

    private static final String TAG_DEATH_Z = "quark:deathZ";

    private static final String TAG_DEATH_DIM = "quark:deathDim";

    public static EntityType<TotemOfHoldingEntity> totemType;

    @Config(description = "Set this to false to remove the behaviour where totems destroy themselves if the player dies again.")
    public static boolean darkSoulsMode = true;

    @Config(name = "Spawn Totem on PVP Kill", description = "Totem will always spawn if the player killer is themselves.")
    public static boolean enableOnPK = false;

    @Config(description = "Set this to true to make it so that if a totem is destroyed, the items it holds are destroyed alongside it rather than dropped")
    public static boolean destroyLostItems = false;

    @Config(description = "Set this to false to only allow the owner of a totem to collect its items rather than any player")
    public static boolean allowAnyoneToCollect = true;

    @LoadEvent
    public final void register(ZRegister event) {
        totemType = EntityType.Builder.of(TotemOfHoldingEntity::new, MobCategory.MISC).sized(0.5F, 1.0F).updateInterval(128).fireImmune().setShouldReceiveVelocityUpdates(false).setCustomClientFactory((spawnEntity, world) -> new TotemOfHoldingEntity(totemType, world)).build("totem");
        Quark.ZETA.registry.register(totemType, "totem", Registries.ENTITY_TYPE);
    }

    @PlayEvent
    public void onPlayerDrops(ZLivingDrops.Lowest event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            Collection drops = event.getDrops();
            if (!event.isCanceled() && (enableOnPK || !(event.getSource().getEntity() instanceof Player) || entity == event.getSource().getEntity())) {
                CompoundTag data = player.getPersistentData();
                CompoundTag persistent = data.getCompound("PlayerPersisted");
                if (!drops.isEmpty()) {
                    TotemOfHoldingEntity totem = new TotemOfHoldingEntity(totemType, player.m_9236_());
                    totem.m_6034_(player.m_20185_(), Math.max((double) (player.m_9236_().m_141937_() + 3), player.m_20186_() + 1.0), player.m_20189_());
                    totem.setOwner(player);
                    totem.m_6593_(player.getDisplayName());
                    drops.stream().filter(Objects::nonNull).map(ItemEntity::m_32055_).filter(stack -> !stack.isEmpty()).forEach(totem::addItem);
                    if (this.zeta.isModLoaded("curios")) {
                        TotemOfHoldingCuriosCompat.saveCurios(player, totem);
                    }
                    if (!player.m_9236_().isClientSide) {
                        player.m_9236_().m_7967_(totem);
                    }
                    persistent.putString("quark:lastTotemOfHolding", totem.m_20148_().toString());
                    event.setCanceled(true);
                } else {
                    persistent.putString("quark:lastTotemOfHolding", "");
                }
                BlockPos pos = player.m_20183_();
                persistent.putInt("quark:deathX", pos.m_123341_());
                persistent.putInt("quark:deathZ", pos.m_123343_());
                persistent.putString("quark:deathDim", player.m_9236_().dimension().location().toString());
                if (!data.contains("PlayerPersisted")) {
                    data.put("PlayerPersisted", persistent);
                }
            }
        }
    }

    public static String getTotemUUID(Player player) {
        CompoundTag cmp = player.getPersistentData().getCompound("PlayerPersisted");
        return cmp.contains("quark:lastTotemOfHolding") ? cmp.getString("quark:lastTotemOfHolding") : "";
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends TotemOfHoldingModule {

        @LoadEvent
        public final void clientSetup(ZClientSetup event) {
            EntityRenderers.register(totemType, TotemOfHoldingRenderer::new);
        }

        @LoadEvent
        public void registerAdditionalModels(ZAddModels event) {
            event.register(new ModelResourceLocation("quark", "extra/totem_of_holding", "inventory"));
        }
    }
}