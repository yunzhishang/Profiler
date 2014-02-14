package com.baidu.rigel.profiler.instrument;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class ProfilerMethodAdaptor extends AdviceAdapter {

	private String className;

	private String methodFullName;

	private Type[] parameters;

	protected ProfilerMethodAdaptor(MethodVisitor mv, int access,
			String className, String methodName, String desc) {
		super(Opcodes.ASM4, mv, access, methodName, desc);
		this.className = className;
		this.methodFullName = className + "." + methodName;
		this.parameters = Type.getArgumentTypes(desc);
	}

	/**
	 * Called at the beginning of the method or after super class class call in
	 * the constructor. <br>
	 * <br>
	 */
	protected void onMethodEnter() {
	}

	/**
	 * Called before explicit exit from the method using either return or throw.
	 * Top element on the stack contains the return value or exception instance.
	 */
	protected void onMethodExit(int opcode) {
		visitLdcInsn(className);
		visitLdcInsn(methodFullName);
		int argsCnt = 0;
		argsCnt = parameters.length;
		if (argsCnt > 0) {
			loadArgArray();
		} else {
			visitInsn(ACONST_NULL);
		}
		visitMethodInsn(Opcodes.INVOKESTATIC,
				"com/baidu/rigel/profiler/Profiler", "xxxx",
				"(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V");
	}

}
