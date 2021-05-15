package org.parchmentmc.feather.io.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GSON adapter for {@link OffsetDateTime}s.
 */
public class OffsetDateTimeAdapter extends TypeAdapter<OffsetDateTime> {
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

    @Override
    public void write(JsonWriter out, OffsetDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(getFormatter().format(value));
    }

    @Override
    public OffsetDateTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return OffsetDateTime.parse(in.nextString(), getFormatter());
    }
}
