package org.parchmentmc.feather.io.moshi;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Moshi adapter for {@link OffsetDateTime}s.
 */
public class OffsetDateTimeAdapter {
    private final DateTimeFormatter formatter;

    /**
     * Constructs this adapter with the specified formatter.
     *
     * @param formatter the date time formatter formatter
     */
    public OffsetDateTimeAdapter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * Constructs this adapter, using the {@link DateTimeFormatter#ISO_OFFSET_DATE_TIME} formatter.
     */
    public OffsetDateTimeAdapter() {
        this(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    /**
     * Returns the date time formatter in use by this adapter.
     *
     * @return the formatter used by this adapter
     */
    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    @ToJson
    String toJson(OffsetDateTime dateTime) {
        return formatter.format(dateTime);
    }

    @FromJson
    OffsetDateTime fromJson(String str) {
        return OffsetDateTime.parse(str, formatter);
    }
}
