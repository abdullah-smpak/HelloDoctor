package com.hellodoc.abdullah.hellodoc.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hellodoc.abdullah.hellodoc.Model.User;

public class Common {
    public static User currentUser;

    public static String convertCodestatus(String status) {
        if(status.equals("0"))
            return "Pending";
        else if(status.equals("1"))
            return "Accepted";
        else
            return "Completed";
    }

    public static final String USER_KEY="User";
    public static final String PWD_KEY="Password";

    public  static boolean isConnected(Context context)
    {
        ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager !=null)
        {
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            if(infos !=null)
            {
                for(int i=0;i<infos.length;i++)
                {

                if(infos[i].getState()==NetworkInfo.State.CONNECTED)
                 return  true;
                }
            }
        }
        return false;
    }

}


