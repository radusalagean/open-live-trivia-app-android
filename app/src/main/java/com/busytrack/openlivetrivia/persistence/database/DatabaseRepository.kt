package com.busytrack.openlivetrivia.persistence.database

import com.busytrack.openlivetrivia.persistence.database.user.UserEntity
import com.busytrack.openlivetriviainterface.rest.model.UserModel
import io.reactivex.Observable

class DatabaseRepository(
    private val database: AppDatabase
) {

    fun clearUsers() {
        database.userDao().clear()
    }

    fun insertUsers(users: List<UserModel>) {
        database.userDao().insert(users.map {
            UserEntity.fromUserModel(it)
        })
    }

    fun getAllUsers(): Observable<List<UserModel>> =
        Observable.create {
            it.onNext(database.userDao().getAllUsers().map { userEntity ->
                UserEntity.toUserModel(userEntity)
            })
            it.onComplete()
        }
}