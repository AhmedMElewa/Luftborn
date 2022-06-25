package com.elewa.luftborn.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.elewa.luftborn.R
import com.elewa.luftborn.data.model.TaskModel
import com.elewa.luftborn.databinding.ActivityMainBinding
import com.elewa.luftborn.service.ForegroundService
import com.elewa.luftborn.util.Constants.getDate
import com.elewa.luftborn.util.Constants.setSafeOnClickListener
import com.elewa.luftborn.util.Status
import com.jakewharton.rxbinding2.view.RxView
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TaskAdapter

    private var taskList = ArrayList<TaskModel>()
    private var taskMap = HashMap<Int,TaskModel>()


    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Luftborn)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        init()
        observer()
    }

    private fun init() {
        adapter = TaskAdapter()
        binding.recyclerTasks.adapter = adapter


        binding.btnTask1.setSafeOnClickListener  {
            val task = TaskModel(1,"Task 1",getDate())
            runTask(task)

        }

        binding.btnTask2.setSafeOnClickListener  {
            val task = TaskModel(2,"Task 2",getDate())
            runTask(task)

        }

        binding.btnTask3.setSafeOnClickListener  {
            val task = TaskModel(3,"Task 3",getDate())
            runTask(task)

        }

        binding.btnTask4.setSafeOnClickListener  {
            val task = TaskModel(4,"Task 4",getDate())
            runTask(task)

        }
    }


    private fun observer() {


        viewModel.taskList.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data.isNullOrEmpty()) {
                        Toast.makeText(this, "No Tasks Added", Toast.LENGTH_LONG).show()
                    } else {
                        adapter.apply {
                            taskMap.clear()
                            for (i in it.data) {
                                if (i.status > 0 && i.status < 100) {
                                    taskMap.put(i.id, i)
                                }
                            }
                        }
                    }
                }
                Status.LOADING -> {
//                    binding.recyclerTasks.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun runTask(task: TaskModel){
        var currentTask = taskMap.get(task.id)
        if (currentTask!=null && currentTask.status<100 && currentTask.status>0){
            Toast.makeText(this,task.name+" still running ("+ currentTask.status+")%",Toast.LENGTH_SHORT).show()
        }else if(currentTask==null){
            taskMap.put(task.id,task)
            taskList.add(task)
            val tobeSavedTasks = ArrayList<TaskModel>()
            for (i in taskMap){
                tobeSavedTasks.add(i.value)
            }
            viewModel.saveTasksInDB(tobeSavedTasks)
            ForegroundService.startService(this, task.name+" is running...",task.id)
        }else{
            taskList.add(task)
            currentTask?.status = 0
            val tobeSavedTasks = ArrayList<TaskModel>()
            for (i in taskMap){
                tobeSavedTasks.add(i.value)
            }
            viewModel.saveTasksInDB(tobeSavedTasks)
            ForegroundService.startService(this, task.name+" is running...",task.id)
        }
        adapter.apply {
            adapter.submitList(taskList)
            adapter.notifyDataSetChanged()
            binding.recyclerTasks.scrollToPosition(taskList.size - 1)
        }
    }


}