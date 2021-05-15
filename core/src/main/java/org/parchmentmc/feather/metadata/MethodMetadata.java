package org.parchmentmc.feather.metadata;

import java.util.List;

/**
 * Represents the metadata of a given method.
 */
public interface MethodMetadata extends MethodReference, SecuredObject
{
    /**
     * Indicates if this method is a lambda method.
     *
     * @return True when this is a lambda.
     */
    boolean isLambda();

    /**
     * The target of this method if it is a bouncer.
     * Is null if this method is not a bouncer.
     *
     * @return The bouncer target.
     */
    MethodReference getBouncingTarget();

    /**
     * The methods this method overrides in super classes that the owner either extends or inherits from.
     *
     * @return The methods this method overrides.
     */
    List<MethodReference> getOverrides();
}
