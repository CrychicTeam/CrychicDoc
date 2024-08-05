package com.mna.blocks.tileentities;

import com.mna.Registries;
import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.IRequirePlayerReference;
import com.mna.api.blocks.tile.BlockPosCache;
import com.mna.api.blocks.tile.IEldrinCapacitorTile;
import com.mna.api.blocks.tile.IEldrinConsumerTile;
import com.mna.api.blocks.tile.IMultiblockDefinition;
import com.mna.api.blocks.tile.MultiblockTile;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.events.EldrinCraftingEvent;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.tools.MATags;
import com.mna.api.tools.RLoc;
import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.entities.utility.PresentItem;
import com.mna.network.ServerMessageDispatcher;
import com.mna.network.messages.to_client.SpawnParticleEffectMessage;
import com.mna.particles.types.movers.ParticleBezierMover;
import com.mna.particles.types.movers.ParticleLerpMover;
import com.mna.particles.types.movers.ParticleSphereOrbitMover;
import com.mna.particles.types.movers.ParticleVelocityMover;
import com.mna.recipes.ItemAndPatternCraftingInventory;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.eldrin.EldrinAltarRecipe;
import com.mna.sound.PredicateLoopingSound;
import com.mna.tools.InventoryUtilities;
import com.mna.tools.math.MathUtils;
import com.mna.tools.render.MultiblockRenderer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;

public class EldrinAltarTile extends MultiblockTile implements IForgeBlockEntity, IRequirePlayerReference<EldrinAltarTile>, IEldrinConsumerTile {

    private static final int MAX_ITEMS = 1;

    public static final int INVENTORY_SLOT_INDEX = 0;

    public static final int CONSUME_RATE = 10;

    public static final Vec3[] crystal_offsets = new Vec3[] { new Vec3(1.0, 0.0, 0.0), new Vec3(-1.0, 0.0, 0.0), new Vec3(0.5, 0.0, 1.0), new Vec3(-0.5, 0.0, 1.0), new Vec3(0.5, 0.0, -1.0), new Vec3(-0.5, 0.0, -1.0) };

    private int stage = EldrinAltarTile.Stage.IDLE.ordinal();

    private int stageTicks = 0;

    private EldrinAltarRecipe __cachedRecipe;

    private ResourceLocation __cachedRecipeId;

    private BlockPosCache __powerSuppliers;

    private int lastCraftTier = 0;

    private IFaction lastCraftFaction = null;

    private HashMap<Affinity, Float> lastCraftEldrin;

    private ResourceLocation lastCraftRecipeID;

    private EldrinAltarRecipe _lastCraftRecipe;

    private NonNullList<ItemStack> lastCraftItems;

    private NonNullList<ItemStack> lastCraftItemsMerged;

    private ItemStack lastCraftOutput = ItemStack.EMPTY;

    private HashMap<Affinity, Float> consumedPower;

    private HashMap<Affinity, Boolean> powerRequirementStatus;

    private UUID __crafterID;

    private Player __crafter;

    private final ArrayList<EldrinAltarTile.ActiveCrystal> activeCrystals = new ArrayList();

    private final ArrayList<EldrinAltarTile.CollectedItem> collectedItems = new ArrayList();

    public EldrinAltarTile(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState state) {
        super(p_i48289_1_, RLoc.create("multiblock/eldrin_altar"), pos, state, 1);
        this.__powerSuppliers = new BlockPosCache(this, 7, (bp, world) -> {
            if (!this.losCheck(bp.above())) {
                return false;
            } else {
                BlockEntity te = this.m_58904_().getBlockEntity(bp);
                return te != null && te instanceof IEldrinCapacitorTile && ((IEldrinCapacitorTile) te).canSupply(Affinity.UNKNOWN, this.__crafter);
            }
        });
        this.consumedPower = new HashMap();
        this.powerRequirementStatus = new HashMap();
        this.lastCraftEldrin = new HashMap();
        this.lastCraftItems = NonNullList.create();
        this.lastCraftItemsMerged = NonNullList.create();
    }

    protected boolean losCheck(BlockPos pos) {
        Vec3 vector3d = Vec3.atCenterOf(this.m_58899_().above());
        Vec3 vector3d1 = Vec3.atCenterOf(pos);
        HitResult result = this.m_58904_().m_45547_(new ClipContext(vector3d, vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        return result.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) result).getBlockPos().equals(pos) : false;
    }

    public EldrinAltarTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.ELDRIN_ALTAR_TILE.get(), pos, state);
    }

    public EldrinAltarTile.Stage getStage() {
        return EldrinAltarTile.Stage.values()[this.stage];
    }

    public int getStageTicks() {
        return this.stageTicks;
    }

    private void setStage(EldrinAltarTile.Stage stage) {
        this.stage = stage.ordinal();
        this.stageTicks = 0;
        if (!this.m_58904_().isClientSide()) {
            this.m_58904_().sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 2);
        }
    }

    private EldrinAltarRecipe getCachedRecipe() {
        if (this.__cachedRecipe == null && this.__cachedRecipeId != null) {
            this.m_58904_().getRecipeManager().byKey(this.__cachedRecipeId).ifPresent(r -> {
                if (r instanceof EldrinAltarRecipe) {
                    this.__cachedRecipe = (EldrinAltarRecipe) r;
                }
            });
        }
        return this.__cachedRecipe;
    }

    private Player getCrafter() {
        if (this.__crafter == null && this.__crafterID != null) {
            this.__crafter = this.m_58904_().m_46003_(this.__crafterID);
        }
        return this.__crafter;
    }

    @Override
    public void setPlayerReference(Player player) {
        this.__crafter = player;
        this.__crafterID = player.m_20148_();
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.m_58899_()).inflate(5.0);
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, EldrinAltarTile tile) {
        MultiblockTile.Tick(level, pos, state, tile);
        tile.__powerSuppliers.tick();
        tile.stageTicks++;
        if (level.isClientSide()) {
            tile.spawnParticles();
            tile.playSounds();
        } else {
            switch(tile.getStage()) {
                case FINDING_POWER:
                    tile.tickLogic_findPower();
                    break;
                case CONSUMING_POWER:
                    tile.tickLogic_consumePower();
                    break;
                case CONSUMING_REAGENTS:
                    tile.tickLogic_consumeReagents();
                    break;
                case CRAFTING:
                    tile.tickLogic_craft();
                    break;
                case COMPLETING:
                    tile.tickLogic_complete();
                case IDLE:
            }
        }
    }

    public boolean structureMatched() {
        return this.structureMatched;
    }

    @OnlyIn(Dist.CLIENT)
    public void setGhostMultiblock() {
        if (!this.structureMatched) {
            this.match(true);
            if (!this.structureMatched) {
                this.getDefinition().ifPresent(r -> {
                    BlockPos anchorPos = this.getMatchOrigin();
                    MultiblockRenderer.setMultiblock(r, Component.translatable(r.getId().toString()), false);
                    MultiblockRenderer.anchorTo(anchorPos, Rotation.NONE);
                });
            }
        }
    }

    public HashMap<Affinity, Boolean> getPowerRequirementStatus() {
        return this.powerRequirementStatus;
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnParticles() {
        if (this.getStage() != EldrinAltarTile.Stage.IDLE) {
            this.spawnParticles_active();
            this.spawnParticles_crystals();
            this.spawnParticles_items();
            this.spawnParticles_lights();
            this.spawnParticles_power();
        }
    }

    private void spawnParticles_power() {
        for (Affinity aff : Affinity.values()) {
            if (this.consumedPower.containsKey(aff) && (Float) this.consumedPower.get(aff) >= 0.0F) {
                for (int i = 0; i < 10; i++) {
                    MAParticleType particle = null;
                    switch(aff) {
                        case ARCANE:
                            particle = new MAParticleType(ParticleInit.ARCANE_MAGELIGHT.get());
                            break;
                        case EARTH:
                            particle = new MAParticleType(ParticleInit.DUST.get());
                            break;
                        case ENDER:
                            particle = new MAParticleType(ParticleInit.ENDER_VELOCITY.get());
                            break;
                        case FIRE:
                        case HELLFIRE:
                        case LIGHTNING:
                            particle = new MAParticleType(ParticleInit.FLAME.get());
                            break;
                        case WATER:
                        case ICE:
                            particle = new MAParticleType(ParticleInit.WATER.get());
                            break;
                        case WIND:
                            particle = new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setScale(0.2F).setColor(10, 10, 10);
                    }
                    if (particle != null) {
                        Vec3 particlePos = Vec3.atCenterOf(this.f_58858_);
                        particle.setMover(new ParticleSphereOrbitMover(particlePos.x, particlePos.y + 1.25, particlePos.z, 0.01F, Math.random() * 360.0, 1.5));
                        this.m_58904_().addParticle(particle, particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnParticles_active() {
        int angleStep = 60;
        Vec3 center = Vec3.atCenterOf(this.m_58899_()).subtract(0.0, 0.5, 0.0);
        for (int i = 0; i < 360; i += angleStep) {
            double angle = (double) ((i + this.stageTicks) % 360) * Math.PI / 180.0;
            Vec3 offset = center.add(new Vec3(Math.sin(angle), 0.0, Math.cos(angle)).normalize().scale(4.0)).add(0.0, 1.0, 0.0);
            this.m_58904_().addParticle(new MAParticleType(ParticleInit.TRAIL_ORBIT.get()).setScale(0.25F).setMaxAge(50).setColor(2, 1, 5).setMover(new ParticleBezierMover(center, offset)), center.x, center.y, center.z, 0.0, 0.0, 0.0);
        }
        if (this.stageTicks == 1) {
            int var7 = 10;
            for (int i = 0; i <= 360; i += var7) {
                int angle = (i + this.stageTicks * 2) % 360;
                Vec3 offset = new Vec3(Math.sin((double) angle * Math.PI / 180.0), 0.0, Math.cos((double) angle * Math.PI / 180.0)).normalize().scale(0.1);
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()).setScale(0.5F).setMaxAge(20), center.x, center.y, center.z, offset.x, offset.y, offset.z);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnParticles_crystals() {
        this.activeCrystals.forEach(e -> {
            Vec3 offset = Vec3.atCenterOf(this.m_58899_()).add(crystal_offsets[e.offsetIndex]);
            Vec3 crystalWidth = new Vec3(0.1, 0.0, 0.1);
            int[] color = e.affinity.getShiftAffinity().getColor();
            for (int i = 0; i < 5; i++) {
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.LIGHT_VELOCITY.get().setColor(color[0], color[1], color[2])), offset.x - crystalWidth.x + Math.random() * crystalWidth.x * 2.0, offset.y + 0.55, offset.z - crystalWidth.z + Math.random() * crystalWidth.z * 2.0, 0.0, 0.01, 0.0);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnParticles_items() {
        if (this.stage >= EldrinAltarTile.Stage.CONSUMING_REAGENTS.ordinal()) {
            Vec3 center = Vec3.atCenterOf(this.m_58899_()).add(0.0, 1.0, 0.0);
            int burstIndex = this.collectedItems.size() - this.stageTicks / 20;
            for (int i = 0; i < this.collectedItems.size(); i++) {
                EldrinAltarTile.CollectedItem ci = (EldrinAltarTile.CollectedItem) this.collectedItems.get(i);
                Vec3 pedestalParticleCenter = new Vec3((double) ci.tilePos.m_123341_() + 0.5, (double) ci.tilePos.m_123342_() + 1.5, (double) ci.tilePos.m_123343_() + 0.5);
                if (this.stage == EldrinAltarTile.Stage.CONSUMING_REAGENTS.ordinal() && this.stageTicks > 0 && this.stageTicks % 20 == 0 && i == burstIndex) {
                    for (int n = 0; n < 20; n++) {
                        this.m_58904_().addParticle(this.createRandomParticle(ci.stack).setMaxAge(5).setMover(new ParticleVelocityMover(Math.random() * 0.2 - 0.1, Math.random() * 0.2 - 0.1, Math.random() * 0.2 - 0.1, true)), pedestalParticleCenter.x, pedestalParticleCenter.y, pedestalParticleCenter.z, 0.0, 0.0, 0.0);
                    }
                }
                if (this.stage == EldrinAltarTile.Stage.CONSUMING_REAGENTS.ordinal() && i >= burstIndex || i <= burstIndex && this.stage == EldrinAltarTile.Stage.CRAFTING.ordinal()) {
                    this.m_58904_().addParticle(this.createRandomParticle(ci.stack).setMover(new ParticleSphereOrbitMover(pedestalParticleCenter.x, pedestalParticleCenter.y, pedestalParticleCenter.z, 0.01F, Math.random() * 360.0, 0.5)), pedestalParticleCenter.x, pedestalParticleCenter.y, pedestalParticleCenter.z, 0.0, 0.0, 0.0);
                }
                if (this.stage == EldrinAltarTile.Stage.CRAFTING.ordinal() && i == burstIndex) {
                    this.m_58904_().addParticle(this.createRandomParticle(ci.stack).setMover(new ParticleLerpMover(pedestalParticleCenter.x, pedestalParticleCenter.y, pedestalParticleCenter.z, center.x, center.y, center.z)), pedestalParticleCenter.x, pedestalParticleCenter.y, pedestalParticleCenter.z, 0.0, 0.0, 0.0);
                }
            }
            if (this.stage >= EldrinAltarTile.Stage.CRAFTING.ordinal()) {
                float radius = 0.75F;
                float scale = 0.1F;
                if (this.getStage() == EldrinAltarTile.Stage.COMPLETING) {
                    if (this.stageTicks > 40) {
                        float pct = MathUtils.clamp01((float) (this.stageTicks - 40) / 40.0F);
                        radius -= 0.74F * pct;
                        scale = (float) ((double) scale - 0.09 * (double) pct);
                    } else if (this.stageTicks > 80) {
                        return;
                    }
                }
                for (int i = 0; i < this.collectedItems.size(); i++) {
                    if (this.getStage() != EldrinAltarTile.Stage.CRAFTING || i > burstIndex) {
                        EldrinAltarTile.CollectedItem cix = (EldrinAltarTile.CollectedItem) this.collectedItems.get(i);
                        this.m_58904_().addParticle(new MAParticleType(ParticleInit.ITEM.get()).setStack(cix.stack).setScale(scale).setMover(new ParticleSphereOrbitMover(center.x, center.y, center.z, 0.01F, Math.random() * 360.0, (double) radius)), center.x, center.y, center.z, 0.0, 0.0, 0.0);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnParticles_lights() {
        for (int i = 0; i < this.collectedItems.size(); i++) {
            EldrinAltarTile.CollectedItem item = (EldrinAltarTile.CollectedItem) this.collectedItems.get(i);
            if (!item.stack.isEmpty()) {
                Vec3 offset = Vec3.atCenterOf(item.tilePos);
                Vec3 crystalWidth = new Vec3(0.25, 0.0, 0.25);
                for (int n = 0; n < 10; n++) {
                    this.m_58904_().addParticle(new MAParticleType(ParticleInit.LIGHT_VELOCITY.get()), offset.x - crystalWidth.x + Math.random() * crystalWidth.x * 2.0, offset.y + 0.55, offset.z - crystalWidth.z + Math.random() * crystalWidth.z * 2.0, 0.0, 0.01, 0.0);
                }
            }
        }
        if (this.getStage() == EldrinAltarTile.Stage.COMPLETING && this.stageTicks == 129) {
            Random rnd = new Random();
            Vec3 basePoint = Vec3.atBottomCenterOf(this.m_58899_());
            Vec3 srcPoint = basePoint.add(0.5, 1.5, 0.5);
            for (int ix = 0; ix < 150; ix++) {
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), srcPoint.x(), srcPoint.y(), srcPoint.z(), -0.5 + Math.random(), 0.01, -0.5 + Math.random());
            }
            for (int ix = 0; ix < 50; ix++) {
                Vec3 lightPoint = new Vec3(srcPoint.x() - 0.2F + (double) (rnd.nextFloat() * 0.4F), srcPoint.y() - 0.3F, srcPoint.z() - 0.2F + (double) (rnd.nextFloat() * 0.4F));
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.LIGHT_VELOCITY.get()), lightPoint.x(), lightPoint.y(), lightPoint.z(), 0.0, 0.01, 0.0);
            }
            int angleStep = 10;
            for (int ix = 0; ix <= 360; ix += angleStep) {
                int angle = (ix + this.stageTicks * 2) % 360;
                Vec3 offset = new Vec3(Math.sin((double) angle * Math.PI / 180.0), 0.0, Math.cos((double) angle * Math.PI / 180.0)).normalize().scale(0.3);
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()).setScale(0.5F).setMaxAge(20), basePoint.x, basePoint.y, basePoint.z, offset.x, offset.y, offset.z);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private MAParticleType createRandomParticle(ItemStack stack) {
        if (Math.random() < 0.5 && this.getCachedRecipe() != null) {
            ArrayList<Affinity> affs = new ArrayList();
            affs.addAll(this.getCachedRecipe().getPowerRequirements().keySet());
            if (affs.size() > 0) {
                Affinity rand = (Affinity) affs.get((int) (Math.random() * (double) affs.size()));
                switch(rand) {
                    case ARCANE:
                        return new MAParticleType(ParticleInit.ARCANE_MAGELIGHT.get());
                    case EARTH:
                        return new MAParticleType(ParticleInit.DUST.get());
                    case ENDER:
                        return new MAParticleType(ParticleInit.ENDER_VELOCITY.get());
                    case FIRE:
                    case HELLFIRE:
                        return new MAParticleType(ParticleInit.FLAME.get());
                    case LIGHTNING:
                    case WIND:
                        return new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setScale(0.04F).setColor(30, 30, 30);
                    case WATER:
                    case ICE:
                        return new MAParticleType(ParticleInit.WATER.get()).setScale(0.03F);
                    case UNKNOWN:
                }
            }
        }
        return new MAParticleType(ParticleInit.ITEM.get()).setStack(stack).setScale(0.1F);
    }

    @OnlyIn(Dist.CLIENT)
    private void playSounds() {
        if (this.getStage() == EldrinAltarTile.Stage.FINDING_POWER && this.stageTicks == 1) {
            this.PlaySound(SFX.Spell.Cast.ENDER);
            this.PlayLoopingSound(SFX.Loops.ELDRIN_ALTAR);
        }
        int ticksPerBeam = 5;
        if (this.stage == EldrinAltarTile.Stage.CONSUMING_REAGENTS.ordinal() && this.stageTicks % ticksPerBeam == 0 && this.stageTicks < this.getActiveCrystals().size() * ticksPerBeam) {
            this.PlaySound(SFX.Spell.Cast.ARCANE);
        }
        int burstFreq = 20;
        if (this.stage == EldrinAltarTile.Stage.CONSUMING_REAGENTS.ordinal() && this.stageTicks > 0 && this.stageTicks % burstFreq == 0 && this.stageTicks <= burstFreq * this.getCollectedItems().size()) {
            this.PlaySound(SFX.Spell.Impact.Single.EARTH);
        }
        if (this.stage == EldrinAltarTile.Stage.CRAFTING.ordinal() && this.stageTicks > 0 && this.stageTicks % burstFreq == 0 && this.stageTicks < burstFreq * this.getCollectedItems().size()) {
            this.PlaySound(SFX.Event.Eldrin.DRAW_IN_ITEM);
        }
        if (this.getStage() == EldrinAltarTile.Stage.COMPLETING && this.stageTicks == 129) {
            this.PlaySound(SFX.Event.Eldrin.CRAFT_COMPLETE);
        }
    }

    private void PlaySound(SoundEvent soundID) {
        this.m_58904_().playLocalSound((double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_(), soundID, SoundSource.BLOCKS, 1.0F, 1.0F, false);
    }

    @OnlyIn(Dist.CLIENT)
    private void PlayLoopingSound(SoundEvent soundID) {
        Minecraft.getInstance().getSoundManager().play(new PredicateLoopingSound(soundID, "eldrin_altar", this::soundLoopPredicate).setVolume(0.2F).setPosition(this.f_58858_));
    }

    private boolean soundLoopPredicate(String id) {
        return this.stage > 0;
    }

    public boolean startCrafting(Player player) {
        if (!this.structureMatched) {
            return false;
        } else if (!this.cacheRecipe(player, false)) {
            return false;
        } else {
            LazyOptional<IPlayerProgression> progression = player.getCapability(PlayerProgressionProvider.PROGRESSION);
            if (!progression.isPresent()) {
                return false;
            } else if (this.__cachedRecipe != null && this.__cachedRecipe.getTier() <= ((IPlayerProgression) progression.resolve().get()).getTier()) {
                this.activeCrystals.clear();
                this.collectedItems.clear();
                MutableObject<HashMap<Affinity, Float>> playerNWStr = new MutableObject();
                MutableBoolean canStart = new MutableBoolean(true);
                this.m_58904_().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> playerNWStr.setValue(m.getWellspringRegistry().getNodeNetworkStrengthFor(player)));
                if (canStart.booleanValue()) {
                    this.__crafter = player;
                    this.consumedPower.clear();
                    this.setStage(EldrinAltarTile.Stage.FINDING_POWER);
                }
                return canStart.booleanValue();
            } else {
                player.m_213846_(Component.translatable("gui.mna.recipe.lowtier"));
                return false;
            }
        }
    }

    private boolean cacheRecipe(Player crafter, boolean consume) {
        if (!this.getDisplayedItem().isEmpty() && crafter != null) {
            Optional<IMultiblockDefinition> def = this.getDefinition();
            if (!def.isPresent()) {
                return false;
            } else {
                List<BlockPos> offsets = ((IMultiblockDefinition) def.get()).getPositions(Arrays.asList(BlockInit.PEDESTAL.getId(), BlockInit.PEDESTAL_WITH_SIGN.getId()), null);
                if (offsets.size() != 8) {
                    throw new RuntimeException("THIS IS NOT AN M&A ISSUE.  A packmaker has replaced the structure for the Runic Altar and didn't put exactly 8 pedestals.  The pedestals are required to match the recipe.  Go tell them to fix it.");
                } else {
                    BlockPos origin = BlockPos.ZERO;
                    if (this.matchOriginIsCenter()) {
                        origin = origin.subtract(new Vec3i(((IMultiblockDefinition) def.get()).getSize().getX() / 2, 0, ((IMultiblockDefinition) def.get()).getSize().getZ() / 2));
                    }
                    ArrayList<ItemStack> stacks = new ArrayList();
                    stacks.add(this.getDisplayedItem().copy());
                    if (consume) {
                        ItemStack stack = this.removeItem(0, 1);
                        if (!stack.isEmpty()) {
                            this.collectedItems.add(new EldrinAltarTile.CollectedItem(this.m_58899_(), stack));
                        }
                    }
                    for (BlockPos pos : offsets) {
                        BlockPos adjustedpos = origin.offset(pos);
                        if (adjustedpos.m_123341_() != 0 || adjustedpos.m_123342_() != 0 || adjustedpos.m_123343_() != 0) {
                            BlockEntity te = this.m_58904_().getBlockEntity(this.m_58899_().offset(adjustedpos));
                            if (te == null || !(te instanceof PedestalTile)) {
                                return false;
                            }
                            stacks.add(((PedestalTile) te).getDisplayedItem().copy());
                            if (consume) {
                                ItemStack stack = ((PedestalTile) te).removeItem(0, 1);
                                if (!stack.isEmpty()) {
                                    this.collectedItems.add(new EldrinAltarTile.CollectedItem(te.getBlockPos(), stack));
                                }
                            }
                        }
                    }
                    ItemAndPatternCraftingInventory craftinginventory = new ItemAndPatternCraftingInventory(9, new ArrayList());
                    for (int i = 0; i < 9; i++) {
                        craftinginventory.m_6836_(i, (ItemStack) stacks.get(i));
                    }
                    this.__cachedRecipe = (EldrinAltarRecipe) this.m_58904_().getRecipeManager().getRecipeFor(RecipeInit.ELDRIN_ALTAR_TYPE.get(), craftinginventory, this.f_58857_).orElse(null);
                    if (this.__cachedRecipe != null) {
                        IPlayerProgression progression = (IPlayerProgression) crafter.getCapability(PlayerProgressionProvider.PROGRESSION).resolve().get();
                        IFaction playerfaction = progression.getAlliedFaction();
                        int playerTier = progression.getTier();
                        if (this.__cachedRecipe.getFactionRequirement() != null && this.__cachedRecipe.getFactionRequirement() != playerfaction) {
                            this.__cachedRecipe = null;
                            if (!crafter.m_9236_().isClientSide()) {
                                crafter.m_213846_(Component.translatable("block.mna.eldrin_altar.wrongFaction"));
                            }
                        }
                        if (this.__cachedRecipe.getTier() > playerTier) {
                            this.__cachedRecipe = null;
                            if (!crafter.m_9236_().isClientSide()) {
                                crafter.m_213846_(Component.translatable("block.mna.runic_anvil.low_tier"));
                            }
                        }
                    }
                    return this.__cachedRecipe != null;
                }
            }
        } else {
            return false;
        }
    }

    private void tickLogic_findPower() {
        if (this.stageTicks == 1) {
            this.__powerSuppliers.queueRecheck();
        } else if (!this.__powerSuppliers.isSearching()) {
            if (this.getCachedRecipe() == null) {
                this.setStage(EldrinAltarTile.Stage.IDLE);
                return;
            }
            HashMap<Affinity, Float> requiredpower = new HashMap();
            this.getCachedRecipe().getPowerRequirements().forEach((k, v) -> requiredpower.put(k, v));
            this.__powerSuppliers.getCachedPositions().forEach(bp -> {
                BlockEntity te = this.m_58904_().getBlockEntity(bp);
                if (te != null && te instanceof IEldrinCapacitorTile) {
                    ((IEldrinCapacitorTile) te).getAffinities().forEach(suppliedPower -> {
                        if (requiredpower.containsKey(suppliedPower)) {
                            requiredpower.remove(suppliedPower);
                            this.activeCrystals.add(new EldrinAltarTile.ActiveCrystal(suppliedPower, this.getClosestOffset(bp), bp));
                        }
                    });
                }
            });
            if (requiredpower.size() == 0) {
                this.setStage(EldrinAltarTile.Stage.CONSUMING_POWER);
            } else {
                if (this.getCrafter() != null) {
                    this.getCrafter().m_213846_(Component.translatable("gui.mna.recipe.missing_conduit"));
                }
                this.consumedPower.clear();
                this.setStage(EldrinAltarTile.Stage.IDLE);
            }
        }
    }

    private void tickLogic_consumePower() {
        MutableBoolean fullyConsumed = new MutableBoolean(true);
        MutableBoolean consumedAny = new MutableBoolean(false);
        EldrinAltarRecipe cachedRecipe = this.getCachedRecipe();
        if (cachedRecipe == null) {
            this.setStage(EldrinAltarTile.Stage.IDLE);
        }
        cachedRecipe.getPowerRequirements().forEach((affinity, required) -> {
            if (!this.consumedPower.containsKey(affinity)) {
                this.consumedPower.put(affinity, 0.0F);
            }
            if (!this.powerRequirementStatus.containsKey(affinity)) {
                this.powerRequirementStatus.put(affinity, true);
            }
            float existing = (Float) this.consumedPower.get(affinity);
            if (existing < required && required > 0.0F && existing >= 0.0F) {
                fullyConsumed.setFalse();
                float toConsume = Math.min(10.0F, required - existing);
                float consumed = this.consume(this.__crafter, this.m_58899_(), Vec3.atCenterOf(this.m_58899_()), affinity, toConsume);
                if (consumed > 0.0F) {
                    float newAmt = existing + consumed;
                    if (newAmt < required) {
                        this.consumedPower.put(affinity, newAmt);
                    } else {
                        this.consumedPower.put(affinity, -1.0F);
                        consumedAny.setTrue();
                    }
                    this.powerRequirementStatus.put(affinity, true);
                } else {
                    this.powerRequirementStatus.put(affinity, false);
                }
            }
        });
        if (!this.m_58904_().isClientSide() && this.m_58904_().getGameTime() % 20L == 0L) {
            this.m_58904_().sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 2);
        }
        if (fullyConsumed.booleanValue()) {
            this.setStage(EldrinAltarTile.Stage.CONSUMING_REAGENTS);
        }
    }

    private int getClosestOffset(BlockPos pos) {
        int output = -1;
        double dist = Double.MAX_VALUE;
        Vec3 center = Vec3.atCenterOf(this.m_58899_());
        Vec3 target = Vec3.atCenterOf(pos);
        for (int i = 0; i < crystal_offsets.length; i++) {
            double loopDist = center.add(crystal_offsets[i]).distanceToSqr(target);
            if (loopDist < dist) {
                dist = loopDist;
                output = i;
            }
        }
        return output;
    }

    private void tickLogic_consumeReagents() {
        if (this.stageTicks == 1) {
            EldrinAltarRecipe prev = this.getCachedRecipe();
            if (!this.cacheRecipe(this.getCrafter(), true) || prev != this.getCachedRecipe()) {
                this.setStage(EldrinAltarTile.Stage.IDLE);
                return;
            }
            this.m_58904_().sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 2);
        }
        if (this.stageTicks >= 30 * this.getCachedRecipe().getRequiredItems().length) {
            this.setStage(EldrinAltarTile.Stage.CRAFTING);
        }
    }

    private void tickLogic_craft() {
        if (this.stageTicks >= 30 * this.getCachedRecipe().getRequiredItems().length) {
            this.setStage(EldrinAltarTile.Stage.COMPLETING);
        }
    }

    private void tickLogic_complete() {
        if (this.stageTicks >= 130) {
            Vec3 center = Vec3.atCenterOf(this.m_58899_());
            MinecraftForge.EVENT_BUS.post(new EldrinCraftingEvent(this.getCachedRecipe(), this.getCachedRecipe().getResultItem(), this.getCrafter()));
            Player crafter = this.getCrafter();
            if (crafter != null && crafter instanceof ServerPlayer) {
                CustomAdvancementTriggers.ELDRIN_ALTAR_CRAFT.trigger((ServerPlayer) crafter, this.getCachedRecipe().getResultItem());
            }
            PresentItem epi = new PresentItem(this.m_58904_(), center.x, center.y + 0.5, center.z, this.getCachedRecipe().getResultItem());
            this.m_58904_().m_7967_(epi);
            for (ItemStack byproduct : this.getCachedRecipe().rollByproducts(this.f_58857_.getRandom())) {
                InventoryUtilities.DropItemAt(byproduct, Vec3.atCenterOf(this.m_58899_().above()), this.f_58857_, true);
            }
            this.copyCraftInputToLastCraft();
            this.activeCrystals.clear();
            this.collectedItems.clear();
            this.setStage(EldrinAltarTile.Stage.IDLE);
            this.m_6596_();
        }
    }

    private boolean checkReCraftReagents(Player player) {
        if (player.isCreative()) {
            return true;
        } else {
            MutableBoolean progressionMet = new MutableBoolean(false);
            player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                if (p.getTier() < this.lastCraftTier) {
                    player.m_213846_(Component.translatable("gui.mna.recipe.lowtier"));
                } else if (this.lastCraftFaction != null && p.getAlliedFaction() != this.lastCraftFaction) {
                    player.m_213846_(Component.translatable("gui.mna.recipe.wrongfaction"));
                } else {
                    progressionMet.setTrue();
                }
            });
            if (!progressionMet.booleanValue()) {
                return false;
            } else {
                MutableBoolean reagentsPresent = new MutableBoolean(true);
                this.lastCraftItemsMerged.forEach(stack -> {
                    if (!stack.isEmpty() && !InventoryUtilities.hasStackInInventory(stack, true, true, new InvWrapper(player.getInventory()))) {
                        reagentsPresent.setFalse();
                    }
                });
                if (!reagentsPresent.getValue()) {
                    player.m_213846_(Component.translatable("gui.mna.recipe.missing_items"));
                    return false;
                } else {
                    MutableBoolean eldrinMet = new MutableBoolean(false);
                    player.m_9236_().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> {
                        HashMap<Affinity, Float> power = m.getWellspringRegistry().getNodeNetworkAmountFor(player);
                        for (Affinity aff : this.lastCraftEldrin.keySet()) {
                            if ((Float) power.getOrDefault(aff, 0.0F) < (Float) this.lastCraftEldrin.getOrDefault(aff, 0.0F)) {
                                player.m_213846_(Component.translatable("gui.mna.recipe.missing_wellspring", aff.name()));
                                return;
                            }
                        }
                        eldrinMet.setTrue();
                    });
                    return eldrinMet.booleanValue();
                }
            }
        }
    }

    private void consumeReCraftReagents(Player player) {
        if (!player.isCreative()) {
            this.m_58904_().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> this.lastCraftEldrin.entrySet().forEach(e -> m.getWellspringRegistry().consumePower(player.getGameProfile().getId(), this.f_58857_, (Affinity) e.getKey(), (Float) e.getValue())));
            this.lastCraftItemsMerged.forEach(stack -> InventoryUtilities.removeItemFromInventory(stack, true, true, new InvWrapper(player.getInventory())));
        }
    }

    public boolean reCraft(Player player) {
        if (!this.m_7983_() || this.m_58904_().isClientSide()) {
            return false;
        } else if (this.checkReCraftReagents(player)) {
            ServerMessageDispatcher.sendParticleEffect(this.m_58904_().dimension(), 32.0F, (double) ((float) this.m_58899_().m_123341_() + 0.5F), (double) ((float) this.m_58899_().m_123342_() + 1.1F), (double) ((float) this.m_58899_().m_123343_() + 0.5F), SpawnParticleEffectMessage.ParticleTypes.MANAWEAVE_CRAFT_COMPLETE);
            this.m_58904_().playSound(null, (double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_(), SFX.Event.Eldrin.CRAFT_COMPLETE, SoundSource.PLAYERS, 1.0F, (float) (0.9 + Math.random() * 0.2));
            this.setPlayerReference(player);
            this.consumeReCraftReagents(player);
            ItemStack output = this.lastCraftOutput.copy();
            if (player != null && player instanceof ServerPlayer) {
                CustomAdvancementTriggers.ELDRIN_ALTAR_CRAFT.trigger((ServerPlayer) player, output);
            }
            boolean sendUpdate = false;
            if (!player.getInventory().add(output)) {
                this.setItem(0, output);
                sendUpdate = true;
            }
            if (this._lastCraftRecipe == null && this.lastCraftRecipeID != null) {
                this.f_58857_.getRecipeManager().byKey(this.lastCraftRecipeID).ifPresent(recipe -> {
                    if (recipe instanceof EldrinAltarRecipe eaRecipe) {
                        this._lastCraftRecipe = eaRecipe;
                    }
                });
            }
            if (this._lastCraftRecipe != null) {
                int count = 1;
                for (ItemStack byproduct : this._lastCraftRecipe.rollByproducts(this.f_58857_.getRandom())) {
                    if (!player.getInventory().add(byproduct)) {
                        if (count++ < 9) {
                            this.setItem(count, byproduct);
                            sendUpdate = true;
                        } else {
                            InventoryUtilities.DropItemAt(byproduct, Vec3.atCenterOf(this.m_58899_().above()), this.f_58857_, true);
                        }
                    }
                }
            }
            if (sendUpdate) {
                this.f_58857_.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 2);
            }
            return true;
        } else {
            return false;
        }
    }

    private void copyCraftInputToLastCraft() {
        this.lastCraftItems.clear();
        this.lastCraftItemsMerged.clear();
        this.lastCraftEldrin.clear();
        for (EldrinAltarTile.CollectedItem ci : this.collectedItems) {
            ItemStack stack = ci.stack;
            this.lastCraftItems.add(stack.copy());
            Optional<ItemStack> existing = this.lastCraftItemsMerged.stream().filter(is -> ItemStack.matches(is, stack)).findFirst();
            if (existing.isPresent()) {
                ((ItemStack) existing.get()).setCount(((ItemStack) existing.get()).getCount() + stack.getCount());
            } else {
                this.lastCraftItemsMerged.add(stack.copy());
            }
        }
        this.__cachedRecipe.getPowerRequirements().forEach((a, f) -> this.lastCraftEldrin.put(a, f));
        this.lastCraftOutput = this.__cachedRecipe.getResultItem().copy();
        this._lastCraftRecipe = this.__cachedRecipe;
        this.lastCraftRecipeID = this.__cachedRecipe.m_6423_();
    }

    public ItemStack getReCraftOutput() {
        return this.lastCraftOutput;
    }

    public HashMap<Affinity, Float> getReCraftEldrin() {
        return this.lastCraftEldrin;
    }

    public List<ItemStack> getReCraftInput() {
        return (List<ItemStack>) this.lastCraftItems.stream().filter(stack -> !stack.isEmpty()).collect(Collectors.toList());
    }

    @Nullable
    public IFaction getLastCraftFaction() {
        return this.lastCraftFaction;
    }

    public int getLastCraftTier() {
        return this.lastCraftTier;
    }

    public void setReCraftRecipe(ItemStack output, List<ItemStack> inputs, IFaction faction, int tier, HashMap<Affinity, Float> powerRequirements) {
        this.lastCraftOutput = output;
        this.lastCraftItems.clear();
        this.lastCraftItemsMerged.clear();
        for (ItemStack stack : inputs) {
            this.lastCraftItems.add(stack.copy());
            Optional<ItemStack> existing = this.lastCraftItemsMerged.stream().filter(is -> ItemStack.matches(is, stack)).findFirst();
            if (existing.isPresent()) {
                ((ItemStack) existing.get()).setCount(((ItemStack) existing.get()).getCount() + stack.getCount());
            } else {
                this.lastCraftItemsMerged.add(stack.copy());
            }
        }
        this.lastCraftFaction = faction;
        this.lastCraftTier = tier;
        this.lastCraftEldrin = powerRequirements;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        super.m_6836_(index, stack);
        this.updateBlockState();
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack out = super.m_7407_(index, count);
        this.updateBlockState();
        return out;
    }

    private void updateBlockState() {
        this.updateBlockState("invChange", 131);
    }

    private void updateBlockState(String id, Integer data) {
        this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), data);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    public ItemStack getDisplayedItem() {
        return this.m_8020_(0);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] { 0 };
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return this.m_8020_(0).isEmpty();
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return !this.m_8020_(0).isEmpty();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag base = super.m_5995_();
        CompoundTag sub = this.writeAdditonal(new CompoundTag());
        base.put("invSync", sub);
        CompoundTag powerStatus = new CompoundTag();
        this.powerRequirementStatus.entrySet().forEach(e -> powerStatus.putBoolean(((Affinity) e.getKey()).name(), (Boolean) e.getValue()));
        base.put("powerStatus", powerStatus);
        return base;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        CompoundTag sub = tag.getCompound("invSync");
        this.readAdditional(sub);
        CompoundTag powerStatus = tag.getCompound("powerStatus");
        this.powerRequirementStatus.clear();
        for (int i = 0; i < Affinity.values().length; i++) {
            Affinity aff = Affinity.values()[i];
            if (powerStatus.contains(aff.name())) {
                this.powerRequirementStatus.put(aff, powerStatus.getBoolean(aff.name()));
            }
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag sub = pkt.getTag().getCompound("invSync");
        this.readAdditional(sub);
        CompoundTag powerStatus = pkt.getTag().getCompound("powerStatus");
        this.powerRequirementStatus.clear();
        for (int i = 0; i < Affinity.values().length; i++) {
            Affinity aff = Affinity.values()[i];
            if (powerStatus.contains(aff.name())) {
                this.powerRequirementStatus.put(aff, powerStatus.getBoolean(aff.name()));
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        this.writeAdditonal(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        this.readAdditional(compound);
        super.m_142466_(compound);
    }

    private CompoundTag writeAdditonal(CompoundTag compound) {
        ContainerHelper.saveAllItems(compound, this.inventoryItems);
        compound.putInt("stage", this.stage);
        compound.putInt("stageTicks", this.stageTicks);
        ListTag activeCrystals = new ListTag();
        this.activeCrystals.forEach(a -> activeCrystals.add(a.toNBT()));
        compound.put("activeCrystals", activeCrystals);
        ListTag collectedItems = new ListTag();
        this.collectedItems.forEach(i -> collectedItems.add(i.toNBT()));
        compound.put("collectedItems", collectedItems);
        CompoundTag consumed = new CompoundTag();
        this.consumedPower.entrySet().forEach(e -> consumed.putFloat(((Affinity) e.getKey()).name(), (Float) e.getValue()));
        compound.put("consumedPower", consumed);
        if (this.__crafterID != null) {
            compound.putString("crafter_uuid", this.__crafterID.toString());
        }
        if (this.__cachedRecipe != null) {
            compound.putString("cachedRecipeId", this.__cachedRecipe.m_6423_().toString());
        }
        CompoundTag lastCraft = new CompoundTag();
        lastCraft.put("lastCraftReagents", ContainerHelper.saveAllItems(new CompoundTag(), this.lastCraftItems));
        lastCraft.putInt("tier", this.lastCraftTier);
        if (this.lastCraftFaction != null) {
            lastCraft.putString("faction", ((IForgeRegistry) Registries.Factions.get()).getKey(this.lastCraftFaction).toString());
        }
        lastCraft.put("lastCraftOutput", this.lastCraftOutput.save(new CompoundTag()));
        CompoundTag eldrin = new CompoundTag();
        this.lastCraftEldrin.forEach((a, f) -> eldrin.putFloat(a.name(), f));
        lastCraft.put("eldrin", eldrin);
        compound.put("lastCraft", lastCraft);
        return compound;
    }

    private void readAdditional(CompoundTag compound) {
        this.m_6211_();
        this.lastCraftItems.clear();
        for (int i = 0; i < 9; i++) {
            this.lastCraftItems.add(ItemStack.EMPTY);
        }
        ContainerHelper.loadAllItems(compound, this.inventoryItems);
        this.stage = compound.getInt("stage");
        this.stageTicks = compound.getInt("stageTicks");
        if (compound.contains("cachedRecipeId")) {
            this.__cachedRecipeId = new ResourceLocation(compound.getString("cachedRecipeId"));
        }
        ListTag list = compound.getList("activeCrystals", 10);
        this.activeCrystals.clear();
        list.forEach(inbt -> {
            CompoundTag activeCrystal = (CompoundTag) inbt;
            this.activeCrystals.add(EldrinAltarTile.ActiveCrystal.fromNBT(activeCrystal));
        });
        list = compound.getList("collectedItems", 10);
        this.collectedItems.clear();
        list.forEach(inbt -> {
            CompoundTag collectedItem = (CompoundTag) inbt;
            this.collectedItems.add(EldrinAltarTile.CollectedItem.fromNBT(collectedItem));
        });
        if (compound.contains("consumedPower")) {
            this.consumedPower.clear();
            CompoundTag consumed = compound.getCompound("consumedPower");
            for (Affinity aff : Affinity.values()) {
                if (consumed.contains(aff.name())) {
                    this.consumedPower.put(aff, consumed.getFloat(aff.name()));
                }
            }
        }
        if (compound.contains("crafter_uuid")) {
            this.__crafterID = UUID.fromString(compound.getString("crafter_uuid"));
        }
        if (compound.contains("lastCraft")) {
            CompoundTag lastCraft = compound.getCompound("lastCraft");
            this.lastCraftOutput = ItemStack.of(lastCraft.getCompound("lastCraftOutput"));
            ContainerHelper.loadAllItems(lastCraft.getCompound("lastCraftReagents"), this.lastCraftItems);
            this.lastCraftTier = lastCraft.getInt("tier");
            if (lastCraft.contains("faction")) {
                ResourceLocation factionID = new ResourceLocation(lastCraft.getString("faction"));
                this.lastCraftFaction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(factionID);
            }
            this.lastCraftEldrin.clear();
            if (lastCraft.contains("eldrin")) {
                CompoundTag eldrin = lastCraft.getCompound("eldrin");
                for (Affinity affx : Affinity.values()) {
                    if (eldrin.contains(affx.name())) {
                        this.lastCraftEldrin.put(affx, eldrin.getFloat(affx.name()));
                    }
                }
            }
            this.lastCraftRecipeID = new ResourceLocation(lastCraft.getString("lastCraftRecipeID"));
            for (ItemStack stack : this.lastCraftItems) {
                Optional<ItemStack> existing = this.lastCraftItemsMerged.stream().filter(is -> ItemStack.matches(is, stack)).findFirst();
                if (existing.isPresent()) {
                    ((ItemStack) existing.get()).setCount(((ItemStack) existing.get()).getCount() + stack.getCount());
                } else {
                    this.lastCraftItemsMerged.add(stack.copy());
                }
            }
        }
    }

    public boolean MatchesReagent(ResourceLocation rLoc) {
        return this.m_8020_(0).isEmpty() ? false : MATags.isItemEqual(this.m_8020_(0), rLoc);
    }

    public ArrayList<EldrinAltarTile.ActiveCrystal> getActiveCrystals() {
        return this.activeCrystals;
    }

    public ArrayList<EldrinAltarTile.CollectedItem> getCollectedItems() {
        return this.collectedItems;
    }

    public static class ActiveCrystal {

        public final Affinity affinity;

        public final int offsetIndex;

        public final BlockPos tilePos;

        public ActiveCrystal(Affinity affinity, int offsetIndex, BlockPos tilePos) {
            this.affinity = affinity;
            this.offsetIndex = offsetIndex;
            this.tilePos = tilePos;
        }

        public CompoundTag toNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("affinity", this.affinity.name());
            nbt.putInt("offsetIndex", this.offsetIndex);
            nbt.putLong("position", this.tilePos.asLong());
            return nbt;
        }

        public static EldrinAltarTile.ActiveCrystal fromNBT(CompoundTag nbt) {
            return new EldrinAltarTile.ActiveCrystal(Affinity.valueOf(nbt.getString("affinity")), nbt.getInt("offsetIndex"), BlockPos.of(nbt.getLong("position")));
        }
    }

    public static class CollectedItem {

        public final BlockPos tilePos;

        public final ItemStack stack;

        public CollectedItem(BlockPos pos, ItemStack stack) {
            this.tilePos = pos;
            this.stack = stack;
        }

        public CompoundTag toNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putLong("pos", this.tilePos.asLong());
            this.stack.save(nbt);
            return nbt;
        }

        public static EldrinAltarTile.CollectedItem fromNBT(CompoundTag nbt) {
            return new EldrinAltarTile.CollectedItem(BlockPos.of(nbt.getLong("pos")), ItemStack.of(nbt));
        }
    }

    public static enum Stage {

        IDLE,
        FINDING_POWER,
        CONSUMING_POWER,
        CONSUMING_REAGENTS,
        CRAFTING,
        COMPLETING
    }
}