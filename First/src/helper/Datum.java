package helper;

public class Datum {

	// Attribute
	
	private int tag;
	private int monat;
	private int jahr;

	// Konstruktoren
	
	public Datum(int j, int m, int t) {
		jahr = j;
		monat = m;
		tag = t;
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
		return tag;
	}

	public int setTag(int tag) {
		this.tag = tag;
		System.out.printf("Der Tag wurde auf %02d geändert.\n", tag);
		return tag;
	}

	public int setMonat() {
		monat = DatumHelper.readMonat();
		System.out.printf("Der Monat wurde auf %02d geändert.\n", monat);
		return monat;
	}

	public int setMonat(int monat) {
		this.monat = monat;
		System.out.printf("Der Monat wurde auf %02d geändert.\n", monat);
		return monat;
	}

	public int setJahr() {
		jahr = DatumHelper.readJahr();
		System.out.printf("Das Jahr wurde auf %4d geändert.\n", jahr);
		return jahr;
	}

	public int setJahr(int jahr) {
		this.jahr = jahr;
		System.out.printf("Das Jahr wurde auf %4d geändert.\n", jahr);
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
	
	