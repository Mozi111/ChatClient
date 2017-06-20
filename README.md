# ChatClient

Drugi projekt pri Programiranju 2 je izdelava preprostega programa za spletni klepet v programskem jeziku Java.

## Kako program deluje:
Da se bomo z ostalimi uporabniki pogovarjali, se moramo najprej prijaviti z uporabniškim imenom, ki še ni zasedeno. Takoj, ko se z izbranim imenom prijavimo, lahko v spodnje vnosno polje vpišemo željeno besedilo in ga s pritiskom na tipko Enter pošljemo ostalim aktivnim uporabnikom (uporabniki so označeni kot aktivni, če so bili aktivni v roku zadnjih 5 minut). Če želimo nekomu izmed prijavljenih uporabnikov postali zasebno sporočilo, kliknemo na njegovo ime na seznamu in v spodnjem gumbu se bo izpisal kot prejemnik. Če želimo sporočilo zopet nazaj spremeniti na javno, kliknemo na gumb, na katerem piše prejemnik (gumb pod seznamom prijavljenih uporabnikov). Ko bomo sporočilo poslali, bomo poleg sporočila videli, ali je bilo objavljeno vsem (javno) ali pa je bilo poslano le izbranemu uporabniku (za: ime uporabnika). Pred imenom pošiljatelja pa piše tudi ura, ko je bilo sporočilo poslano.

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
