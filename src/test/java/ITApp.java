import org.example.Pret;
import org.example.Projet;
import org.junit.jupiter.api.*;


public class ITApp {
    @Disabled
    @Test
    public void happyPath(){
        Projet projet = new Projet();
        projet.setPrixHabitation(100_000);
        projet.setRevenuCadastral(700);
        projet.setFraisNotaireAchat(4_150);
        projet.setFraisTransformation(60_000);
        double apportPersonnel = projet.calculApportMinimal();
        double montantEmprunt = projet.calculResteAEmprunter();
        Pret pret = new Pret();
        pret.setFraisDossierBancaire(500);
        pret.setFraisNotaireCredit(5_475);
        pret.setNombreAnnees(15);
        pret.setTauxAnnuel(0.04);
        double apportBancaire = pret.calculRestantDu(montantEmprunt);

        Assertions.assertEquals((apportBancaire + apportPersonnel), 30085 );
    }

    @Test
    public void calculCoutAppartPo(){
        Projet projet = new Projet();
        projet.setPrixHabitation(110_000);
        projet.setRevenuCadastral(1_353);
        projet.setFraisNotaireAchat(28_000);
        projet.setFraisTransformation(10_000);

        double droitEnregistrement = projet.calculDroitEnregistrement();
        double tvaFraisTransformation = projet.calculTVAFraisTransformation();

        Assertions.assertEquals(projet.getPrixHabitation() + projet.getFraisNotaireAchat() +
                droitEnregistrement + projet.getFraisTransformation() + tvaFraisTransformation, 157_350);
    }
}