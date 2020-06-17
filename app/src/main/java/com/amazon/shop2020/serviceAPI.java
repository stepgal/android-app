package com.amazon.shop2020;

public class serviceAPI
{
    private static serviceAPI instance;

    public static synchronized serviceAPI getInstance() {
        if (instance == null) {
            instance = new serviceAPI();
        }
        return instance;
    }
    public String getAuthHost() {
        return "http://18.222.219.162:3001";
    } // "http://192.168.1.5:3001/user/:email/:password"
    public String getItemsHost() {
        return "http://3.15.18.44:3002";
    } // "http://192.168.1.5:3002/items/all"
    public String getSearchHost() {
        return "http://3.22.74.194:3003";
    } // "http://192.168.1.5:3003/items/search?q=" + charString;

    public String getLoginURL(String email, String password) {
        return getAuthHost()  + "/user/" + email + "/" + password;
    }
    public String getAllItems() {
        return getItemsHost()  + "/items/all";
    }
    public String searchItems(String query) {
        return getSearchHost() + "/items/search?q=" + query;
    }

}
