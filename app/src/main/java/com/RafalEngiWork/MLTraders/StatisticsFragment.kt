package com.RafalEngiWork.MLTraders

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.RafalEngiWork.MLTraders.databinding.FragmentStatisticsBinding
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null

    private val binding get() = _binding!!

    lateinit var lineGraphView: GraphView
    lateinit var lineGraphView2: GraphView
    private var logNumber: Int = 0
    private var maxLogNumber: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameOfData: String? = arguments?.getString("Name")
        var mLRuns: MLRuns? = MLProcessor.mLObjects.firstOrNull { ml ->
            ml.name == nameOfData
        }

        if(mLRuns != null)
        {
            maxLogNumber = (mLRuns?.mLRuns?.size?.minus(1))?:0
            logNumber = maxLogNumber
            binding.currentLogNumber.text = "Log nr: ${logNumber}"

            binding.logMinus.setOnClickListener {
                logNumber = logNumber.dec()
                if(logNumber < 0) { logNumber = 0 }
                binding.currentLogNumber.text = "Log nr: ${logNumber}"
                setGeneralInformations(nameOfData, mLRuns)
            }
            binding.logPlus.setOnClickListener {
                logNumber = logNumber.inc()
                if(logNumber > maxLogNumber) { logNumber = maxLogNumber }
                binding.currentLogNumber.text = "Log nr: ${logNumber}"
                setGeneralInformations(nameOfData, mLRuns)
            }

            setGeneralInformations(nameOfData, mLRuns)
            setEarningsAndLosesChart(mLRuns)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setGeneralInformations(
        nameOfData: String?, mLRuns: MLRuns?
    ) {
        mLRuns?.mLRuns!![logNumber].let { runNumber ->
            binding.mLRunTopText.text = nameOfData
            binding.statsView1Value.text = "${runNumber.steps}"
            binding.statsView2Value.text = "${runNumber.earnedCredits + runNumber.lostCredits + runNumber.sameCredits}"
            binding.statsView3Value.text = "${runNumber.creditBalance}"
            binding.statsView4Value.text = "${runNumber.earnedCredits}"
            binding.statsView5Value.text = "${runNumber.lostCredits}"
            binding.statsView6Value.text = "${runNumber.sameCredits}"
        }
    }

    private fun setEarningsAndLosesChart(mLRuns: MLRuns?) {
        lineGraphView = binding.idGraphView
        lineGraphView2 = binding.idGraphView2

        val earnedPoints: Array<DataPoint>? = mLRuns?.mLRuns?.mapIndexed { index, mlRun ->
            intToDataPoint(
                (index + 1), mlRun.earnedCredits
            )
        }?.toTypedArray()

        val lostPoints: Array<DataPoint>? = mLRuns?.mLRuns?.mapIndexed { index, mlRun ->
            intToDataPoint(
                (index + 1), mlRun.lostCredits
            )
        }?.toTypedArray()

        val creditsBalancePoints: Array<DataPoint>? = mLRuns?.mLRuns?.mapIndexed { index, mlRun ->
            intToDataPoint(
                (index + 1), normalizeToHundred(mlRun.creditBalance,
                    mLRuns.mLRuns.minOf { it.creditBalance },
                    mLRuns.mLRuns.maxOf { it.creditBalance }).toInt()
            )
        }?.toTypedArray()

        val earnSeries: LineGraphSeries<DataPoint> = LineGraphSeries(earnedPoints)
        earnSeries.color = resources.getColor(R.color.primary_700)

        val lostSeries: LineGraphSeries<DataPoint> = LineGraphSeries(lostPoints)
        lostSeries.color = resources.getColor(R.color.complementary_700)

        val creditsBalanceSeries: LineGraphSeries<DataPoint> = LineGraphSeries(creditsBalancePoints)
        creditsBalanceSeries.color = resources.getColor(R.color.complementary_700)

        lineGraphView.addSeries(earnSeries)
        lineGraphView.addSeries(lostSeries)
        lineGraphView2.addSeries(creditsBalanceSeries)

        lineGraphView.viewport.backgroundColor = resources.getColor(R.color.complementary_200)
        lineGraphView2.viewport.backgroundColor = resources.getColor(R.color.primary_200)

        lineGraphView.viewport.isScalable = true
        lineGraphView2.viewport.isScalable = true

        /*
            lineGraphView.viewport.isScrollable = true
            lineGraphView.viewport.setScrollableY(true)
            lineGraphView.animate()
            lineGraphView.viewport.setScalableY(true)
        */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun normalizeToHundred(value: Int, min: Int, max: Int): Float {
        if (max < 0)
            return (((value - min) / (max - min).toFloat()) * 100) - 150
        return (((value - min) / (max - min).toFloat()) * 100) - 50
    }

    private fun intToDataPoint(a: Int, b: Int): DataPoint {
        return DataPoint(a.toDouble(), b.toDouble())
    }
}