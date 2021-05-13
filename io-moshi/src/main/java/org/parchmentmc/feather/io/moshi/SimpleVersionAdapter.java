package org.parchmentmc.feather.io.moshi;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import org.parchmentmc.feather.util.SimpleVersion;

/**
 * Moshi adapter for {@link SimpleVersion}.
 */
public class SimpleVersionAdapter {
    @ToJson
    String toJson(SimpleVersion version) {
        return version.toString();
    }

    @FromJson
    SimpleVersion fromJson(String json) {
        return new SimpleVersion(json);
    }
}
