package com.mna.capabilities.entity;

import java.util.concurrent.Callable;

public class MAPFXFactory implements Callable<MAPFX> {

    public MAPFX call() throws Exception {
        return new MAPFX();
    }
}