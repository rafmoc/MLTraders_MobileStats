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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    val database = Firebase.firestore

    private lateinit var mLRunAdapter: MLRunAdapter
    private lateinit var rvFirebaseDataset: RecyclerView
    private lateinit var mLRunsList: ArrayList<MLRun>

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        database.collection("uniqCollections").document("List").get()
            .addOnSuccessListener { result ->
                Log.d("FireLog", "${result.id} => ${result.data?.keys}")
                binding.textviewSecond.text = result.data?.keys.toString();
            }.addOnFailureListener { exception ->
                Log.d("FireLog", "Error getting documents: ", exception)
            }

        rvFirebaseDataset = view.findViewById(R.id.recyclerViewFirebaseDataset)
        rvFirebaseDataset.layoutManager = LinearLayoutManager(context)

        val mlRun = MLRun("Test", 5, 10, 15, 20, 25, 30)
        val mLRuns : MutableList<MLRun> = mutableListOf<MLRun>()

        mLRuns.add(mlRun)

        mLRunAdapter = MLRunAdapter(mLRuns)
        rvFirebaseDataset.adapter = mLRunAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}