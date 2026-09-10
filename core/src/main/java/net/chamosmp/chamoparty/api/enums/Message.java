package net.chamosmp.chamoparty.api.enums;

import net.chamosmp.chamoparty.core.utils.ColorUtils;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;

public enum Message {
    PREFIX("<aqua>chamoParty</aqua>| "),

    RELOAD_SUCCESS("<green>You have just reloaded the configuration."),
    RELOAD_ERROR("<red>An error has occurred, go to the console."),

    VOTE_BROADCAST_ACTION(MessageType.ACTION, "<white>%player% <gray>has just voted <dark_gray>(<aqua>%chamoparty_votes_recorded%<gray>/<green>%chamoparty_votes_required_total%<dark_gray>)"),
    VOTE_BROADCAST_TCHAT("<white>%player% <gray>has just voted <dark_gray>(<aqua>%chamoparty_votes_recorded%<gray>/<green>%chamoparty_votes_required_total%<dark_gray>)"),
    VOTE_MESSAGE("<gray>You have just voted for the server <dark_purple>Server name<gray>."),
    VOTE_LATER("<gray>You have just received <aqua>%amount% <gray>votes."),
    VOTE_SEND("<gray>You just gave a vote to <white>%player%<gray>"),
    VOTE_REMOVE_SUCCESS("<green>You have just removed a yours from the <white>%player%<green>."),
    VOTE_REMOVE_ERROR("<red>Impossible to remove a vote from the <white>%player%<red>, the player has no vote."),
    VOTE_NEEDED("<aqua>%chamoparty_votes_required_party% <white>votes <gray>needed for the next party! <gray>Vote<dark_gray> <white><u><click:open_url:https://minecraft-server-list.com/server/serverid>here"),

    VOTE_PARTY_START("A new Vote Party started!"),
    NOT_ELIGIBLE_PARTY("<red>You didn’t vote, so you didn't earn any rewards"),
    VOTE_STARTPARTY("<green>You just launched the voting party.");

    private Component message;
    private Map<String, Object> titles = new HashMap<>();
    private final boolean use;
    private MessageType type = MessageType.TCHAT;

    /**
     *
     * @param message
     */
    Message(String message) {
        this.message = ColorUtils.parse("<white>" + message);
        this.use = true;
    }

    /**
     *
     * @param message
     */
    Message(MessageType type, String message) {
        this.message = ColorUtils.parse("<white>" + message);
        this.use = true;
        this.type = type;
    }

    public Component getMessage() {
        return message;
    }

    public boolean isUse() {
        return use;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public String getTitle() {
        return (String) titles.get("title");
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

    public MessageType getType() {
        return type.equals(MessageType.ACTION) ? MessageType.TCHAT : type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getString() {
        return ColorUtils.deParse(message);
    }

}

