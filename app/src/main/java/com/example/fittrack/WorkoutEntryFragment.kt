import com.example.fittrack.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.fittrack.log.WorkoutViewModel
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fittrack.log.DatabaseTable
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class WorkoutEntryFragment : Fragment() {

    private lateinit var editTextExerciseName: AutoCompleteTextView
    private lateinit var editTextDuration: EditText
    private lateinit var editTextCaloriesBurned: EditText
    private lateinit var textViewDate: TextView
    private lateinit var buttonSelectDate: CardView
    private lateinit var buttonSave: CardView
    private lateinit var viewModel: WorkoutViewModel
    private var selectedDate: Calendar? = null
    private var uid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout_entry, container, false)


        //viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)
        uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {

            viewModel = ViewModelProvider(
                this,
                WorkoutViewModelFactory(
                    requireActivity().application,
                    uid!!
                )
            ).get(WorkoutViewModel::class.java)
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return view
        }

        editTextExerciseName = view.findViewById(R.id.editTextExerciseName)
        editTextDuration = view.findViewById(R.id.editTextDuration)
        editTextCaloriesBurned = view.findViewById(R.id.editTextCaloriesBurned)
        textViewDate = view.findViewById(R.id.textViewDate)
        buttonSelectDate = view.findViewById(R.id.btnDate)
        buttonSave = view.findViewById(R.id.btnSave)

        buttonSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        buttonSave.setOnClickListener {
            saveWorkout()
        }

        autoCompleteTextView()

        return view
    }
    private fun autoCompleteTextView(){
        viewModel.readAllData.observe(viewLifecycleOwner, Observer{
            databaseTable ->
            val exerciseName = databaseTable.map { it.exName }.distinct()
            val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_autocomplete, exerciseName)
            editTextExerciseName.setAdapter(adapter)
        })
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                textViewDate.text = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun saveWorkout() {
        val exerciseName = editTextExerciseName.text.toString()
        val duration = editTextDuration.text.toString()
        val caloriesBurned = editTextCaloriesBurned.text.toString()

        if (exerciseName.isNotEmpty() && duration.isNotEmpty() && caloriesBurned.isNotEmpty() && selectedDate != null) {
            val exDuration = duration.toIntOrNull()
            val exCaloriesBurned = caloriesBurned.toIntOrNull()
            val currentDate = Calendar.getInstance()

            if (selectedDate!!.after(currentDate)) {
                Toast.makeText(requireContext(), "Cannot add future workouts", Toast.LENGTH_SHORT).show()
                return
            }

            if (exDuration != null && exCaloriesBurned != null) {
                val dateFormatted = "${selectedDate!!.get(Calendar.DAY_OF_MONTH)}/${selectedDate!!.get(Calendar.MONTH) + 1}/${selectedDate!!.get(Calendar.YEAR)}"
                val entry = DatabaseTable(exerciseName, exDuration, exCaloriesBurned, dateFormatted, uid = uid!!)
                viewModel.insertWorkout(entry)
                Toast.makeText(requireContext(), "Workout saved successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Invalid duration or calories", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
