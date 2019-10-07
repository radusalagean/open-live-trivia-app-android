package com.busytrack.openlivetrivia.persistence.database

import androidx.annotation.CallSuper
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before

/**
 * Base Room DAO Test class, responsible for preparing the in-memory database and clearing
 *  the resources used by it.
 *
 * Make sure to extend all DAO test classes from this one,
 *  and call super for the [setUp] and [tearDown] methods, if you override them.
 */
abstract class DaoTest {

    protected lateinit var db: AppDatabase

    @Before
    @CallSuper
    open fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    @CallSuper
    open fun tearDown() {
        db.close()
    }
}