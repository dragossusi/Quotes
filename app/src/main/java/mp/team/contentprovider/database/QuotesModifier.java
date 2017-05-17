package mp.team.contentprovider.database;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import mp.team.contentprovider.Quote;
import mp.team.contentprovider.provider.QuotesProvider;

import static mp.team.contentprovider.database.DBHelper.COL_1;
import static mp.team.contentprovider.database.DBHelper.COL_2;
import static mp.team.contentprovider.database.DBHelper.COL_3;

/**
 * Created by Dragos on 17.05.2017.
 */

public class QuotesModifier {


    public static DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

    public static void addQuote(Activity a, Quote q) {
        ContentValues values = new ContentValues();
        values.put(COL_2, q.text);
        values.put(COL_3, df.format(q.date));
        Uri uri = a.getContentResolver().insert(
                QuotesProvider.CONTENT_URI, values);
        System.out.println(uri);
    }

    public static int deleteQuote(Activity a, Quote q) {
        int count = a.getContentResolver().delete(
                QuotesProvider.CONTENT_URI, COL_1 + "=" + q.id, null
        );
        return count;
    }

    public static List<Quote> getQuotes(Activity a) throws ParseException {
        String[] columns = {COL_2, COL_3};
        //Cursor cursor = db.rawQuery("select citat,data_creare from quotes order by data_creare asc",null);
        String URL = "content://mp.team.contentprovider.provider.QuotesProvider/quotes";
        Uri friends = Uri.parse(URL);
        Cursor cursor = a.getContentResolver().query(friends, null, null, null, COL_3 + " desc");
        List<Quote> quotes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                System.out.println(cursor.getString(cursor.getColumnIndex(COL_2)));
                System.out.println(cursor.getString(cursor.getColumnIndex(COL_3)));

                quotes.add(new Quote(cursor.getInt(cursor.getColumnIndex(COL_1)),
                        cursor.getString(cursor.getColumnIndex(COL_2)),
                        df.parse(cursor.getString(cursor.getColumnIndex(COL_3)))));
            } while (cursor.moveToNext());
        }
        return quotes;
    }
}
