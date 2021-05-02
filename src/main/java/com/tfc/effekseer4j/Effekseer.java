package com.tfc.effekseer4j;

import Effekseer.swig.EffekseerBackendCore;
import com.tfc.effekseer4j.enums.DeviceType;

import java.util.Objects;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class Effekseer {
	static {
		Library.init();
//		for (File file : Library.getDllFile().listFiles()) {
//			try {
//				Bridge.LoadAndRegisterAssemblyFrom(file, Effekseer.class.getClassLoader());
//			} catch (Throwable ignored) {
//				ignored.printStackTrace();
//			}
//		}
//		Bridge.LoadAndRegisterAssemblyFrom(new File(Library.getDllFile() + "/IronPython.dll"), Effekseer.class.getClassLoader());
//		Bridge.LoadAndRegisterAssemblyFrom(new File(Library.getDllFile() + "/IronPython.Modules.dll"), Effekseer.class.getClassLoader());
//		Bridge.LoadAndRegisterAssemblyFrom(new File(Library.getDllFile() + "/EffekseerCore.dll"), Effekseer.class.getClassLoader());
//		System.out.println(Type.GetType("Effekseer"));
//		System.out.println(Type.GetType("Effekseer.Core"));
//		try {
//			for (int i = 0; ; i++) {
//				System.out.println(Bridge.getKnownAssemblies().getItem(i));
//			}
//		} catch (Throwable ignored) {
//		}
//		Bridge.LoadAndRegisterAssemblyFrom(new File(Library.getDllFile() + "/EffekseerMaterialCompilerGL.dll"));
	}
	
	private final EffekseerBackendCore core;
	
	public Effekseer() {
		core = new EffekseerBackendCore();
	}
	
	public void delete() {
		core.delete();
	}
	
	@Override
	protected void finalize() throws Throwable {
		delete();
		super.finalize();
	}
	
	public static DeviceType getDevice() {
		return DeviceType.fromOrd(EffekseerBackendCore.GetDevice().swigValue());
	}
	
	public static boolean setupForOpenGL() {
		return EffekseerBackendCore.InitializeAsOpenGL();
	}
	
	public static void finish() {
		EffekseerBackendCore.Terminate();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Effekseer)) return false;
		return EffekseerBackendCore.getCPtr(core) == EffekseerBackendCore.getCPtr(((Effekseer) obj).core);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(core);
	}
	
	public EffekseerBackendCore unwrap() {
		return core;
	}
	
	public static void init(){}
}
