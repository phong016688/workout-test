package com.example.workoutstest.ui.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.workoutstest.R
import com.example.workoutstest.databinding.ItemWorkoutBinding
import com.example.workoutstest.domain.entries.Assignment
import com.example.workoutstest.domain.entries.DayOfWeek
import com.workouts.base_module.utils.clicks
import com.workouts.base_module.utils.gone
import com.workouts.base_module.utils.visible
import java.util.Calendar


data class WorkoutItem(
    val assignments: Assignment? = null,
    val dayOfWeek: DayOfWeek,
    val isShowDay: Boolean = false,
    val isSelected: Boolean = false
)

class WorkoutDiffUtils : DiffUtil.ItemCallback<WorkoutItem>() {
    override fun areItemsTheSame(oldItem: WorkoutItem, newItem: WorkoutItem): Boolean {
        return oldItem.dayOfWeek.day == newItem.dayOfWeek.day && oldItem.assignments?.id == newItem.assignments?.id && oldItem.isSelected == newItem.isSelected
    }

    override fun areContentsTheSame(oldItem: WorkoutItem, newItem: WorkoutItem): Boolean {
        return oldItem == newItem
    }
}

class WorkoutAdapter : ListAdapter<WorkoutItem, WorkoutAdapter.WorkoutViewHolder>(
    WorkoutDiffUtils()
) {
    var itemWorkoutClickListener: ((WorkoutItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val lif = LayoutInflater.from(parent.context)
        val binding = ItemWorkoutBinding.inflate(lif, parent, false)
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WorkoutViewHolder(private val binding: ItemWorkoutBinding) :
        ViewHolder(binding.root) {
        private var itemWorkout: WorkoutItem? = null

        init {
            binding.root.clicks { itemWorkout?.let { itemWorkoutClickListener?.invoke(it) } }
        }

        fun bind(workoutItem: WorkoutItem) {
            this.itemWorkout = workoutItem
            binding.apply {
                handleForDayText(workoutItem)
                if (workoutItem.assignments?.id == null) {
                    hideAssignments()
                } else {
                    handleForContentAssignment(workoutItem.assignments, workoutItem)
                }
                if (adapterPosition != 0 && workoutItem.isShowDay) {
                    separator.visible()
                } else {
                    separator.gone()
                }
            }
        }

        private fun getColor(@ColorRes id: Int): Int {
            return ContextCompat.getColor(itemView.context, id)
        }

        private fun ItemWorkoutBinding.handleForContentAssignment(
            assignments: Assignment,
            workoutItem: WorkoutItem
        ) {
            cardAss.visible()
            itemView.context.apply {
                tvWorkoutName.text = assignments.title
                tvWorkoutName.setTextColor(getColor(R.color.color_1e0a3c))
                tvStatus.setTextColor(getColor(R.color.color_1e0a3c))
                handleTextStatusWithState(workoutItem, this, assignments)
                if (workoutItem.isSelected) {
                    ivTick.visible()
                    cardAss.setCardBackgroundColor(getColor(R.color.color_7470ef))
                    tvWorkoutName.setTextColor(getColor(R.color.white))
                    tvStatus.setTextColor(getColor(R.color.white))
                } else {
                    ivTick.gone()
                    cardAss.setCardBackgroundColor(getColor(R.color.white))
                }
            }
        }

        private fun ItemWorkoutBinding.handleTextStatusWithState(
            workoutItem: WorkoutItem,
            context: Context,
            assignments: Assignment
        ) {
            when (getStateForItem(workoutItem)) {
                State.FUTURE -> {
                    tvWorkoutName.setTextColor(getColor(R.color.color_7b7e91))
                    tvStatus.setTextColor(getColor(R.color.color_7b7e91))
                    tvStatus.text = context.getString(
                        R.string.exercises_no_status,
                        assignments.exercisesCount
                    )
                }

                State.NORMAL -> {
                    tvStatus.setTextColor(getColor(R.color.color_1e0a3c))
                    tvStatus.text = context.getString(
                        R.string.exercises_no_status,
                        assignments.exercisesCount
                    )
                }

                State.COMPLETED -> {
                    tvStatus.setTextColor(getColor(R.color.color_1e0a3c))
                    tvStatus.text = context.getString(R.string.completed)
                }

                State.MISSED -> {
                    val textSpan = SpannableString(
                        context.getString(R.string.exercises_missed, assignments.exercisesCount)
                    )
                    textSpan.setSpan(
                        ForegroundColorSpan(getColor(R.color.color_FF5E5E)),
                        0,
                        context.getString(R.string.missed).length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tvStatus.text = textSpan
                }
            }
        }

        private fun ItemWorkoutBinding.hideAssignments() {
            cardAss.gone()
            tvWorkoutName.text = ""
            tvStatus.text = ""
        }

        private fun ItemWorkoutBinding.handleForDayText(workoutItem: WorkoutItem) {
            if (workoutItem.isShowDay) {
                val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                tvDayName.text = workoutItem.dayOfWeek.name
                tvDay.text = workoutItem.dayOfWeek.day.toString()
                if (workoutItem.dayOfWeek.day == currentDay) {
                    tvDayName.setTextColor(getColor(R.color.color_7470ef))
                    tvDay.setTextColor(getColor(R.color.color_7470ef))
                } else {
                    tvDayName.setTextColor(getColor(R.color.color_7b7e91))
                    tvDay.setTextColor(getColor(R.color.color_1e0a3c))
                }
            } else {
                tvDayName.text = ""
                tvDay.text = ""
            }
        }

        private fun getStateForItem(workoutItem: WorkoutItem): State {
            val calendar = Calendar.getInstance()
            calendar.time = workoutItem.dayOfWeek.date
            val workoutDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
            val status = workoutItem.assignments?.status?.toInt() ?: 0
            return when {
                workoutDayOfYear > Calendar.getInstance().get(Calendar.DAY_OF_YEAR) -> State.FUTURE
                workoutDayOfYear == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) -> State.NORMAL
                status == 0 -> State.MISSED
                status == 2 -> State.COMPLETED
                else -> State.NORMAL
            }
        }
    }

    companion object {
        enum class State {
            MISSED, COMPLETED, FUTURE, NORMAL
        }
    }
}