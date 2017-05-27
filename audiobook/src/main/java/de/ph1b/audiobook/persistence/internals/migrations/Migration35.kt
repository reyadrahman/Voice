package de.ph1b.audiobook.persistence.internals.migrations

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import de.ph1b.audiobook.persistence.internals.*


class Migration35 : Migration {

  private val ID = "bookId"
  private val NAME = "bookName"
  private val AUTHOR = "bookAuthor"
  private val CURRENT_MEDIA_PATH = "bookCurrentMediaPath"
  private val PLAYBACK_SPEED = "bookSpeed"
  private val ROOT = "bookRoot"
  private val TIME = "bookTime"
  private val TYPE = "bookType"
  private val ACTIVE = "BOOK_ACTIVE"
  private val TABLE_NAME = "tableBooks"
  private val CREATE_TABLE = """
    CREATE TABLE $TABLE_NAME (
      $ID INTEGER PRIMARY KEY AUTOINCREMENT,
      $NAME TEXT NOT NULL,
      $AUTHOR TEXT,
      $CURRENT_MEDIA_PATH TEXT NOT NULL,
      $PLAYBACK_SPEED REAL NOT NULL,
      $ROOT TEXT NOT NULL,
      $TIME INTEGER NOT NULL,
      $TYPE TEXT NOT NULL,
      $ACTIVE INTEGER NOT NULL DEFAULT 1
    )
  """

  override fun migrate(db: SQLiteDatabase) {
    val entries = db.query(TABLE_NAME)
        .mapRows {
          Holder(long(ID), string(NAME), stringNullable(AUTHOR), string(CURRENT_MEDIA_PATH), float(PLAYBACK_SPEED), string(ROOT), long(TIME), string(TYPE), int(ACTIVE))
        }
    db.asTransaction {
      db.execSQL("DROP TABLE $TABLE_NAME")
      db.execSQL(CREATE_TABLE)
      entries.forEach {
        val cv = ContentValues().apply {
          put(ID, it.id)
          put(NAME, it.name)
          put(AUTHOR, it.author)
          put(CURRENT_MEDIA_PATH, it.path)
          put(PLAYBACK_SPEED, it.speed)
          put(ROOT, it.root)
          put(TIME, it.time)
          put(TYPE, it.type)
          put(ACTIVE, it.active)
        }
        db.insert(TABLE_NAME, null, cv)
      }
    }
  }

  data class Holder(val id: Long, val name: String, val author: String?, val path: String, val speed: Float, val root: String, val time: Long, val type: String, val active: Int)
}