package org.embeddedt.modernfix.dfu;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LazyDataFixer implements DataFixer {

    private static final Logger LOGGER = LogManager.getLogger("ModernFix");

    private DataFixer backingDataFixer;

    private final Supplier<DataFixer> dfuSupplier;

    public LazyDataFixer(Supplier<DataFixer> dfuSupplier) {
        LOGGER.info("Bypassed Mojang DFU");
        this.backingDataFixer = null;
        this.dfuSupplier = dfuSupplier;
    }

    private DataFixer getDataFixer() {
        synchronized (this) {
            if (this.backingDataFixer == null) {
                LOGGER.info("Instantiating Mojang DFU");
                DFUBlaster.blastMaps();
                this.backingDataFixer = (DataFixer) this.dfuSupplier.get();
            }
        }
        return this.backingDataFixer;
    }

    public <T> Dynamic<T> update(TypeReference type, Dynamic<T> input, int version, int newVersion) {
        return version >= newVersion ? input : this.getDataFixer().update(type, input, version, newVersion);
    }

    public Schema getSchema(int key) {
        return this.getDataFixer().getSchema(key);
    }
}