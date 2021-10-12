package me.modmuss50.optifabric.patcher.fixes;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import me.modmuss50.optifabric.util.RemappingUtils;

public class AmbientOcclusionCalculatorFix implements ClassFixer {
	@Override
	public void fix(ClassNode optifine, ClassNode minecraft) {
		//OptiFine manages to switch the constructors around so Indigo expecting the first to take a BlockModelRenderer is wrong
		//Simple enough to go fishing for the right constructor and put it back though
		String targetDesc = "(L" + RemappingUtils.getClassName("class_778") + ";)V";

		MethodNode constructor = null;
		for (MethodNode method : optifine.methods) {
			if ("<init>".equals(method.name) && targetDesc.equals(method.desc)) {
				constructor = method;
				break;
			}
		}

		if (constructor != null && optifine.methods.remove(constructor)) {
			optifine.methods.add(0, constructor);
		}
	}
}