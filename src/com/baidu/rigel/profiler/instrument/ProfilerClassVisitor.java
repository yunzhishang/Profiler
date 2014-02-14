package com.baidu.rigel.profiler.instrument;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ProfilerClassVisitor extends ClassVisitor {

	// set/get
	private List<String> fieldSetGetMethod = new ArrayList<String>();
	
	private String staticInitMethodName = "<clinit>";
	
	private String initMethodName = "<init>";
	
	private String className;
	
	public ProfilerClassVisitor(ClassVisitor classVisitor) {
		super(Opcodes.ASM4, classVisitor);
	}
	
	/**
     * Visits the header of the class.
     *
     * @param version the class version.
     * @param access the class's access flags (see {@link Opcodes}). This
     *        parameter also indicates if the class is deprecated.
     * @param name the internal name of the class (see
     *        {@link Type#getInternalName() getInternalName}).
     * @param signature the signature of this class. May be <tt>null</tt> if
     *        the class is not a generic one, and does not extend or implement
     *        generic classes or interfaces.
     * @param superName the internal of name of the super class (see
     *        {@link Type#getInternalName() getInternalName}). For interfaces,
     *        the super class is {@link Object}. May be <tt>null</tt>, but
     *        only for the {@link Object} class.
     * @param interfaces the internal names of the class's interfaces (see
     *        {@link Type#getInternalName() getInternalName}). May be
     *        <tt>null</tt>.
     */
	@Override
	public void visit(final int version, final int access, final String name,
			final String signature, final String superName,
			final String[] interfaces) {
		this.className = name.replace("/", ".");
		super.visit(version, access, name, signature, superName, interfaces);
	}
	
	/**
     * Visits a field of the class.
     *
     * @param access the field's access flags (see {@link Opcodes}). This
     *        parameter also indicates if the field is synthetic and/or
     *        deprecated.
     * @param name the field's name.
     * @param desc the field's descriptor (see {@link Type Type}).
     * @param signature the field's signature. May be <tt>null</tt> if the
     *        field's type does not use generic types.
     * @param value the field's initial value. This parameter, which may be
     *        <tt>null</tt> if the field does not have an initial value, must
     *        be an {@link Integer}, a {@link Float}, a {@link Long}, a
     *        {@link Double} or a {@link String} (for <tt>int</tt>,
     *        <tt>float</tt>, <tt>long</tt> or <tt>String</tt> fields
     *        respectively). <i>This parameter is only used for static fields</i>.
     *        Its value is ignored for non static fields, which must be
     *        initialized through bytecode instructions in constructors or
     *        methods.
     * @return a visitor to visit field annotations and attributes, or
     *         <tt>null</tt> if this class visitor is not interested in
     *         visiting these annotations and attributes.
     */
    public FieldVisitor visitField(
        int access,
        String name,
        String desc,
        String signature,
        Object value) {
    	
    	String field = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
		String getMethodName = "get" + field;
		String setMethodName = "set" + field;
		String isMethodName = "is" + field;
		
		fieldSetGetMethod.add(getMethodName);
		fieldSetGetMethod.add(setMethodName);
		fieldSetGetMethod.add(isMethodName);

		return super.visitField(access, name, desc, signature, value);
    }
      
    /**
     * Visits a method of the class. This method <i>must</i> return a new
     * {@link MethodVisitor} instance (or <tt>null</tt>) each time it is
     * called, i.e., it should not return a previously returned visitor.
     *
     * @param access the method's access flags (see {@link Opcodes}). This
     *        parameter also indicates if the method is synthetic and/or
     *        deprecated.
     * @param name the method's name.
     * @param desc the method's descriptor (see {@link Type Type}).
     * @param signature the method's signature. May be <tt>null</tt> if the
     *        method parameters, return type and exceptions do not use generic
     *        types.
     * @param exceptions the internal names of the method's exception classes
     *        (see {@link Type#getInternalName() getInternalName}). May be
     *        <tt>null</tt>.
     * @return an object to visit the byte code of the method, or <tt>null</tt>
     *         if this class visitor is not interested in visiting the code of
     *         this method.
     */
	@Override
	public final MethodVisitor visitMethod(final int access, final String name,
			final String desc, final String signature, final String[] exceptions) {
		// exclude set/get method
		if (!fieldSetGetMethod.contains(name)) {
			if (!name.equals(staticInitMethodName) && !name.equals(initMethodName)) {
				MethodVisitor mVisitor = super.visitMethod(access, name, desc, signature, exceptions);
				ProfilerMethodVisitor pmVisitor = new ProfilerMethodVisitor(access, name, name, desc, mVisitor);
				return pmVisitor;
			}
		} 
		return super.visitMethod(access, name, desc, signature, exceptions); 
	}

}
