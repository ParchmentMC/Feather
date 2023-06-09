package org.parchmentmc.feather.spi;

public interface IOAdapterFactory {

    /**
     * Creates a {@link IOAdapter <T>} for the passed in Class
     *
     * @param clazz The class of the object to convert
     * @param <T>   The type of the object to convert
     * @return an {@link IOAdapter <T>} configured for the specified Class
     */
    <T> IOAdapter<T> create(Class<T> clazz);

}
