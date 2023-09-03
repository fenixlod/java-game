package com.lunix.javagame.engine.graphic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import com.lunix.javagame.configs.ResourceConfigs.SpriteSheetData;
import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.exception.ResourceNotFound;

public class SpriteSheet {
	private TextureType texture;
	private List<Sprite> sprites;

	public SpriteSheet(SpriteSheetData spriteSheetData) throws ResourceNotFound, IOException {
		this(spriteSheetData.texture(), spriteSheetData.width(), spriteSheetData.height());
	}

	public SpriteSheet(TextureType textureType, int spriteWidth, int spriteHeight) throws ResourceNotFound, IOException {
		this.texture = textureType;
		this.sprites = new ArrayList<>();

		Texture texture = ResourcePool.getTexture(this.texture);
		Vector2f total = new Vector2f(texture.wdth(), texture.height());
		float currentX = 0, currentY = spriteHeight;
		
		while(true) {
			Vector2f[] uv = new Vector2f[] { new Vector2f(), new Vector2f(), new Vector2f(), new Vector2f() };
			uv[0].x = currentX;
			uv[0].y = currentY;

			uv[1].x = currentX + spriteWidth;
			uv[1].y = currentY;

			uv[2].x = currentX + spriteWidth;
			uv[2].y = currentY - spriteHeight;

			uv[3].x = currentX;
			uv[3].y = currentY - spriteHeight;
			
			uv[0].div(total);
			uv[1].div(total);
			uv[2].div(total);
			uv[3].div(total);
			sprites.add(new Sprite(this.texture, uv));
			
			currentX += spriteWidth;
			if (currentX + spriteWidth > total.x) {
				currentX = 0;
				currentY += spriteHeight;
			}

			if (currentY > total.y)
				break;
		}
	}

	public Sprite get(int index) {
		return this.sprites.get(index);
	}

	public int size() {
		return this.sprites.size();
	}
}
