package com.amartyasingh.linkmanager.presentation.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.amartyasingh.linkmanager.R
import com.amartyasingh.linkmanager.databinding.FragmentLinksBinding
import com.amartyasingh.linkmanager.presentation.adapters.ViewPagerAdapter
import com.amartyasingh.linkmanager.utils.Resource
import com.amartyasingh.linkmanager.utils.autoCleared
import com.amartyasingh.linkmanager.viewmodels.MainViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LinksFragment : Fragment() {

    private var binding:FragmentLinksBinding by autoCleared()
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private var whatsAppNumber: String = "0"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLinksBinding.inflate(inflater, container, false)

        mainViewModel.fetchLinks()

        binding.run {
            greetingsText.text = mainViewModel.greetings()
        }

        fetchData()

        tabInit()

        chartInit()

        fetchChartData()

        binding.contactWhatsapp.setOnClickListener {
            val uri = Uri.parse("https://wa.me/${whatsAppNumber.toLong()}")
            val intent = Intent(Intent.ACTION_VIEW, uri)

                    startActivity(intent)
        }


        return binding.root
    }

    private fun isAppInstalled(uri: String): Boolean {
        val pm = activity?.packageManager
        return try {
            pm?.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun fetchChartData() {
      mainViewModel.getChartData().observe(viewLifecycleOwner){
          response ->
          when(response.status){
              Resource.Status.SUCCESS -> {
                     response.data?.let {
                        drawChart(it)
                     }
              }

              Resource.Status.ERROR -> TODO()
              Resource.Status.LOADING -> TODO()
          }
      }
    }

    private fun drawChart(overallURLChart: Map<String, Long>) {
        val chartData = ArrayList<Entry>()
        var first = "0"
        var sec = "0"
        overallURLChart.keys.forEachIndexed { index, key ->
            if(index == 0) {
                first = key
            }
            if(index == overallURLChart.size - 1) {
                sec = key
            }
            chartData.add(
                Entry(
                    index.toFloat(),
                    overallURLChart[key]?.toFloat() ?: 0f,
                    key
                )
            )

        }

        val lineDataset = LineDataSet(chartData, "Overview")
        lineDataset.apply {
            setDrawIcons(false)
            setDrawCircles(false)
            setDrawCircleHole(false)
            setDrawValues(false)
            //formLineWidth = 1f
            lineWidth = 3f
            setDrawFilled(true)
            fillDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.gradient_blue)
        }

        lineDataset.color = binding.root.context.getColor(R.color.mainBg)
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(lineDataset)
        binding.apply {
            lineChart.data = LineData(dataSets)
            lineChart.xAxis.valueFormatter = object : IndexAxisValueFormatter() {}
            lineDataset.notifyDataSetChanged()
            lineChart.notifyDataSetChanged()
            lineChart.invalidate();

        }
    }

    private fun chartInit() {
       binding.lineChart.apply {
           setTouchEnabled(true)
           setPinchZoom(true)
           description.isEnabled = false
           axisRight.isEnabled = false
           setScaleEnabled(true)
           isDragEnabled = true
           setDrawGridBackground(true)
           xAxis.position = XAxis.XAxisPosition.BOTTOM
           setGridBackgroundColor(Color.WHITE)
           legend.isEnabled = false
       }
    }

    private fun tabInit() {
        binding.apply {
            linksTab.addTab(
                linksTab.newTab().setText("Top Links")
            )
            linksTab.addTab(
                linksTab.newTab().setText("Recent Links")
            )

            viewPagerAdapter = ViewPagerAdapter(childFragmentManager,lifecycle)
            viewPager.adapter = viewPagerAdapter

            linksTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewPager.currentItem = tab!!.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    linksTab.selectTab(linksTab.getTabAt(position))
                }
            })

        }
    }

    private fun fetchData() {
         mainViewModel.getLinksData().observe(viewLifecycleOwner){ response ->
             when(response.status){
                Resource.Status.SUCCESS -> {
                    response.data?.let {
                        // Handle Success
                         binding.todayClicksText.text = it.today_clicks.toString()
                         binding.topLocationText.text = it.top_location.toString()
                         binding.topSourceText.text = it.top_source.toString()
                         whatsAppNumber = it.support_whatsapp_number.toString()
                         binding.progress.visibility = View.GONE
                    }
                }
                Resource.Status.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()

                }
                Resource.Status.ERROR -> {
                    // Handle Error
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }

             }

         }
    }


}