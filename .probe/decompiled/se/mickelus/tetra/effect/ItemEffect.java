package se.mickelus.tetra.effect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ItemEffect {

    public static final String hauntedKey = "destabilized/haunted";

    private static final Map<String, ItemEffect> effectMap = new ConcurrentHashMap();

    public static final ItemEffect workable = get("workable");

    public static final ItemEffect unstable = get("unstable");

    public static final ItemEffect howling = get("howling");

    public static final ItemEffect bleeding = get("bleeding");

    public static final ItemEffect backstab = get("backstab");

    public static final ItemEffect armorPenetration = get("armorPenetration");

    public static final ItemEffect crushing = get("crushing");

    public static final ItemEffect skewering = get("skewering");

    public static final ItemEffect sweeping = get("sweeping");

    public static final ItemEffect truesweep = get("truesweep");

    public static final ItemEffect strikingAxe = get("strikingAxe");

    public static final ItemEffect strikingPickaxe = get("strikingPickaxe");

    public static final ItemEffect strikingCut = get("strikingCut");

    public static final ItemEffect strikingShovel = get("strikingShovel");

    public static final ItemEffect strikingHoe = get("strikingHoe");

    public static final ItemEffect sweepingStrike = get("sweepingStrike");

    public static final ItemEffect sweepingFocus = get("sweepingFocus");

    public static final ItemEffect planarSweep = get("planarSweep");

    public static final ItemEffect extraction = get("extraction");

    public static final ItemEffect unboundExtraction = get("unboundExtraction");

    public static final ItemEffect extractionMedialLimit = get("extractionMedialLimit");

    public static final ItemEffect extractionLateralLimit = get("extractionLateralLimit");

    public static final ItemEffect extractionAxialLimit = get("extractionAxialLimit");

    public static final ItemEffect extractionAxialAmplify = get("extractionAxialAmplify");

    public static final ItemEffect extractionPlanarAmplify = get("extractionPlanarAmplify");

    public static final ItemEffect unbreaking = get("unbreaking");

    public static final ItemEffect blocking = get("blocking");

    public static final ItemEffect blockingReflect = get("blockingReflect");

    public static final ItemEffect bashing = get("bashing");

    public static final ItemEffect ricochet = get("ricochet");

    public static final ItemEffect piercing = get("piercing");

    public static final ItemEffect piercingHarvest = get("piercingHarvest");

    public static final ItemEffect jab = get("jab");

    public static final ItemEffect counterweight = get("counterweight");

    public static final ItemEffect quickStrike = get("quickStrike");

    public static final ItemEffect softStrike = get("softStrike");

    public static final ItemEffect severing = get("severing");

    public static final ItemEffect stun = get("stun");

    public static final ItemEffect abilityDefensive = get("abilityDefensive");

    public static final ItemEffect abilityOvercharge = get("abilityOvercharge");

    public static final ItemEffect abilityMomentum = get("abilityMomentum");

    public static final ItemEffect abilityCombo = get("abilityCombo");

    public static final ItemEffect abilityRevenge = get("abilityRevenge");

    public static final ItemEffect abilityOverextend = get("abilityOverextend");

    public static final ItemEffect abilityExhilaration = get("abilityExhilaration");

    public static final ItemEffect abilitySpeed = get("abilitySpeed");

    public static final ItemEffect abilityEcho = get("abilityEcho");

    public static final ItemEffect execute = get("execute");

    public static final ItemEffect lunge = get("lunge");

    public static final ItemEffect slam = get("slam");

    public static final ItemEffect puncture = get("puncture");

    public static final ItemEffect pry = get("pry");

    public static final ItemEffect overpower = get("overpower");

    public static final ItemEffect reap = get("reap");

    public static final ItemEffect denailing = get("denailing");

    public static final ItemEffect fierySelf = get("fierySelf");

    public static final ItemEffect enderReverb = get("enderReverb");

    public static final ItemEffect sculkTaint = get("sculkTaint");

    public static final ItemEffect haunted = get("haunted");

    public static final ItemEffect stabilizing = get("stabilizing");

    public static final ItemEffect criticalStrike = get("criticalStrike");

    public static final ItemEffect intuit = get("intuit");

    public static final ItemEffect earthbind = get("earthbind");

    public static final ItemEffect reaching = get("reaching");

    public static final ItemEffect janking = get("janking");

    public static final ItemEffect throwable = get("throwable");

    public static final ItemEffect shieldbreaker = get("shieldbreaker");

    public static final ItemEffect booster = get("booster");

    public static final ItemEffect quickSlot = get("quickSlot");

    public static final ItemEffect storageSlot = get("storageSlot");

    public static final ItemEffect potionSlot = get("potionSlot");

    public static final ItemEffect quiverSlot = get("quiverSlot");

    public static final ItemEffect quickAccess = get("quickAccess");

    public static final ItemEffect cellSocket = get("cellSocket");

    public static final ItemEffect suspendSelf = get("suspendSelf");

    public static final ItemEffect releaseLatch = get("releaseLatch");

    public static final ItemEffect flow = get("flow");

    public static final ItemEffect overbowed = get("overbowed");

    public static final ItemEffect multishot = get("multishot");

    public static final ItemEffect ammoCapacity = get("ammoCapacity");

    public static final ItemEffect zoom = get("zoom");

    public static final ItemEffect spread = get("spread");

    public static final ItemEffect focus = get("focus");

    public static final ItemEffect focusEcho = get("focusEcho");

    public static final ItemEffect velocity = get("velocity");

    public static final ItemEffect suspend = get("suspend");

    public static final ItemEffect rangeCritical = get("rangeCritical");

    public static final ItemEffect sweeperRange = get("sweeperRange");

    public static final ItemEffect sweeperHorizontalSpread = get("sweeperHorizontalSpread");

    public static final ItemEffect sweeperVerticalSpread = get("sweeperVerticalSpread");

    public static final ItemEffect percussionScanner = get("percussionScanner");

    private final String key;

    private ItemEffect(String key) {
        this.key = key;
    }

    public static ItemEffect get(String key) {
        return (ItemEffect) effectMap.computeIfAbsent(key, k -> new ItemEffect(key));
    }

    public String getKey() {
        return this.key;
    }
}