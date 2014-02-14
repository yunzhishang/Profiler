package com.baidu.rigel.profiler.test;

public class Test {

	public static void test () throws Exception {
		Thread.sleep(1);
	}
	
	public static void main(String[] args) throws Exception {
		int i = 1;
		test();
		int j = 1;
	}
}
