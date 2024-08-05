package ca.fxco.memoryleakfix.mixinextras.service;

class Versioned<T> {

    final int version;

    final T value;

    Versioned(int version, T value) {
        this.version = version;
        this.value = value;
    }
}