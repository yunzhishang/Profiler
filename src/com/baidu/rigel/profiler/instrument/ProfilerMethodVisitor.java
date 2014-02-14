package com.baidu.rigel.profiler.instrument;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class ProfilerMethodVisitor extends AdviceAdapter {
	
	private int time;
	
	private String className;

	private String methodFullName;

	private Type[] parameters;
	
	protected ProfilerMethodVisitor(
	        final int access,
	        final String className,
	        final String methodName,
	        final String desc,
	        final MethodVisitor mv){
		super(Opcodes.ASM4, mv, access, methodName, desc);
		this.className = className;
		this.methodFullName = className + "." + methodName;
		this.parameters = Type.getArgumentTypes(desc);
	}

	/**
     * Visits a zero operand instruction.
     *
     * @param opcode the opcode of the instruction to be visited. This opcode is
     *        either NOP, ACONST_NULL, ICONST_M1, ICONST_0, ICONST_1, ICONST_2,
     *        ICONST_3, ICONST_4, ICONST_5, LCONST_0, LCONST_1, FCONST_0,
     *        FCONST_1, FCONST_2, DCONST_0, DCONST_1, IALOAD, LALOAD, FALOAD,
     *        DALOAD, AALOAD, BALOAD, CALOAD, SALOAD, IASTORE, LASTORE, FASTORE,
     *        DASTORE, AASTORE, BASTORE, CASTORE, SASTORE, POP, POP2, DUP,
     *        DUP_X1, DUP_X2, DUP2, DUP2_X1, DUP2_X2, SWAP, IADD, LADD, FADD,
     *        DADD, ISUB, LSUB, FSUB, DSUB, IMUL, LMUL, FMUL, DMUL, IDIV, LDIV,
     *        FDIV, DDIV, IREM, LREM, FREM, DREM, INEG, LNEG, FNEG, DNEG, ISHL,
     *        LSHL, ISHR, LSHR, IUSHR, LUSHR, IAND, LAND, IOR, LOR, IXOR, LXOR,
     *        I2L, I2F, I2D, L2I, L2F, L2D, F2I, F2L, F2D, D2I, D2L, D2F, I2B,
     *        I2C, I2S, LCMP, FCMPL, FCMPG, DCMPL, DCMPG, IRETURN, LRETURN,
     *        FRETURN, DRETURN, ARETURN, RETURN, ARRAYLENGTH, ATHROW,
     *        MONITORENTER, or MONITOREXIT.
     */
	@Override
	public void visitInsn(final int opcode) {
		if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
			mv.visitVarInsn(Opcodes.LLOAD, time);
			mv.visitLdcInsn(methodFullName);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/baidu/rigel/profiler/Profiler",
					"end", "(JLjava/lang/String;)V");
		}
		super.visitInsn(opcode);
	}
	
	public void visitCode() {
		
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/baidu/rigel/profiler/Profiler",
				"start", "()J");
		time = newLocal(Type.LONG_TYPE);
		mv.visitVarInsn(Opcodes.LSTORE, time);
		
		super.visitCode();
	}
	
	public void visitMaxs(int maxStack, int maxLocals) {
		super.visitMaxs(maxStack, maxLocals + 3);
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
