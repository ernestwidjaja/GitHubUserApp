package com.example.githubuserapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.data.response.ItemsItem
import com.example.githubuserapp.databinding.FragmentFollowBinding
import com.example.githubuserapp.ui.detail.DetailViewModel


class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private var position: Int? = null
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvListUserFollow.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvListUserFollow.addItemDecoration(itemDecoration)

        viewModel.userFollowers.observe(viewLifecycleOwner) { user ->
            setDetailUserFollow(user)
        }

        viewModel.userFollowing.observe(viewLifecycleOwner) { user ->
            setDetailUserFollow(user)
        }
        if (savedInstanceState == null) {
            viewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        if (position == 1) {
            viewModel.findUserFollowers(username.toString())
        } else {
            viewModel.findUserFollowing(username.toString())
        }
    }

    private fun setDetailUserFollow(user: List<ItemsItem>){
        val adapter = ListUserAdapter()
        adapter.submitList(user)
        binding.rvListUserFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}