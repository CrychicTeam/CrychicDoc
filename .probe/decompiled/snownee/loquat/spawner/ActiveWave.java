package snownee.loquat.spawner;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import snownee.loquat.Loquat;
import snownee.loquat.duck.LoquatMob;
import snownee.loquat.util.CommonProxy;
import snownee.lychee.core.ActionRuntime;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.contextual.ContextualHolder;
import snownee.lychee.core.post.PostAction;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.util.json.JsonPointer;

public class ActiveWave implements ILycheeRecipe<LycheeContext> {

    private final SpawnMobAreaEvent event;

    private final int waveIndex;

    private final Spawner.Wave wave;

    private final LycheeContext context;

    private final ObjectArrayList<SpawnMobAction> pendingMobs = ObjectArrayList.of();

    private final Set<UUID> mobs = Sets.newHashSet();

    private final Set<UUID> successiveSpawnableMobs = Sets.newHashSet();

    private final Difficulty.DifficultyLevel difficulty;

    private boolean pendingMobsNeedShuffle;

    private int spawnCooldown;

    private Consumer<Entity> deathListener;

    private BiConsumer<Entity, Entity> successiveSpawnListener;

    private boolean isFinished;

    private int successiveSpawnCooldown;

    private int proactiveCheckCooldown;

    public ActiveWave(SpawnMobAreaEvent event, int waveIndex, LycheeContext context, Difficulty.DifficultyLevel difficulty) {
        this.event = event;
        this.waveIndex = waveIndex;
        this.wave = event.getSpawner().waves[waveIndex];
        this.context = context;
        this.difficulty = difficulty;
    }

    public static boolean canSuccessiveSpawn(LivingEntity entity) {
        return entity instanceof Slime;
    }

    public boolean tick(ServerLevel world) {
        if (this.successiveSpawnCooldown > 0 && --this.successiveSpawnCooldown == 0) {
            this.checkIfFinished();
            if (this.isFinished) {
                return true;
            }
        }
        if (this.context.runtime.state == ActionRuntime.State.STOPPED && --this.proactiveCheckCooldown <= 0) {
            this.mobs.removeIf(uuid -> world.getEntity(uuid) == null);
            this.checkIfFinished();
            this.proactiveCheckCooldown = 60;
        }
        if (this.spawnCooldown > 0) {
            this.spawnCooldown--;
            return false;
        } else {
            if (this.pendingMobsNeedShuffle) {
                this.pendingMobsNeedShuffle = false;
                Util.shuffle(this.pendingMobs, world.f_46441_);
            }
            this.pendingMobs.removeIf(action -> {
                List<ServerPlayer> players = world.players();
                int offset = world.f_46441_.nextInt(players.size());
                MutableObject<ServerPlayer> target = new MutableObject();
                for (int i = 0; i < players.size(); i++) {
                    ServerPlayer player = (ServerPlayer) players.get((i + offset) % players.size());
                    if (!player.isSpectator() && this.event.getArea().contains(player.m_20191_())) {
                        target.setValue(player);
                        break;
                    }
                }
                Entity entity = action.getMob().createMob(world, this.event.getArea(), action.getZone(), e -> {
                    if (target.getValue() != null) {
                        e.lookAt(EntityAnchorArgument.Anchor.EYES, ((ServerPlayer) target.getValue()).m_20182_());
                        if (e instanceof Monster monster) {
                            monster.m_6710_((LivingEntity) target.getValue());
                        }
                    }
                });
                if (entity != null) {
                    this.addMob(entity);
                    if (entity.isVehicle()) {
                        entity.getIndirectPassengers().forEach(this::addMob);
                    }
                }
                return entity != null;
            });
            if (!this.pendingMobs.isEmpty()) {
                this.spawnCooldown = 20;
            }
            if (this.isFinished) {
                CommonProxy.unregisterDeathListener(this.deathListener);
                this.deathListener = null;
                if (this.successiveSpawnListener != null) {
                    CommonProxy.unregisterSuccessiveSpawnListener(this.successiveSpawnListener);
                    this.successiveSpawnListener = null;
                }
            }
            return this.isFinished;
        }
    }

    public float getMobAmountMultiplier() {
        return this.difficulty.amount;
    }

    public void addPendingMob(SpawnMobAction action) {
        this.pendingMobsNeedShuffle = true;
        this.pendingMobs.add(action);
    }

    public void addMob(Entity entity) {
        if (entity instanceof LivingEntity living) {
            if (entity instanceof Mob mob) {
                mob.setPersistenceRequired();
                ((LoquatMob) mob).loquat$setRestriction(this.event);
            }
            LivingEntity var4 = this.difficulty.apply(living);
            this.mobs.add(var4.m_20148_());
            if (canSuccessiveSpawn(var4)) {
                this.successiveSpawnableMobs.add(var4.m_20148_());
                if (this.successiveSpawnListener == null) {
                    this.successiveSpawnListener = (e, newEntity) -> {
                        if (this.successiveSpawnableMobs.contains(e.getUUID())) {
                            this.addMob(newEntity);
                        }
                    };
                    CommonProxy.registerSuccessiveSpawnListener(this.successiveSpawnListener);
                }
            }
        }
    }

    public void onStart() {
        this.deathListener = entity -> {
            if (this.mobs.remove(entity.getUUID())) {
                if (this.successiveSpawnableMobs.contains(entity.getUUID())) {
                    this.successiveSpawnCooldown = 30;
                }
                this.onKilled(entity);
            }
        };
        CommonProxy.registerDeathListener(this.deathListener);
    }

    public void onKilled(Entity entity) {
        this.checkIfFinished();
    }

    public void checkIfFinished() {
        if (!this.isFinished) {
            this.isFinished = this.mobs.isEmpty() && this.pendingMobs.isEmpty() && this.context.runtime.state == ActionRuntime.State.STOPPED && this.successiveSpawnCooldown <= 0;
        }
    }

    public int getRemainMobs() {
        return this.mobs.size() + this.pendingMobs.size();
    }

    @Override
    public ResourceLocation lychee$getId() {
        return Loquat.id("spawner/%s/%s/%d".formatted(this.event.getSpawnerId().getNamespace(), this.event.getSpawnerId().getPath(), this.waveIndex));
    }

    @Override
    public IntList getItemIndexes(JsonPointer jsonPointer) {
        return IntList.of();
    }

    @Override
    public Stream<PostAction> getPostActions() {
        return Stream.of(this.wave.post);
    }

    @Override
    public ContextualHolder getContextualHolder() {
        return this.wave.contextual;
    }

    @Nullable
    @Override
    public String getComment() {
        return null;
    }

    @Override
    public boolean showInRecipeViewer() {
        return false;
    }

    @Override
    public void applyPostActions(LycheeContext ctx, int times) {
        ctx.enqueueActions(this.getPostActions(), times, true);
    }

    @Override
    public Map<JsonPointer, List<PostAction>> getActionGroups() {
        return Map.of();
    }

    public Spawner.Wave getWave() {
        return this.wave;
    }

    public LycheeContext getContext() {
        return this.context;
    }
}