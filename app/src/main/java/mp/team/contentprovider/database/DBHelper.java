package mp.team.contentprovider.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dragos on 12.05.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "citate.db";
    public static final String TABLE_QUOTES = "quotes";
    public static final String COL_1 = "_id";
    public static final String COL_2 = "citat";
    public static final String COL_3 = "data_creare";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_QUOTES + "(" +
                COL_1 + " integer not null primary key autoincrement, " +
                COL_2 + " text not null, " +
                COL_3 + " text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUOTES);
        onCreate(db);
    }

}
