package com.mna;

import com.mna.api.particles.ParticleInit;
import com.mna.api.tools.RLoc;
import com.mna.apibridge.APIBridge;
import com.mna.blocks.BlockClientInit;
import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.init.TileEntityClientInit;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.brewing.BrewingInit;
import com.mna.commands.CommandInit;
import com.mna.commands.CommandSerializerInit;
import com.mna.config.ClientConfig;
import com.mna.config.GeneralConfig;
import com.mna.effects.EffectInit;
import com.mna.effects.particles.EffectParticleSpawner;
import com.mna.enchantments.framework.EnchantmentInit;
import com.mna.entities.EntityClientInit;
import com.mna.entities.EntityInit;
import com.mna.entities.attributes.AttributeInit;
import com.mna.entities.models.constructs.modular.ConstructModelRegistry;
import com.mna.events.ClientEventHandler;
import com.mna.gui.GuiInit;
import com.mna.gui.HUDOverlayRenderer;
import com.mna.gui.containers.ContainerInit;
import com.mna.guide.GuideBookEntries;
import com.mna.items.ItemClientInit;
import com.mna.items.ItemInit;
import com.mna.loot.GlobalLootModifiers;
import com.mna.particles.ParticleClientInit;
import com.mna.recipes.RecipeInit;
import com.mna.tools.ISidedProxy;
import com.mna.tools.debugging.ForgeLoggerTweaker;
import com.mna.tools.proxy.ClientProxy;
import com.mna.tools.proxy.ServerProxy;
import com.mna.villagers.VillagerRegistry;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod("mna")
public class ManaAndArtifice {

    public static final Logger LOGGER = LogManager.getLogger();

    public static ManaAndArtifice instance;

    public static final ResourceLocation EMPTY = new ResourceLocation("mna", "empty");

    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    public ISidedProxy proxy;

    public final boolean isDebug;

    public final ArrayList<UUID> enabled_auras = new ArrayList();

    public ManaAndArtifice() {
        this.isDebug = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
        boolean HIDE_CONSOLE_NOISE = this.isDebug;
        if (HIDE_CONSOLE_NOISE) {
            ForgeLoggerTweaker.setMinimumLevel(Level.WARN);
            ForgeLoggerTweaker.applyLoggerFilter();
        }
        ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(Type.SERVER, GeneralConfig.GENERAL_SPEC, "mna-general.toml");
        APIBridge.earlyInit();
        LOGGER.info("M&A -> Configs Registered");
        ForgeMod.enableMilkFluid();
        instance = this;
        ItemInit.ITEMS.register(this.modEventBus);
        BlockInit.BLOCKS.register(this.modEventBus);
        TileEntityInit.TILE_ENTITY_TYPES.register(this.modEventBus);
        AttributeInit.ATTRIBUTES.register(this.modEventBus);
        EntityInit.ENTITY_TYPES.register(this.modEventBus);
        ParticleInit.PARTICLES.register(this.modEventBus);
        ContainerInit.CONTAINERS.register(this.modEventBus);
        RecipeInit.SERIALIZERS.register(this.modEventBus);
        RecipeInit.TYPES.register(this.modEventBus);
        VillagerRegistry.POI_TYPES.register(this.modEventBus);
        VillagerRegistry.PROFESSIONS.register(this.modEventBus);
        CommandSerializerInit.ARGUMENTS.register(this.modEventBus);
        GlobalLootModifiers.LOOT_MODIFIERS.register(this.modEventBus);
        EffectInit.EFFECTS.register(this.modEventBus);
        EnchantmentInit.ENCHANTMENTS.register(this.modEventBus);
        BrewingInit.POTIONS.register(this.modEventBus);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(HUDOverlayRenderer.class);
            MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
            MinecraftForge.EVENT_BUS.register(EffectParticleSpawner.class);
            MinecraftForge.EVENT_BUS.register(GuideBookEntries.class);
            this.modEventBus.addListener(this::clientInit);
            this.modEventBus.register(GuiInit.class);
            this.modEventBus.register(ParticleClientInit.class);
            this.modEventBus.register(TileEntityClientInit.class);
            this.modEventBus.register(BlockClientInit.class);
            this.modEventBus.register(ItemClientInit.class);
            this.modEventBus.register(ConstructModelRegistry.class);
            this.modEventBus.register(EntityClientInit.class);
            this.proxy = new ClientProxy();
        });
        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> this.proxy = new ServerProxy());
        MinecraftForge.EVENT_BUS.register(CommandInit.class);
        this.modEventBus.register(CommandSerializerInit.class);
        GeckoLib.initialize();
        try {
            URL url = new URL("https://manaandartifice.com/auras_enabled.txt");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setInstanceFollowRedirects(false);
            int status = con.getResponseCode();
            if (status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    try {
                        UUID uuid = UUID.fromString(inputLine);
                        this.enabled_auras.add(uuid);
                    } catch (Exception var8) {
                    }
                }
                in.close();
            }
            con.disconnect();
        } catch (Exception var9) {
        }
        LOGGER.info("M&A -> Mod Event Bus Handlers Registered");
    }

    @OnlyIn(Dist.CLIENT)
    public void clientInit(FMLClientSetupEvent event) {
        ItemProperties.register(ItemInit.THAUMATURGIC_COMPASS.get(), new ResourceLocation("angle"), new ClampedItemPropertyFunction() {

            private final ManaAndArtifice.Angle wobble = ManaAndArtifice.this.new Angle();

            private final ManaAndArtifice.Angle wobbleRandom = ManaAndArtifice.this.new Angle();

            @Override
            public float unclampedCall(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity living, int some_number) {
                Entity entity = (Entity) (living != null ? living : stack.getEntityRepresentation());
                if (entity == null) {
                    return 0.0F;
                } else {
                    if (world == null && entity.level() instanceof ClientLevel) {
                        world = (ClientLevel) entity.level();
                    }
                    BlockPos blockpos = ItemInit.THAUMATURGIC_COMPASS.get().getTrackedPosition(stack, world.m_46472_());
                    long i = world.m_46467_();
                    if (blockpos != null && !(entity.position().distanceToSqr((double) blockpos.m_123341_() + 0.5, entity.position().y(), (double) blockpos.m_123343_() + 0.5) < 1.0E-5F)) {
                        boolean flag = living instanceof Player && ((Player) living).isLocalPlayer();
                        double d1 = 0.0;
                        if (flag) {
                            d1 = (double) living.m_146908_();
                        } else if (entity instanceof ItemFrame) {
                            d1 = this.getFrameRotation((ItemFrame) entity);
                        } else if (entity instanceof ItemEntity) {
                            d1 = (double) (180.0F - ((ItemEntity) entity).getSpin(0.5F) / (float) (Math.PI * 2) * 360.0F);
                        } else if (living != null) {
                            d1 = (double) living.yBodyRot;
                        }
                        d1 = Mth.positiveModulo(d1 / 360.0, 1.0);
                        double d2 = this.getAngleTo(Vec3.atCenterOf(blockpos), entity) / (float) (Math.PI * 2);
                        double d3;
                        if (flag) {
                            if (this.wobble.shouldUpdate(i)) {
                                this.wobble.update(i, 0.5 - (d1 - 0.25));
                            }
                            d3 = d2 + this.wobble.rotation;
                        } else {
                            d3 = 0.5 - (d1 - 0.25 - d2);
                        }
                        return Mth.positiveModulo((float) d3, 1.0F);
                    } else {
                        if (this.wobbleRandom.shouldUpdate(i)) {
                            this.wobbleRandom.update(i, Math.random());
                        }
                        double d0 = this.wobbleRandom.rotation + (double) ((float) stack.hashCode() / 2.1474836E9F);
                        return Mth.positiveModulo((float) d0, 1.0F);
                    }
                }
            }

            private double getFrameRotation(ItemFrame p_239441_1_) {
                Direction direction = p_239441_1_.m_6350_();
                int i = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
                return (double) Mth.wrapDegrees(180 + direction.get2DDataValue() * 90 + p_239441_1_.getRotation() * 45 + i);
            }

            private double getAngleTo(Vec3 p_239443_1_, Entity p_239443_2_) {
                return Math.atan2(p_239443_1_.z() - p_239443_2_.getZ(), p_239443_1_.x() - p_239443_2_.getX());
            }
        });
        ItemProperties.register(ItemInit.HELLFIRE_TRIDENT.get(), RLoc.create("throwing"), new ClampedItemPropertyFunction() {

            @Override
            public float unclampedCall(ItemStack pStack, ClientLevel pLevel, LivingEntity pEntity, int pSeed) {
                return pEntity != null && pEntity.isUsingItem() ? 1.0F : 0.0F;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    class Angle {

        private double rotation;

        private double deltaRotation;

        private long lastUpdateTick;

        private Angle() {
        }

        private boolean shouldUpdate(long p_239448_1_) {
            return this.lastUpdateTick != p_239448_1_;
        }

        private void update(long p_239449_1_, double p_239449_3_) {
            this.lastUpdateTick = p_239449_1_;
            double d0 = p_239449_3_ - this.rotation;
            d0 = Mth.positiveModulo(d0 + 0.5, 1.0) - 0.5;
            this.deltaRotation += d0 * 0.1;
            this.deltaRotation *= 0.8;
            this.rotation = Mth.positiveModulo(this.rotation + this.deltaRotation, 1.0);
        }
    }
}