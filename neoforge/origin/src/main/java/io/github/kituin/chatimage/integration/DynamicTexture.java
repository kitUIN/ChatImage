// ONLY >= neoforge-1.21.5
package io.github.kituin.chatimage.integration;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.logging.LogUtils;

import java.io.IOException;
import java.nio.file.Path;

import #AbstractTexture#;
import net.minecraft.client.renderer.texture.Dumpable;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class DynamicTexture extends AbstractTexture implements Dumpable {
    private static final Logger LOGGER = LogUtils.getLogger();
    @javax.annotation.Nullable
    private NativeImage pixels;

    public DynamicTexture(NativeImage p_404902_) {
        this.pixels = p_404902_;
        if (!RenderSystem.isOnRenderThread()) {
            try {
                net.minecraft.client.Minecraft.getInstance().execute(() -> {
// IF >= neoforge-1.21.6
//                    this.texture = RenderSystem.getDevice().createTexture(
//                            (String) null,
//                            1,
//                            TextureFormat.RGBA8, this.pixels.getWidth(),
//                            this.pixels.getHeight(),
//                            1,
//                            1);
//                    this.textureView = RenderSystem.getDevice().createTextureView(this.texture);
// ELSE
//                     this.texture = RenderSystem.getDevice().createTexture((String) null, TextureFormat.RGBA8, this.pixels.getWidth(), this.pixels.getHeight(), 1);
// END IF
                    this.upload();
                });
            } catch (Exception e) {
                LOGGER.error("Failed to upload texture", e);
            }
        } else {
// IF >= neoforge-1.21.6
//            this.texture = RenderSystem.getDevice().createTexture(
//                    (String) null,
//                    1,
//                    TextureFormat.RGBA8, this.pixels.getWidth(),
//                    this.pixels.getHeight(),
//                    1,
//                    1);
//            this.textureView = RenderSystem.getDevice().createTextureView(this.texture);
// ELSE
//                     this.texture = RenderSystem.getDevice().createTexture((String) null, TextureFormat.RGBA8, this.pixels.getWidth(), this.pixels.getHeight(), 1);
// END IF
            this.upload();
        }
    }

    public void upload() {
        if (this.pixels != null && this.texture != null) {
            RenderSystem.getDevice().createCommandEncoder().writeToTexture(this.texture, this.pixels);
        } else {
            LOGGER.warn("Trying to upload disposed texture {}", this.getTexture().getLabel());
        }

    }

    @javax.annotation.Nullable
    public NativeImage getPixels() {
        return this.pixels;
    }

    public void setPixels(NativeImage p_117989_) {
        if (this.pixels != null) {
            this.pixels.close();
        }

        this.pixels = p_117989_;
    }

    public void close() {
        if (this.pixels != null) {
            this.pixels.close();
            this.pixels = null;
        }

        super.close();
    }

    public void dumpContents(ResourceLocation p_276119_, Path p_276105_) throws IOException {
        if (this.pixels != null) {
            String s = p_276119_.toDebugFileName() + ".png";
            Path path = p_276105_.resolve(s);
            this.pixels.writeToFile(path);
        }

    }
}