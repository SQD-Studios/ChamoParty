package net.chamosmp.chamoparty.command.commands;

import net.chamosmp.chamoparty.ZVotePartyPlugin;
import net.chamosmp.chamoparty.api.VotePartyManager;
import net.chamosmp.chamoparty.api.enums.Message;
import net.chamosmp.chamoparty.api.enums.Permission;
import net.chamosmp.chamoparty.command.VCommand;
import net.chamosmp.chamoparty.zcore.utils.commands.CommandType;

public class CommandVote extends VCommand {

	public CommandVote(ZVotePartyPlugin plugin) {
		super(plugin);
		this.setDescription(Message.DESCRIPTION_HELP);
		this.setPermission(Permission.ZVOTEPARTY_VOTE);
		this.setConsoleCanUse(false);
	}

	@Override
	protected CommandType perform(ZVotePartyPlugin plugin) {

		this.manager.openVote(this.player);
		
		return CommandType.SUCCESS;
	}

}
