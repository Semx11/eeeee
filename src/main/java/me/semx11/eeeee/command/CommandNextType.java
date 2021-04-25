package me.semx11.eeeee.command;

import me.semx11.eeeee.render.CustomFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommandNextType extends CommandBase {

    private static final CommandNextType INSTANCE = new CommandNextType();

    private CommandNextType() {
    }

    public static CommandNextType getInstance() {
        return INSTANCE;
    }

    @Override
    public String getCommandName() {
        return "nexttype";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/nexttype";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        CustomFontRenderer.nextRenderType();
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                "\u00a7aChanged render type to " + CustomFontRenderer.getRenderType() + "!"));
    }

}
