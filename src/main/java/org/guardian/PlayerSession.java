package org.guardian;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.guardian.tools.SessionToolData;
import org.guardian.tools.Tool;

public class PlayerSession
{
	private final CommandSender sender;
	private final Map<Tool, SessionToolData> toolDatas;

	public PlayerSession(CommandSender sender) {
		this.sender = sender;
		toolDatas = new HashMap<Tool, SessionToolData>();
		if (sender instanceof Player)
			for (final Tool tool : Config.tools)
				toolDatas.put(tool, new SessionToolData(tool, (Player)sender));
	}

	public CommandSender getSender() {
		return sender;
	}

	public Map<Tool, SessionToolData> getToolDatas() {
		return toolDatas;
	}
}
