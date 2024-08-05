package vazkii.patchouli.common.multiblock;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.TriPredicate;
import vazkii.patchouli.common.util.RotationUtil;

public class DenseMultiblock extends AbstractMultiblock {

    private final String[][] pattern;

    private IStateMatcher[][][] stateTargets;

    private final Vec3i size;

    public DenseMultiblock(String[][] pattern, Map<Character, IStateMatcher> targets) {
        this.pattern = pattern;
        this.size = this.build(targets, getPatternDimensions(pattern));
    }

    public DenseMultiblock(String[][] pattern, Object... targets) {
        this.pattern = pattern;
        this.size = this.build(targetsToMatchers(targets), getPatternDimensions(pattern));
    }

    @Override
    public Pair<BlockPos, Collection<IMultiblock.SimulateResult>> simulate(Level world, BlockPos anchor, Rotation rotation, boolean forView) {
        BlockPos disp = forView ? new BlockPos(-this.viewOffX, -this.viewOffY + 1, -this.viewOffZ).rotate(rotation) : new BlockPos(-this.offX, -this.offY, -this.offZ).rotate(rotation);
        BlockPos origin = anchor.offset(disp);
        List<IMultiblock.SimulateResult> ret = new ArrayList();
        for (int x = 0; x < this.size.getX(); x++) {
            for (int y = 0; y < this.size.getY(); y++) {
                for (int z = 0; z < this.size.getZ(); z++) {
                    BlockPos currDisp = new BlockPos(x, y, z).rotate(rotation);
                    BlockPos actionPos = origin.offset(currDisp);
                    char currC = this.pattern[y][x].charAt(z);
                    ret.add(new SimulateResultImpl(actionPos, this.stateTargets[x][y][z], currC));
                }
            }
        }
        return Pair.of(origin, ret);
    }

    @Override
    public boolean test(Level world, BlockPos start, int x, int y, int z, Rotation rotation) {
        this.setWorld(world);
        if (x >= 0 && y >= 0 && z >= 0 && x < this.size.getX() && y < this.size.getY() && z < this.size.getZ()) {
            BlockPos checkPos = start.offset(new BlockPos(x, y, z).rotate(RotationUtil.fixHorizontal(rotation)));
            TriPredicate<BlockGetter, BlockPos, BlockState> pred = this.stateTargets[x][y][z].getStatePredicate();
            BlockState state = world.getBlockState(checkPos).m_60717_(rotation);
            return pred.test(world, checkPos, state);
        } else {
            return false;
        }
    }

    private static Map<Character, IStateMatcher> targetsToMatchers(Object... targets) {
        if (targets.length % 2 == 1) {
            throw new IllegalArgumentException("Illegal argument length for targets array " + targets.length);
        } else {
            Map<Character, IStateMatcher> stateMap = new HashMap();
            for (int i = 0; i < targets.length / 2; i++) {
                char c = (Character) targets[i * 2];
                Object o = targets[i * 2 + 1];
                if (o instanceof Block) {
                    state = StateMatcher.fromBlockLoose((Block) o);
                } else if (o instanceof BlockState) {
                    state = StateMatcher.fromState((BlockState) o);
                } else if (o instanceof String) {
                    try {
                        state = StringStateMatcher.fromString((String) o);
                    } catch (CommandSyntaxException var7) {
                        throw new RuntimeException(var7);
                    }
                } else if (!(o instanceof IStateMatcher state)) {
                    throw new IllegalArgumentException("Invalid target " + o);
                }
                stateMap.put(c, state);
            }
            if (!stateMap.containsKey('_')) {
                stateMap.put('_', StateMatcher.ANY);
            }
            if (!stateMap.containsKey(' ')) {
                stateMap.put(' ', StateMatcher.AIR);
            }
            if (!stateMap.containsKey('0')) {
                stateMap.put('0', StateMatcher.AIR);
            }
            return stateMap;
        }
    }

    private Vec3i build(Map<Character, IStateMatcher> stateMap, Vec3i dimensions) {
        boolean foundCenter = false;
        this.stateTargets = new IStateMatcher[dimensions.getX()][dimensions.getY()][dimensions.getZ()];
        for (int y = 0; y < dimensions.getY(); y++) {
            for (int x = 0; x < dimensions.getX(); x++) {
                for (int z = 0; z < dimensions.getZ(); z++) {
                    char c = this.pattern[y][x].charAt(z);
                    if (!stateMap.containsKey(c)) {
                        throw new IllegalArgumentException("Character " + c + " isn't mapped");
                    }
                    IStateMatcher matcher = (IStateMatcher) stateMap.get(c);
                    if (c == '0') {
                        if (foundCenter) {
                            throw new IllegalArgumentException("A structure can't have two centers");
                        }
                        foundCenter = true;
                        this.offX = x;
                        this.offY = dimensions.getY() - y - 1;
                        this.offZ = z;
                        this.setViewOffset();
                    }
                    this.stateTargets[x][dimensions.getY() - y - 1][z] = matcher;
                }
            }
        }
        if (!foundCenter) {
            throw new IllegalArgumentException("A structure can't have no center");
        } else {
            return dimensions;
        }
    }

    private static Vec3i getPatternDimensions(String[][] pattern) {
        int expectedLenX = -1;
        int expectedLenZ = -1;
        for (String[] arr : pattern) {
            if (expectedLenX == -1) {
                expectedLenX = arr.length;
            }
            if (arr.length != expectedLenX) {
                throw new IllegalArgumentException("Inconsistent array length. Expected" + expectedLenX + ", got " + arr.length);
            }
            for (String s : arr) {
                if (expectedLenZ == -1) {
                    expectedLenZ = s.length();
                }
                if (s.length() != expectedLenZ) {
                    throw new IllegalArgumentException("Inconsistent array length. Expected" + expectedLenX + ", got " + arr.length);
                }
            }
        }
        return new Vec3i(expectedLenX, pattern.length, expectedLenZ);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        int x = pos.m_123341_();
        int y = pos.m_123342_();
        int z = pos.m_123343_();
        if (x >= 0 && y >= 0 && z >= 0 && x < this.size.getX() && y < this.size.getY() && z < this.size.getZ()) {
            long ticks = this.world != null ? this.world.getGameTime() : 0L;
            return this.stateTargets[x][y][z].getDisplayedState(ticks);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public Vec3i getSize() {
        return this.size;
    }

    @Override
    public int getHeight() {
        return 255;
    }

    @Override
    public int getMinBuildHeight() {
        return 0;
    }
}