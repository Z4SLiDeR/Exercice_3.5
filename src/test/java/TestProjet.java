import org.example.Projet;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

@Disabled
public class TestProjet {

    private static Projet mockedProjet;
    private static Projet projet;

    @BeforeAll
    public static void setup() {
        projet = new Projet();
        TestProjet.mockedProjet = Mockito.spy(projet);
    }

    @Nested
    class calculTVAFraisTransformation {
        @Test
        @DisplayName("Calcul de la tva sur les frais de transformation: Validation simple")
        public void calculTVAFraisTransformationSimple() {
            // Test de valeurs simples, le test doit les vérifier toutes
            Assertions.assertAll(
                    () -> {
                        projet.setFraisTransformation(90_000.00);
                        Assertions.assertEquals(5_400.00, projet.calculTVAFraisTransformation(), 0.001);
                    },
                    () -> {
                        projet.setFraisTransformation(0.00);
                        Assertions.assertEquals(0.00, projet.calculTVAFraisTransformation(),0.001);
                    },
                    () -> {
                        projet.setFraisTransformation(100_000.00);
                        Assertions.assertEquals(6_000.00, projet.calculTVAFraisTransformation(),0.001);
                    },
                    () -> {
                        projet.setFraisTransformation(59_595.00);
                        Assertions.assertEquals(3_575.70, projet.calculTVAFraisTransformation(),0.001);
                    },
                    () -> {
                        projet.setFraisTransformation(1.00);
                        Assertions.assertEquals(0.06, projet.calculTVAFraisTransformation(),0.001);
                    }
            );
        }


        @Test
        @DisplayName("Calcul de la tva sur les frais de transformation: Validation des frais arrondi")
        public void calculTVAFraisTransformationArrondi() {
            // Test de probleme d'arrondis
            projet.setFraisTransformation(92_123.89);
            Assertions.assertEquals(5_527.44, projet.calculTVAFraisTransformation());
        }

        @ParameterizedTest
        @ValueSource(doubles = {-90_000.00, -25_000.00})
        @DisplayName("Calcul de la tva sur les frais de transformation: Validation des frais negatifs")
        public void calculTVAFraisTransformationNegatif(double fraisTransformation) {
            // Test de valeurs negatives
            projet.setFraisTransformation(fraisTransformation);
            Assertions.assertThrows(Exception.class, () -> projet.calculTVAFraisTransformation());
        }
    }

    @Nested
    @DisplayName("Calcul du droit d'enregistrement")
    class calculDroitEnregistrement {
        @Test
        public void calculDroitEnregistrementRevenuCadastralInferieur745() {
            Assertions.assertAll(() -> {
                mockedProjet.setPrixHabitation(350_000.00);
                Mockito.doReturn(40_000.00).when(mockedProjet).calculAbattement();
                mockedProjet.setRevenuCadastral(740);
                Assertions.assertEquals(18_600.00, mockedProjet.calculDroitEnregistrement());
                }
            );
        }
    }
}
