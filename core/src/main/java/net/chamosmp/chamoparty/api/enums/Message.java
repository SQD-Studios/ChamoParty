package net.chamosmp.chamoparty.api.enums;

import net.chamosmp.chamoparty.core.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Message {

    PREFIX(ColorUtils.parse("<aqua>ChamoParty</aqua>| ")),

    INVENTORY_CLONE_NULL(ColorUtils.parse("<red>The inventory clone is null!")),
    INVENTORY_OPEN_ERROR(ColorUtils.parse("<red>An error occurred with the opening of the inventory <gold>%id%<red>.")),
    INVENTORY_NULL(ColorUtils.parse("<red>Impossible to find the inventory with the id <gold>%id%<red>.")),
    INVENTORY_BUTTON_PREVIOUS(ColorUtils.parse("<white>§ <gray>Previous page")),
    INVENTORY_BUTTON_NEXT(ColorUtils.parse("<white>§ <gray>Next page")),

    RELOAD_SUCCESS(ColorUtils.parse("<green>You have just reloaded the configuration.")),
    RELOAD_ERROR(ColorUtils.parse("<red>An error has occurred, go to the console.")),


    VOTE_INFORMATIONS(MessageType.CENTER,
            ColorUtils.parse("""
                    <dark_gray><st>-+------------------------------+-
                    
                    "<gray>Vote for the server <dark_purple>Server Name<gray> !
                    
                    "<dark_gray><st>-+------------------------------+-
                    """)),

    VOTE_BROADCAST_ACTION(MessageType.ACTION, ColorUtils.parse("<white>%player% <gray>has just voted <dark_gray>(<aqua>%chamoparty_votes_recorded%<gray>/<green>%chamoparty_votes_required_total%<dark_gray>)")),
    VOTE_BROADCAST_TCHAT(ColorUtils.parse("<white>%player% <gray>has just voted <dark_gray>(<aqua>%chamoparty_votes_recorded%<gray>/<green>%chamoparty_votes_required_total%<dark_gray>)")),
    VOTE_MESSAGE(ColorUtils.parse("<gray>You have just voted for the server <dark_purple>Server name<gray>.")),
    VOTE_LATER(ColorUtils.parse("<gray>You have just received <aqua>%amount% <gray>votes.")),
    VOTE_SEND(ColorUtils.parse("<gray>You just gave a vote to <white>%player%<gray>")),
    VOTE_REMOVE_SUCCESS(ColorUtils.parse("<green>You have just removed a yours from the <white>%player%<green>.")),
    VOTE_REMOVE_ERROR(ColorUtils.parse("<red>Impossible to remove a vote from the <white>%player%<red>, the player has no vote.")),
    VOTE_NEEDED(
            ColorUtils.parse("""
                                <aqua>%chamoparty_votes_required_party% <white>votes <gray>needed for the next party !
                                <gray>Vote<dark_gray>: <white>(Your Vote Link)
                    """)),

    VOTE_PARTY_START(MessageType.CENTER,
            ColorUtils.parse("""
                                 <dark_gray><st>-+------------------------------+-
                    
                                 <gray>Launch of the voting party!
                    
                                 <dark_gray><st>-+------------------------------+-
                    """)

    ),
    NOT_ELIGIBLE_PARTY(MessageType.CENTER,
            ColorUtils.parse("""
                                <dark_gray><st>-+------------------------------+-
                    
                                <red>You didn’t vote, so you aren’t eligible for the Vote Party rewards!
                    
                                <dark_gray><st>-+------------------------------+-
                    """)),
    VOTE_STARTPARTY(ColorUtils.parse("<green>You just launched the voting party.")),

    TIME_DAY(ColorUtils.parse("%02d days(s) %02d hour(s) %02d minute(s) %02d second(s)")),
    TIME_HOUR(ColorUtils.parse("%02d hours(s) %02d minute(s) %02d second(s)")),
    TIME_HOUR_SIMPLE(ColorUtils.parse("%02d:%02d:%02d")),
    TIME_MINUTE(ColorUtils.parse("%02d minute(s) %02d second(s)")),
    TIME_SECOND(ColorUtils.parse("%02d second(s)")),

    COMMAND_SYNTAX_ERROR(ColorUtils.parse("<red>You must execute the command like this<gray>: <green>%syntax%")),
    COMMAND_NO_PERMISSION(ColorUtils.parse("<red>You do not have permission to run this command.")),
    COMMAND_NO_CONSOLE(ColorUtils.parse("<red>Only one player can execute this command.")),
    COMMAND_NO_ARG(ColorUtils.parse("<red>Impossible to find the command with its arguments.")),
    COMMAND_SYNTAX_HELP(ColorUtils.parse("<green>%syntax% <aqua>§ <gray>%description%")),

    DESCRIPTION_VERSION(ColorUtils.parse("Show plugin version")),
    DESCRIPTION_RELOAD(ColorUtils.parse("Reload configurations")),
    DESCRIPTION_CONFIG(ColorUtils.parse("Change configuration")),
    DESCRIPTION_ADD(ColorUtils.parse("Add a vote to a player.")),
    DESCRIPTION_REMOVE(ColorUtils.parse("Remove a vote to a player.")),
    DESCRIPTION_STARTPARTY(ColorUtils.parse("Force launch a Vote Party")),
    DESCRIPTION_HELP(ColorUtils.parse("Show commands")),
    DESCRIPTION_VOTE(ColorUtils.parse("Allows you to open the voting inventory"));


    private List<Component> messages;
    private Component message;
    private Map<String, Object> titles = new HashMap<>();
    private final boolean use;
    private MessageType type = MessageType.TCHAT;

    /**
     *
     * @param message
     */
    Message(Component message) {
        this.message = message;
        this.use = true;
    }

    /**
     *
     * @param message
     */
    Message(MessageType type, Component message) {
        this.message = message;
        this.use = true;
        this.type = type;
    }

    public Component getMessage() {
        return message;
    }


    /**
     * @apiNote You should use {@link Message#getMessage()}
     */
    @ApiStatus.Obsolete
    public Component msg() {
        return message;
    }

    public boolean isUse() {
        return use;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public List<Component> getMessages() {
        return messages == null ? Collections.singletonList(message) : messages;
    }

    public void setMessages(List<Component> messages) {
        this.messages = messages;
    }

    public boolean isMessage() {
        return messages != null && messages.size() > 1;
    }

    public String getTitle() {
        return (String) titles.get("title");
    }

    public Map<String, Object> getTitles() {
        return titles;
    }

    public void setTitles(Map<String, Object> titles) {
        this.titles = titles;
    }

    public String getSubTitle() {
        return (String) titles.get("subtitle");
    }

    public int getStart() {
        return ((Number) titles.get("start")).intValue();
    }

    public int getEnd() {
        return ((Number) titles.get("end")).intValue();
    }

    public int getTime() {
        return ((Number) titles.get("time")).intValue();
    }

    public Component replace(String a, String b) {
        return message.replaceText(TextReplacementConfig.builder().replacement(a).replacement(b).build());
    }

    public MessageType getType() {
        return type.equals(MessageType.ACTION) ? MessageType.TCHAT : type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getString() {
        return ColorUtils.deParse(message);
    }

    public List<String> getStrings() {
        return messages == null ?
                Collections.singletonList(ColorUtils.deParse(message))
                :
                ColorUtils.deParse(messages)
                ;
    }

}

