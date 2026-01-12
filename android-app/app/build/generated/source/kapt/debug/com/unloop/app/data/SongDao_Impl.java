package com.unloop.app.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SongDao_Impl implements SongDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Song> __insertionAdapterOfSong;

  private final EntityInsertionAdapter<DailyStats> __insertionAdapterOfDailyStats;

  private final EntityInsertionAdapter<SkipHistoryEntry> __insertionAdapterOfSkipHistoryEntry;

  private final EntityDeletionOrUpdateAdapter<Song> __deletionAdapterOfSong;

  private final EntityDeletionOrUpdateAdapter<Song> __updateAdapterOfSong;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllSongs;

  private final SharedSQLiteStatement __preparedStmtOfSetWhitelisted;

  private final SharedSQLiteStatement __preparedStmtOfSetBlacklisted;

  private final SharedSQLiteStatement __preparedStmtOfClearSkipHistory;

  public SongDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSong = new EntityInsertionAdapter<Song>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `songs` (`id`,`title`,`artist`,`platform`,`playCount`,`skipCount`,`quickSkipCount`,`loopsAvoided`,`firstPlayedAt`,`lastPlayedAt`,`totalListenTimeMs`,`youtubePlayCount`,`spotifyPlayCount`,`youtubeListenTimeMs`,`spotifyListenTimeMs`,`isWhitelisted`,`isBlacklisted`,`affectionScore`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Song entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getArtist() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getArtist());
        }
        if (entity.getPlatform() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPlatform());
        }
        statement.bindLong(5, entity.getPlayCount());
        statement.bindLong(6, entity.getSkipCount());
        statement.bindLong(7, entity.getQuickSkipCount());
        statement.bindLong(8, entity.getLoopsAvoided());
        statement.bindLong(9, entity.getFirstPlayedAt());
        statement.bindLong(10, entity.getLastPlayedAt());
        statement.bindLong(11, entity.getTotalListenTimeMs());
        statement.bindLong(12, entity.getYoutubePlayCount());
        statement.bindLong(13, entity.getSpotifyPlayCount());
        statement.bindLong(14, entity.getYoutubeListenTimeMs());
        statement.bindLong(15, entity.getSpotifyListenTimeMs());
        final int _tmp = entity.isWhitelisted() ? 1 : 0;
        statement.bindLong(16, _tmp);
        final int _tmp_1 = entity.isBlacklisted() ? 1 : 0;
        statement.bindLong(17, _tmp_1);
        statement.bindDouble(18, entity.getAffectionScore());
      }
    };
    this.__insertionAdapterOfDailyStats = new EntityInsertionAdapter<DailyStats>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `daily_stats` (`date`,`songsDiscovered`,`loopsAvoided`,`listenTimeMs`,`youtubePlays`,`spotifyPlays`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DailyStats entity) {
        if (entity.getDate() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getDate());
        }
        statement.bindLong(2, entity.getSongsDiscovered());
        statement.bindLong(3, entity.getLoopsAvoided());
        statement.bindLong(4, entity.getListenTimeMs());
        statement.bindLong(5, entity.getYoutubePlays());
        statement.bindLong(6, entity.getSpotifyPlays());
      }
    };
    this.__insertionAdapterOfSkipHistoryEntry = new EntityInsertionAdapter<SkipHistoryEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `skip_history` (`id`,`songId`,`songTitle`,`songArtist`,`platform`,`reason`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SkipHistoryEntry entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getSongId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getSongId());
        }
        if (entity.getSongTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSongTitle());
        }
        if (entity.getSongArtist() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSongArtist());
        }
        if (entity.getPlatform() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPlatform());
        }
        if (entity.getReason() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getReason());
        }
        statement.bindLong(7, entity.getTimestamp());
      }
    };
    this.__deletionAdapterOfSong = new EntityDeletionOrUpdateAdapter<Song>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `songs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Song entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfSong = new EntityDeletionOrUpdateAdapter<Song>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `songs` SET `id` = ?,`title` = ?,`artist` = ?,`platform` = ?,`playCount` = ?,`skipCount` = ?,`quickSkipCount` = ?,`loopsAvoided` = ?,`firstPlayedAt` = ?,`lastPlayedAt` = ?,`totalListenTimeMs` = ?,`youtubePlayCount` = ?,`spotifyPlayCount` = ?,`youtubeListenTimeMs` = ?,`spotifyListenTimeMs` = ?,`isWhitelisted` = ?,`isBlacklisted` = ?,`affectionScore` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Song entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getArtist() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getArtist());
        }
        if (entity.getPlatform() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPlatform());
        }
        statement.bindLong(5, entity.getPlayCount());
        statement.bindLong(6, entity.getSkipCount());
        statement.bindLong(7, entity.getQuickSkipCount());
        statement.bindLong(8, entity.getLoopsAvoided());
        statement.bindLong(9, entity.getFirstPlayedAt());
        statement.bindLong(10, entity.getLastPlayedAt());
        statement.bindLong(11, entity.getTotalListenTimeMs());
        statement.bindLong(12, entity.getYoutubePlayCount());
        statement.bindLong(13, entity.getSpotifyPlayCount());
        statement.bindLong(14, entity.getYoutubeListenTimeMs());
        statement.bindLong(15, entity.getSpotifyListenTimeMs());
        final int _tmp = entity.isWhitelisted() ? 1 : 0;
        statement.bindLong(16, _tmp);
        final int _tmp_1 = entity.isBlacklisted() ? 1 : 0;
        statement.bindLong(17, _tmp_1);
        statement.bindDouble(18, entity.getAffectionScore());
        if (entity.getId() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteAllSongs = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM songs";
        return _query;
      }
    };
    this.__preparedStmtOfSetWhitelisted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE songs SET isWhitelisted = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetBlacklisted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE songs SET isBlacklisted = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearSkipHistory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM skip_history";
        return _query;
      }
    };
  }

  @Override
  public Object insertSong(final Song song, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSong.insert(song);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSongs(final List<Song> songs, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSong.insert(songs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertDailyStats(final DailyStats stats,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDailyStats.insert(stats);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSkipHistory(final SkipHistoryEntry entry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSkipHistoryEntry.insert(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSkipHistoryList(final List<SkipHistoryEntry> entries,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSkipHistoryEntry.insert(entries);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSong(final Song song, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSong.handle(song);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSong(final Song song, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSong.handle(song);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllSongs(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllSongs.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllSongs.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setWhitelisted(final String songId, final boolean isWhitelisted,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetWhitelisted.acquire();
        int _argIndex = 1;
        final int _tmp = isWhitelisted ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        if (songId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, songId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetWhitelisted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setBlacklisted(final String songId, final boolean isBlacklisted,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetBlacklisted.acquire();
        int _argIndex = 1;
        final int _tmp = isBlacklisted ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        if (songId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, songId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetBlacklisted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearSkipHistory(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearSkipHistory.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearSkipHistory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Song>> getAllSongs() {
    final String _sql = "SELECT * FROM songs ORDER BY lastPlayedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"songs"}, new Callable<List<Song>>() {
      @Override
      @NonNull
      public List<Song> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final int _cursorIndexOfSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skipCount");
          final int _cursorIndexOfQuickSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "quickSkipCount");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfFirstPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstPlayedAt");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfTotalListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalListenTimeMs");
          final int _cursorIndexOfYoutubePlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlayCount");
          final int _cursorIndexOfSpotifyPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlayCount");
          final int _cursorIndexOfYoutubeListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubeListenTimeMs");
          final int _cursorIndexOfSpotifyListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyListenTimeMs");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsBlacklisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlacklisted");
          final int _cursorIndexOfAffectionScore = CursorUtil.getColumnIndexOrThrow(_cursor, "affectionScore");
          final List<Song> _result = new ArrayList<Song>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Song _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            final int _tmpSkipCount;
            _tmpSkipCount = _cursor.getInt(_cursorIndexOfSkipCount);
            final int _tmpQuickSkipCount;
            _tmpQuickSkipCount = _cursor.getInt(_cursorIndexOfQuickSkipCount);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpFirstPlayedAt;
            _tmpFirstPlayedAt = _cursor.getLong(_cursorIndexOfFirstPlayedAt);
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final long _tmpTotalListenTimeMs;
            _tmpTotalListenTimeMs = _cursor.getLong(_cursorIndexOfTotalListenTimeMs);
            final int _tmpYoutubePlayCount;
            _tmpYoutubePlayCount = _cursor.getInt(_cursorIndexOfYoutubePlayCount);
            final int _tmpSpotifyPlayCount;
            _tmpSpotifyPlayCount = _cursor.getInt(_cursorIndexOfSpotifyPlayCount);
            final long _tmpYoutubeListenTimeMs;
            _tmpYoutubeListenTimeMs = _cursor.getLong(_cursorIndexOfYoutubeListenTimeMs);
            final long _tmpSpotifyListenTimeMs;
            _tmpSpotifyListenTimeMs = _cursor.getLong(_cursorIndexOfSpotifyListenTimeMs);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsBlacklisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlacklisted);
            _tmpIsBlacklisted = _tmp_1 != 0;
            final float _tmpAffectionScore;
            _tmpAffectionScore = _cursor.getFloat(_cursorIndexOfAffectionScore);
            _item = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpPlatform,_tmpPlayCount,_tmpSkipCount,_tmpQuickSkipCount,_tmpLoopsAvoided,_tmpFirstPlayedAt,_tmpLastPlayedAt,_tmpTotalListenTimeMs,_tmpYoutubePlayCount,_tmpSpotifyPlayCount,_tmpYoutubeListenTimeMs,_tmpSpotifyListenTimeMs,_tmpIsWhitelisted,_tmpIsBlacklisted,_tmpAffectionScore);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getSongById(final String songId, final Continuation<? super Song> $completion) {
    final String _sql = "SELECT * FROM songs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (songId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, songId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Song>() {
      @Override
      @Nullable
      public Song call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final int _cursorIndexOfSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skipCount");
          final int _cursorIndexOfQuickSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "quickSkipCount");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfFirstPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstPlayedAt");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfTotalListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalListenTimeMs");
          final int _cursorIndexOfYoutubePlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlayCount");
          final int _cursorIndexOfSpotifyPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlayCount");
          final int _cursorIndexOfYoutubeListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubeListenTimeMs");
          final int _cursorIndexOfSpotifyListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyListenTimeMs");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsBlacklisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlacklisted");
          final int _cursorIndexOfAffectionScore = CursorUtil.getColumnIndexOrThrow(_cursor, "affectionScore");
          final Song _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            final int _tmpSkipCount;
            _tmpSkipCount = _cursor.getInt(_cursorIndexOfSkipCount);
            final int _tmpQuickSkipCount;
            _tmpQuickSkipCount = _cursor.getInt(_cursorIndexOfQuickSkipCount);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpFirstPlayedAt;
            _tmpFirstPlayedAt = _cursor.getLong(_cursorIndexOfFirstPlayedAt);
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final long _tmpTotalListenTimeMs;
            _tmpTotalListenTimeMs = _cursor.getLong(_cursorIndexOfTotalListenTimeMs);
            final int _tmpYoutubePlayCount;
            _tmpYoutubePlayCount = _cursor.getInt(_cursorIndexOfYoutubePlayCount);
            final int _tmpSpotifyPlayCount;
            _tmpSpotifyPlayCount = _cursor.getInt(_cursorIndexOfSpotifyPlayCount);
            final long _tmpYoutubeListenTimeMs;
            _tmpYoutubeListenTimeMs = _cursor.getLong(_cursorIndexOfYoutubeListenTimeMs);
            final long _tmpSpotifyListenTimeMs;
            _tmpSpotifyListenTimeMs = _cursor.getLong(_cursorIndexOfSpotifyListenTimeMs);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsBlacklisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlacklisted);
            _tmpIsBlacklisted = _tmp_1 != 0;
            final float _tmpAffectionScore;
            _tmpAffectionScore = _cursor.getFloat(_cursorIndexOfAffectionScore);
            _result = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpPlatform,_tmpPlayCount,_tmpSkipCount,_tmpQuickSkipCount,_tmpLoopsAvoided,_tmpFirstPlayedAt,_tmpLastPlayedAt,_tmpTotalListenTimeMs,_tmpYoutubePlayCount,_tmpSpotifyPlayCount,_tmpYoutubeListenTimeMs,_tmpSpotifyListenTimeMs,_tmpIsWhitelisted,_tmpIsBlacklisted,_tmpAffectionScore);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Song>> searchSongs(final String query) {
    final String _sql = "SELECT * FROM songs WHERE title LIKE '%' || ? || '%' OR artist LIKE '%' || ? || '%' ORDER BY lastPlayedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 2;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"songs"}, new Callable<List<Song>>() {
      @Override
      @NonNull
      public List<Song> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final int _cursorIndexOfSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skipCount");
          final int _cursorIndexOfQuickSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "quickSkipCount");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfFirstPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstPlayedAt");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfTotalListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalListenTimeMs");
          final int _cursorIndexOfYoutubePlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlayCount");
          final int _cursorIndexOfSpotifyPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlayCount");
          final int _cursorIndexOfYoutubeListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubeListenTimeMs");
          final int _cursorIndexOfSpotifyListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyListenTimeMs");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsBlacklisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlacklisted");
          final int _cursorIndexOfAffectionScore = CursorUtil.getColumnIndexOrThrow(_cursor, "affectionScore");
          final List<Song> _result = new ArrayList<Song>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Song _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            final int _tmpSkipCount;
            _tmpSkipCount = _cursor.getInt(_cursorIndexOfSkipCount);
            final int _tmpQuickSkipCount;
            _tmpQuickSkipCount = _cursor.getInt(_cursorIndexOfQuickSkipCount);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpFirstPlayedAt;
            _tmpFirstPlayedAt = _cursor.getLong(_cursorIndexOfFirstPlayedAt);
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final long _tmpTotalListenTimeMs;
            _tmpTotalListenTimeMs = _cursor.getLong(_cursorIndexOfTotalListenTimeMs);
            final int _tmpYoutubePlayCount;
            _tmpYoutubePlayCount = _cursor.getInt(_cursorIndexOfYoutubePlayCount);
            final int _tmpSpotifyPlayCount;
            _tmpSpotifyPlayCount = _cursor.getInt(_cursorIndexOfSpotifyPlayCount);
            final long _tmpYoutubeListenTimeMs;
            _tmpYoutubeListenTimeMs = _cursor.getLong(_cursorIndexOfYoutubeListenTimeMs);
            final long _tmpSpotifyListenTimeMs;
            _tmpSpotifyListenTimeMs = _cursor.getLong(_cursorIndexOfSpotifyListenTimeMs);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsBlacklisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlacklisted);
            _tmpIsBlacklisted = _tmp_1 != 0;
            final float _tmpAffectionScore;
            _tmpAffectionScore = _cursor.getFloat(_cursorIndexOfAffectionScore);
            _item = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpPlatform,_tmpPlayCount,_tmpSkipCount,_tmpQuickSkipCount,_tmpLoopsAvoided,_tmpFirstPlayedAt,_tmpLastPlayedAt,_tmpTotalListenTimeMs,_tmpYoutubePlayCount,_tmpSpotifyPlayCount,_tmpYoutubeListenTimeMs,_tmpSpotifyListenTimeMs,_tmpIsWhitelisted,_tmpIsBlacklisted,_tmpAffectionScore);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Song>> searchWhitelistedSongs(final String query) {
    final String _sql = "SELECT * FROM songs WHERE (title LIKE '%' || ? || '%' OR artist LIKE '%' || ? || '%') AND isWhitelisted = 1 ORDER BY lastPlayedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 2;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"songs"}, new Callable<List<Song>>() {
      @Override
      @NonNull
      public List<Song> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final int _cursorIndexOfSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skipCount");
          final int _cursorIndexOfQuickSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "quickSkipCount");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfFirstPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstPlayedAt");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfTotalListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalListenTimeMs");
          final int _cursorIndexOfYoutubePlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlayCount");
          final int _cursorIndexOfSpotifyPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlayCount");
          final int _cursorIndexOfYoutubeListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubeListenTimeMs");
          final int _cursorIndexOfSpotifyListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyListenTimeMs");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsBlacklisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlacklisted");
          final int _cursorIndexOfAffectionScore = CursorUtil.getColumnIndexOrThrow(_cursor, "affectionScore");
          final List<Song> _result = new ArrayList<Song>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Song _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            final int _tmpSkipCount;
            _tmpSkipCount = _cursor.getInt(_cursorIndexOfSkipCount);
            final int _tmpQuickSkipCount;
            _tmpQuickSkipCount = _cursor.getInt(_cursorIndexOfQuickSkipCount);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpFirstPlayedAt;
            _tmpFirstPlayedAt = _cursor.getLong(_cursorIndexOfFirstPlayedAt);
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final long _tmpTotalListenTimeMs;
            _tmpTotalListenTimeMs = _cursor.getLong(_cursorIndexOfTotalListenTimeMs);
            final int _tmpYoutubePlayCount;
            _tmpYoutubePlayCount = _cursor.getInt(_cursorIndexOfYoutubePlayCount);
            final int _tmpSpotifyPlayCount;
            _tmpSpotifyPlayCount = _cursor.getInt(_cursorIndexOfSpotifyPlayCount);
            final long _tmpYoutubeListenTimeMs;
            _tmpYoutubeListenTimeMs = _cursor.getLong(_cursorIndexOfYoutubeListenTimeMs);
            final long _tmpSpotifyListenTimeMs;
            _tmpSpotifyListenTimeMs = _cursor.getLong(_cursorIndexOfSpotifyListenTimeMs);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsBlacklisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlacklisted);
            _tmpIsBlacklisted = _tmp_1 != 0;
            final float _tmpAffectionScore;
            _tmpAffectionScore = _cursor.getFloat(_cursorIndexOfAffectionScore);
            _item = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpPlatform,_tmpPlayCount,_tmpSkipCount,_tmpQuickSkipCount,_tmpLoopsAvoided,_tmpFirstPlayedAt,_tmpLastPlayedAt,_tmpTotalListenTimeMs,_tmpYoutubePlayCount,_tmpSpotifyPlayCount,_tmpYoutubeListenTimeMs,_tmpSpotifyListenTimeMs,_tmpIsWhitelisted,_tmpIsBlacklisted,_tmpAffectionScore);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Song>> searchBlacklistedSongs(final String query) {
    final String _sql = "SELECT * FROM songs WHERE (title LIKE '%' || ? || '%' OR artist LIKE '%' || ? || '%') AND isBlacklisted = 1 ORDER BY lastPlayedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 2;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"songs"}, new Callable<List<Song>>() {
      @Override
      @NonNull
      public List<Song> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final int _cursorIndexOfSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skipCount");
          final int _cursorIndexOfQuickSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "quickSkipCount");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfFirstPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstPlayedAt");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfTotalListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalListenTimeMs");
          final int _cursorIndexOfYoutubePlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlayCount");
          final int _cursorIndexOfSpotifyPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlayCount");
          final int _cursorIndexOfYoutubeListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubeListenTimeMs");
          final int _cursorIndexOfSpotifyListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyListenTimeMs");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsBlacklisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlacklisted");
          final int _cursorIndexOfAffectionScore = CursorUtil.getColumnIndexOrThrow(_cursor, "affectionScore");
          final List<Song> _result = new ArrayList<Song>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Song _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            final int _tmpSkipCount;
            _tmpSkipCount = _cursor.getInt(_cursorIndexOfSkipCount);
            final int _tmpQuickSkipCount;
            _tmpQuickSkipCount = _cursor.getInt(_cursorIndexOfQuickSkipCount);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpFirstPlayedAt;
            _tmpFirstPlayedAt = _cursor.getLong(_cursorIndexOfFirstPlayedAt);
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final long _tmpTotalListenTimeMs;
            _tmpTotalListenTimeMs = _cursor.getLong(_cursorIndexOfTotalListenTimeMs);
            final int _tmpYoutubePlayCount;
            _tmpYoutubePlayCount = _cursor.getInt(_cursorIndexOfYoutubePlayCount);
            final int _tmpSpotifyPlayCount;
            _tmpSpotifyPlayCount = _cursor.getInt(_cursorIndexOfSpotifyPlayCount);
            final long _tmpYoutubeListenTimeMs;
            _tmpYoutubeListenTimeMs = _cursor.getLong(_cursorIndexOfYoutubeListenTimeMs);
            final long _tmpSpotifyListenTimeMs;
            _tmpSpotifyListenTimeMs = _cursor.getLong(_cursorIndexOfSpotifyListenTimeMs);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsBlacklisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlacklisted);
            _tmpIsBlacklisted = _tmp_1 != 0;
            final float _tmpAffectionScore;
            _tmpAffectionScore = _cursor.getFloat(_cursorIndexOfAffectionScore);
            _item = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpPlatform,_tmpPlayCount,_tmpSkipCount,_tmpQuickSkipCount,_tmpLoopsAvoided,_tmpFirstPlayedAt,_tmpLastPlayedAt,_tmpTotalListenTimeMs,_tmpYoutubePlayCount,_tmpSpotifyPlayCount,_tmpYoutubeListenTimeMs,_tmpSpotifyListenTimeMs,_tmpIsWhitelisted,_tmpIsBlacklisted,_tmpAffectionScore);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRecentSongs(final int limit,
      final Continuation<? super List<Song>> $completion) {
    final String _sql = "SELECT * FROM songs ORDER BY lastPlayedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Song>>() {
      @Override
      @NonNull
      public List<Song> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final int _cursorIndexOfSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skipCount");
          final int _cursorIndexOfQuickSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "quickSkipCount");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfFirstPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstPlayedAt");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfTotalListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalListenTimeMs");
          final int _cursorIndexOfYoutubePlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlayCount");
          final int _cursorIndexOfSpotifyPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlayCount");
          final int _cursorIndexOfYoutubeListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubeListenTimeMs");
          final int _cursorIndexOfSpotifyListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyListenTimeMs");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsBlacklisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlacklisted");
          final int _cursorIndexOfAffectionScore = CursorUtil.getColumnIndexOrThrow(_cursor, "affectionScore");
          final List<Song> _result = new ArrayList<Song>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Song _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            final int _tmpSkipCount;
            _tmpSkipCount = _cursor.getInt(_cursorIndexOfSkipCount);
            final int _tmpQuickSkipCount;
            _tmpQuickSkipCount = _cursor.getInt(_cursorIndexOfQuickSkipCount);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpFirstPlayedAt;
            _tmpFirstPlayedAt = _cursor.getLong(_cursorIndexOfFirstPlayedAt);
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final long _tmpTotalListenTimeMs;
            _tmpTotalListenTimeMs = _cursor.getLong(_cursorIndexOfTotalListenTimeMs);
            final int _tmpYoutubePlayCount;
            _tmpYoutubePlayCount = _cursor.getInt(_cursorIndexOfYoutubePlayCount);
            final int _tmpSpotifyPlayCount;
            _tmpSpotifyPlayCount = _cursor.getInt(_cursorIndexOfSpotifyPlayCount);
            final long _tmpYoutubeListenTimeMs;
            _tmpYoutubeListenTimeMs = _cursor.getLong(_cursorIndexOfYoutubeListenTimeMs);
            final long _tmpSpotifyListenTimeMs;
            _tmpSpotifyListenTimeMs = _cursor.getLong(_cursorIndexOfSpotifyListenTimeMs);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsBlacklisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlacklisted);
            _tmpIsBlacklisted = _tmp_1 != 0;
            final float _tmpAffectionScore;
            _tmpAffectionScore = _cursor.getFloat(_cursorIndexOfAffectionScore);
            _item = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpPlatform,_tmpPlayCount,_tmpSkipCount,_tmpQuickSkipCount,_tmpLoopsAvoided,_tmpFirstPlayedAt,_tmpLastPlayedAt,_tmpTotalListenTimeMs,_tmpYoutubePlayCount,_tmpSpotifyPlayCount,_tmpYoutubeListenTimeMs,_tmpSpotifyListenTimeMs,_tmpIsWhitelisted,_tmpIsBlacklisted,_tmpAffectionScore);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRecentSongsByArtist(final String artist, final int limit,
      final Continuation<? super List<Song>> $completion) {
    final String _sql = "SELECT * FROM songs WHERE artist = ? ORDER BY lastPlayedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (artist == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, artist);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Song>>() {
      @Override
      @NonNull
      public List<Song> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final int _cursorIndexOfSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skipCount");
          final int _cursorIndexOfQuickSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "quickSkipCount");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfFirstPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstPlayedAt");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfTotalListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalListenTimeMs");
          final int _cursorIndexOfYoutubePlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlayCount");
          final int _cursorIndexOfSpotifyPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlayCount");
          final int _cursorIndexOfYoutubeListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubeListenTimeMs");
          final int _cursorIndexOfSpotifyListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyListenTimeMs");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsBlacklisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlacklisted");
          final int _cursorIndexOfAffectionScore = CursorUtil.getColumnIndexOrThrow(_cursor, "affectionScore");
          final List<Song> _result = new ArrayList<Song>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Song _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            final int _tmpSkipCount;
            _tmpSkipCount = _cursor.getInt(_cursorIndexOfSkipCount);
            final int _tmpQuickSkipCount;
            _tmpQuickSkipCount = _cursor.getInt(_cursorIndexOfQuickSkipCount);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpFirstPlayedAt;
            _tmpFirstPlayedAt = _cursor.getLong(_cursorIndexOfFirstPlayedAt);
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final long _tmpTotalListenTimeMs;
            _tmpTotalListenTimeMs = _cursor.getLong(_cursorIndexOfTotalListenTimeMs);
            final int _tmpYoutubePlayCount;
            _tmpYoutubePlayCount = _cursor.getInt(_cursorIndexOfYoutubePlayCount);
            final int _tmpSpotifyPlayCount;
            _tmpSpotifyPlayCount = _cursor.getInt(_cursorIndexOfSpotifyPlayCount);
            final long _tmpYoutubeListenTimeMs;
            _tmpYoutubeListenTimeMs = _cursor.getLong(_cursorIndexOfYoutubeListenTimeMs);
            final long _tmpSpotifyListenTimeMs;
            _tmpSpotifyListenTimeMs = _cursor.getLong(_cursorIndexOfSpotifyListenTimeMs);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsBlacklisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlacklisted);
            _tmpIsBlacklisted = _tmp_1 != 0;
            final float _tmpAffectionScore;
            _tmpAffectionScore = _cursor.getFloat(_cursorIndexOfAffectionScore);
            _item = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpPlatform,_tmpPlayCount,_tmpSkipCount,_tmpQuickSkipCount,_tmpLoopsAvoided,_tmpFirstPlayedAt,_tmpLastPlayedAt,_tmpTotalListenTimeMs,_tmpYoutubePlayCount,_tmpSpotifyPlayCount,_tmpYoutubeListenTimeMs,_tmpSpotifyListenTimeMs,_tmpIsWhitelisted,_tmpIsBlacklisted,_tmpAffectionScore);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTotalSongCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> getTotalSongCountFlow() {
    final String _sql = "SELECT COUNT(*) FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"songs"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getUniqueArtistCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(DISTINCT artist) FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> getUniqueArtistCountFlow() {
    final String _sql = "SELECT COUNT(DISTINCT artist) FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"songs"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTotalLoopsAvoided(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT SUM(loopsAvoided) FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> getTotalLoopsAvoidedFlow() {
    final String _sql = "SELECT SUM(loopsAvoided) FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"songs"}, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTotalSkipCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT SUM(skipCount) FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTotalListenTime(final Continuation<? super Long> $completion) {
    final String _sql = "SELECT SUM(totalListenTimeMs) FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Long> getTotalListenTimeFlow() {
    final String _sql = "SELECT SUM(totalListenTimeMs) FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"songs"}, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getYoutubeListenTime(final Continuation<? super Long> $completion) {
    final String _sql = "SELECT SUM(youtubeListenTimeMs) FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSpotifyListenTime(final Continuation<? super Long> $completion) {
    final String _sql = "SELECT SUM(spotifyListenTimeMs) FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getYoutubePlays(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT SUM(youtubePlayCount) FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSpotifyPlays(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT SUM(spotifyPlayCount) FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSongsDiscoveredSince(final long since,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM songs WHERE firstPlayedAt >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getArtistsDiscoveredSince(final long since,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(DISTINCT artist) FROM songs WHERE firstPlayedAt >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTopArtists(final int limit,
      final Continuation<? super List<ArtistStats>> $completion) {
    final String _sql = "\n"
            + "        SELECT artist as name, \n"
            + "               COUNT(*) as songCount, \n"
            + "               SUM(playCount) as totalPlayCount, \n"
            + "               SUM(totalListenTimeMs) as totalListenTimeMs,\n"
            + "               SUM(loopsAvoided) as loopsAvoided\n"
            + "        FROM songs \n"
            + "        GROUP BY artist \n"
            + "        ORDER BY totalPlayCount DESC \n"
            + "        LIMIT ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ArtistStats>>() {
      @Override
      @NonNull
      public List<ArtistStats> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = 0;
          final int _cursorIndexOfSongCount = 1;
          final int _cursorIndexOfTotalPlayCount = 2;
          final int _cursorIndexOfTotalListenTimeMs = 3;
          final int _cursorIndexOfLoopsAvoided = 4;
          final List<ArtistStats> _result = new ArrayList<ArtistStats>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ArtistStats _item;
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final int _tmpSongCount;
            _tmpSongCount = _cursor.getInt(_cursorIndexOfSongCount);
            final int _tmpTotalPlayCount;
            _tmpTotalPlayCount = _cursor.getInt(_cursorIndexOfTotalPlayCount);
            final long _tmpTotalListenTimeMs;
            _tmpTotalListenTimeMs = _cursor.getLong(_cursorIndexOfTotalListenTimeMs);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            _item = new ArtistStats(_tmpName,_tmpSongCount,_tmpTotalPlayCount,_tmpTotalListenTimeMs,_tmpLoopsAvoided);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTopSongs(final int limit, final Continuation<? super List<Song>> $completion) {
    final String _sql = "SELECT * FROM songs ORDER BY playCount DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Song>>() {
      @Override
      @NonNull
      public List<Song> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final int _cursorIndexOfSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skipCount");
          final int _cursorIndexOfQuickSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "quickSkipCount");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfFirstPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstPlayedAt");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfTotalListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalListenTimeMs");
          final int _cursorIndexOfYoutubePlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlayCount");
          final int _cursorIndexOfSpotifyPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlayCount");
          final int _cursorIndexOfYoutubeListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubeListenTimeMs");
          final int _cursorIndexOfSpotifyListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyListenTimeMs");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsBlacklisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlacklisted");
          final int _cursorIndexOfAffectionScore = CursorUtil.getColumnIndexOrThrow(_cursor, "affectionScore");
          final List<Song> _result = new ArrayList<Song>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Song _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            final int _tmpSkipCount;
            _tmpSkipCount = _cursor.getInt(_cursorIndexOfSkipCount);
            final int _tmpQuickSkipCount;
            _tmpQuickSkipCount = _cursor.getInt(_cursorIndexOfQuickSkipCount);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpFirstPlayedAt;
            _tmpFirstPlayedAt = _cursor.getLong(_cursorIndexOfFirstPlayedAt);
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final long _tmpTotalListenTimeMs;
            _tmpTotalListenTimeMs = _cursor.getLong(_cursorIndexOfTotalListenTimeMs);
            final int _tmpYoutubePlayCount;
            _tmpYoutubePlayCount = _cursor.getInt(_cursorIndexOfYoutubePlayCount);
            final int _tmpSpotifyPlayCount;
            _tmpSpotifyPlayCount = _cursor.getInt(_cursorIndexOfSpotifyPlayCount);
            final long _tmpYoutubeListenTimeMs;
            _tmpYoutubeListenTimeMs = _cursor.getLong(_cursorIndexOfYoutubeListenTimeMs);
            final long _tmpSpotifyListenTimeMs;
            _tmpSpotifyListenTimeMs = _cursor.getLong(_cursorIndexOfSpotifyListenTimeMs);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsBlacklisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlacklisted);
            _tmpIsBlacklisted = _tmp_1 != 0;
            final float _tmpAffectionScore;
            _tmpAffectionScore = _cursor.getFloat(_cursorIndexOfAffectionScore);
            _item = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpPlatform,_tmpPlayCount,_tmpSkipCount,_tmpQuickSkipCount,_tmpLoopsAvoided,_tmpFirstPlayedAt,_tmpLastPlayedAt,_tmpTotalListenTimeMs,_tmpYoutubePlayCount,_tmpSpotifyPlayCount,_tmpYoutubeListenTimeMs,_tmpSpotifyListenTimeMs,_tmpIsWhitelisted,_tmpIsBlacklisted,_tmpAffectionScore);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Song>> getWhitelistedSongs() {
    final String _sql = "SELECT * FROM songs WHERE isWhitelisted = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"songs"}, new Callable<List<Song>>() {
      @Override
      @NonNull
      public List<Song> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final int _cursorIndexOfSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skipCount");
          final int _cursorIndexOfQuickSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "quickSkipCount");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfFirstPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstPlayedAt");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfTotalListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalListenTimeMs");
          final int _cursorIndexOfYoutubePlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlayCount");
          final int _cursorIndexOfSpotifyPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlayCount");
          final int _cursorIndexOfYoutubeListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubeListenTimeMs");
          final int _cursorIndexOfSpotifyListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyListenTimeMs");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsBlacklisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlacklisted");
          final int _cursorIndexOfAffectionScore = CursorUtil.getColumnIndexOrThrow(_cursor, "affectionScore");
          final List<Song> _result = new ArrayList<Song>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Song _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            final int _tmpSkipCount;
            _tmpSkipCount = _cursor.getInt(_cursorIndexOfSkipCount);
            final int _tmpQuickSkipCount;
            _tmpQuickSkipCount = _cursor.getInt(_cursorIndexOfQuickSkipCount);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpFirstPlayedAt;
            _tmpFirstPlayedAt = _cursor.getLong(_cursorIndexOfFirstPlayedAt);
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final long _tmpTotalListenTimeMs;
            _tmpTotalListenTimeMs = _cursor.getLong(_cursorIndexOfTotalListenTimeMs);
            final int _tmpYoutubePlayCount;
            _tmpYoutubePlayCount = _cursor.getInt(_cursorIndexOfYoutubePlayCount);
            final int _tmpSpotifyPlayCount;
            _tmpSpotifyPlayCount = _cursor.getInt(_cursorIndexOfSpotifyPlayCount);
            final long _tmpYoutubeListenTimeMs;
            _tmpYoutubeListenTimeMs = _cursor.getLong(_cursorIndexOfYoutubeListenTimeMs);
            final long _tmpSpotifyListenTimeMs;
            _tmpSpotifyListenTimeMs = _cursor.getLong(_cursorIndexOfSpotifyListenTimeMs);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsBlacklisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlacklisted);
            _tmpIsBlacklisted = _tmp_1 != 0;
            final float _tmpAffectionScore;
            _tmpAffectionScore = _cursor.getFloat(_cursorIndexOfAffectionScore);
            _item = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpPlatform,_tmpPlayCount,_tmpSkipCount,_tmpQuickSkipCount,_tmpLoopsAvoided,_tmpFirstPlayedAt,_tmpLastPlayedAt,_tmpTotalListenTimeMs,_tmpYoutubePlayCount,_tmpSpotifyPlayCount,_tmpYoutubeListenTimeMs,_tmpSpotifyListenTimeMs,_tmpIsWhitelisted,_tmpIsBlacklisted,_tmpAffectionScore);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Song>> getBlacklistedSongs() {
    final String _sql = "SELECT * FROM songs WHERE isBlacklisted = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"songs"}, new Callable<List<Song>>() {
      @Override
      @NonNull
      public List<Song> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final int _cursorIndexOfSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skipCount");
          final int _cursorIndexOfQuickSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "quickSkipCount");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfFirstPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstPlayedAt");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfTotalListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalListenTimeMs");
          final int _cursorIndexOfYoutubePlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlayCount");
          final int _cursorIndexOfSpotifyPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlayCount");
          final int _cursorIndexOfYoutubeListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubeListenTimeMs");
          final int _cursorIndexOfSpotifyListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyListenTimeMs");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsBlacklisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlacklisted");
          final int _cursorIndexOfAffectionScore = CursorUtil.getColumnIndexOrThrow(_cursor, "affectionScore");
          final List<Song> _result = new ArrayList<Song>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Song _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            final int _tmpSkipCount;
            _tmpSkipCount = _cursor.getInt(_cursorIndexOfSkipCount);
            final int _tmpQuickSkipCount;
            _tmpQuickSkipCount = _cursor.getInt(_cursorIndexOfQuickSkipCount);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpFirstPlayedAt;
            _tmpFirstPlayedAt = _cursor.getLong(_cursorIndexOfFirstPlayedAt);
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final long _tmpTotalListenTimeMs;
            _tmpTotalListenTimeMs = _cursor.getLong(_cursorIndexOfTotalListenTimeMs);
            final int _tmpYoutubePlayCount;
            _tmpYoutubePlayCount = _cursor.getInt(_cursorIndexOfYoutubePlayCount);
            final int _tmpSpotifyPlayCount;
            _tmpSpotifyPlayCount = _cursor.getInt(_cursorIndexOfSpotifyPlayCount);
            final long _tmpYoutubeListenTimeMs;
            _tmpYoutubeListenTimeMs = _cursor.getLong(_cursorIndexOfYoutubeListenTimeMs);
            final long _tmpSpotifyListenTimeMs;
            _tmpSpotifyListenTimeMs = _cursor.getLong(_cursorIndexOfSpotifyListenTimeMs);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsBlacklisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlacklisted);
            _tmpIsBlacklisted = _tmp_1 != 0;
            final float _tmpAffectionScore;
            _tmpAffectionScore = _cursor.getFloat(_cursorIndexOfAffectionScore);
            _item = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpPlatform,_tmpPlayCount,_tmpSkipCount,_tmpQuickSkipCount,_tmpLoopsAvoided,_tmpFirstPlayedAt,_tmpLastPlayedAt,_tmpTotalListenTimeMs,_tmpYoutubePlayCount,_tmpSpotifyPlayCount,_tmpYoutubeListenTimeMs,_tmpSpotifyListenTimeMs,_tmpIsWhitelisted,_tmpIsBlacklisted,_tmpAffectionScore);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllSongsList(final Continuation<? super List<Song>> $completion) {
    final String _sql = "SELECT * FROM songs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Song>>() {
      @Override
      @NonNull
      public List<Song> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final int _cursorIndexOfSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skipCount");
          final int _cursorIndexOfQuickSkipCount = CursorUtil.getColumnIndexOrThrow(_cursor, "quickSkipCount");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfFirstPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstPlayedAt");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfTotalListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalListenTimeMs");
          final int _cursorIndexOfYoutubePlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlayCount");
          final int _cursorIndexOfSpotifyPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlayCount");
          final int _cursorIndexOfYoutubeListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubeListenTimeMs");
          final int _cursorIndexOfSpotifyListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyListenTimeMs");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsBlacklisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isBlacklisted");
          final int _cursorIndexOfAffectionScore = CursorUtil.getColumnIndexOrThrow(_cursor, "affectionScore");
          final List<Song> _result = new ArrayList<Song>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Song _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            final int _tmpSkipCount;
            _tmpSkipCount = _cursor.getInt(_cursorIndexOfSkipCount);
            final int _tmpQuickSkipCount;
            _tmpQuickSkipCount = _cursor.getInt(_cursorIndexOfQuickSkipCount);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpFirstPlayedAt;
            _tmpFirstPlayedAt = _cursor.getLong(_cursorIndexOfFirstPlayedAt);
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final long _tmpTotalListenTimeMs;
            _tmpTotalListenTimeMs = _cursor.getLong(_cursorIndexOfTotalListenTimeMs);
            final int _tmpYoutubePlayCount;
            _tmpYoutubePlayCount = _cursor.getInt(_cursorIndexOfYoutubePlayCount);
            final int _tmpSpotifyPlayCount;
            _tmpSpotifyPlayCount = _cursor.getInt(_cursorIndexOfSpotifyPlayCount);
            final long _tmpYoutubeListenTimeMs;
            _tmpYoutubeListenTimeMs = _cursor.getLong(_cursorIndexOfYoutubeListenTimeMs);
            final long _tmpSpotifyListenTimeMs;
            _tmpSpotifyListenTimeMs = _cursor.getLong(_cursorIndexOfSpotifyListenTimeMs);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsBlacklisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBlacklisted);
            _tmpIsBlacklisted = _tmp_1 != 0;
            final float _tmpAffectionScore;
            _tmpAffectionScore = _cursor.getFloat(_cursorIndexOfAffectionScore);
            _item = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpPlatform,_tmpPlayCount,_tmpSkipCount,_tmpQuickSkipCount,_tmpLoopsAvoided,_tmpFirstPlayedAt,_tmpLastPlayedAt,_tmpTotalListenTimeMs,_tmpYoutubePlayCount,_tmpSpotifyPlayCount,_tmpYoutubeListenTimeMs,_tmpSpotifyListenTimeMs,_tmpIsWhitelisted,_tmpIsBlacklisted,_tmpAffectionScore);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getDailyStats(final String date,
      final Continuation<? super DailyStats> $completion) {
    final String _sql = "SELECT * FROM daily_stats WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DailyStats>() {
      @Override
      @Nullable
      public DailyStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSongsDiscovered = CursorUtil.getColumnIndexOrThrow(_cursor, "songsDiscovered");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "listenTimeMs");
          final int _cursorIndexOfYoutubePlays = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlays");
          final int _cursorIndexOfSpotifyPlays = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlays");
          final DailyStats _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            final int _tmpSongsDiscovered;
            _tmpSongsDiscovered = _cursor.getInt(_cursorIndexOfSongsDiscovered);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpListenTimeMs;
            _tmpListenTimeMs = _cursor.getLong(_cursorIndexOfListenTimeMs);
            final int _tmpYoutubePlays;
            _tmpYoutubePlays = _cursor.getInt(_cursorIndexOfYoutubePlays);
            final int _tmpSpotifyPlays;
            _tmpSpotifyPlays = _cursor.getInt(_cursorIndexOfSpotifyPlays);
            _result = new DailyStats(_tmpDate,_tmpSongsDiscovered,_tmpLoopsAvoided,_tmpListenTimeMs,_tmpYoutubePlays,_tmpSpotifyPlays);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRecentDailyStats(final int days,
      final Continuation<? super List<DailyStats>> $completion) {
    final String _sql = "SELECT * FROM daily_stats ORDER BY date DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, days);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DailyStats>>() {
      @Override
      @NonNull
      public List<DailyStats> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSongsDiscovered = CursorUtil.getColumnIndexOrThrow(_cursor, "songsDiscovered");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "listenTimeMs");
          final int _cursorIndexOfYoutubePlays = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlays");
          final int _cursorIndexOfSpotifyPlays = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlays");
          final List<DailyStats> _result = new ArrayList<DailyStats>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyStats _item;
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            final int _tmpSongsDiscovered;
            _tmpSongsDiscovered = _cursor.getInt(_cursorIndexOfSongsDiscovered);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpListenTimeMs;
            _tmpListenTimeMs = _cursor.getLong(_cursorIndexOfListenTimeMs);
            final int _tmpYoutubePlays;
            _tmpYoutubePlays = _cursor.getInt(_cursorIndexOfYoutubePlays);
            final int _tmpSpotifyPlays;
            _tmpSpotifyPlays = _cursor.getInt(_cursorIndexOfSpotifyPlays);
            _item = new DailyStats(_tmpDate,_tmpSongsDiscovered,_tmpLoopsAvoided,_tmpListenTimeMs,_tmpYoutubePlays,_tmpSpotifyPlays);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DailyStats>> getRecentDailyStatsFlow(final int days) {
    final String _sql = "SELECT * FROM daily_stats ORDER BY date DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, days);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"daily_stats"}, new Callable<List<DailyStats>>() {
      @Override
      @NonNull
      public List<DailyStats> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSongsDiscovered = CursorUtil.getColumnIndexOrThrow(_cursor, "songsDiscovered");
          final int _cursorIndexOfLoopsAvoided = CursorUtil.getColumnIndexOrThrow(_cursor, "loopsAvoided");
          final int _cursorIndexOfListenTimeMs = CursorUtil.getColumnIndexOrThrow(_cursor, "listenTimeMs");
          final int _cursorIndexOfYoutubePlays = CursorUtil.getColumnIndexOrThrow(_cursor, "youtubePlays");
          final int _cursorIndexOfSpotifyPlays = CursorUtil.getColumnIndexOrThrow(_cursor, "spotifyPlays");
          final List<DailyStats> _result = new ArrayList<DailyStats>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyStats _item;
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            final int _tmpSongsDiscovered;
            _tmpSongsDiscovered = _cursor.getInt(_cursorIndexOfSongsDiscovered);
            final int _tmpLoopsAvoided;
            _tmpLoopsAvoided = _cursor.getInt(_cursorIndexOfLoopsAvoided);
            final long _tmpListenTimeMs;
            _tmpListenTimeMs = _cursor.getLong(_cursorIndexOfListenTimeMs);
            final int _tmpYoutubePlays;
            _tmpYoutubePlays = _cursor.getInt(_cursorIndexOfYoutubePlays);
            final int _tmpSpotifyPlays;
            _tmpSpotifyPlays = _cursor.getInt(_cursorIndexOfSpotifyPlays);
            _item = new DailyStats(_tmpDate,_tmpSongsDiscovered,_tmpLoopsAvoided,_tmpListenTimeMs,_tmpYoutubePlays,_tmpSpotifyPlays);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRecentSkipHistory(final int limit,
      final Continuation<? super List<SkipHistoryEntry>> $completion) {
    final String _sql = "SELECT * FROM skip_history ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SkipHistoryEntry>>() {
      @Override
      @NonNull
      public List<SkipHistoryEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSongId = CursorUtil.getColumnIndexOrThrow(_cursor, "songId");
          final int _cursorIndexOfSongTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "songTitle");
          final int _cursorIndexOfSongArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "songArtist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<SkipHistoryEntry> _result = new ArrayList<SkipHistoryEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SkipHistoryEntry _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSongId;
            if (_cursor.isNull(_cursorIndexOfSongId)) {
              _tmpSongId = null;
            } else {
              _tmpSongId = _cursor.getString(_cursorIndexOfSongId);
            }
            final String _tmpSongTitle;
            if (_cursor.isNull(_cursorIndexOfSongTitle)) {
              _tmpSongTitle = null;
            } else {
              _tmpSongTitle = _cursor.getString(_cursorIndexOfSongTitle);
            }
            final String _tmpSongArtist;
            if (_cursor.isNull(_cursorIndexOfSongArtist)) {
              _tmpSongArtist = null;
            } else {
              _tmpSongArtist = _cursor.getString(_cursorIndexOfSongArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final String _tmpReason;
            if (_cursor.isNull(_cursorIndexOfReason)) {
              _tmpReason = null;
            } else {
              _tmpReason = _cursor.getString(_cursorIndexOfReason);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new SkipHistoryEntry(_tmpId,_tmpSongId,_tmpSongTitle,_tmpSongArtist,_tmpPlatform,_tmpReason,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllSkipHistory(final Continuation<? super List<SkipHistoryEntry>> $completion) {
    final String _sql = "SELECT * FROM skip_history";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SkipHistoryEntry>>() {
      @Override
      @NonNull
      public List<SkipHistoryEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSongId = CursorUtil.getColumnIndexOrThrow(_cursor, "songId");
          final int _cursorIndexOfSongTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "songTitle");
          final int _cursorIndexOfSongArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "songArtist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<SkipHistoryEntry> _result = new ArrayList<SkipHistoryEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SkipHistoryEntry _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSongId;
            if (_cursor.isNull(_cursorIndexOfSongId)) {
              _tmpSongId = null;
            } else {
              _tmpSongId = _cursor.getString(_cursorIndexOfSongId);
            }
            final String _tmpSongTitle;
            if (_cursor.isNull(_cursorIndexOfSongTitle)) {
              _tmpSongTitle = null;
            } else {
              _tmpSongTitle = _cursor.getString(_cursorIndexOfSongTitle);
            }
            final String _tmpSongArtist;
            if (_cursor.isNull(_cursorIndexOfSongArtist)) {
              _tmpSongArtist = null;
            } else {
              _tmpSongArtist = _cursor.getString(_cursorIndexOfSongArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final String _tmpReason;
            if (_cursor.isNull(_cursorIndexOfReason)) {
              _tmpReason = null;
            } else {
              _tmpReason = _cursor.getString(_cursorIndexOfReason);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new SkipHistoryEntry(_tmpId,_tmpSongId,_tmpSongTitle,_tmpSongArtist,_tmpPlatform,_tmpReason,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SkipHistoryEntry>> getRecentSkipHistoryFlow(final int limit) {
    final String _sql = "SELECT * FROM skip_history ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"skip_history"}, new Callable<List<SkipHistoryEntry>>() {
      @Override
      @NonNull
      public List<SkipHistoryEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSongId = CursorUtil.getColumnIndexOrThrow(_cursor, "songId");
          final int _cursorIndexOfSongTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "songTitle");
          final int _cursorIndexOfSongArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "songArtist");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<SkipHistoryEntry> _result = new ArrayList<SkipHistoryEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SkipHistoryEntry _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSongId;
            if (_cursor.isNull(_cursorIndexOfSongId)) {
              _tmpSongId = null;
            } else {
              _tmpSongId = _cursor.getString(_cursorIndexOfSongId);
            }
            final String _tmpSongTitle;
            if (_cursor.isNull(_cursorIndexOfSongTitle)) {
              _tmpSongTitle = null;
            } else {
              _tmpSongTitle = _cursor.getString(_cursorIndexOfSongTitle);
            }
            final String _tmpSongArtist;
            if (_cursor.isNull(_cursorIndexOfSongArtist)) {
              _tmpSongArtist = null;
            } else {
              _tmpSongArtist = _cursor.getString(_cursorIndexOfSongArtist);
            }
            final String _tmpPlatform;
            if (_cursor.isNull(_cursorIndexOfPlatform)) {
              _tmpPlatform = null;
            } else {
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            }
            final String _tmpReason;
            if (_cursor.isNull(_cursorIndexOfReason)) {
              _tmpReason = null;
            } else {
              _tmpReason = _cursor.getString(_cursorIndexOfReason);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new SkipHistoryEntry(_tmpId,_tmpSongId,_tmpSongTitle,_tmpSongArtist,_tmpPlatform,_tmpReason,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTotalSkipHistoryCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM skip_history";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
