package familyapp.foodsharing

import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import familyapp.foodsharing.databinding.FargotPasswordDialogBinding
import familyapp.foodsharing.util.Validate

class ForgotPasswordDialog: DialogFragment() {

    private var _binding: FargotPasswordDialogBinding? = null
    private val binding get() = _binding!!
    companion object{
        val validate = Validate
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FargotPasswordDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWidthPercent(90)
        val auth = FirebaseAuth.getInstance()
        binding.buttonForgot.setOnClickListener {
            view.hideKeyboard()
            val email = binding.editTextForgotEmail.text.toString()
            val animatedVectorDrawable = binding.tickMark.drawable as AnimatedVectorDrawable
            if (validate.validateEmail(email, requireContext())){
                auth.sendPasswordResetEmail(email).addOnSuccessListener {
                    binding.buttonForgot.visibility = View.INVISIBLE
                    binding.editTextForgotEmail.visibility = View.INVISIBLE
                    binding.textViewForgotPassword.visibility = View.INVISIBLE
                    binding.tickMark.visibility = View.VISIBLE
                    animatedVectorDrawable.start()
                    val longestAnimationTime = 3000
                    binding.tickMark.postDelayed({
                            dialog?.dismiss()
                        },
                        longestAnimationTime.toLong()
                    )
                    Toast.makeText(requireContext(), R.string.password_send, Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    if (it.message=="There is no user record corresponding to this identifier. The user may have been deleted."){
                        Toast.makeText(requireContext(), R.string.email_not_found, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.imageViewCloseDialog.setOnClickListener {
            binding.editTextForgotEmail.text.clear()
            dismiss()
        }
    }



    fun DialogFragment.setWidthPercent(percentage: Int) {
        val percent = percentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun DialogFragment.setFullScreen() {
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        binding.editTextForgotEmail.text.clear()
    }
}