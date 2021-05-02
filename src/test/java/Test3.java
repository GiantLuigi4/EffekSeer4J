import com.tfc.effekseer4j.natives_config.InitializationConfigs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Test3 {
	private static final String url = "https://github.com/effekseer/EffekseerForMultiLanguages/suites/2628870886/artifacts/57647090";
	
	public static void main(String[] args) throws IOException {
		File file = new File("libs/test.zip");
		file.getParentFile().mkdirs();
		file.createNewFile();
		BufferedInputStream stream1 = new BufferedInputStream(new URL(url).openStream());
		int b;
		FileOutputStream stream2 = new FileOutputStream(file);
		while ((b = stream1.read()) != -1) stream2.write(b);
		stream2.flush();
		stream1.close();
		stream2.close();
	}
}
