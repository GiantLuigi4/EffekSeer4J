package com.tfc.effekseer4j;

import Effekseer.swig.EffekseerCoreJNI;
import com.tfc.effekseer4j.natives_config.InitializationConfigs;

import java.io.*;

public class Library {
	static {
		try {
			File file = getDllFileEffekseer();
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
				
				ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
				InputStream stream = Library.class.getClassLoader().getResourceAsStream(
						"EffekseerNativeForJava" + (InitializationConfigs.os.equals("Win") ? ".dll" : ".so")
				);
				int b;
				while ((b = stream.read()) != -1) stream2.write(b);
				stream.close();
				byte[] bytes = stream2.toByteArray();
				
				FileOutputStream stream1 = new FileOutputStream(file);
				stream1.write(bytes);
				stream1.close();
				stream1.flush();
			}
		} catch (Throwable err) {
			if (err instanceof RuntimeException) throw (RuntimeException) err;
			throw new RuntimeException(err);
		}
	}
	
	public static File getDllFileEffekseer() {
		return new File(InitializationConfigs.binPath + "/effekseer/" + "EffekseerNativeForJava-effekseer4j-1" + (InitializationConfigs.os.equals("Win") ? ".dll" : ".so"));
	}
	
/*	private static final HashMap<String, byte[]> effekSeerJarEntries = new HashMap<>();
	private static final HashMap<String, byte[]> jni4netJarEntries = new HashMap<>();
	
//	public static final INJEnv env;
	
	protected static File getDll(ZipEntry entry) throws IOException {
		return extractDll(entry);
	}
	
	static {
		try {
//			File jni4net = downloadJni4netZip();
//			cacheZip(jni4net, jni4netJarEntries);
//			jni4netJarEntries.keySet().forEach((entry)->{
//				if (entry.endsWith(".dll") && entry.startsWith("lib")) {
//					try {
//						extractDll("jni4net", new ZipEntry(entry), jni4netJarEntries);
//					} catch (IOException e) {
//						throw new RuntimeException(e);
//					}
//				}
//			});

//			File effekSeer = downloadZipEffekseer();
//			cacheZip(effekSeer, effekSeerJarEntries);
//			effekSeerJarEntries.keySet().forEach((entry)->{
//				if (entry.endsWith(".dll") && entry.startsWith("Tool")) {
//					try {
//						extractDll(new ZipEntry(entry));
//					} catch (IOException e) {
//						throw new RuntimeException(e);
//					}
//				}
//			});

//			if (!Bridge.isRegistered()) {
//				Bridge.setVerbose(true);
//				Bridge.init(new File("bin/jni4net"));
//				Bridge.getSetup().setVeryVerbose(true);
//				Bridge.getSetup().setBindCLRTypes(true);
//				Bridge.getSetup().setDebug(true);
//				Bridge.getSetup().setBindStatic(true);
//				Bridge.getSetup().setBindNative(true);
//			}
//				try {
//					for (int i = 0; ; i++) {
//						System.out.println(Bridge.getKnownAssemblies().getItem(i));
//					}
//				} catch (Throwable ignored) {
//				}
////				System.out.println(obj.getClass());
////				env = (INJEnv) (obj);
//			}
		} catch (Throwable err) {
			if (err instanceof RuntimeException) throw (RuntimeException) err;
			throw new RuntimeException(err);
		}
	}
	
	private static void cacheZip(File file, HashMap<String, byte[]> map) throws IOException {
		ZipFile jar = new ZipFile(file);
		jar.stream().iterator().forEachRemaining((entry)->{
			try {
				InputStream stream = jar.getInputStream(entry);
				byte[] bytes = new byte[stream.available()];
				stream.read(bytes);
				stream.close();
				map.put(entry.getName(), bytes);
			} catch (Throwable err) {
				if (err instanceof RuntimeException) throw (RuntimeException) err;
				throw new RuntimeException(err);
			}
		});
		jar.close();
	}*/
	
	public static File getDllFile() {
		return new File(
				InitializationConfigs.binPath + "/" +
						InitializationConfigs.versionFile() + "/"
		);
	}
	
//	private static File extractDll(String path, String name, ClassLoader cl) throws IOException {
//		byte[] bytes;
//		{
//			InputStream stream = cl.getResourceAsStream(name);
//			bytes = new byte[stream.available()];
//			stream.read(bytes);
//			try {
//				stream.close();
//			} catch (Throwable err) {
//				err.printStackTrace();
//			}
//		}
//		File targ = new File(
//				InitializationConfigs.binPath + "/" +
//						path + "/" +
//						name.substring(name.lastIndexOf("/"))
//		);
//		if (!targ.exists()) {
//			targ.getParentFile().mkdirs();
//			targ.createNewFile();
//			FileOutputStream stream = new FileOutputStream(targ);
//			stream.write(bytes);
//			stream.close();
//		}
//		return targ;
//	}

/*	//TODO: fix this so JNI4NET doesn't error when trying to load the dll files extraxted using this
	private static File extractDll(String path, ZipEntry entry, HashMap<String, byte[]> map) throws IOException {
		File targ = new File(
				InitializationConfigs.binPath + "/" +
						path + "/" +
						entry.getName().substring(entry.getName().lastIndexOf("/"))
		);
		if (!targ.exists()) {
			targ.getParentFile().mkdirs();
			targ.createNewFile();
			byte[] bytes = map.get(entry.getName());
			FileOutputStream stream = new FileOutputStream(targ);
			stream.write(bytes);
			stream.close();
		}
		return targ;
	}
	
	private static File extractDll(ZipEntry entry) throws IOException {
		File targ = new File(
				InitializationConfigs.binPath + "/" +
						InitializationConfigs.versionFile() + "/" +
						entry.getName().substring(entry.getName().lastIndexOf("/"))
		);
		if (!targ.exists()) {
			targ.getParentFile().mkdirs();
			targ.createNewFile();
			byte[] bytes = effekSeerJarEntries.get(entry.getName());
			FileOutputStream stream = new FileOutputStream(targ);
			stream.write(bytes);
			stream.close();
			stream.flush();
		}
		return targ;
	}
	
	private static File downloadZipEffekseer() throws IOException {
		File file = new File(
				InitializationConfigs.libsPath + "/" +
						InitializationConfigs.versionFile() + "/" +
						InitializationConfigs.version + ".zip"
		);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
			String url = InitializationConfigs.getDownloadPath();
			BufferedInputStream stream1 = new BufferedInputStream(new URL(url).openStream());
			int b;
			FileOutputStream stream2 = new FileOutputStream(file);
			while ((b = stream1.read()) != -1) stream2.write(b);
			stream2.flush();
			stream1.close();
			stream2.close();
		}
		return file;
	}
	
	private static File downloadJni4netZip() throws IOException {
		File file = new File(InitializationConfigs.libsPath + "/jni4net/0.8.8.0.zip");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
			String url = InitializationConfigs.jni4netDownload;
			BufferedInputStream stream1 = new BufferedInputStream(new URL(url).openStream());
			int b;
			FileOutputStream stream2 = new FileOutputStream(file);
			while ((b = stream1.read()) != -1) stream2.write(b);
			stream1.close();
			stream2.close();
			stream2.flush();
		}
		return file;
	}*/
	
	protected static void init() {
	}
}
