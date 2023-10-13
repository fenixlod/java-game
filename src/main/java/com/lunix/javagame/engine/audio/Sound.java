package com.lunix.javagame.engine.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

public class Sound {
	private static final Logger logger = LogManager.getLogger(Sound.class);
	private int bufferId;
	private int sourceId;
	private String filePath;
	private boolean isPlaying;
	private boolean loops;
	private boolean loaded;

	public Sound() {
		this("", false);
	}

	public Sound(String filePath, boolean loops) {
		this.filePath = filePath;
		this.loops = loops;
	}

	public void load() throws IOException {
		if (loaded)
			return;
		
		Path resourcePath = new ClassPathResource(filePath).getFile().toPath();

		// Allocate space to store returned information from stb
		stackPush();
		IntBuffer channelsBuffer = stackMallocInt(1);
		stackPush();
		IntBuffer sampleRateBuffer = stackMallocInt(1);
		ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(resourcePath.toString(), channelsBuffer,
				sampleRateBuffer);
		if (rawAudioBuffer == null) {
			logger.error("Cannot load sound: {}", filePath);
			stackPop();
			stackPop();
			throw new IOException("Cannot load sound: " + filePath);
		}

		// Retrieve the extra information stored in the buffers
		int channels = channelsBuffer.get();
		int sampleRate = sampleRateBuffer.get();
		// Free
		stackPop();
		stackPop();

		// Find the correct openAL format
		int format = -1;
		if (channels == 1) {
			format = AL_FORMAT_MONO16;
		} else if (channels == 2) {
			format = AL_FORMAT_STEREO16;
		}

		bufferId = alGenBuffers();
		alBufferData(bufferId, format, rawAudioBuffer, sampleRate);
		// Generate the source
		sourceId = alGenSources();
		alSourcei(sourceId, AL_BUFFER, bufferId);
		alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);
		alSourcei(sourceId, AL_POSITION, 0);
		alSourcef(sourceId, AL_GAIN, 0.3f);// Volume

		// Free raw audio buffer
		memFree(rawAudioBuffer);

		loaded = true;
	}

	public void delete() {
		alDeleteSources(sourceId);
		alDeleteBuffers(bufferId);
	}

	public void play() {
		int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
		if (state == AL_STOPPED) {
			isPlaying = false;
			alSourcei(sourceId, AL_POSITION, 0);
		}

		if (!isPlaying) {
			alSourcePlay(sourceId);
			isPlaying = true;
		}
	}

	public void stop() {
		if (isPlaying) {
			alSourceStop(sourceId);
			isPlaying = false;
		}
	}

	public String filePath() {
		return filePath;
	}

	public boolean isPlaying() {
		int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
		if (state == AL_STOPPED) {
			isPlaying = false;
		}

		return isPlaying;
	}
}
