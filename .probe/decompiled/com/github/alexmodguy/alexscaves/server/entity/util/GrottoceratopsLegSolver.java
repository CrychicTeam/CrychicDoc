package com.github.alexmodguy.alexscaves.server.entity.util;

import com.github.alexthe666.citadel.animation.LegSolver;

public class GrottoceratopsLegSolver extends LegSolver {

    public final LegSolver.Leg backLeft = this.legs[0];

    public final LegSolver.Leg backRight = this.legs[1];

    public final LegSolver.Leg frontLeft = this.legs[2];

    public final LegSolver.Leg frontRight = this.legs[3];

    public GrottoceratopsLegSolver() {
        this(0.0F, 0.65F, 0.5F, 0.5F, 1.0F);
    }

    public GrottoceratopsLegSolver(float forwardCenter, float forward, float sideBack, float sideFront, float range) {
        super(new LegSolver.Leg(forwardCenter - forward, sideBack, range, false), new LegSolver.Leg(forwardCenter - forward, -sideBack, range, false), new LegSolver.Leg(forwardCenter + forward, sideFront, range, true), new LegSolver.Leg(forwardCenter + forward, -sideFront, range, true));
    }

    protected float getFallSpeed() {
        return 0.1F;
    }

    protected float getRiseSpeed() {
        return 0.1F;
    }
}