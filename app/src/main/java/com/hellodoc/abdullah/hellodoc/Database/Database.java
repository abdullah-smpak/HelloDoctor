package com.hellodoc.abdullah.hellodoc.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.v7.app.AppCompatActivity;

import com.hellodoc.abdullah.hellodoc.Model.Appoint;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    public static final  String DB_NAME="HelloDocDB";
    private  static final int DB_VER=1;

    public Database(Context context) {

        super(context, DB_NAME,null,DB_VER);
    }

    public List<Appoint> getDocs()    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect={"P_id","P_name","P_Age","P_gender","P_phone","P_dayntime"};
        String sqlTable="ApointDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Appoint> result = new ArrayList<>();
        if (c.moveToFirst())
        {
            do
            {
                result.add(new Appoint(c.getString(c.getColumnIndex("P_id")),
                        c.getString(c.getColumnIndex("P_name")),
                        c.getString(c.getColumnIndex("P_Age")),
                        c.getString(c.getColumnIndex("P_gender")),
                        c.getString(c.getColumnIndex("P_phone")),
                        c.getString(c.getColumnIndex("P_dayntime"))

                ));
            }while (c.moveToFirst());

        }
        return  result;

    }

    public void  addAppoint(Appoint appoint)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO ApointDetail(P_id,P_name,P_Age,P_gender,P_phone,P_dayntime) VALUES('%s','%s','%s','%s','%s','%s');",
                appoint.getP_id(),
                appoint.getP_name(),
                appoint.getP_age(),
                appoint.getP_gender(),
                appoint.getP_phone(),
                appoint.getP_dayntime());
        db.execSQL(query);
    }

    public void  cleanappoint()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM ApointDetail");
        db.execSQL(query);
    }

}
