package com.baidu.rigel.profiler;

public class Profiler {

	public static long start() {
		System.out.println("-------------------");
		return System.currentTimeMillis();
	}
	
	public static void end(long startTime, String name) {
		System.out.println(name + " invocation time: " + (System.currentTimeMillis() - startTime) + "ms.");
	}
	
	public static void xxxx(String className, String methodName, Object[] params) {
		System.out.println(className);
		System.out.println(methodName);
		if (null != params) {
			for (int i = 0; i < params.length; i++) {
				if (null != params[i]) {
					System.out.println(" ----- > method param: " + params[i].toString());
				}
			}
		}
	}
	
	private static String getInvokeStack(Throwable t) {
		StackTraceElement ste[] = t.getStackTrace();
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < ste.length; i++) {
			sb.append(ste[i].toString()).append(";").append("\r\n");
		}
		return sb.toString();
	}
	
}
