package me.semx11.eeeee;

import static me.semx11.eeeee.util.ReflectionUtil.findField;
import static me.semx11.eeeee.util.ReflectionUtil.findMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import me.semx11.eeeee.command.CommandNextType;
import me.semx11.eeeee.render.CustomFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.command.CommandBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Eeeee.MODID, version = Eeeee.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8, 1.8.9]")
public class Eeeee {

    public static final String MODID = "eeeee";
    public static final String VERSION = "1.0";

    @SuppressWarnings("deprecation")
    private static void registerEvents(Object... events) {
        Arrays.asList(events).forEach(event -> {
            MinecraftForge.EVENT_BUS.register(event);
            FMLCommonHandler.instance().bus().register(event);
        });
    }

    private static void registerCommands(CommandBase... commands) {
        Arrays.asList(commands).forEach(ClientCommandHandler.instance::registerCommand);
    }

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void init(FMLInitializationEvent event) {
        registerCommands(
                CommandNextType.getInstance()
        );
    }

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void postInit(FMLPostInitializationEvent event) {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            mc.fontRendererObj = new CustomFontRenderer(
                    mc.gameSettings,
                    new ResourceLocation("textures/font/ascii.png"),
                    mc.renderEngine,
                    false);

            if (mc.gameSettings.language != null) {
                mc.fontRendererObj.setUnicodeFlag(mc.isUnicode());
                Object langManager = findField(Minecraft.class, "field_135017_as",
                        "mcLanguageManager").get(mc);
                boolean bidi = (boolean) findMethod(LanguageManager.class, new String[]{
                        "func_135044_b", "isCurrentLanguageBidirectional"}).invoke(langManager);
                mc.fontRendererObj.setBidiFlag(bidi);
            }

            Object resManager = findField(Minecraft.class, "field_110451_am",
                    "mcResourceManager").get(mc);
            findMethod(IReloadableResourceManager.class, new String[]{
                            "func_110542_a", "registerReloadListener"},
                    IResourceManagerReloadListener.class).invoke(resManager, mc.fontRendererObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
