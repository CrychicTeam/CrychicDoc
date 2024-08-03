package net.minecraft.gametest.framework;

import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.LongStream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class GameTestHelper {

    private final GameTestInfo testInfo;

    private boolean finalCheckAdded;

    public GameTestHelper(GameTestInfo gameTestInfo0) {
        this.testInfo = gameTestInfo0;
    }

    public ServerLevel getLevel() {
        return this.testInfo.getLevel();
    }

    public BlockState getBlockState(BlockPos blockPos0) {
        return this.getLevel().m_8055_(this.absolutePos(blockPos0));
    }

    @Nullable
    public BlockEntity getBlockEntity(BlockPos blockPos0) {
        return this.getLevel().m_7702_(this.absolutePos(blockPos0));
    }

    public void killAllEntities() {
        this.killAllEntitiesOfClass(Entity.class);
    }

    public void killAllEntitiesOfClass(Class class0) {
        AABB $$1 = this.getBounds();
        List<Entity> $$2 = this.getLevel().m_6443_(class0, $$1.inflate(1.0), p_177131_ -> !(p_177131_ instanceof Player));
        $$2.forEach(Entity::m_6074_);
    }

    public ItemEntity spawnItem(Item item0, float float1, float float2, float float3) {
        ServerLevel $$4 = this.getLevel();
        Vec3 $$5 = this.absoluteVec(new Vec3((double) float1, (double) float2, (double) float3));
        ItemEntity $$6 = new ItemEntity($$4, $$5.x, $$5.y, $$5.z, new ItemStack(item0, 1));
        $$6.m_20334_(0.0, 0.0, 0.0);
        $$4.addFreshEntity($$6);
        return $$6;
    }

    public ItemEntity spawnItem(Item item0, BlockPos blockPos1) {
        return this.spawnItem(item0, (float) blockPos1.m_123341_(), (float) blockPos1.m_123342_(), (float) blockPos1.m_123343_());
    }

    public <E extends Entity> E spawn(EntityType<E> entityTypeE0, BlockPos blockPos1) {
        return this.spawn(entityTypeE0, Vec3.atBottomCenterOf(blockPos1));
    }

    public <E extends Entity> E spawn(EntityType<E> entityTypeE0, Vec3 vec1) {
        ServerLevel $$2 = this.getLevel();
        E $$3 = entityTypeE0.create($$2);
        if ($$3 == null) {
            throw new NullPointerException("Failed to create entity " + entityTypeE0.builtInRegistryHolder().key().location());
        } else {
            if ($$3 instanceof Mob $$4) {
                $$4.setPersistenceRequired();
            }
            Vec3 $$5 = this.absoluteVec(vec1);
            $$3.moveTo($$5.x, $$5.y, $$5.z, $$3.getYRot(), $$3.getXRot());
            $$2.addFreshEntity($$3);
            return $$3;
        }
    }

    public <E extends Entity> E spawn(EntityType<E> entityTypeE0, int int1, int int2, int int3) {
        return this.spawn(entityTypeE0, new BlockPos(int1, int2, int3));
    }

    public <E extends Entity> E spawn(EntityType<E> entityTypeE0, float float1, float float2, float float3) {
        return this.spawn(entityTypeE0, new Vec3((double) float1, (double) float2, (double) float3));
    }

    public <E extends Mob> E spawnWithNoFreeWill(EntityType<E> entityTypeE0, BlockPos blockPos1) {
        E $$2 = (E) this.spawn(entityTypeE0, blockPos1);
        $$2.removeFreeWill();
        return $$2;
    }

    public <E extends Mob> E spawnWithNoFreeWill(EntityType<E> entityTypeE0, int int1, int int2, int int3) {
        return this.spawnWithNoFreeWill(entityTypeE0, new BlockPos(int1, int2, int3));
    }

    public <E extends Mob> E spawnWithNoFreeWill(EntityType<E> entityTypeE0, Vec3 vec1) {
        E $$2 = (E) this.spawn(entityTypeE0, vec1);
        $$2.removeFreeWill();
        return $$2;
    }

    public <E extends Mob> E spawnWithNoFreeWill(EntityType<E> entityTypeE0, float float1, float float2, float float3) {
        return this.spawnWithNoFreeWill(entityTypeE0, new Vec3((double) float1, (double) float2, (double) float3));
    }

    public GameTestSequence walkTo(Mob mob0, BlockPos blockPos1, float float2) {
        return this.startSequence().thenExecuteAfter(2, () -> {
            Path $$3 = mob0.getNavigation().createPath(this.absolutePos(blockPos1), 0);
            mob0.getNavigation().moveTo($$3, (double) float2);
        });
    }

    public void pressButton(int int0, int int1, int int2) {
        this.pressButton(new BlockPos(int0, int1, int2));
    }

    public void pressButton(BlockPos blockPos0) {
        this.assertBlockState(blockPos0, p_177212_ -> p_177212_.m_204336_(BlockTags.BUTTONS), () -> "Expected button");
        BlockPos $$1 = this.absolutePos(blockPos0);
        BlockState $$2 = this.getLevel().m_8055_($$1);
        ButtonBlock $$3 = (ButtonBlock) $$2.m_60734_();
        $$3.press($$2, this.getLevel(), $$1);
    }

    public void useBlock(BlockPos blockPos0) {
        this.useBlock(blockPos0, this.makeMockPlayer());
    }

    public void useBlock(BlockPos blockPos0, Player player1) {
        BlockPos $$2 = this.absolutePos(blockPos0);
        this.useBlock(blockPos0, player1, new BlockHitResult(Vec3.atCenterOf($$2), Direction.NORTH, $$2, true));
    }

    public void useBlock(BlockPos blockPos0, Player player1, BlockHitResult blockHitResult2) {
        BlockPos $$3 = this.absolutePos(blockPos0);
        BlockState $$4 = this.getLevel().m_8055_($$3);
        InteractionResult $$5 = $$4.m_60664_(this.getLevel(), player1, InteractionHand.MAIN_HAND, blockHitResult2);
        if (!$$5.consumesAction()) {
            UseOnContext $$6 = new UseOnContext(player1, InteractionHand.MAIN_HAND, blockHitResult2);
            player1.m_21120_(InteractionHand.MAIN_HAND).useOn($$6);
        }
    }

    public LivingEntity makeAboutToDrown(LivingEntity livingEntity0) {
        livingEntity0.m_20301_(0);
        livingEntity0.setHealth(0.25F);
        return livingEntity0;
    }

    public Player makeMockSurvivalPlayer() {
        return new Player(this.getLevel(), BlockPos.ZERO, 0.0F, new GameProfile(UUID.randomUUID(), "test-mock-player")) {

            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return false;
            }
        };
    }

    public LivingEntity withLowHealth(LivingEntity livingEntity0) {
        livingEntity0.setHealth(0.25F);
        return livingEntity0;
    }

    public Player makeMockPlayer() {
        return new Player(this.getLevel(), BlockPos.ZERO, 0.0F, new GameProfile(UUID.randomUUID(), "test-mock-player")) {

            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return true;
            }

            @Override
            public boolean isLocalPlayer() {
                return true;
            }
        };
    }

    public ServerPlayer makeMockServerPlayerInLevel() {
        ServerPlayer $$0 = new ServerPlayer(this.getLevel().getServer(), this.getLevel(), new GameProfile(UUID.randomUUID(), "test-mock-player")) {

            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return true;
            }
        };
        this.getLevel().getServer().getPlayerList().placeNewPlayer(new Connection(PacketFlow.SERVERBOUND), $$0);
        return $$0;
    }

    public void pullLever(int int0, int int1, int int2) {
        this.pullLever(new BlockPos(int0, int1, int2));
    }

    public void pullLever(BlockPos blockPos0) {
        this.assertBlockPresent(Blocks.LEVER, blockPos0);
        BlockPos $$1 = this.absolutePos(blockPos0);
        BlockState $$2 = this.getLevel().m_8055_($$1);
        LeverBlock $$3 = (LeverBlock) $$2.m_60734_();
        $$3.pull($$2, this.getLevel(), $$1);
    }

    public void pulseRedstone(BlockPos blockPos0, long long1) {
        this.setBlock(blockPos0, Blocks.REDSTONE_BLOCK);
        this.runAfterDelay(long1, () -> this.setBlock(blockPos0, Blocks.AIR));
    }

    public void destroyBlock(BlockPos blockPos0) {
        this.getLevel().m_46953_(this.absolutePos(blockPos0), false, null);
    }

    public void setBlock(int int0, int int1, int int2, Block block3) {
        this.setBlock(new BlockPos(int0, int1, int2), block3);
    }

    public void setBlock(int int0, int int1, int int2, BlockState blockState3) {
        this.setBlock(new BlockPos(int0, int1, int2), blockState3);
    }

    public void setBlock(BlockPos blockPos0, Block block1) {
        this.setBlock(blockPos0, block1.defaultBlockState());
    }

    public void setBlock(BlockPos blockPos0, BlockState blockState1) {
        this.getLevel().m_7731_(this.absolutePos(blockPos0), blockState1, 3);
    }

    public void setNight() {
        this.setDayTime(13000);
    }

    public void setDayTime(int int0) {
        this.getLevel().setDayTime((long) int0);
    }

    public void assertBlockPresent(Block block0, int int1, int int2, int int3) {
        this.assertBlockPresent(block0, new BlockPos(int1, int2, int3));
    }

    public void assertBlockPresent(Block block0, BlockPos blockPos1) {
        BlockState $$2 = this.getBlockState(blockPos1);
        this.assertBlock(blockPos1, p_177216_ -> $$2.m_60713_(block0), "Expected " + block0.getName().getString() + ", got " + $$2.m_60734_().getName().getString());
    }

    public void assertBlockNotPresent(Block block0, int int1, int int2, int int3) {
        this.assertBlockNotPresent(block0, new BlockPos(int1, int2, int3));
    }

    public void assertBlockNotPresent(Block block0, BlockPos blockPos1) {
        this.assertBlock(blockPos1, p_177251_ -> !this.getBlockState(blockPos1).m_60713_(block0), "Did not expect " + block0.getName().getString());
    }

    public void succeedWhenBlockPresent(Block block0, int int1, int int2, int int3) {
        this.succeedWhenBlockPresent(block0, new BlockPos(int1, int2, int3));
    }

    public void succeedWhenBlockPresent(Block block0, BlockPos blockPos1) {
        this.succeedWhen(() -> this.assertBlockPresent(block0, blockPos1));
    }

    public void assertBlock(BlockPos blockPos0, Predicate<Block> predicateBlock1, String string2) {
        this.assertBlock(blockPos0, predicateBlock1, (Supplier<String>) (() -> string2));
    }

    public void assertBlock(BlockPos blockPos0, Predicate<Block> predicateBlock1, Supplier<String> supplierString2) {
        this.assertBlockState(blockPos0, p_177296_ -> predicateBlock1.test(p_177296_.m_60734_()), supplierString2);
    }

    public <T extends Comparable<T>> void assertBlockProperty(BlockPos blockPos0, Property<T> propertyT1, T t2) {
        BlockState $$3 = this.getBlockState(blockPos0);
        boolean $$4 = $$3.m_61138_(propertyT1);
        if (!$$4 || !$$3.m_61143_(propertyT1).equals(t2)) {
            String $$5 = $$4 ? "was " + $$3.m_61143_(propertyT1) : "property " + propertyT1.getName() + " is missing";
            String $$6 = String.format(Locale.ROOT, "Expected property %s to be %s, %s", propertyT1.getName(), t2, $$5);
            throw new GameTestAssertPosException($$6, this.absolutePos(blockPos0), blockPos0, this.testInfo.getTick());
        }
    }

    public <T extends Comparable<T>> void assertBlockProperty(BlockPos blockPos0, Property<T> propertyT1, Predicate<T> predicateT2, String string3) {
        this.assertBlockState(blockPos0, p_277264_ -> {
            if (!p_277264_.m_61138_(propertyT1)) {
                return false;
            } else {
                T $$3 = (T) p_277264_.m_61143_(propertyT1);
                return predicateT2.test($$3);
            }
        }, () -> string3);
    }

    public void assertBlockState(BlockPos blockPos0, Predicate<BlockState> predicateBlockState1, Supplier<String> supplierString2) {
        BlockState $$3 = this.getBlockState(blockPos0);
        if (!predicateBlockState1.test($$3)) {
            throw new GameTestAssertPosException((String) supplierString2.get(), this.absolutePos(blockPos0), blockPos0, this.testInfo.getTick());
        }
    }

    public void assertRedstoneSignal(BlockPos blockPos0, Direction direction1, IntPredicate intPredicate2, Supplier<String> supplierString3) {
        BlockPos $$4 = this.absolutePos(blockPos0);
        ServerLevel $$5 = this.getLevel();
        BlockState $$6 = $$5.m_8055_($$4);
        int $$7 = $$6.m_60746_($$5, $$4, direction1);
        if (!intPredicate2.test($$7)) {
            throw new GameTestAssertPosException((String) supplierString3.get(), $$4, blockPos0, this.testInfo.getTick());
        }
    }

    public void assertEntityPresent(EntityType<?> entityType0) {
        List<? extends Entity> $$1 = this.getLevel().m_142425_(entityType0, this.getBounds(), Entity::m_6084_);
        if ($$1.isEmpty()) {
            throw new GameTestAssertException("Expected " + entityType0.toShortString() + " to exist");
        }
    }

    public void assertEntityPresent(EntityType<?> entityType0, int int1, int int2, int int3) {
        this.assertEntityPresent(entityType0, new BlockPos(int1, int2, int3));
    }

    public void assertEntityPresent(EntityType<?> entityType0, BlockPos blockPos1) {
        BlockPos $$2 = this.absolutePos(blockPos1);
        List<? extends Entity> $$3 = this.getLevel().m_142425_(entityType0, new AABB($$2), Entity::m_6084_);
        if ($$3.isEmpty()) {
            throw new GameTestAssertPosException("Expected " + entityType0.toShortString(), $$2, blockPos1, this.testInfo.getTick());
        }
    }

    public void assertEntityPresent(EntityType<?> entityType0, Vec3 vec1, Vec3 vec2) {
        List<? extends Entity> $$3 = this.getLevel().m_142425_(entityType0, new AABB(vec1, vec2), Entity::m_6084_);
        if ($$3.isEmpty()) {
            throw new GameTestAssertPosException("Expected " + entityType0.toShortString() + " between ", BlockPos.containing(vec1), BlockPos.containing(vec2), this.testInfo.getTick());
        }
    }

    public void assertEntitiesPresent(EntityType<?> entityType0, BlockPos blockPos1, int int2, double double3) {
        BlockPos $$4 = this.absolutePos(blockPos1);
        List<? extends Entity> $$5 = this.getEntities((EntityType<? extends Entity>) entityType0, blockPos1, double3);
        if ($$5.size() != int2) {
            throw new GameTestAssertPosException("Expected " + int2 + " entities of type " + entityType0.toShortString() + ", actual number of entities found=" + $$5.size(), $$4, blockPos1, this.testInfo.getTick());
        }
    }

    public void assertEntityPresent(EntityType<?> entityType0, BlockPos blockPos1, double double2) {
        List<? extends Entity> $$3 = this.getEntities((EntityType<? extends Entity>) entityType0, blockPos1, double2);
        if ($$3.isEmpty()) {
            BlockPos $$4 = this.absolutePos(blockPos1);
            throw new GameTestAssertPosException("Expected " + entityType0.toShortString(), $$4, blockPos1, this.testInfo.getTick());
        }
    }

    public <T extends Entity> List<T> getEntities(EntityType<T> entityTypeT0, BlockPos blockPos1, double double2) {
        BlockPos $$3 = this.absolutePos(blockPos1);
        return this.getLevel().m_142425_(entityTypeT0, new AABB($$3).inflate(double2), Entity::m_6084_);
    }

    public void assertEntityInstancePresent(Entity entity0, int int1, int int2, int int3) {
        this.assertEntityInstancePresent(entity0, new BlockPos(int1, int2, int3));
    }

    public void assertEntityInstancePresent(Entity entity0, BlockPos blockPos1) {
        BlockPos $$2 = this.absolutePos(blockPos1);
        List<? extends Entity> $$3 = this.getLevel().m_142425_(entity0.getType(), new AABB($$2), Entity::m_6084_);
        $$3.stream().filter(p_177139_ -> p_177139_ == entity0).findFirst().orElseThrow(() -> new GameTestAssertPosException("Expected " + entity0.getType().toShortString(), $$2, blockPos1, this.testInfo.getTick()));
    }

    public void assertItemEntityCountIs(Item item0, BlockPos blockPos1, double double2, int int3) {
        BlockPos $$4 = this.absolutePos(blockPos1);
        List<ItemEntity> $$5 = this.getLevel().m_142425_(EntityType.ITEM, new AABB($$4).inflate(double2), Entity::m_6084_);
        int $$6 = 0;
        for (ItemEntity $$7 : $$5) {
            ItemStack $$8 = $$7.getItem();
            if ($$8.is(item0)) {
                $$6 += $$8.getCount();
            }
        }
        if ($$6 != int3) {
            throw new GameTestAssertPosException("Expected " + int3 + " " + item0.getDescription().getString() + " items to exist (found " + $$6 + ")", $$4, blockPos1, this.testInfo.getTick());
        }
    }

    public void assertItemEntityPresent(Item item0, BlockPos blockPos1, double double2) {
        BlockPos $$3 = this.absolutePos(blockPos1);
        for (Entity $$5 : this.getLevel().m_142425_(EntityType.ITEM, new AABB($$3).inflate(double2), Entity::m_6084_)) {
            ItemEntity $$6 = (ItemEntity) $$5;
            if ($$6.getItem().getItem().equals(item0)) {
                return;
            }
        }
        throw new GameTestAssertPosException("Expected " + item0.getDescription().getString() + " item", $$3, blockPos1, this.testInfo.getTick());
    }

    public void assertItemEntityNotPresent(Item item0, BlockPos blockPos1, double double2) {
        BlockPos $$3 = this.absolutePos(blockPos1);
        for (Entity $$5 : this.getLevel().m_142425_(EntityType.ITEM, new AABB($$3).inflate(double2), Entity::m_6084_)) {
            ItemEntity $$6 = (ItemEntity) $$5;
            if ($$6.getItem().getItem().equals(item0)) {
                throw new GameTestAssertPosException("Did not expect " + item0.getDescription().getString() + " item", $$3, blockPos1, this.testInfo.getTick());
            }
        }
    }

    public void assertEntityNotPresent(EntityType<?> entityType0) {
        List<? extends Entity> $$1 = this.getLevel().m_142425_(entityType0, this.getBounds(), Entity::m_6084_);
        if (!$$1.isEmpty()) {
            throw new GameTestAssertException("Did not expect " + entityType0.toShortString() + " to exist");
        }
    }

    public void assertEntityNotPresent(EntityType<?> entityType0, int int1, int int2, int int3) {
        this.assertEntityNotPresent(entityType0, new BlockPos(int1, int2, int3));
    }

    public void assertEntityNotPresent(EntityType<?> entityType0, BlockPos blockPos1) {
        BlockPos $$2 = this.absolutePos(blockPos1);
        List<? extends Entity> $$3 = this.getLevel().m_142425_(entityType0, new AABB($$2), Entity::m_6084_);
        if (!$$3.isEmpty()) {
            throw new GameTestAssertPosException("Did not expect " + entityType0.toShortString(), $$2, blockPos1, this.testInfo.getTick());
        }
    }

    public void assertEntityTouching(EntityType<?> entityType0, double double1, double double2, double double3) {
        Vec3 $$4 = new Vec3(double1, double2, double3);
        Vec3 $$5 = this.absoluteVec($$4);
        Predicate<? super Entity> $$6 = p_177346_ -> p_177346_.getBoundingBox().intersects($$5, $$5);
        List<? extends Entity> $$7 = this.getLevel().m_142425_(entityType0, this.getBounds(), $$6);
        if ($$7.isEmpty()) {
            throw new GameTestAssertException("Expected " + entityType0.toShortString() + " to touch " + $$5 + " (relative " + $$4 + ")");
        }
    }

    public void assertEntityNotTouching(EntityType<?> entityType0, double double1, double double2, double double3) {
        Vec3 $$4 = new Vec3(double1, double2, double3);
        Vec3 $$5 = this.absoluteVec($$4);
        Predicate<? super Entity> $$6 = p_177231_ -> !p_177231_.getBoundingBox().intersects($$5, $$5);
        List<? extends Entity> $$7 = this.getLevel().m_142425_(entityType0, this.getBounds(), $$6);
        if ($$7.isEmpty()) {
            throw new GameTestAssertException("Did not expect " + entityType0.toShortString() + " to touch " + $$5 + " (relative " + $$4 + ")");
        }
    }

    public <E extends Entity, T> void assertEntityData(BlockPos blockPos0, EntityType<E> entityTypeE1, Function<? super E, T> functionSuperET2, @Nullable T t3) {
        BlockPos $$4 = this.absolutePos(blockPos0);
        List<E> $$5 = this.getLevel().m_142425_(entityTypeE1, new AABB($$4), Entity::m_6084_);
        if ($$5.isEmpty()) {
            throw new GameTestAssertPosException("Expected " + entityTypeE1.toShortString(), $$4, blockPos0, this.testInfo.getTick());
        } else {
            for (E $$6 : $$5) {
                T $$7 = (T) functionSuperET2.apply($$6);
                if ($$7 == null) {
                    if (t3 != null) {
                        throw new GameTestAssertException("Expected entity data to be: " + t3 + ", but was: " + $$7);
                    }
                } else if (!$$7.equals(t3)) {
                    throw new GameTestAssertException("Expected entity data to be: " + t3 + ", but was: " + $$7);
                }
            }
        }
    }

    public <E extends LivingEntity> void assertEntityIsHolding(BlockPos blockPos0, EntityType<E> entityTypeE1, Item item2) {
        BlockPos $$3 = this.absolutePos(blockPos0);
        List<E> $$4 = this.getLevel().m_142425_(entityTypeE1, new AABB($$3), Entity::m_6084_);
        if ($$4.isEmpty()) {
            throw new GameTestAssertPosException("Expected entity of type: " + entityTypeE1, $$3, blockPos0, this.getTick());
        } else {
            for (E $$5 : $$4) {
                if ($$5.isHolding(item2)) {
                    return;
                }
            }
            throw new GameTestAssertPosException("Entity should be holding: " + item2, $$3, blockPos0, this.getTick());
        }
    }

    public <E extends Entity & InventoryCarrier> void assertEntityInventoryContains(BlockPos blockPos0, EntityType<E> entityTypeE1, Item item2) {
        BlockPos $$3 = this.absolutePos(blockPos0);
        List<E> $$4 = this.getLevel().m_142425_(entityTypeE1, new AABB($$3), p_263479_ -> ((Entity) p_263479_).isAlive());
        if ($$4.isEmpty()) {
            throw new GameTestAssertPosException("Expected " + entityTypeE1.toShortString() + " to exist", $$3, blockPos0, this.getTick());
        } else {
            for (E $$5 : $$4) {
                if ($$5.getInventory().m_216874_(p_263481_ -> p_263481_.is(item2))) {
                    return;
                }
            }
            throw new GameTestAssertPosException("Entity inventory should contain: " + item2, $$3, blockPos0, this.getTick());
        }
    }

    public void assertContainerEmpty(BlockPos blockPos0) {
        BlockPos $$1 = this.absolutePos(blockPos0);
        BlockEntity $$2 = this.getLevel().m_7702_($$1);
        if ($$2 instanceof BaseContainerBlockEntity && !((BaseContainerBlockEntity) $$2).m_7983_()) {
            throw new GameTestAssertException("Container should be empty");
        }
    }

    public void assertContainerContains(BlockPos blockPos0, Item item1) {
        BlockPos $$2 = this.absolutePos(blockPos0);
        BlockEntity $$3 = this.getLevel().m_7702_($$2);
        if (!($$3 instanceof BaseContainerBlockEntity)) {
            throw new GameTestAssertException("Expected a container at " + blockPos0 + ", found " + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey($$3.getType()));
        } else if (((BaseContainerBlockEntity) $$3).m_18947_(item1) != 1) {
            throw new GameTestAssertException("Container should contain: " + item1);
        }
    }

    public void assertSameBlockStates(BoundingBox boundingBox0, BlockPos blockPos1) {
        BlockPos.betweenClosedStream(boundingBox0).forEach(p_177267_ -> {
            BlockPos $$3 = blockPos1.offset(p_177267_.m_123341_() - boundingBox0.minX(), p_177267_.m_123342_() - boundingBox0.minY(), p_177267_.m_123343_() - boundingBox0.minZ());
            this.assertSameBlockState(p_177267_, $$3);
        });
    }

    public void assertSameBlockState(BlockPos blockPos0, BlockPos blockPos1) {
        BlockState $$2 = this.getBlockState(blockPos0);
        BlockState $$3 = this.getBlockState(blockPos1);
        if ($$2 != $$3) {
            this.fail("Incorrect state. Expected " + $$3 + ", got " + $$2, blockPos0);
        }
    }

    public void assertAtTickTimeContainerContains(long long0, BlockPos blockPos1, Item item2) {
        this.runAtTickTime(long0, () -> this.assertContainerContains(blockPos1, item2));
    }

    public void assertAtTickTimeContainerEmpty(long long0, BlockPos blockPos1) {
        this.runAtTickTime(long0, () -> this.assertContainerEmpty(blockPos1));
    }

    public <E extends Entity, T> void succeedWhenEntityData(BlockPos blockPos0, EntityType<E> entityTypeE1, Function<E, T> functionET2, T t3) {
        this.succeedWhen(() -> this.assertEntityData(blockPos0, entityTypeE1, functionET2, t3));
    }

    public <E extends Entity> void assertEntityProperty(E e0, Predicate<E> predicateE1, String string2) {
        if (!predicateE1.test(e0)) {
            throw new GameTestAssertException("Entity " + e0 + " failed " + string2 + " test");
        }
    }

    public <E extends Entity, T> void assertEntityProperty(E e0, Function<E, T> functionET1, String string2, T t3) {
        T $$4 = (T) functionET1.apply(e0);
        if (!$$4.equals(t3)) {
            throw new GameTestAssertException("Entity " + e0 + " value " + string2 + "=" + $$4 + " is not equal to expected " + t3);
        }
    }

    public void succeedWhenEntityPresent(EntityType<?> entityType0, int int1, int int2, int int3) {
        this.succeedWhenEntityPresent(entityType0, new BlockPos(int1, int2, int3));
    }

    public void succeedWhenEntityPresent(EntityType<?> entityType0, BlockPos blockPos1) {
        this.succeedWhen(() -> this.assertEntityPresent(entityType0, blockPos1));
    }

    public void succeedWhenEntityNotPresent(EntityType<?> entityType0, int int1, int int2, int int3) {
        this.succeedWhenEntityNotPresent(entityType0, new BlockPos(int1, int2, int3));
    }

    public void succeedWhenEntityNotPresent(EntityType<?> entityType0, BlockPos blockPos1) {
        this.succeedWhen(() -> this.assertEntityNotPresent(entityType0, blockPos1));
    }

    public void succeed() {
        this.testInfo.succeed();
    }

    private void ensureSingleFinalCheck() {
        if (this.finalCheckAdded) {
            throw new IllegalStateException("This test already has final clause");
        } else {
            this.finalCheckAdded = true;
        }
    }

    public void succeedIf(Runnable runnable0) {
        this.ensureSingleFinalCheck();
        this.testInfo.createSequence().thenWaitUntil(0L, runnable0).thenSucceed();
    }

    public void succeedWhen(Runnable runnable0) {
        this.ensureSingleFinalCheck();
        this.testInfo.createSequence().thenWaitUntil(runnable0).thenSucceed();
    }

    public void succeedOnTickWhen(int int0, Runnable runnable1) {
        this.ensureSingleFinalCheck();
        this.testInfo.createSequence().thenWaitUntil((long) int0, runnable1).thenSucceed();
    }

    public void runAtTickTime(long long0, Runnable runnable1) {
        this.testInfo.setRunAtTickTime(long0, runnable1);
    }

    public void runAfterDelay(long long0, Runnable runnable1) {
        this.runAtTickTime(this.testInfo.getTick() + long0, runnable1);
    }

    public void randomTick(BlockPos blockPos0) {
        BlockPos $$1 = this.absolutePos(blockPos0);
        ServerLevel $$2 = this.getLevel();
        $$2.m_8055_($$1).m_222972_($$2, $$1, $$2.f_46441_);
    }

    public int getHeight(Heightmap.Types heightmapTypes0, int int1, int int2) {
        BlockPos $$3 = this.absolutePos(new BlockPos(int1, 0, int2));
        return this.relativePos(this.getLevel().m_5452_(heightmapTypes0, $$3)).m_123342_();
    }

    public void fail(String string0, BlockPos blockPos1) {
        throw new GameTestAssertPosException(string0, this.absolutePos(blockPos1), blockPos1, this.getTick());
    }

    public void fail(String string0, Entity entity1) {
        throw new GameTestAssertPosException(string0, entity1.blockPosition(), this.relativePos(entity1.blockPosition()), this.getTick());
    }

    public void fail(String string0) {
        throw new GameTestAssertException(string0);
    }

    public void failIf(Runnable runnable0) {
        this.testInfo.createSequence().thenWaitUntil(runnable0).thenFail(() -> new GameTestAssertException("Fail conditions met"));
    }

    public void failIfEver(Runnable runnable0) {
        LongStream.range(this.testInfo.getTick(), (long) this.testInfo.getTimeoutTicks()).forEach(p_177365_ -> this.testInfo.setRunAtTickTime(p_177365_, runnable0::run));
    }

    public GameTestSequence startSequence() {
        return this.testInfo.createSequence();
    }

    public BlockPos absolutePos(BlockPos blockPos0) {
        BlockPos $$1 = this.testInfo.getStructureBlockPos();
        BlockPos $$2 = $$1.offset(blockPos0);
        return StructureTemplate.transform($$2, Mirror.NONE, this.testInfo.getRotation(), $$1);
    }

    public BlockPos relativePos(BlockPos blockPos0) {
        BlockPos $$1 = this.testInfo.getStructureBlockPos();
        Rotation $$2 = this.testInfo.getRotation().getRotated(Rotation.CLOCKWISE_180);
        BlockPos $$3 = StructureTemplate.transform(blockPos0, Mirror.NONE, $$2, $$1);
        return $$3.subtract($$1);
    }

    public Vec3 absoluteVec(Vec3 vec0) {
        Vec3 $$1 = Vec3.atLowerCornerOf(this.testInfo.getStructureBlockPos());
        return StructureTemplate.transform($$1.add(vec0), Mirror.NONE, this.testInfo.getRotation(), this.testInfo.getStructureBlockPos());
    }

    public Vec3 relativeVec(Vec3 vec0) {
        Vec3 $$1 = Vec3.atLowerCornerOf(this.testInfo.getStructureBlockPos());
        return StructureTemplate.transform(vec0.subtract($$1), Mirror.NONE, this.testInfo.getRotation(), this.testInfo.getStructureBlockPos());
    }

    public void assertTrue(boolean boolean0, String string1) {
        if (!boolean0) {
            throw new GameTestAssertException(string1);
        }
    }

    public void assertFalse(boolean boolean0, String string1) {
        if (boolean0) {
            throw new GameTestAssertException(string1);
        }
    }

    public long getTick() {
        return this.testInfo.getTick();
    }

    private AABB getBounds() {
        return this.testInfo.getStructureBounds();
    }

    private AABB getRelativeBounds() {
        AABB $$0 = this.testInfo.getStructureBounds();
        return $$0.move(BlockPos.ZERO.subtract(this.absolutePos(BlockPos.ZERO)));
    }

    public void forEveryBlockInStructure(Consumer<BlockPos> consumerBlockPos0) {
        AABB $$1 = this.getRelativeBounds();
        BlockPos.MutableBlockPos.m_121921_($$1.move(0.0, 1.0, 0.0)).forEach(consumerBlockPos0);
    }

    public void onEachTick(Runnable runnable0) {
        LongStream.range(this.testInfo.getTick(), (long) this.testInfo.getTimeoutTicks()).forEach(p_177283_ -> this.testInfo.setRunAtTickTime(p_177283_, runnable0::run));
    }

    public void placeAt(Player player0, ItemStack itemStack1, BlockPos blockPos2, Direction direction3) {
        BlockPos $$4 = this.absolutePos(blockPos2.relative(direction3));
        BlockHitResult $$5 = new BlockHitResult(Vec3.atCenterOf($$4), direction3, $$4, false);
        UseOnContext $$6 = new UseOnContext(player0, InteractionHand.MAIN_HAND, $$5);
        itemStack1.useOn($$6);
    }
}