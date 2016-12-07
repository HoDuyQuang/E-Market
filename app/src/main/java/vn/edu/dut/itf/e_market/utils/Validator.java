package vn.edu.dut.itf.e_market.utils;

public class Validator {
	public static final int MAX_PASSWORD_LENGTH = 20;
	public static final int MIN_PASSWORD_LENGTH = 6;

	public static boolean isValidEmail(CharSequence target) {
		return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}

	public static boolean isValidPhone(CharSequence target) {
		return target != null && android.util.Patterns.PHONE.matcher(target).matches();
	}

	public static boolean isValidEmailOrPhone(CharSequence target) {
		return target != null && (android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches() || android.util.Patterns.PHONE.matcher(target).matches());
	}

	public static boolean isValidPassword(CharSequence password) {
		return password != null && !(password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH);
	}
}
