package com.bonc.tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Arith {
	public static final int SCALE = 2;

	public static double add(double v1, double v2, int scale) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return round(b1.add(b2).doubleValue(), scale);
	}

	public static double sub(double v1, double v2, int scale) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return round(b1.subtract(b2).doubleValue(), scale);
	}

	public static double mul(double v1, double v2, int scale) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return round(b1.multiply(b2).doubleValue(), scale);
	}

	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		BigDecimal b3 = b1.divide(b2, scale, 4);
		return b1.divide(b2, scale, 4).doubleValue();
	}

	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, 4).doubleValue();
	}

	public static double trunc(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b1.divide(one, scale, BigDecimal.ROUND_FLOOR).doubleValue();
	}

	public static long ceil(long v1, long v2) {
		if (v2 == 0) {
			throw new IllegalArgumentException(
					"The v2 must be not zero");
		}
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, 0, BigDecimal.ROUND_CEILING).longValue();
	}

	public static boolean equal(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal V1 = new BigDecimal(Double.toString(v1)).divide(
				new BigDecimal(1), scale, 4);
		BigDecimal V2 = new BigDecimal(Double.toString(v2)).divide(
				new BigDecimal(1), scale, 4);

		return V1.equals(V2);
	}

	public static int compareTo(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal V1 = new BigDecimal(Double.toString(v1)).divide(
				new BigDecimal(1), scale, 4);
		BigDecimal V2 = new BigDecimal(Double.toString(v2)).divide(
				new BigDecimal(1), scale, 4);

		return V1.compareTo(V2);
	}

	public static String getDecimalFormatNum(double number, int len,
			boolean isdf) {
		String result = String.valueOf(number);
		boolean isf = false;
		if (number < 0.0D) {
			number = Math.abs(number);
			isf = true;
		}

		String df = "###,###,###,###,##0";
		if (!isdf) {
			df = df.replaceAll(",", "");
		}
		if (len > 0) {
			df = df + ".";
			for (int i = 0; i < len; i++) {
				df = df + "0";
			}
		}
		DecimalFormat dfNew = new DecimalFormat(df);
		result = dfNew.format(number);

		if (isf) {
			result = "-" + result;
		}
		return result;
	}
}
