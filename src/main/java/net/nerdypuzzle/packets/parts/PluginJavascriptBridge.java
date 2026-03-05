package net.nerdypuzzle.packets.parts;

import net.mcreator.minecraft.DataListEntry;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.dialogs.DataListSelectorDialog;
import net.mcreator.ui.init.L10N;
import net.mcreator.workspace.Workspace;
import net.mcreator.workspace.elements.ModElement;
import net.nerdypuzzle.packets.elements.Packet;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PluginJavascriptBridge {
    private final MCreator mcreator;

    public PluginJavascriptBridge(MCreator mcreator) {
        this.mcreator = mcreator;
    }

    @SuppressWarnings("unused") public void openDataListEntrySelector(String type, Consumer<String[]> callback) {
        SwingUtilities.invokeLater(() -> {
            System.out.println(loadServersidePackets(mcreator.getWorkspace()));
            String[] retval = new String[] { "", L10N.t("blockly.extension.data_list_selector.no_entry") };
            DataListEntry selected = DataListSelectorDialog.openSelectorDialog(mcreator, type.equals("server")
                            ? PluginJavascriptBridge::loadServersidePackets
                            : PluginJavascriptBridge::loadClientsidePackets,
                    L10N.t("dialog.selector.title"), L10N.t("dialog.selector." + type + ".message"));
            if (selected != null) {
                retval[0] = selected.getName();
                retval[1] = selected.getReadableName();
            }
            callback.accept(retval);
        });
    }

    private static List<DataListEntry> loadServersidePackets(Workspace workspace) {
        List<DataListEntry> retval = getCustomElements(workspace,
                mu -> {
                    boolean isServerside = false;
                    if (mu.getType() == PluginElementTypes.PACKET) {
                        Packet packet = (Packet) mu.getGeneratableElement();
                        if (packet.serverBound != null)
                            isServerside = true;
                    }
                    return isServerside;
                });
        retval.sort(DataListEntry.getComparator(workspace, retval));
        return retval;
    }

    private static List<DataListEntry> loadClientsidePackets(Workspace workspace) {
        List<DataListEntry> retval = getCustomElements(workspace,
                mu -> {
                    boolean isClientside = false;
                    if (mu.getType() == PluginElementTypes.PACKET) {
                        Packet packet = (Packet) mu.getGeneratableElement();
                        if (packet.clientBound != null)
                            isClientside = true;
                    }
                    return isClientside;
                });
        retval.sort(DataListEntry.getComparator(workspace, retval));
        return retval;
    }

    private static List<DataListEntry> getCustomElements(@Nonnull Workspace workspace,
                                                         Predicate<ModElement> predicate) {
        return workspace.getModElements().stream().filter(predicate).map(DataListEntry.Custom::new)
                .collect(Collectors.toList());
    }

}