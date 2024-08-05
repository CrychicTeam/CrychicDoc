package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.upgrade.EntropicEnumeratorUpgrade;
import com.rekindled.embers.util.Misc;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3i;

public class EntropicEnumeratorBlockEntity extends BlockEntity implements IExtraCapabilityInformation {

    public EntropicEnumeratorBlockEntity.Cubie[][][] visualCube = new EntropicEnumeratorBlockEntity.Cubie[2][2][2];

    public int previousMove = -10;

    public EntropicEnumeratorBlockEntity.Cubie[][][] cube = new EntropicEnumeratorBlockEntity.Cubie[][][] { { { new EntropicEnumeratorBlockEntity.Cubie(new Vector3d(-1.0, -1.0, -1.0), new Vector3d(1.0, 2.0, 3.0)), new EntropicEnumeratorBlockEntity.Cubie(new Vector3d(-1.0, -1.0, 1.0), new Vector3d(1.0, 2.0, 4.0)) }, { new EntropicEnumeratorBlockEntity.Cubie(new Vector3d(-1.0, 1.0, -1.0), new Vector3d(1.0, 5.0, 3.0)), new EntropicEnumeratorBlockEntity.Cubie(new Vector3d(-1.0, 1.0, 1.0), new Vector3d(1.0, 5.0, 4.0)) } }, { { new EntropicEnumeratorBlockEntity.Cubie(new Vector3d(1.0, -1.0, -1.0), new Vector3d(6.0, 2.0, 3.0)), new EntropicEnumeratorBlockEntity.Cubie(new Vector3d(1.0, -1.0, 1.0), new Vector3d(6.0, 2.0, 4.0)) }, { new EntropicEnumeratorBlockEntity.Cubie(new Vector3d(1.0, 1.0, -1.0), new Vector3d(6.0, 5.0, 3.0)), new EntropicEnumeratorBlockEntity.Cubie(new Vector3d(1.0, 1.0, 1.0), new Vector3d(6.0, 5.0, 4.0)) } } };

    public long nextMoveTime = -1L;

    public int moveOffset = -1;

    public EntropicEnumeratorBlockEntity.Move[] moveQueue = new EntropicEnumeratorBlockEntity.Move[0];

    public int[] offsetQueue = new int[0];

    public boolean solving;

    public static Random seededRand = new Random();

    public EntropicEnumeratorUpgrade upgrade = new EntropicEnumeratorUpgrade(this);

    public static final int GODS_NUMBER = 14;

    public static final EntropicEnumeratorBlockEntity.Move[] B = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.B };

    public static final EntropicEnumeratorBlockEntity.Move[] L = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L };

    public static final EntropicEnumeratorBlockEntity.Move[] L_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L_ };

    public static final EntropicEnumeratorBlockEntity.Move[] B_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.B_ };

    public static final EntropicEnumeratorBlockEntity.Move[] U_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.U_ };

    public static final EntropicEnumeratorBlockEntity.Move[] DB = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.B };

    public static final EntropicEnumeratorBlockEntity.Move[] D_B = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.B };

    public static final EntropicEnumeratorBlockEntity.Move[] D2B = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D2, EntropicEnumeratorBlockEntity.Move.B };

    public static final EntropicEnumeratorBlockEntity.Move[] LB_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L, EntropicEnumeratorBlockEntity.Move.B_ };

    public static final EntropicEnumeratorBlockEntity.Move[] DLB_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.L, EntropicEnumeratorBlockEntity.Move.B_ };

    public static final EntropicEnumeratorBlockEntity.Move[] D2LB_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D2, EntropicEnumeratorBlockEntity.Move.L, EntropicEnumeratorBlockEntity.Move.B_ };

    public static final EntropicEnumeratorBlockEntity.Move[] B2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.B2 };

    public static final EntropicEnumeratorBlockEntity.Move[] L2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L2 };

    public static final EntropicEnumeratorBlockEntity.Move[] DB2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.B2 };

    public static final EntropicEnumeratorBlockEntity.Move[] D_B2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.B2 };

    public static final EntropicEnumeratorBlockEntity.Move[] D2B2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D2, EntropicEnumeratorBlockEntity.Move.B2 };

    public static final EntropicEnumeratorBlockEntity.Move[] L_B_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L_, EntropicEnumeratorBlockEntity.Move.B_ };

    public static final EntropicEnumeratorBlockEntity.Move[] LD_B2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L, EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.B2 };

    public static final EntropicEnumeratorBlockEntity.Move[] BDL_U_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.B, EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.L_, EntropicEnumeratorBlockEntity.Move.U_ };

    public static final EntropicEnumeratorBlockEntity.Move[] B2DL_U_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.B2, EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.L_, EntropicEnumeratorBlockEntity.Move.U_ };

    public static final EntropicEnumeratorBlockEntity.Move[] L_U_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L_, EntropicEnumeratorBlockEntity.Move.U_ };

    public static final EntropicEnumeratorBlockEntity.Move[] DL_U_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.L_, EntropicEnumeratorBlockEntity.Move.U_ };

    public static final EntropicEnumeratorBlockEntity.Move[] LU_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L, EntropicEnumeratorBlockEntity.Move.U_ };

    public static final EntropicEnumeratorBlockEntity.Move[] BLU_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.B, EntropicEnumeratorBlockEntity.Move.L, EntropicEnumeratorBlockEntity.Move.U_ };

    public static final EntropicEnumeratorBlockEntity.Move[] DL = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.L };

    public static final EntropicEnumeratorBlockEntity.Move[] L2D_L = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L2, EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.L };

    public static final EntropicEnumeratorBlockEntity.Move[] DL2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.L2 };

    public static final EntropicEnumeratorBlockEntity.Move[] BL_B_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.B, EntropicEnumeratorBlockEntity.Move.L_, EntropicEnumeratorBlockEntity.Move.B_ };

    public static final EntropicEnumeratorBlockEntity.Move[] D2L = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D2, EntropicEnumeratorBlockEntity.Move.L };

    public static final EntropicEnumeratorBlockEntity.Move[] D2L2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D2, EntropicEnumeratorBlockEntity.Move.L2 };

    public static final EntropicEnumeratorBlockEntity.Move[] D_L = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.L };

    public static final EntropicEnumeratorBlockEntity.Move[] D_L2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.L2 };

    public static final EntropicEnumeratorBlockEntity.Move[] D2L_U_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D2, EntropicEnumeratorBlockEntity.Move.L_, EntropicEnumeratorBlockEntity.Move.U_ };

    public static final EntropicEnumeratorBlockEntity.Move[] D_L_U_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.L_, EntropicEnumeratorBlockEntity.Move.U_ };

    public static final EntropicEnumeratorBlockEntity.Move[] F_DF = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.F_, EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.F };

    public static final EntropicEnumeratorBlockEntity.Move[] LDLD_L2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L, EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.L, EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.L2 };

    public static final EntropicEnumeratorBlockEntity.Move[] F_D_F_DF2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.F_, EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.F_, EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.F2 };

    public static final EntropicEnumeratorBlockEntity.Move[] DL2D_L2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.L2, EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.L2 };

    public static final EntropicEnumeratorBlockEntity.Move[] LD_L_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L, EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.L_ };

    public static final EntropicEnumeratorBlockEntity.Move[] F_D2F = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.F_, EntropicEnumeratorBlockEntity.Move.D2, EntropicEnumeratorBlockEntity.Move.F };

    public static final EntropicEnumeratorBlockEntity.Move[] F_D_F = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.F_, EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.F };

    public static final EntropicEnumeratorBlockEntity.Move[] LDL_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L, EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.L_ };

    public static final EntropicEnumeratorBlockEntity.Move[] L2D_L2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L2, EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.L2 };

    public static final EntropicEnumeratorBlockEntity.Move[] F2DF2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.F2, EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.F2 };

    public static final EntropicEnumeratorBlockEntity.Move[] D2F_DF = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D2, EntropicEnumeratorBlockEntity.Move.F_, EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.F };

    public static final EntropicEnumeratorBlockEntity.Move[] D_L2D_L2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.L2, EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.L2 };

    public static final EntropicEnumeratorBlockEntity.Move[] D_LD2L_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.L, EntropicEnumeratorBlockEntity.Move.D2, EntropicEnumeratorBlockEntity.Move.L_ };

    public static final EntropicEnumeratorBlockEntity.Move[] LD2L_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L, EntropicEnumeratorBlockEntity.Move.D2, EntropicEnumeratorBlockEntity.Move.L_ };

    public static final EntropicEnumeratorBlockEntity.Move[] U = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.U };

    public static final EntropicEnumeratorBlockEntity.Move[] U2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.U2 };

    public static final EntropicEnumeratorBlockEntity.Move[] D = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D };

    public static final EntropicEnumeratorBlockEntity.Move[] D_ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D_ };

    public static final EntropicEnumeratorBlockEntity.Move[] D2 = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.D2 };

    public static final EntropicEnumeratorBlockEntity.Move[] OLL_H = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.R2, EntropicEnumeratorBlockEntity.Move.U2, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U2, EntropicEnumeratorBlockEntity.Move.R2 };

    public static final EntropicEnumeratorBlockEntity.Move[] OLL_PI = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U2, EntropicEnumeratorBlockEntity.Move.R2, EntropicEnumeratorBlockEntity.Move.U_, EntropicEnumeratorBlockEntity.Move.R2, EntropicEnumeratorBlockEntity.Move.U_, EntropicEnumeratorBlockEntity.Move.R2, EntropicEnumeratorBlockEntity.Move.U2, EntropicEnumeratorBlockEntity.Move.R };

    public static final EntropicEnumeratorBlockEntity.Move[] OLL_ANTISUNE = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U2, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.U_, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U_, EntropicEnumeratorBlockEntity.Move.R_ };

    public static final EntropicEnumeratorBlockEntity.Move[] OLL_SUNE = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.U, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U2, EntropicEnumeratorBlockEntity.Move.R_ };

    public static final EntropicEnumeratorBlockEntity.Move[] OLL_L = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.F, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.F_, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U_, EntropicEnumeratorBlockEntity.Move.R_ };

    public static final EntropicEnumeratorBlockEntity.Move[] OLL_T = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.U_, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.F, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.F_ };

    public static final EntropicEnumeratorBlockEntity.Move[] OLL_U = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.F, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.U_, EntropicEnumeratorBlockEntity.Move.F_ };

    public static final EntropicEnumeratorBlockEntity.Move[] PBL_ADJ_ADJ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.R2, EntropicEnumeratorBlockEntity.Move.U_, EntropicEnumeratorBlockEntity.Move.B2, EntropicEnumeratorBlockEntity.Move.U2, EntropicEnumeratorBlockEntity.Move.R2, EntropicEnumeratorBlockEntity.Move.U_, EntropicEnumeratorBlockEntity.Move.R2 };

    public static final EntropicEnumeratorBlockEntity.Move[] PBL_ADJ_DIAG = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U_, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.F2, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.U, EntropicEnumeratorBlockEntity.Move.R_ };

    public static final EntropicEnumeratorBlockEntity.Move[] PBL_DIAG_ADJ = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.F2, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.R };

    public static final EntropicEnumeratorBlockEntity.Move[] PBL_DIAG_DIAG = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.L2, EntropicEnumeratorBlockEntity.Move.B2, EntropicEnumeratorBlockEntity.Move.R2 };

    public static final EntropicEnumeratorBlockEntity.Move[] PBL_ADJ_U = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.R2, EntropicEnumeratorBlockEntity.Move.F2, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.F2, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.F_, EntropicEnumeratorBlockEntity.Move.R };

    public static final EntropicEnumeratorBlockEntity.Move[] PBL_ADJ_D = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.R2, EntropicEnumeratorBlockEntity.Move.F2, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.F2, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.F, EntropicEnumeratorBlockEntity.Move.R_ };

    public static final EntropicEnumeratorBlockEntity.Move[] PBL_DIAG_U = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U_, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.U_, EntropicEnumeratorBlockEntity.Move.F2, EntropicEnumeratorBlockEntity.Move.U_, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.U, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.U, EntropicEnumeratorBlockEntity.Move.F2 };

    public static final EntropicEnumeratorBlockEntity.Move[] PBL_DIAG_D = new EntropicEnumeratorBlockEntity.Move[] { EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.F2, EntropicEnumeratorBlockEntity.Move.D, EntropicEnumeratorBlockEntity.Move.R_, EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.R, EntropicEnumeratorBlockEntity.Move.D_, EntropicEnumeratorBlockEntity.Move.F2 };

    public static int solvingMoveTime = 4;

    public static int moveTime = 13;

    public static int queueSize = 10;

    public static int queueTime = moveTime * queueSize;

    public EntropicEnumeratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.ENTROPIC_ENUMERATOR_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    String key = "cubie_" + x + "_" + y + "_" + z;
                    if (nbt.contains(key)) {
                        CompoundTag cubieTag = nbt.getCompound(key);
                        this.cube[x][y][z] = new EntropicEnumeratorBlockEntity.Cubie(this.cube[x][y][z].basePosition, this.cube[x][y][z].baseColors, new Quaterniond(cubieTag.getDouble("qx"), cubieTag.getDouble("qy"), cubieTag.getDouble("qz"), cubieTag.getDouble("qw")));
                    }
                }
            }
        }
        if (nbt.contains("nextMoveTime")) {
            this.nextMoveTime = nbt.getLong("nextMoveTime");
        }
        if (nbt.contains("queue")) {
            ListTag queue = nbt.getList("queue", 10);
            this.moveQueue = new EntropicEnumeratorBlockEntity.Move[queue.size()];
            this.offsetQueue = new int[queue.size()];
            for (int i = 0; i < queue.size(); i++) {
                this.moveQueue[i] = EntropicEnumeratorBlockEntity.Move.valueOf(queue.getCompound(i).getString("move"));
                this.offsetQueue[i] = queue.getCompound(i).getInt("offset");
            }
        }
        this.moveOffset = nbt.getInt("moveOffset");
        this.solving = nbt.getBoolean("solving");
        this.previousMove = -10;
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.save(nbt);
    }

    public void save(CompoundTag nbt) {
        super.saveAdditional(nbt);
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    CompoundTag cubieTag = new CompoundTag();
                    cubieTag.putDouble("qx", this.cube[x][y][z].rotation.x);
                    cubieTag.putDouble("qy", this.cube[x][y][z].rotation.y);
                    cubieTag.putDouble("qz", this.cube[x][y][z].rotation.z);
                    cubieTag.putDouble("qw", this.cube[x][y][z].rotation.w);
                    nbt.put("cubie_" + x + "_" + y + "_" + z, cubieTag);
                }
            }
        }
        if (this.nextMoveTime > 0L) {
            nbt.putLong("nextMoveTime", this.nextMoveTime);
        }
        ListTag queue = new ListTag();
        for (int i = 0; i < this.moveQueue.length; i++) {
            CompoundTag entry = new CompoundTag();
            entry.putString("move", this.moveQueue[i].name());
            entry.putInt("offset", this.offsetQueue[i]);
            queue.add(i, (Tag) entry);
        }
        nbt.put("queue", queue);
        nbt.putInt("moveOffset", this.moveOffset);
        nbt.putBoolean("solving", this.solving);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        this.save(nbt);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void solveInefficient() {
        this.solving = true;
        this.applyCurrentQueue(this.cube);
        this.nextMoveTime = this.f_58857_.getGameTime() + (long) (moveTime / 2);
        EntropicEnumeratorBlockEntity.Cubie[][][] cubeClone = new EntropicEnumeratorBlockEntity.Cubie[2][2][2];
        boolean done = false;
        EntropicEnumeratorBlockEntity.Move[] moves = new EntropicEnumeratorBlockEntity.Move[14];
        for (int i = 0; (double) i < Math.pow((double) EntropicEnumeratorBlockEntity.Move.quarterMoves.length, 14.0) && !done; i++) {
            for (int j = 0; j < 14; j++) {
                moves[j] = EntropicEnumeratorBlockEntity.Move.quarterMoves[i % (EntropicEnumeratorBlockEntity.Move.quarterMoves.length * (j + 1)) / (j + 1)];
            }
            for (int x = 0; x < 2; x++) {
                for (int y = 0; y < 2; y++) {
                    for (int z = 0; z < 2; z++) {
                        cubeClone[x][y][z] = new EntropicEnumeratorBlockEntity.Cubie(this.cube[x][y][z].basePosition, this.cube[x][y][z].baseColors, new Quaterniond(this.cube[x][y][z].rotation));
                    }
                }
            }
            for (int k = 0; k < 14; k++) {
                moves[k].makeMove(cubeClone);
                if (isSolved(cubeClone)) {
                    this.moveQueue = new EntropicEnumeratorBlockEntity.Move[k];
                    for (int l = 0; l < k; l++) {
                        this.moveQueue[l] = moves[l];
                    }
                    done = true;
                    break;
                }
            }
        }
        this.offsetQueue = new int[this.moveQueue.length];
        for (int i = 0; i < this.moveQueue.length; i++) {
            this.offsetQueue[i] = moveTime;
        }
        this.offsetQueue[0] = 0;
        this.setChanged();
    }

    public static void updateColors(EntropicEnumeratorBlockEntity.Cubie[][][] cube, Vector3d[][][] colors) {
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    colors[(int) (cube[x][y][z].getPos().x / 2.0 + 1.0)][(int) (cube[x][y][z].getPos().y / 2.0 + 1.0)][(int) (cube[x][y][z].getPos().z / 2.0 + 1.0)] = cube[x][y][z].getColors();
                }
            }
        }
    }

    public void applyCurrentQueue(EntropicEnumeratorBlockEntity.Cubie[][][] cube) {
        int currentMove = this.moveQueue.length - 1;
        long turnTicks = this.f_58857_.getGameTime() - this.nextMoveTime;
        for (int i = 0; i < this.moveQueue.length - 1; i++) {
            turnTicks -= (long) this.offsetQueue[i];
            if ((float) turnTicks < (float) this.offsetQueue[i + 1]) {
                currentMove = i;
                break;
            }
        }
        for (int ix = 0; ix <= currentMove; ix++) {
            this.moveQueue[ix].makeMove(cube);
        }
    }

    public void solve(boolean direct, int delay) {
        this.solving = true;
        this.applyCurrentQueue(this.cube);
        EntropicEnumeratorBlockEntity.Cubie[][][] cubeClone = new EntropicEnumeratorBlockEntity.Cubie[2][2][2];
        Vector3d[][][] colors = new Vector3d[2][2][2];
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    cubeClone[x][y][z] = new EntropicEnumeratorBlockEntity.Cubie(this.cube[x][y][z].basePosition, this.cube[x][y][z].baseColors, new Quaterniond(this.cube[x][y][z].rotation));
                    colors[(int) (this.cube[x][y][z].getPos().x / 2.0 + 1.0)][(int) (this.cube[x][y][z].getPos().y / 2.0 + 1.0)][(int) (this.cube[x][y][z].getPos().z / 2.0 + 1.0)] = this.cube[x][y][z].getColors();
                }
            }
        }
        ArrayList<EntropicEnumeratorBlockEntity.Move[]> algorithms = new ArrayList();
        double topColor = colors[0][1][0].y;
        if (colors[0][1][1].y != topColor) {
            if (colors[0][0][1].x == topColor) {
                algorithms.add(B);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, B);
            } else if (colors[1][1][1].x == topColor) {
                algorithms.add(B_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, B_);
            } else if (colors[1][1][0].y == topColor) {
                algorithms.add(U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U_);
            } else if (colors[1][0][1].y == topColor) {
                algorithms.add(B2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, B2);
            } else if (colors[1][0][1].x == topColor) {
                algorithms.add(LB_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, LB_);
            } else if (colors[1][1][0].x == topColor) {
                algorithms.add(L_B_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, L_B_);
            } else if (colors[1][0][1].z == topColor) {
                algorithms.add(D_B);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D_B);
            } else if (colors[0][0][0].z == topColor) {
                algorithms.add(DB);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, DB);
            } else if (colors[1][0][0].z == topColor) {
                algorithms.add(L_U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, L_U_);
            } else if (colors[1][1][1].z == topColor) {
                algorithms.add(LU_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, LU_);
            } else if (colors[0][0][1].y == topColor) {
                algorithms.add(DB2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, DB2);
            } else if (colors[1][0][0].x == topColor) {
                algorithms.add(D2B);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D2B);
            } else if (colors[1][0][0].y == topColor) {
                algorithms.add(D_B2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D_B2);
            } else if (colors[0][0][0].y == topColor) {
                algorithms.add(D2B2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D2B2);
            } else if (colors[0][1][1].z == topColor) {
                algorithms.add(BLU_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, BLU_);
            } else if (colors[0][0][1].z == topColor) {
                algorithms.add(DLB_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, DLB_);
            } else if (colors[1][1][0].z == topColor) {
                algorithms.add(LD_B2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, LD_B2);
            } else if (colors[0][0][0].x == topColor) {
                algorithms.add(D2LB_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D2LB_);
            } else if (colors[1][1][1].y == topColor) {
                algorithms.add(BDL_U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, BDL_U_);
            } else if (colors[0][1][1].x == topColor) {
                algorithms.add(B2DL_U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, B2DL_U_);
            }
            updateColors(cubeClone, colors);
        }
        if (colors[1][1][1].y != topColor) {
            if (colors[1][0][1].z == topColor) {
                algorithms.add(L);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, L);
            } else if (colors[1][1][0].z == topColor) {
                algorithms.add(L_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, L_);
            } else if (colors[1][0][0].y == topColor) {
                algorithms.add(L2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, L2);
            } else if (colors[1][1][0].y == topColor) {
                algorithms.add(U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U_);
            } else if (colors[0][0][1].x == topColor) {
                algorithms.add(DL);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, DL);
            } else if (colors[1][0][0].z == topColor) {
                algorithms.add(L_U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, L_U_);
            } else if (colors[1][1][1].z == topColor) {
                algorithms.add(LU_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, LU_);
            } else if (colors[1][0][0].x == topColor) {
                algorithms.add(D_L);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D_L);
            } else if (colors[0][0][0].y == topColor) {
                algorithms.add(D_L2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D_L2);
            } else if (colors[1][0][1].y == topColor) {
                algorithms.add(DL2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, DL2);
            } else if (colors[0][0][0].z == topColor) {
                algorithms.add(D2L);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D2L);
            } else if (colors[0][0][1].y == topColor) {
                algorithms.add(D2L2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D2L2);
            } else if (colors[1][1][0].x == topColor) {
                algorithms.add(BL_B_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, BL_B_);
            } else if (colors[1][0][1].x == topColor) {
                algorithms.add(DL_U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, DL_U_);
            } else if (colors[0][0][0].x == topColor) {
                algorithms.add(D_L_U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D_L_U_);
            } else if (colors[1][1][1].x == topColor) {
                algorithms.add(L2D_L);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, L2D_L);
            } else if (colors[0][0][1].z == topColor) {
                algorithms.add(D2L_U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D2L_U_);
            }
            updateColors(cubeClone, colors);
        }
        if (colors[1][1][0].y != topColor) {
            if (colors[1][0][1].z == topColor) {
                algorithms.add(F_DF);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, F_DF);
            } else if (colors[1][1][0].z == topColor) {
                algorithms.add(LDLD_L2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, LDLD_L2);
            } else if (colors[1][0][0].y == topColor) {
                algorithms.add(DL2D_L2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, DL2D_L2);
            } else if (colors[0][0][1].x == topColor) {
                algorithms.add(F_D2F);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, F_D2F);
            } else if (colors[1][0][0].z == topColor) {
                algorithms.add(F_D_F);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, F_D_F);
            } else if (colors[1][0][0].x == topColor) {
                algorithms.add(LDL_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, LDL_);
            } else if (colors[0][0][0].y == topColor) {
                algorithms.add(L2D_L2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, L2D_L2);
            } else if (colors[1][0][1].y == topColor) {
                algorithms.add(F2DF2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, F2DF2);
            } else if (colors[0][0][0].z == topColor) {
                algorithms.add(D2F_DF);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D2F_DF);
            } else if (colors[0][0][1].y == topColor) {
                algorithms.add(D_L2D_L2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D_L2D_L2);
            } else if (colors[1][1][0].x == topColor) {
                algorithms.add(F_D_F_DF2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, F_D_F_DF2);
            } else if (colors[1][0][1].x == topColor) {
                algorithms.add(D_LD2L_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D_LD2L_);
            } else if (colors[0][0][0].x == topColor) {
                algorithms.add(LD_L_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, LD_L_);
            } else if (colors[0][0][1].z == topColor) {
                algorithms.add(LD2L_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, LD2L_);
            }
            updateColors(cubeClone, colors);
        }
        topColor = 7.0 - topColor;
        int correctFaces = 0;
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                if (colors[x][0][z].y == topColor) {
                    correctFaces++;
                }
            }
        }
        if (correctFaces != 4) {
            algorithms.add(PBL_DIAG_DIAG);
            EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, PBL_DIAG_DIAG);
            updateColors(cubeClone, colors);
        }
        if (correctFaces == 0) {
            if (colors[0][1][0].z == topColor && colors[1][1][0].z == topColor) {
                if (colors[0][1][1].z == topColor) {
                    algorithms.add(OLL_H);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_H);
                } else {
                    algorithms.add(U);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U);
                    algorithms.add(OLL_PI);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_PI);
                }
            } else if (colors[0][1][0].x == topColor && colors[0][1][1].x == topColor) {
                if (colors[1][1][0].x == topColor) {
                    algorithms.add(U_);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U_);
                    algorithms.add(OLL_H);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_H);
                } else {
                    algorithms.add(U2);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U2);
                    algorithms.add(OLL_PI);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_PI);
                }
            } else if (colors[0][1][1].z == topColor && colors[1][1][1].z == topColor) {
                algorithms.add(U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U_);
                algorithms.add(OLL_PI);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_PI);
            } else {
                algorithms.add(OLL_PI);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_PI);
            }
        } else if (correctFaces == 1) {
            if (colors[0][1][0].y == topColor) {
                if (colors[0][1][1].x == topColor) {
                    algorithms.add(U);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U);
                    algorithms.add(OLL_SUNE);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_SUNE);
                } else {
                    algorithms.add(U_);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U_);
                    algorithms.add(OLL_ANTISUNE);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_ANTISUNE);
                }
            } else if (colors[0][1][1].y == topColor) {
                if (colors[1][1][1].z == topColor) {
                    algorithms.add(U2);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U2);
                    algorithms.add(OLL_SUNE);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_SUNE);
                } else {
                    algorithms.add(OLL_ANTISUNE);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_ANTISUNE);
                }
            } else if (colors[1][1][1].y == topColor) {
                if (colors[1][1][0].x == topColor) {
                    algorithms.add(U_);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U_);
                    algorithms.add(OLL_SUNE);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_SUNE);
                } else {
                    algorithms.add(U);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U);
                    algorithms.add(OLL_ANTISUNE);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_ANTISUNE);
                }
            } else if (colors[1][1][0].y == topColor) {
                if (colors[0][1][0].z == topColor) {
                    algorithms.add(OLL_SUNE);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_SUNE);
                } else {
                    algorithms.add(U2);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U2);
                    algorithms.add(OLL_ANTISUNE);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_ANTISUNE);
                }
            }
        } else if (correctFaces == 2) {
            if (colors[0][1][0].y == topColor) {
                if (colors[0][1][1].y == topColor) {
                    if (colors[1][1][0].z == topColor) {
                        algorithms.add(OLL_T);
                        EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_T);
                    } else {
                        algorithms.add(OLL_U);
                        EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_U);
                    }
                } else if (colors[1][1][0].y == topColor) {
                    algorithms.add(U_);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U_);
                    if (colors[1][1][1].x == topColor) {
                        algorithms.add(OLL_T);
                        EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_T);
                    } else {
                        algorithms.add(OLL_U);
                        EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_U);
                    }
                } else if (colors[1][1][1].y == topColor) {
                    if (colors[1][1][0].z == topColor) {
                        algorithms.add(OLL_L);
                        EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_L);
                    } else {
                        algorithms.add(U2);
                        EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U2);
                        algorithms.add(OLL_L);
                        EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_L);
                    }
                }
            } else if (colors[1][1][1].y == topColor) {
                if (colors[0][1][1].y == topColor) {
                    algorithms.add(U);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U);
                    if (colors[1][1][0].x == topColor) {
                        algorithms.add(OLL_T);
                        EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_T);
                    } else {
                        algorithms.add(OLL_U);
                        EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_U);
                    }
                } else if (colors[1][1][0].y == topColor) {
                    algorithms.add(U2);
                    EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U2);
                    if (colors[0][1][0].z == topColor) {
                        algorithms.add(OLL_T);
                        EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_T);
                    } else {
                        algorithms.add(OLL_U);
                        EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_U);
                    }
                }
            } else if (colors[0][1][0].z == topColor) {
                algorithms.add(U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U_);
                algorithms.add(OLL_L);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_L);
            } else {
                algorithms.add(U);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U);
                algorithms.add(OLL_L);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, OLL_L);
            }
        }
        if (correctFaces != 4) {
            updateColors(cubeClone, colors);
        }
        EntropicEnumeratorBlockEntity.PBLState topState;
        if (colors[0][1][0].z == colors[1][1][0].z) {
            if (colors[0][1][1].z == colors[1][1][1].z) {
                topState = EntropicEnumeratorBlockEntity.PBLState.SOLVED;
            } else {
                topState = EntropicEnumeratorBlockEntity.PBLState.ADJ;
            }
        } else if (colors[0][1][1].z != colors[1][1][1].z && colors[0][1][0].x != colors[0][1][1].x && colors[1][1][0].x != colors[1][1][1].x) {
            topState = EntropicEnumeratorBlockEntity.PBLState.DIAG;
        } else {
            topState = EntropicEnumeratorBlockEntity.PBLState.ADJ;
        }
        EntropicEnumeratorBlockEntity.PBLState bottomState;
        if (colors[0][0][0].z == colors[1][0][0].z) {
            if (colors[0][0][1].z == colors[1][0][1].z) {
                bottomState = EntropicEnumeratorBlockEntity.PBLState.SOLVED;
            } else {
                bottomState = EntropicEnumeratorBlockEntity.PBLState.ADJ;
            }
        } else if (colors[0][0][1].z != colors[1][0][1].z && colors[0][0][0].x != colors[0][0][1].x && colors[1][0][0].x != colors[1][0][1].x) {
            bottomState = EntropicEnumeratorBlockEntity.PBLState.DIAG;
        } else {
            bottomState = EntropicEnumeratorBlockEntity.PBLState.ADJ;
        }
        if (topState == EntropicEnumeratorBlockEntity.PBLState.ADJ && bottomState == EntropicEnumeratorBlockEntity.PBLState.ADJ) {
            if (colors[0][1][0].x == colors[0][1][1].x) {
                algorithms.add(U);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U);
            } else if (colors[1][1][0].x == colors[1][1][1].x) {
                algorithms.add(U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U_);
            } else if (colors[0][1][1].z == colors[1][1][1].z) {
                algorithms.add(U2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U2);
            }
            if (colors[0][0][0].x == colors[0][0][1].x) {
                algorithms.add(D_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D_);
            } else if (colors[1][0][0].x == colors[1][0][1].x) {
                algorithms.add(D);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D);
            } else if (colors[0][0][1].z == colors[1][0][1].z) {
                algorithms.add(D2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D2);
            }
            algorithms.add(PBL_ADJ_ADJ);
            EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, PBL_ADJ_ADJ);
        } else if (topState == EntropicEnumeratorBlockEntity.PBLState.ADJ && bottomState == EntropicEnumeratorBlockEntity.PBLState.DIAG) {
            if (colors[0][1][0].x == colors[0][1][1].x) {
                algorithms.add(U);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U);
            } else if (colors[1][1][0].x == colors[1][1][1].x) {
                algorithms.add(U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U_);
            } else if (colors[0][1][1].z == colors[1][1][1].z) {
                algorithms.add(U2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U2);
            }
            algorithms.add(PBL_ADJ_DIAG);
            EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, PBL_ADJ_DIAG);
        } else if (topState == EntropicEnumeratorBlockEntity.PBLState.DIAG && bottomState == EntropicEnumeratorBlockEntity.PBLState.ADJ) {
            if (colors[0][0][0].x == colors[0][0][1].x) {
                algorithms.add(D_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D_);
            } else if (colors[1][0][0].x == colors[1][0][1].x) {
                algorithms.add(D);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D);
            } else if (colors[0][0][1].z == colors[1][0][1].z) {
                algorithms.add(D2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D2);
            }
            algorithms.add(PBL_DIAG_ADJ);
            EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, PBL_DIAG_ADJ);
        } else if (topState == EntropicEnumeratorBlockEntity.PBLState.DIAG && bottomState == EntropicEnumeratorBlockEntity.PBLState.DIAG) {
            algorithms.add(PBL_DIAG_DIAG);
            EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, PBL_DIAG_DIAG);
        } else if (topState == EntropicEnumeratorBlockEntity.PBLState.ADJ && bottomState == EntropicEnumeratorBlockEntity.PBLState.SOLVED) {
            if (colors[0][1][0].z == colors[1][1][0].z) {
                algorithms.add(U_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U_);
            } else if (colors[1][1][0].x == colors[1][1][1].x) {
                algorithms.add(U2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U2);
            } else if (colors[0][1][1].z == colors[1][1][1].z) {
                algorithms.add(U);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, U);
            }
            algorithms.add(PBL_ADJ_U);
            EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, PBL_ADJ_U);
        } else if (topState == EntropicEnumeratorBlockEntity.PBLState.SOLVED && bottomState == EntropicEnumeratorBlockEntity.PBLState.ADJ) {
            if (colors[0][0][0].z == colors[1][0][0].z) {
                algorithms.add(D);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D);
            } else if (colors[1][0][0].x == colors[1][0][1].x) {
                algorithms.add(D2);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D2);
            } else if (colors[0][0][1].z == colors[1][0][1].z) {
                algorithms.add(D_);
                EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, D_);
            }
            algorithms.add(PBL_ADJ_D);
            EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, PBL_ADJ_D);
        } else if (topState == EntropicEnumeratorBlockEntity.PBLState.DIAG && bottomState == EntropicEnumeratorBlockEntity.PBLState.SOLVED) {
            algorithms.add(PBL_DIAG_U);
            EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, PBL_DIAG_U);
        } else if (topState == EntropicEnumeratorBlockEntity.PBLState.SOLVED && bottomState == EntropicEnumeratorBlockEntity.PBLState.DIAG) {
            algorithms.add(PBL_DIAG_D);
            EntropicEnumeratorBlockEntity.Move.makeMoves(cubeClone, PBL_DIAG_D);
        }
        if (topState != EntropicEnumeratorBlockEntity.PBLState.SOLVED || bottomState != EntropicEnumeratorBlockEntity.PBLState.SOLVED) {
            updateColors(cubeClone, colors);
        }
        if (colors[0][0][0].z == colors[0][1][1].x) {
            algorithms.add(U);
        } else if (colors[0][0][0].z == colors[1][1][0].x) {
            algorithms.add(U_);
        } else if (colors[0][0][0].z == colors[1][1][1].z) {
            algorithms.add(U2);
        }
        int moveCount = 0;
        for (EntropicEnumeratorBlockEntity.Move[] algorithm : algorithms) {
            moveCount += algorithm.length;
        }
        this.moveQueue = new EntropicEnumeratorBlockEntity.Move[moveCount];
        int moveIndex = 0;
        for (int i = 0; i < algorithms.size(); i++) {
            EntropicEnumeratorBlockEntity.Move[] algorithm = (EntropicEnumeratorBlockEntity.Move[]) algorithms.get(i);
            for (int j = 0; j < algorithm.length; j++) {
                this.moveQueue[moveIndex] = algorithm[j];
                moveIndex++;
            }
        }
        this.moveQueue = optimizeAlgorithm(this.moveQueue);
        this.offsetQueue = new int[this.moveQueue.length];
        for (int i = 0; i < this.moveQueue.length; i++) {
            this.offsetQueue[i] = solvingMoveTime;
        }
        if (this.moveQueue.length > 0) {
            this.offsetQueue[0] = 0;
        }
        if (direct) {
            this.nextMoveTime = this.f_58857_.getGameTime() + (long) delay;
        } else {
            this.nextMoveTime = this.f_58857_.getGameTime() + (long) delay + (long) seededRand.nextInt(solvingMoveTime) - (long) (this.moveQueue.length * solvingMoveTime);
        }
        this.setChanged();
    }

    public void restartScramble(int offset) {
        if (this.moveQueue.length > 0) {
            for (int i = 0; i < this.moveQueue.length; i++) {
                this.moveQueue[i].makeMove(this.cube);
            }
        }
        this.moveQueue = new EntropicEnumeratorBlockEntity.Move[0];
        this.offsetQueue = new int[0];
        this.solving = false;
        this.moveOffset = ((int) (this.f_58857_.getGameTime() % (long) queueTime) + offset) % queueTime;
        this.setChanged();
    }

    public static EntropicEnumeratorBlockEntity.Move[] optimizeAlgorithm(EntropicEnumeratorBlockEntity.Move[] algorithm) {
        ArrayList<EntropicEnumeratorBlockEntity.Move> newAlgorithm = new ArrayList();
        for (int i = 0; i < algorithm.length; i++) {
            if (i == algorithm.length - 1) {
                newAlgorithm.add(algorithm[i]);
                break;
            }
            String current = algorithm[i].name();
            String next = algorithm[i + 1].name();
            if (current.charAt(0) == next.charAt(0)) {
                char currentMod = current.length() > 1 ? current.charAt(1) : 32;
                char nextMod = next.length() > 1 ? next.charAt(1) : 32;
                if ((currentMod != '_' || nextMod != '_') && (currentMod != ' ' || nextMod != ' ')) {
                    if ((currentMod != '2' || nextMod != '_') && (currentMod != '_' || nextMod != '2')) {
                        if (currentMod == '2' && nextMod == ' ' || currentMod == ' ' && nextMod == '2') {
                            newAlgorithm.add(EntropicEnumeratorBlockEntity.Move.valueOf(current.charAt(0) + "_"));
                        }
                    } else {
                        newAlgorithm.add(EntropicEnumeratorBlockEntity.Move.valueOf(current.charAt(0) + ""));
                    }
                } else {
                    newAlgorithm.add(EntropicEnumeratorBlockEntity.Move.valueOf(current.charAt(0) + "2"));
                }
                i++;
            } else {
                newAlgorithm.add(algorithm[i]);
            }
        }
        return newAlgorithm.size() < algorithm.length ? (EntropicEnumeratorBlockEntity.Move[]) newAlgorithm.toArray(new EntropicEnumeratorBlockEntity.Move[newAlgorithm.size()]) : algorithm;
    }

    public boolean isSolved() {
        EntropicEnumeratorBlockEntity.Cubie[][][] cubeClone = new EntropicEnumeratorBlockEntity.Cubie[2][2][2];
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    cubeClone[x][y][z] = new EntropicEnumeratorBlockEntity.Cubie(this.cube[x][y][z].basePosition, this.cube[x][y][z].baseColors, new Quaterniond(this.cube[x][y][z].rotation));
                }
            }
        }
        this.applyCurrentQueue(cubeClone);
        return isSolved(cubeClone);
    }

    public static boolean isSolved(EntropicEnumeratorBlockEntity.Cubie[][][] cube) {
        Vector3d[][][] colors = new Vector3d[2][2][2];
        updateColors(cube, colors);
        double downCol = colors[0][0][0].y;
        double upCol = 7.0 - downCol;
        double northCol = colors[0][0][0].z;
        double southCol = 7.0 - northCol;
        double westCol = colors[0][0][0].x;
        double eastCol = 7.0 - westCol;
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    if (y == 0) {
                        if (colors[x][y][z].y != downCol) {
                            return false;
                        }
                    } else if (colors[x][y][z].y != upCol) {
                        return false;
                    }
                    if (z == 0) {
                        if (colors[x][y][z].z != northCol) {
                            return false;
                        }
                    } else if (colors[x][y][z].z != southCol) {
                        return false;
                    }
                    if (x == 0) {
                        if (colors[x][y][z].x != westCol) {
                            return false;
                        }
                    } else if (colors[x][y][z].x != eastCol) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EntropicEnumeratorBlockEntity blockEntity) {
        if (!blockEntity.solving) {
            if (blockEntity.moveOffset == -1) {
                blockEntity.moveOffset = ((int) (level.getGameTime() % (long) queueTime) + 2) % queueTime;
                blockEntity.setChanged();
            }
            if (level.getGameTime() % (long) queueTime == (long) blockEntity.moveOffset) {
                EntropicEnumeratorBlockEntity.Move lastMove = null;
                if (blockEntity.moveQueue.length > 0) {
                    for (int i = 0; i < blockEntity.moveQueue.length; i++) {
                        blockEntity.moveQueue[i].makeMove(blockEntity.cube);
                    }
                    lastMove = blockEntity.moveQueue[blockEntity.moveQueue.length - 1];
                }
                blockEntity.nextMoveTime = level.getGameTime() + (long) (moveTime / 2);
                blockEntity.moveQueue = new EntropicEnumeratorBlockEntity.Move[queueSize];
                blockEntity.offsetQueue = new int[queueSize];
                for (int i = 0; i < queueSize; i++) {
                    EntropicEnumeratorBlockEntity.Move[] nextMoves = EntropicEnumeratorBlockEntity.Move.getNextMoves(lastMove);
                    blockEntity.moveQueue[i] = nextMoves[Misc.random.nextInt(nextMoves.length)];
                    blockEntity.offsetQueue[i] = moveTime;
                    lastMove = blockEntity.moveQueue[i];
                }
                blockEntity.offsetQueue[0] = 0;
                blockEntity.setChanged();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientTick(Level level, BlockPos pos, BlockState state, EntropicEnumeratorBlockEntity blockEntity) {
        if (level.getGameTime() >= blockEntity.nextMoveTime) {
            long turnTicks = level.getGameTime() - blockEntity.nextMoveTime;
            for (int i = 0; i < blockEntity.moveQueue.length; i++) {
                if (turnTicks == (long) blockEntity.offsetQueue[i]) {
                    level.playSound((Player) Minecraft.getInstance().player, pos, EmbersSounds.ENTROPIC_ENUMERATOR_TURN.get(), SoundSource.BLOCKS, blockEntity.solving ? 0.7F : 0.5F, blockEntity.solving ? 1.2F : 0.8F);
                    break;
                }
                if (turnTicks < (long) blockEntity.offsetQueue[i]) {
                    break;
                }
                turnTicks -= (long) blockEntity.offsetQueue[i];
            }
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return this.f_58859_ || !this.f_58857_.getBlockState(this.f_58858_).m_61138_(BlockStateProperties.FACING) && side != null || cap != EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY || side != null && side.getOpposite() != this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.FACING) ? super.getCapability(cap, side) : this.upgrade.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.upgrade.invalidate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }

    public static class Cubie {

        public static Vector3d zero = new Vector3d();

        public Vector3d basePosition;

        public Vector3d baseColors;

        public Quaterniond rotation;

        public Vector3d currentPosition = null;

        public Vector3d currentColors = null;

        public Cubie(Vector3d position, Vector3d colors, Quaterniond rotation) {
            this.basePosition = position;
            this.baseColors = colors;
            this.rotation = rotation;
        }

        public Cubie(Vector3d position, Vector3d colors) {
            this(position, colors, new Quaterniond());
        }

        public Cubie(Vector3d position, Quaterniond rotation) {
            this(position, zero, rotation);
        }

        public void setChanged() {
            this.currentPosition = null;
            this.currentColors = null;
        }

        public Vector3d getPos() {
            if (this.currentPosition != null) {
                return this.currentPosition;
            } else {
                this.currentPosition = this.rotation.transform(this.basePosition, new Vector3d()).round();
                return this.currentPosition;
            }
        }

        public Vector3d getColors() {
            if (this.currentColors != null) {
                return this.currentColors;
            } else {
                this.currentColors = this.rotation.transform(this.baseColors, new Vector3d()).absolute().round();
                return this.currentColors;
            }
        }
    }

    public static enum Move {

        U("U", new Vector3d(0.0, 1.0, 0.0), -0.5, 4, new Vector3i(-1, 1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1)),
        U_("U'", new Vector3d(0.0, 1.0, 0.0), 0.5, 4, new Vector3i(-1, 1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1)),
        U2("U2", new Vector3d(0.0, 1.0, 0.0), 1.0, 6, new Vector3i(-1, 1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1)),
        D("D", new Vector3d(0.0, 1.0, 0.0), 0.5, 4, new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, -1, 1), new Vector3i(-1, -1, 1)),
        D_("D'", new Vector3d(0.0, 1.0, 0.0), -0.5, 4, new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, -1, 1), new Vector3i(-1, -1, 1)),
        D2("D2", new Vector3d(0.0, 1.0, 0.0), 1.0, 6, new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, -1, 1), new Vector3i(-1, -1, 1)),
        L("L", new Vector3d(1.0, 0.0, 0.0), -0.5, 4, new Vector3i(1, -1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(1, -1, 1)),
        L_("L'", new Vector3d(1.0, 0.0, 0.0), 0.5, 4, new Vector3i(1, -1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(1, -1, 1)),
        L2("L2", new Vector3d(1.0, 0.0, 0.0), 1.0, 6, new Vector3i(1, -1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(1, -1, 1)),
        R("R", new Vector3d(1.0, 0.0, 0.0), 0.5, 4, new Vector3i(-1, -1, -1), new Vector3i(-1, 1, -1), new Vector3i(-1, 1, 1), new Vector3i(-1, -1, 1)),
        R_("R'", new Vector3d(1.0, 0.0, 0.0), -0.5, 4, new Vector3i(-1, -1, -1), new Vector3i(-1, 1, -1), new Vector3i(-1, 1, 1), new Vector3i(-1, -1, 1)),
        R2("R2", new Vector3d(1.0, 0.0, 0.0), 1.0, 6, new Vector3i(-1, -1, -1), new Vector3i(-1, 1, -1), new Vector3i(-1, 1, 1), new Vector3i(-1, -1, 1)),
        F("F", new Vector3d(0.0, 0.0, 1.0), 0.5, 4, new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, 1, -1), new Vector3i(-1, 1, -1)),
        F_("F'", new Vector3d(0.0, 0.0, 1.0), -0.5, 4, new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, 1, -1), new Vector3i(-1, 1, -1)),
        F2("F2", new Vector3d(0.0, 0.0, 1.0), 1.0, 6, new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, 1, -1), new Vector3i(-1, 1, -1)),
        B("B", new Vector3d(0.0, 0.0, 1.0), -0.5, 4, new Vector3i(-1, -1, 1), new Vector3i(1, -1, 1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1)),
        B_("B'", new Vector3d(0.0, 0.0, 1.0), 0.5, 4, new Vector3i(-1, -1, 1), new Vector3i(1, -1, 1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1)),
        B2("B2", new Vector3d(0.0, 0.0, 1.0), 1.0, 6, new Vector3i(-1, -1, 1), new Vector3i(1, -1, 1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1)),
        X("X", new Vector3d(1.0, 0.0, 0.0), 0.5, 4, new Vector3i(-1, 1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1), new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, -1, 1), new Vector3i(-1, -1, 1)),
        X_("X'", new Vector3d(1.0, 0.0, 0.0), -0.5, 4, new Vector3i(-1, 1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1), new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, -1, 1), new Vector3i(-1, -1, 1)),
        X2("X2", new Vector3d(1.0, 0.0, 0.0), 1.0, 6, new Vector3i(-1, 1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1), new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, -1, 1), new Vector3i(-1, -1, 1)),
        Y("Y", new Vector3d(0.0, 1.0, 0.0), 0.5, 4, new Vector3i(-1, 1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1), new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, -1, 1), new Vector3i(-1, -1, 1)),
        Y_("Y'", new Vector3d(0.0, 1.0, 0.0), -0.5, 4, new Vector3i(-1, 1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1), new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, -1, 1), new Vector3i(-1, -1, 1)),
        Y2("Y2", new Vector3d(0.0, 1.0, 0.0), 1.0, 6, new Vector3i(-1, 1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1), new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, -1, 1), new Vector3i(-1, -1, 1)),
        Z("Z", new Vector3d(0.0, 0.0, 1.0), 0.5, 4, new Vector3i(-1, 1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1), new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, -1, 1), new Vector3i(-1, -1, 1)),
        Z_("Z'", new Vector3d(0.0, 0.0, 1.0), -0.5, 4, new Vector3i(-1, 1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1), new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, -1, 1), new Vector3i(-1, -1, 1)),
        Z2("Z2", new Vector3d(0.0, 0.0, 1.0), 1.0, 6, new Vector3i(-1, 1, -1), new Vector3i(1, 1, -1), new Vector3i(1, 1, 1), new Vector3i(-1, 1, 1), new Vector3i(-1, -1, -1), new Vector3i(1, -1, -1), new Vector3i(1, -1, 1), new Vector3i(-1, -1, 1));

        public static final EntropicEnumeratorBlockEntity.Move[] axisMoves = new EntropicEnumeratorBlockEntity.Move[] { X, X_, Y, Y_, Z, Z_ };

        public static final EntropicEnumeratorBlockEntity.Move[] quarterMoves = new EntropicEnumeratorBlockEntity.Move[] { U, U_, D, D_, L, L_, R, R_, F, F_, B, B_ };

        public static final EntropicEnumeratorBlockEntity.Move[] movesX = new EntropicEnumeratorBlockEntity.Move[] { U, U_, D, D_, F, F_, B, B_ };

        public static final EntropicEnumeratorBlockEntity.Move[] movesY = new EntropicEnumeratorBlockEntity.Move[] { L, L_, R, R_, F, F_, B, B_ };

        public static final EntropicEnumeratorBlockEntity.Move[] movesZ = new EntropicEnumeratorBlockEntity.Move[] { U, U_, D, D_, L, L_, R, R_ };

        public static final EntropicEnumeratorBlockEntity.Move[] halfMoves = new EntropicEnumeratorBlockEntity.Move[] { U2, D2, L2, R2, F2, B2 };

        public static final EntropicEnumeratorBlockEntity.Move[] movesX2 = new EntropicEnumeratorBlockEntity.Move[] { U2, D2, F2, B2 };

        public static final EntropicEnumeratorBlockEntity.Move[] movesY2 = new EntropicEnumeratorBlockEntity.Move[] { L2, R2, F2, B2 };

        public static final EntropicEnumeratorBlockEntity.Move[] movesZ2 = new EntropicEnumeratorBlockEntity.Move[] { U2, D2, L2, R2 };

        public final String name;

        public final Vector3d axis;

        public final double angle;

        public final int length;

        public final Vector3i[] pieces;

        private Move(String name, Vector3d axis, double angle, int length, Vector3i... pieces) {
            this.name = name;
            this.axis = axis;
            this.angle = angle * Math.PI;
            this.length = length;
            this.pieces = pieces;
        }

        public static void makeMoves(EntropicEnumeratorBlockEntity.Cubie[][][] cubies, EntropicEnumeratorBlockEntity.Move[] moves) {
            for (EntropicEnumeratorBlockEntity.Move move : moves) {
                move.makeMove(cubies);
            }
        }

        public void makeMove(EntropicEnumeratorBlockEntity.Cubie[][][] cubies) {
            ArrayList<EntropicEnumeratorBlockEntity.Cubie> affectedCubies = new ArrayList();
            for (Vector3i pos : this.pieces) {
                for (int x = 0; x < 2; x++) {
                    for (int y = 0; y < 2; y++) {
                        for (int z = 0; z < 2; z++) {
                            Vector3d position = cubies[x][y][z].getPos();
                            if ((int) position.x == pos.x && (int) position.y == pos.y && (int) position.z == pos.z) {
                                affectedCubies.add(cubies[x][y][z]);
                            }
                        }
                    }
                }
            }
            for (EntropicEnumeratorBlockEntity.Cubie cubie : affectedCubies) {
                cubie.setChanged();
                cubie.rotation.rotateAxis(this.angle, cubie.rotation.transformInverse(this.axis, new Vector3d()));
            }
        }

        public Quaternionf makePartialMove(Quaternionf cubie, Vector3d position, float moveAmount) {
            for (Vector3i pos : this.pieces) {
                if ((int) position.x == pos.x && (int) position.y == pos.y && (int) position.z == pos.z) {
                    Vector3d newAxis = cubie.transformInverse(this.axis, new Vector3d());
                    cubie.rotateAxis((float) this.angle * moveAmount, (float) newAxis.x(), (float) newAxis.y(), (float) newAxis.z());
                }
            }
            return cubie;
        }

        public EntropicEnumeratorBlockEntity.Move getOpposite() {
            switch(this) {
                case U:
                    return U_;
                case U_:
                    return U;
                case D:
                    return D_;
                case D_:
                    return D;
                case L:
                    return L_;
                case L_:
                    return L;
                case R:
                    return R_;
                case R_:
                    return R;
                case F:
                    return F_;
                case F_:
                    return F;
                case B:
                    return B_;
                case B_:
                    return B;
                default:
                    return U;
            }
        }

        public static EntropicEnumeratorBlockEntity.Move[] getNextMoves(EntropicEnumeratorBlockEntity.Move move) {
            if (move == null) {
                return quarterMoves;
            } else {
                switch(move) {
                    case U:
                    case U_:
                    case D:
                    case D_:
                        return movesY;
                    case L:
                    case L_:
                    case R:
                    case R_:
                        return movesX;
                    case F:
                    case F_:
                    case B:
                    case B_:
                        return movesZ;
                    case L2:
                    case R2:
                        return movesX2;
                    case U2:
                    case D2:
                        return movesY2;
                    case F2:
                    case B2:
                        return movesZ2;
                    default:
                        return quarterMoves;
                }
            }
        }

        public static EntropicEnumeratorBlockEntity.Move[] getMoves(String algorithm) {
            String[] names = algorithm.split(" ");
            EntropicEnumeratorBlockEntity.Move[] moves = new EntropicEnumeratorBlockEntity.Move[names.length];
            for (int i = 0; i < names.length; i++) {
                moves[i] = valueOf(names[i]);
            }
            return moves;
        }
    }

    public static enum PBLState {

        ADJ, DIAG, SOLVED
    }
}