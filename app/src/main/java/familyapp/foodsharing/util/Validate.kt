package familyapp.foodsharing.util

import android.content.Context
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import familyapp.foodsharing.R

object Validate {
    fun validateLogin(email:String, password:String, context: Context): Boolean{
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()&&password.length>=6){
            return true
        } else if(email.isEmpty()){
            Toast.makeText(context, R.string.email_empty, Toast.LENGTH_LONG).show()
            return false
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(context, R.string.email_not_valid, Toast.LENGTH_LONG).show()
            return false
        } else if(password.isEmpty()){
            Toast.makeText(context, R.string.password_empty, Toast.LENGTH_LONG).show()
            return false
        } else if(password.length<6){
            Toast.makeText(context, R.string.password_not_valid, Toast.LENGTH_LONG).show()
            return false
        }
        return false
    }

    fun validateRegistration(email: String, password: String, repassword: String, name: String, context: Context):Boolean{
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()&&password.length>=6&&name.isNotEmpty()&&repassword.length>=6){
            return true
        } else if(email.isEmpty()){
            Toast.makeText(context, R.string.email_empty, Toast.LENGTH_LONG).show()
            return false
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(context, R.string.email_not_valid, Toast.LENGTH_LONG).show()
            return false
        } else if(password.isEmpty()){
            Toast.makeText(context, R.string.password_empty, Toast.LENGTH_LONG).show()
            return false
        } else if(password.length<6){
            Toast.makeText(context, R.string.password_not_valid, Toast.LENGTH_LONG).show()
            return false
        } else if(repassword!=password){
            Toast.makeText(context, R.string.password_repassword_mismatch, Toast.LENGTH_LONG).show()
            return false
        } else if(name.isEmpty()){
            Toast.makeText(context, R.string.name_empty, Toast.LENGTH_LONG).show()
            return false
        }
        return false
    }

    fun validateEmail(email: String, context: Context): Boolean{
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true
        } else if(email.isEmpty()){
            Toast.makeText(context, R.string.email_empty, Toast.LENGTH_LONG).show()
            return false
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(context, R.string.email_not_valid, Toast.LENGTH_LONG).show()
            return false
        }
        return false
    }
}