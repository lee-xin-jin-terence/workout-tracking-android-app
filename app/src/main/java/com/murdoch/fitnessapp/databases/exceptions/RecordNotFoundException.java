package com.murdoch.fitnessapp.databases.exceptions;

/**
 * An exception that is thrown when a record cannot be found in
 * the (SQLite) database
 *
 * */
public class RecordNotFoundException extends Exception
{
    public RecordNotFoundException()
    {
        super();
    }

    public RecordNotFoundException(String message)
    {
        super(message);
    }

    public RecordNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
