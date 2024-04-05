package com.example.gallery_app

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {

    companion object {
        private const val DATABASE_NAME = "image.db"
        private const val DATABASE_VER = 1
        private const val TABLE_NAME = "allimages"
        private const val COLUMN_ID = "id"
        private const val COLUMN_SRC = "src"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_SRC TEXT)"
        p0?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        p0?.execSQL(dropTableQuery)
        onCreate(p0)
    }

    fun addImage(image: Image) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SRC,image.src)
        }
        db.insert(TABLE_NAME,null,values)
        db.close()
    }

    fun getAllImage(): MutableList<Image> {
        val images = mutableListOf<Image>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val src = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SRC))
            val image = Image(id, src)
            images.add(image)
        }
        cursor.close()
        db.close()
        return images
    }
}