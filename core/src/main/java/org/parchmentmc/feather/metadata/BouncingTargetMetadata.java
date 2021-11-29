package org.parchmentmc.feather.metadata;

import org.parchmentmc.feather.util.HasImmutable;

import java.util.Optional;

/**
 * The metadata used to indicate the methods bounding relationship.
 */
public interface BouncingTargetMetadata extends HasImmutable<BouncingTargetMetadata>
{
    /**
     * The method that this bouncer bounces to.
     *
     * @return The bouncing target method.
     */
    Optional<Reference> getTarget();

    /**
     * The method that this bouncer bounces from or is owned by.
     *
     * @return The bouncing owning method.
     */
    Optional<Reference> getOwner();
}
