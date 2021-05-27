package org.parchmentmc.feather.metadata;

import org.parchmentmc.feather.util.AccessFlag;

/**
 * Represents an object which has JVM Access flags associated with it.
 */
public interface WithSecurity
{
    /**
     * Represents the ASM Bit field that specifies the access level to this object.
     *
     * @return The ASM access level bit field.
     */
    int getSecuritySpecification();

    /**
     * Indicates is this secured object has a given access flag enabled or not.
     *
     * @param flag The flag to check.
     * @return {@code true} when the flag is enabled, {@code false} when not.
     */
    default boolean hasAccessFlag(final AccessFlag flag) {
        return flag.isActive(getSecuritySpecification());
    }

    /**
     * Indicates if this secured object is publicly accessible.
     *
     * @return {@code true} when publicly accessible.
     */
    default boolean isPublic() {
        return hasAccessFlag(AccessFlag.PUBLIC);
    }

    /**
     * Indicates if this secured object is privately accessible.
     *
     * @return {@code true} when privately accessible.
     */
    default boolean isPrivate() {
        return hasAccessFlag(AccessFlag.PRIVATE);
    }

    /**
     * Indicates if this secured object is protected accessible.
     *
     * @return {@code true} when protected accessible.
     */
    default boolean isProtected() {
        return hasAccessFlag(AccessFlag.PROTECTED);
    }

    /**
     * Indicates if this secured object is statically accessible.
     *
     * @return {@code true} when statically accessible.
     */
    default boolean isStatic() {
        return hasAccessFlag(AccessFlag.STATIC);
    }

    /**
     * Indicates if this secured object is final.
     *
     * @return {@code true} when final.
     */
    default boolean isFinal() {
        return hasAccessFlag(AccessFlag.FINAL);
    }

    /**
     * Indicates if this is a super class.
     *
     * @return {@code true} when this is a super class.
     */
    default boolean isSuper() {
        return hasAccessFlag(AccessFlag.SUPER);
    }
}
