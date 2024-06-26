package net.pl3x.lib.gui.icon;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class Icon {
    private final ResourceLocation texture;

    public Icon(@NotNull ResourceLocation texture) {
        this.texture = texture;
    }

    public @NotNull ResourceLocation getTexture() {
        return this.texture;
    }

    public void render(@NotNull GuiGraphics gfx, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        gfx.blit(getTexture(), x, y, u, v, width, height, textureWidth, textureHeight);
    }
}
