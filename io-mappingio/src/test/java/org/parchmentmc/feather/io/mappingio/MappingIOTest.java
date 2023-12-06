package org.parchmentmc.feather.io.mappingio;

import net.fabricmc.mappingio.MappedElementKind;
import net.fabricmc.mappingio.MappingUtil;
import net.fabricmc.mappingio.adapter.FlatAsRegularMappingVisitor;
import net.fabricmc.mappingio.tree.MappingTree;
import net.fabricmc.mappingio.tree.MemoryMappingTree;
import net.fabricmc.mappingio.tree.VisitableMappingTree;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MappingIOTest {
    @Test
    public void testMio() throws IOException {
        VisitableMappingTree tree = new MemoryMappingTree();
        String srcNs = MappingUtil.NS_SOURCE_FALLBACK;
        
        tree.visitNamespaces(srcNs, Collections.emptyList());
        
        tree.visitClass("net/minecraft/class_1");
        tree.visitComment(MappedElementKind.CLASS, "class_1 comment");
        
        tree.visitField("field_1", "I");
        tree.visitComment(MappedElementKind.FIELD, "field_1 comment");
        
        tree.visitMethod("method_1", "()V");
        tree.visitComment(MappedElementKind.METHOD, "method_1 comment");
        
        tree.visitMethodArg(-1, 0, "arg_1");
        tree.visitComment(MappedElementKind.METHOD_ARG, "arg_1 comment");
        
        tree.visitEnd();

        MappingTree convertedTree = MdcToMio.toTree(MioToMdc.fromTree(tree), srcNs);

        convertedTree.accept(new FlatAsRegularMappingVisitor(new SubsetAssertingVisitor(tree, null, null)));
        tree.accept(new FlatAsRegularMappingVisitor(new SubsetAssertingVisitor(convertedTree, null, null)));
    }
}
