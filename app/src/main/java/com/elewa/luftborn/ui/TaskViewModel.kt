package com.elewa.luftborn.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elewa.luftborn.data.model.TaskModel
import com.elewa.luftborn.data.repository.MainRepository
import com.elewa.luftborn.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    private val _taskList = MutableLiveData<Resource<List<TaskModel>>>()
    val taskList: LiveData<Resource<List<TaskModel>>>
        get() = _taskList


    init {
        fetchTasks()
    }


    fun fetchTasks() {
        _taskList.postValue(Resource.loading(null))
            viewModelScope.launch {
                repository.getTasks().catch { e->
                    _taskList.value = Resource.error("No Tasks Added", emptyList())
                }.collect{
                    if (it.isNotEmpty()){
                        _taskList.value = Resource.success(it)
                    }else{
                        _taskList.value = Resource.error("No Tasks Added", emptyList())
                    }
                }
            }
    }

    fun saveTasksInDB(tasks: List<TaskModel>) {
        viewModelScope.launch {
            repository.insert(tasks)
            fetchTasks()
        }
    }


}