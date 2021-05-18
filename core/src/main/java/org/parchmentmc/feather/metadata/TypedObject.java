package org.parchmentmc.feather.metadata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.parchmentmc.feather.named.Named;

/**
 * Represents an object with a descriptor and signature.
 * Most notably are fields and methods.
 */
public interface TypedObject {

    /**
     * The JVM Descriptor of the object.
     *
     * @return The JVM Bytecode descriptor holder of the object.
     */
    @NonNull
    Named getDescriptor();

    /**
     * The ASM Signature of the object.
     * If the signature extraction fails then no naming scheme will reside in the named object.
     *
     * @return The signature holder of the object.
     */
    @NonNull
    Named getSignature();
}
