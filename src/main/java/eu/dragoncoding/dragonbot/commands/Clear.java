package eu.dragoncoding.dragonbot.commands;

import eu.dragoncoding.dragonbot.structures.Command;
import eu.dragoncoding.dragonbot.utils.ChatUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;
import java.util.List;

public class Clear implements Command {

    @Override
    public void performCommand(Message message, int subString) {
        int errorCode,
            amount = 0;

        //noinspection ConstantConditions
        if(message.getMember().hasPermission(message.getTextChannel(), Permission.MESSAGE_MANAGE)) {
            String[] args = message.getContentDisplay().substring(subString).split(" ");

            if(args.length > 1 ) {
                try {
                    amount = Integer.parseInt(args[1]);

                    message.getChannel().purgeMessages(get(message.getChannel(), amount));
                    errorCode = 0;

                } catch (NumberFormatException e) {
                    errorCode = 3;
                }
            } else {
                errorCode = 2;
            }
        } else {
            errorCode = 1;
        }

        ArrayList<String> responseMessages = new ArrayList<>();
        responseMessages.add("Purged " + amount + (amount == 1 ? " message!" : " messages!"));
        responseMessages.add("Missing Permission! (Manage Messages)");
        responseMessages.add("Invalid argument length. See '-help clear' for more information.");
        responseMessages.add("The amount of messages to delete is invalid. Pleas only use numbers.");

        ChatUtils.sendResponseToDiscord(errorCode, message.getTextChannel(), responseMessages);
    }

    public List<Message> get(MessageChannel channel, int amount) {
        List<Message> messages = new ArrayList<>();
        int i = amount + 1;

        for(Message message : channel.getIterableHistory().cache(false)) {

            if(!message.isPinned()) {
                messages.add(message);
            }
            --i;
            if(i <= 0) break;
        }

        return messages;
    }

}
