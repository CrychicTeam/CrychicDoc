package yesman.epicfight.gameasset;

import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;
import yesman.epicfight.api.collider.OBBCollider;

public class ColliderPreset {

    public static final Collider DAGGER = new MultiOBBCollider(3, 0.4, 0.4, 0.6, 0.0, 0.0, -0.1);

    public static final Collider DUAL_DAGGER_DASH = new OBBCollider(0.8, 0.5, 1.0, 0.0, 1.0, -0.6);

    public static final Collider BIPED_BODY_COLLIDER = new MultiOBBCollider(new OBBCollider(0.8, 0.5, 1.0, 0.0, 1.0, -0.6), new OBBCollider(0.8, 0.5, 1.0, 0.0, 1.0, -0.6));

    public static final Collider DRAGON_BODY = new OBBCollider(2.0, 1.5, 4.0, 0.0, 1.5, -0.5);

    public static final Collider DRAGON_LEG = new MultiOBBCollider(3, 0.8, 1.6, 0.8, 0.0, -0.6, 0.7);

    public static final Collider DUAL_SWORD = new OBBCollider(0.8, 0.5, 1.0, 0.0, 0.5, -1.0);

    public static final Collider DUAL_SWORD_DASH = new OBBCollider(0.8, 0.5, 1.0, 0.0, 1.0, -1.0);

    public static final Collider BATTOJUTSU = new OBBCollider(2.5, 0.25, 1.5, 0.0, 1.0, -1.0);

    public static final Collider BATTOJUTSU_DASH = new MultiOBBCollider(new OBBCollider(0.7, 0.7, 1.0, 0.0, 1.0, -1.0), new OBBCollider(0.7, 0.7, 1.0, 0.0, 1.0, -1.0), new OBBCollider(0.7, 0.7, 1.0, 0.0, 1.0, -1.0), new OBBCollider(0.7, 0.7, 1.0, 0.0, 1.0, -1.0), new OBBCollider(1.5, 0.7, 1.0, 0.0, 1.0, -1.0));

    public static final Collider FIST = new MultiOBBCollider(3, 0.4, 0.4, 0.4, 0.0, 0.0, 0.0);

    public static final Collider GREATSWORD = new MultiOBBCollider(3, 0.5, 0.8, 1.0, 0.0, 0.0, -1.0);

    public static final Collider HEAD = new OBBCollider(0.4, 0.4, 0.4, 0.0, 0.0, -0.3);

    public static final Collider HEADBUTT_RAVAGER = new OBBCollider(0.8, 0.8, 0.8, 0.0, 0.0, -0.3);

    public static final Collider UCHIGATANA = new MultiOBBCollider(5, 0.4, 0.4, 0.7, 0.0, 0.0, -0.7);

    public static final Collider TACHI = new MultiOBBCollider(3, 0.4, 0.4, 0.95, 0.0, 0.0, -0.95);

    public static final Collider SWORD = new MultiOBBCollider(3, 0.4, 0.4, 0.7, 0.0, 0.0, -0.35);

    public static final Collider LONGSWORD = new MultiOBBCollider(3, 0.4, 0.4, 0.8, 0.0, 0.0, -0.75);

    public static final Collider SPEAR = new MultiOBBCollider(3, 0.6, 0.6, 1.0, 0.0, 0.0, -1.0);

    public static final Collider SPIDER = new OBBCollider(0.8, 0.8, 0.8, 0.0, 0.0, -0.4);

    public static final Collider TOOLS = new MultiOBBCollider(3, 0.4, 0.4, 0.55, 0.0, 0.0, -0.25);

    public static final Collider ENDERMAN_LIMB = new OBBCollider(0.4, 0.8, 0.4, 0.0, 0.0, 0.0);

    public static final Collider GOLEM_SMASHDOWN = new MultiOBBCollider(3, 0.75, 0.5, 0.5, 0.6, 0.5, 0.0);

    public static final Collider GOLEM_SWING_ARM = new MultiOBBCollider(2, 0.6, 0.9, 0.6, 0.0, 0.0, 0.0);

    public static final Collider FIST_FIXED = new OBBCollider(0.4, 0.4, 0.5, 0.0, 1.0, -0.85);

    public static final Collider DUAL_SWORD_AIR_SLASH = new OBBCollider(0.8, 0.4, 1.0, 0.0, 0.5, -0.5);

    public static final Collider DUAL_DAGGER_AIR_SLASH = new OBBCollider(0.8, 0.4, 0.75, 0.0, 0.5, -0.5);

    public static final Collider WITHER_CHARGE = new MultiOBBCollider(5, 0.7, 0.9, 0.7, 0.0, 1.0, -0.35);

    public static final Collider VEX_CHARGE = new MultiOBBCollider(3, 0.4, 0.4, 0.95, 0.0, 1.0, -0.85);

    public static void update() {
    }
}