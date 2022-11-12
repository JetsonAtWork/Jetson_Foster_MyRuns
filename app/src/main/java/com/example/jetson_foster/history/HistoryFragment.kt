package com.example.jetson_foster.history

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jetson_foster.R
import com.example.jetson_foster.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {
    val tabTitle = "History"
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private val viewModel: HistoryViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.entryRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val entryListAdapter = EntryListAdapter({ selectedEntry ->
            //onClick
            val intent = Intent(activity,EntryRecordActivity::class.java)
            intent.putExtra("selected_record_id",selectedEntry.id)
            startActivity(intent)
        },requireContext())
        recyclerView.adapter = entryListAdapter
        lifecycle.coroutineScope.launch {
            viewModel.entryList.collect {
                entryListAdapter.submitList(it)
            }
        }
    }
}