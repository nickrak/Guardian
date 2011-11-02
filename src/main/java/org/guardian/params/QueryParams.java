package org.guardian.params;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.guardian.ActionType;
import com.sk89q.worldedit.bukkit.selections.Selection;

/**
 * @author DiddiZ
 */
public class QueryParams implements Cloneable
{
	private List<ActionType> actions;
	private long before, since;
	private List<Integer> blocks;
	private boolean excludePlayersMode;
	private int limit;
	private Location loc;
	private Order order;
	private List<String> players;
	private int radius;
	private Selection sel;
	private boolean silent;
	private SummarizationMode sum;
	private String textMatch;
	private List<World> worlds;
	@SuppressWarnings("unused")
	private boolean needId, needDate, needPlayer, needCoords, needSubTableInfo;

	/**
	 * Constructs a new QueryParams with default values
	 */
	QueryParams() {
		// TODO Set default values
	}

	public List<ActionType> getActions() {
		return actions;
	}

	public long getBefore() {
		return before;
	}

	public List<Integer> getBlocks() {
		return blocks;
	}

	public int getLimit() {
		return limit;
	}

	public Location getLocation() {
		return loc;
	}

	public Order getOrder() {
		return order;
	}

	public List<String> getPlayers() {
		return players;
	}

	public int getRadius() {
		return radius;
	}

	public Selection getSelection() {
		return sel;
	}

	public long getSince() {
		return since;
	}

	public SummarizationMode getSummarizationMode() {
		return sum;
	}

	public String getTextMatch() {
		return textMatch;
	}

	public List<World> getWorlds() {
		return worlds;
	}

	public boolean isExcludePlayersMode() {
		return excludePlayersMode;
	}

	public boolean isNeedCoords() {
		return needCoords;
	}

	public boolean isSilent() {
		return silent;
	}

	public void setActions(List<ActionType> actions) {
		this.actions = actions;
	}

	public void setBefore(long before) {
		this.before = before;
	}

	public void setBlocks(List<Integer> blocks) {
		this.blocks = blocks;
	}

	public void setExcludePlayersMode(boolean excludePlayersMode) {
		this.excludePlayersMode = excludePlayersMode;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setLocation(Location loc) {
		this.loc = loc;
	}

	public void setNeedCoords(boolean needCoords) {
		this.needCoords = needCoords;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public void setSelection(Selection sel) {
		this.sel = sel;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	public void setSince(long since) {
		this.since = since;
	}

	public void setSummarizationMode(SummarizationMode sum) {
		this.sum = sum;
	}

	public void setTextMatch(String textMatch) {
		this.textMatch = textMatch;
	}

	public void setWorlds(List<World> worlds) {
		this.worlds = worlds;
	}

	@Override
	public QueryParams clone() {
		// TODO
		return null;
	}
}
