package could.bluepay.renyumvvm.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bluepay on 2017/11/28.
 */

public class PrefsHelper {
    private static SharedPreferences sharedPreferences;
    private static PrefsHelper prefsInstance;

    private static String DEFAULTSTRING = "";
    private static long DEFAULTLONG = 0;

    private PrefsHelper(Context context, String preferencesName){
        sharedPreferences = context.getApplicationContext().
                getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }
    public static PrefsHelper with(Context context,String perferencesName){
        prefsInstance = new PrefsHelper(context,perferencesName);
        return prefsInstance;
    }

    //region========String=====
    public void write(String where,String what){
        sharedPreferences.edit().putString(where,what).apply();
    }

    public String read(String what,String defaultString){
        return sharedPreferences.getString(what,defaultString);
    }

    public String read(String what){
        return read(what,DEFAULTSTRING);
    }

    //endregion========String=====

    //region========long=========

    public void writeLong(String where,long what){
        sharedPreferences.edit().putLong(where,what).apply();
    }
    public long readLong(String where){
        return readLong(where,DEFAULTLONG);
    }
    public long readLong(String where,long defaultLong){
        return sharedPreferences.getLong(where,defaultLong);
    }
    //endregion========long=========

}
