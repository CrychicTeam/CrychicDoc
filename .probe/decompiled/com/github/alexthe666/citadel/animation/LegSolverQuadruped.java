package com.github.alexthe666.citadel.animation;

public final class LegSolverQuadruped extends LegSolver {

    public final LegSolver.Leg backLeft = this.legs[0];

    public final LegSolver.Leg backRight = this.legs[1];

    public final LegSolver.Leg frontLeft = this.legs[2];

    public final LegSolver.Leg frontRight = this.legs[3];

    public LegSolverQuadruped(float forward, float side) {
        this(0.0F, forward, side, side, 1.0F);
    }

    public LegSolverQuadruped(float forwardCenter, float forward, float sideBack, float sideFront, float range) {
        super(new LegSolver.Leg(forwardCenter - forward, sideBack, range, false), new LegSolver.Leg(forwardCenter - forward, -sideBack, range, false), new LegSolver.Leg(forwardCenter + forward, sideFront, range, true), new LegSolver.Leg(forwardCenter + forward, -sideFront, range, true));
    }
}