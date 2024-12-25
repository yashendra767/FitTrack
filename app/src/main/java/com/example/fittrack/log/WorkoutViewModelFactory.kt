import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fittrack.log.WorkoutViewModel

class WorkoutViewModelFactory(
    private val application: Application,
    private val uid: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            return WorkoutViewModel(application, uid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
