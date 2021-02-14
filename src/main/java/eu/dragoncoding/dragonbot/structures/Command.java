package eu.dragoncoding.dragonbot.structures;

import net.dv8tion.jda.api.entities.Message;

public interface Command {
    void performCommand(Message message,  int subString);
}