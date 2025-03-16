import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.decompose.ComponentContext
import com.stevdza_san.demo.data.PostSDK
import com.stevdza_san.demo.domain.Postt
import com.stevdza_san.demo.domain.RequestState
import com.stevdza_san.demo.room.Post
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


// ✅ تعريف ViewModel مع Decompose
class MainViewModel(
    private val sdk: PostSDK, // يفترض أن يكون لديك كلاس SDK لجلب البيانات
    componentContext: ComponentContext
) : ComponentContext by componentContext, InstanceKeeper.Instance {



    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // ✅ الاحتفاظ بالحالة عبر MutableStateFlow
    private val _allPosts = MutableStateFlow<RequestState<List<Post>>>(RequestState.Idle)
    val allPosts: StateFlow<RequestState<List<Post>>> = _allPosts

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        scope.launch {
            _allPosts.value = RequestState.Idle

            sdk.getAllPosts().collect { requestState ->
                _allPosts.value = requestState
            }
        }
    }

    override fun onDestroy() {
        scope.cancel() // إيقاف الكوروتينات عند تدمير الـ ViewModel
    }
}
