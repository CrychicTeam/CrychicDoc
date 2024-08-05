package vazkii.patchouli.common.multiblock;

import com.google.gson.annotations.SerializedName;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import vazkii.patchouli.api.IStateMatcher;

public class SerializedMultiblock {

    @SerializedName("pattern")
    private String[][] densePattern = null;

    @SerializedName("sparse_pattern")
    private Map<String, List<List<Integer>>> sparsePattern = null;

    private Map<String, String> mapping = new HashMap();

    private boolean symmetrical = false;

    private int[] offset = new int[] { 0, 0, 0 };

    private static char assertValidMappingKey(String s) {
        if (s.length() != 1) {
            throw new IllegalArgumentException(s + " is an invalid mapping key, every mapping key must be 1 character long");
        } else {
            return s.charAt(0);
        }
    }

    private static Map<Character, IStateMatcher> deserializeMapping(Map<String, String> mapping) {
        Map<Character, IStateMatcher> ret = new HashMap(mapping.size());
        for (Entry<String, String> e : mapping.entrySet()) {
            char key = assertValidMappingKey((String) e.getKey());
            String value = (String) e.getValue();
            IStateMatcher matcher;
            try {
                matcher = StringStateMatcher.fromString(value);
            } catch (CommandSyntaxException var8) {
                throw new IllegalArgumentException("Failure parsing state matcher", var8);
            }
            ret.put(key, matcher);
        }
        if (!ret.containsKey('_')) {
            ret.put('_', StateMatcher.ANY);
        }
        if (!ret.containsKey(' ')) {
            ret.put(' ', StateMatcher.AIR);
        }
        if (!ret.containsKey('0')) {
            ret.put('0', StateMatcher.AIR);
        }
        return ret;
    }

    private SparseMultiblock deserializeSparse() {
        Map<Character, IStateMatcher> matchers = deserializeMapping(this.mapping);
        Map<BlockPos, IStateMatcher> data = new HashMap();
        for (Entry<String, List<List<Integer>>> e : this.sparsePattern.entrySet()) {
            char key = assertValidMappingKey((String) e.getKey());
            this.assertMappingContains(key);
            for (List<Integer> position : (List) e.getValue()) {
                if (position.size() != 3) {
                    throw new IllegalArgumentException("Position has more than three coordinates: " + position);
                }
                BlockPos pos = new BlockPos((Integer) position.get(0), (Integer) position.get(1), (Integer) position.get(2));
                data.put(pos, (IStateMatcher) matchers.get(key));
            }
        }
        return new SparseMultiblock(data);
    }

    private void assertMappingContains(char c) {
        if (c != '0' && c != '_' && c != ' ' && !this.mapping.containsKey(String.valueOf(c))) {
            throw new IllegalArgumentException("Character " + c + " in multiblock isn't mapped to a block");
        }
    }

    public DenseMultiblock deserializeDense() {
        for (String[] line : this.densePattern) {
            for (String s : line) {
                for (char c : s.toCharArray()) {
                    this.assertMappingContains(c);
                }
            }
        }
        return new DenseMultiblock(this.densePattern, deserializeMapping(this.mapping));
    }

    public AbstractMultiblock toMultiblock() {
        if (this.densePattern != null == (this.sparsePattern != null)) {
            throw new IllegalArgumentException("One and only one of pattern and sparse_pattern should be specified");
        } else {
            AbstractMultiblock mb;
            if (this.densePattern != null) {
                mb = this.deserializeDense();
            } else {
                mb = this.deserializeSparse();
            }
            mb.setSymmetrical(this.symmetrical);
            mb.offset(this.offset[0], this.offset[1], this.offset[2]);
            return mb;
        }
    }
}