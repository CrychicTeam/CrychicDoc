package me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.emitter;

import java.io.IOException;

interface EmitterState {

    void expect() throws IOException;
}