package mukesh.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatVM() : ViewModel() {
    var selectedOption:MutableLiveData<Int> = MutableLiveData(1)
    var selectedModeMonth:MutableLiveData<Int> = MutableLiveData(0)
}