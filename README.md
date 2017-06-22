# ChatClient

Drugi projekt pri Programiranju 2 je izdelava preprostega programa za spletni klepet v programskem jeziku Java.

## Kako program deluje:
Da se bomo z ostalimi uporabniki pogovarjali, se moramo najprej prijaviti z uporabniškim imenom, ki še ni zasedeno. Takoj, ko se z izbranim imenom prijavimo, lahko v spodnje vnosno polje vpišemo željeno besedilo in ga s pritiskom na tipko Enter ali gumb Pošlji pošljemo ostalim aktivnim uporabnikom (uporabniki so označeni kot aktivni, če so bili aktivni v roku zadnjih 5 minut). Če želimo nekomu izmed prijavljenih uporabnikov postali zasebno sporočilo, kliknemo na njegovo ime na seznamu in odprlo se nam bo okno z zasebnimi sporočili s tem uporabnikom (zraven vnosnega polja pa se bo izpisalo "Zasebno"). Če Želimo zopet odpreti okno z javnimi sporočili, kliknemo na gumb, na katerem piše "Javna sporočila" (gumb pod seznamom prijavljenih uporabnikov). Pred imenom pošiljatelja piše tudi ura, ko je bilo sporočilo poslano. Če želimo, da se naše sporočilo izpiše z določeno barvo, na koncu dodamo /r, /y, /g ali /b.

## Plan dela:
* Zasnova ideje
* Vaje v Javi in protokolu HTTP (uporaba Maven za pridobitev knjižnjic)
* Git repozitorij z README in LICENSE
* Izdelava načrta dela
* Izdelava GUI
  * Polje za vnos uporabniškega imena
  * Gumba za prijavo in odjavo
  * Glavno polje z vsemi sporočili
  * Seznam prijavljenih uporabnikov
  * Polje za vnos sporočila
* Pretvarjanje v in iz formata JSON
* Komunikacija s strežnikom (pošiljanje in sprejemanje informacij)
* Testiranje in odprava napak
* Dokumentiranje kode
* Poliranje GUI
* Čiščenje kode (imena funkcij, spremenljivk, brisanje nekoristnih delov)
* Oddaja projekta
