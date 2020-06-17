package com.amazon.shop2020.data;

import com.amazon.shop2020.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

public class LoginDataSource
{
    public Result<LoggedInUser> login(String username, final String password)
    {
        if(username.equals(""))
        {
            return new Result.Error(new IOException("Error logging in"));
        }
        try
        {
            LoggedInUser fakeUser = new LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe");
            return new Result.Success<>(fakeUser);

        }
        catch (Exception e)
        {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout()
    {
        // TODO: revoke authentication
    }
}