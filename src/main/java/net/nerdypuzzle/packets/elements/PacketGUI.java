package net.nerdypuzzle.packets.elements;

import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.laf.themes.Theme;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.workspace.elements.ModElement;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

public class PacketGUI extends ModElementGUI<Packet> {
    private ProcedureSelector serverBound;
    private ProcedureSelector clientBound;

    public PacketGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        this.initGUI();
        super.finalizeGUI();
    }

    protected void initGUI() {
        serverBound = new ProcedureSelector(this.withEntry("packet/serverbound"), mcreator,
                L10N.t("elementgui.packet.serverbound"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/inboundString:string"));
        clientBound = new ProcedureSelector(this.withEntry("packet/clientbound"), mcreator,
                L10N.t("elementgui.packet.clientbound"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/inboundString:string"));

        JPanel procedurepanel = new JPanel(new GridLayout(1, 2, 10, 2));
        procedurepanel.setOpaque(false);
        procedurepanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.current().getForegroundColor(), 1),
                L10N.t("elementgui.packet.procedures_border"), 0, 0, getFont().deriveFont(12.0f),
                Theme.current().getForegroundColor()));
        procedurepanel.add(serverBound);
        procedurepanel.add(clientBound);

        addPage(PanelUtils.totalCenterInPanel(procedurepanel)).lazyValidate(() -> {
            if (serverBound.getSelectedProcedure() == null && clientBound.getSelectedProcedure() == null)
                return new AggregatedValidationResult.FAIL(L10N.t("elementgui.packet.no_procedure", new Object[0]));
            return new AggregatedValidationResult.PASS();
        });
    }

    public void reloadDataLists() {
        super.reloadDataLists();
        serverBound.refreshListKeepSelected();
        clientBound.refreshListKeepSelected();
    }

    public void openInEditingMode(Packet packet) {
        serverBound.setSelectedProcedure(packet.serverBound);
        clientBound.setSelectedProcedure(packet.clientBound);
    }

    public Packet getElementFromGUI() {
        Packet packet = new Packet(this.modElement);
        packet.serverBound = serverBound.getSelectedProcedure();
        packet.clientBound = clientBound.getSelectedProcedure();
        return packet;
    }

    @Override public URI contextURL() throws URISyntaxException {
        return null;
    }

}
