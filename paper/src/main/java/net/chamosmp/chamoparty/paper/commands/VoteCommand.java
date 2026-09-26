package net.chamosmp.chamoparty.paper.commands;

import net.chamosmp.chamoparty.paper.ChamoPartyManager;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.strokkur.commands.Command;
import net.strokkur.commands.Executes;
import net.strokkur.commands.paper.Executor;
import net.strokkur.commands.permission.Permission;
import org.bukkit.entity.Player;

@Command("vote")
public class VoteCommand extends ChamoPartyManager {

    public VoteCommand(ChamoPartyPlugin plugin) {
        super(plugin);
    }

    @Permission("chamoparty.vote")
    @Executes
    public void onExecute(@Executor Player sender) {
        openVote(sender);
    }
}