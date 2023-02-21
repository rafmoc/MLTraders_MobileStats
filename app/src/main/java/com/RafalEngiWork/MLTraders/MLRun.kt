package com.RafalEngiWork.MLTraders

import java.util.concurrent.ConcurrentLinkedQueue

data class MLRun(
    val name: String?,
    val iterations: Int?,
    val steps: Int?,
    val creditBalance: Int?,
    val earnedCredits: Int?,
    val sameCredits: Int?,
    val lostCredits: Int?
)

data class MLRuns(
    var name: String,
    var mLRuns: MutableList<MLRun>
)

object MLProcessor{
    val mLObjects:  ConcurrentLinkedQueue<MLRuns> = ConcurrentLinkedQueue<MLRuns>()
    fun addMLRun(mLRuns: MLRuns){
        mLObjects.add(mLRuns)
    }
}