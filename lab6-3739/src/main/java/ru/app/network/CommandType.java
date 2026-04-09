package ru.app.network;

/**
 * Enum representing available command types.
 *
 * <p>These commands are sent from client to server for processing.
 *
 * @author Lab6
 * @version 1.0
 */
public enum CommandType {
  HELP,
  INFO,
  SHOW,
  ADD,
  UPDATE,
  REMOVE_BY_ID,
  CLEAR,
  SAVE,
  EXECUTE_SCRIPT,
  EXIT,
  REMOVE_FIRST,
  ADD_IF_MIN,
  HISTORY,
  SUM_OF_PRICE,
  AVERAGE_OF_PRICE,
  FILTER_BY_UNIT_OF_MEASURE
}
