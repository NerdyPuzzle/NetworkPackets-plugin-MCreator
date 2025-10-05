package net.nerdypuzzle.packets;

import net.mcreator.plugin.JavaPlugin;
import net.mcreator.plugin.Plugin;
import net.mcreator.plugin.events.PreGeneratorsLoadingEvent;
import net.mcreator.plugin.events.ui.BlocklyPanelRegisterJSObjects;
import net.nerdypuzzle.packets.parts.PluginElementTypes;
import net.nerdypuzzle.packets.parts.PluginJavascriptBridge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Launcher extends JavaPlugin {

	private static final Logger LOG = LogManager.getLogger("Network Packets");
    public static PluginJavascriptBridge pluginJavascriptBridge = null;

	public Launcher(Plugin plugin) {
		super(plugin);

		addListener(PreGeneratorsLoadingEvent.class, e -> PluginElementTypes.load());
        addListener(BlocklyPanelRegisterJSObjects.class, e -> {
            pluginJavascriptBridge = new PluginJavascriptBridge(e.getBlocklyPanel().getMCreator());
            e.getDOMWindow().put("packetbridge", pluginJavascriptBridge);
        });

		LOG.info("Network Packets plugin was loaded");
	}

}