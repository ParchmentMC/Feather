package org.parchmentmc.feather.metadata;

import java.util.List;

public interface MethodMetadata extends MethodReferenceMetadata
{

    /**
     * The mojmap name of the class that the method resides in.
     *
     * @return The mojmap name of the owner of the method.
     */
    String getMojMapOwnerName();

    /**
     * The mojmap name of the method.
     *
     * @return The mojmap name.
     */
    String getMojMapName();

    /**
     * The mojmap based descriptor of the method.
     *
     * @return The mojmap descriptor.
     */
    String getMojMapDescriptor();

    /**
     * Indicates if this method is a lambda method.
     *
     * @return True when this is a lambda.
     */
    boolean isLambda();

    /**
     * The ASM Access specification.
     * See the ASM OPPCODES for information.
     *
     * @return The ASM Access specification.
     */
    Integer getAccessSpecification();

    /**
     * The obfuscated signature of the method.
     *
     * @return The obfuscated signature.
     */
    String getObfuscatedSignature();

    /**
     * The mojmap signature of the method.
     *
     * @return The mojmap signature.
     */
    String getMojMapSignature();

    MethodReferenceMetadata getBouncingTarget();

    List<MethodReferenceMetadata> getOverrides();


}
