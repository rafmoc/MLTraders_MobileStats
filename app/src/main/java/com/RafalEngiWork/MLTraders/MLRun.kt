package com.RafalEngiWork.MLTraders

data class MLRun(
    val name: String,
    val iterations: Int,
    val steps: Int,
    val creditBalance: Int,
    val earnedCredits: Int,
    val sameCredits: Int,
    val lostCredits: Int
)