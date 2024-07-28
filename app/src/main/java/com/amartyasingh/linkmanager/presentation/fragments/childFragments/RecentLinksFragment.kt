package com.amartyasingh.linkmanager.presentation.fragments.childFragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amartyasingh.linkmanager.MainActivity
import com.amartyasingh.linkmanager.data.models.LinksModel
import com.amartyasingh.linkmanager.databinding.FragmentRecentLinksBinding
import com.amartyasingh.linkmanager.presentation.adapters.LinksAdapter
import com.amartyasingh.linkmanager.utils.Resource
import com.amartyasingh.linkmanager.utils.autoCleared
import com.amartyasingh.linkmanager.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentLinksFragment : Fragment() {

    private var binding: FragmentRecentLinksBinding by autoCleared()
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var linksAdapter: LinksAdapter

    private var listData: List<LinksModel> = listOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentLinksBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.fetchLinks()

        linksAdapter = LinksAdapter{
            link ->
            context?.let { context ->
                val myClipboard: ClipboardManager = (activity as MainActivity).getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("link", link)
                myClipboard.setPrimaryClip(clipData)
                Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_LONG).show()
            }

        }

        binding.apply {
            rvRecent.adapter = linksAdapter
            rvRecent.layoutManager = LinearLayoutManager(activity)

        }

        binding.viewAllLinksCard.setOnClickListener {
            linksAdapter.differ.submitList(listData)
            linksAdapter.notifyDataSetChanged()
        }


        getRecentLink()

    }

    private fun getRecentLink() {
        mainViewModel.getRecentLinks().observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let {data ->
                        listData = data
                        val endSize = if(data.size >= 4) 4 else data.size
                        linksAdapter.differ.submitList(data.subList(0, endSize))
                        linksAdapter.notifyDataSetChanged()
                        binding.rvRecent.adapter = linksAdapter
                        binding.rvRecent.layoutManager = LinearLayoutManager(activity)
                        binding.progress.visibility = View.GONE
                    }
                }

                Resource.Status.ERROR ->  {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    binding.progress.visibility = View.GONE
                }
                Resource.Status.LOADING -> {
                     binding.progress.visibility = View.VISIBLE
                }
            }
        }
    }


}