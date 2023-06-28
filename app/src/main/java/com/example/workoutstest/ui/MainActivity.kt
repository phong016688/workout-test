package com.example.workoutstest.ui

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutstest.R
import com.example.workoutstest.databinding.ActivityMainBinding
import com.example.workoutstest.domain.entries.Workout
import com.example.workoutstest.ui.adapter.WorkoutAdapter
import com.example.workoutstest.ui.adapter.WorkoutItem
import com.example.workoutstest.utils.DateUtils
import com.workouts.base_module.presentation.BaseActivity
import com.workouts.base_module.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main) {
    override val binding by viewBindings(ActivityMainBinding::bind)
    override val viewModel by viewModels<MainViewModel>()
    private val workoutAdapter by lazy { WorkoutAdapter() }

    override fun setupViews() {
        binding.rvWorkout.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            itemAnimator = null
            adapter = workoutAdapter
            workoutAdapter.submitList(emptyList())
        }
        workoutAdapter.itemWorkoutClickListener = { selected ->
            val newWorkoutList = workoutAdapter.currentList.map {
                if (selected.assignments?.id != null && it.assignments?.id == selected.assignments.id)
                    it.copy(isSelected = !it.isSelected)
                else
                    it
            }
            workoutAdapter.submitList(newWorkoutList)
        }
    }

    override fun bindViewModel() {
        viewModel.workoutState
            .onEach { updateData(it) }
            .launchIn(lifecycleScope)
    }

    private fun updateData(listWorkout: List<Workout>) {
        val allDay = DateUtils.getCurrentWeekDays()
        val workoutItem = allDay.mapIndexed { index, day ->
            val assignments = listWorkout.getOrNull(index)?.assignments
            assignments?.mapIndexed { i, ass ->
                WorkoutItem(ass, day, i == 0, false)
            }?.takeIf { it.isNotEmpty() } ?: listOf(WorkoutItem(dayOfWeek = day, isShowDay = true))
        }.flatten()
        workoutAdapter.submitList(workoutItem)
    }
}