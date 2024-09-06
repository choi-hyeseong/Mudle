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

    private var callback: ActivityCallback? = null
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as ActivityCallback
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // 회원가입 토스트 발생
        Toast.makeText(context, getString(R.string.register), Toast.LENGTH_SHORT).show()
        // 프래그먼트용 바인딩 호출
        val binding = DataBindingUtil.inflate<RegisterMainBinding>(inflater, R.layout.register_main, container, false)
        initObserver(binding)
        return binding.root
    }

    private fun initObserver(binding: RegisterMainBinding) {
        binding.viewModel = registerViewModel.apply {
            validLiveData.observe(viewLifecycleOwner) {
                binding.error.text = it
                binding.error.visibility = View.VISIBLE
            }
            binding.button.setOnClickListener {
                val name = binding.name.text.toString()
                register(name)
            }
            responseLiveData.observe(viewLifecycleOwner) { isSuccess ->
                //response toast
                if (isSuccess) callback?.switchMain()
                else Toast.makeText(requireContext(), "회원가입에 실패하였습니다. 이름을 바꿔보거나 잠시후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }


        }
    }
}