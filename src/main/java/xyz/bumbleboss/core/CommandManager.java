package xyz.bumbleboss.core;

import com.jockie.bot.core.command.ICommand;
import com.jockie.bot.core.command.impl.CommandEvent;
import com.jockie.bot.core.command.impl.CommandEventListener;

public class CommandManager extends CommandEventListener {

  @Override
  public void onCommandExecutionException(ICommand command, CommandEvent event, Throwable throwable) {
    Util.respond(event, String.format("Otto, something happened!\n```Java\n%s```", throwable.getMessage()));
    Util.postError(command, throwable);
  }
}