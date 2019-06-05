package dev.projects.argus;

public class Utils {

	public static boolean isCpfValid(String cpf) {
		if(cpf.equals("00000000000")||cpf.equals("11111111111")||cpf.equals("22222222222")||cpf.equals("33333333333")||cpf.equals("44444444444")||cpf.equals("55555555555")||cpf.equals("66666666666")||cpf.equals("77777777777")||cpf.equals("88888888888")||cpf.equals("99999999999"))
			return false;
		int[] digits = new int[11];
		int mult1 = 0, mult2 = 0, secdigit1 = 0, secdigit2 = 0, j = 10;

		for (int i = 1; i < 12; i++) {
			digits[i - 1] = Integer.valueOf(cpf.substring((i - 1), i));
		}

		for (int i = 0; i <= 9; i++) {
			if (j >= 2) {
				mult1 += digits[i] * j;
			}
			j--;
		}

		secdigit1 = (mult1 * 10) % 11;
		if (String.valueOf(secdigit1).equals(String.valueOf(digits[9]))) {
			j = 11;
			for (int i = 0; i <= 9; i++) {
				if (j >= 2) {
					mult2 += digits[i] * j;
				}
				j--;
			}
			secdigit2 = (mult2 * 10) % 11;
			if (String.valueOf(secdigit2).equals(String.valueOf(digits[10]))) {
				return true;
			}
		}

		return false;
	}
	
}
