import com.example.fittrack.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import java.util.*

class WorkoutEntryFragment : Fragment() {

    private lateinit var editTextExerciseName: EditText
    private lateinit var editTextDuration: EditText
    private lateinit var editTextCaloriesBurned: EditText
    private lateinit var textViewDate: TextView
    private lateinit var buttonSelectDate: CardView
    private lateinit var buttonSave: CardView

    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout_entry, container, false)

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

        return view
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            textViewDate.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun saveWorkout() {
        val exerciseName = editTextExerciseName.text.toString()
        val duration = editTextDuration.text.toString()
        val caloriesBurned = editTextCaloriesBurned.text.toString()

        if (exerciseName.isNotEmpty() && duration.isNotEmpty() && caloriesBurned.isNotEmpty() && selectedDate.isNotEmpty()) {
            //koom krenege

            Toast.makeText(requireContext(), "Workout saved successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}