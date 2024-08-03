package icyllis.modernui.util;

import javax.annotation.Nonnull;

public final class StateSet {

    private static final int[][] VIEW_STATE_SETS = new int[1 << (StateSet.VIEW_STATE_IDS.length >> 1)][];

    public static final int[] WILD_CARD = new int[0];

    public static final int VIEW_STATE_WINDOW_FOCUSED = 1;

    public static final int VIEW_STATE_SELECTED = 2;

    public static final int VIEW_STATE_FOCUSED = 4;

    public static final int VIEW_STATE_ENABLED = 8;

    public static final int VIEW_STATE_PRESSED = 16;

    public static final int VIEW_STATE_ACTIVATED = 32;

    public static final int VIEW_STATE_HOVERED = 64;

    public static final int VIEW_STATE_DRAG_CAN_ACCEPT = 128;

    public static final int VIEW_STATE_DRAG_HOVERED = 256;

    private static final int[] VIEW_STATE_IDS = new int[] { 16842909, 1, 16842913, 2, 16844130, 4, 16842910, 8, 16842919, 16, 16843518, 32, 16843623, 64, 16843624, 128, 16843625, 256 };

    private StateSet() {
    }

    public static int[] get(int mask) {
        return VIEW_STATE_SETS[mask];
    }

    public static boolean isWildCard(@Nonnull int[] state) {
        return state.length == 0 || state[0] == 0;
    }

    public static boolean stateSetMatches(@Nonnull int[] stateSpec, @Nonnull int[] stateSet) {
        label44: for (int stateSpecState : stateSpec) {
            if (stateSpecState == 0) {
                return true;
            }
            boolean mustMatch;
            if (stateSpecState > 0) {
                mustMatch = true;
            } else {
                mustMatch = false;
                stateSpecState = -stateSpecState;
            }
            for (int state : stateSet) {
                if (state == 0) {
                    if (mustMatch) {
                        return false;
                    }
                    continue label44;
                }
                if (state == stateSpecState) {
                    if (!mustMatch) {
                        return false;
                    }
                    continue label44;
                }
            }
            if (mustMatch) {
                return false;
            }
        }
        return true;
    }

    public static boolean stateSetMatches(@Nonnull int[] stateSpec, int state) {
        for (int stateSpecState : stateSpec) {
            if (stateSpecState == 0) {
                return true;
            }
            if (stateSpecState > 0) {
                if (state != stateSpecState) {
                    return false;
                }
            } else if (state == -stateSpecState) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsAttribute(@Nonnull int[][] stateSpecs, int attr) {
        for (int[] spec : stateSpecs) {
            for (int specAttr : spec) {
                if (specAttr == attr || -specAttr == attr) {
                    return true;
                }
            }
        }
        return false;
    }

    @Nonnull
    public static int[] trimStateSet(@Nonnull int[] states, int newSize) {
        if (states.length == newSize) {
            return states;
        } else {
            int[] trimmedStates = new int[newSize];
            System.arraycopy(states, 0, trimmedStates, 0, newSize);
            return trimmedStates;
        }
    }

    static {
        VIEW_STATE_SETS[0] = WILD_CARD;
        for (int i = 1; i < VIEW_STATE_SETS.length; i++) {
            int numBits = Integer.bitCount(i);
            int[] set = new int[numBits];
            int pos = 0;
            for (int j = 0; j < VIEW_STATE_IDS.length; j += 2) {
                if ((i & VIEW_STATE_IDS[j + 1]) != 0) {
                    set[pos++] = VIEW_STATE_IDS[j];
                }
            }
            VIEW_STATE_SETS[i] = set;
        }
    }
}