package org.guardian.tools;

import org.bukkit.entity.Player;
import org.guardian.params.QueryParams;

public class SessionToolData
{
	private boolean enabled;
	private ToolMode mode;
	private QueryParams params;

	public SessionToolData(Tool tool, Player player) {
		enabled = tool.defaultEnabled && player.hasPermission("guardian.tools." + tool.name);
		params = tool.params.clone();
		mode = tool.mode;
	}

	public ToolMode getMode() {
		return mode;
	}

	public QueryParams getParams() {
		return params;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setMode(ToolMode mode) {
		this.mode = mode;
	}

	public void setParams(QueryParams params) {
		this.params = params;
	}
}
