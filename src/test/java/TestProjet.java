import org.example.Projet;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

public class TestProjet {

    private static Projet mockedProjet;
    private static Projet projet;


    @BeforeAll
    public static void setup() {
        projet = new Projet();
        TestProjet.mockedProjet = Mockito.spy(projet);
    }

    @Disabled
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

    @Disabled
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

    @Disabled
    @Nested
    @DisplayName("Calcul du total euro pour le projet")
    class  calculTotalProjetAchat{
        @Test
        @DisplayName("Calcul du cout total du projet : Validation simple")
        public void calculTotalProjetAchatSimple(){
            Assertions.assertAll(
                    () -> {
                        projet.setPrixHabitation(100_000);
                        projet.setFraisNotaireAchat(28000);
                        projet.setRevenuCadastral(1345);
                        projet.setFraisTransformation(0);
                        Assertions.assertEquals(135500, projet.calculTotalProjetAchat());
                    },
                    () -> {
                        projet.setPrixHabitation(150_000);
                        projet.setFraisNotaireAchat(12000);
                        projet.setRevenuCadastral(200);
                        projet.setFraisTransformation(1000);
                        Assertions.assertEquals(169660, projet.calculTotalProjetAchat());
                    },
                    () -> {
                        projet.setPrixHabitation(40_000);
                        projet.setFraisNotaireAchat(10000);
                        projet.setRevenuCadastral(350);
                        projet.setFraisTransformation(40000);
                        Assertions.assertEquals(92400, projet.calculTotalProjetAchat());
                    },
                    () -> {
                        projet.setPrixHabitation(40_000);
                        projet.setFraisNotaireAchat(10000);
                        projet.setRevenuCadastral(350);
                        projet.setFraisTransformation(37456);
                        Assertions.assertEquals(89703.36, projet.calculTotalProjetAchat());
                    },
                    () -> {
                        projet.setPrixHabitation(400000);
                        projet.setFraisNotaireAchat(10034);
                        projet.setRevenuCadastral(343);
                        projet.setFraisTransformation(37455);
                        Assertions.assertEquals(471736.30, projet.calculTotalProjetAchat());
                    },
                    () -> {
                        projet.setPrixHabitation(0);
                        projet.setFraisNotaireAchat(0);
                        projet.setRevenuCadastral(0);
                        projet.setFraisTransformation(0);
                        Assertions.assertEquals(Exception.class, projet.calculTotalProjetAchat());
                    }
            );
        }

        @Test
        @DisplayName("Calcul du cout total du projet : Validation negative")
        public void calculTotalProjetAchatNegatif(){
            Assertions.assertAll(
                    () -> {
                        projet.setPrixHabitation(-100_000);
                        projet.setFraisNotaireAchat(28000);
                        projet.setRevenuCadastral(1345);
                        projet.setFraisTransformation(0);
                        Assertions.assertEquals(Exception.class, projet.calculTotalProjetAchat());
                    },
                    () -> {
                        projet.setPrixHabitation(150_000);
                        projet.setFraisNotaireAchat(-12000);
                        projet.setRevenuCadastral(200);
                        projet.setFraisTransformation(1000);
                        Assertions.assertEquals(Exception.class, projet.calculTotalProjetAchat());
                    },
                    () -> {
                        projet.setPrixHabitation(40_000);
                        projet.setFraisNotaireAchat(10000);
                        projet.setRevenuCadastral(-350);
                        projet.setFraisTransformation(40000);
                        Assertions.assertEquals(Exception.class, projet.calculTotalProjetAchat());
                    },
                    () -> {
                        projet.setPrixHabitation(40_000);
                        projet.setFraisNotaireAchat(10000);
                        projet.setRevenuCadastral(350);
                        projet.setFraisTransformation(-37456);
                        Assertions.assertEquals(Exception.class, projet.calculTotalProjetAchat());
                    },
                    () -> {
                        projet.setPrixHabitation(-400000);
                        projet.setFraisNotaireAchat(-10034);
                        projet.setRevenuCadastral(-343);
                        projet.setFraisTransformation(-37455);
                        Assertions.assertEquals(Exception.class, projet.calculTotalProjetAchat());
                    }
            );
        }

        @Test
        @DisplayName("Calcul du cout total du projet : Validation nombre réel")
        public void calculTotalProjetAchatNombreReel(){
            Assertions.assertAll(
                    () -> {
                        projet.setPrixHabitation(100_000.15);
                        projet.setFraisNotaireAchat(28000);
                        projet.setRevenuCadastral(1345);
                        projet.setFraisTransformation(0);
                        Assertions.assertEquals(135500.17, projet.calculTotalProjetAchat());
                    },
                    () -> {
                        projet.setPrixHabitation(150_000);
                        projet.setFraisNotaireAchat(12000.90);
                        projet.setRevenuCadastral(200);
                        projet.setFraisTransformation(1000);
                        Assertions.assertEquals(169660.90, projet.calculTotalProjetAchat());
                    },
                    () -> {
                        projet.setPrixHabitation(40_000);
                        projet.setFraisNotaireAchat(10000);
                        projet.setRevenuCadastral(350);
                        projet.setFraisTransformation(40000.32154);
                        Assertions.assertEquals(92400.34, projet.calculTotalProjetAchat());
                    },
                    () -> {
                        projet.setPrixHabitation(40_000.123);
                        projet.setFraisNotaireAchat(10000.123);
                        projet.setRevenuCadastral(350);
                        projet.setFraisTransformation(37456.123);
                        Assertions.assertEquals(89703.74, projet.calculTotalProjetAchat());
                    },
                    () -> {
                        projet.setPrixHabitation(-400000.123);
                        projet.setFraisNotaireAchat(-10034.123);
                        projet.setRevenuCadastral(-343);
                        projet.setFraisTransformation(-37455.123);
                        Assertions.assertEquals(Exception.class, projet.calculTotalProjetAchat());
                    }
            );
        }
    }


    @Nested
    @DisplayName("Calcul de l'apport Minimal pour le projet")
    class calculApportMinimal{
        @Test
        @DisplayName("Calcul de l'apport minimal : Validation simple")
        public void calculApportMinimalSimple(){
            Assertions.assertAll(
                    () -> {
                        projet.setPrixHabitation(100_000);
                        projet.setRevenuCadastral(1345);
                        projet.setFraisTransformation(1000);
                        projet.setFraisNotaireAchat(5000);
                        Assertions.assertEquals(22606, projet.calculApportMinimal());
                    },
                    () -> {
                        projet.setPrixHabitation(100_000);
                        projet.setFraisNotaireAchat(28000);
                        projet.setRevenuCadastral(1345);
                        projet.setFraisTransformation(0);
                        Assertions.assertEquals(45500, projet.calculApportMinimal());
                    },
                    () -> {
                        projet.setPrixHabitation(150_000);
                        projet.setFraisNotaireAchat(12000);
                        projet.setRevenuCadastral(200);
                        projet.setFraisTransformation(1000);
                        Assertions.assertEquals(33706, projet.calculApportMinimal());
                    },
                    () -> {
                        projet.setPrixHabitation(40000);
                        projet.setRevenuCadastral(350);
                        projet.setFraisTransformation(40000);
                        projet.setFraisNotaireAchat(10000);
                        Assertions.assertEquals(18240, projet.calculApportMinimal());
                    },                    () -> {
                        projet.setPrixHabitation(40_000);
                        projet.setFraisNotaireAchat(10000);
                        projet.setRevenuCadastral(350);
                        projet.setFraisTransformation(37456);
                        Assertions.assertEquals(17970.34, projet.calculApportMinimal());
                    },
                    () -> {
                        projet.setPrixHabitation(400_000);
                        projet.setFraisNotaireAchat(10034);
                        projet.setRevenuCadastral(343);
                        projet.setFraisTransformation(37455);
                        Assertions.assertEquals(76004.23, projet.calculApportMinimal());
                    },
                    () -> {
                        projet.setPrixHabitation(0);
                        projet.setFraisNotaireAchat(0);
                        projet.setRevenuCadastral(0);
                        projet.setFraisTransformation(0);
                        Assertions.assertEquals(Exception.class, projet.calculApportMinimal());
                    }
            );
        }

        @Test
        @DisplayName("Calcul de l'apport minimal : Validation valeur négative ")
        public void calculApportMinimalNegatif(){
            Assertions.assertAll(
                    () -> {
                        projet.setPrixHabitation(-100_000);
                        projet.setFraisNotaireAchat(28000);
                        projet.setRevenuCadastral(1345);
                        projet.setFraisTransformation(0);
                        Assertions.assertEquals(Exception.class, projet.calculApportMinimal());
                    },
                    () -> {
                        projet.setPrixHabitation(150_000);
                        projet.setFraisNotaireAchat(-12000);
                        projet.setRevenuCadastral(200);
                        projet.setFraisTransformation(1000);
                        Assertions.assertEquals(Exception.class, projet.calculApportMinimal());
                    },
                    () -> {
                        projet.setPrixHabitation(40_000);
                        projet.setFraisNotaireAchat(10000);
                        projet.setRevenuCadastral(-350);
                        projet.setFraisTransformation(40000);
                        Assertions.assertEquals(Exception.class, projet.calculApportMinimal());
                    },
                    () -> {
                        projet.setPrixHabitation(40_000);
                        projet.setFraisNotaireAchat(10000);
                        projet.setRevenuCadastral(350);
                        projet.setFraisTransformation(-37456);
                        Assertions.assertEquals(Exception.class, projet.calculApportMinimal());
                    },
                    () -> {
                        projet.setPrixHabitation(-400000);
                        projet.setFraisNotaireAchat(-10034);
                        projet.setRevenuCadastral(-343);
                        projet.setFraisTransformation(-37455);
                        Assertions.assertEquals(Exception.class, projet.calculApportMinimal());
                    }
            );
        }

        @Test
        @DisplayName("Calcul de l'apport minimal : Validation nombre réel")
        public void calculApportMinimalNombreReel(){
            Assertions.assertAll(
                    () -> {
                        projet.setPrixHabitation(100_000.15);
                        projet.setFraisNotaireAchat(28000);
                        projet.setRevenuCadastral(1345);
                        projet.setFraisTransformation(0);
                        Assertions.assertEquals(45500.03, projet.calculApportMinimal());
                    },
                    () -> {
                        projet.setPrixHabitation(150_000);
                        projet.setFraisNotaireAchat(12000.90);
                        projet.setRevenuCadastral(200);
                        projet.setFraisTransformation(1000);
                        Assertions.assertEquals(33706.90, projet.calculApportMinimal());
                    },
                    () -> {
                        projet.setPrixHabitation(40_000);
                        projet.setFraisNotaireAchat(10000);
                        projet.setRevenuCadastral(350);
                        projet.setFraisTransformation(40000.32154);
                        Assertions.assertEquals(18240.03, projet.calculApportMinimal());
                    },
                    () -> {
                        projet.setPrixHabitation(40_000.123);
                        projet.setFraisNotaireAchat(10000.123);
                        projet.setRevenuCadastral(350);
                        projet.setFraisTransformation(37456.123);
                        Assertions.assertEquals(17970.49, projet.calculApportMinimal());
                    },
                    () -> {
                        projet.setPrixHabitation(-400000.123);
                        projet.setFraisNotaireAchat(-10034.123);
                        projet.setRevenuCadastral(-343);
                        projet.setFraisTransformation(-37455.123);
                        Assertions.assertEquals(Exception.class, projet.calculApportMinimal());
                    }
            );
        }
    }
}