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

    /*------------------------------------------------------------------------------------------------------------------
    * ------------------------------------------Devoir à partir d'ici---------------------------------------------------
    * ------------------------------------------------------------------------------------------------------------------ */

    
    @Nested
    @DisplayName("Calcul du total en euro pour le projet")
    class CalculTotalProjetAchatTests {

        @Test
        @DisplayName("Validation des cas simples")
        public void calculTotalProjetAchatSimple() {
            Assertions.assertAll(
                    () -> {
                        mockedProjet.setPrixHabitation(100_000);
                        mockedProjet.setFraisNotaireAchat(28_000);
                        mockedProjet.setRevenuCadastral(1345);
                        mockedProjet.setFraisTransformation(0);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(7_500.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(0.00);

                        Assertions.assertEquals(135_500.00, mockedProjet.calculTotalProjetAchat());
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(150_000);
                        mockedProjet.setFraisNotaireAchat(12_000);
                        mockedProjet.setRevenuCadastral(200);
                        mockedProjet.setFraisTransformation(1_000);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(6_600.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(60.00);

                        Assertions.assertEquals(169_660.00, mockedProjet.calculTotalProjetAchat());
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(40_000);
                        mockedProjet.setFraisNotaireAchat(10_000);
                        mockedProjet.setRevenuCadastral(350);
                        mockedProjet.setFraisTransformation(40_000);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(0.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(2_400.00);

                        Assertions.assertEquals(92_400.00, mockedProjet.calculTotalProjetAchat());
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(40_000);
                        mockedProjet.setFraisNotaireAchat(10_000);
                        mockedProjet.setRevenuCadastral(350);
                        mockedProjet.setFraisTransformation(37_456);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(0.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(2_247.36);

                        Assertions.assertEquals(89_703.36, mockedProjet.calculTotalProjetAchat());
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(400_000);
                        mockedProjet.setFraisNotaireAchat(10_034);
                        mockedProjet.setRevenuCadastral(343);
                        mockedProjet.setFraisTransformation(37_455);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(33_333.33);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(22_000.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(2_247.30);

                        Assertions.assertEquals(471_736.30, mockedProjet.calculTotalProjetAchat());
                    }
            );
        }



        @Test
        @DisplayName("Validation des cas négatifs")
        public void calculTotalProjetAchatNegatif() {
            Assertions.assertAll(
                    () -> {
                        mockedProjet.setPrixHabitation(-100_000);
                        mockedProjet.setFraisNotaireAchat(28_000);
                        mockedProjet.setRevenuCadastral(1345);
                        mockedProjet.setFraisTransformation(0);

                        Mockito.when(mockedProjet.calculAbattement()).thenThrow(Exception.class);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenThrow(Exception.class);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(0.00);

                        Assertions.assertThrows(Exception.class, mockedProjet::calculTotalProjetAchat);
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(150_000);
                        mockedProjet.setFraisNotaireAchat(-12_000);
                        mockedProjet.setRevenuCadastral(200);
                        mockedProjet.setFraisTransformation(1_000);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(6_600.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(60.00);

                        Assertions.assertThrows(Exception.class, mockedProjet::calculTotalProjetAchat);
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(40_000);
                        mockedProjet.setFraisNotaireAchat(10_000);
                        mockedProjet.setRevenuCadastral(-350);
                        mockedProjet.setFraisTransformation(40_000);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenThrow(Exception.class);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(2_400.00);

                        Assertions.assertThrows(Exception.class, mockedProjet::calculTotalProjetAchat);
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(40_000);
                        mockedProjet.setFraisNotaireAchat(10_000);
                        mockedProjet.setRevenuCadastral(350);
                        mockedProjet.setFraisTransformation(-37_456);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(0.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenThrow(Exception.class);

                        Assertions.assertThrows(Exception.class, mockedProjet::calculTotalProjetAchat);
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(-400_000);
                        mockedProjet.setFraisNotaireAchat(-10_034);
                        mockedProjet.setRevenuCadastral(-343);
                        mockedProjet.setFraisTransformation(-37_455);

                        Mockito.when(mockedProjet.calculAbattement()).thenThrow(Exception.class);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenThrow(Exception.class);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenThrow(Exception.class);

                        Assertions.assertThrows(Exception.class, mockedProjet::calculTotalProjetAchat);
                    }
            );
        }


        @Test
        @DisplayName("Validation des nombres réels")
        public void calculTotalProjetAchatNombreReel() {
            Assertions.assertAll(
                    () -> {
                        mockedProjet.setPrixHabitation(100_000.15);
                        mockedProjet.setFraisNotaireAchat(28_000);
                        mockedProjet.setRevenuCadastral(1345);
                        mockedProjet.setFraisTransformation(0);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(7_500.02);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(0.00);

                        Assertions.assertEquals(135_500.17, mockedProjet.calculTotalProjetAchat());
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(150_000);
                        mockedProjet.setFraisNotaireAchat(12_000.90);
                        mockedProjet.setRevenuCadastral(200);
                        mockedProjet.setFraisTransformation(1_000);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(6_600.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(60.00);

                        Assertions.assertEquals(169_660.00, mockedProjet.calculTotalProjetAchat());
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(40_000);
                        mockedProjet.setFraisNotaireAchat(10_000);
                        mockedProjet.setRevenuCadastral(350);
                        mockedProjet.setFraisTransformation(40_000.32154);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(0.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(2_400.02);

                        Assertions.assertEquals(92_400.34, mockedProjet.calculTotalProjetAchat());
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(40_000.123);
                        mockedProjet.setFraisNotaireAchat(10_000.123);
                        mockedProjet.setRevenuCadastral(350);
                        mockedProjet.setFraisTransformation(37_456.123);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(0.01);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(2_247.37);

                        Assertions.assertEquals(89_703.74, mockedProjet.calculTotalProjetAchat());
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(-40_000.123);
                        mockedProjet.setFraisNotaireAchat(-10_034.123);
                        mockedProjet.setRevenuCadastral(-343);
                        mockedProjet.setFraisTransformation(-37_455.123);

                        Mockito.when(mockedProjet.calculAbattement()).thenThrow(Exception.class);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenThrow(Exception.class);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenThrow(Exception.class);

                        Assertions.assertThrows(Exception.class, mockedProjet::calculTotalProjetAchat);
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
                        mockedProjet.setPrixHabitation(100_000);
                        mockedProjet.setRevenuCadastral(1345);
                        mockedProjet.setFraisTransformation(1000);
                        mockedProjet.setFraisNotaireAchat(5000);

                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(60.00);
                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(7_500.00);

                        Assertions.assertEquals(22_606.00, mockedProjet.calculApportMinimal());

                    },
                    () -> {
                        mockedProjet.setPrixHabitation(100_000);
                        mockedProjet.setFraisNotaireAchat(28000);
                        mockedProjet.setRevenuCadastral(1345);
                        mockedProjet.setFraisTransformation(0);

                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(0.00);
                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(7_500.00);

                        Assertions.assertEquals(45_500.00, mockedProjet.calculApportMinimal());

                    },
                    () -> {
                        mockedProjet.setPrixHabitation(150_000);
                        mockedProjet.setFraisNotaireAchat(12000);
                        mockedProjet.setRevenuCadastral(200);
                        mockedProjet.setFraisTransformation(1000);

                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(60.00);
                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(6_600.00);

                        Assertions.assertEquals(33_706.00, mockedProjet.calculApportMinimal());

                    },
                    () -> {
                        mockedProjet.setPrixHabitation(40000);
                        mockedProjet.setRevenuCadastral(350);
                        mockedProjet.setFraisTransformation(40000);
                        mockedProjet.setFraisNotaireAchat(10000);

                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(2_400.00);
                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(0.00);

                        Assertions.assertEquals(18_240.00, mockedProjet.calculApportMinimal());

                    },                    () -> {
                        mockedProjet.setPrixHabitation(40_000);
                        mockedProjet.setFraisNotaireAchat(10000);
                        mockedProjet.setRevenuCadastral(350);
                        mockedProjet.setFraisTransformation(37456);

                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(2_247.36);
                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(0.00);

                        Assertions.assertEquals(17_970.34, mockedProjet.calculApportMinimal());

                    },
                    () -> {
                        mockedProjet.setPrixHabitation(400_000);
                        mockedProjet.setFraisNotaireAchat(10034);
                        mockedProjet.setRevenuCadastral(343);
                        mockedProjet.setFraisTransformation(37455);

                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(2_247.30);
                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(33_333.33);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(21_999.99);

                        Assertions.assertEquals(76_004.22, mockedProjet.calculApportMinimal());

                    },
                    () -> {
                        mockedProjet.setPrixHabitation(0);
                        mockedProjet.setFraisNotaireAchat(0);
                        mockedProjet.setRevenuCadastral(0);
                        mockedProjet.setFraisTransformation(0);

                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenThrow(Exception.class);

                        Assertions.assertThrows(Exception.class, mockedProjet::calculApportMinimal);

                    }
            );
        }

        @Test
        @DisplayName("Calcul de l'apport minimal : Validation valeur négative")
        public void calculApportMinimalNegatif() {
            Assertions.assertAll(
                    () -> {
                        mockedProjet.setPrixHabitation(-100_000);
                        mockedProjet.setFraisNotaireAchat(28_000);
                        mockedProjet.setRevenuCadastral(1345);
                        mockedProjet.setFraisTransformation(0);

                        Mockito.when(mockedProjet.calculAbattement()).thenThrow(Exception.class);

                        Assertions.assertThrows(Exception.class, mockedProjet::calculApportMinimal);
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(150_000);
                        mockedProjet.setFraisNotaireAchat(-12_000);
                        mockedProjet.setRevenuCadastral(200);
                        mockedProjet.setFraisTransformation(1_000);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(60.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(6_600.00);

                        Assertions.assertThrows(Exception.class, mockedProjet::calculApportMinimal);
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(40_000);
                        mockedProjet.setFraisNotaireAchat(10_000);
                        mockedProjet.setRevenuCadastral(-350);
                        mockedProjet.setFraisTransformation(40_000);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(2_400.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenThrow(Exception.class);

                        Assertions.assertThrows(Exception.class, mockedProjet::calculApportMinimal);
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(40_000);
                        mockedProjet.setFraisNotaireAchat(10_000);
                        mockedProjet.setRevenuCadastral(350);
                        mockedProjet.setFraisTransformation(-37_456);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenThrow(Exception.class);

                        Assertions.assertThrows(Exception.class, mockedProjet::calculApportMinimal);
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(-400_000);
                        mockedProjet.setFraisNotaireAchat(-10_034);
                        mockedProjet.setRevenuCadastral(-343);
                        mockedProjet.setFraisTransformation(-37_455);

                        Mockito.when(mockedProjet.calculAbattement()).thenThrow(Exception.class);

                        Assertions.assertThrows(Exception.class, mockedProjet::calculApportMinimal);
                    }
            );
        }

        @Test
        @DisplayName("Calcul de l'apport minimal : Validation nombre réel")
        public void calculApportMinimalNombreReel() {
            Assertions.assertAll(
                    () -> {
                        mockedProjet.setPrixHabitation(100_000.15);
                        mockedProjet.setFraisNotaireAchat(28_000);
                        mockedProjet.setRevenuCadastral(1345);
                        mockedProjet.setFraisTransformation(0);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(7_500.02);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(0.00);

                        Assertions.assertEquals(45_500.03, mockedProjet.calculApportMinimal());
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(150_000);
                        mockedProjet.setFraisNotaireAchat(12_000.90);
                        mockedProjet.setRevenuCadastral(200);
                        mockedProjet.setFraisTransformation(1_000);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(6_600.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(60.00);

                        Assertions.assertEquals(33_706.90, mockedProjet.calculApportMinimal());
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(40_000);
                        mockedProjet.setFraisNotaireAchat(10_000);
                        mockedProjet.setRevenuCadastral(350);
                        mockedProjet.setFraisTransformation(40_000.32154);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(0.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(2_400.02);

                        Assertions.assertEquals(18_240.03, mockedProjet.calculApportMinimal());
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(40_000.123);
                        mockedProjet.setFraisNotaireAchat(10_000.123);
                        mockedProjet.setRevenuCadastral(350);
                        mockedProjet.setFraisTransformation(37_456.123);

                        Mockito.when(mockedProjet.calculAbattement()).thenReturn(40_000.00);
                        Mockito.when(mockedProjet.calculDroitEnregistrement()).thenReturn(0.00);
                        Mockito.when(mockedProjet.calculTVAFraisTransformation()).thenReturn(2_247.37);

                        Assertions.assertEquals(17_970.49, mockedProjet.calculApportMinimal());
                    },
                    () -> {
                        mockedProjet.setPrixHabitation(-400_000.123);
                        mockedProjet.setFraisNotaireAchat(-10_034.123);
                        mockedProjet.setRevenuCadastral(-343);
                        mockedProjet.setFraisTransformation(-37_455.123);

                        Mockito.when(mockedProjet.calculAbattement()).thenThrow(Exception.class);

                        Assertions.assertThrows(Exception.class, mockedProjet::calculApportMinimal);
                    }
            );
        }

    }
}