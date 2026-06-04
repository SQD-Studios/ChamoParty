package net.chamosmp.chamoparty.command.brigadier;


import net.chamosmp.chamoparty.ZVotePartyPlugin;
import net.chamosmp.chamoparty.command.VCommand;
import net.chamosmp.chamoparty.zcore.utils.MessageUtils;
import net.chamosmp.chamoparty.zcore.utils.commands.CommandType;
import net.strokkur.commands.Command;
import net.strokkur.commands.Executes;
import net.strokkur.commands.paper.Description;
import net.strokkur.commands.paper.Executor;
import net.strokkur.commands.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("vote")
@Description("Open the vote gui")
public class Vote extends VCommand {

    /**
     * @param plugin
     */
    public Vote(ZVotePartyPlugin plugin) {
        super(plugin);
    }

    @Permission("chamoparty.vote")
        @Executes
        void onExecute(@Executor Player sender) {
        this.manager.openVote(sender);

        }


    @Override
    protected CommandType perform(ZVotePartyPlugin plugin) {
        return null;
    }
}


