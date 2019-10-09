package com.busytrack.openlivetrivia.generic.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

/**
 * More info:
 * https://medium.com/@PaulinaSadowska/writing-unit-tests-on-asynchronous-events-with-rxjava-and-rxkotlin-1616a27f69aa
 */

interface BaseSchedulerProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun main(): Scheduler
}

class SchedulerProvider : BaseSchedulerProvider {
    override fun computation(): Scheduler = Schedulers.computation()
    override fun main(): Scheduler = AndroidSchedulers.mainThread()
    override fun io(): Scheduler = Schedulers.io()
}

class TrampolineSchedulerProvider : BaseSchedulerProvider {
    override fun computation(): Scheduler = Schedulers.trampoline()
    override fun main(): Scheduler = Schedulers.trampoline()
    override fun io(): Scheduler = Schedulers.trampoline()
}

class TestSchedulerProvider(private val scheduler: TestScheduler) : BaseSchedulerProvider {
    override fun computation(): TestScheduler = scheduler
    override fun main(): TestScheduler = scheduler
    override fun io(): TestScheduler = scheduler
}