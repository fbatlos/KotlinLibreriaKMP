import androidx.navigation.NavHostController
import org.example.projects.NavController.AppNavigator


class AndroidNavigator(
    private val navController: NavHostController
) : AppNavigator {
    override fun navigateTo(destination: String) {
        navController.navigate(destination)
    }

    override fun goBack() {
        navController.popBackStack()
    }
}