package org.example;


public class Main {
    public static void main(String[] args) throws Exception {

        Projet mockedProjet = new Projet();

        mockedProjet.setPrixHabitation(100_000);
        mockedProjet.setFraisNotaireAchat(28000);
        mockedProjet.setRevenuCadastral(1345);
        mockedProjet.setFraisTransformation(0);

        System.out.println("1");
        System.out.println(String.format("%.2f", mockedProjet.calculAbattement()) + " Abattement");
        System.out.println(String.format("%.2f", mockedProjet.calculDroitEnregistrement())  + " Droit");
        System.out.println(String.format("%.2f", mockedProjet.calculTVAFraisTransformation()) + " TVAFrais");
        System.out.println(String.format("%.2f", mockedProjet.calculApportMinimal()));

    }
}