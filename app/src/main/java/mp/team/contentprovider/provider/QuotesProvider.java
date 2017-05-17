package mp.team.contentprovider.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

import mp.team.contentprovider.database.DBHelper;

import static mp.team.contentprovider.database.DBHelper.COL_1;
import static mp.team.contentprovider.database.DBHelper.COL_2;
import static mp.team.contentprovider.database.DBHelper.TABLE_QUOTES;

/**
 * Created by Dragos on 15.05.2017.
 */

public class QuotesProvider extends ContentProvider {

    static final String PROVIDER_NAME = "mp.team.contentprovider.provider.QuotesProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/quotes";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    static final UriMatcher uriMatcher;
    DBHelper helper;
    SQLiteDatabase database;
    static final int QUOTES = 1;
    static final int QUOTES_ID = 2;
    private static HashMap<String, String> QUOTES_PROJECTION_MAP;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "quotes", QUOTES);
        uriMatcher.addURI(PROVIDER_NAME, "quotes/#", QUOTES_ID);
    }

    @Override
    public boolean onCreate() {
        helper = new DBHelper(getContext());
        database = helper.getWritableDatabase();
        if (database == null)
            return false;
        else
            return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_QUOTES);
        switch (uriMatcher.match(uri)) {
            case QUOTES:
                qb.setProjectionMap(QUOTES_PROJECTION_MAP);
                break;
            case QUOTES_ID:
                qb.appendWhere(COL_1 + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            /**
             * By default sort on student names
             */
            sortOrder = COL_2;
        }

        Cursor c = qb.query(database, projection, selection,
                selectionArgs, null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case QUOTES:
                return "vnd.android.cursor.dir/vnd.example.quotes";
            case QUOTES_ID:
                return "vnd.android.cursor.item/vnd.example.quotes";
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long row = database.insert(TABLE_QUOTES, "", contentValues);
        // If record is added successfully
        if (row > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLException("Fail to add a new record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case QUOTES:
                // delete all the records of the table
                count = database.delete(TABLE_QUOTES, selection, selectionArgs);
                break;
            case QUOTES_ID:
                String id = uri.getLastPathSegment(); //gets the id
                count = database.delete( TABLE_QUOTES, COL_1 +  " = " + id +
                (!TextUtils.isEmpty(selection) ? " AND (" +
                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case QUOTES:
                count = database.update(TABLE_QUOTES, contentValues, selection, strings);
                break;
            case QUOTES_ID:
                count = database.update(TABLE_QUOTES, contentValues, DBHelper.COL_1 +
                        " = " + uri.getLastPathSegment() +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), strings);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
