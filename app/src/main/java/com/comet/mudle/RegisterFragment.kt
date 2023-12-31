package com.comet.mudle

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.comet.mudle.callback.ActivityCallback
import com.comet.mudle.databinding.RegisterMainBinding
import com.comet.mudle.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var callback : ActivityCallback? = null
    private val registerViewModel : RegisterViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as ActivityCallback
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Toast.makeText(context, getString(R.string.register), Toast.LENGTH_SHORT).show()
        val binding = DataBindingUtil.inflate<RegisterMainBinding>(inflater, R.layout.register_main, container, false)
        //api 형태로 해서 파라미터 안받게 하기 TODO
        binding.viewModel = registerViewModel.apply {
            validLiveData.observe(viewLifecycleOwner) {
                binding.error.text = it
                binding.error.visibility = View.VISIBLE
            }
            binding.button.setOnClickListener {
                val name = binding.name.text.toString()
                //viewmodel에 생성자 붙여놓고 nullable..
                register(name)?.observe(viewLifecycleOwner)  { isSuccess ->
                    if (isSuccess)
                        callback?.switchMain()
                }
            }
            responseLiveData.observe(viewLifecycleOwner) {
                //response toast
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }


        }

        return binding.root
    }
}