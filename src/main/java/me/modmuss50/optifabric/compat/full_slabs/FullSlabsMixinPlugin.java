package me.modmuss50.optifabric.compat.full_slabs;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.tinyremapper.IMappingProvider.Member;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;

public class FullSlabsMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("BlockRenderManagerMixin".equals(mixinInfo.getName())) {//BlockModels, getModel, (BlockState)BakedModel
			Member getModel = RemappingUtils.mapMethod("class_773", "method_3335", "(Lnet/minecraft/class_2680;)Lnet/minecraft/class_1087;");
			String renderDamageDesc = "(Lnet/minecraft/class_2680;Lnet/minecraft/class_2338;Lnet/minecraft/class_1920;Lnet/minecraft/class_4587;Lnet/minecraft/class_4588;)V";
			String renderDamage = RemappingUtils.getMethodName("class_776", "method_23071", renderDamageDesc); //(BlockState, BlockPos, BlockRenderView, MatrixStack, VertexConsumer)
			renderDamageDesc = RemappingUtils.mapMethodDescriptor(renderDamageDesc); //^ BlockRenderManager, renderDamage 

			for (MethodNode method : targetClass.methods) {
				if (renderDamage.equals(method.name) && renderDamageDesc.equals(method.desc)) {
					LabelNode skip = new LabelNode();

					InsnList extra = new InsnList();
					extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
					extra.add(new InsnNode(Opcodes.NULL));
					extra.add(new InsnNode(Opcodes.NULL));
					extra.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, getModel.owner, getModel.name, getModel.desc, false));
					extra.add(new InsnNode(Opcodes.POP));
					extra.add(skip);

					method.instructions.insertBefore(method.instructions.getLast(), extra);
					break;
				}
			}
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}