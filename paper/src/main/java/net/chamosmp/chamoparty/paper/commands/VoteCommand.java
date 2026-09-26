package net.chamosmp.chamoparty.paper.commands;

import net.chamosmp.chamoparty.paper.api.VotePartyManager;
import net.strokkur.commands.Command;
import net.strokkur.commands.Executes;
import net.strokkur.commands.paper.Executor;
import net.strokkur.commands.permission.Permission;
import org.bukkit.entity.Player;

@Command("vote")
public class VoteCommand {

    private final VotePartyManager manager;

    public VoteCommand(VotePartyManager manager) {
        this.manager = manager;
    }

    @Permission("chamoparty.vote")
    @Executes
    public void onExecute(@Executor Player sender) {
        manager.openVote(sender);
    }
}