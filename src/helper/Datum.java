package helper;

public class Datum {

	public static void main(String[] args) {
		Datum dat = new Datum(1, 3, 2);
		Datum dat2 = new Datum(2024, 2, 29);
		Datum dat3 = new Datum(-9283, 12, 3);
		System.out.println("-------------------");
		System.out.println("asDE:\t\t" + DatumHelper.asDE(dat.tag, dat.monat, dat.jahr));
		System.out.println("asDE:\t\t" + DatumHelper.asDE(dat2.tag, dat2.monat, dat2.jahr));
		System.out.println("asDE:\t\t" + DatumHelper.asDE(dat3.tag, dat3.monat, dat3.jahr));
		System.out.println("-------------------");
		System.out.println("asUS:\t\t" + DatumHelper.asUS(dat.monat, dat.tag, dat.jahr));
		System.out.println("asUS:\t\t" + DatumHelper.asUS(dat2.monat, dat2.tag, dat2.jahr));
		System.out.println("asUS:\t\t" + DatumHelper.asUS(dat3.monat, dat3.tag, dat3.jahr));
		System.out.println("-------------------");
		System.out.println("asISO:\t" + DatumHelper.asISO(dat.jahr, dat.monat, dat.tag));
		System.out.println("asISO:\t" + DatumHelper.asISO(dat2.jahr, dat2.monat, dat2.tag));
		System.out.println("asISO:\t" + DatumHelper.asISO(dat3.jahr, dat3.monat, dat3.tag));
		System.out.println("-------------------");

	}

	// Attribute
	private int tag;
	private int monat;
	private int jahr;

	// Konstruktoren
	public Datum(int j, int m, int t) {
		jahr = j;
		monat = m;
		tag = t;
//		gibErgebniss();
	}

	public Datum() {
		jahr = setJahr();
		monat = setMonat();
		tag = setTag();
	}

	// Setter und Getter
	public int setTag() {
		tag = DatumHelper.readTag(jahr, monat);
		System.out.printf("Der Tag wurde auf %02d geändert.\n", tag);
//		gibErgebniss();
		return tag;
	}

	public int setTag(int tag) {
		this.tag = tag;
		System.out.printf("Der Tag wurde auf %02d geändert.\n", tag);
//		gibErgebniss();
		return tag;
	}

	public int setMonat() {
		monat = DatumHelper.readMonat();
		System.out.printf("Der Monat wurde auf %02d geändert.\n", monat);
//		gibErgebniss();
		return monat;
	}

	public int setMonat(int monat) {
		this.monat = monat;
		System.out.printf("Der Monat wurde auf %02d geändert.\n", monat);
//		gibErgebniss();
		return monat;
	}

	public int setJahr() {
		jahr = DatumHelper.readJahr();
		System.out.printf("Das Jahr wurde auf %4d geändert.\n", jahr);
//		gibErgebniss();
		return jahr;
	}

	public int setJahr(int jahr) {
		this.jahr = jahr;
		System.out.printf("Das Jahr wurde auf %4d geändert.\n", jahr);
//		gibErgebniss();
		return jahr;
	}

	public int getTag() {
		return tag;
	}

	public int getMonat() {
		return monat;
	}

	public int getJahr() {
		return jahr;
	}

	public String getDatum() {
		return String.format("%02d.%02d.%4d", tag, monat, jahr);
	}

	// Methoden

	public static boolean testeDatum(int tag, int monat, int jahr) {
		
		return DatumHelper.istJahr(jahr) && DatumHelper.istMonat(monat) && DatumHelper.istTag(jahr, monat, tag);
	}

	public String asDE() {
		return String.format("%02d.%02d.%4d", tag, monat, jahr);
	}

	public void printDatumDE() {
		System.out.println(asDE());
	}

	public String asUS() {
		return String.format("%02d/%02d/%4d", monat, tag, jahr);
	}

	public void printDatumUS() {
		System.out.println(asUS());
	}

	public String asISO() {
		return String.format("%4d-%02d-%02d", jahr, monat, tag);
	}

	public void printDatumISO() {
		System.out.println(asISO());
	}
//
//	public void gibErgebniss() {
//		if (testeDatum()) {
//			System.out.printf("Das Datum %s existiert.\n", asDE());
//		} else {
//			System.out.printf("Das Datum %s existiert nicht!\n", asDE());
//		}
//	}
}
