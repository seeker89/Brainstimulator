Le projet ENSIMAG, 2012-2013

Sergio Vasquez, Mikolaj Pawlikowski


Les fonctionnalites de l'application:

	* consulter les videos (par categorie, les favorits, mes propres videos),
	* ajouter les commentaires (en ajax),
	* voter (ajax),
	* ajouter aux favorits (ajax),
	
	* creer les series (un groupement des videos),
	* ajouter les videos dans les categories et dans les series
	
	* webservice:
		- geolocalisation - accessible dans Account, button a droite "geolocalize me",
		- gravatar - si l'utilisateur remplit un mail qu'il a autilise pour creer son gravatar, il sera affiche dans Account
		 (partie interessante - on calcule les hash md5 via javascript - bilbio open source)
		 (vous pouvez le tester en vous login avec mikolaj@pawlikowski.pl/test)
		
	* admin (ecrit en GWT, sources dans BrainStimulatorAdmin_ADMIN):
		- enlever les utilisateurs (en cascade avec leurs creations),
		- enlever les videos
	
	* flux rss
	* version mobile d'appli via Bootstrap (ne marche pas bien avec les videos, qui ont une taille prédefinie)
	
	
Les roles:

	* guest (pas login) - peut consulter les videos,
	* user (login) - peut ajouter les videos, mettre en favorits, voter, etc,
	* admin - est un user, et en plus peut supprimer les autres users, et les videos



Quelques donnees par defaut sont remplis dans Global.java. En particulier:
	* un compte admin (admin@mail.com/admin), qui permet d'acceder au panel admin (/admin),
	* quelques videos, une serie, commentaires


Configuration:
	* partie admin écrite en GWT,
	* partie ajax écrite en Coffee, en utilisant JQuery,
	* pour le css = Twitter Bootstrap,
	
	* la bd - configure pour mysql sur localhost (brainsimulator: root/root)
