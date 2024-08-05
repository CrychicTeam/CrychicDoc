package org.embeddedt.embeddium.taint.incompats;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

public interface ModDeclaration {

    boolean matches();

    public static class And implements ModDeclaration {

        private final ModDeclaration left;

        private final ModDeclaration right;

        public And(ModDeclaration left, ModDeclaration right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean matches() {
            return this.left.matches() && this.right.matches();
        }

        public String toString() {
            return this.left.toString() + " and " + this.right.toString();
        }
    }

    public static class Or implements ModDeclaration {

        private final ModDeclaration left;

        private final ModDeclaration right;

        public Or(ModDeclaration left, ModDeclaration right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean matches() {
            return this.left.matches() || this.right.matches();
        }

        public String toString() {
            return this.left.toString() + " or " + this.right.toString();
        }
    }

    public static class Single implements ModDeclaration {

        private final String modId;

        private final String friendlyName;

        private final VersionRange versionRange;

        public Single(String modId, String friendlyName) {
            this(modId, friendlyName, null);
        }

        public Single(String modId, String friendlyName, @Nullable String versionRange) {
            this.modId = modId;
            this.friendlyName = friendlyName;
            try {
                this.versionRange = versionRange == null ? null : VersionRange.createFromVersionSpec(versionRange);
            } catch (InvalidVersionSpecificationException var5) {
                throw new IllegalArgumentException(var5);
            }
        }

        @Override
        public boolean matches() {
            Optional<? extends ModContainer> modContainerOpt = ModList.get().getModContainerById(this.modId);
            return !modContainerOpt.isPresent() ? false : this.versionRange == null || this.versionRange.containsVersion(((ModContainer) modContainerOpt.get()).getModInfo().getVersion());
        }

        public String toString() {
            return this.versionRange == null ? this.friendlyName : this.friendlyName + "(" + this.versionRange + ")";
        }
    }
}