package com.baidu.rigel.profiler.instrument;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class ProfilerInstrument implements ClassFileTransformer {

	public static boolean include(String className) {
		if (className.startsWith("com/lmax")) {
			return true;
		}
		return false;
	}
	
	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {
		
			if (include(className)) {
				try {
					ClassReader reader = new ClassReader(classfileBuffer);
					ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
					ClassVisitor adapter = new ProfilerClassVisitor(writer);
					reader.accept(adapter, ClassReader.EXPAND_FRAMES);
					// 生成新类字节码
					return writer.toByteArray();
				} catch (Exception e) {
					e.printStackTrace();
					// 返回旧类字节码
					return classfileBuffer;
				}
			}
			return classfileBuffer;
	}

}
