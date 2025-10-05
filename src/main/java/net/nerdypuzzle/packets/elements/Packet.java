package net.nerdypuzzle.packets.elements;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.workspace.elements.ModElement;

public class Packet extends GeneratableElement {
    public Procedure serverBound;
    public Procedure clientBound;

    private Packet() {
        this(null);
    }

    public Packet(ModElement element) {
        super(element);
    }

}
