package com.baidu.rigel.profiler.main;

import java.lang.instrument.Instrumentation;

import com.baidu.rigel.profiler.instrument.ProfilerInstrument;

public class ProfilerStart {

	public static void premain(String args, Instrumentation inst) {
		inst.addTransformer(new ProfilerInstrument());
	}
	
}
