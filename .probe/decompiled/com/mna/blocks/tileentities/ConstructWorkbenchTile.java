package com.mna.blocks.tileentities;

import com.mna.ManaAndArtifice;
import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.affinity.Affinity;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.IConstructConstruction;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.blocks.artifice.ConstructWorkbenchBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.entities.constructs.animated.Construct;
import com.mna.entities.constructs.animated.ConstructConstruction;
import com.mna.entities.sorcery.AffinityIcon;
import com.mna.events.EventDispatcher;
import com.mna.items.ItemInit;
import com.mna.particles.types.movers.ParticleBezierMover;
import com.mna.particles.types.movers.ParticleLerpMover;
import com.mna.particles.types.movers.ParticleSphereOrbitMover;
import com.mna.particles.types.movers.ParticleVelocityMover;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ConstructWorkbenchTile extends BlockEntity implements GeoBlockEntity {

    private static final String NBT_CRAFTING = "crafting";

    private static final String NBT_CRAFT_TICKS = "craft_ticks";

    private static final String NBT_CRAFTER = "crafter";

    private static final String NBT_NAME = "custom_name";

    private static final String NBT_KNOWN_POS = "known_pos";

    IConstructConstruction construct;

    private boolean crafting = false;

    private int craftTicks = 0;

    private UUID crafter;

    ArrayList<ConstructWorkbenchTile.PosAffinityData> knownPositions;

    private String constructName = null;

    private AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    public ConstructWorkbenchTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.CONSTRUCT_WORKBENCH.get(), pos, state);
        this.construct = new ConstructConstruction();
        this.knownPositions = new ArrayList();
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, ConstructWorkbenchTile tile) {
        if (tile.crafting) {
            if (tile.m_58904_().isClientSide()) {
                Direction facing = (Direction) state.m_61143_(ConstructWorkbenchBlock.FACING);
                Vec3 me = null;
                switch(facing) {
                    case NORTH:
                        me = new Vec3((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 1.65F), (double) pos.m_123343_());
                        break;
                    case SOUTH:
                        me = new Vec3((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 1.65F), (double) ((float) pos.m_123343_() + 1.25F));
                        break;
                    case EAST:
                        me = new Vec3((double) ((float) pos.m_123341_() + 1.0F), (double) ((float) pos.m_123342_() + 1.65F), (double) ((float) pos.m_123343_() + 0.5F));
                        break;
                    case WEST:
                        me = new Vec3((double) pos.m_123341_(), (double) ((float) pos.m_123342_() + 1.65F), (double) ((float) pos.m_123343_() + 0.5F));
                        break;
                    default:
                        return;
                }
                if (tile.knownPositions.size() == 0) {
                    tile.findNearbyPoints();
                }
                for (ConstructWorkbenchTile.PosAffinityData pad : tile.knownPositions) {
                    Vec3 start = new Vec3((double) ((float) pad.getPos().m_123341_() + 0.5F), (double) ((float) pad.getPos().m_123342_() + 0.5F), (double) ((float) pad.getPos().m_123343_() + 0.5F));
                    tile.getForAffinity(pad.aff, start, me);
                }
            }
            tile.craftTicks++;
            if (tile.craftTicks >= 200 && !tile.m_58904_().isClientSide()) {
                tile.spawnConstruct(tile.crafter);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private MAParticleType getForAffinity(Affinity aff, Vec3 start, Vec3 end) {
        MAParticleType particle = null;
        switch(aff) {
            case ARCANE:
                particle = new MAParticleType(ParticleInit.ARCANE_MAGELIGHT.get()).setMaxAge(60).setMover(new ParticleLerpMover(start.x, start.y, start.z, end.x, end.y, end.z));
                break;
            case EARTH:
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.ITEM.get()).setStack(new ItemStack(ItemInit.GREATER_MOTE_EARTH.get())).setScale(0.1F).setMover(new ParticleVelocityMover(0.0, 0.2F, 0.0, true)).setGravity(0.05F), end.x - 1.0 + 2.0 * Math.random(), end.y - 1.4, end.z - 1.0 + 2.0 * Math.random(), 0.0, 0.0, 0.0);
                particle = new MAParticleType(ParticleInit.DUST_LERP.get()).setMover(new ParticleVelocityMover(0.0, 0.15F, 0.0, true)).setMaxAge(60).setScale(0.05F);
                break;
            case ENDER:
                particle = new MAParticleType(ParticleInit.ENDER.get()).setScale(0.15F);
                break;
            case FIRE:
            case HELLFIRE:
                particle = new MAParticleType(ParticleInit.FLAME_LERP.get()).setScale(0.15F).setMover(new ParticleBezierMover(start, end));
                break;
            case LIGHTNING:
                particle = new MAParticleType(ParticleInit.LIGHTNING_BOLT.get());
                break;
            case WATER:
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.WATER.get()).setColor(14, 28, 35).setScale(0.1F), start.x, start.y, start.z, 0.0, 0.0, 0.0);
                particle = new MAParticleType(ParticleInit.WATER_LERP.get()).setScale(0.05F).setColor(0, 128, 255).setMaxAge(60).setMover(new ParticleSphereOrbitMover(end.x, end.y, end.z, 0.05, (180.0 / Math.PI) * (double) (ManaAndArtifice.instance.proxy.getGameTicks() % 360L), 1.25));
                break;
            case ICE:
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.FROST.get()).setColor(14, 28, 35).setScale(0.1F), start.x, start.y, start.z, 0.0, 0.0, 0.0);
                Vec3 newEnd = new Vec3(end.x - 1.0 + Math.random() * 2.0, end.y - 2.0, end.z - 1.0 + Math.random() * 2.0);
                particle = new MAParticleType(ParticleInit.FROST_LERP.get()).setScale(0.1F).setColor(14, 28, 35).setMover(new ParticleLerpMover(newEnd.x, newEnd.y, newEnd.z, newEnd.x, newEnd.y + 3.0, newEnd.z));
                break;
            case WIND:
                Vec3 controlA = start.add(end.subtract(start).scale(0.33F)).add(0.0, 0.25 + Math.random(), 0.0);
                Vec3 controlB = start.add(end.subtract(start).scale(0.66F)).add(0.0, 0.25 + Math.random(), 0.0);
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.AIR_LERP.get()).setScale(0.2F).setColor(10, 10, 10).setMover(new ParticleBezierMover(start, end, controlA, controlB)), start.x, start.y, start.z, 0.0, 0.0, 0.0);
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setScale(0.2F).setColor(10, 10, 10), end.x, end.y - 1.0, end.z, 0.1F, 0.1F, 1.25);
                break;
            case UNKNOWN:
            default:
                particle = new MAParticleType(ParticleInit.SPARKLE_LERP_POINT.get());
        }
        if (particle != null) {
            this.m_58904_().addParticle(particle, start.x, start.y, start.z, end.x, end.y, end.z);
        }
        return particle;
    }

    public int getCraftTicks() {
        return this.craftTicks;
    }

    public ArrayList<ConstructWorkbenchTile.PosAffinityData> getKnownPositions() {
        return this.knownPositions;
    }

    public boolean getIsCrafting() {
        return this.crafting;
    }

    public ItemStack placePart(ItemStack stack) {
        if (!this.crafting && stack.getCount() == 1 && stack.getItem() instanceof ItemConstructPart) {
            ItemStack output = this.construct.setPart(stack);
            if (!this.m_58904_().isClientSide()) {
                this.m_6596_();
                this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
            }
            return output;
        } else {
            return stack;
        }
    }

    public ItemStack popPart() {
        ItemStack output = ItemStack.EMPTY;
        if (this.crafting) {
            return output;
        } else {
            ConstructSlot[] slots = new ConstructSlot[] { ConstructSlot.LEFT_ARM, ConstructSlot.RIGHT_ARM, ConstructSlot.TORSO, ConstructSlot.HEAD, ConstructSlot.LEGS };
            for (ConstructSlot slot : slots) {
                output = this.construct.removePart(slot);
                if (!output.isEmpty()) {
                    break;
                }
            }
            if (!this.m_58904_().isClientSide()) {
                this.m_6596_();
                this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
            }
            return output;
        }
    }

    public void setConstructData(IConstructConstruction data, String name) {
        if (!this.m_58904_().isClientSide()) {
            this.construct = data.copy();
            this.constructName = name;
            this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
        }
    }

    private void spawnConstruct(UUID owner) {
        if (this.construct.isComplete() && !this.m_58904_().isClientSide()) {
            this.crafting = false;
            this.craftTicks = 0;
            this.crafter = null;
            Construct eac = new Construct(this.f_58857_);
            BlockPos pos = this.m_58899_();
            Direction dir = (Direction) this.m_58900_().m_61143_(ConstructWorkbenchBlock.FACING);
            Vec3 fwdDir = new Vec3((double) dir.getStepX(), (double) dir.getStepY(), (double) dir.getStepZ());
            Vec3 vPos = new Vec3((double) ((float) pos.m_123341_() + 0.5F) + fwdDir.x, (double) pos.m_123342_() + 0.5, (double) ((float) pos.m_123343_() + 0.5F) + fwdDir.z);
            eac.m_7678_(vPos.x, vPos.y, vPos.z, 0.0F, 0.0F);
            eac.m_7618_(EntityAnchorArgument.Anchor.FEET, vPos.add(fwdDir.scale(10.0)));
            eac.setConstructParts(this.construct);
            if (this.constructName != null) {
                eac.m_6593_(Component.literal(this.constructName));
                this.constructName = null;
            }
            eac.setOwner(owner);
            HashMap<Affinity, Integer> affinities = new HashMap();
            this.getNearbyEntities().forEach(e -> {
                if (!affinities.containsKey(e.getAffinity())) {
                    affinities.put(e.getAffinity(), 1);
                } else {
                    affinities.put(e.getAffinity(), (Integer) affinities.get(e.getAffinity()) + 1);
                }
                e.m_142687_(Entity.RemovalReason.DISCARDED);
            });
            for (Affinity aff : affinities.keySet()) {
                eac.getConstructData().setAffinityScore(aff, (Integer) affinities.get(aff));
            }
            this.m_58904_().m_7967_(eac);
            this.m_6596_();
            this.construct = new ConstructConstruction();
            this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
            Player player = this.f_58857_.m_46003_(owner);
            if (player != null) {
                EventDispatcher.DispatchConstructCrafted(eac, player);
                if (player instanceof ServerPlayer) {
                    CustomAdvancementTriggers.SUMMON_CONSTRUCT.trigger((ServerPlayer) player, eac);
                }
            }
        }
    }

    private List<AffinityIcon> getNearbyEntities() {
        int search_radius = 5;
        final Vec3 myVec = new Vec3((double) ((float) this.m_58899_().m_123341_() + 0.5F), (double) ((float) this.m_58899_().m_123342_() + 0.5F), (double) ((float) this.m_58899_().m_123343_() + 0.5F));
        AABB bb = new AABB(this.m_58899_()).inflate((double) search_radius);
        return (List<AffinityIcon>) this.m_58904_().m_45976_(AffinityIcon.class, bb).stream().map(e -> e).sorted(new Comparator<AffinityIcon>() {

            public int compare(AffinityIcon o1, AffinityIcon o2) {
                Double d1 = o1.m_20182_().distanceToSqr(myVec);
                Double d2 = o2.m_20182_().distanceToSqr(myVec);
                return d1.compareTo(d2);
            }
        }).limit(8L).collect(Collectors.toList());
    }

    private boolean findNearbyPoints() {
        List<AffinityIcon> entities = this.getNearbyEntities();
        if (entities.size() < 8) {
            return false;
        } else {
            this.knownPositions.clear();
            entities.stream().forEach(e -> this.knownPositions.add(new ConstructWorkbenchTile.PosAffinityData(BlockPos.containing(e.m_20182_()), e.getAffinity())));
            return true;
        }
    }

    public boolean isEmpty() {
        return this.construct.isEmpty();
    }

    public boolean startCrafting(@Nonnull Player player) {
        if (!this.crafting && this.construct.isComplete() && !this.m_58904_().isClientSide() && this.findNearbyPoints()) {
            this.crafting = true;
            this.craftTicks = 0;
            this.crafter = player.m_20148_();
            this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
            return true;
        } else {
            return false;
        }
    }

    private void writeKnownPositions(CompoundTag nbt) {
        CompoundTag known = new CompoundTag();
        known.putInt("count", this.knownPositions.size());
        for (int i = 0; i < this.knownPositions.size(); i++) {
            known.put("pos" + i, NbtUtils.writeBlockPos(((ConstructWorkbenchTile.PosAffinityData) this.knownPositions.get(i)).getPos()));
            known.putString("aff" + i, ((ConstructWorkbenchTile.PosAffinityData) this.knownPositions.get(i)).getAffinity().toString());
        }
        nbt.put("known_pos", known);
    }

    private void readKnownPositions(CompoundTag nbt) {
        if (nbt.contains("known_pos")) {
            CompoundTag known = nbt.getCompound("known_pos");
            int count = known.getInt("count");
            this.knownPositions.clear();
            for (int i = 0; i < count; i++) {
                try {
                    BlockPos pos = NbtUtils.readBlockPos(known.getCompound("pos" + i));
                    Affinity aff = Affinity.valueOf(known.getString("aff" + i));
                    this.knownPositions.add(new ConstructWorkbenchTile.PosAffinityData(pos, aff));
                } catch (Exception var7) {
                }
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag base) {
        this.construct.WriteNBT(base);
        base.putBoolean("crafting", this.crafting);
        base.putInt("craft_ticks", this.craftTicks);
        base.putString("crafter", this.crafter != null ? this.crafter.toString() : "");
        if (this.constructName != null) {
            base.putString("custom_name", this.constructName);
        }
        this.writeKnownPositions(base);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.construct.ReadNBT(nbt);
        if (nbt.contains("crafting")) {
            this.crafting = nbt.getBoolean("crafting");
        }
        if (nbt.contains("craft_ticks")) {
            this.craftTicks = nbt.getInt("craft_ticks");
        }
        if (nbt.contains("crafter")) {
            String crafterUUIDString = nbt.getString("crafter");
            if (!crafterUUIDString.isEmpty()) {
                this.crafter = UUID.fromString(crafterUUIDString);
            }
        }
        if (nbt.contains("custom_name")) {
            this.constructName = nbt.getString("custom_name");
        }
        this.readKnownPositions(nbt);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag nbt = pkt.getTag();
        boolean wasCrafting = this.crafting;
        this.construct.ReadNBT(nbt);
        if (nbt.contains("crafting")) {
            this.crafting = nbt.getBoolean("crafting");
        }
        if (nbt.contains("craft_ticks")) {
            this.craftTicks = nbt.getInt("craft_ticks");
        }
        if (this.crafting && !wasCrafting) {
            BlockPos soundPos = this.m_58899_();
            this.m_58904_().playSound(ManaAndArtifice.instance.proxy.getClientPlayer(), (double) soundPos.m_123341_(), (double) soundPos.m_123342_(), (double) soundPos.m_123343_(), SFX.Event.Block.GANTRY_SUMMON, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        this.readKnownPositions(nbt);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag payload = super.getUpdateTag();
        this.construct.WriteNBT(payload);
        payload.putBoolean("crafting", this.crafting);
        payload.putInt("craft_ticks", this.craftTicks);
        this.writeKnownPositions(payload);
        return payload;
    }

    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    public IConstructConstruction getConstruct() {
        return this.construct;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        AnimationController<ConstructWorkbenchTile> controller = new AnimationController<>(this, state -> this.crafting ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct_arch.assemble").thenLoop("animation.construct_arch.assemble_idle")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct_arch.assemble_end").thenLoop("animation.construct_arch.idle")));
        registrar.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.m_58899_()).inflate(5.0);
    }

    class PosAffinityData {

        BlockPos pos;

        Affinity aff;

        public PosAffinityData(BlockPos pos, Affinity affinity) {
            this.aff = affinity;
            this.pos = pos;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public Affinity getAffinity() {
            return this.aff;
        }
    }
}