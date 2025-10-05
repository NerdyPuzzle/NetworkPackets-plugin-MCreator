package net.nerdypuzzle.packets.parts;

import net.mcreator.element.ModElementType;
import net.nerdypuzzle.packets.elements.Packet;
import net.nerdypuzzle.packets.elements.PacketGUI;

import static net.mcreator.element.ModElementTypeLoader.register;

public class PluginElementTypes {
    public static ModElementType<?> PACKET;

    public static void load() {

        PACKET = register(
                new ModElementType<>("packet", (Character) 'p', PacketGUI::new, Packet.class)
        );

    }

}
