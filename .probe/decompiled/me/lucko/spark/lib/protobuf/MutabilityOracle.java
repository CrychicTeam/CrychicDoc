package me.lucko.spark.lib.protobuf;

interface MutabilityOracle {

    MutabilityOracle IMMUTABLE = new MutabilityOracle() {

        @Override
        public void ensureMutable() {
            throw new UnsupportedOperationException();
        }
    };

    void ensureMutable();
}