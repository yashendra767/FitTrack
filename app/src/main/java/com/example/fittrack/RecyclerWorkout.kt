package com.example.fittrack
import WorkoutViewModelFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import android.view.ViewGroup
import android.widget.Toast
import com.example.fittrack.MyAdapter.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fittrack.log.DatabaseTable
import com.example.fittrack.log.WorkoutViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class RecyclerWorkout : Fragment(), MyAdapter.OnItemClickListener {

    private lateinit var viewModel: WorkoutViewModel
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val view = inflater.inflate(R.layout.fragment_recycler_workout, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = MyAdapter(emptyList(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val factory = WorkoutViewModelFactory(requireActivity().application, uid.toString())
        viewModel = ViewModelProvider(this, factory).get(WorkoutViewModel::class.java)

        viewModel.readAllData.observe(viewLifecycleOwner, Observer{
            databaseTable ->
            adapter.setData(databaseTable)
        })

        return view

    }
    override fun onDeleteButtonClick(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { dialog, _ ->
            val workout = adapter.workoutList[position]
            viewModel.deleteWorkout(workout)
            Toast.makeText(requireContext(), "Deleted item at position: $position", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setTitle("Delete the log")
        builder.setMessage("Are you sure you want to delete?")
        builder.create().show()
    }


}