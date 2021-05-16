package org.parchmentmc.feather.mapping;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.parchmentmc.feather.util.SimpleVersion;

import java.util.Collection;

public class VersionedMDCDelegate<MDC extends MappingDataContainer> implements VersionedMappingDataContainer {
    private final SimpleVersion version;
    private final MDC delegate;

    public VersionedMDCDelegate(SimpleVersion version, MDC delegate) {
        this.version = version;
        this.delegate = delegate;
    }

    public MDC getDelegate() {
        return delegate;
    }

    @Override
    public SimpleVersion getFormatVersion() {
        return version;
    }

    @Override
    public Collection<? extends PackageData> getPackages() {
        return delegate.getPackages();
    }

    @Override
    public Collection<? extends ClassData> getClasses() {
        return delegate.getClasses();
    }

    @Override
    @Nullable
    public PackageData getPackage(String packageName) {
        return delegate.getPackage(packageName);
    }

    @Override
    @Nullable
    public ClassData getClass(String className) {
        return delegate.getClass(className);
    }
}
