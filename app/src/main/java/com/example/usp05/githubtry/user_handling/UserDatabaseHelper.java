package com.example.usp05.githubtry.user_handling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.usp05.githubtry.user_handling.User;



/**
 * Created by nathan on 4/20/18.
 */

class UserDatabaseHelper extends SQLiteOpenHelper {

    /*
    *
    * IMPORTANT! If any changes are made to the structure
    * of the database (e.g. changing the name of a column),
    * the database version MUST be updated!!
    *
    * */

    // Database information
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "USERS_DATABASE";

    // Database tables
    private static final String TABLE_USERS = "USERS";

    /***** COLUMN NAMES FOR TABLES *****/
    // Common column names
    static final String KEY_ID = "ID";
    static final String KEY_CREATED_AT = "CREATED";

    // User table -- column names
    private static final String USER_KEY = "USER_KEY";
    private static final String COLUMN_USERNAME = "USERNAME";
    private static final String COLUMN_PASSWORD = "PASSWORD";
    private static final String COLUMN_SEC_ANS_1 = "SECURITY_ANSWER_1";
    private static final String COLUMN_SEC_ANS_2 = "SECURITY_ANSWER_2";
    private static final String COLUMN_SEC_ANS_3 = "SECURITY_ANSWER_3";


    /***** SQL STATEMENTS *****/
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + USER_KEY + " TEXT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_SEC_ANS_1 + " TEXT,"
            + COLUMN_SEC_ANS_2 + " TEXT,"
            + COLUMN_SEC_ANS_3 + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String DROP_TABLE_USERS = "DROP TABLE IF EXISTS "
            + TABLE_USERS;

    long row_nInsert;
    SQLiteDatabase userDatabase;

   /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    UserDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * The database is not actually created or opened until one of
     * {@link #getWritableDatabase} or {@link #getReadableDatabase} is called.
     * <p>
     * <p>Accepts input param: a concrete instance of {@link DatabaseErrorHandler} to be
     * used to handle corruption when sqlite reports database corruption.</p>
     *
     * @param context      to use to open or create the database
     * @param name         of the database file, or null for an in-memory database
     * @param factory      to use for creating cursor objects, or null for the default
     * @param version      number of the database (starting at 1); if the database is older,
     *                     {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                     newer, {@link #onDowngrade} will be used to downgrade the database
     * @param errorHandler the {@link DatabaseErrorHandler} to be used when sqlite reports database
     */
    UserDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        userDatabase = db;
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_USERS);
        onCreate(db);
    }

    protected void insertUser(User a) {
        userDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, a.getUsername());
        values.put(COLUMN_PASSWORD, a.getPassword());
        values.put(COLUMN_SEC_ANS_1, a.getSecurityAnswer1());
        values.put(COLUMN_SEC_ANS_2, a.getSecurityAnswer2());
        values.put(COLUMN_SEC_ANS_3, a.getSecurityAnswer3());

        userDatabase.insert(TABLE_USERS, null, values);
        userDatabase.close();
    }

    protected Boolean searchUsername(String username) {
        userDatabase = getReadableDatabase();

        String query = "SELECT " + COLUMN_USERNAME
                + " FROM " + TABLE_USERS;

        Cursor cursor = userDatabase.rawQuery(query, null);

        Boolean result = false;
        if(cursor.moveToFirst()) {
            do {
                // Find username
                String a = cursor.getString(0);
                if (a.equals(username)) {
                    result = true;
                }
            } while (!result && cursor.moveToNext());
        }
        cursor.close();

        return result;
    }

    protected Boolean searchUsernameAndPassword(String username, String password) {
        userDatabase = getReadableDatabase();

        String query = "SELECT " + COLUMN_USERNAME
                + ", " + COLUMN_PASSWORD
                + " FROM " + TABLE_USERS;

        Cursor cursor = userDatabase.rawQuery(query, null);

        Boolean result = false;
        if(cursor.moveToFirst()) {
            do {
                // Find username
                String a = cursor.getString(0);
                if (a.equals(username)) {
                    // Get password
                    String b = cursor.getString(1);
                    if (b.equals(password)) {
                        result = true;
                    }
                }
            } while (!result && cursor.moveToNext());
        }

        cursor.close();

        return result;
    }

    protected Cursor getSecAnswers(String username) {
        userDatabase = getReadableDatabase();
        String query = "SELECT " + COLUMN_SEC_ANS_1 + ", " +  COLUMN_SEC_ANS_2 + ", " + COLUMN_SEC_ANS_3
                + " FROM " + TABLE_USERS
                + " WHERE " + COLUMN_USERNAME + " = '" + username + "';";
        Cursor cursor = userDatabase.rawQuery(query, null);
        return cursor;
    }

    // TODO: this method does not work
    protected User getUser(String username){
        userDatabase = getReadableDatabase();

        String query = "SELECT "
                + " * " + " FROM "
                + TABLE_USERS + ';';

        Cursor cursor = userDatabase.rawQuery(query, null);

        Boolean foundUser = false;
        User result = new User();
        if(cursor.moveToFirst()) {
            do {
                // Find username
                String a = cursor.getString(0);
                if (a.equals(username)) {
                    foundUser = true;
                    result.setUsername(username);
                    result.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                    result.setSecurityAnswer1(cursor.getString(cursor.getColumnIndex(COLUMN_SEC_ANS_1)));
                    result.setSecurityAnswer2(cursor.getString(cursor.getColumnIndex(COLUMN_SEC_ANS_2)));
                    result.setSecurityAnswer3(cursor.getString(cursor.getColumnIndex(COLUMN_SEC_ANS_3)));
                }
            } while (!foundUser && cursor.moveToNext());
        }
        cursor.close();

        return result;
    }

    protected void updatePassword(String username, String password) {
        userDatabase = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_USERS +
                " SET " + COLUMN_PASSWORD + " = '" + password + "'" +
                " WHERE " + COLUMN_USERNAME + " = '" + username + "';";
        userDatabase.execSQL(query);
        userDatabase.close();
    }
}
