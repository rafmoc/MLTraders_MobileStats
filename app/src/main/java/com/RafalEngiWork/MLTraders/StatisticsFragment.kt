package com.RafalEngiWork.MLTraders

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import com.RafalEngiWork.MLTraders.databinding.FragmentStatisticsBinding
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null

    private val binding get() = _binding!!

    lateinit var lineGraphView: GraphView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameOfData: String? = arguments?.getString("Name")
        var mLRuns: MLRuns? = MLProcessor.mLObjects.firstOrNull { ml ->
            ml.name == nameOfData
        }

        binding.mLRunTopText.text = nameOfData
        binding.statsView1.text = mLRuns?.mLRuns?.last()?.steps.toString()
        binding.statsView2.text = mLRuns?.mLRuns?.last()?.creditBalance.toString()
        binding.statsView3.text = mLRuns?.mLRuns?.last()?.earnedCredits.toString()
        binding.statsView4.text = mLRuns?.mLRuns?.last()?.lostCredits.toString()

        lineGraphView = binding.idGraphView

        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                // on below line we are adding
                // each point on our x and y axis.
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 3.0),
                DataPoint(2.0, 4.0),
                DataPoint(3.0, 9.0),
                DataPoint(4.0, 6.0),
                DataPoint(5.0, 3.0),
                DataPoint(6.0, 6.0),
                DataPoint(7.0, 1.0),
                DataPoint(8.0, 2.0)
            )
        )

        lineGraphView.animate()

        // on below line we are setting scrollable
        // for point graph view
        lineGraphView.viewport.isScrollable = true
        lineGraphView.viewport.isScalable = true
        lineGraphView.viewport.setScalableY(true)
        lineGraphView.viewport.setScrollableY(true)

        //series.color = R.color.white

        lineGraphView.addSeries(series)
        lineGraphView.viewport.backgroundColor = R.color.complementary_200

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}