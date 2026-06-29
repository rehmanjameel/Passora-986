package com.ko.keyora10.adapter

import android.graphics.BlurMaskFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ko.keyora10.databinding.ItemPasswordBinding
import com.ko.keyora10.models.PasswordEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PasswordAdapter(

    private val onViewClick: (PasswordEntity) -> Unit,
    private val onCopyClick: (PasswordEntity) -> Unit,
    private val onEditClick: (PasswordEntity) -> Unit

) : ListAdapter<PasswordEntity, PasswordAdapter.PasswordViewHolder>(
    DiffCallback()
) {

    inner class PasswordViewHolder(
        val binding:
        ItemPasswordBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PasswordViewHolder {

        val binding = ItemPasswordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PasswordViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: PasswordViewHolder,
        position: Int
    ) {

        val item = getItem(position)

        with(holder.binding) {

            txtWebsite.text = item.website

            txtUsername.text = item.username

            // Always blur password
//            txtPassword.text = "••••••••••"
            holder.binding.txtPassword.text = item.password

            blurText(holder.binding.txtPassword)
            chipCategory.text = item.category

            txtDate.text = formatDate(item.updatedAt)

            btnView.setOnClickListener {

                onViewClick(item)


            }

            btnCopy.setOnClickListener {

                onCopyClick(item)

            }

            btnEdit.setOnClickListener {

                onEditClick(item)

            }

        }

    }

    private fun blurText(textView: TextView) {

        textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        textView.paint.maskFilter =
            BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)
    }

    private fun clearBlur(textView: TextView) {

        textView.paint.maskFilter = null

        textView.invalidate()
    }

    /**
     * Format Date
     */
    private fun formatDate(time: Long): String {

        val sdf = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        )

        return sdf.format(Date(time))

    }

    companion object {

        class DiffCallback : DiffUtil.ItemCallback<PasswordEntity>() {

            override fun areItemsTheSame(
                oldItem: PasswordEntity,
                newItem: PasswordEntity
            ): Boolean {

                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(
                oldItem: PasswordEntity,
                newItem: PasswordEntity
            ): Boolean {

                return oldItem == newItem

            }

        }

    }

}