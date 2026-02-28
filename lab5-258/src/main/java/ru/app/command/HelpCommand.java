package ru.app.command;

import ru.app.collection.Collection;
import java.io.PrintWriter;

/** Команда вывода справки по доступным командам. */
public class HelpCommand extends AbstractCommand {

  public HelpCommand(Collection collection, PrintWriter out) {
    super(collection, out);
  }

  @Override
  public String getName() {
    return "help";
  }

  @Override
  public void execute() {
    println("Available commands:");
    println("help : output help");
    println("info : information about the collection");
    println("show : display all elements");
    println("add : add a new element");
    println("update id : update the element with the specified id");
    println("remove_by_id id : delete the element by id");
    println("clear : clear the collection");
    println("save : save the collection to a file");
    println("execute_script file_name : execute the script from the file");
    println("exit : exit the program");
    println("remove_first : remove the first element");
    println("add_if_min : add the element if it is less than the minimum");
    println("history : last 13 commands");
    println("sum_of_price : sum of prices");
    println("average_of_price : average price");
    println(
        "filter_by_unit_of_measure unitOfMeasure : output elements with the specified unit of measurement");
  }
}
