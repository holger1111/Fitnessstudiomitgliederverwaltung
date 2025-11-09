package Backend.Manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RolleManager {

    // Map<RoleID, Set<ErlaubteAktionen>>
    private final Map<Integer, Set<String>> rollenRechte = new HashMap<>();

    public RolleManager() {
        // Rechte für Trainer (Rollen-ID 1)
        Set<String> trainerRechte = new HashSet<>();
        trainerRechte.add("KURSE_EIGENE_SEHEN");
        trainerRechte.add("KURSTERMIN_EIGENE_SEHEN");
        trainerRechte.add("KURSTERMIN_POTENZIAL_SEHEN");
        trainerRechte.add("TEILNEHMERLISTE_EIGENE_SEHEN");
        rollenRechte.put(1, trainerRechte);

        // Rechte für Mitarbeiter (Rollen-ID 2)
        Set<String> mitarbeiterRechte = new HashSet<>(trainerRechte);
        mitarbeiterRechte.add("KURSE_ALLE_SEHEN");
        mitarbeiterRechte.add("SUCHEN");
        mitarbeiterRechte.add("KURSTERMIN_ALLE_SEHEN");
        mitarbeiterRechte.add("KURS_ANMELDEN");
        mitarbeiterRechte.add("KURS_ABMELDEN");
        mitarbeiterRechte.add("TEILNEHMERLISTE_ANZEIGEN");
        mitarbeiterRechte.add("VOLLSTÄNDIGE_SUCHFUNKTION");
        mitarbeiterRechte.add("INTERESSENTEN_ANLEGEN");
        mitarbeiterRechte.add("ARTIKEL_VERKAUFEN");
        rollenRechte.put(2, mitarbeiterRechte);

        // Rechte für Verkäufer (Rollen-ID 3)
        Set<String> verkaeuferRechte = new HashSet<>(mitarbeiterRechte);
        verkaeuferRechte.add("NEUKUNDEN_ANLEGEN");
        verkaeuferRechte.add("VERTRAEGE_ABSCHLIESSEN");
        rollenRechte.put(3, verkaeuferRechte);

        // Rechte für Verwaltung (Rollen-ID 4)
        Set<String> verwaltungRechte = new HashSet<>(verkaeuferRechte);
        verwaltungRechte.add("MITARBEITER_ANLEGEN");
        verwaltungRechte.add("KURS_ANLEGEN");
        verwaltungRechte.add("KURSTERMIN_ANLEGEN");
        verwaltungRechte.add("INTERVALL_ANLEGEN");
        verwaltungRechte.add("VERTRAG_ANLEGEN");
        verwaltungRechte.add("ARTIKEL_ANLEGEN");
        verwaltungRechte.add("KATEGORIE_ANLEGEN");
        rollenRechte.put(4, verwaltungRechte);

        // Rechte für Admin (Rollen-ID 5)
        Set<String> adminRechte = new HashSet<>(verwaltungRechte);
        adminRechte.add("ALLE_RECHTE");
        rollenRechte.put(5, adminRechte);
    }

    // Prüfen, ob Rolle eine Aktion darf
    public boolean hatRecht(int rolleId, String aktion) {
        Set<String> rechte = rollenRechte.get(rolleId);
        return rechte != null && rechte.contains(aktion);
    }

    // Optional: weitere Methoden wie alle Rechte einer Rolle zurückgeben etc.
}
