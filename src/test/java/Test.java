import com.tfc.effekseer4j.Effekseer;
import com.tfc.effekseer4j.EffekseerEffect;
import com.tfc.effekseer4j.EffekseerManager;
import com.tfc.effekseer4j.EffekseerParticleEmitter;
import com.tfc.effekseer4j.enums.TextureType;
import com.tfc.effekseer4j.natives_config.InitializationConfigs;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.io.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.*;

public class Test {
	public static void main(String[] args) throws IOException {
		PrintStream oldOut = System.out;
		System.setOut(new PrintStream(new File("console.txt")) {
			@Override
			public void write(byte[] buf, int off, int len) {
				super.write(buf, off, len);
				oldOut.write(buf, off, len);
			}
			
			@Override
			public void write(int b) {
				super.write(b);
				oldOut.write(b);
			}
			
			@Override
			public void print(String s) {
				super.print(s);
			}
		});
		System.out.print("renderDevice: ");
		System.out.println(Effekseer.getDevice());
		System.out.print("OS: ");
		System.out.println(InitializationConfigs.getOs());
		if (!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		long window = GLFW.glfwCreateWindow(1000, 1000, "Test", MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL) throw new RuntimeException("Failed to create the GLFW window");
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1);
		GL.createCapabilities();
		GLFW.glfwShowWindow(window);
		System.out.print("successfulSetup: ");
		System.out.println(Effekseer.setupForOpenGL());
		System.out.print("renderDevice: ");
		System.out.println(Effekseer.getDevice());
		EffekseerManager manager = new EffekseerManager();
		manager.initialize(100);
		manager.setViewport(1000, 1000);
		EffekseerEffect effect = new EffekseerEffect();
		InputStream stream = Test.class.getClassLoader().getResourceAsStream("test.efkefc");
		System.out.print("isEffectLoaded: ");
		System.out.println(effect.isLoaded());
		assert stream != null;
		effect.load(stream, stream.available(), 1);
		stream = Test.class.getClassLoader().getResourceAsStream("test.png");
		assert stream != null;
		effect.loadTexture(stream, stream.available(), 0, TextureType.COLOR);
		stream = Test.class.getClassLoader().getResourceAsStream("test1.png");
		assert stream != null;
		effect.loadTexture(stream, stream.available(), 1, TextureType.COLOR);
		System.out.println();
		System.out.println("Effect info:");
		System.out.print(" curveCount: ");
		System.out.println(effect.curveCount());
		System.out.print(" materialCount: ");
		System.out.println(effect.materialCount());
		System.out.print(" modelCount: ");
		System.out.println(effect.modelCount());
		System.out.print(" textureCount: ");
		System.out.println(effect.textureCount());
		System.out.print(" texturePath0: ");
		System.out.println(effect.getTexturePath(0, TextureType.COLOR));
		System.out.print(" texturePath1: ");
		System.out.println(effect.getTexturePath(1, TextureType.COLOR));
		System.out.print(" isLoaded: ");
		System.out.println(effect.isLoaded());
		System.out.print(" minTerm: ");
		System.out.println(effect.minTerm());
		System.out.print(" maxTerm: ");
		System.out.println(effect.maxTerm());
		System.out.println();
		EffekseerParticleEmitter particle = manager.createParticle(effect);
		System.out.println("Particle 0:");
		System.out.println(" handle:" +particle.handle);
		System.out.println(" isPaused:" +particle.isPaused);
		System.out.println(" isVisible:" +particle.isVisible);
		System.out.println();
		EffekseerParticleEmitter particle1 = manager.createParticle(effect);
		System.out.println("Particle 1:");
		System.out.println(" handle:" + particle1.handle);
		System.out.println(" isPaused:" + particle1.isPaused);
		System.out.println(" isVisible:" + particle1.isVisible);
		System.out.println();
		System.out.println("particle.equals(particle1) == " + particle.equals(particle1));
		//framebuffer usage is optional
		int framebuffer = GL30.glGenFramebuffers();
		int renderbuffer = GL30.glGenRenderbuffers();
		int texture = GL30.glGenTextures();
		int width = 1000;
		int height = 1000;
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);
		glBindRenderbuffer(GL_RENDERBUFFER, renderbuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderbuffer);
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		while (!GLFW.glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture);
			glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
			manager.update(1);
			//active shaders here
			manager.draw();
			glBindFramebuffer(GL_FRAMEBUFFER, 0);
			//activate post processing shaders here
			drawQuad();
			GLFW.glfwSwapBuffers(window);
			GLFW.glfwPollEvents();
		}
		glDeleteFramebuffers(framebuffer);
		glDeleteRenderbuffers(renderbuffer);
		glDeleteTextures(texture);
		manager.stopEffects();
		Effekseer.finish();
		manager.delete();
		Effekseer.finish();
	}
	
	private static void drawQuad() {
		glBegin(GL_QUADS);
		glColor3f(0, 0, 0);
		glVertex2d(-1, -1);
		glColor3f(0, 0, 0);
		glVertex2d(-1, 1);
		glColor3f(0, 0, 0);
		glVertex2d(1, 1);
		glColor3f(0, 0, 0);
		glVertex2d(1, -1);
		glEnd();
	}
}
