package net.chamosmp.chamoparty.paper.command;

import net.chamosmp.chamoparty.paper.ChamoPartyManager;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.strokkur.commands.Command;
import net.strokkur.commands.Executes;
import net.strokkur.commands.paper.Description;
import net.strokkur.commands.paper.Executor;
import net.strokkur.commands.permission.Permission;
import org.bukkit.entity.Player;

@Command("vote")
@Description("Open the vote gui")
public class Vote extends ChamoPartyManager {


    public Vote(ChamoPartyPlugin plugin) {
        super(plugin);
    }

    @Permission("chamoparty.vote")
    @Executes
    void onExecute(@Executor Player sender) {
        openVote(sender);
    }
}