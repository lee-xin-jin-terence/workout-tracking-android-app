package com.murdoch.fitnessapp.databases.exceptions;

/**
 * An exception that is thrown when a record fails to be inserted into
 * the (SQLite) database
 *
 * */
public class RecordInsertionException extends Exception
{
    public RecordInsertionException()
    {
        super();
    }

    public RecordInsertionException(String message)
    {
        super(message);
    }

    public RecordInsertionException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
