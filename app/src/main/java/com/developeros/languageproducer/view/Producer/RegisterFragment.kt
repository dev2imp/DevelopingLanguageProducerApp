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
import com.developeros.languageproducer.model.Authenticaiton.RegisterUserModel
import com.developeros.languageproducer.viewmodel.AuthenticaitionViewModel
class RegisterFragment : Fragment(), View.OnClickListener {
    lateinit var LoginButton: Button
    lateinit var RegisterButton: Button
    lateinit var UsernameEditText: EditText
    lateinit var UserEmailEditText: EditText
    lateinit var UserPasswordEditText: EditText
    lateinit var authenticaitionViewModel: AuthenticaitionViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view: View = inflater.inflate(R.layout.fragment_register, container, false)
        LoginButton = view.findViewById(R.id.LoginButton)
        RegisterButton = view.findViewById(R.id.RegisterButton)
        UsernameEditText = view.findViewById(R.id.UserNameEditText)
        UserEmailEditText = view.findViewById(R.id.UserEmailEditText)
        UserPasswordEditText = view.findViewById(R.id.UserPasswordEditText)
        LoginButton.setOnClickListener(this)
        RegisterButton.setOnClickListener(this)
        authenticaitionViewModel =
            AuthenticaitionViewModel(requireActivity().application as Languageproducer)
        authenticaitionViewModel.RegisterState?.observe(requireActivity(), Observer {
            var action = RegisterFragmentDirections.actionRegisterFragmentToProducerFragment()
            findNavController().navigate(action)
        })
        return view
    }

    override fun onClick(v: View) {
        if (v.id == R.id.LoginButton) {
            var action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(action)
        } else if (v.id == R.id.RegisterButton) {
            RegisterUserToFireBase();
        }
    }

    fun RegisterUserToFireBase() {
        var Username = UsernameEditText.text.toString()
        var UserEmail = UserEmailEditText.text.toString()
        var UserPassword = UserPasswordEditText.text.toString()
        var registerUserModel = RegisterUserModel(Username, UserEmail, UserPassword)
        //Pass registerUserModel to Viewmodel to Register user to Firebase
        authenticaitionViewModel.RegisterUserToFirebase(registerUserModel)

    }
}