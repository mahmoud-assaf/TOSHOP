package com.appssquare.mahmoud.myshoppinglist;

/**
 * Created by mahmoud on 27/01/2018.
 */

public class Utils {
    public final static boolean isValidEmail(String email) {
        if (email.trim().equalsIgnoreCase("")) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
        }
    }


    public final static boolean isValidPassword(String password) {
        if (password.equalsIgnoreCase("")||password.length()>20 || password.length()<8) {
            return false;
        } else {
            return true;
        }
    }

    public  final static boolean isMatchPassword(String pass1,String pass2){
        return pass1.equals(pass2);
    }




}
