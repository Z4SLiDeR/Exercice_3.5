import org.example.Pret;
import org.example.Projet;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

@DisplayName("Tests d'int´egration: ensemble des composants")
public class ITApp {

    @Test
    @DisplayName("Validation du meilleur scénario")
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
}
