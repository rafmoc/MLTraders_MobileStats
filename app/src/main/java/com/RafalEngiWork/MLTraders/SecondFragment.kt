package com.RafalEngiWork.MLTraders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.RafalEngiWork.MLTraders.databinding.FragmentSecondBinding
import com.RafalEngiWork.MLTraders.regexps.Date
import com.RafalEngiWork.MLTraders.regexps.getDateFromTimestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.atomic.AtomicInteger

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    val database = Firebase.firestore

    private lateinit var mLRunAdapter: MLRunAdapter
    private lateinit var rvFirebaseDataset: RecyclerView

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFirebaseDataset = view.findViewById(R.id.recyclerViewFirebaseDataset)
        rvFirebaseDataset.layoutManager = LinearLayoutManager(context)

        binding.buttonGoBack.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.buttonRefresh.setOnClickListener {
            if(this::mLRunAdapter.isInitialized){
                mLRunAdapter.clearMLRuns()
            }

            firebaseGetDataAndAddToRV()
        }

        firebaseGetDataAndAddToRV()
    }

    private fun firebaseGetDataAndAddToRV() {
        database.collection("uniqCollections").document("List")
            .get()
            .addOnSuccessListener { result ->
                firebaseDataToRV(result)
            }.addOnFailureListener { exception ->
                Log.d("FireLog", "Error getting documents: ", exception)
            }
    }

    private fun firebaseDataToRV(result: DocumentSnapshot) {
        result.data?.let { data ->
            Log.d("FireLog", "Collections: ${result.id} => ${data.keys}")

            var mlRun: MLRun
            var mLRunsData: MLRuns = MLRuns(
                "none",
                mutableListOf()
            )
            val mLRuns: MutableList<MLRun> = mutableListOf<MLRun>()
            val counter: AtomicInteger = AtomicInteger(0)
            data.keys.forEach { collectionName ->
                database.collection(collectionName)
                    .get()
                    .addOnSuccessListener { result ->
                        val listOfAll = listOfAllFireDocuments(result)
                        val mappedData = listOfAll.distinctBy { it.toString() }.map { group ->
                            listOfAll.filter { it.toString() == group.toString() }
                                .sortedBy { it.steps }
                        }

                        Log.d("FireLog", collectionName)
                        for (group in mappedData) {
                            val steps = (group.last().data?.get("True Steps") ?: 1)
                            mlRun = MLRun(
                                collectionName + ":\n" + group.last().toString(),
                                (group.last().steps ?: 1) / steps,
                                steps,
                                group.last().data?.get("Credits Balance")?:0,
                                group.last().data?.get("Earned Credits")?:0,
                                group.last().data?.get("Same Credits")?:0,
                                group.last().data?.get("Lost Credits")?:0
                            )
                            mLRuns.add(mlRun)

                            mLRunsData = MLRuns(
                                collectionName + ":\n" + group.last().toString(),
                                mutableListOf()
                            )
                            group.forEach { mlData ->
                                mlRun = MLRun(
                                    collectionName + ":\n" + mlData.toString(),
                                    (mlData.steps ?: 1) / steps,
                                    mlData.data?.get("True Steps") ?: 1,
                                    mlData.data?.get("Credits Balance")?: 0,
                                    mlData.data?.get("Earned Credits")?: 0,
                                    mlData.data?.get("Same Credits")?: 0,
                                    mlData.data?.get("Lost Credits")?: 0
                                )
                                mLRunsData.mLRuns.add(mlRun)
                            }
                            MLProcessor.addMLRun(mLRunsData)
                        }
                        if(counter.incrementAndGet()==data.keys.size)
                        {
                            mLRunAdapter = MLRunAdapter(mLRuns)
                            rvFirebaseDataset.adapter = mLRunAdapter
                        }

                    }.addOnFailureListener { exception ->
                        Log.d("FireLog", "Error getting documents: ", exception)
                    }
            }
        } ?: run {
            Log.d("FireLog", "Data missing! Data is Null.")
        }
    }

    private fun listOfAllFireDocuments(result: QuerySnapshot): List<Date> {
        return result.map { document ->
            getDateFromTimestamp(document.id, document.data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}