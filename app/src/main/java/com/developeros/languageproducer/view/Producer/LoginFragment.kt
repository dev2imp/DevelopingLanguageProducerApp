package com.developeros.languageproducer.view.Producer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.developeros.languageproducer.Applicaiton.Languageproducer
import com.developeros.languageproducer.R
import com.developeros.languageproducer.model.Authenticaiton.LoginUserModel
import com.developeros.languageproducer.viewmodel.AuthenticaitionViewModel

class LoginFragment : Fragment(), View.OnClickListener {
    lateinit var authenticaitionViewModel: AuthenticaitionViewModel
    lateinit var RegisterButton: Button
    lateinit var LoginButton: Button
    lateinit var UserEmailEditText: EditText
    lateinit var UserPasswordEditText: EditText
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view: View = inflater.inflate(R.layout.fragment_login, container, false)
        RegisterButton = view.findViewById(R.id.RegisterButton)
        LoginButton = view.findViewById(R.id.LoginButton)
        UserEmailEditText = view.findViewById(R.id.UserEmailEditText)
        UserPasswordEditText = view.findViewById(R.id.UserPasswordEditText)
        RegisterButton.setOnClickListener(this)
        LoginButton.setOnClickListener(this)
        authenticaitionViewModel =
            AuthenticaitionViewModel(requireActivity().application as Languageproducer)
        authenticaitionViewModel.LoginState!!.observe(requireActivity(), Observer {
            val action = LoginFragmentDirections.actionLoginFragmentToProducerFragment()
            findNavController().navigate(action)
        })
        return view
    }

    override fun onClick(v: View) {
        if (v.id == R.id.RegisterButton) {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        } else if (v.id == R.id.LoginButton) {
            LoginUser()
        }
    }

    fun LoginUser() {
        var UserEmail = UserEmailEditText.text.toString()
        var UserPassword = UserPasswordEditText.text.toString()
        var loginUserModel = LoginUserModel(UserEmail, UserPassword)
        authenticaitionViewModel.LoginUser(loginUserModel)
    }
}