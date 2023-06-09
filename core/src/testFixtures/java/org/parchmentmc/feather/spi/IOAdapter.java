package org.parchmentmc.feather.spi;

public interface IOAdapter<T> {

    /**
     * @return Simple descriptive name of the underlying implementation
     */
    String name();

    /**
     * Converts a Json representation into an instance of T
     * @param input Json
     * @return an instance of T
     */
    T fromJson(String input) throws Exception;

    /**
     * Converts an instance of T into an Json representation
     * @param value the Instance
     * @return Json
     */
    String toJson(T value) throws Exception;
}
