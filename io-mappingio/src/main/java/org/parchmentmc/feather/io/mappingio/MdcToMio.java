package org.parchmentmc.feather.io.mappingio;

import net.fabricmc.mappingio.MappedElementKind;
import net.fabricmc.mappingio.MappingVisitor;
import net.fabricmc.mappingio.tree.MappingTree;
import net.fabricmc.mappingio.tree.MemoryMappingTree;
import org.parchmentmc.feather.mapping.MappingDataContainer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MdcToMio {
    /**
     * Constructs a new {@link MappingTree} from a given {@link MappingDataContainer}'s content.
     * All data is added to the source namespace.
     *
     * @param mdc The {@link MappingDataContainer} to read from.
     * @param srcNs The source namespace to use for the tree.
     * @return The constructed {@link MappingTree}.
     * @throws IOException If an I/O error occurs.
     */
    public static MappingTree toTree(MappingDataContainer mdc, String srcNs) throws IOException {
        MemoryMappingTree tree = new MemoryMappingTree();
        accept(mdc, tree, srcNs);
        return tree;
    }

    /**
     * Reads mapping data from a given {@link MappingDataContainer} and passes it to a {@link MappingVisitor} (on the source namespace).
     * 
     * @param mdc The {@link MappingDataContainer} to read from.
     * @param visitor The {@link MappingVisitor} to pass the data to.
     * @param srcNs The source namespace to pass to the visitor.
     * @throws IOException If an I/O error occurs.
     */
    public static void accept(MappingDataContainer mdc, MappingVisitor visitor, String srcNs) throws IOException {
        if (visitor.visitHeader()) {
            visitor.visitNamespaces(srcNs, Collections.emptyList());
        }

        if (visitor.visitContent()) {
            for (MappingDataContainer.ClassData cls : mdc.getClasses()) {
                if (!visitor.visitClass(cls.getName())) continue;
                if (!visitor.visitElementContent(MappedElementKind.CLASS)) continue;
                
                if (!cls.getJavadoc().isEmpty()) {
                    visitor.visitComment(MappedElementKind.CLASS, joinComment(cls.getJavadoc()));
                }
                
                for (MappingDataContainer.FieldData fld : cls.getFields()) {
                    if (!visitor.visitField(fld.getName(), fld.getDescriptor())) continue;
                    if (!visitor.visitElementContent(MappedElementKind.FIELD)) continue;
                    
                    if (!fld.getJavadoc().isEmpty()) {
                        visitor.visitComment(MappedElementKind.FIELD, joinComment(fld.getJavadoc()));
                    }
                }

                for (MappingDataContainer.MethodData mth : cls.getMethods()) {
                    if (!visitor.visitMethod(mth.getName(), mth.getDescriptor())) continue;
                    if (!visitor.visitElementContent(MappedElementKind.METHOD)) continue;

                    if (!mth.getJavadoc().isEmpty()) {
                        visitor.visitComment(MappedElementKind.FIELD, joinComment(mth.getJavadoc()));
                    }
                    
                    for (MappingDataContainer.ParameterData arg : mth.getParameters()) {
                        if (!visitor.visitMethodArg(-1, arg.getIndex(), arg.getName())) continue;
                        if (!visitor.visitElementContent(MappedElementKind.METHOD_ARG)) continue;

                        if (arg.getJavadoc() != null) {
                            visitor.visitComment(MappedElementKind.METHOD_ARG, arg.getJavadoc());
                        }
                    }
                }
            }
        }
    }
    
    private static String joinComment(List<String> commentLines) {
        return String.join("\n", commentLines);
    }
}
