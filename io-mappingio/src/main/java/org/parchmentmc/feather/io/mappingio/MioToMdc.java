package org.parchmentmc.feather.io.mappingio;

import net.fabricmc.mappingio.FlatMappingVisitor;
import net.fabricmc.mappingio.MappingReader;
import net.fabricmc.mappingio.adapter.FlatAsRegularMappingVisitor;
import net.fabricmc.mappingio.tree.MappingTreeView;
import org.jetbrains.annotations.Nullable;
import org.parchmentmc.feather.mapping.MappingDataBuilder;
import org.parchmentmc.feather.mapping.MappingDataContainer;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.List;

public class MioToMdc {
    public static MappingDataContainer fromPath(Path path) throws IOException {
        ConvertingVisitor converter = new ConvertingVisitor();
        MappingReader.read(path, new FlatAsRegularMappingVisitor(converter));
        return converter.getResult();
    }

    public static MappingDataContainer fromReader(Reader reader) throws IOException {
        ConvertingVisitor converter = new ConvertingVisitor();
        MappingReader.read(reader, new FlatAsRegularMappingVisitor(converter));
        return converter.getResult();
    }

    public static MappingDataContainer fromTree(MappingTreeView tree) throws IOException {
        ConvertingVisitor converter = new ConvertingVisitor();
        tree.accept(new FlatAsRegularMappingVisitor(converter));
        return converter.getResult();
    }

    private static class ConvertingVisitor implements FlatMappingVisitor {
        MappingDataBuilder mdcBuilder = new MappingDataBuilder();
        MappingDataBuilder.MutableClassData cls;
        MappingDataBuilder.MutableFieldData fld;
        MappingDataBuilder.MutableMethodData mth;
        MappingDataBuilder.MutableParameterData arg;

        @Override
        public void visitNamespaces(String srcNamespace, List<String> dstNamespaces) throws IOException {
        }

        @Override
        public boolean visitClass(String srcName, String[] dstNames) throws IOException {
            cls = mdcBuilder.getOrCreateClass(srcName);

            return true;
        }

        @Override
        public void visitClassComment(String srcName, String[] dstNames, String comment) throws IOException {
            cls.addJavadoc(splitComment(comment));
        }

        @Override
        public boolean visitField(String srcClsName, String srcName, @Nullable String srcDesc, @Nullable String[] dstClsNames, @Nullable String[] dstNames, @Nullable String[] dstDescs) throws IOException {
            fld = cls.getOrCreateField(srcName, srcDesc);

            return true;
        }

        @Override
        public void visitFieldComment(String srcClsName, String srcName, @Nullable String srcDesc, @Nullable String[] dstClsNames, @Nullable String[] dstNames, @Nullable String[] dstDescs, String comment) throws IOException {
            fld.addJavadoc(splitComment(comment));
        }

        @Override
        public boolean visitMethod(String srcClsName, String srcName, @Nullable String srcDesc, @Nullable String[] dstClsNames, @Nullable String[] dstNames, @Nullable String[] dstDescs) throws IOException {
            mth = cls.getOrCreateMethod(srcName, srcDesc);

            return true;
        }

        @Override
        public void visitMethodComment(String srcClsName, String srcName, @Nullable String srcDesc, @Nullable String[] dstClsNames, @Nullable String[] dstNames, @Nullable String[] dstDescs, String comment) throws IOException {
            mth.addJavadoc(splitComment(comment));
        }

        @Override
        public boolean visitMethodArg(String srcClsName, String srcMethodName, @Nullable String srcMethodDesc, int argPosition, int lvIndex, @Nullable String srcName, @Nullable String[] dstClsNames, @Nullable String[] dstMethodNames, @Nullable String[] dstMethodDescs, String[] dstNames) throws IOException {
            if (lvIndex > Byte.MAX_VALUE) throw new IOException("Feather doesn't support lvIndices larger than " + Byte.MAX_VALUE);
            arg = mth.getOrCreateParameter((byte) lvIndex);

            return true;
        }

        @Override
        public void visitMethodArgComment(String srcClsName, String srcMethodName, @Nullable String srcMethodDesc, int argPosition, int lvIndex, @Nullable String srcName, @Nullable String[] dstClsNames, @Nullable String[] dstMethodNames, @Nullable String[] dstMethodDescs, @Nullable String[] dstNames, String comment) throws IOException {
            arg.addJavadoc(splitComment(comment));
        }

        @Override
        public boolean visitMethodVar(String srcClsName, String srcMethodName, @Nullable String srcMethodDesc, int lvtRowIndex, int lvIndex, int startOpIdx, int endOpIdx, @Nullable String srcName, @Nullable String[] dstClsNames, @Nullable String[] dstMethodNames, @Nullable String[] dstMethodDescs, String[] dstNames) throws IOException {
            return false;
        }

        @Override
        public void visitMethodVarComment(String srcClsName, String srcMethodName, @Nullable String srcMethodDesc, int lvtRowIndex, int lvIndex, int startOpIdx, int endOpIdx, @Nullable String srcName, @Nullable String[] dstClsNames, @Nullable String[] dstMethodNames, @Nullable String[] dstMethodDescs, @Nullable String[] dstNames, String comment) throws IOException {
        }

        MappingDataContainer getResult() {
            return mdcBuilder;
        }
    }

    private static String[] splitComment(String comment) {
        return comment.split("\n");
    }
}
