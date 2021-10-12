package me.modmuss50.optifabric.patcher.fixes;

import com.google.common.collect.MoreCollectors;

import org.apache.commons.lang3.Validate;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import me.modmuss50.optifabric.util.RemappingUtils;

public class KeyboardFix implements ClassFixer {
	//net/minecraft/client/Keyboard.onKey(JIIII)V
	private final String onKeyName = RemappingUtils.getMethodName("class_309", "method_1466", "(JIIII)V");

	@Override
	public void fix(ClassNode optifine, ClassNode minecraft) {
		Validate.notNull(onKeyName, "onKeyName null");

		//Remove the old "broken" method
		optifine.methods.removeIf(methodNode -> methodNode.name.equals(onKeyName));

		//Find the vanilla method
		MethodNode methodNode = minecraft.methods.stream().filter(node -> node.name.equals(onKeyName)).collect(MoreCollectors.onlyElement());
		Validate.notNull(methodNode, "old method null");
		
		//Find the lambda inside the vanilla method (not matched as Optifine changes its descriptor)
		MethodNode lambdaNode = minecraft.methods.stream().filter(node -> "method_1454".equals(node.name)).collect(MoreCollectors.onlyElement());
		Validate.notNull(lambdaNode, "old method lambda null");

		//Add the vanilla methods back in
		optifine.methods.add(methodNode);
		optifine.methods.add(lambdaNode);
	}
}