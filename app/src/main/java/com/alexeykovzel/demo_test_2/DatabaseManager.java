package com.alexeykovzel.demo_test_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alexeykovzel.demo_test_2.model.Labels;
import com.alexeykovzel.demo_test_2.model.Names;
import com.alexeykovzel.demo_test_2.model.Product;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DatabaseManager extends SQLiteOpenHelper {
private static DatabaseManager sInstance;

private static final String DATABASE_NAME = "demo-test-db";
private static final int DATABASE_VERSION = 6;

// Table Name
private static final String PRODUCTS = "products";
private static final String LABELS = "labels";
private static final String NAMES = "names";

// Product Table Columns
private static final String PRODUCTS_UUID = "uuid";
private static final String PRODUCTS_CALORIES_IN_100G = "caloriesIn100g";
private static final String PRODUCTS_GLYCEMIC_INDEX = "glycemicIndex";
private static final String PRODUCTS_PROTEINS = "proteins";
private static final String PRODUCTS_FATS = "fats";
private static final String PRODUCTS_CARBOHYDRATES = "carbohydrates";
private static final String PRODUCTS_IS_COMPLEX = "isComplex";
private static final String PRODUCTS_KIND = "kind";
private static final String PRODUCTS_TYPE = "type";
private static final String PRODUCTS_SUBTYPE = "subtype";

// Labels Table Columns
private static final String LABELS_PRODUCT_UUID = "product_uuid";
private static final String LABELS_EN = "en";
private static final String LABELS_UA = "ua";

// Names Table Columns
private static final String NAMES_PRODUCT_UUID = "product_uuid";
private static final String NAMES_EN = "en";
private static final String NAMES_UA = "ua";

    public void deleteProductByUuid(String productUuid) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(LABELS, LABELS_PRODUCT_UUID + "= ?", new String[]{productUuid});
            db.delete(NAMES, NAMES_PRODUCT_UUID + "= ?", new String[]{productUuid});
            db.delete(PRODUCTS, PRODUCTS_UUID + "= ?",  new String[]{productUuid});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete the product");
        } finally {
            db.endTransaction();
        }
    }

public void addProduct(Product product) {
    SQLiteDatabase db = getWritableDatabase();
    db.beginTransaction();
    try {
        ContentValues productValues = new ContentValues();
        String productUuid = product.getUuid();
        productValues.put(PRODUCTS_UUID, productUuid);
        productValues.put(PRODUCTS_CALORIES_IN_100G, product.getCaloriesIn100g());
        productValues.put(PRODUCTS_GLYCEMIC_INDEX, product.getGlycemicIndex());
        productValues.put(PRODUCTS_PROTEINS, product.getProteins());
        productValues.put(PRODUCTS_FATS, product.getFats());
        productValues.put(PRODUCTS_CARBOHYDRATES, product.getCarbohydrates());
        int isComplex = 0;
        if (product.getComplex()) {
            isComplex = 1;
        }
        productValues.put(PRODUCTS_IS_COMPLEX, isComplex);
        productValues.put(PRODUCTS_KIND, product.getKind());
        productValues.put(PRODUCTS_TYPE, product.getType());
        productValues.put(PRODUCTS_SUBTYPE, product.getSubtype());

        ContentValues namesValues = new ContentValues();
        namesValues.put(NAMES_PRODUCT_UUID, productUuid);
        Names names = product.getNames();
        namesValues.put(NAMES_EN, names.getEn());
        namesValues.put(NAMES_UA, names.getUa());

        ContentValues labelsValues = new ContentValues();
        labelsValues.put(LABELS_PRODUCT_UUID, productUuid);
        Labels labels = product.getLabels();
        labelsValues.put(LABELS_EN, labels.getEn());
        labelsValues.put(LABELS_UA, labels.getUa());

        // First try to update the user in case the user already exists in the database
        // This assumes userNames are unique
        int rows = db.update(PRODUCTS, productValues, PRODUCTS_UUID + "= ?", new String[]{productUuid});

        // Check if update succeeded
        if (rows == 1) {
            // Get the primary key of the user we just updated
            String usersSelectQuery = String.format("SELECT * FROM %s WHERE %s = ?",
                    PRODUCTS, PRODUCTS_UUID);
            Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{productUuid});
            try {
                if (cursor.moveToFirst()) {
                    db.setTransactionSuccessful();
                }
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        } else {
            db.insertOrThrow(PRODUCTS, null, productValues);
            db.insertOrThrow(NAMES, null, namesValues);
            db.insertOrThrow(LABELS, null, labelsValues);
            db.setTransactionSuccessful();
        }
    } catch (Exception e) {
        Log.d(TAG, "Error while trying to add or update product");
    } finally {
        db.endTransaction();
    }
}


public List<Product> getAllProducts() {
    SQLiteDatabase db = getReadableDatabase();
    List<Product> products = new ArrayList<>();
    String PRODUCTS_SELECT_QUERY = "SELECT * FROM " + PRODUCTS + ";";
    Cursor cursor = db.rawQuery(PRODUCTS_SELECT_QUERY, null);
    try {
        if (cursor.moveToFirst()) {
            do {
                //configure product variables
                Product product = new Product();
                String productUuid = cursor.getString(cursor.getColumnIndex(PRODUCTS_UUID));
                product.setUuid(productUuid);
                product.setCaloriesIn100g(cursor.getDouble(cursor.getColumnIndex(PRODUCTS_CALORIES_IN_100G)));
                product.setGlycemicIndex(cursor.getDouble(cursor.getColumnIndex(PRODUCTS_GLYCEMIC_INDEX)));
                product.setProteins(cursor.getDouble(cursor.getColumnIndex(PRODUCTS_PROTEINS)));
                product.setFats(cursor.getDouble(cursor.getColumnIndex(PRODUCTS_FATS)));
                product.setCarbohydrates(cursor.getDouble(cursor.getColumnIndex(PRODUCTS_CARBOHYDRATES)));
                product.setComplex(cursor.getInt(cursor.getColumnIndex(PRODUCTS_IS_COMPLEX)) > 0);
                product.setKind(cursor.getString(cursor.getColumnIndex(PRODUCTS_KIND)));
                product.setType(cursor.getString(cursor.getColumnIndex(PRODUCTS_TYPE)));
                product.setSubtype(cursor.getString(cursor.getColumnIndex(PRODUCTS_SUBTYPE)));

                //configure all product labels
                String LABELS_SELECT_QUERY = "SELECT * FROM " + LABELS + " WHERE " + LABELS_PRODUCT_UUID + " = '" + productUuid + "';";
                Cursor cursorLabels = db.rawQuery(LABELS_SELECT_QUERY, null);
                Labels labels = new Labels();
                try {
                    if (cursorLabels.moveToFirst()) {
                        do {
                            labels.setEn(cursorLabels.getString(cursorLabels.getColumnIndex(LABELS_EN)));
                            labels.setUa(cursorLabels.getString(cursorLabels.getColumnIndex(LABELS_UA)));
                        } while (cursorLabels.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Error while trying to get labels from database");
                } finally {
                    if (cursorLabels != null && !cursorLabels.isClosed()) {
                        cursorLabels.close();
                    }
                }
                product.setLabels(labels);

                //configure all product names
                String NAMES_SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = '%s';",
                        NAMES, NAMES_PRODUCT_UUID, productUuid);
                Cursor cursorNames = db.rawQuery(NAMES_SELECT_QUERY, null);
                Names names = new Names();
                try {
                    if (cursorNames.moveToFirst()) {
                        do {
                            names.setEn(cursorNames.getString(cursorNames.getColumnIndex(LABELS_EN)));
                            names.setUa(cursorNames.getString(cursorNames.getColumnIndex(LABELS_UA)));
                        } while (cursorNames.moveToNext());
                    }
                    product.setNames(names);
                } catch (Exception e) {
                    Log.d(TAG, "Error while trying to get names from database");
                } finally {
                    if (cursorNames != null && !cursorNames.isClosed()) {
                        cursorNames.close();
                    }
                }

                products.add(product);
            } while (cursor.moveToNext());
        }
    } catch (Exception e) {
        Log.d(TAG, "Error while trying to get products from database");
    } finally {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
    return products;
}

public DatabaseManager(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
}

// Called when the database connection is being configured.
// Configure database settings for things like foreign key support, write-ahead logging, etc.
@Override
public void onConfigure(SQLiteDatabase db) {
    super.onConfigure(db);
    db.setForeignKeyConstraintsEnabled(true);
}

// Called when the database is created for the FIRST time.
// If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
@Override
public void onCreate(SQLiteDatabase db) {
    String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + PRODUCTS +
            "(" +
            PRODUCTS_UUID + " varchar(255) NOT NULL PRIMARY KEY, " + // Define a primary key
            PRODUCTS_CALORIES_IN_100G + " real, " +
            PRODUCTS_GLYCEMIC_INDEX + " real, " +
            PRODUCTS_PROTEINS + " real, " +
            PRODUCTS_FATS + " real, " +
            PRODUCTS_CARBOHYDRATES + " real, " +
            PRODUCTS_IS_COMPLEX + " int, " +
            PRODUCTS_KIND + " varchar(255), " +
            PRODUCTS_TYPE + " varchar(255), " +
            PRODUCTS_SUBTYPE + " varchar(255)" +
            ");";

    String CREATE_LABELS_TABLE = "CREATE TABLE " + LABELS +
            "(" +
            LABELS_PRODUCT_UUID + " varchar(255) NOT NULL, " +
            LABELS_EN + " varchar(255), " +
            LABELS_UA + " varchar(255), " +
            "FOREIGN KEY (" + LABELS_PRODUCT_UUID + ") REFERENCES " + PRODUCTS + "(" + PRODUCTS_UUID + ")" +
            ");";

    String CREATE_NAMES_TABLE = "CREATE TABLE " + NAMES +
            " (" +
            NAMES_PRODUCT_UUID + " varchar(255) NOT NULL, " +
            NAMES_EN + " varchar(255), " +
            NAMES_UA + " varchar(255), " +
            "FOREIGN KEY (" + NAMES_PRODUCT_UUID + ") REFERENCES " + PRODUCTS + "(" + PRODUCTS_UUID + ")" +
            ");";

    db.execSQL(CREATE_PRODUCTS_TABLE);
    db.execSQL(CREATE_LABELS_TABLE);
    db.execSQL(CREATE_NAMES_TABLE);
}

// Called when the database needs to be upgraded.
// This method will only be called if a database already exists on disk with the same DATABASE_NAME,
// but the DATABASE_VERSION is different than the version of the database that exists on disk.
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    if (oldVersion != newVersion) {
        // Simplest implementation is to drop all old tables and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + LABELS);
        db.execSQL("DROP TABLE IF EXISTS " + NAMES);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS);
        onCreate(db);
    }
}

public static synchronized DatabaseManager getInstance(Context context) {
    if (sInstance == null) {
        sInstance = new DatabaseManager(context.getApplicationContext());
    }
    return sInstance;
}
}

