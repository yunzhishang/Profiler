package com.baidu.rigel.profiler.instrument;

import java.io.FileOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;


public class ProfilerTransformer {

	public static void main(String[] args) throws Exception {
		ClassReader reader = new ClassReader("com.baidu.disruptor.DisruptorTest");
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		ClassVisitor adapter = new ProfilerClassVisitor(writer);
		reader.accept(adapter, ClassReader.EXPAND_FRAMES);
		
		byte[] bytecode = writer.toByteArray();
		 
		try {
		    FileOutputStream fos = new FileOutputStream("E:\\instrument\\DisruptorTest.class");
		    fos.write(bytecode);
		} catch (Exception e) {
			
		}

	}
	

}
