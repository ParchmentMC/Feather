package org.parchmentmc.feather.manifests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

public class LauncherManifestTest {
    @Test
    public void testLatestVersionInfo() {
        assertSerialization(new LauncherManifest.LatestVersionInfo("1.0.0", "21w01a"));
    }

    @Test
    public void testVersionData() {
        assertSerialization(new LauncherManifest.VersionData("21w01a", "release",
                "https://example.com/manifest.json", OffsetDateTime.MIN, OffsetDateTime.MIN,
                "00000000000000000000000000000000000000000", 0));
    }

    @Test
    public void testLauncherManifest() {
        List<LauncherManifest.VersionData> versions = Arrays.asList(
                new LauncherManifest.VersionData("21w01a", "release",
                        "https://example.com/manifest.json", OffsetDateTime.MIN, OffsetDateTime.MIN,
                        "00000000000000000000000000000000000000000", 0),
                new LauncherManifest.VersionData("21w02a", "snapshot",
                        "https://example.com/manifest2.json", OffsetDateTime.MAX, OffsetDateTime.MAX,
                        "00000000000000000000000000000000000000000", 0));

        assertSerialization(new LauncherManifest(new LauncherManifest.LatestVersionInfo("1.0.0", "21w01a"),
                versions));
    }

    private void assertSerialization(Object obj) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(output)) {
            objectOutput.writeObject(obj);
        } catch (IOException e) {
            Assertions.fail("Exception while serializing object", e);
        }

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        Object resultingObj = new Object();
        try (ObjectInputStream objectInput = new ObjectInputStream(input)) {
            resultingObj = objectInput.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Assertions.fail("Exception while deserializing object", e);
        }

        Assertions.assertSame(obj.getClass(), resultingObj.getClass(), "Deserialized object is not of same class as expected object");
        Assertions.assertEquals(obj, resultingObj, "Deserialized object is not equal to expected object");
    }
}
