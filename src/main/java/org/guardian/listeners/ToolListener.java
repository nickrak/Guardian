package org.guardian.listeners;

import static org.guardian.Config.loggedWorlds;
import static org.guardian.Config.toolsByType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.guardian.SessionManager;
import org.guardian.params.QueryParams;
import org.guardian.tools.SessionToolData;
import org.guardian.tools.Tool;
import org.guardian.tools.ToolBehavior;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

public class ToolListener extends PlayerListener
{
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!event.isCancelled() && event.getMaterial() != null) {
			final int type = event.getMaterial().getId();
			final Tool tool = toolsByType.get(type);
			if (tool != null) {
				final Action action = event.getAction();
				final Player player = event.getPlayer();
				if ((action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) && loggedWorlds.contains(player.getWorld()) && player.hasPermission("guardian.tools." + tool.name)) {
					final ToolBehavior behavior = action == Action.RIGHT_CLICK_BLOCK ? tool.rightClickBehavior : tool.leftClickBehavior;
					final SessionToolData toolData = SessionManager.getSession(player).getToolDatas().get(tool);
					if (behavior != ToolBehavior.NONE && toolData.isEnabled()) {
						final Block block = event.getClickedBlock();
						final QueryParams params = toolData.getParams();
						params.setLocation(null);
						params.setSelection(null);
						if (behavior == ToolBehavior.BLOCK)
							params.setLocation(block.getRelative(event.getBlockFace()).getLocation());
						else if (block.getTypeId() != 54 || tool.params.getRadius() != 0)
							params.setLocation(block.getLocation());
						else {
							for (final BlockFace face : new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST})
								if (block.getRelative(face).getTypeId() == 54)
									params.setSelection(new CuboidSelection(event.getPlayer().getWorld(), block.getLocation(), block.getRelative(face).getLocation()));
							if (params.getSelection() == null)
								params.setLocation(block.getLocation());
						}

						// TODO Execute command
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
