package com.mna.capabilities.particles;

import java.util.concurrent.Callable;

public class ParticleAuraFactory implements Callable<ParticleAura> {

    public ParticleAura call() throws Exception {
        return new ParticleAura();
    }
}