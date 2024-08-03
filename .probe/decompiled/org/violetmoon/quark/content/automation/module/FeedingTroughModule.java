package org.violetmoon.quark.content.automation.module;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.automation.block.FeedingTroughBlock;
import org.violetmoon.quark.content.automation.block.be.FeedingTroughBlockEntity;
import org.violetmoon.quark.content.automation.client.screen.TroughScreen;
import org.violetmoon.quark.mixin.mixins.accessor.AccessorTemptingSensor;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.ZEntityJoinLevel;
import org.violetmoon.zeta.event.play.entity.living.ZBabyEntitySpawn;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "automation")
public class FeedingTroughModule extends ZetaModule {

    private static final ResourceKey<PoiType> FEEDING_TROUGH_POI_KEY = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Quark.asResource("feeding_trough"));

    private static final Set<FakePlayer> FREE_FAKE_PLAYERS = new HashSet();

    private static final WeakHashMap<Animal, FeedingTroughModule.TroughPointer> NEARBY_TROUGH_CACHE = new WeakHashMap();

    private static final ThreadLocal<Boolean> breedingOccurred = ThreadLocal.withInitial(() -> false);

    private static int fakePlayersCount = 0;

    public static BlockEntityType<FeedingTroughBlockEntity> blockEntityType;

    public static MenuType<DispenserMenu> menuType;

    @Hint
    Block feeding_trough;

    @Config(description = "How long, in game ticks, between animals being able to eat from the trough")
    @Config.Min(1.0)
    public static int cooldown = 30;

    @Config(description = "The maximum amount of animals allowed around the trough's range for an animal to enter love mode")
    public static int maxAnimals = 32;

    @Config(description = "The chance (between 0 and 1) for an animal to enter love mode when eating from the trough")
    @Config.Min(value = 0.0, exclusive = true)
    @Config.Max(1.0)
    public static double loveChance = 0.333333333;

    @Config
    public static double range = 10.0;

    @Config(description = "Chance that an animal decides to look for a through. Closer it is to 1 the more performance it will take. Decreasing will make animals take longer to find one")
    public static double lookChance = 0.015;

    @PlayEvent
    public void onBreed(ZBabyEntitySpawn.Lowest event) {
        if (event.getCausedByPlayer() == null && event.getParentA().m_9236_().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            breedingOccurred.set(true);
        }
    }

    @PlayEvent
    public void onOrbSpawn(ZEntityJoinLevel event) {
        if (event.getEntity() instanceof ExperienceOrb && (Boolean) breedingOccurred.get()) {
            event.setCanceled(true);
            breedingOccurred.remove();
        }
    }

    @Nullable
    public static Player modifyTemptingSensor(TemptingSensor sensor, Animal animal, ServerLevel level) {
        return modifyTempt(level, animal, ((AccessorTemptingSensor) sensor).quark$getTemptations());
    }

    @Nullable
    public static Player modifyTemptGoal(TemptGoal goal, Animal animal, ServerLevel level) {
        return modifyTempt(level, animal, goal.items);
    }

    @Nullable
    private static Player modifyTempt(ServerLevel level, Animal animal, Ingredient temptations) {
        if (Quark.ZETA.modules.isEnabled(FeedingTroughModule.class) && animal.canFallInLove() && animal.m_146764_() == 0) {
            Iterator<Entry<Animal, FeedingTroughModule.TroughPointer>> iterator = NEARBY_TROUGH_CACHE.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<Animal, FeedingTroughModule.TroughPointer> entry = (Entry<Animal, FeedingTroughModule.TroughPointer>) iterator.next();
                FeedingTroughModule.TroughPointer pointer = (FeedingTroughModule.TroughPointer) entry.getValue();
                if (!pointer.valid((Animal) entry.getKey())) {
                    iterator.remove();
                    FREE_FAKE_PLAYERS.add(pointer.fakePlayer);
                }
            }
            FeedingTroughModule.TroughPointer pointer = (FeedingTroughModule.TroughPointer) NEARBY_TROUGH_CACHE.get(animal);
            if (pointer == null && (double) level.f_46441_.nextFloat() <= lookChance * 20.0) {
                pointer = FeedingTroughModule.TroughPointer.find(level, animal, temptations);
                if (pointer != null) {
                    NEARBY_TROUGH_CACHE.put(animal, pointer);
                    FREE_FAKE_PLAYERS.remove(pointer.fakePlayer);
                }
            }
            if (pointer != null) {
                pointer.tryEatingOrTickCooldown(animal);
                if (!pointer.isOnCooldown()) {
                    BlockPos location = pointer.pos;
                    Vec3 eyesPos = animal.m_20182_().add(0.0, (double) animal.m_20192_(), 0.0);
                    Vec3 targetPos = new Vec3((double) location.m_123341_(), (double) location.m_123342_(), (double) location.m_123343_()).add(0.5, 0.0625, 0.5);
                    BlockHitResult ray = level.m_45547_(new ClipContext(eyesPos, targetPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, animal));
                    if (ray.getType() == HitResult.Type.BLOCK && ray.getBlockPos().equals(location)) {
                        return pointer.fakePlayer;
                    }
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @LoadEvent
    public final void register(ZRegister event) {
        this.feeding_trough = new FeedingTroughBlock("feeding_trough", this, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().strength(0.6F).sound(SoundType.WOOD));
        blockEntityType = BlockEntityType.Builder.<FeedingTroughBlockEntity>of(FeedingTroughBlockEntity::new, this.feeding_trough).build(null);
        event.getRegistry().register(blockEntityType, "feeding_trough", Registries.BLOCK_ENTITY_TYPE);
        PoiType feedingTroughPoi = new PoiType(ImmutableSet.copyOf(this.feeding_trough.getStateDefinition().getPossibleStates()), 1, 32);
        event.getRegistry().register(feedingTroughPoi, FEEDING_TROUGH_POI_KEY.location(), Registries.POINT_OF_INTEREST_TYPE);
        menuType = IForgeMenuType.create((windowId, inv, data) -> new DispenserMenu(windowId, inv));
        event.getRegistry().register(menuType, "feeding_trough", Registries.MENU);
    }

    @LoadEvent
    public final void clientSetup(ZClientSetup event) {
        event.enqueueWork(() -> MenuScreens.register(menuType, TroughScreen::new));
    }

    private static FakePlayer getOrCreateFakePlayer(ServerLevel serverLevel) {
        Optional<FakePlayer> any = FREE_FAKE_PLAYERS.stream().findAny();
        if (any.isEmpty()) {
            GameProfile dummyProfile = new GameProfile(UUID.randomUUID(), "[FeedingTrough-" + ++fakePlayersCount + "]");
            FakePlayer p = FakePlayerFactory.get(serverLevel, dummyProfile);
            FREE_FAKE_PLAYERS.add(p);
            return p;
        } else {
            return (FakePlayer) any.get();
        }
    }

    private static final class TroughPointer {

        private final BlockPos pos;

        private final FakePlayer fakePlayer;

        private final Ingredient temptations;

        private int eatCooldown = 0;

        private int giveUpCooldown = 400;

        private TroughPointer(BlockPos pos, FakePlayer player, Ingredient temptations) {
            this.pos = pos;
            this.fakePlayer = player;
            this.temptations = temptations;
        }

        boolean valid(Animal animal) {
            if (animal.m_213877_() || !animal.m_6084_() || this.fakePlayer.m_9236_() != animal.m_9236_() || this.pos.m_123331_(animal.m_20183_()) > FeedingTroughModule.range * FeedingTroughModule.range) {
                return false;
            } else if (this.eatCooldown == 1) {
                return false;
            } else if (this.giveUpCooldown <= 0) {
                return false;
            } else if (this.eatCooldown != 0) {
                return true;
            } else if (animal.m_9236_().getBlockEntity(this.pos) instanceof FeedingTroughBlockEntity trough) {
                trough.updateFoodHolder(animal, this.temptations, this.fakePlayer);
                return !this.fakePlayer.m_21205_().isEmpty();
            } else {
                return false;
            }
        }

        void tryEatingOrTickCooldown(Animal animal) {
            this.giveUpCooldown--;
            if (this.eatCooldown == 0) {
                float feedDistance = 0.5F + animal.m_20205_() * 1.8F;
                if (this.pos.m_203193_(animal.m_20182_()) < (double) (feedDistance * feedDistance) && animal.m_9236_().getBlockEntity(this.pos) instanceof FeedingTroughBlockEntity trough) {
                    switch(trough.tryFeedingAnimal(animal)) {
                        case FED:
                            this.eatCooldown = FeedingTroughModule.cooldown;
                            break;
                        case SECS:
                            this.eatCooldown = 1;
                    }
                }
            } else {
                this.eatCooldown--;
            }
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj != null && obj.getClass() == this.getClass()) {
                FeedingTroughModule.TroughPointer that = (FeedingTroughModule.TroughPointer) obj;
                return Objects.equals(this.pos, that.pos) && Objects.equals(this.fakePlayer, that.fakePlayer);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.pos, this.fakePlayer });
        }

        public boolean isOnCooldown() {
            return this.eatCooldown != 0;
        }

        @Nullable
        static FeedingTroughModule.TroughPointer find(ServerLevel level, Animal animal, Ingredient temptations) {
            BlockPos position = animal.m_20097_();
            Optional<BlockPos> opt = level.getPoiManager().findClosest(holder -> holder.is(FeedingTroughModule.FEEDING_TROUGH_POI_KEY), p -> p.m_123331_(position) <= FeedingTroughModule.range * FeedingTroughModule.range, position, (int) FeedingTroughModule.range, PoiManager.Occupancy.ANY);
            if (opt.isPresent()) {
                BlockPos pos = (BlockPos) opt.get();
                if (level.m_7702_(pos) instanceof FeedingTroughBlockEntity trough) {
                    FakePlayer foodHolder = FeedingTroughModule.getOrCreateFakePlayer(level);
                    if (foodHolder != null) {
                        trough.updateFoodHolder(animal, temptations, foodHolder);
                        if (!foodHolder.m_21205_().isEmpty()) {
                            return new FeedingTroughModule.TroughPointer(pos, foodHolder, temptations);
                        }
                    }
                    return null;
                }
            }
            return null;
        }
    }
}