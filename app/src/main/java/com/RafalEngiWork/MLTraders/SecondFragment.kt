package com.RafalEngiWork.MLTraders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.RafalEngiWork.MLTraders.databinding.FragmentSecondBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    val database = Firebase.firestore

    // This property is only valid between onCreateView and
    // onDestroyView.
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
            }.addOnFailureListener { exception ->
                Log.d("FireLog", "Error getting documents: ", exception)
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}