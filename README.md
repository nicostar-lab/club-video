Un club de location de cassettes vidéo vous demande l’analyse-conception de son système 
d’information. On se limitera dans cette pré-étude à l’enregistrement des locations de cassette, 
et à celui des retours. 
Actuellement, il existe : 
 un fichier CASSETTE en séquentiel indexé, avec : n° cassette, date-achat, titre, 
auteur, durée, prix, catégorie, libellé-catégorie ; 
 un fichier ABONNE en séquentiel indexé, avec : n° abonné, nom-abonné, 
adresseabonné, date-abonnement, date entrée, nombre-location (à un moment donné 
un abonné ne peut pas avoir plus de 3 cassettes) ; 
 un fichier LOCATION, avec : n° abonné, n° cassette, date-location ; 
 un fichier de saisie des retours avec n° abonné, nom-abonné ; 
 une carte d’abonné (plastifiée) avec n°abonné, nom-abonné, adresse-abonné, 
dateabonnement, date-entrée. 
Un abonné peut louer plusieurs cassettes, et plusieurs fois la même cassette, on ne garde que 
la dernière date de location pour un abonné et une cassette donnée. 
Pour un même titre, il existe plusieurs cassettes. Chaque titre appartient à une seule catégorie. 
Expression des besoins
Après avoir modélisé ce SI avec MERISE ou UML, proposez une base de données et une 
interface graphique permettant la gestion de ce club de cassette.