package me.semx11.eeeee.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class CustomFontRenderer extends FontRenderer {

    public static EnumRenderType renderType = EnumRenderType.EEE;

    public CustomFontRenderer(GameSettings settings, ResourceLocation location,
            TextureManager manager, boolean unicode) {
        super(settings, location, manager, unicode);
    }

    @Override
    public int drawString(String text, float x, float y, int color, boolean dropShadow) {
        text = applyFunction(text);
        return super.drawString(text, x, y, color, dropShadow);
    }

    @Override
    public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor) {
        str = applyFunction(str);
        super.drawSplitString(str, x, y, wrapWidth, textColor);
    }

    @Override
    public int getStringWidth(String text) {
        text = applyFunction(text);
        return super.getStringWidth(text);
    }

    private static String applyFunction(String in) {
        if (renderType.hasFunction()) {
            return renderType.apply(in);
        }
        return in;
    }

    public static void setRenderType(EnumRenderType renderType) {
        CustomFontRenderer.renderType = renderType;
    }

    public static EnumRenderType getRenderType() {
        return renderType;
    }

    public static void nextRenderType() {
        CustomFontRenderer.renderType = renderType.next();
    }

}
