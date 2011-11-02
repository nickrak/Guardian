package org.guardian;

import static org.bukkit.Bukkit.getWorld;
import static org.guardian.util.BukkitUtils.warning;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.PermissionDefault;
import org.guardian.tools.Tool;
import org.guardian.tools.ToolBehavior;
import org.guardian.tools.ToolMode;

public class Config
{
	public static int version;
	public static String bridge;
	public static boolean debug;
	public static boolean ninja;
	public static boolean werollback;
	public static Set<World> loggedWorlds;
	public static List<Tool> tools;
	public static Map<String, Tool> toolsByName;
	public static Map<Integer, Tool> toolsByType;

	static void load(Guardian guardian) {
		final FileConfiguration config = guardian.getConfig();
		config.options().copyDefaults(true);
		guardian.saveConfig();

		version = config.getInt("version");

		bridge = config.getString("bridge", "Guardian-MySQL-0.1-SNAPSHOT");
		if (!bridge.endsWith(".jar"))
			bridge += ".jar";
		debug = config.getBoolean("debug", false);
		ninja = config.getBoolean("ninja", false);

		for (final String worldName : toStringList(config.getStringList("loggedWorlds"))) {
			final World world = getWorld(worldName);
			if (world != null)
				loggedWorlds.add(world);
			else
				warning("There is no world called '" + worldName + "'");
		}

		final Set<String> toolNames = config.getConfigurationSection("tools").getKeys(false);
		tools = new ArrayList<Tool>();
		for (final String toolName : toolNames)
			try {
				final String path = "tools." + toolName;
				final List<String> aliases = toStringList(config.getList(path + ".aliases", null));
				final ToolBehavior leftClickBehavior = ToolBehavior.valueOf(config.getString(path + ".leftClickBehavior").toUpperCase());
				final ToolBehavior rightClickBehavior = ToolBehavior.valueOf(config.getString(path + ".rightClickBehavior").toUpperCase());
				final boolean defaultEnabled = config.getBoolean(path + ".defaultEnabled", false);
				final int item = config.getInt(path + ".item", 0);
				// TODO
				// final QueryParams params = new QueryParams();
				// params.prepareToolQuery = true;
				// params.parseArgs(Bukkit.getConsoleSender(), Arrays.asList(config.getString(path + ".params").split(" ")));
				final ToolMode mode = ToolMode.valueOf(config.getString(path + ".mode").toUpperCase());
				final PermissionDefault pdef = PermissionDefault.valueOf(config.getString(path + ".permissionDefault").toUpperCase());
				final boolean giveTool = config.getBoolean(path + ".giveTool", true);
				tools.add(new Tool(toolName, aliases, leftClickBehavior, rightClickBehavior, defaultEnabled, item, null, mode, pdef, giveTool));
			} catch (final Exception ex) {
				warning("Error at parsing tool '" + toolName + "':)", ex);
			}
		toolsByName = new HashMap<String, Tool>();
		toolsByType = new HashMap<Integer, Tool>();
		for (final Tool tool : tools) {
			toolsByType.put(tool.item, tool);
			toolsByName.put(tool.name, tool);
			for (final String alias : tool.aliases)
				toolsByName.put(alias, tool);
		}
	}

	public static List<String> toStringList(List<?> list) {
		if (list == null)
			return new ArrayList<String>();
		final List<String> strs = new ArrayList<String>(list.size());
		for (final Object obj : list)
			if (obj instanceof String)
				strs.add((String)obj);
			else
				strs.add(String.valueOf(obj));
		return strs;
	}
}
