package helper;

public class DatumHelper {

	public static int readJahr() {
		int jahr = IO.readInt("Jahr: ");
		while ((!istSchaltjahr(jahr) || istSchaltjahr(jahr)) && !istJahr(jahr)) {
			System.out.printf("Das Jahr %4d gibt es nicht.\n", jahr);
			jahr = IO.readInt("Jahr: ");
		}
		return jahr;
	}

	public static int readMonat() {
		int monat = IO.readInt("Monat: ");
		while (!istMonat(monat)) {
			System.out.printf("Der Monat %02d gibt es nicht.\n", monat);
			monat = IO.readInt("Monat: ");
		}
		return monat;
	}

	public static int readTag(int jahr, int monat) {
		int tag = IO.readInt("Tag: ");
		while (!istTag(jahr, monat, tag)) {
			System.out.printf("Den Tag %02d gibt es nicht.\n", tag);
			tag = IO.readInt("Tag: ");
		}
		return tag;
	}

	public static boolean istSchaltjahr(int jahr) {
		return ((jahr % 4 == 0 && jahr % 100 != 0) || (jahr % 400 == 0));
	}

	public static boolean istJahr(int jahr) {
		return (jahr != 0);
	}

	public static boolean istMonat(int monat) {
		return monat >= 1 && monat <= 12;
	}

	public static boolean istTag(int jahr, int monat, int tag) {
		int maxtage = 31;
		return tag >= 1 && tag <= maxtage ? monat == 4 || monat == 7 || monat == 9 || monat == 11 ? tag <= maxtage - 1
				: monat == 2 ? (istSchaltjahr(jahr) ? tag <= maxtage - 2 : tag <= maxtage - 3) : true : false;
	}

	public static String asDE(int dtag, int dmonat, int djahr) {
		String dedat = "";
		if (dtag < 10) {
			dedat = "0" + dtag;
		} else {
			dedat = "" + dtag;
		}
		dedat += ".";
		if (dmonat < 10) {
			dedat += "0" + dmonat;
		} else {
			dedat += dmonat;
		}
		dedat += ".";
		int deneg = 0;
		if (djahr < 0) {
			deneg = -1;
		}
		djahr = Math.abs(djahr);
		if (djahr < 10) {
			dedat += "   " + djahr;
		} else if (djahr < 100) {
			dedat += "  " + djahr;
		} else if (djahr < 1000) {
			dedat += " " + djahr;
		} else if (djahr < 10000) {
			dedat += djahr;
		} else {
			dedat += djahr + " Jahreszahl zu groß! Prüfe auf Fehler!";
		}
		if (deneg < 0) {
			dedat += " v. Chr.";
		}
		return dedat;
	}

	public static String asUS(int usmonat, int ustag, int usjahr) {
		String usdat = "";
		int usneg = 0;
		if (usjahr < 0) {
			usneg = -1;
		}
		if (usmonat < 10) {
			usdat = "0" + usmonat;
		} else {
			usdat = "" + usmonat;
		}
		usdat += "/";
		if (ustag < 10) {
			usdat += "0" + ustag;
		} else {
			usdat += ustag;
		}
		usdat += "/";
		usjahr = Math.abs(usjahr);
		if (usjahr < 10) {
			usdat += "   " + usjahr;
		} else if (usjahr < 100) {
			usdat += "  " + usjahr;
		} else if (usjahr < 1000) {
			usdat += " " + usjahr;
		} else if (usjahr < 10000) {
			usdat += usjahr;
		} else {
			usdat += usjahr + "\nJahreszahl zu groß! Prüfe auf Fehler!";
		}
		if (usneg != 0) {
			usdat += " BC";
		}
		return usdat;
	}

	public static String asISO(int isojahr, int isomonat, int isotag) {
		String isodat = "\t";
		if (isojahr < 0) {
			isodat += "-";
		} else {
			isodat += " ";
		}
		isojahr = Math.abs(isojahr);
		if (isojahr < 10) {
			isodat += "   " + isojahr;
		} else if (isojahr < 100) {
			isodat += "  " + isojahr;
		} else if (isojahr < 1000) {
			isodat += " " + isojahr;
		} else if (isojahr < 10000) {
			isodat += isojahr;
		} else {
			isodat += isojahr + "\nJahreszahl zu groß! Prüfe auf Fehler!";
		}
		isodat += "-";
		if (isomonat < 10) {
			isodat += "0" + isomonat;
		} else {
			isodat += "" + isomonat;
		}
		isodat += "-";
		if (isotag < 10) {
			isodat += "0" + isotag;
		} else {
			isodat += isotag;
		}

		return isodat;
	}
}
// vor Christus v. Chr, BC, -