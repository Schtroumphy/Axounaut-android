package com.jeanloth.project.android.kotlin.axounaut.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jeanloth.project.android.kotlin.axounaut.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.cvCommands.onClick {
            goToCommands()
        }

        binding.cvAnalysis.onClick {
            goToAnalysis()
        }

        binding.cvStock.onClick {
            goToStock()
        }

        binding.cvArticles.onClick {
            goToArticle()
        }

        binding.cvClients.onClick {
            goToClientFragment()
        }*/
    }

    private fun goToCommands() {
        findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavCommandList())
    }

    private fun goToAnalysis() {
        findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavAnalysis())

    }

    private fun goToStock() {
        findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavStock())
    }

    private fun goToClientFragment() {
        findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavClient())
    }

    private fun goToArticle() {
        findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavArticle())
    }



}