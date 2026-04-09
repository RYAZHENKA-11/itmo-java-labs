package ru.app.server;

import ru.app.command.AbstractCommand;
import ru.app.command.AddCommand;
import ru.app.command.AddIfMinCommand;
import ru.app.command.AverageOfPriceCommand;
import ru.app.command.ClearCommand;
import ru.app.command.ExecuteScriptCommand;
import ru.app.command.ExitCommand;
import ru.app.command.FilterByUnitOfMeasureCommand;
import ru.app.command.HelpCommand;
import ru.app.command.HistoryCommand;
import ru.app.command.InfoCommand;
import ru.app.command.RemoveByIdCommand;
import ru.app.command.RemoveFirstCommand;
import ru.app.command.SaveCommand;
import ru.app.command.ShowCommand;
import ru.app.command.SumOfPriceCommand;
import ru.app.command.UpdateCommand;
import ru.app.network.CommandType;

/**
 * Factory for creating command objects based on command type.
 *
 * <p>Maps {@link CommandType} enum values to corresponding command implementations.
 *
 * @author Lab6
 * @version 1.0
 */
public class CommandFactory {

  /**
   * Creates a command based on command type.
   *
   * @param type command type
   * @return command instance
   */
  public AbstractCommand create(CommandType type) {
    return switch (type) {
      case HELP -> new HelpCommand();
      case INFO -> new InfoCommand();
      case SHOW -> new ShowCommand();
      case ADD -> new AddCommand();
      case UPDATE -> new UpdateCommand();
      case REMOVE_BY_ID -> new RemoveByIdCommand();
      case CLEAR -> new ClearCommand();
      case SAVE -> new SaveCommand();
      case EXIT -> new ExitCommand();
      case REMOVE_FIRST -> new RemoveFirstCommand();
      case ADD_IF_MIN -> new AddIfMinCommand();
      case HISTORY -> new HistoryCommand();
      case SUM_OF_PRICE -> new SumOfPriceCommand();
      case AVERAGE_OF_PRICE -> new AverageOfPriceCommand();
      case FILTER_BY_UNIT_OF_MEASURE -> new FilterByUnitOfMeasureCommand();
      case EXECUTE_SCRIPT -> new ExecuteScriptCommand();
    };
  }
}
