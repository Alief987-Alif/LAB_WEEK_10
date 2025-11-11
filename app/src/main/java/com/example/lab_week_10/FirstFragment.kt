package com.example.lab_week_10

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab_week_10.viewmodels.TotalViewModel

class FirstFragment : Fragment() {

    private lateinit var viewModel: TotalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Shared ViewModel antara Activity & Fragment
        viewModel = ViewModelProvider(requireActivity())[TotalViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set teks awal agar tidak ada jeda tampilan
        updateText(viewModel.total.value ?: 0)

        // Observe LiveData dengan lifecycle Fragment
        viewModel.total.observe(viewLifecycleOwner) { t ->
            updateText(t)
        }
    }

    private fun updateText(total: Int) {
        view?.findViewById<TextView>(R.id.text_total)?.text =
            getString(R.string.text_total, total)
    }

    companion object {
        fun newInstance(): FirstFragment = FirstFragment()
    }
}
