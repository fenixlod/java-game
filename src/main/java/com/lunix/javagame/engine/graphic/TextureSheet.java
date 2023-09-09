package com.lunix.javagame.engine.graphic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import com.lunix.javagame.configs.ResourceConfigs.SpriteSheetData;
import com.lunix.javagame.engine.enums.TextureType;

public class TextureSheet {
	private static final Logger logger = LogManager.getLogger(TextureSheet.class);
	private Texture texture;
	private List<Sprite> sprites;
	private int spriteWidth;
	private int spriteHeight;

	public TextureSheet(TextureType type, SpriteSheetData data) {
		this.texture = new Texture(type, data.path());
		this.sprites = new ArrayList<>();
		this.spriteWidth = data.width();
		this.spriteHeight = data.height();
	}

	public TextureSheet load() throws IOException {
		this.sprites.clear();

		texture.load();
		Vector2f total = new Vector2f(this.texture.width(), this.texture.height());

		if (this.spriteWidth == 0 || this.spriteHeight == 0) {
			this.spriteWidth = texture.width();
			this.spriteHeight = texture.height();
		}

		float currentX = 0, currentY = this.texture.height() - this.spriteHeight;

		while (true) {
			Vector2f[] uv = new Vector2f[] { new Vector2f(), new Vector2f(), new Vector2f(), new Vector2f() };
			uv[0].x = currentX;
			uv[0].y = currentY;

			uv[1].x = currentX + this.spriteWidth;
			uv[1].y = currentY;

			uv[2].x = currentX + this.spriteWidth;
			uv[2].y = currentY + this.spriteHeight;

			uv[3].x = currentX;
			uv[3].y = currentY + this.spriteHeight;

			uv[0].div(total);
			uv[1].div(total);
			uv[2].div(total);
			uv[3].div(total);
			this.sprites.add(new Sprite(this.texture.type(), uv));

			currentX += this.spriteWidth;
			if (currentX + this.spriteWidth > total.x) {
				currentX = 0;
				currentY -= this.spriteHeight;
			}

			if (currentY < 0)
				break;
		}

		return this;
	}

	public Sprite get(int index) {
		if (index < 0 || index >= this.sprites.size()) {
			logger.error("Invalid sheet sprite index: {}, texture: {}, sprites count: {}", index, texture,
					this.sprites.size());
			index = 0;

		}
		return this.sprites.get(index);
	}

	public int size() {
		return this.sprites.size();
	}

	public TextureSheet texture(Texture texture) {
		this.texture = texture;
		return this;
	}

	public Texture texture() {
		return this.texture;
	}

	public TextureSheet spriteWidth(int spriteWidth) {
		this.spriteWidth = spriteWidth;
		return this;
	}

	public TextureSheet spriteHeight(int spriteHeight) {
		this.spriteHeight = spriteHeight;
		return this;
	}

	public List<Sprite> sprites() {
		return this.sprites;
	}
}
