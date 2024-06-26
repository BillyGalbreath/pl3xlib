package net.pl3x.lib.gui.icon;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Arrays;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.metadata.animation.AnimationFrame;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.pl3x.lib.gui.GL;
import net.pl3x.lib.gui.animation.Animation;
import net.pl3x.lib.gui.animation.Easing;
import org.jetbrains.annotations.NotNull;

public class AnimatedIcon extends Icon {
    private final FrameSize size;
    private final int[] frames;
    private final Animation animation;

    private int currentFrame = 0;

    public AnimatedIcon(@NotNull ResourceLocation texture, @NotNull JsonObject animation, int size) {
        super(texture);

        AnimationMetadataSection meta = AnimationMetadataSection.SERIALIZER.fromJson(animation);

        this.size = meta.calculateFrameSize(size, size);
        this.frames = new int[this.size.width() * this.size.height()];

        Arrays.fill(this.frames, meta.getDefaultFrameTime());

        // todo - reorder frames from mcmeta
        for (AnimationFrame frame : meta.frames) {
            this.frames[frame.getIndex()] = frame.getTime(meta.getDefaultFrameTime());
        }

        this.animation = new Animation(0, 1, this.frames[0], true, Easing.Linear.flat, anim -> {
            if (++this.currentFrame >= this.frames.length) {
                this.currentFrame = 0;
            }
            anim.setTicks(this.frames[this.currentFrame] - 0.5F);
        });
    }

    @Override
    public void render(@NotNull GuiGraphics gfx, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        this.animation.start();

        @SuppressWarnings("IntegerDivisionInFloatingPointContext")
        // we're relying on integer division as a cheap floor since this is always positive
        float u0 = (this.currentFrame / this.size.height()) / (float) this.size.width();
        float v0 = (this.currentFrame % this.size.height()) / (float) this.size.height();
        float u1 = u0 + textureWidth / (float) (textureWidth * this.size.width());
        float v1 = v0 + textureHeight / (float) (textureHeight * this.size.height());

        RenderSystem.enableBlend();

        GL.drawTexture(gfx, getTexture(), x, y, width, height, u0, v0, u1, v1);
    }
}
