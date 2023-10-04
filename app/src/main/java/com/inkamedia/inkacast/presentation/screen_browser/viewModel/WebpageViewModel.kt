package com.inkamedia.inkacast.presentation.screen_browser.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.inkacast.gdrivevideoplayer.repository.Webpage
import com.inkamedia.inkacast.data.remote.localDataSource.repository.WebpageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class WebpageViewModel @Inject constructor(
    private val repository: WebpageRepository
) : ViewModel() {

    val readData = repository.readWebPage().asLiveData()

    fun createPage(webpage: Webpage){
        viewModelScope.launch(Dispatchers.IO) {
            repository.createWebPage(webpage)
        }
    }

    fun deletePage(webpage: Webpage){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWebPage(webpage)
        }
    }



    fun countByUrl(webpage: Webpage): LiveData<Boolean> {
        val resultLiveData = MutableLiveData<Boolean>()

        viewModelScope.launch(Dispatchers.IO) {
            val count = repository.countByUrl(webpage)
            resultLiveData.postValue(count > 0)
        }

        return resultLiveData
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Webpage>> {
        return repository.searchWebPage(searchQuery).asLiveData()
    }

}